/*
 * Copyright 2017 afarre.
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

import org.bytemechanics.commons.functional.*
import spock.lang.*
import spock.lang.Specification
import java.util.function.*
import java.text.*

/**
 * @author afarre
 */
class LambdaUnckeckerSpec extends Specification {

	def "When uncheck Consumer with checked exception shouldn't be necessary any exception especified"(){
		println(">>>>> LambdaUnckeckerSpec >>>> When uncheck Consumer with checked exception shouldn't be necessary any exception especified")
		setup:
			LambdaUnckeckerDummie dummie=new LambdaUnckeckerDummie()
			dummie.consumer_with_checked_exceptions_uncheck()
	}
	def "When uncheck Consumer with checked exception shouldn't be necessary any exception especified but still launch an exception"(){
		println(">>>>> LambdaUnckeckerSpec >>>> When uncheck Consumer with checked exception shouldn't be necessary any exception especified but still launch an exception")
		setup:
			LambdaUnckeckerDummie dummie=new LambdaUnckeckerDummie()

		when: 
			dummie.consumer_with_checked_exceptions_uncheck_still_thrown()
			
		then:
			thrown(ClassNotFoundException)
	}
	def "When silence Consumer with checked exception shouldn't be necessary any exception especified"(){
		println(">>>>> LambdaUnckeckerSpec >>>> When silence Consumer with checked exception shouldn't be necessary any exception especified")
		setup:
			LambdaUnckeckerDummie dummie=new LambdaUnckeckerDummie()
			dummie.consumer_with_checked_exceptions_silenced()
	}
	def "When silence Consumer with checked exception shouldn't be necessary any exception especified and never will launch an exception"(){
		println(">>>>> LambdaUnckeckerSpec >>>> When silence Consumer with checked exception shouldn't be necessary any exception especified and never will launch an exception")
		setup:
			LambdaUnckeckerDummie dummie=new LambdaUnckeckerDummie()
			dummie.consumer_with_checked_exceptions_silenced_not_thrown()
	}

	
	def "When uncheck BiConsumer with checked exception shouldn't be necessary any exception especified"(){
		println(">>>>> LambdaUnckeckerSpec >>>> When uncheck BiConsumer with checked exception shouldn't be necessary any exception especified")
		setup:
			LambdaUnckeckerDummie dummie=new LambdaUnckeckerDummie()
			dummie.biConsumer_with_checked_exceptions_uncheck()
	}
	def "When uncheck BiConsumer with checked exception shouldn't be necessary any exception especified but still launch an exception"(){
		println(">>>>> LambdaUnckeckerSpec >>>> When uncheck BiConsumer with checked exception shouldn't be necessary any exception especified but still launch an exception")
		setup:
			LambdaUnckeckerDummie dummie=new LambdaUnckeckerDummie()

		when: 
			dummie.biConsumer_with_checked_exceptions_uncheck_still_thrown()
			
		then:
			thrown(ParseException)
	}
	def "When silence BiConsumer with checked exception shouldn't be necessary any exception especified"(){
		println(">>>>> LambdaUnckeckerSpec >>>> When silence BiConsumer with checked exception shouldn't be necessary any exception especified")
		setup:
			LambdaUnckeckerDummie dummie=new LambdaUnckeckerDummie()
			dummie.biConsumer_with_checked_exceptions_silenced()
	}
	def "When silence BiConsumer with checked exception shouldn't be necessary any exception especified and never will launch an exception"(){
		println(">>>>> LambdaUnckeckerSpec >>>> When silence BiConsumer with checked exception shouldn't be necessary any exception especified and never will launch an exception")
		setup:
			LambdaUnckeckerDummie dummie=new LambdaUnckeckerDummie()
			dummie.biConsumer_with_checked_exceptions_silenced_not_thrown()
	}

	
	def "When uncheck Function with checked exception shouldn't be necessary any exception especified"(){
		println(">>>>> LambdaUnckeckerSpec >>>> When uncheck Function with checked exception shouldn't be necessary any exception especified")
		setup:
			LambdaUnckeckerDummie dummie=new LambdaUnckeckerDummie()
			dummie.function_with_checked_exceptions_uncheck()
	}
	def "When uncheck Function with checked exception shouldn't be necessary any exception especified but still launch an exception"(){
		println(">>>>> LambdaUnckeckerSpec >>>> When uncheck Function with checked exception shouldn't be necessary any exception especified but still launch an exception")
		setup:
			LambdaUnckeckerDummie dummie=new LambdaUnckeckerDummie()

		when: 
			dummie.function_with_checked_exceptions_uncheck_still_thrown()
			
		then:
			thrown(ParseException)
	}
	def "When silence Function with checked exception shouldn't be necessary any exception especified"(){
		println(">>>>> LambdaUnckeckerSpec >>>> When silence Function with checked exception shouldn't be necessary any exception especified")
		setup:
			LambdaUnckeckerDummie dummie=new LambdaUnckeckerDummie()
			dummie.function_with_checked_exceptions_silenced()
	}
	def "When silence Function with checked exception shouldn't be necessary any exception especified and never will launch an exception"(){
		println(">>>>> LambdaUnckeckerSpec >>>> When silence Function with checked exception shouldn't be necessary any exception especified and never will launch an exception")
		setup:
			LambdaUnckeckerDummie dummie=new LambdaUnckeckerDummie()
			dummie.function_with_checked_exceptions_silenced_not_thrown()
	}

	
	def "When uncheck Supplier with checked exception shouldn't be necessary any exception especified"(){
		println(">>>>> LambdaUnckeckerSpec >>>> When uncheck Supplier with checked exception shouldn't be necessary any exception especified")
		setup:
			LambdaUnckeckerDummie dummie=new LambdaUnckeckerDummie()
			dummie.supplier_with_checked_exceptions_uncheck()
	}
	def "When uncheck Supplier with checked exception shouldn't be necessary any exception especified but still launch an exception"(){
		println(">>>>> LambdaUnckeckerSpec >>>> When uncheck Supplier with checked exception shouldn't be necessary any exception especified but still launch an exception")
		setup:
			LambdaUnckeckerDummie dummie=new LambdaUnckeckerDummie()

		when: 
			dummie.supplier_with_checked_exceptions_uncheck_still_thrown()
			
		then:
			thrown(ParseException)
	}
	def "When silence Supplier with checked exception shouldn't be necessary any exception especified"(){
		println(">>>>> LambdaUnckeckerSpec >>>> When silence Supplier with checked exception shouldn't be necessary any exception especified")
		setup:
			LambdaUnckeckerDummie dummie=new LambdaUnckeckerDummie()
			dummie.supplier_with_checked_exceptions_silenced()
	}
	def "When silence Supplier with checked exception shouldn't be necessary any exception especified and never will launch an exception"(){
		println(">>>>> LambdaUnckeckerSpec >>>> When silence Supplier with checked exception shouldn't be necessary any exception especified and never will launch an exception")
		setup:
			LambdaUnckeckerDummie dummie=new LambdaUnckeckerDummie()
			dummie.supplier_with_checked_exceptions_silenced_not_thrown()
	}

	
	def "When uncheck Runnable with checked exception shouldn't be necessary any exception especified"(){
		println(">>>>> LambdaUnckeckerSpec >>>> When uncheck Runnable with checked exception shouldn't be necessary any exception especified")
		setup:
			LambdaUnckeckerDummie dummie=new LambdaUnckeckerDummie()
			dummie.runnable_with_checked_exceptions_uncheck()
	}
	def "When uncheck Runnable with checked exception shouldn't be necessary any exception especified but still launch an exception"(){
		println(">>>>> LambdaUnckeckerSpec >>>> When uncheck Runnable with checked exception shouldn't be necessary any exception especified but still launch an exception")
		setup:
			LambdaUnckeckerDummie dummie=new LambdaUnckeckerDummie()

		when: 
			dummie.runnable_with_checked_exceptions_uncheck_still_thrown()
			
		then:
			thrown(ParseException)
	}
	def "When silence Runnable with checked exception shouldn't be necessary any exception especified"(){
		println(">>>>> LambdaUnckeckerSpec >>>> When silence Runnable with checked exception shouldn't be necessary any exception especified")
		setup:
			LambdaUnckeckerDummie dummie=new LambdaUnckeckerDummie()
			dummie.runnable_with_checked_exceptions_silenced()
	}
	def "When silence Runnable with checked exception shouldn't be necessary any exception especified and never will launch an exception"(){
		println(">>>>> LambdaUnckeckerSpec >>>> When silence Runnable with checked exception shouldn't be necessary any exception especified and never will launch an exception")
		setup:
			LambdaUnckeckerDummie dummie=new LambdaUnckeckerDummie()
			dummie.runnable_with_checked_exceptions_silenced_not_thrown()
	}
}