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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 *
 * @author afarre
 */
public class LambdaUncheckerTest {

	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> LambdaUncheckerTest >>>> setupSpec");
		try(InputStream inputStream = LambdaUnchecker.class.getResourceAsStream("/logging.properties")){
			LogManager.getLogManager().readConfiguration(inputStream);
		}catch (final IOException e){
			Logger.getAnonymousLogger().severe("Could not load default logging.properties file");
			Logger.getAnonymousLogger().severe(e.getMessage());
		}
	}

	@Test
	@DisplayName("When uncheck Consumer with checked exception shouldn't be necessary any exception especified")
	public void testConsumerNoCheckedNecessary(){
		Stream.of("java.lang.Object", "java.lang.Integer", "java.lang.String")
				.forEach(LambdaUnchecker.uncheckedConsumer(className -> System.out.println(Class.forName(className))));

		Stream.of("java.lang.Object", "java.lang.Integer", "java.lang.String")
				.forEach(LambdaUnchecker.uncheckedConsumer(System.out::println));

		LambdaUnchecker.uncheckedConsumer(className -> System.out.println(Class.forName((String)className)))
							.accept("java.lang.Object");

		LambdaUnchecker.uncheckedAccept(className -> System.out.println(Class.forName((String)className)),"java.lang.Object");
	}
	@Test
	@DisplayName("When uncheck Consumer with checked exception shouldn't be necessary any exception especified but still launch an exception")
	@SuppressWarnings("ThrowableResultIgnored")
	public void testConsumerNoCheckedNecessaryButLaunched(){
		Assertions.assertThrows(ParseException.class,
								() -> LambdaUnchecker.uncheckedAccept((String value,String pattern) ->  new DecimalFormat(pattern).parse(value), "a","#0"),
					           "Expected doThing() to throw, but it didn't");
	}	
	@Test
	@DisplayName("When silence Consumer with checked exception shouldn't be necessary any exception especified")
	public void testConsumerSilenced(){
		Stream.of("java.lang.Object", "java.lang.Integer", "java.lang.String")
				.forEach(LambdaUnchecker.silencedConsumer(className -> System.out.println(Class.forName(className))));

		Stream.of("java.lang.Object", "java.lang.Integer", "java.lang.String")
				.forEach(LambdaUnchecker.silencedConsumer(System.out::println));

		LambdaUnchecker.silencedConsumer(className -> System.out.println(Class.forName((String)className)))
							.accept("java.lang.Object");

		LambdaUnchecker.silencedAccept(className -> System.out.println(Class.forName((String)className)),"java.lang.Object");
	}	
	@Test
	@DisplayName("When silence Consumer with checked exception shouldn't be necessary any exception especified and never will launch an exception")
	public void testConsumerSilenceNotThrown(){
		LambdaUnchecker.silencedAccept((String value,String pattern) ->  new DecimalFormat(pattern).parse(value), "a","#0");
	}

	@Test
	@DisplayName("When uncheck BiConsumer with checked exception shouldn't be necessary any exception especified")
	public void testBiConsumerNoCheckedNecessary(){
		LambdaUnchecker.uncheckedBiConsumer((String value,String pattern) ->  new DecimalFormat(pattern).parse(value))
							.accept("1","#0");
		LambdaUnchecker.uncheckedAccept((String value,String pattern) ->  new DecimalFormat(pattern).parse(value),"1","#0");
	}	
	@Test
	@DisplayName("When uncheck BiConsumer with checked exception shouldn't be necessary any exception especified but still launch an exception")
	@SuppressWarnings("ThrowableResultIgnored")
	public void testBiConsumerNoCheckedNecessaryButLaunched(){
		Assertions.assertThrows(ParseException.class,
								() -> LambdaUnchecker.uncheckedAccept((String value,String pattern) ->  new DecimalFormat(pattern).parse(value), "a","#0"),
					           "Expected doThing() to throw, but it didn't");
	}	
	@Test
	@DisplayName("When silence BiConsumer with checked exception shouldn't be necessary any exception especified")
	public void testBiConsumerSilenced(){
		LambdaUnchecker.silencedBiConsumer((String value,String pattern) ->  new DecimalFormat(pattern).parse(value))
							.accept("1","#0");
		LambdaUnchecker.silencedAccept((String value,String pattern) ->  new DecimalFormat(pattern).parse(value),"1","#0");

	}	
	@Test
	@DisplayName("When silence BiConsumer with checked exception shouldn't be necessary any exception especified and never will launch an exception")
	public void testBiConsumerSilenceNotThrown(){
		LambdaUnchecker.silencedAccept((String value,String pattern) ->  new DecimalFormat(pattern).parse(value), "a","#0");
	}

	@Test
	@DisplayName("When uncheck Function with checked exception shouldn't be necessary any exception especified")
	public void testFunctionNoCheckedNecessary(){
		LambdaUnchecker.uncheckedFunction((String value) -> new DecimalFormat("#0").parse(value))
								.apply("1");
		LambdaUnchecker.uncheckedApply((String value) -> new DecimalFormat("#0").parse(value),"1");
	}	
	@Test
	@DisplayName("When uncheck Function with checked exception shouldn't be necessary any exception especified but still launch an exception")
	@SuppressWarnings("ThrowableResultIgnored")
	public void testFunctionNoCheckedNecessaryButLaunched(){
		Assertions.assertThrows(ParseException.class,
								() -> LambdaUnchecker.uncheckedApply((String value) -> new DecimalFormat("#0").parse(value),"a"),
					           "Expected doThing() to throw, but it didn't");
	}	
	@Test
	@DisplayName("When silence Function with checked exception shouldn't be necessary any exception especified")
	public void testFunctionSilenced(){
		LambdaUnchecker.silencedFunction((String value) -> new DecimalFormat("#0").parse(value))
								.apply("1");
		LambdaUnchecker.silencedApply((String value) -> new DecimalFormat("#0").parse(value),"1");

	}	
	@Test
	@DisplayName("When silence Function with checked exception shouldn't be necessary any exception especified and never will launch an exception")
	public void testFunctionSilenceNotThrown(){
		LambdaUnchecker.silencedApply((String value) -> new DecimalFormat("#0").parse(value),"a");
	}

	
	@Test
	@DisplayName("When uncheck Runnable with checked exception shouldn't be necessary any exception especified")
	public void testRunnableNoCheckedNecessary(){
		LambdaUnchecker.uncheckedRunnable(() -> new DecimalFormat("#0").parse("1"))
								.run();
		LambdaUnchecker.uncheckedRun(() -> new DecimalFormat("#0").parse("1"));
	}	
	@Test
	@DisplayName("When uncheck Runnable with checked exception shouldn't be necessary any exception especified but still launch an exception")
	@SuppressWarnings("ThrowableResultIgnored")
	public void testRunnableNoCheckedNecessaryButLaunched(){
		Assertions.assertThrows(ParseException.class,
								() -> 		LambdaUnchecker.uncheckedRun(() -> new DecimalFormat("#0").parse("a")),
					           "Expected doThing() to throw, but it didn't");
	}	
	@Test
	@DisplayName("When silence Runnable with checked exception shouldn't be necessary any exception especified")
	public void testRunnableSilenced(){
		LambdaUnchecker.silencedRunnable(() -> new DecimalFormat("#0").parse("1"))
								.run();
		LambdaUnchecker.silencedRun(() -> new DecimalFormat("#0").parse("1"));
	}	
	@Test
	@DisplayName("When silence Runnable with checked exception shouldn't be necessary any exception especified and never will launch an exception")
	public void testRunnableSilenceNotThrown(){
		LambdaUnchecker.silencedRun(() -> new DecimalFormat("#0").parse("a"));
	}

	@Test
	@DisplayName("When uncheck Supplier with checked exception shouldn't be necessary any exception especified")
	public void testSupplierNoCheckedNecessary(){
		LambdaUnchecker.uncheckedSupplier(() -> new DecimalFormat("#0").parse("1"))
								.get();
		LambdaUnchecker.uncheckedGet(() -> new DecimalFormat("#0").parse("1"));
	}	
	@Test
	@DisplayName("When uncheck Supplier with checked exception shouldn't be necessary any exception especified but still launch an exception")
	@SuppressWarnings("ThrowableResultIgnored")
	public void testSupplierNoCheckedNecessaryButLaunched(){
		Assertions.assertThrows(ParseException.class,
								() -> 		LambdaUnchecker.uncheckedGet(() -> new DecimalFormat("#0").parse("a")),
					           "Expected doThing() to throw, but it didn't");
	}	
	@Test
	@DisplayName("When silence Supplier with checked exception shouldn't be necessary any exception especified")
	public void testSupplierSilenced(){
		LambdaUnchecker.silencedSupplier(() -> new DecimalFormat("#0").parse("1"))
								.get();
		LambdaUnchecker.silencedGet(() -> new DecimalFormat("#0").parse("1"));
	}	
	@Test
	@DisplayName("When silence Supplier with checked exception shouldn't be necessary any exception especified and never will launch an exception")
	public void testSupplierSilenceNotThrown(){
		LambdaUnchecker.silencedGet(() -> new DecimalFormat("#0").parse("a"));
	}
}
