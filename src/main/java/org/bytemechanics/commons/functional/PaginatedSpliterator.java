package org.bytemechanics.commons.functional;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Spliterator implementation that populates new data to the stream by calling recurrently a provided page supplier. Usage:
 * <code>
 *		Spliterator<MyType> spliterator=new PaginatedSupplierSpliterator<>(myLastType -> myTypePageSupplier(),estimatedAmount);
 *		Stream<MyType> autoPopulatedStream=StreamSupport.stream(spliterator,true);
 * </code>
 * This spliterator return the fields ORDERED, return NONNULL values, is IMMUTABLE and CONCURRENT compatible but NOT SIZED
 * Very important: the supplier must return an ordered results otherwise can not ensure to not return the same value more than once
 * @author afarre
 * @param <T> supplier type
 */
public class PaginatedSpliterator<T> implements Spliterator<T>{

	public static final long DEFAULT_ESTIMATED_TOTAL=Long.MAX_VALUE;
	
	private final long estimatedTotal;
	private final Function<T,List<T>> pageSupplier;
	protected Deque<T> buffer;
	protected T from;
	
	/**
	 * Constructor receiving the _pageSupplier using DEFAULT_ESTIMATED_TOTAL as estimated total.
	 * @param _pageSupplier page supplier (must return an ordered results otherwise can not ensure to not return the same value more than once)
	 */
	public PaginatedSpliterator(final Function<T,List<T>> _pageSupplier){
		this(_pageSupplier,DEFAULT_ESTIMATED_TOTAL);
	}
	/**
	 * Constructor receiving the _pageSupplier using _total as estimated total.
	 * @param _pageSupplier page supplier (must return an ordered results otherwise can not ensure to not return the same value more than once)
	 * @param _total estimated total
	 */
	public PaginatedSpliterator(final Function<T,List<T>> _pageSupplier,final long _total){
		this.pageSupplier=_pageSupplier;
		this.estimatedTotal=_total;
		this.buffer=null;
		this.from=null;
	}

	/**
	 * Return the current buffer from the last page supplied
	 * @return Queue with the buffer content
	 */
	protected Queue<T> getBuffer(){
		
		if(this.buffer==null){
			this.buffer=new ArrayDeque<>();
			Stream.of(this.from)
					.sequential()
					.map(pageSupplier::apply)
					.filter(Objects::nonNull)
					.allMatch(this.buffer::addAll);
		}else if(this.buffer.isEmpty()){
			Stream.of(this.from)
					.sequential()
					.filter(Objects::nonNull)
					.map(pageSupplier::apply)
					.filter(Objects::nonNull)
					.allMatch(this.buffer::addAll);
		}
		this.from=Optional.of(this.buffer)
								.map(Deque::peekLast)
								.filter(Objects::nonNull)
								.orElse(null);
		
		return this.buffer;
	}
	
	/**
	 * @see Spliterator#tryAdvance(java.util.function.Consumer) 
	 */
	@Override
	public boolean tryAdvance(Consumer<? super T> _action) {
		
		final T val=getBuffer().poll();

		if(val!=null){
			_action.accept(val);
			return true;
		}
		
		return false;
	}

	/**
	 * @see Spliterator#trySplit()
	 */
	@Override
	public Spliterator<T> trySplit() {
		
		final Spliterator<T> reply;
		
		final Queue<T> currentBuffer=getBuffer();
		if(currentBuffer.isEmpty()){
			reply=null;
		}else{
			reply=currentBuffer.spliterator();
			this.buffer=new ArrayDeque<>();
		}
		
		return reply;
	}

	/**
	 * @see Spliterator#estimateSize()
	 */
	@Override
	public long estimateSize() {
		return this.estimatedTotal;
	}

	/**
	 * The spliterator characteristics by definition were: ORDERED, NONNULL, IMMUTABLE and CONCURRENT
	 * @see Spliterator#characteristics()
	 */
	@Override
	public int characteristics() {
		return Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.IMMUTABLE | Spliterator.CONCURRENT;
	}
}
