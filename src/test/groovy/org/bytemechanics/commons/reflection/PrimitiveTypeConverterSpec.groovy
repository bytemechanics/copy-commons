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

package org.bytemechanics.commons.reflection

import java.util.logging.*
import spock.lang.Specification;
import spock.lang.Unroll
import java.util.logging.*
import java.lang.reflect.*

/**
 *
 * @author afarre
 */
class PrimitiveTypeConverterSpec extends Specification{
	
	def setupSpec(){
		println(">>>>> PrimitiveTypeConverterSpec >>>> setupSpec")
		final InputStream inputStream = PrimitiveTypeConverter.class.getResourceAsStream("/logging.properties");
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

	@Unroll
	def "Converting primitive #primitive should return its corresponding object class #clazz"(){
		println(">>>>> PrimitiveTypeConverterSpec >>>> Converting primitive $primitive should return its corresponding object class $clazz")

		when:
			def actual=PrimitiveTypeConverter.convert(primitive)

		then:
			actual!=null
			actual.equals(clazz)
			
		where:
			primitive		| clazz
			void.class		| Void.class
			byte.class		| Byte.class
			boolean.class	| Boolean.class
			char.class		| Character.class
			short.class		| Short.class
			int.class		| Integer.class
			long.class		| Long.class
			double.class	| Double.class
			float.class		| Float.class
	}

	def "Try to convert a null primitive shoud raise a ClassCastExeption"(){
		println(">>>>> PrimitiveTypeConverterSpec >>>> Try to convert a null primitive shoud raise a ClassCastExeption")

		when:
			PrimitiveTypeConverter.convert(null)

		then:
			def e=thrown(NullPointerException.class)
			e.getMessage()=="Unable to get class from null _primitiveClass"
	}

	def "Try to convert an unnexistent primitive #primitive shoud return the same class"(){
		println(">>>>> PrimitiveTypeConverterSpec >>>> Try to convert an unnexistent primitive $primitive shoud return the same class")

		when:
			def actual=PrimitiveTypeConverter.convert(primitive)

		then:
			actual==primitive
			
		where:
			primitive << [Integer.class]
	}
}

