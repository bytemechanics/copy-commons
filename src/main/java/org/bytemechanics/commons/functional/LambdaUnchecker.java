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

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class to hide checked exceptions.
 * Thanks to: 
 * <ul>
 * <li>https://stackoverflow.com/questions/27644361/how-can-i-throw-checked-exceptions-from-inside-java-8-streams#27668305</li>
 * <li>http://www.philandstuff.com/2012/04/28/sneakily-throwing-checked-exceptions.html http://www.mail-archive.com/javaposse@googlegroups.com/msg05984.html</li>
 * <li>https://softwareengineering.stackexchange.com/questions/225931/workaround-for-java-checked-exceptions?newreg=ddf0dd15e8174af8ba52e091cf85688e</li>
 * <li>https://stackoverflow.com/questions/27644361/how-can-i-throw-checked-exceptions-from-inside-java-8-streams</li>
 * </ul>
 * @author not sure
 * @since 1.1.0
 */
public final class LambdaUnchecker {

	/**
	 * Functional interface for consumers with checked exceptions
	 * @param <T> consumer input type
	 * @param <E> checked exception
	 * @see Consumer
	 */
	@FunctionalInterface
	public interface ConsumerWithExceptions<T, E extends Exception> {
		/**
		 * @param t consumer input
		 * @throws E checked exception
		 * @see Consumer#accept(java.lang.Object) 
		 */
		void accept(T t) throws E;
	}

	/**
	 * Functional interface for biconsumers with checked exceptions
	 * @param <T> biconsumer first input type
	 * @param <U> biconsumer second input type
	 * @param <E> checked exception
	 * @see BiConsumer
	 */
	@FunctionalInterface
	public interface BiConsumerWithExceptions<T, U, E extends Exception> {
		/**
		 * @param t consumer input
		 * @param u biconsumer second
		 * @throws E checked exception
		 * @see BiConsumer#accept(java.lang.Object, java.lang.Object) 
		 */
		void accept(T t, U u) throws E;
	}

	/**
	 * Functional interface for functions with checked exceptions
	 * @param <T> function input type
	 * @param <R> function result type
	 * @param <E> checked exception
	 * @see Function
	 */
	@FunctionalInterface
	public interface FunctionWithExceptions<T, R, E extends Exception> {
		/**
		 * @param t consumer input
		 * @return function result
		 * @throws E checked exception
		 * @see Function#apply(java.lang.Object) 
		 */
		R apply(T t) throws E;
	}

	/**
	 * Functional interface for suppliers with checked exceptions
	 * @param <T> supplier result
	 * @param <E> checked exception
	 * @see Supplier
	 */
	@FunctionalInterface
	public interface SupplierWithExceptions<T, E extends Exception> {
		/**
		 * @return function result
		 * @throws E checked exception
		 * @see Supplier#get() 
		 */
		T get() throws E;
	}

	/**
	 * Functional interface for runnables with checked exceptions
	 * @param <E> checked exception
	 * @see Runnable
	 */
	@FunctionalInterface
	public interface RunnableWithExceptions<E extends Exception> {
		/**
		 * @throws E checked exception
		 * @see Runnable#run() 
		 */
		void run() throws E;
	}

	
	/**
	 * Wraps consumer lambda returning the consumer but hacking the compiler to avoid compilation check.
	 * Example:
	 * <code>.forEach(uncheckedConsumer(name -&gt; System.out.println(Class.forName(name))));</code>
	 * or 
	 * <code>.forEach(uncheckedConsumer(ClassNameUtil::println));</code>
	 * @param <T> consumer input type
	 * @param <E> checked exception
	 * @param _consumer consumer to wrap
	 * @return wrapped consumer that throws the exceptions produced without compilation check
	 * @see ConsumerWithExceptions
	 */
	public static <T, E extends Exception> Consumer<T> uncheckedConsumer(final ConsumerWithExceptions<T, E> _consumer) {
		return t -> {
			try {
				_consumer.accept(t);
			} catch (Exception e) {
				throwAsUnchecked(e);
			}
		};
	}

	/**
	 * Wraps consumer lambda returning the same one but silencing the exception
	 * Example:
	 * <code>.forEach(silencedConsumer(name -&gt; System.out.println(Class.forName(name))));</code>
	 * or 
	 * <code>.forEach(silencedConsumer(ClassNameUtil::println));</code>
	 * @param <T> consumer input type
	 * @param <E> checked exception
	 * @param _consumer consumer to wrap
	 * @return wrapped consumer that silence the exceptions produced without compilation check
	 * @see ConsumerWithExceptions
	 */
	public static <T, E extends Exception> Consumer<T> silencedConsumer(final ConsumerWithExceptions<T, E> _consumer) {
		return t -> {
			try {
				_consumer.accept(t);
			} catch (Exception e) {
				Logger.getLogger(LambdaUnchecker.class.getName())
						.log(Level.FINEST, null, e);
			}
		};
	}
	
