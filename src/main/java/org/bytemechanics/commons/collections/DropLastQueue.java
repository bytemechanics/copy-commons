package org.bytemechanics.commons.collections;

import java.lang.reflect.Array;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Synchronized queue that keeps the same size whatever adds done by droping the extra elements from the head
 * This implementation is not fast but never overflow the initial size
 * @author afarre
 * @param <T>
 */
public class DropLastQueue<T> {

	/**
	 * Queue node
	 * @param <T> node type 
	 */
	private static class Node<T>{
		
		private final T content;
		private Node<T> child;

		public Node(final T _content){
			this.content=_content;
			this.child=null;
		}

		private Node<T> secureFindChild(final int _childNumber){
			
			Node<T> reply=this;
			
			for(int ic1=0;ic1<_childNumber;ic1++){
				if(reply!=null){
					reply=reply.child;
				}
			}
			
			return reply;
		}

		public T getContent() {
			return content;
		}
		public Node<T> getChild() {
			return child;
		}

		public Node<T> purge(){
			
			final Node<T> reply=this.child;

			this.child=null;

			return reply;
		}
		public Node<T> findChild(final int _childNumber){
			return Optional.of(_childNumber)
						.map(childNumber -> childNumber-1)
						.filter(childNumber -> childNumber>0)
						.map(this::secureFindChild)
						.orElse(this);
		}
		
		public Spliterator<T> spliterator(final Node<T> _limit){
			
			final Deque<T> childs=new ArrayDeque<>();
			Node<T> current=this;
				
			while(current!=null){
				childs.addFirst(current.content);
				if(current!=_limit){
					current=current.child;
				}else{
					current=null;
				}
			}
			
			return childs.spliterator();
		}
		public Stream<T> stream(final Node<T> _limit){
			return StreamSupport.stream(spliterator(_limit), false);
		}
		public Spliterator<T> reverseSpliterator(final Node<T> _limit){
			
			final Node<T> startingPoint=this;
			final Node<T> endingPoint=_limit;
			
			return new Spliterator<T>(){

				private Node<T> current=startingPoint;
				
				@Override
				public boolean tryAdvance(Consumer<? super T> action) {
					action.accept(this.current.content);
					if((this.current!=endingPoint)&&(this.current.child!=null)){
						this.current=this.current.child;
						return true;
					}else{
						return false;
					}
				}
				@Override
				public Spliterator<T> trySplit() {
					return null;
				}
				@Override
				public long estimateSize() {
					return 0;
				}
				@Override
				public int characteristics() {
					return 0;
				}
			};
		}
		public Stream<T> reverseStream(final Node<T> _limit){
			return StreamSupport.stream(reverseSpliterator(_limit), false);
		}
		
		@Override
		public int hashCode() {
			int hash = 5;
			hash = 59 * hash + Objects.hashCode(this.content);
			hash = 59 * hash + Objects.hashCode(this.child);
			return hash;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final Node<?> other = (Node<?>) obj;
			if (!Objects.equals(this.content, other.content)) {
				return false;
			}
			return Objects.equals(this.child, other.child);
		}
	}
	/**
	 * Read only pointers to nodes with size control
	 * This queue can when accumulate is called can left some old nodes still pointed by the oldest node,
	 * those should be purged by parent class
	 * @param <T> queue typ
	 */
	private static class ReadOnlyQueue<T> {

		protected final int maxSize;
		protected final int size;
		
		protected final Node<T> youngest;
		protected final Node<T> oldest;
		
		public ReadOnlyQueue(final int _maxSize) {
			this(_maxSize,0,null,null);
		}
		public ReadOnlyQueue(final int _maxSize,final int _size,final Node<T> _youngest,final Node<T> _oldest) {

			this.maxSize=_maxSize;
			this.size=Optional.of(_size)
								.filter(val -> val>-1)
								.map(val -> (val>this.maxSize)? this.maxSize : val)
								.orElse(0);
			this.youngest=_youngest;
			this.oldest=_oldest;
		}
		
