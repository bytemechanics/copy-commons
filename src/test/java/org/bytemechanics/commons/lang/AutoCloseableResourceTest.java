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

import java.lang.reflect.Method;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;


/**
 *
 * @author afarre
 */
public class AutoCloseableResourceTest {

	public boolean build;
	public boolean closed; 
	
	@BeforeEach
    void beforeEachTest(final TestInfo testInfo) {
        System.out.println(">>>>> "+this.getClass().getSimpleName()+" >>>> "+testInfo.getTestMethod().map(Method::getName).orElse("Unkown")+""+testInfo.getTags().toString()+" >>>> "+testInfo.getDisplayName());
		this.build=false;
		this.closed=false;
	}
	
	@Test
	public void testSupplierConsumer() throws Exception {
		Assertions.assertFalse(this.build);
		Assertions.assertFalse(this.closed);
		try(AutoCloseableResource instance=new AutoCloseableResource(() -> new TestClass(this), test -> test.toClose())){
			Assertions.assertNotNull(instance);
			Assertions.assertTrue(this.build);
			System.out.println("AutoCloseableResourceTest >> testSupplierConsumer >> doing nothing");
			Assertions.assertFalse(this.closed);
		}
		Assertions.assertTrue(this.build);
		Assertions.assertTrue(this.closed);
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
		
		Assertions.assertFalse(this.build);
		Assertions.assertFalse(this.closed);
		try(AutoCloseableResource instance=new AutoCloseableResource(this::init, this::close)){
			Assertions.assertNotNull(instance);
			Assertions.assertTrue(this.build);
			System.out.println("AutoCloseableResourceTest >> testInstanceTryClose >> doing nothing");
			Assertions.assertFalse(this.closed);
		}
		Assertions.assertTrue(this.build);
		Assertions.assertTrue(this.closed);
	}

	public void init(){
		this.build=true;
	}
	public void close(){
		this.closed=true;
	}
}
