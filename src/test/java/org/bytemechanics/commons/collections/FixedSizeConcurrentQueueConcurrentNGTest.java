/*
 * Copyright 2018 Byte Mechanics.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bytemechanics.commons.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author afarre
 */
public class FixedSizeConcurrentQueueConcurrentNGTest {

	protected void assertLessOrEqual(final int _actual,final int _expected){
	
		if(_actual>_expected){
			throw new ArrayIndexOutOfBoundsException("Expected less than "+_expected+" but found "+_actual);
		}
	}
	
	protected void userSimulation1(final int _task,final int queueSize,final Queue<Integer> concurrentQueue, final AtomicReference<Throwable> _exception){
		
		try{
			System.out.println("FixedSizeConcurrentQueueConcurrentNGTest >>>> test_concurrent1("+_task+") >>> thread "+Thread.currentThread().getId());
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.addAll(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20)));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.stream().count()>0);
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(5));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.stream().count()>0);
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.addAll(Arrays.asList(1,2,3,4,5,6,7,8,9)));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.addAll(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20)));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.stream().mapToInt(val -> val).sum()>0);
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.stream().mapToInt(val -> val).sum()>0);
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.stream().mapToInt(val -> val).sum()>0);
			assertLessOrEqual(concurrentQueue.size(),queueSize);
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("FixedSizeConcurrentQueueConcurrentNGTest >>>> test_concurrent("+_task+")  >>> thread "+Thread.currentThread().getId()+" >>>> WARNING: "+e.getMessage());
		}catch(Throwable e){
			System.out.println("FixedSizeConcurrentQueueConcurrentNGTest >>>> test_concurrent("+_task+")  >>> thread "+Thread.currentThread().getId()+" >>>> FAILURE: "+e.getMessage());
			_exception.set(e);
		}
	}
	protected void userSimulation2(final int _task,final int queueSize,final Queue<Integer> concurrentQueue,final AtomicReference<Throwable> _exception){
		
		try{
			System.out.println("FixedSizeConcurrentQueueConcurrentNGTest >>>> test_concurrent2("+_task+") >>> thread "+Thread.currentThread().getId());
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.addAll(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20)));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.addAll(Arrays.asList(1,2,3,4,5,6,7,8,9)));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.stream().count()>0);
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(5));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.stream().mapToInt(val -> val).sum()>0);
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.addAll(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20)));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.stream().mapToInt(val -> val).sum()>0);
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.stream().count()>0);
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.stream().mapToInt(val -> val).sum()>0);
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("FixedSizeConcurrentQueueConcurrentNGTest >>>> test_concurrent("+_task+")  >>> thread "+Thread.currentThread().getId()+" >>>> WARNING: "+e.getMessage());
		}catch(Throwable e){
			System.out.println("FixedSizeConcurrentQueueConcurrentNGTest >>>> test_concurrent("+_task+")  >>> thread "+Thread.currentThread().getId()+" >>>> FAILURE: "+e.getMessage());
			_exception.set(e);
		}
	}
	protected void userSimulation3(final int _task,final int queueSize,final Queue<Integer> concurrentQueue,final AtomicReference<Throwable> _exception){
		
		try{
			System.out.println("FixedSizeConcurrentQueueConcurrentNGTest >>>> test_concurrent3("+_task+") >>> thread "+Thread.currentThread().getId());
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.addAll(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20)));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.addAll(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20)));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.addAll(Arrays.asList(1,2,3,4,5,6,7,8,9)));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.stream().mapToInt(val -> val).sum()>0);
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.stream().mapToInt(val -> val).sum()>0);
			assertLessOrEqual(concurrentQueue.size(),queueSize);
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("FixedSizeConcurrentQueueConcurrentNGTest >>>> test_concurrent("+_task+")  >>> thread "+Thread.currentThread().getId()+" >>>> WARNING: "+e.getMessage());
		}catch(Throwable e){
			System.out.println("FixedSizeConcurrentQueueConcurrentNGTest >>>> test_concurrent("+_task+")  >>> thread "+Thread.currentThread().getId()+" >>>> FAILURE: "+e.getMessage());
			_exception.set(e);
		}
	}
	protected void userSimulation4(final int _task,final int queueSize,final Queue<Integer> concurrentQueue,final AtomicReference<Throwable> _exception){
		
		try{
			System.out.println("FixedSizeConcurrentQueueConcurrentNGTest >>>> test_concurrent4("+_task+") >>> thread "+Thread.currentThread().getId());
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.addAll(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20)));
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(5));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.addAll(Arrays.asList(1,2,3,4,5,6,7,8,9)));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.addAll(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20)));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.stream().mapToInt(val -> val).sum()>0);
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.stream().mapToInt(val -> val).sum()>0);
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.stream().mapToInt(val -> val).sum()>0);
			assertLessOrEqual(concurrentQueue.size(),queueSize);
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("FixedSizeConcurrentQueueConcurrentNGTest >>>> test_concurrent("+_task+")  >>> thread "+Thread.currentThread().getId()+" >>>> WARNING: "+e.getMessage());
		}catch(Throwable e){
			System.out.println("FixedSizeConcurrentQueueConcurrentNGTest >>>> test_concurrent("+_task+")  >>> thread "+Thread.currentThread().getId()+" >>>> FAILURE: "+e.getMessage());
			_exception.set(e);
		}
	}
	protected void userSimulation5(final int _task,final int queueSize,final Queue<Integer> concurrentQueue,final AtomicReference<Throwable> _exception){
		
		try{
			System.out.println("FixedSizeConcurrentQueueConcurrentNGTest >>>> test_concurrent5("+_task+") >>> thread "+Thread.currentThread().getId());
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.addAll(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20)));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			concurrentQueue.poll();
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.stream().mapToInt(val -> val).sum()>0);
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.offer(534));
			assertLessOrEqual(concurrentQueue.size(),queueSize);
			Assert.assertTrue(concurrentQueue.stream().mapToInt(val -> val).sum()>0);
			assertLessOrEqual(concurrentQueue.size(),queueSize);
		}catch(ArrayIndexOutOfBoundsException e){
			System.out.println("FixedSizeConcurrentQueueConcurrentNGTest >>>> test_concurrent("+_task+")  >>> thread "+Thread.currentThread().getId()+" >>>> WARNING: "+e.getMessage());
		}catch(Throwable e){
			System.out.println("FixedSizeConcurrentQueueConcurrentNGTest >>>> test_concurrent("+_task+")  >>> thread "+Thread.currentThread().getId()+" >>>> FAILURE: "+e.getMessage());
			_exception.set(e);
		}
	}
	
	@Test(testName ="concurrent" , singleThreaded = true, invocationCount = 10,successPercentage = 100)
	public void test_concurrent() throws InterruptedException, ExecutionException, Throwable {
		System.out.println("FixedSizeConcurrentQueueConcurrentNGTest >>>> test_concurrent");
		final int queueSize=30;
		final Queue<Integer> concurrentQueue=new FixedSizeConcurrentQueue<>(queueSize);
		System.out.println("FixedSizeConcurrentQueueConcurrentNGTest >>>> test_concurrent >>> ConcurrentQueue Created");
		final ForkJoinPool forkJoinPool =new ForkJoinPool(Runtime.getRuntime().availableProcessors());
		final Random random=new Random(System.currentTimeMillis());
		System.out.println("FixedSizeConcurrentQueueConcurrentNGTest >>>> test_concurrent >>> ThreadPool Created");
		final List<Runnable> tasks=new ArrayList<>(100);
		final AtomicReference<Throwable> exception=new AtomicReference<>(null);
		for(int ic1=0;ic1<100;ic1++){
			final int it1=ic1;
			switch(random.nextInt(5)){
				case 0: tasks.add(() -> userSimulation1(it1,queueSize,concurrentQueue,exception));
						break;
				case 1: tasks.add(() -> userSimulation2(it1,queueSize,concurrentQueue,exception));
						break;
				case 2: tasks.add(() -> userSimulation3(it1,queueSize,concurrentQueue,exception));
						break;
				case 3: tasks.add(() -> userSimulation4(it1,queueSize,concurrentQueue,exception));
						break;
				default:tasks.add(() -> userSimulation5(it1,queueSize,concurrentQueue,exception));
						break;
			}
		}
		System.out.println("FixedSizeConcurrentQueueConcurrentNGTest >>>> test_concurrent >>> Tasks Created");
		final ForkJoinTask task=forkJoinPool.submit(() -> {
																tasks.stream()
																		.parallel()
																		.forEach(forkJoinPool::execute);
															});
		forkJoinPool.awaitTermination(5, TimeUnit.MILLISECONDS);
		if(task.isCompletedAbnormally()){
			throw task.getException();
		}
		if(exception.get()!=null){
			System.out.println("FixedSizeConcurrentQueueConcurrentNGTest >>>> test_concurrent >>> exception found");
			throw exception.get();
		}
		assertLessOrEqual(concurrentQueue.size(),queueSize+2);
		System.out.println("FixedSizeConcurrentQueueConcurrentNGTest >>>> test_concurrent >>> Finished");
	}
}