	/**
	 * Accept consumer input but hacking the compiler to avoid compilation check.
	 * Example:
	 * <code>uncheckedAccept(name -&gt; System.out.println(Class.forName(name)),"java.util.List");</code>
	 * @param <T> consumer input type
	 * @param <E> checked exception
	 * @param _consumer consumer to wrap
	 * @param _input consumer input
	 * @see ConsumerWithExceptions
	 */
	public static <T, E extends Exception> void uncheckedAccept(final ConsumerWithExceptions<T, E> _consumer,final T _input) {
		uncheckedConsumer(_consumer)
				.accept(_input);
	}
	
	/**
	 * Accept consumer input but silencing the exception
	 * Example:
	 * <code>uncheckedAccept(name -&gt; System.out.println(Class.forName(name)),"java.util.List");</code>
	 * @param <T> consumer input type
	 * @param <E> checked exception
	 * @param _consumer consumer to wrap
	 * @param _input consumer input
	 * @see ConsumerWithExceptions
	 */
	public static <T, E extends Exception> void silencedAccept(final ConsumerWithExceptions<T, E> _consumer,final T _input) {
		silencedConsumer(_consumer)
				.accept(_input);
	}

	/**
	 * Wraps biconsumer lambda returning the same one but hacking the compiler to avoid compilation check.
	 * Example:
	 * <code>.forEach(uncheckedBiConsumer((name,value) -&gt; System.out.println(Class.forName(name)+value)));</code>
	 * @param <T> biconsumer first input type
	 * @param <U> biconsumer second input type
	 * @param <E> checked exception
	 * @param _biConsumer biconsumer to wrap
	 * @return wrapped biconsumer that throws the exceptions produced without compilation check
	 * @see BiConsumer
	 * @see BiConsumerWithExceptions
	 */
	public static <T, U, E extends Exception> BiConsumer<T, U> uncheckedBiConsumer(final BiConsumerWithExceptions<T, U, E> _biConsumer) {
		return (t, u) -> {
			try {
				_biConsumer.accept(t, u);
			} catch (Exception exception) {
				throwAsUnchecked(exception);
			}
		};
	}

	/**
	 * Wraps biconsumer lambda returning the same one but returning null when exception and silencing the exception
	 * Example:
	 * <code>.forEach(silencedBiConsumer((name,value) -&gt; System.out.println(Class.forName(name)+value)));</code>
	 * @param <T> biconsumer first input type
	 * @param <U> biconsumer second input type
	 * @param <E> checked exception
	 * @param _biConsumer biconsumer to wrap
	 * @return wrapped biconsumer that silence the exceptions produced without compilation check
	 * @see BiConsumer
	 * @see BiConsumerWithExceptions
	 */
	public static <T, U, E extends Exception> BiConsumer<T, U> silencedBiConsumer(final BiConsumerWithExceptions<T, U, E> _biConsumer) {
		return (t, u) -> {
			try {
				_biConsumer
						.accept(t, u);
			} catch (Exception e) {
				Logger.getLogger(LambdaUnchecker.class.getName())
						.log(Level.FINEST, null, e);
			}
		};
	}

	
	/**
	 * Accept consumer input but hacking the compiler to avoid compilation check.
	 * Example:
	 * <code>uncheckedAccept((name,value) -&gt; System.out.println(Class.forName(name)+value));</code>
	 * @param <T> biconsumer first input type
	 * @param <U> biconsumer second input type
	 * @param <E> checked exception
	 * @param _biConsumer biconsumer to wrap
	 * @param _firstInput biconsumer first input
	 * @param _secondInput biconsumer second input
	 * @see BiConsumer
	 * @see BiConsumerWithExceptions
	 */
	public static <T, U, E extends Exception> void uncheckedAccept(final BiConsumerWithExceptions<T, U, E> _biConsumer,final T _firstInput,final U _secondInput) {
		uncheckedBiConsumer(_biConsumer)
				.accept(_firstInput,_secondInput);
	}
	
	/**
	 * Accept consumer input but silencing the exception
	 * Example:
	 * <code>silencedAccept((name,value) -&gt; System.out.println(Class.forName(name)+value));</code>
	 * @param <T> biconsumer first input type
	 * @param <U> biconsumer second input type
	 * @param <E> checked exception
	 * @param _biConsumer biconsumer to wrap
	 * @param _firstInput biconsumer first input
	 * @param _secondInput biconsumer second input
	 * @see BiConsumer
	 * @see BiConsumerWithExceptions
	 */
	public static <T, U, E extends Exception> void silencedAccept(final BiConsumerWithExceptions<T, U, E> _biConsumer,final T _firstInput,final U _secondInput) {
		silencedBiConsumer(_biConsumer)
				.accept(_firstInput,_secondInput);
	}
	
