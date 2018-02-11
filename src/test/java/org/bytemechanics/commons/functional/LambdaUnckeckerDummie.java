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
package org.bytemechanics.commons.functional;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.stream.Stream;
import org.junit.Test;

/**
 * @author afarre
 */
public class LambdaUnckeckerDummie {

	public void consumer_with_checked_exceptions_uncheck() {
		Stream.of("java.lang.Object", "java.lang.Integer", "java.lang.String")
				.forEach(LambdaUnchecker.uncheckedConsumer(className -> System.out.println(Class.forName(className))));

		Stream.of("java.lang.Object", "java.lang.Integer", "java.lang.String")
				.forEach(LambdaUnchecker.uncheckedConsumer(System.out::println));

		LambdaUnchecker.uncheckedConsumer(className -> System.out.println(Class.forName((String)className)))
							.accept("java.lang.Object");

		LambdaUnchecker.uncheckedAccept(className -> System.out.println(Class.forName((String)className)),"java.lang.Object");
	}
	public void consumer_with_checked_exceptions_uncheck_still_thrown() {
		LambdaUnchecker.uncheckedAccept(className -> System.out.println(Class.forName((String)className)),"java.lang.noexist");
	}
	public void consumer_with_checked_exceptions_silenced() {
		Stream.of("java.lang.Object", "java.lang.Integer", "java.lang.String")
				.forEach(LambdaUnchecker.silencedConsumer(className -> System.out.println(Class.forName(className))));

		Stream.of("java.lang.Object", "java.lang.Integer", "java.lang.String")
				.forEach(LambdaUnchecker.silencedConsumer(System.out::println));

		LambdaUnchecker.silencedConsumer(className -> System.out.println(Class.forName((String)className)))
							.accept("java.lang.Object");

		LambdaUnchecker.silencedAccept(className -> System.out.println(Class.forName((String)className)),"java.lang.Object");
	}
	public void consumer_with_checked_exceptions_silenced_not_thrown() {
		LambdaUnchecker.silencedAccept(className -> System.out.println(Class.forName((String)className)),"java.lang.noexist");
	}

	
	@Test
	public void biConsumer_with_checked_exceptions_uncheck() {
		System.out.println("LambdaExceptionUtilTest >>> test_BiConsumer_with_checked_exceptions_uncheck");
		LambdaUnchecker.uncheckedBiConsumer((String value,String pattern) ->  new DecimalFormat(pattern).parse(value))
							.accept("1","#0");
		LambdaUnchecker.uncheckedAccept((String value,String pattern) ->  new DecimalFormat(pattern).parse(value),"1","#0");
	}
	@Test(expected = ParseException.class)
	public void biConsumer_with_checked_exceptions_uncheck_still_thrown() {
		System.out.println("LambdaExceptionUtilTest >>> test_BiConsumer_with_checked_exceptions_uncheck_still_thrown");
		LambdaUnchecker.uncheckedAccept((String value,String pattern) ->  new DecimalFormat(pattern).parse(value), "a","#0");
	}

