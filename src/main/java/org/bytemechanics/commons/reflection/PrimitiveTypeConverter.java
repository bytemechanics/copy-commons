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
package org.bytemechanics.commons.reflection;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * This enumerated allows to convert primitive types to it's corresponding class type
 * int - Integer, float - Float...
 * Usage: Class objectClass=PrimitiveTypeConverter.convert(primitiveClass)
 * @author afarre
 * @since 1.3.0
 */
public enum PrimitiveTypeConverter {
	
	VOID(void.class,Void.class),
	BYTE(byte.class,Byte.class),
	BOOLEAN(boolean.class,Boolean.class),
	INTEGER(int.class,Integer.class),
	SHORT(short.class,Short.class),
	LONG(long.class,Long.class),
	FLOAT(float.class,Float.class),
	DOUBLE(double.class,Double.class),
	CHAR(char.class,Character.class),
	;
	
	public final Class primitiveClass;
	public final Class objectClass;
	
	PrimitiveTypeConverter(final Class _primitiveClass,final Class _objectClass){
		this.primitiveClass=_primitiveClass;
		this.objectClass=_objectClass;
	}
	
	
	/**
	 * Try to convert primitive types to it's corresponding class type if are primitives 
	 * @param _primitiveClass primitive to convert
	 * @return it's correponding primitive class or the same class if it's not primitive
	 * @throws NullPointerException if the class provided is null
	 */
	public static final Class convert(final Class _primitiveClass){
		
		return Optional.ofNullable(_primitiveClass)
							.map(candidate -> Stream.of(PrimitiveTypeConverter.values())
														.filter(value -> candidate.isPrimitive())
														.filter(value -> value.primitiveClass.equals(candidate))
														.map(value -> value.objectClass)
														.findAny()
															.orElse(_primitiveClass))
							.orElseThrow(() -> new NullPointerException("Unable to get class from null _primitiveClass"));
	}
}
