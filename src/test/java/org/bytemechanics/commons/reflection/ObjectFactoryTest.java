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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.bytemechanics.commons.functional.LambdaUnchecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 *
 * @author afarre
 */
public class ObjectFactoryTest {
	
	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> ObjectFactoryTest >>>> setupSpec");
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
		Handler ch = new ConsoleHandler();
		ch.setLevel(Level.ALL);
		Logger.getLogger("org.bytemechanics.service.repository.internal.ObjectFactory").addHandler(ch);
		Logger.getLogger("org.bytemechanics.service.repository.internal.ObjectFactory").setLevel(Level.ALL);
    }
	
	@ParameterizedTest(name = "Calling of({0}) should return an instance of ObjectFactory with instantiation objective #objectiveClass")
	@ValueSource(classes = {DummieServiceImpl.class})
	public void testConstructor(final Class _objectiveClass){

		ObjectFactory objectFactory=ObjectFactory.of(_objectiveClass);
		
		Assertions.assertNotNull(objectFactory);
		Assertions.assertTrue(objectFactory instanceof ObjectFactory);
		Assertions.assertNotNull(objectFactory.getToInstantiate());
		Assertions.assertEquals(_objectiveClass,objectFactory.getToInstantiate());
	}

	static Stream<Arguments> parameterizedDataPack() {
	    return Stream.of(
			Arguments.of(DummieServiceImpl.class,new Object[]{}),
			Arguments.of(DummieServiceImpl.class,new Object[]{"1arg-arg1"}),
			Arguments.of(DummieServiceImpl.class,new Object[]{"1arg-arg1",3,"3arg-arg2"})
		);
	}
	@ParameterizedTest(name = "Calling with({1}) over an existent ObjectFactory of {0} should return an instance of ObjectFactory with instantiation objective {0} with {1} constructor")
	@MethodSource("parameterizedDataPack")
	public void testParameterizedConstructor(final Class _objectiveClass,final Object[] _args){

		ObjectFactory objectFactory=ObjectFactory.of(_objectiveClass)
										.with((Object[])_args);

		Assertions.assertNotNull(objectFactory);
		Assertions.assertTrue(objectFactory instanceof ObjectFactory);
		Assertions.assertNotNull(objectFactory.getToInstantiate());
		Assertions.assertEquals(_objectiveClass,objectFactory.getToInstantiate());
		Assertions.assertNotNull(objectFactory.getAttributes());
		Assertions.assertEquals(Arrays.asList(_args),objectFactory.getAttributes());
	}

	static Stream<Arguments> supplierDataPack() throws NoSuchMethodException {
	    return Stream.of(
			Arguments.of(DummieServiceImpl.class,new Object[]{}),
			Arguments.of(DummieServiceImpl.class,new Object[]{"1arg-arg1"}),
			Arguments.of(DummieServiceImpl.class,new Object[]{"1arg-arg1",3,"3arg-arg2"})
		);
	}
	@ParameterizedTest(name = "when object factory builds a supplier of {0} with {1} should return a supplier of {0}")
	@MethodSource("supplierDataPack")
	public void testConstructorSupplier(final Class _supplierClass,final Object[] _args){

		Supplier<Optional> supplier=ObjectFactory.of(_supplierClass)
										.with((Object[])_args)
										.supplier();
		DummieServiceImpl instance=(DummieServiceImpl)((supplier!=null)? supplier.get().get() : null);
		Object[] expected=new Object[]{(_args.length>0)? _args[0] : "",(_args.length>1)? _args[1] : 0,(_args.length>2)? _args[2] : ""};			
		Object[] args=(instance!=null)? new Object[]{instance.getArg1(),instance.getArg2(),instance.getArg3()} : null;

		Assertions.assertNotNull(supplier);
		Assertions.assertNotNull(instance);
		Assertions.assertEquals(_supplierClass,instance.getClass());
		Assertions.assertArrayEquals(args,expected);
	}


	static Stream<Arguments> supplierDataPackNoMatch() throws NoSuchMethodException {
	    return Stream.of(
			Arguments.of(DummieServiceImpl.class,new Object[]{"1arg-arg1",3,false,false}),
			Arguments.of(DummieServiceImpl.class,new Object[]{"1arg-arg1",3,24.3,false})
		);
	}
	@ParameterizedTest(name = "when object factory builds a supplier of {0} with {1} (no match) should return a empty optional and write a log")
	@MethodSource("supplierDataPackNoMatch")
	public void testConstructorSupplierNoMatch(final Class _supplierClass,final Object[] _args){

		Optional result=(Optional)ObjectFactory.of(_supplierClass)
												.with((Object[])_args)
												.supplier()
												.get();

		Assertions.assertNotNull(result);
		Assertions.assertFalse(result.isPresent());
	}

	static Stream<Arguments> supplierDataPackWithException() throws NoSuchMethodException {
	    return Stream.of(
			Arguments.of(DummieServiceImpl.class,new Object[]{"1arg-arg1",null,false,"3arg-arg2"}),
			Arguments.of(DummieServiceImpl.class,new Object[]{"1arg-arg1",3,false,"3arg-arg2"})
		);
	}
	@ParameterizedTest(name = "When object factory builds a supplier of {0} with {1} that throws an exception should fail with InvocationTargetException.class}")
	@MethodSource("supplierDataPackWithException")
	@SuppressWarnings("ThrowableResultIgnored")
	public void testConstructorSupplierWithException(final Class _supplierClass,final Object[] _args){

		Assertions.assertThrows(InvocationTargetException.class,
								() -> ObjectFactory.of(_supplierClass)
										.with((Object[])_args)
										.supplier()
										.get());
	}

	static Stream<Arguments> autoboxDataPack() {
	    return Stream.of(
			Arguments.of(void.class	,Void.class),
			Arguments.of(byte.class	, Byte.class),
			Arguments.of(boolean.class,Boolean.class),
			Arguments.of(char.class, Character.class),
			Arguments.of(short.class, Short.class),
			Arguments.of(int.class, Integer.class),
			Arguments.of(long.class	,Long.class),
			Arguments.of(float.class, Float.class),
			Arguments.of(double.class, Double.class),
			Arguments.of(Integer.class, Integer.class),
			Arguments.of(String.class,String.class)
		);
	}
	@ParameterizedTest(name = "when object factory try to autobox {0} primitive returns {1} class")
	@MethodSource("autoboxDataPack")
	public void testAutobox(final Class _primitive,final Class _object){

		Object result=ObjectFactory.of(DummieServiceImpl.class)
										.autobox(_primitive);
		
		Assertions.assertNotNull(result);
		Assertions.assertEquals(_object,result);
	}
}
