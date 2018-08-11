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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author afarre
 */
public class DropLastQueueNGTest {

	private DropLastQueue<Integer> queue;
	
	@BeforeMethod
	public void setUpMethod() throws Exception {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> setUpMethod");
		this.queue=new DropLastQueue<>(20);
	}

	@AfterMethod
	public void tearDownMethod() throws Exception {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> tearDownMethod");
		this.queue=null;
	}

	@Test//(invocationCount = 10)
	public void empty_true() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> size >>> Collection >>> true");
		Assert.assertTrue(queue.isEmpty());
	}
	@Test//(invocationCount = 10)
	public void empty_false() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> size >>> Collection >>> false");
		queue.push(1);
		Assert.assertFalse(queue.isEmpty());
	}
	@Test//(invocationCount = 10)
	public void size_zero() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> size >>> Collection >>> zero");
		Assert.assertEquals(queue.size(),0);
	}
	@Test//(invocationCount = 10)
	public void size_non_zero() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> size >>> Collection >>> non_zero");
		queue.push(1,2);
		Assert.assertEquals(queue.size(),2);
	}

	@Test//(invocationCount = 10)
	public void push_none() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> none");

		Assert.assertTrue(queue.push().count()==0);
		Assert.assertEquals(queue.size(),0);
	}
	@Test//(invocationCount = 10)
	public void push_stream_null() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> push >>> stream >>> null");

		Assert.assertTrue(queue.push((Stream<Integer>)null).count()==0);
		Assert.assertEquals(queue.size(),0);
	}
	@Test//(invocationCount = 10)
	public void push_stream_empty() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> push >>> stream >>> null");

		Assert.assertTrue(queue.push(Stream.empty()).count()==0);
		Assert.assertEquals(queue.size(),0);
	}
	@Test//(invocationCount = 10)
	public void push_stream_of_one() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> push >>> stream >>> one");


		Assert.assertTrue(queue.push(Arrays.asList(1).stream()).count()==0);
		Assert.assertEquals(queue.size(),1);
	}
	@Test//(invocationCount = 10)
	public void push_stream_of_some() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> push >>> stream >>> some");

		Assert.assertTrue(queue.push(Arrays.asList(1,2,3,4,5).stream()).count()==0);
		Assert.assertEquals(queue.size(),5);
	}
	@Test//(invocationCount = 10)
	public void push_stream_of_maxSize() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> push >>> stream >>> maxsize");

		Assert.assertTrue(queue.push(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20).stream()).count()==0);
		Assert.assertEquals(queue.size(),20);
	}
	@Test//(invocationCount = 10)
	public void push_stream_of_maxSize_plus3() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> push >>> stream >>> maxsize plus 3 ");

		Assert.assertNotNull(queue.push(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23).stream()));
		Assert.assertEquals(queue.size(),20);
		Assert.assertEquals(queue.toArray(Integer.class),new Integer[]{4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23});
	}
	@Test//(invocationCount = 10)
	public void push_collection_null() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> push >>> Collection >>> null");

		Assert.assertTrue(queue.push((Collection<Integer>)null).count()==0);
		Assert.assertEquals(queue.size(),0);
	}
	@Test//(invocationCount = 10)
	public void push_collection_empty() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> push >>> Collection >>> null");

		Assert.assertTrue(queue.push(Collections.emptyList()).count()==0);
		Assert.assertEquals(queue.size(),0);
	}
	@Test//(invocationCount = 10)
	public void push_collection_of_one() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> push >>> Collection >>> one");


		Assert.assertTrue(queue.push(Arrays.asList(1)).count()==0);
		Assert.assertEquals(queue.size(),1);
	}
	@Test//(invocationCount = 10)
	public void push_collection_of_some() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> push >>> Collection >>> some");

		Assert.assertTrue(queue.push(Arrays.asList(1,2,3,4,5)).count()==0);
		Assert.assertEquals(queue.size(),5);
	}
	@Test//(invocationCount = 10)
	public void push_collection_of_maxSize() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> push >>> Collection >>> maxsize");

		Assert.assertTrue(queue.push(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20)).count()==0);
		Assert.assertEquals(queue.size(),20);
	}
	@Test//(invocationCount = 10)
	public void push_collection_of_maxSize_plus3() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> push >>> Collection >>> maxsize plus 3 ");

		Assert.assertNotNull(queue.push(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23)));
		Assert.assertEquals(queue.size(),20);
		Assert.assertEquals(queue.toArray(Integer.class),new Integer[]{4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23});
	}
	@Test//(invocationCount = 10)
	public void push_array_null() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> push >>> array >>> null");

		Assert.assertTrue(queue.push((Integer[])null).count()==0);
		Assert.assertEquals(queue.size(),0);
	}
	@Test//(invocationCount = 10)
	public void push_array_empty() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> push >>> array >>> empty");

		Assert.assertTrue(queue.push(new Integer[0]).count()==0);
		Assert.assertEquals(queue.size(),0);
	}
	@Test//(invocationCount = 10)
	public void push_array_of_nulls() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> push >>> array >>> null");

		Assert.assertTrue(queue.push(null,null).count()==0);
		Assert.assertEquals(queue.size(),0);
	}

	@Test//(invocationCount = 10)
	public void push_array_of_one() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> push >>> array >>> one");

		Assert.assertTrue(queue.push(1).count()==0);
		Assert.assertEquals(queue.size(),1);
	}
	@Test//(invocationCount = 10)
	public void push_array_of_some() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> push >>> array >>> some");

		Assert.assertTrue(queue.push(1,2,3,4,5).count()==0);
		Assert.assertEquals(queue.size(),5);
	}
	@Test//(invocationCount = 10)
	public void push_array_of_maxSize() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> push >>> array >>> maxsize");

		Assert.assertTrue(queue.push(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20).count()==0);
		Assert.assertEquals(queue.size(),20);
	}
	@Test//(invocationCount = 10)
	public void push_array_of_maxSize_plus3() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> push >>> array >>> maxsize plus 3 ");

		Assert.assertNotNull(queue.push(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23));
		Assert.assertEquals(queue.size(),20);
		Assert.assertEquals(queue.toArray(Integer.class),new Integer[]{4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23});
	}

	@Test//(invocationCount = 10)
	public void peek_first_empty() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> peek >>> first >>> empty ");

		Optional<Integer> val=queue.peek();
		Assert.assertNotNull(val);
		Assert.assertFalse(val.isPresent());
		Assert.assertEquals(queue.size(),0);
	}
	@Test//(invocationCount = 10)
	public void peek_first_one() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> peek >>> first >>> one ");

		queue.push(1);
		Assert.assertEquals(queue.size(),1);
		Optional<Integer> val=queue.peek();
		Assert.assertEquals(queue.size(),1);
		Assert.assertNotNull(val);
		Assert.assertTrue(val.isPresent());
		Assert.assertEquals(val.get(),Integer.valueOf(1));
	}
	@Test//(invocationCount = 10)
	public void peek_first_multiple() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> peek >>> first >>> multiple ");

		queue.push(1,2,3,4);
		Assert.assertEquals(queue.size(),4);
		Optional<Integer> val=queue.peek();
		Assert.assertEquals(queue.size(),4);
		Assert.assertNotNull(val);
		Assert.assertTrue(val.isPresent());
		Assert.assertEquals(val.get(),Integer.valueOf(1));
	}

	@Test//(invocationCount = 10)
	public void peek_first_segment_empty() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> peek >>> segment >>> empty ");

		Stream<Integer> val=queue.peek(1);
		Assert.assertNotNull(val);
		Assert.assertEquals(val.count(),0);
	}
	@Test//(invocationCount = 10)
	public void peek_first_segment_one() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> peek >>> segment >>> one ");

		queue.push(1);
		Assert.assertEquals(queue.size(),1);
		Stream<Integer> val=queue.peek(1);
		Assert.assertEquals(queue.size(),1);
		Assert.assertNotNull(val);
		Assert.assertEquals(val.collect(Collectors.toList()).toArray(new Integer[0]),
							new Integer[]{1});
	}
	@Test//(invocationCount = 10)
	public void peek_first_segment_less() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> peek >>> segment >>> less ");

		queue.push(1,2,3,4,5,6,7,8,9,10);
		Assert.assertEquals(queue.size(),10);
		Stream<Integer> val=queue.peek(7);
		Assert.assertEquals(queue.size(),10);
		Assert.assertNotNull(val);
		Assert.assertEquals(val.collect(Collectors.toList()).toArray(new Integer[0]),
							new Integer[]{1,2,3,4,5,6,7});
	}
	@Test//(invocationCount = 10)
	public void peek_first_segment_more() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> peek >>> segment >>> more ");

		queue.push(1,2,3,4,5,6,7,8,9,10);
		Assert.assertEquals(queue.size(),10);
		Stream<Integer> val=queue.peek(15);
		Assert.assertEquals(queue.size(),10);
		Assert.assertNotNull(val);
		Assert.assertEquals(val.collect(Collectors.toList()).toArray(new Integer[0]),
							new Integer[]{1,2,3,4,5,6,7,8,9,10});
	}
	@Test//(invocationCount = 10)
	public void peek_first_segment_more_maxsize() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> peek >>> segment >>> more maxsize ");

		queue.push(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);
		Assert.assertEquals(queue.size(),20);
		Stream<Integer> val=queue.peek(23);
		Assert.assertEquals(queue.size(),20);
		Assert.assertNotNull(val);
		Assert.assertEquals(val.collect(Collectors.toList()).toArray(new Integer[0]),
							new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20});
	}

	@Test//(invocationCount = 10)
	public void toArray_object_empty() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> toArray >>> object >>> empty ");

		Object[] val=queue.toArray();
		Assert.assertNotNull(val);
		Assert.assertEquals(val.length,0);
	}
	@Test//(invocationCount = 10)
	public void toArray_object_several() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> toArray >>> object >>> several ");

		queue.push(1,2,3,4,5,6,7,8,9,10);
		Object[] val=queue.toArray();
		Assert.assertNotNull(val);
		Assert.assertEquals(val,new Object[]{1,2,3,4,5,6,7,8,9,10});
	}
	@Test//(invocationCount = 10)
	public void toArray_object_maxSize() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> toArray >>> object >>> maxsize ");

		queue.push(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);
		Object[] val=queue.toArray();
		Assert.assertNotNull(val);
		Assert.assertEquals(val,new Object[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20});
	}


	@Test//(invocationCount = 10)
	public void toArray_integer_empty() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> toArray >>> integer >>> empty ");

		Integer[] val=queue.toArray(Integer.class);
		Assert.assertNotNull(val);
		Assert.assertEquals(val.length,0);
	}
	@Test//(invocationCount = 10)
	public void toArray_integer_several() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> toArray >>> integer >>> several ");

		queue.push(1,2,3,4,5,6,7,8,9,10);
		Integer[] val=queue.toArray(Integer.class);
		Assert.assertNotNull(val);
		Assert.assertEquals(val,new Integer[]{1,2,3,4,5,6,7,8,9,10});
	}
	@Test//(invocationCount = 10)
	public void toArray_integer_maxSize() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> toArray >>> integer >>> maxsize ");

		queue.push(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);
		Integer[] val=queue.toArray(Integer.class);
		Assert.assertNotNull(val);
		Assert.assertEquals(val,new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20});
	}

	@Test//(invocationCount = 10)
	public void toList_empty() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> toList >>> empty");

		List<Integer> val=queue.toList();
		Assert.assertNotNull(val);
		Assert.assertTrue(val.isEmpty());
	}
	@Test//(invocationCount = 10)
	public void toList_several() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> toList >>> several");

		queue.push(1,2,3,4,5,6,7,8,9,10);
		List<Integer> val=queue.toList();
		Assert.assertNotNull(val);
		Assert.assertEquals(val,Arrays.asList(1,2,3,4,5,6,7,8,9,10));
	}
	@Test//(invocationCount = 10)
	public void toList_maxSize() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> toList >>> maxSize");

		queue.push(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);
		List<Integer> val=queue.toList();
		Assert.assertNotNull(val);
		Assert.assertEquals(val,Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20));
	}

	@Test//(invocationCount = 10)
	public void stream_empty() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> stream >>> empty");

		Stream<Integer> val=queue.stream();
		Assert.assertNotNull(val);
		Assert.assertEquals(val.count(),0);
	}
	@Test//(invocationCount = 10)
	public void stream_several() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> stream >>> several");

		queue.push(1,2,3,4,5,6,7,8,9,10);
		Stream<Integer> val=queue.stream();
		Assert.assertNotNull(val);
		Assert.assertEquals(val.collect(Collectors.toList()),Arrays.asList(1,2,3,4,5,6,7,8,9,10));
	}
	@Test//(invocationCount = 10)
	public void stream_maxSize() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> stream >>> maxSize");

		queue.push(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);
		Stream<Integer> val=queue.stream();
		Assert.assertNotNull(val);
		Assert.assertEquals(val.collect(Collectors.toList()),Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20));
	}

	@Test//(invocationCount = 10)
	public void reverseStream_empty() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> reverseStream >>> empty");

		Stream<Integer> val=queue.reverseStream();
		Assert.assertNotNull(val);
		Assert.assertEquals(val.count(),0);
	}
	@Test//(invocationCount = 10)
	public void reverseStream_several() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> reverseStream >>> several");

		queue.push(1,2,3,4,5,6,7,8,9,10);
		Stream<Integer> val=queue.reverseStream();
		Assert.assertNotNull(val);
		Assert.assertEquals(val.collect(Collectors.toList()),Arrays.asList(10,9,8,7,6,5,4,3,2,1));
	}
	@Test//(invocationCount = 10)
	public void reverseStream_maxSize() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> reverseStream >>> maxSize");

		queue.push(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);
		Stream<Integer> val=queue.reverseStream();
		Assert.assertNotNull(val);
		Assert.assertEquals(val.collect(Collectors.toList()),Arrays.asList(20,19,18,17,16,15,14,13,12,11,10,9,8,7,6,5,4,3,2,1));
	}

	@Test//(invocationCount = 10)
	public void clear_empty() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> clear >>> empty");

		Stream<Integer> val=queue.clear();
		Assert.assertEquals(queue.size(), 0);
		Assert.assertNotNull(val);
		Assert.assertEquals(val.count(),0);
	}
	@Test//(invocationCount = 10)
	public void clear_several() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> clear >>> several");

		queue.push(1,2,3,4,5,6,7,8,9,10);
		Stream<Integer> val=queue.clear();
		Assert.assertEquals(queue.size(), 0);
		Assert.assertNotNull(val);
		Assert.assertEquals(val.collect(Collectors.toList()),Arrays.asList(1,2,3,4,5,6,7,8,9,10));
	}
	@Test//(invocationCount = 10)
	public void clear_maxSize() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> clear >>> maxSize");

		queue.push(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);
		Stream<Integer> val=queue.clear();
		Assert.assertEquals(queue.size(), 0);
		Assert.assertNotNull(val);
		Assert.assertEquals(val.collect(Collectors.toList()),Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20));
	}

	@Test//(invocationCount = 10)
	public void remove_one_empty() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> remove >>> empty");

		Stream<Integer> val=queue.remove(3);
		Assert.assertEquals(queue.size(), 0);
		Assert.assertNotNull(val);
		Assert.assertEquals(val.count(),0);
	}
	@Test//(invocationCount = 10)
	public void remove_zero_several() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> remove[zero] >>> several");

		queue.push(1,2,3,4,5,6,7,8,9,10);
		Assert.assertEquals(queue.size(), 10);
		Stream<Integer> val=queue.remove(0);
		Assert.assertEquals(queue.size(), 10);
		Assert.assertNotNull(val);
		Assert.assertEquals(val.count(),0);
		Assert.assertEquals(queue.toList(),Arrays.asList(1,2,3,4,5,6,7,8,9,10));
	}
	@Test//(invocationCount = 10)
	public void remove_several_several() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> remove[several] >>> several");

		queue.push(1,2,3,4,5,6,7,8,9,10);
		Assert.assertEquals(queue.size(), 10);
		Stream<Integer> val=queue.remove(4);
		Assert.assertEquals(queue.size(), 6);
		Assert.assertNotNull(val);
		Assert.assertEquals(queue.toList(),Arrays.asList(5,6,7,8,9,10));
	}
	@Test//(invocationCount = 10)
	public void remove_all_several() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> remove[all] >>> several");

		queue.push(1,2,3,4,5,6,7,8,9,10);
		Assert.assertEquals(queue.size(), 10);
		Stream<Integer> val=queue.remove(10);
		Assert.assertEquals(queue.size(), 0);
		Assert.assertNotNull(val);
	}
	@Test//(invocationCount = 10)
	public void remove_all3_several() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> remove[all+3] >>> several");

		queue.push(1,2,3,4,5,6,7,8,9,10);
		Assert.assertEquals(queue.size(), 10);
		Stream<Integer> val=queue.remove(13);
		Assert.assertEquals(queue.size(), 0);
		Assert.assertNotNull(val);
	}
	@Test//(invocationCount = 10)
	public void remove_several_maxSize() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> remove[several] >>> maxSize");

		queue.push(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);
		Assert.assertEquals(queue.size(), 20);
		Stream<Integer> val=queue.remove(13);
		Assert.assertEquals(queue.size(), 7);
		Assert.assertNotNull(val);
		Assert.assertEquals(queue.toList(),Arrays.asList(14,15,16,17,18,19,20));
	}
	@Test//(invocationCount = 10)
	public void remove_all_maxSize() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> remove[all] >>> maxSize");

		queue.push(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);
		Assert.assertEquals(queue.size(), 20);
		Stream<Integer> val=queue.remove(20);
		Assert.assertEquals(queue.size(), 0);
		Assert.assertNotNull(val);
	}
	@Test//(invocationCount = 10)
	public void remove_all3_maxSize() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> remove[all+3] >>> maxSize");

		queue.push(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);
		Assert.assertEquals(queue.size(), 20);
		Stream<Integer> val=queue.remove(23);
		Assert.assertEquals(queue.size(), 0);
		Assert.assertNotNull(val);
	}


	@Test//(invocationCount = 10)
	public void poll_one_empty() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> poll >>> empty");

		Stream<Integer> val=queue.poll(3);
		Assert.assertEquals(queue.size(), 0);
		Assert.assertNotNull(val);
		Assert.assertEquals(val.count(),0);
	}
	@Test//(invocationCount = 10)
	public void poll_zero_several() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> poll[zero] >>> several");

		queue.push(1,2,3,4,5,6,7,8,9,10);
		Assert.assertEquals(queue.size(), 10);
		Stream<Integer> val=queue.poll(0);
		Assert.assertEquals(queue.size(), 10);
		Assert.assertNotNull(val);
		Assert.assertEquals(val.count(),0);
		Assert.assertEquals(queue.toList(),Arrays.asList(1,2,3,4,5,6,7,8,9,10));
	}
	@Test//(invocationCount = 10)
	public void poll_several_several() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> poll[several] >>> several");

		queue.push(1,2,3,4,5,6,7,8,9,10);
		Assert.assertEquals(queue.size(), 10);
		Stream<Integer> val=queue.poll(4);
		Assert.assertEquals(queue.size(), 6);
		Assert.assertNotNull(val);
		Assert.assertEquals(val.collect(Collectors.toList()),Arrays.asList(1,2,3,4));
		Assert.assertEquals(queue.toList(),Arrays.asList(5,6,7,8,9,10));
	}
	@Test//(invocationCount = 10)
	public void poll_all_several() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> poll[all] >>> several");

		queue.push(1,2,3,4,5,6,7,8,9,10);
		Assert.assertEquals(queue.size(), 10);
		Stream<Integer> val=queue.poll(10);
		Assert.assertEquals(queue.size(), 0);
		Assert.assertNotNull(val);
		Assert.assertEquals(val.collect(Collectors.toList()),Arrays.asList(1,2,3,4,5,6,7,8,9,10));
	}
	@Test//(invocationCount = 10)
	public void poll_all3_several() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> poll[all+3] >>> several");

		queue.push(1,2,3,4,5,6,7,8,9,10);
		Assert.assertEquals(queue.size(), 10);
		Stream<Integer> val=queue.poll(13);
		Assert.assertEquals(queue.size(), 0);
		Assert.assertNotNull(val);
		Assert.assertEquals(val.collect(Collectors.toList()),Arrays.asList(1,2,3,4,5,6,7,8,9,10));
	}
	@Test//(invocationCount = 10)
	public void poll_several_maxSize() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> poll[several] >>> maxSize");

		queue.push(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);
		Assert.assertEquals(queue.size(), 20);
		Stream<Integer> val=queue.poll(13);
		Assert.assertEquals(queue.size(), 7);
		Assert.assertNotNull(val);
		Assert.assertEquals(val.collect(Collectors.toList()),Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13));
		Assert.assertEquals(queue.toList(),Arrays.asList(14,15,16,17,18,19,20));
	}
	@Test//(invocationCount = 10)
	public void poll_all_maxSize() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> poll[all] >>> maxSize");

		queue.push(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);
		Assert.assertEquals(queue.size(), 20);
		Stream<Integer> val=queue.poll(20);
		Assert.assertEquals(queue.size(), 0);
		Assert.assertNotNull(val);
		Assert.assertEquals(val.collect(Collectors.toList()),Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20));
	}
	@Test//(invocationCount = 10)
	public void poll_all3_maxSize() {
		System.out.println("FixedSizeConcurrentQueueNGTest >>>> poll[all+3] >>> maxSize");

		queue.push(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20);
		Assert.assertEquals(queue.size(), 20);
		Stream<Integer> val=queue.poll(23);
		Assert.assertEquals(queue.size(), 0);
		Assert.assertNotNull(val);
		Assert.assertEquals(val.collect(Collectors.toList()),Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20));
	}
}