		@SuppressWarnings("null")
		public static <B> ReadOnlyQueue<B> of(final int _maxSize,final Stream<B> _values) {

			final Iterator<B> iterator=Spliterators.iterator(_values.spliterator());
			Node<B> youngest=null;
			Node<B> oldest=null;
			int size=0;
			
			while(iterator.hasNext()){
				final B val=iterator.next();
				final Node<B> newNode=new Node<>(val);
				if(oldest==null){
					oldest=newNode;
				}else{
					newNode.child=youngest;
				}
				youngest=newNode;
				size++;
			}
			if(size>_maxSize){
				youngest.findChild(_maxSize).child=null;
				size=_maxSize;
			}

			return new ReadOnlyQueue<>(_maxSize,size,youngest,oldest);
		}
				
		public static <B> ReadOnlyQueue<B> accumulate(final ReadOnlyQueue<B> _preview,final ReadOnlyQueue<B> _next){
			
			if(_next.size==_preview.maxSize){
				return _next;
			}
			//_next.size is less than reply.maxSize
			if(_preview.size+_next.size<=_preview.maxSize){
				_next.oldest.child=_preview.youngest;
				return new ReadOnlyQueue<>(_preview.maxSize,_preview.size+_next.size,_next.youngest,(_preview.oldest==null)? _next.oldest : _preview.oldest);
			}				
			_next.oldest.child=_preview.youngest;
			final Node<B> nextOldest=_preview.youngest.findChild(_preview.maxSize-_next.size);
			return new ReadOnlyQueue<>(_preview.maxSize,_preview.maxSize,_next.youngest,nextOldest);
		}
		public static <B> ReadOnlyQueue<B> reduce(final ReadOnlyQueue<B> _preview,final ReadOnlyQueue<B> _next){
			
			if(_next.size==0){
				return _preview;
			}
			if(_next.size>=_preview.size){
				return new ReadOnlyQueue<>(_preview.maxSize);
			}
			final Node<B> nextOldest=_preview.youngest.findChild(_preview.size-_next.size);
			return new ReadOnlyQueue<>(_preview.maxSize,_preview.size-_next.size,_preview.youngest,nextOldest);
		}

		public Stream<T> purge(){
			
			return Optional.ofNullable(this.oldest)
								.map(Node::getChild)
								.map(Node::purge)
								.map(node -> node.stream(null))
								.orElseGet(Stream::empty);
		}
		public Stream<T> peek(final int _number){
			
			if(_number==1){
				return Optional.ofNullable(this.oldest)
									.map(Node::getContent)
									.map(Stream::of)
									.orElseGet(Stream::empty);
			}
			return Optional.ofNullable(this.youngest)
								.map(node -> node.findChild((_number>this.size)? 0 : 1+this.size-_number))
								.map(node -> node.stream(this.oldest))
								.orElseGet(Stream::empty);
		}
		public Stream<T> peekAll(){
			return peek(this.size);
		}
		
		public Spliterator<T> spliterator(){
			return Optional.ofNullable(this.youngest)
								.map(node -> node.spliterator(this.oldest))
								.orElseGet(Spliterators::emptySpliterator);
		}
		public Spliterator<T> reverseSpliterator(){
			return Optional.ofNullable(this.youngest)
								.map(node -> node.reverseSpliterator(this.oldest))
								.orElseGet(Spliterators::emptySpliterator);
		}
		public Stream<T> stream(){
			return Optional.ofNullable(this.youngest)
								.map(node -> node.stream(this.oldest))
								.orElseGet(Stream::empty);
		}
		public Stream<T> reverseStream(){
			return Optional.ofNullable(this.youngest)
								.map(node -> node.reverseStream(this.oldest))
								.orElseGet(Stream::empty);
		}

		@Override
		public int hashCode() {
			
			int hash = 3;
			
			hash = 17 * hash + this.maxSize;
			hash = 17 * hash + this.size;
			hash = 17 * hash + Objects.hashCode(this.youngest);
			hash = 17 * hash + Objects.hashCode(this.oldest);
			
			return hash;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			final ReadOnlyQueue<?> other = (ReadOnlyQueue<?>) obj;
			if (this.maxSize != other.maxSize) {
				return false;
			}
			if (this.size != other.size) {
				return false;
			}
			if (!Objects.equals(this.youngest, other.youngest)) {
				return false;
			}
			return Objects.equals(this.oldest, other.oldest);
		}
	}
	
