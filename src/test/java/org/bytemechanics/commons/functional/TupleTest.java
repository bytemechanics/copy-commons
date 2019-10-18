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
package org.bytemechanics.commons.functional;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 *
 * @author afarre
 */
public class TupleTest {
	
	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> TupleTest >>>> setupSpec");
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

	
	static Stream<Arguments> dataPack() {
	    return Stream.of(
			Arguments.of(2	,"left-value"),
			Arguments.of("right-value",2.2d),
			Arguments.of(null,"left-value-not-null"),
			Arguments.of("right-value-not-null", null),
			Arguments.of(null,null)
		);
	}

	@ParameterizedTest(name = "When constructor is called with left={0} and right={1} then the same left and right is recovered")
	@MethodSource("dataPack")
	public void testConstructor(final Object _left,final Object _right){
		final Tuple tuple=Tuple.of(_left,_right);
		Assertions.assertEquals(_left,tuple.left());
		Assertions.assertEquals(_left,tuple.getLeft());
		Assertions.assertEquals(_right,tuple.right());
		Assertions.assertEquals(_right,tuple.getRight());
	}
	@ParameterizedTest(name ="When wo instances with the same left={0} and right={1} values are created then hashcode must be the same")
	@MethodSource("dataPack")
	public void testHashCode(final Object _left,final Object _right){
		final Tuple tuple=Tuple.of(_left,_right);
		final Tuple tuple2=Tuple.of(_left,_right);
		Assertions.assertEquals(tuple.hashCode(), tuple2.hashCode());
	}
	@ParameterizedTest(name ="When wo instances with the same left={0} and right={1} values are created then equals must be true")
	@MethodSource("dataPack")
	public void testEquals(final Object _left,final Object _right){
		final Tuple tuple=Tuple.of(_left,_right);
		final Tuple tuple2=Tuple.of(_left,_right);
		Assertions.assertTrue(tuple.equals(tuple2));
	}
	@ParameterizedTest(name ="When wo instances with the same left={0} and right={1} values shouldn't be the same instance")
	@MethodSource("dataPack")
	public void testNotSame(final Object _left,final Object _right){
		final Tuple tuple=Tuple.of(_left,_right);
		final Tuple tuple2=Tuple.of(_left,_right);
		Assertions.assertNotSame(tuple, tuple2);
	}

	static Stream<Arguments> replaceLeftDatapack() {
	    return Stream.of(
			Arguments.of(2, 3,"left-value"),
			Arguments.of("right-value", "newVal", 2.2d),
			Arguments.of(null, 2.2d, "left-value-not-null"),
			Arguments.of("right-value-not-null"	, 126, null),
			Arguments.of(null, "another_val", null)
		);
	}

	@ParameterizedTest(name ="When replace left={0} value with newLeft={1} then a new instance is created with newLeft value but keeping the original right={2} value")
	@MethodSource("replaceLeftDatapack")
	public void testReplaceLeft(final Object _left,final Object _newLeft,final Object _right){
		final Tuple tuple=Tuple.of(_left,_right);
		final Tuple tuple2=tuple.left(_newLeft);
		
		Assertions.assertEquals(_left,tuple.left());
		Assertions.assertEquals(_left,tuple.getLeft());
		Assertions.assertEquals(_right,tuple.right());
		Assertions.assertEquals(_right,tuple.getRight());
		Assertions.assertNotEquals(tuple.hashCode(), tuple2.hashCode());
		Assertions.assertFalse(tuple.equals(tuple2));
		Assertions.assertEquals(_newLeft,tuple2.left());
		Assertions.assertEquals(_newLeft,tuple2.getLeft());
		Assertions.assertEquals(_right,tuple2.right());
		Assertions.assertEquals(_right,tuple2.getRight());
	}

	static Stream<Arguments> replaceRightDatapack() {
	    return Stream.of(
			Arguments.of(2,"left-value",3),		
			Arguments.of("right-value",2.2d,"newVal"),	
			Arguments.of(null, "left-value-not-null",2.2d),			
			Arguments.of("right-value-not-null",null,126),			
			Arguments.of(null,null,"another_val")
		);
	}

	@ParameterizedTest(name ="When replace right={1} value with newRight={2} then a new instance is created with newRight value but keeping the original left={0} value")
	@MethodSource("replaceRightDatapack")
	public void testReplaceRight(final Object _left,final Object _right,final Object _newRight){
		final Tuple tuple=Tuple.of(_left,_right);
		final Tuple tuple2=tuple.right(_newRight);
		
		Assertions.assertEquals(_left,tuple.left());
		Assertions.assertEquals(_left,tuple.getLeft());
		Assertions.assertEquals(_right,tuple.right());
		Assertions.assertEquals(_right,tuple.getRight());
		Assertions.assertNotEquals(tuple.hashCode(), tuple2.hashCode());
		Assertions.assertFalse(tuple.equals(tuple2));
		Assertions.assertEquals(_left,tuple2.left());
		Assertions.assertEquals(_left,tuple2.getLeft());
		Assertions.assertEquals(_newRight,tuple2.right());
		Assertions.assertEquals(_newRight,tuple2.getRight());
	}
	static Stream<Arguments> replaceBothDatapack() {
	    return Stream.of(
			Arguments.of(2	, 3	, "left-value", 10),		
			Arguments.of("right-value","newVal", 2.2d, 1),	
			Arguments.of(null, 2.2d	,"left-value-not-null",'a'	),	
			Arguments.of("right-value-not-null",126, null	,"jjj"),		
			Arguments.of(null,"another_val",null, 124.0f)
		);
	}

	@ParameterizedTest(name ="When replace left={0} with newLeft={1} and right={2} value with newRight={3} then a new instance is created with newRight value and newLeft value")
	@MethodSource("replaceBothDatapack")
	public void testReplaceBoth(final Object _left,final Object _newLeft,final Object _right,final Object _newRight){
		final Tuple tuple=Tuple.of(_left,_right);
		final Tuple tuple2=tuple.with(_newLeft,_newRight);
		
		Assertions.assertEquals(_left,tuple.left());
		Assertions.assertEquals(_left,tuple.getLeft());
		Assertions.assertEquals(_right,tuple.right());
		Assertions.assertEquals(_right,tuple.getRight());
		Assertions.assertNotEquals(tuple.hashCode(), tuple2.hashCode());
		Assertions.assertFalse(tuple.equals(tuple2));
		Assertions.assertEquals(_newLeft,tuple2.left());
		Assertions.assertEquals(_newLeft,tuple2.getLeft());
		Assertions.assertEquals(_newRight,tuple2.right());
		Assertions.assertEquals(_newRight,tuple2.getRight());
	}
}
