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

package org.bytemechanics.commons.functional

import org.bytemechanics.commons.functional.*
import spock.lang.*
import spock.lang.Specification
import java.util.function.*
import java.text.*
import java.io.*
import java.util.logging.*

/**
 * @author afarre
 */
class TupleSpec extends Specification {

	def setupSpec(){
		println(">>>>> TupleSpec >>>> setupSpec")
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

	@Unroll
	def "When constructor is called with left=#left and right=#right then the same left and right is recovered"(){
		println(">>>>> TupleSpec >>>> When constructor is called with left=$left and right=$right then the same left and right is recovered")
		when:
			Tuple tuple=Tuple.of(left,right)

		then:
			tuple.left()==left
			tuple.getLeft()==left
			tuple.right()==right
			tuple.getRight()==right
		
		where:
			left					| right
			2						| "left-value"
			"right-value"			| 2.2d
			null					| "left-value-not-null"
			"right-value-not-null"	| null
			null					| null
	}

	@Unroll
	def "When two instances with the same left=#left and right=#right values are created then hashcode must be the same"(){
		println(">>>>> TupleSpec >>>> When two instances with the same left=$left and right=$right values are created then hashcode must be the same")
		when:
			Tuple tuple=Tuple.of(left,right)
			Tuple tuple2=Tuple.of(left,right)

		then:
			tuple.hashCode()==tuple2.hashCode()		

		where:
			left					| right
			2						| "left-value"
			"right-value"			| 2.2d
			null					| "left-value-not-null"
			"right-value-not-null"	| null
			null					| null
	}

	@Unroll
	def "When two instances with the same left=#left and right=#right values are created then equals must be true"(){
		println(">>>>> TupleSpec >>>> When two instances with the same left=$left and right=$right values are created then equals must be true")
		when:
			Tuple tuple=Tuple.of(left,right)
			Tuple tuple2=Tuple.of(left,right)

		then:
			tuple.equals(tuple2)		
			tuple2.equals(tuple)		

		where:
			left					| right
			2						| "left-value"
			"right-value"			| 2.2d
			null					| "left-value-not-null"
			"right-value-not-null"	| null
			null					| null
	}

	@Unroll
	def "When two instances with the same left=#left and right=#right values shouldn't be the same instance"(){
		println(">>>>> TupleSpec >>>> When two instances with the same left=$left and right=$right values shouldn't be the same instance")
		when:
			Tuple tuple=Tuple.of(left,right)
			Tuple tuple2=Tuple.of(left,right)

		then:
			!tuple.is(tuple2)

		where:
			left					| right
			2						| "left-value"
			"right-value"			| 2.2d
			null					| "left-value-not-null"
			"right-value-not-null"	| null
			null					| null
	}

	@Unroll
	def "When replace left=#left value with newLeft=#newLeft then a new instance is created with newLeft value but keeping the original right=#right value"(){
		println(">>>>> TupleSpec >>>> When replace left=$left value with newLeft=$newLeft then a new instance is created with newLeft value but keeping the original right=$right value")
		when:
			Tuple tuple=Tuple.of(left,right)
			Tuple tuple2=tuple.left(newLeft)

		then:
			tuple.left()==left
			tuple.getLeft()==left
			tuple.right()==right
			tuple.getRight()==right
			!tuple.hashCode()!=tuple2.hashCode()
			!tuple.equals(tuple2)
			tuple!=tuple2
			tuple2.left()==newLeft
			tuple2.getLeft()==newLeft
			tuple2.right()==right
			tuple2.getRight()==right
		
		where:
			left					| newLeft		| right
			2						| 3				| "left-value"
			"right-value"			| "newVal"		| 2.2d
			null					| 2.2d			| "left-value-not-null"
			"right-value-not-null"	| 126			| null
			null					| "another_val"	| null
	}

	@Unroll
	def "When replace right=#right value with newRight=#newRight then a new instance is created with newRight value but keeping the original left=#left value"(){
		println(">>>>> TupleSpec >>>> When replace right=$right value with newRight=$newRight then a new instance is created with newRight value but keeping the original left=$left value")
		when:
			Tuple tuple=Tuple.of(left,right)
			Tuple tuple2=tuple.right(newRight)

		then:
			tuple.left()==left
			tuple.getLeft()==left
			tuple.right()==right
			tuple.getRight()==right
			!tuple.hashCode()!=tuple2.hashCode()
			!tuple.equals(tuple2)
			tuple!=tuple2
			tuple2.left()==left
			tuple2.getLeft()==left
			tuple2.right()==newRight
			tuple2.getRight()==newRight
		
		where:
			left					| right					| newRight		
			2						| "left-value"			| 3				
			"right-value"			| 2.2d					| "newVal"		
			null					| "left-value-not-null"	| 2.2d			
			"right-value-not-null"	| null					| 126			
			null					| null					| "another_val"	
	}

	@Unroll
	def "When replace left=#left with newLeft=#newLeft and right=#right value with newRight=#newRight then a new instance is created with newRight value and newLeft value"(){
		println(">>>>> TupleSpec >>>> When replace left=$left with newLeft=$newLeft and right=$right value with newRight=$newRight then a new instance is created with newRight value and newLeft value")
		when:
			Tuple tuple=Tuple.of(left,right)
			Tuple tuple2=tuple.with(newLeft,newRight)

		then:
			tuple.left()==left
			tuple.getLeft()==left
			tuple.right()==right
			tuple.getRight()==right
			!tuple.hashCode()!=tuple2.hashCode()
			!tuple.equals(tuple2)
			tuple!=tuple2
			tuple2.left()==newLeft
			tuple2.getLeft()==newLeft
			tuple2.right()==newRight
			tuple2.getRight()==newRight
		
		where:
			left					| newLeft		| right					| newRight		
			2						| 3				| "left-value"			| 10				
			"right-value"			| "newVal"		| 2.2d					| 1		
			null					| 2.2d			| "left-value-not-null"	| 'a'			
			"right-value-not-null"	| 126			| null					| "jjj"			
			null					| "another_val"	| null					| 124.0f
	}
}

