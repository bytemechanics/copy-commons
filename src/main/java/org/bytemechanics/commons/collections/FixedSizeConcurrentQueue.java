package org.bytemechanics.commons.collections;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author afarre
 * @param <T>
 */
public class FixedSizeConcurrentQueue<T> extends ConcurrentLinkedQueue<T>{

	private final AtomicInteger currentSize;
	private final int maxSize;
	
	
	public FixedSizeConcurrentQueue(final int _maxSize){
		this.maxSize=_maxSize;
		this.currentSize=new AtomicInteger(0);
	}

	
	@Override
	public boolean addAll(final Collection<? extends T> _c) {
		
		boolean reply=false;
		
		reply = _c.stream()
				.map(value -> super.add(value))
				.reduce(reply, (accumulator, inserted) -> accumulator | inserted);
		
		return reply;
	}

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
}
