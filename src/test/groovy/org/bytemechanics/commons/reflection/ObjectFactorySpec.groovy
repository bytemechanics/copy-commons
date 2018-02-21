/*
 * Copyright 2017 Byte Mechanics.
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

import org.bytemechanics.commons.reflection.ObjectFactory;
import org.bytemechanics.commons.reflection.DummieServiceImpl;
import java.util.logging.*
import spock.lang.Specification;
import spock.lang.Unroll
import java.util.logging.*
import java.lang.reflect.*

/**
 *
 * @author afarre
 */
class ObjectFactorySpec extends Specification{
	
	def setupSpec(){
		println(">>>>> ObjectFactorySpec >>>> setupSpec")
		final InputStream inputStream = ObjectFactorySpec.class.getResourceAsStream("/logging.properties");
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
	
	
	def setup(){
		println(">>>>> ObjectFactorySpec >>>> setup")
		Handler ch = new ConsoleHandler();
		ch.setLevel(Level.ALL)
		Logger.getLogger("org.bytemechanics.service.repository.internal.ObjectFactory").addHandler(ch)
		Logger.getLogger("org.bytemechanics.service.repository.internal.ObjectFactory").setLevel(Level.ALL)		
	}
	
	@Unroll
	def "Calling of(#objectiveClass) should return an instance of ObjectFactory with instantiation objective #objectiveClass"(){
		println(">>>>> ObjectFactorySpec >>>> Calling of($objectiveClass) should return an instance of ObjectFactory with instantiation objective $objectiveClass")

		when:
			def objectFactory=ObjectFactory.of(objectiveClass)

		then:
			objectFactory!=null
			objectFactory instanceof ObjectFactory
			objectFactory.getToInstantiate()!=null
			objectFactory.getToInstantiate()==objectiveClass
			
		where:
			objectiveClass << [DummieServiceImpl.class]
	}
	
	@Unroll
	def "Calling with(#arguments) over an existent ObjectFactory of #objectiveClass should return an instance of ObjectFactory with instantiation objective #objectiveClass with #arguments constructor"(){
		println(">>>>> ObjectFactorySpec >>>> Calling with($arguments) over an existent ObjectFactory of $objectiveClass should return an instance of ObjectFactory with instantiation objective $objectiveClass with $arguments constructor")

		when:
			def objectFactory=ObjectFactory.of(objectiveClass)
										.with((Object[])arguments)

		then:
			objectFactory!=null
			objectFactory instanceof ObjectFactory
			objectFactory.getToInstantiate()!=null
			objectFactory.getToInstantiate()==objectiveClass
			objectFactory.getAttributes()!=null
			objectFactory.getAttributes()==arguments
			
		where:
			objectiveClass			| arguments
			DummieServiceImpl.class	| []
			DummieServiceImpl.class	| ["1arg-arg1"]
			DummieServiceImpl.class	| ["1arg-arg1",3,"3arg-arg2"]
	}
	
	@Unroll
	def "Search constructor over ObjectFactory instance of #objectiveClass with #arguments should return an optional of #constructor"(){
		println(">>>>> ObjectFactorySpec >>>> Search constructor over ObjectFactory instance of $objectiveClass with $arguments should return an optional of $constructor")

		when:
			def optionalConstructor=ObjectFactory.of(objectiveClass)
										.with((Object[])arguments)
										.findConstructor()

		then:
			optionalConstructor!=null
			optionalConstructor instanceof Optional
			optionalConstructor.isPresent()
			constructor==optionalConstructor.get()
			
		where:
			objectiveClass			| arguments						| constructor
			DummieServiceImpl.class	| []							| DummieServiceImpl.class.getConstructor()
			DummieServiceImpl.class	| ["1arg-arg1"]					| DummieServiceImpl.class.getConstructor((Class[])[String.class])
			DummieServiceImpl.class	| ["1arg-arg1",3,"3arg-arg2"]	| DummieServiceImpl.class.getConstructor((Class[])[String.class,int.class,String.class])
			DummieServiceImpl.class	| [null,3,"3arg-arg2"]			| DummieServiceImpl.class.getConstructor((Class[])[String.class,int.class,String.class])
			DummieServiceImpl.class	| ["1arg-arg1",3,null]			| DummieServiceImpl.class.getConstructor((Class[])[String.class,int.class,String.class])
			DummieServiceImpl.class	| ["1arg-arg1",null,"3arg-arg2"]| DummieServiceImpl.class.getConstructor((Class[])[String.class,int.class,String.class])
	}
	
	@Unroll
	def "when object factory builds a supplier of #supplierClass with #arguments should return a supplier of #supplierClass"(){
		println(">>>>> ObjectFactorySpec >>>> when object factory builds a supplier of $supplierClass with $arguments should return a supplier of $supplierClass")

		when:
			def supplier=ObjectFactory.of(supplierClass)
										.with((Object[])arguments)
										.supplier()
			def instance=(supplier!=null)? supplier.get().get() : null
			def args=(instance!=null)? [instance.getArg1(),instance.getArg2(),instance.getArg3()] : null

		then:
			supplier!=null
			instance!=null
			supplierClass.equals(instance.getClass())
			expected==args
			
		where:
			supplierClass			| arguments
			DummieServiceImpl.class	| []
			DummieServiceImpl.class	| ["1arg-arg1"]
			DummieServiceImpl.class	| ["1arg-arg1",3,"3arg-arg2"]
			expected=[(arguments.size()>0)? arguments[0] : "",(arguments.size()>1)? arguments[1] : 0,(arguments.size()>2)? arguments[2] : ""]			
	}

	@Unroll
	def "when object factory builds a supplier of #supplierClass with #arguments should return a empty optional and write a log"(){
		println(">>>>> ObjectFactorySpec >>>> when object factory builds a supplier of $supplierClass with $arguments should return a empty optional and write a log")

		when:
			def result=ObjectFactory.of(supplierClass)
										.with((Object[])arguments)
										.supplier()
										.get()

		then:
			result!=null
			!result.isPresent()
			
		where:
			supplierClass			| arguments
			DummieServiceImpl.class	| ["1arg-arg1",3,false,false]
			DummieServiceImpl.class	| ["1arg-arg1",3,24.3,false]
	}
	
	@Unroll
	def "When object factory builds a supplier of #supplierClass with #arguments that throws an exception should fail with #failure"(){
		println(">>>>> ObjectFactorySpec >>>> When object factory builds a supplier of $supplierClass with $arguments that throws an exception should fail with $failure")

		when:
			def result=ObjectFactory.of(supplierClass)
										.with((Object[])arguments)
										.supplier()
										.get()

		then:
			thrown(failure)
			
		where:
			supplierClass			| arguments								| failure
			DummieServiceImpl.class	| ["1arg-arg1",null,false,"3arg-arg2"]	| IllegalArgumentException.class
			DummieServiceImpl.class	| ["1arg-arg1",3,false,"3arg-arg2"]		| InvocationTargetException.class
	}
	
	@Unroll
	def "when object factory try to autobox #primitive primitive returns #object class"(){
		println(">>>>> ObjectFactorySpec >>>> when object factory try to autobox $primitive primitive returns $object class")

		when:
			def result=ObjectFactory.of(DummieServiceImpl.class)
										.autobox(primitive)

		then:
			result!=null
			result==object
			
		where:
			primitive		| object
			void.class		| Void.class
			byte.class		| Byte.class
			boolean.class	| Boolean.class
			char.class		| Character.class
			short.class		| Short.class
			int.class		| Integer.class
			long.class		| Long.class
			float.class		| Float.class
			double.class	| Double.class
			Integer.class	| Integer.class
			String.class	| String.class
	}
}