	/**
	 * Wraps function lambda returning the same one but hacking the compiler to avoid compilation check.
	 * Example:
	 * <code>.map(uncheckedFunction(name -&gt; Class.forName(name)))</code>
	 * or
	 * <code>.map(uncheckedFunction(Class::forName))</code>
	 * @param <T> function input type
	 * @param <R> function result type
	 * @param <E> checked exception
	 * @param _function biconsumer to wrap
	 * @return wrapped function that throws the exceptions produced without compilation check
	 * @see Function
	 * @see FunctionWithExceptions
	 */
	public static <T, R, E extends Exception> Function<T, R> uncheckedFunction(final FunctionWithExceptions<T, R, E> _function) {
		return t -> {
			try {
				return _function.apply(t);
			} catch (Exception e) {
				throwAsUnchecked(e);
				return null;
			}
		};
	}
	/**
	 * Wraps function lambda returning the same one but returning null when exception and silencing the exception
	 * Example:
	 * <code>.map(silencedFunction(name -&gt; Class.forName(name)))</code>
	 * or
	 * <code>.map(silencedFunction(Class::forName))</code>
	 * @param <T> function input type
	 * @param <R> function result type
	 * @param <E> checked exception
	 * @param _function biconsumer to wrap
	 * @return wrapped function that silence the exceptions produced without compilation check
	 * @see Function
	 * @see FunctionWithExceptions
	 */
	public static <T, R, E extends Exception> Function<T, R> silencedFunction(final FunctionWithExceptions<T, R, E> _function) {
		return t -> {
			try {
				return _function.apply(t);
			} catch (Exception e) {
				Logger.getLogger(LambdaUnchecker.class.getName())
						.log(Level.FINEST, null, e);
				return null;
			}
		};
	}
	/**
	 * Apply function returning the result but hacking the compiler to avoid compilation check.
	 * Example:
	 * <code>uncheckedFunction(name -&gt; Class.forName(name))</code>
	 * or
	 * <code>uncheckedFunction(Class::forName)</code>
	 * @param <T> function input type
	 * @param <R> function result type
	 * @param <E> checked exception
	 * @param _function biconsumer to wrap
	 * @param _input function input
	 * @return function result
	 * @see Function
	 * @see FunctionWithExceptions
	 */
	public static <T, R, E extends Exception> R uncheckedApply(final FunctionWithExceptions<T, R, E> _function,final T _input) {
		return uncheckedFunction(_function)
				.apply(_input);
	}
	
	/**
	 * Apply function returning the result but returning null when exception and silencing the exception and in this case returning null
	 * Example:
	 * <code>silencedFunction(name -&gt; Class.forName(name))</code>
	 * or
	 * <code>silencedFunction(Class::forName)</code>
	 * @param <T> function input type
	 * @param <R> function result type
	 * @param <E> checked exception
	 * @param _function biconsumer to wrap
	 * @param _input function input
	 * @return function result or null if exception
	 * @see Function
	 * @see FunctionWithExceptions
	 */
	public static <T, R, E extends Exception> R silencedApply(final FunctionWithExceptions<T, R, E> _function,final T _input) {
		return silencedFunction(_function)
				.apply(_input);
	}
	