	public void biConsumer_with_checked_exceptions_silenced() {
		System.out.println("LambdaExceptionUtilTest >>> test_BiConsumer_with_checked_exceptions_silenced");
		LambdaUnchecker.silencedBiConsumer((String value,String pattern) ->  new DecimalFormat(pattern).parse(value))
							.accept("1","#0");
		LambdaUnchecker.silencedAccept((String value,String pattern) ->  new DecimalFormat(pattern).parse(value),"1","#0");
	}
	public void biConsumer_with_checked_exceptions_silenced_not_thrown() {
		System.out.println("LambdaExceptionUtilTest >>> test_BiConsumer_with_checked_exceptions_silenced_not_thrown");
		LambdaUnchecker.silencedAccept((String value,String pattern) ->  new DecimalFormat(pattern).parse(value), "a","#0");
	}

	
	public void function_with_checked_exceptions_uncheck() {
		LambdaUnchecker.uncheckedFunction((String value) -> new DecimalFormat("#0").parse(value))
								.apply("1");
		LambdaUnchecker.uncheckedApply((String value) -> new DecimalFormat("#0").parse(value),"1");
	}
	public void function_with_checked_exceptions_uncheck_still_thrown() {
		LambdaUnchecker.uncheckedApply((String value) -> new DecimalFormat("#0").parse(value),"a");
	}
	public void function_with_checked_exceptions_silenced() {
		LambdaUnchecker.silencedFunction((String value) -> new DecimalFormat("#0").parse(value))
								.apply("1");
		LambdaUnchecker.silencedApply((String value) -> new DecimalFormat("#0").parse(value),"1");
	}
	public void function_with_checked_exceptions_silenced_not_thrown() {
		LambdaUnchecker.silencedApply((String value) -> new DecimalFormat("#0").parse(value),"a");
	}
	
	
	public void supplier_with_checked_exceptions_uncheck() {
		LambdaUnchecker.uncheckedSupplier(() -> new DecimalFormat("#0").parse("1"))
								.get();
		LambdaUnchecker.uncheckedGet(() -> new DecimalFormat("#0").parse("1"));
	}
	public void supplier_with_checked_exceptions_uncheck_still_thrown() {
		LambdaUnchecker.uncheckedGet(() -> new DecimalFormat("#0").parse("a"));
	}
	public void supplier_with_checked_exceptions_silenced() {
		LambdaUnchecker.silencedSupplier(() -> new DecimalFormat("#0").parse("1"))
								.get();
		LambdaUnchecker.silencedGet(() -> new DecimalFormat("#0").parse("1"));
	}
	public void supplier_with_checked_exceptions_silenced_not_thrown() {
		LambdaUnchecker.silencedGet(() -> new DecimalFormat("#0").parse("a"));
	}

	
	public void runnable_with_checked_exceptions_uncheck() {
		LambdaUnchecker.uncheckedRunnable(() -> new DecimalFormat("#0").parse("1"))
								.run();
		LambdaUnchecker.uncheckedRun(() -> new DecimalFormat("#0").parse("1"));
	}
	public void runnable_with_checked_exceptions_uncheck_still_thrown() {
		LambdaUnchecker.uncheckedRun(() -> new DecimalFormat("#0").parse("a"));
	}
	public void runnable_with_checked_exceptions_silenced() {
		LambdaUnchecker.silencedRunnable(() -> new DecimalFormat("#0").parse("1"))
								.run();
		LambdaUnchecker.silencedRun(() -> new DecimalFormat("#0").parse("1"));
	}
	public void runnable_with_checked_exceptions_silenced_not_thrown() {
		LambdaUnchecker.silencedRun(() -> new DecimalFormat("#0").parse("a"));
	}

/*
	@Test
	public void test_Function_with_checked_exceptions() throws ClassNotFoundException {
		List<Class> classes1
				= Stream.of("Object", "Integer", "String")
						.map(LambdaUnchecker.uncheckedFunction(className -> Class.forName("java.lang." + className)))
						.collect(Collectors.toList());

		List<Class> classes2
				= Stream.of("java.lang.Object", "java.lang.Integer", "java.lang.String")
						.map(LambdaUnchecker.uncheckedFunction(Class::forName))
						.collect(Collectors.toList());
	}

	@Test
	public void test_Supplier_with_checked_exceptions() throws ClassNotFoundException {
		Collector.of(
				LambdaUnchecker.uncheckedSupplier(() -> new StringJoiner(new String(new byte[]{77, 97, 114, 107}, "UTF-8"))),
				StringJoiner::add, StringJoiner::merge, StringJoiner::toString);
	}

	@Test
	public void test_uncheck_exception_thrown_by_method() {
		Class clazz1 = LambdaUnchecker.uncheckedFunction(() -> Class.forName("java.lang.String"));

		Class clazz2 = LambdaUnchecker.uncheckedApply(Class::forName, "java.lang.String");
	}

	@Test(expected = ClassNotFoundException.class)
	public void test_if_correct_exception_is_still_thrown_by_method() {
		Class clazz3 = LambdaUnchecker.uncheckedApply(Class::forName, "INVALID");
	}
*/
}
