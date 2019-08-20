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

import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author afarre
 */
public class ArrayUtilsTest {
	
	@Test
	public void testConcat_first_null() {
		System.out.println("ArrayUtilsTest > testConcat_first_null");
		Object[] _first = null;
		Object[] _second = new Object[]{1};
		Object[] actual=ArrayUtils.concat(_first, _second);
		Assert.assertArrayEquals(_second, actual);
	}
	@Test
	public void testConcat_second_null() {
		System.out.println("ArrayUtilsTest > testConcat_second_null");
		Object[] _first = new Object[]{1};
		Object[] _second = null;
		Object[] actual=ArrayUtils.concat(_first, _second);
		Assert.assertArrayEquals(_first, actual);
	}
	@Test
	public void testConcat_both_null() {
		System.out.println("ArrayUtilsTest > testConcat_both_null");
		Object[] _first = null;
		Object[] _second = null;
		Object[] actual=ArrayUtils.concat(_first, _second);
		Assert.assertNull(actual);
	}
	@Test
	public void testConcat() {
		System.out.println("ArrayUtilsTest > testConcat_both_null");
		Object[] _first = new Object[]{1,3};
		Object[] _second = new Object[]{2,5};
		Object[] actual=ArrayUtils.concat(_first, _second);
		Assert.assertArrayEquals(new Object[]{1,3,2,5},actual);
	}
	
}