	/**
	 * Wraps supplier lambda returning the same one but hacking the compiler to avoid compilation check.
	 * Example:
	 * <code>uncheckedSupplier(() -&gt; new StringJoiner(new String(new byte[]{77, 97, 114, 107}, "UTF-8"))),</code>
	 * @param <T> supplier result
	 * @param <E> checked exception
	 * @param _supplier supplier to wrap
	 * @return wrapped function that throws the exceptions produced without compilation check
	 * @see Supplier
	 * @see SupplierWithExceptions
	 */
	public static <T, E extends Exception> Supplier<T> uncheckedSupplier(final SupplierWithExceptions<T, E> _supplier) {
		return () -> {
			try {
				return _supplier.get();
			} catch (Exception exception) {
				throwAsUnchecked(exception);
				return null;
			}
		};
	}
	/**
	 * Wraps supplier lambda returning the same one but returning null when exception and silencing the exception
	 * Example:
	 * <code>silencedSupplier(() -&gt; new StringJoiner(new String(new byte[]{77, 97, 114, 107}, "UTF-8"))),</code>
	 * @param <T> supplier result
	 * @param <E> checked exception
	 * @param _supplier supplier to wrap
	 * @return wrapped function that silence the exceptions produced without compilation check
	 * @see Supplier
	 * @see SupplierWithExceptions
	 */
	public static <T, E extends Exception> Supplier<T> silencedSupplier(final SupplierWithExceptions<T, E> _supplier) {
		return () -> {
			try {
				return _supplier.get();
			} catch (Exception e) {
				Logger.getLogger(LambdaUnchecker.class.getName())
						.log(Level.FINEST, null, e);
				return null;
			}
		};
	}
	/**
	 * Get supplier returning the supplier result but hacking the compiler to avoid compilation check.
	 * Example:
	 * <code>uncheckedGet(() -&gt; new StringJoiner(new String(new byte[]{77, 97, 114, 107}, "UTF-8")))</code>
	 * @param <T> supplier result
	 * @param <E> checked exception
	 * @param _supplier supplier to wrap
	 * @return supplier result
	 * @see Supplier
	 * @see SupplierWithExceptions
	 */
	public static <T, E extends Exception> T uncheckedGet(final SupplierWithExceptions<T, E> _supplier) {
		return uncheckedSupplier(_supplier)
					.get();
	}
	/**
	 * Get supplier returning the supplier result or null if exception
	 * Example:
	 * <code>silencedGet(() -&gt; new StringJoiner(new String(new byte[]{77, 97, 114, 107}, "UTF-8")))</code>
	 * @param <T> supplier result
	 * @param <E> checked exception
	 * @param _supplier supplier to wrap
	 * @return supplier result or get if exception
	 * @see Supplier
	 * @see SupplierWithExceptions
	 */
	public static <T, E extends Exception> T silencedGet(final SupplierWithExceptions<T, E> _supplier) {
		return silencedSupplier(_supplier)
					.get();
	}

	
	/**
	 * Wraps Runnable lambda returning the same one but hacking the compiler to avoid compilation check.
	 * Example:
	 * <code>uncheckedSupplier(() -&gt; new StringJoiner(new String(new byte[]{77, 97, 114, 107}, "UTF-8"))),</code>
	 * @param _runnable runnable to wrap
	 * @return wrapped runnable that throws the exceptions produced without compilation check
	 * @see Runnable
	 * @see RunnableWithExceptions
	 */
	public static Runnable uncheckedRunnable(final RunnableWithExceptions _runnable) {
		return () -> {
			try {
				_runnable.run();
			} catch (Exception exception) {
				throwAsUnchecked(exception);
			}
		};
	}
	/**
	 * Wraps Runnable lambda returning the same one but returning null when exception and silencing the exception
	 * Example:
	 * <code>silencedSupplier(() -&gt; new StringJoiner(new String(new byte[]{77, 97, 114, 107}, "UTF-8"))),</code>
	 * @param _runnable runnable to wrap
	 * @return wrapped runnable that silence the exceptions produced without compilation check
	 * @see Runnable
	 * @see RunnableWithExceptions
	 */
	public static Runnable silencedRunnable(final RunnableWithExceptions _runnable) {
		return () -> {
			try {
				_runnable.run();
			} catch (Exception e) {
				Logger.getLogger(LambdaUnchecker.class.getName())
						.log(Level.FINEST, null, e);
			}
		};
	}
	/**
	 * Run Runnable but hacking the compiler to avoid compilation check.
	 * Example:
	 * <code>uncheckedGet(() -&gt; new StringJoiner(new String(new byte[]{77, 97, 114, 107}, "UTF-8")))</code>
	 * @param _runnable runnable to wrap
	 * @see Supplier
	 * @see SupplierWithExceptions
	 */
	public static void uncheckedRun(final RunnableWithExceptions _runnable) {
		uncheckedRunnable(_runnable)
					.run();
	}
	/**
	 * Run Runnable but silencing any exception
	 * Example:
	 * <code>silencedGet(() -&gt; new StringJoiner(new String(new byte[]{77, 97, 114, 107}, "UTF-8")))</code>
	 * @param _runnable runnable to wrap
	 * @see Supplier
	 * @see SupplierWithExceptions
	 */
	public static void silencedRun(final RunnableWithExceptions _runnable) {
		silencedRunnable(_runnable)
					.run();
	}
	
	/**
	 * Converts checked exception to throwable in order to hack compiler to avoid compilation time exceptions check
	 * @param <E> checked exception
	 * @param _exception exception to silencedSupplier
	 */
	@SuppressWarnings("unchecked")
	private static <E extends Throwable> void throwAsUnchecked(Exception _exception) throws E {
		throw (E) _exception;
	}
}
