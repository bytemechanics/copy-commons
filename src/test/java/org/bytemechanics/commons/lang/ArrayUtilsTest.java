/*
 * Copyright 2019 Byte Mechanics.
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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.bytemechanics.commons.functional.LambdaUnchecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 *
 * @author afarre
 */
@DisplayName("ArrayUtils test case")
public class ArrayUtilsTest {

	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> ArrayUtilsTest >>>> setupSpec");
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
	}
	
	@Test
	@DisplayName("Concat array with first argument as null should return second")
	public void testConcatNulls() {
		Object[] _first = null;
		Object[] _second = new Object[]{1};
		Object[] actual=ArrayUtils.concat(_first, _second);
		Assertions.assertArrayEquals(_second, actual);
	}
	@Test
	@DisplayName("Concat array with second argument as null should return first")
	public void testConcat_second_null() {
		Object[] _first = new Object[]{1};
		Object[] _second = null;
		Object[] actual=ArrayUtils.concat(_first, _second);
		Assertions.assertArrayEquals(_first, actual);
	}
	@Test
	@DisplayName("Concat array with both arguments as null should return null")
	public void testConcat_both_null() {
		Object[] _first = null;
		Object[] _second = null;
		Object[] actual=ArrayUtils.concat(_first, _second);
		Assertions.assertNull(actual);
	}
	@Test
	@DisplayName("Concat array with both arguments present should return the array concatenation")
	public void testConcat() {
		Object[] _first = new Object[]{1,3};
		Object[] _second = new Object[]{2,5};
		Object[] actual=ArrayUtils.concat(_first, _second);
		Assertions.assertArrayEquals(new Object[]{1,3,2,5},actual);
	}
	
}