	/** queue max size */
	private final int maxSize;
	/** read only queue structure */
	private final AtomicReference<ReadOnlyQueue<T>> readOnlyQueue;
	
	/**
	 * Builds a new DropLastQueue from the giving _maxSize
	 * @param _maxSize max size allowed for this queue
	 */
	public DropLastQueue(final int _maxSize){
		if(_maxSize<1){
			throw new IllegalArgumentException("Max size can't be zero nor negative");
		}
		this.maxSize=_maxSize;
		this.readOnlyQueue=new AtomicReference<>(new ReadOnlyQueue<>(this.maxSize));
	}

	/**
	 * Recovers the maximum allowed size for this queue
	 * @return queue max size
	 */
	public int maxSize(){
		return this.maxSize;
	}

	/**
	 * Recovers the current queue size
	 * @return queue current size
	 */
	public int size(){
		return this.readOnlyQueue.get().size;
	}

	/**
	 * Returns true if queue is empty
	 * @return true if queue is empty
	 */
	public boolean isEmpty() {
		return this.readOnlyQueue.get().size==0;
	}

	/**
	 * Pushes all values from the stream returning a stream of droped values if has been necessary
	 * Note that returned stream not allways return all droped values
	 * @param _value stream of values to add to queue
	 * @return stream of droped values or empty stream if no drop has been necessary
	 */
	public Stream<T> push(final Stream<T> _value){

		return Optional.ofNullable(_value)
							.map(value -> ReadOnlyQueue.of(this.maxSize, value))
							.filter(queue -> queue.size>0)
							.map(queue -> this.readOnlyQueue.accumulateAndGet(queue, ReadOnlyQueue::accumulate))
							.map(ReadOnlyQueue::purge)
							.orElseGet(Stream::empty);
	}
	/**
	 * Pushes all values from the collection returning a stream of droped values if has been necessary
	 * Note that returned stream not allways return all droped values
	 * @param _collection collection of elements to add to stream
	 * @return stream of droped values or empty stream if no drop has been necessary
	 */
	public Stream<T> push(final Collection<T> _collection){

		return Optional.ofNullable(_collection)
							.filter(collection -> collection.size()>0)
							.map(Collection::stream)
							.map(stream -> stream.filter(val -> val!=null))
							.map(stream -> ReadOnlyQueue.of(this.maxSize, stream))
							.filter(queue -> queue.size>0)
							.map(queue -> this.readOnlyQueue.accumulateAndGet(queue, ReadOnlyQueue::accumulate))
							.map(ReadOnlyQueue::purge)
							.orElseGet(Stream::empty);
	}
	/**
	 * Pushes all values from the array returning a stream of droped values if has been necessary
	 * Note that returned stream not allways return all droped values
	 * @param _array array of elements to add to stream
	 * @return stream of droped values or empty stream if no drop has been necessary
	 */
	public Stream<T> push(final T... _array){

		return Optional.ofNullable(_array)
							.filter(array -> array.length>0)
							.map(Stream::of)
							.map(stream -> stream.filter(val -> val!=null))
							.map(stream -> ReadOnlyQueue.of(this.maxSize, stream))
							.filter(queue -> queue.size>0)
							.map(queue -> this.readOnlyQueue.accumulateAndGet(queue, ReadOnlyQueue::accumulate))
							.map(ReadOnlyQueue::purge)
							.orElseGet(Stream::empty);
	}

	/**
	 * Returns the first element of the queue without pulling out
	 * @return Optional with the first element of the queue or empty optional if queue is empty
	 */
	public Optional<T> peek(){
		return peek(1)
				.findFirst();
	}
	/**
	 * Returns _number first elements of the queue without pulling out
	 * @param _number number of elements to peek
	 * @return Stream with the _number first elements of the queue or empty stream if queue is empty
	 */
	public Stream<T> peek(final int _number){
		return Optional.of(_number)
						.filter(num -> num>0)
						.map(num -> this.readOnlyQueue.get())
						.map(queue -> queue.peek(_number))
						.orElseGet(Stream::empty);
	}
		
