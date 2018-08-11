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
import java.util.Queue;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author afarre
 */
public class FixedSizeConcurrentQueueNGTest {

	private Queue<Integer> concurrentQueue;
	
	@BeforeMethod
	public void setUpMethod() throws Exception {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> setUpMethod");
		this.concurrentQueue=new FixedSizeConcurrentQueue<>(20);
	}

	@AfterMethod
	public void tearDownMethod() throws Exception {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> tearDownMethod");
		this.concurrentQueue=null;
	}

	@Test
	public void testAddAll() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> testAddAll");

		Assert.assertTrue(concurrentQueue.isEmpty());

		Assert.assertFalse(concurrentQueue.addAll(new ArrayList<>()));
		Assert.assertTrue(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),0);

		Assert.assertTrue(concurrentQueue.addAll(Arrays.asList(1,2,3,4,5,6,7,8,9,10)));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),10);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{1,2,3,4,5,6,7,8,9,10});

		Assert.assertTrue(concurrentQueue.addAll(Arrays.asList(11,12,13,14,15,16,17,18,19,20)));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),20);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20});

		Assert.assertTrue(concurrentQueue.addAll(Arrays.asList(31,32,33)));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),20);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,31,32,33});

		concurrentQueue.clear();
		Assert.assertTrue(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),0);
		
		Assert.assertTrue(concurrentQueue.addAll(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21)));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),20);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21});

		Assert.assertFalse(concurrentQueue.addAll(new ArrayList<>()));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),20);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21});

		Assert.assertTrue(concurrentQueue.addAll(Arrays.asList(50,51,52,53,54,56)));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),20);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{8,9,10,11,12,13,14,15,16,17,18,19,20,21,50,51,52,53,54,56});
	}

	@Test
	public void testRemove() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> testRemove");
		
		Assert.assertTrue(concurrentQueue.isEmpty());

		Assert.assertFalse(concurrentQueue.remove(null));
		Assert.assertTrue(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),0);

		Assert.assertFalse(concurrentQueue.remove(1));
		Assert.assertTrue(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),0);

		Assert.assertTrue(concurrentQueue.addAll(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20)));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertTrue(concurrentQueue.remove(10));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),19);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{1,2,3,4,5,6,7,8,9,11,12,13,14,15,16,17,18,19,20});
		Assert.assertTrue(concurrentQueue.addAll(Arrays.asList(10)));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),20);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{1,2,3,4,5,6,7,8,9,11,12,13,14,15,16,17,18,19,20,10});

		Assert.assertTrue(concurrentQueue.remove(1));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),19);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{2,3,4,5,6,7,8,9,11,12,13,14,15,16,17,18,19,20,10});
		Assert.assertTrue(concurrentQueue.addAll(Arrays.asList(40)));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),20);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{2,3,4,5,6,7,8,9,11,12,13,14,15,16,17,18,19,20,10,40});

		Assert.assertTrue(concurrentQueue.remove(40));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),19);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{2,3,4,5,6,7,8,9,11,12,13,14,15,16,17,18,19,20,10});
		Assert.assertTrue(concurrentQueue.addAll(Arrays.asList(50)));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),20);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{2,3,4,5,6,7,8,9,11,12,13,14,15,16,17,18,19,20,10,50});

		Assert.assertFalse(concurrentQueue.remove(40));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),20);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{2,3,4,5,6,7,8,9,11,12,13,14,15,16,17,18,19,20,10,50});
	}

	@Test
	public void testPoll() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> testPoll");
		
		Assert.assertTrue(concurrentQueue.isEmpty());

		Assert.assertNull(concurrentQueue.poll());
		Assert.assertTrue(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),0);

		Assert.assertTrue(concurrentQueue.addAll(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20)));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.poll(),(Integer)1);
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),19);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20});
		Assert.assertTrue(concurrentQueue.addAll(Arrays.asList(10)));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),20);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,10});

		Assert.assertEquals(concurrentQueue.poll(),(Integer)2);
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),19);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,10});
		Assert.assertEquals(concurrentQueue.poll(),(Integer)3);
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),18);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,10});
		Assert.assertEquals(concurrentQueue.poll(),(Integer)4);
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),17);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,10});
		Assert.assertEquals(concurrentQueue.poll(),(Integer)5);
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),16);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,10});
		Assert.assertEquals(concurrentQueue.poll(),(Integer)6);
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),15);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{7,8,9,10,11,12,13,14,15,16,17,18,19,20,10});
		Assert.assertEquals(concurrentQueue.poll(),(Integer)7);
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),14);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{8,9,10,11,12,13,14,15,16,17,18,19,20,10});
		Assert.assertEquals(concurrentQueue.poll(),(Integer)8);
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),13);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{9,10,11,12,13,14,15,16,17,18,19,20,10});
		Assert.assertEquals(concurrentQueue.poll(),(Integer)9);
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),12);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{10,11,12,13,14,15,16,17,18,19,20,10});
		Assert.assertEquals(concurrentQueue.poll(),(Integer)10);
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),11);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{11,12,13,14,15,16,17,18,19,20,10});
		Assert.assertEquals(concurrentQueue.poll(),(Integer)11);
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),10);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{12,13,14,15,16,17,18,19,20,10});
		Assert.assertEquals(concurrentQueue.poll(),(Integer)12);
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),9);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{13,14,15,16,17,18,19,20,10});
		Assert.assertEquals(concurrentQueue.poll(),(Integer)13);
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),8);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{14,15,16,17,18,19,20,10});
		Assert.assertEquals(concurrentQueue.poll(),(Integer)14);
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),7);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{15,16,17,18,19,20,10});
		Assert.assertEquals(concurrentQueue.poll(),(Integer)15);
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),6);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{16,17,18,19,20,10});
		Assert.assertEquals(concurrentQueue.poll(),(Integer)16);
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),5);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{17,18,19,20,10});
		Assert.assertEquals(concurrentQueue.poll(),(Integer)17);
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),4);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{18,19,20,10});
		Assert.assertEquals(concurrentQueue.poll(),(Integer)18);
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),3);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{19,20,10});
		Assert.assertEquals(concurrentQueue.poll(),(Integer)19);
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),2);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{20,10});
		Assert.assertEquals(concurrentQueue.poll(),(Integer)20);
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),1);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{10});
		Assert.assertEquals(concurrentQueue.poll(),(Integer)10);
		Assert.assertTrue(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),0);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{});
	}
	
	@Test(expectedExceptions = NullPointerException.class)
	public void testOffer_null() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> testOffer_null");
		Assert.assertTrue(concurrentQueue.isEmpty());

		Assert.assertFalse(concurrentQueue.offer(null));
	}

	@Test
	public void testOffer() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> testOffer");
		Assert.assertTrue(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),0);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{});

		Assert.assertTrue(concurrentQueue.offer(20));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),1);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{20});

		Assert.assertTrue(concurrentQueue.offer(30));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),2);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{20,30});

		Assert.assertTrue(concurrentQueue.offer(40));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),3);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{20,30,40});

		Assert.assertTrue(concurrentQueue.offer(50));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),4);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{20,30,40,50});

		Assert.assertTrue(concurrentQueue.offer(5));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),5);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{20,30,40,50,5});
		
		Assert.assertTrue(concurrentQueue.offer(17));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),6);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{20,30,40,50,5,17});

		Assert.assertTrue(concurrentQueue.offer(5));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),7);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{20,30,40,50,5,17,5});

		Assert.assertTrue(concurrentQueue.offer(8));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),8);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{20,30,40,50,5,17,5,8});

		Assert.assertTrue(concurrentQueue.offer(10));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),9);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{20,30,40,50,5,17,5,8,10});

		Assert.assertTrue(concurrentQueue.offer(7));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),10);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{20,30,40,50,5,17,5,8,10,7});

		Assert.assertTrue(concurrentQueue.offer(32));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),11);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{20,30,40,50,5,17,5,8,10,7,32});

		Assert.assertTrue(concurrentQueue.offer(432432));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),12);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{20,30,40,50,5,17,5,8,10,7,32,432432});

		Assert.assertTrue(concurrentQueue.offer(423));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),13);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{20,30,40,50,5,17,5,8,10,7,32,432432,423});

		Assert.assertTrue(concurrentQueue.offer(534));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),14);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{20,30,40,50,5,17,5,8,10,7,32,432432,423,534});

		Assert.assertTrue(concurrentQueue.offer(534));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),15);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{20,30,40,50,5,17,5,8,10,7,32,432432,423,534,534});

		Assert.assertTrue(concurrentQueue.offer(343));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),16);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{20,30,40,50,5,17,5,8,10,7,32,432432,423,534,534,343});

		Assert.assertTrue(concurrentQueue.offer(5434));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),17);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{20,30,40,50,5,17,5,8,10,7,32,432432,423,534,534,343,5434});

		Assert.assertTrue(concurrentQueue.offer(54343));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),18);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{20,30,40,50,5,17,5,8,10,7,32,432432,423,534,534,343,5434,54343});

		Assert.assertTrue(concurrentQueue.offer(54344));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),19);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{20,30,40,50,5,17,5,8,10,7,32,432432,423,534,534,343,5434,54343,54344});

		Assert.assertTrue(concurrentQueue.offer(54345));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),20);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{20,30,40,50,5,17,5,8,10,7,32,432432,423,534,534,343,5434,54343,54344,54345});

		Assert.assertTrue(concurrentQueue.offer(100));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),20);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{30,40,50,5,17,5,8,10,7,32,432432,423,534,534,343,5434,54343,54344,54345,100});

		Assert.assertTrue(concurrentQueue.offer(1000));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),20);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{40,50,5,17,5,8,10,7,32,432432,423,534,534,343,5434,54343,54344,54345,100,1000});

		Assert.assertTrue(concurrentQueue.offer(10000));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),20);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{50,5,17,5,8,10,7,32,432432,423,534,534,343,5434,54343,54344,54345,100,1000,10000});

		Assert.assertTrue(concurrentQueue.offer(100000));
		Assert.assertFalse(concurrentQueue.isEmpty());
		Assert.assertEquals(concurrentQueue.size(),20);
		Assert.assertEquals(concurrentQueue.toArray(),new Integer[]{5,17,5,8,10,7,32,432432,423,534,534,343,5434,54343,54344,54345,100,1000,10000,100000});
	}

}
