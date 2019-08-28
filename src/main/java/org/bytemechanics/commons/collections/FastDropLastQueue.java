package org.bytemechanics.commons.collections;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Concurrent queue that keeps the same size whatever adds done by droping the extra elements from the head
 * This implementation is very fast but can overflow the initial size
 * @author afarre
 * @param <T>
  */
public class FastDropLastQueue<T> extends ConcurrentLinkedQueue<T>{

	private final AtomicInteger currentSize;
	private final int maxSize;
	

	/**
	 * Builds a new DropLastQueue from the giving _maxSize
	 * @param _maxSize max size allowed for this queue
	 */
	public FastDropLastQueue(final int _maxSize){
		this.maxSize=_maxSize;
		this.currentSize=new AtomicInteger(0);
	}

	
	/** @see Queue#addAll(java.util.Collection) */
	@Override
	public boolean addAll(final Collection<? extends T> _c) {
		
		boolean reply=false;
		
		reply = _c.stream()
					.map(value -> offer(value))
					.reduce(reply, (accumulator, inserted) -> accumulator | inserted);
		
		return reply;
	}

	/** @see Queue#remove(java.lang.Object) */
	@Override
	public boolean remove(final Object _o) {
		
		final boolean reply=super.remove(_o);
		
		if(reply){
			if(this.currentSize.get()>0){
				this.currentSize.decrementAndGet();
			}
		}

		return reply;
	}

	/** @see Queue#poll()  */
	@Override
	public T poll() {
		
		final T reply=super.poll();
		
		if(reply!=null){
			if(this.currentSize.get()>0){
				this.currentSize.decrementAndGet();
			}
		}

		return reply;
	}

	/** @see Queue#offer(java.lang.Object) */
	@Override
	public boolean offer(final T _e) {
		
		final boolean reply=super.offer(_e);
		
		if(reply){
			if(this.currentSize.get()>=this.maxSize){
				super.poll();
			}else{
				this.currentSize.incrementAndGet();
			}
		}
		
		return reply;
	}

	@Override
	public void clear() {
		while(!super.isEmpty()){
			poll();
		}
	}

}
