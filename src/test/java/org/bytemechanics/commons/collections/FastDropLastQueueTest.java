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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Queue;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.bytemechanics.commons.functional.LambdaUnchecker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 *
 * @author afarre
 */
public class FastDropLastQueueTest {

	private Queue<Integer> concurrentQueue;
	
	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> FastDropLastQueueTest >>>> setupSpec");
		try(InputStream inputStream = LambdaUnchecker.class.getResourceAsStream("/logging.properties")){
			LogManager.getLogManager().readConfiguration(inputStream);
		}catch (final IOException e){
			Logger.getAnonymousLogger().severe("Could not load default logging.properties file");
			Logger.getAnonymousLogger().severe(e.getMessage());
		}
	}

	@BeforeEach
    void beforeEachTest(final TestInfo testInfo) {
        System.out.println(">>>>> "+this.getClass().getSimpleName()+" >>>> "+testInfo.getTestMethod().map(Method::getName).orElse("Unkown")+""+testInfo.getTags().toString()+" >>>> "+testInfo.getDisplayName());
		this.concurrentQueue=new FastDropLastQueue<>(20);
	}

	@AfterEach
	public void tearDownMethod() throws Exception {
		this.concurrentQueue=null;
	}

	@Test
	public void testAddAll() {

		Assertions.assertTrue(concurrentQueue.isEmpty());

		Assertions.assertFalse(concurrentQueue.addAll(new ArrayList<>()));
		Assertions.assertTrue(concurrentQueue.isEmpty());
		Assertions.assertEquals(0, concurrentQueue.size());

		Assertions.assertTrue(concurrentQueue.addAll(Arrays.asList(1,2,3,4,5,6,7,8,9,10)));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(10, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{1,2,3,4,5,6,7,8,9,10},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.addAll(Arrays.asList(11,12,13,14,15,16,17,18,19,20)));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(20, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.addAll(Arrays.asList(31,32,33)));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(20, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,31,32,33},concurrentQueue.toArray());

		concurrentQueue.clear();
		Assertions.assertTrue(concurrentQueue.isEmpty());
		Assertions.assertEquals(0, concurrentQueue.size());
		
		Assertions.assertTrue(concurrentQueue.addAll(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21)));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(20, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21},concurrentQueue.toArray());

		Assertions.assertFalse(concurrentQueue.addAll(new ArrayList<>()));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(20, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.addAll(Arrays.asList(50,51,52,53,54,56)));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(20, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{8,9,10,11,12,13,14,15,16,17,18,19,20,21,50,51,52,53,54,56},concurrentQueue.toArray());
	}

	@Test
	public void testRemove() {
		
		Assertions.assertTrue(concurrentQueue.isEmpty());

		Assertions.assertFalse(concurrentQueue.remove(null));
		Assertions.assertTrue(concurrentQueue.isEmpty());
		Assertions.assertEquals(0, concurrentQueue.size());

		Assertions.assertFalse(concurrentQueue.remove(1));
		Assertions.assertTrue(concurrentQueue.isEmpty());
		Assertions.assertEquals(0, concurrentQueue.size());

		Assertions.assertTrue(concurrentQueue.addAll(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20)));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertTrue(concurrentQueue.remove(10));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(19, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{1,2,3,4,5,6,7,8,9,11,12,13,14,15,16,17,18,19,20},concurrentQueue.toArray());
		Assertions.assertTrue(concurrentQueue.addAll(Arrays.asList(10)));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(20, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{1,2,3,4,5,6,7,8,9,11,12,13,14,15,16,17,18,19,20,10},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.remove(1));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(19, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{2,3,4,5,6,7,8,9,11,12,13,14,15,16,17,18,19,20,10},concurrentQueue.toArray());
		Assertions.assertTrue(concurrentQueue.addAll(Arrays.asList(40)));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(20, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{2,3,4,5,6,7,8,9,11,12,13,14,15,16,17,18,19,20,10,40},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.remove(40));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(19, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{2,3,4,5,6,7,8,9,11,12,13,14,15,16,17,18,19,20,10},concurrentQueue.toArray());
		Assertions.assertTrue(concurrentQueue.addAll(Arrays.asList(50)));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(20, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{2,3,4,5,6,7,8,9,11,12,13,14,15,16,17,18,19,20,10,50},concurrentQueue.toArray());

		Assertions.assertFalse(concurrentQueue.remove(40));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(20, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{2,3,4,5,6,7,8,9,11,12,13,14,15,16,17,18,19,20,10,50},concurrentQueue.toArray());
	}

	@Test
	public void testPoll() {
		
		Assertions.assertTrue(concurrentQueue.isEmpty());

		Assertions.assertNull(concurrentQueue.poll());
		Assertions.assertTrue(concurrentQueue.isEmpty());
		Assertions.assertEquals(0, concurrentQueue.size());

		Assertions.assertTrue(concurrentQueue.addAll(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20)));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(concurrentQueue.poll(),(Integer)1);
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(19, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20},concurrentQueue.toArray());
		Assertions.assertTrue(concurrentQueue.addAll(Arrays.asList(10)));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(20, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,10},concurrentQueue.toArray());

		Assertions.assertEquals(concurrentQueue.poll(),(Integer)2);
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(19, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,10},concurrentQueue.toArray());
		Assertions.assertEquals(concurrentQueue.poll(),(Integer)3);
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(18, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,10},concurrentQueue.toArray());
		Assertions.assertEquals(concurrentQueue.poll(),(Integer)4);
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(17, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,10},concurrentQueue.toArray());
		Assertions.assertEquals(concurrentQueue.poll(),(Integer)5);
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(16, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,10},concurrentQueue.toArray());
		Assertions.assertEquals(concurrentQueue.poll(),(Integer)6);
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(15, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{7,8,9,10,11,12,13,14,15,16,17,18,19,20,10},concurrentQueue.toArray());
		Assertions.assertEquals(concurrentQueue.poll(),(Integer)7);
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(14, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{8,9,10,11,12,13,14,15,16,17,18,19,20,10},concurrentQueue.toArray());
		Assertions.assertEquals(concurrentQueue.poll(),(Integer)8);
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(13, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{9,10,11,12,13,14,15,16,17,18,19,20,10},concurrentQueue.toArray());
		Assertions.assertEquals(concurrentQueue.poll(),(Integer)9);
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(12, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{10,11,12,13,14,15,16,17,18,19,20,10},concurrentQueue.toArray());
		Assertions.assertEquals(concurrentQueue.poll(),(Integer)10);
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(11, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{11,12,13,14,15,16,17,18,19,20,10},concurrentQueue.toArray());
		Assertions.assertEquals(concurrentQueue.poll(),(Integer)11);
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(10, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{12,13,14,15,16,17,18,19,20,10},concurrentQueue.toArray());
		Assertions.assertEquals(concurrentQueue.poll(),(Integer)12);
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(9, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{13,14,15,16,17,18,19,20,10},concurrentQueue.toArray());
		Assertions.assertEquals(concurrentQueue.poll(),(Integer)13);
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(8, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{14,15,16,17,18,19,20,10},concurrentQueue.toArray());
		Assertions.assertEquals(concurrentQueue.poll(),(Integer)14);
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(7, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{15,16,17,18,19,20,10},concurrentQueue.toArray());
		Assertions.assertEquals(concurrentQueue.poll(),(Integer)15);
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(6, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{16,17,18,19,20,10},concurrentQueue.toArray());
		Assertions.assertEquals(concurrentQueue.poll(),(Integer)16);
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(5, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{17,18,19,20,10},concurrentQueue.toArray());
		Assertions.assertEquals(concurrentQueue.poll(),(Integer)17);
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(4, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{18,19,20,10},concurrentQueue.toArray());
		Assertions.assertEquals(concurrentQueue.poll(),(Integer)18);
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(3, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{19,20,10},concurrentQueue.toArray());
		Assertions.assertEquals(concurrentQueue.poll(),(Integer)19);
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(2, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{20,10},concurrentQueue.toArray());
		Assertions.assertEquals(concurrentQueue.poll(),(Integer)20);
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(1, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{10},concurrentQueue.toArray());
		Assertions.assertEquals(concurrentQueue.poll(),(Integer)10);
		Assertions.assertTrue(concurrentQueue.isEmpty());
		Assertions.assertEquals(0, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{},concurrentQueue.toArray());
	}
	
	@Test
	public void testOffer_null() {
		try{
			Assertions.assertTrue(concurrentQueue.isEmpty());
			Assertions.assertFalse(concurrentQueue.offer(null));
			Assertions.fail("Expected NullPointerException");
		}catch(NullPointerException e){
		}
	}

	@Test
	public void testOffer() {
		Assertions.assertTrue(concurrentQueue.isEmpty());
		Assertions.assertEquals(0, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.offer(20));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(1, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{20},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.offer(30));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(2, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{20,30},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.offer(40));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(3, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{20,30,40},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.offer(50));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(4, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{20,30,40,50},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.offer(5));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(5, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{20,30,40,50,5},concurrentQueue.toArray());
		
		Assertions.assertTrue(concurrentQueue.offer(17));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(6, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{20,30,40,50,5,17},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.offer(5));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(7, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{20,30,40,50,5,17,5},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.offer(8));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(8, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{20,30,40,50,5,17,5,8},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.offer(10));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(9, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{20,30,40,50,5,17,5,8,10},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.offer(7));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(10, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{20,30,40,50,5,17,5,8,10,7},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.offer(32));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(11, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{20,30,40,50,5,17,5,8,10,7,32},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.offer(432432));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(12, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{20,30,40,50,5,17,5,8,10,7,32,432432},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.offer(423));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(13, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{20,30,40,50,5,17,5,8,10,7,32,432432,423},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.offer(534));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(14, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{20,30,40,50,5,17,5,8,10,7,32,432432,423,534},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.offer(534));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(15, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{20,30,40,50,5,17,5,8,10,7,32,432432,423,534,534},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.offer(343));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(16, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{20,30,40,50,5,17,5,8,10,7,32,432432,423,534,534,343},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.offer(5434));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(17, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{20,30,40,50,5,17,5,8,10,7,32,432432,423,534,534,343,5434},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.offer(54343));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(18, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{20,30,40,50,5,17,5,8,10,7,32,432432,423,534,534,343,5434,54343},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.offer(54344));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(19, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{20,30,40,50,5,17,5,8,10,7,32,432432,423,534,534,343,5434,54343,54344},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.offer(54345));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(20, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{20,30,40,50,5,17,5,8,10,7,32,432432,423,534,534,343,5434,54343,54344,54345},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.offer(100));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(20, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{30,40,50,5,17,5,8,10,7,32,432432,423,534,534,343,5434,54343,54344,54345,100},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.offer(1000));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(20, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{40,50,5,17,5,8,10,7,32,432432,423,534,534,343,5434,54343,54344,54345,100,1000},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.offer(10000));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(20, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{50,5,17,5,8,10,7,32,432432,423,534,534,343,5434,54343,54344,54345,100,1000,10000},concurrentQueue.toArray());

		Assertions.assertTrue(concurrentQueue.offer(100000));
		Assertions.assertFalse(concurrentQueue.isEmpty());
		Assertions.assertEquals(20, concurrentQueue.size());
		Assertions.assertArrayEquals(new Integer[]{5,17,5,8,10,7,32,432432,423,534,534,343,5434,54343,54344,54345,100,1000,10000,100000},concurrentQueue.toArray());
	}

}
