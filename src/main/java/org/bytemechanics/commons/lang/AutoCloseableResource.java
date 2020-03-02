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

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * This class intend to convert any resource that needs to be closed but does not implement AutoCloseable to a Closeable resource in order to be able to use into a Try-With-Resource structure
 * @see AutoCloseable
 * @author afarre
 * @since 1.4.0
 */
public class AutoCloseableResource implements AutoCloseable{

	private final Object instance;
	private final Consumer<Object> consumer;
	
	/**
	 * Builds a Closeable resource from it's supplier and consumer. Supplier will be executed during instantiation and consumer will be executed during close()
	 * @param <TYPE> Type to convert to closeable resource
	 * @param _try supplier to execute in this constructor
	 * @param _close consumer to be executed during close
	 */
	@SuppressWarnings("unchecked")
	public <TYPE> AutoCloseableResource(final Supplier<TYPE> _try,final Consumer<TYPE> _close){
		this.instance=_try.get();
		this.consumer=(Consumer<Object>)_close;
	}
	/**
	 * Builds a Closeable resource with the given instance the _try Runnable will be executed during constructor and the _close one during close() method
	 * @param _try Runnable to execute in this constructor
	 * @param _close Runnable to be executed during close
	 */
	public AutoCloseableResource(final Runnable _try,final Runnable _close){
		this.instance=null;
		_try.run();
		this.consumer=inst -> _close.run();
	}
	
	
	/** @see AutoCloseable#close() */
	@Override
	public void close() throws Exception {
		consumer.accept(this.instance);
	}
}