	/**
	 * Poll out the first element of the queue
	 * @return Optional with the first element of the queue or empty optional if queue is empty
	 */
	public Optional<T> poll(){
		return poll(1)
				.findFirst();
	}
	/**
	 * Poll out _number first elements of the queue
	 * @param _number number of elements to poll
	 * @return Stream with the _number first elements of the queue or empty stream if queue is empty
	 */
	public Stream<T> poll(final int _number){
		return Optional.of(_number)
							.filter(num -> num>0)
							.map(number -> new ReadOnlyQueue<T>(this.maxSize,number,null,null))
							.map(reducerQueue -> this.readOnlyQueue.getAndUpdate(queue -> ReadOnlyQueue.reduce(queue, reducerQueue)))
							.map(queue -> queue.peek(_number))
							.orElseGet(Stream::empty);
	}

	/**
	 * Removes _number first elements of the queue
	 * @param _number number of elements to remove
	 * @return Stream with the _number first elements of the queue or empty stream if queue is empty
	 */
	public Stream<T> remove(final int _number){

		return Optional.of(_number)
							.filter(num -> num>0)
							.map(number -> new ReadOnlyQueue<T>(this.maxSize,number,null,null))
							.map(reducerQueue -> this.readOnlyQueue.accumulateAndGet(reducerQueue, ReadOnlyQueue::reduce))
							.map(ReadOnlyQueue::purge)
							.orElseGet(Stream::empty);
	}

	/**
	 * Clear out the queue and returns an stream with the elements removed
	 * @return Stream of elements removed or empty stream
	 */
	public Stream<T> clear(){
		return this.readOnlyQueue
					.getAndSet(new ReadOnlyQueue<>(this.maxSize))
						.stream();
	}
	
	/**
	 * Return an object array representing all queued elements
	 * @return array of object elemments in stream
	 */
	public Object[] toArray(){
		return this.readOnlyQueue
					.get()
						.stream()
							.toArray();
	}
	/**
	 * Return an _class array representing all queued elements
	 * @param _class  class of array elements
	 * @return array of _class elemments in stream
	 */
	public T[] toArray(final Class<T> _class){
		return Optional.ofNullable(_class)
							.map(arr -> this.readOnlyQueue
												.get()
													.stream()
														.collect(Collectors.toList()))
							.filter(collection -> !collection.isEmpty())
							.map(collection -> collection.toArray((T[])Array.newInstance(_class,0)))
							.orElse((T[])Array.newInstance(_class,0));
	}
	/**
	 * Return a list representing all queued elements
	 * @return list of elemments in stream
	 */
	public List<T> toList(){
		return this.readOnlyQueue
					.get()
						.stream()
							.collect(Collectors.toList());
	}
	/**
	 * Return the queue spliterator in natural order
	 * Due implementation this method recovers all elements to reverse order
	 * @return spliterator with the elements in this queue
	 */
	public Spliterator<T> spliterator(){
		return this.readOnlyQueue.get().spliterator();
	}
	/**
	 * Return the queue spliterator in reverse order
	 * Fastest spliterator from this queue
	 * @return spliterator with the elements in this queue in reverse order
	 */
	public Spliterator<T> reverseSpliterator(){
		return this.readOnlyQueue.get().reverseSpliterator();
	}
	/**
	 * Return the queue stream in natural order
	 * Due implementation this method recovers all elements to reverse order
	 * @return stream with the elements in this queue
	 */
	public Stream<T> stream(){
		return this.readOnlyQueue.get().stream();
	}
	/**
	 * Return the queue stream in reverse order
	 * Fastest stream from this queue
	 * @return stream with the elements in this queue in reverse order
	 */
	public Stream<T> reverseStream(){
		return this.readOnlyQueue.get().reverseStream();
	}

	/**@see Object#hashCode() */
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 83 * hash + this.maxSize;
		hash = 83 * hash + Objects.hashCode(this.readOnlyQueue);
		return hash;
	}

	/**@see Object#equals(java.lang.Object)  */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DropLastQueue<?> other = (DropLastQueue<?>) obj;
		if (this.maxSize != other.maxSize) {
			return false;
		}
		return Objects.equals(this.readOnlyQueue, other.readOnlyQueue);
	}
}
