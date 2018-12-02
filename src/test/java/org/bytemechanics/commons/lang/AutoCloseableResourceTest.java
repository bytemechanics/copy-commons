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
package org.bytemechanics.commons.lang;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author afarre
 */
public class AutoCloseableResourceTest {

	public boolean build;
	public boolean closed; 
	
	@Before
	public void before(){
		this.build=false;
		this.closed=false;
	}
	
	@Test
	public void testSupplierConsumer() throws Exception {
		System.out.println("AutoCloseableResourceTest >> testSupplierConsumer");
		
		Assert.assertFalse(this.build);
		Assert.assertFalse(this.closed);
		try(AutoCloseableResource instance=new AutoCloseableResource(() -> new TestClass(this), test -> test.toClose())){
			Assert.assertNotNull(instance);
			Assert.assertTrue(this.build);
			System.out.println("AutoCloseableResourceTest >> testSupplierConsumer >> doing nothing");
			Assert.assertFalse(this.closed);
		}
		Assert.assertTrue(this.build);
		Assert.assertTrue(this.closed);
	}

	class TestClass{
		
		private final AutoCloseableResourceTest test;
		
		public TestClass(AutoCloseableResourceTest _test){
			this.test=_test;
			this.test.build=true;
		}
		
		public void toClose(){
			this.test.closed=true;
		}
	}

	@Test
	public void testInstanceTryClose() throws Exception {
		System.out.println("AutoCloseableResourceTest >> testInstanceTryClose");
		
		Assert.assertFalse(this.build);
		Assert.assertFalse(this.closed);
		try(AutoCloseableResource instance=new AutoCloseableResource(this::init, this::close)){
			Assert.assertNotNull(instance);
			Assert.assertTrue(this.build);
			System.out.println("AutoCloseableResourceTest >> testInstanceTryClose >> doing nothing");
			Assert.assertFalse(this.closed);
		}
		Assert.assertTrue(this.build);
		Assert.assertTrue(this.closed);
	}

	public void init(){
		this.build=true;
	}
	public void close(){
		this.closed=true;
	}
}
