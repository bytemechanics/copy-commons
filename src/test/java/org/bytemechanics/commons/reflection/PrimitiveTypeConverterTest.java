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
package org.bytemechanics.commons.reflection;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.bytemechanics.commons.functional.LambdaUnchecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 *
 * @author afarre
 */
public class PrimitiveTypeConverterTest {
	
	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> PrimitiveTypeConverterTest >>>> setupSpec");
		final InputStream inputStream = LambdaUnchecker.class.getResourceAsStream("/logging.properties");
		try{
			LogManager.getLogManager().readConfiguration(inputStream);
		}catch (final IOException e){
			Logger.getAnonymousLogger().severe("Could not load default logging.properties file");
			Logger.getAnonymousLogger().severe(e.getMessage());
		}finally{
			if(inputStream!=null)
				inputStream.close();
		}
	}
	@BeforeEach
    void beforeEachTest(final TestInfo testInfo) {
        System.out.println(">>>>> "+this.getClass().getSimpleName()+" >>>> "+testInfo.getTestMethod().map(Method::getName).orElse("Unkown")+""+testInfo.getTags().toString()+" >>>> "+testInfo.getDisplayName());
    }
	
	static Stream<Arguments> primitiveDataPack() {
	    return Stream.of(
			Arguments.of(void.class	,Void.class),
			Arguments.of(byte.class	, Byte.class),
			Arguments.of(boolean.class,Boolean.class),
			Arguments.of(char.class, Character.class),
			Arguments.of(short.class, Short.class),
			Arguments.of(int.class, Integer.class),
			Arguments.of(long.class	,Long.class),
			Arguments.of(float.class, Float.class),
			Arguments.of(double.class, Double.class)
		);
	}
	@ParameterizedTest(name = "Converting primitive {0} should return its corresponding object class {1}")
	@MethodSource("primitiveDataPack")
	public void testPrimitive(final Class _primitive,final Class _clazz){

		Class result=PrimitiveTypeConverter.convert(_primitive);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(_clazz,result);
	}

	@Test
	@DisplayName("Try to convert a null primitive shoud raise a ClassCastExeption")
	@SuppressWarnings("ThrowableResultIgnored")
	public void testPrimitiveNull(){

		Assertions.assertThrows(NullPointerException.class,
								() -> PrimitiveTypeConverter.convert(null));
	}

	@Test
	@DisplayName("Try to convert an unnexistent primitive #primitive shoud return the same class")
	public void testPrimitiveNoExist(){

		Class actual=PrimitiveTypeConverter.convert(Integer.class);
		
		Assertions.assertNotNull(actual);
		Assertions.assertEquals(Integer.class,actual);
	}
}
