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
package org.bytemechanics.commons.string;

import spock.lang.*
import spock.lang.Specification
import org.bytemechanics.commons.string.*
import java.util.function.*
import java.io.*
import java.util.logging.*

/**
 * @author afarre
 */
class SimpleFormatSpec extends Specification {

	def setupSpec(){
		println(">>>>> GenericTextParserSpec >>>> setupSpec")
		final InputStream inputStream = SimpleFormat.class.getResourceAsStream("/logging.properties");
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
	def "When #message is formatted with #arguments result should be #result"(){
		println(">>>>> SimpleFormatSpec >>>> When $message is formatted with $arguments result should be $result")
		when:
			def String actual=SimpleFormat.format(message,(Object[])arguments);
			
		then:
			actual!=null
			actual==result
		
		where:
			message							| arguments					| result
 			"message without args"			| []						| "message without args"
			"message without args"			| ["fsdf"]					| "message without args"
			"message without args"			| [1]						| "message without args"
			"message with 1:{} arg"			| []						| "message with 1:null arg"
			"message with 1:{} arg"			| ["fsdf"]					| "message with 1:fsdf arg"
			"message with 1:{} arg"			| [1]						| "message with 1:1 arg"
			"message with 1:{} arg"			| ["fsdf",2]				| "message with 1:fsdf arg"
			"message with 1:{} arg"			| ["fsdf","fsdfsd"]			| "message with 1:fsdf arg"
			"message with arg 1:{}"			| []						| "message with arg 1:null"
			"message with arg 1:{}"			| ["fsdf"]					| "message with arg 1:fsdf"
			"message with arg 1:{}"			| [1]						| "message with arg 1:1"
			"message with arg 1:{}"			| ["fsdf",2]				| "message with arg 1:fsdf"
			"message with arg 1:{}"			| ["fsdf","fsdfsd"]			| "message with arg 1:fsdf"
			"message with arg 1:{},2:{}"	| []						| "message with arg 1:null,2:null"
			"message with arg 1:{},2:{}"	| ["fsdf"]					| "message with arg 1:fsdf,2:null"
			"message with arg 1:{},2:{}"	| [1]						| "message with arg 1:1,2:null"
			"message with arg 1:{},2:{}"	| ["fsdf",2]				| "message with arg 1:fsdf,2:2"
			"message with arg 1:{},2:{}"	| ["fsdf","fsdfsd"]			| "message with arg 1:fsdf,2:fsdfsd"
			"{} message with arg 1:,2:{}"	| []						| "null message with arg 1:,2:null"
			"{} message with arg 2:{}"		| ["fsdf"]					| "fsdf message with arg 2:null"
			"{} message with arg 2:{}"		| [1]						| "1 message with arg 2:null"
			"{} message with arg 2:{}"		| ["fsdf",2]				| "fsdf message with arg 2:2"
			"{} message with arg 2:{}"		| ["fsdf","fsdfsd"]			| "fsdf message with arg 2:fsdfsd"
			"message with arg 1:{},2:{}"	| []						| "message with arg 1:null,2:null"
			"message with arg 1:{},2:{}"	| ["fsdf {}"]				| "message with arg 1:fsdf {},2:null"
			"message with arg 1:{},2:{}"	| [1]						| "message with arg 1:1,2:null"
			"message with arg 1:{},2:{}"	| ["fsdf {}",2]				| "message with arg 1:fsdf {},2:2"
			"message with arg 1:{},2:{}"	| ["fsdf {}","fsdfsd {}"]	| "message with arg 1:fsdf {},2:fsdfsd {}"
			"{}{}"							| ["par1"]					| "par1null"						
			"{}{}"							| ["par1","par2"]			| "par1par2"						
			"{}{}"							| ["par1","par2","par3"]	| "par1par2"						
			"{}{} text"						| ["par1"]					| "par1null text"					
			"{}{} text"						| ["par1","par2"]			| "par1par2 text"					
			"{}{} text"						| ["par1","par2","par3"]	| "par1par2 text"					
			"text {}{}"						| ["par1"]					| "text par1null"					
			"text {}{}"						| ["par1","par2"]			| "text par1par2"					
			"text {}{}"						| ["par1","par2","par3"]	| "text par1par2"					
			"text {}{} text"				| ["par1"]					| "text par1null text"			
			"text {}{} text"				| ["par1","par2"]			| "text par1par2 text"			
			"text {}{} text"				| ["par1","par2","par3"]	| "text par1par2 text"			
			"text {}{}{} text"				| ["par1"]					| "text par1nullnull text"			
			"text {}{}{} text"				| ["par1","par2"]			| "text par1par2null text"			
			"text {}{}{} text"				| ["par1","par2","par3"]	| "text par1par2par3 text"			
			"text {}{}{}{} text"			| ["p1"]					| "text p1nullnullnull text"			
			"text {}{}{}{} text"			| ["p1","p2"]				| "text p1p2nullnull text"			
			"text {}{}{}{} text"			| ["p1","p2","p3"]			| "text p1p2p3null text"			
			"text {}{}{}{} text"			| ["p1","p2","p3","p4"]		| "text p1p2p3p4 text"			
	}

	@Unroll
	def "When #message is supplied with #arguments result should be #result"(){
		println(">>>>> SimpleFormatSpec >>>> When $message is supplied with $arguments result should be $result")
		when:
			def Supplier<String> actual=SimpleFormat.supplier(message,(Object[])arguments);
			
		then:
			actual!=null
			actual.get()!=null
			actual.get()==result
		
		where:
			message							| arguments					| result
 			"message without args"			| []						| "message without args"
			"message without args"			| ["fsdf"]					| "message without args"
			"message without args"			| [1]						| "message without args"
			"message with 1:{} arg"			| []						| "message with 1:null arg"
			"message with 1:{} arg"			| ["fsdf"]					| "message with 1:fsdf arg"
			"message with 1:{} arg"			| [1]						| "message with 1:1 arg"
			"message with 1:{} arg"			| ["fsdf",2]				| "message with 1:fsdf arg"
			"message with 1:{} arg"			| ["fsdf","fsdfsd"]			| "message with 1:fsdf arg"
			"message with arg 1:{}"			| []						| "message with arg 1:null"
			"message with arg 1:{}"			| ["fsdf"]					| "message with arg 1:fsdf"
			"message with arg 1:{}"			| [1]						| "message with arg 1:1"
			"message with arg 1:{}"			| ["fsdf",2]				| "message with arg 1:fsdf"
			"message with arg 1:{}"			| ["fsdf","fsdfsd"]			| "message with arg 1:fsdf"
			"message with arg 1:{},2:{}"	| []						| "message with arg 1:null,2:null"
			"message with arg 1:{},2:{}"	| ["fsdf"]					| "message with arg 1:fsdf,2:null"
			"message with arg 1:{},2:{}"	| [1]						| "message with arg 1:1,2:null"
			"message with arg 1:{},2:{}"	| ["fsdf",2]				| "message with arg 1:fsdf,2:2"
			"message with arg 1:{},2:{}"	| ["fsdf","fsdfsd"]			| "message with arg 1:fsdf,2:fsdfsd"
			"{} message with arg 1:,2:{}"	| []						| "null message with arg 1:,2:null"
			"{} message with arg 2:{}"		| ["fsdf"]					| "fsdf message with arg 2:null"
			"{} message with arg 2:{}"		| [1]						| "1 message with arg 2:null"
			"{} message with arg 2:{}"		| ["fsdf",2]				| "fsdf message with arg 2:2"
			"{} message with arg 2:{}"		| ["fsdf","fsdfsd"]			| "fsdf message with arg 2:fsdfsd"
			"message with arg 1:{},2:{}"	| []						| "message with arg 1:null,2:null"
			"message with arg 1:{},2:{}"	| ["fsdf {}"]				| "message with arg 1:fsdf {},2:null"
			"message with arg 1:{},2:{}"	| [1]						| "message with arg 1:1,2:null"
			"message with arg 1:{},2:{}"	| ["fsdf {}",2]				| "message with arg 1:fsdf {},2:2"
			"message with arg 1:{},2:{}"	| ["fsdf {}","fsdfsd {}"]	| "message with arg 1:fsdf {},2:fsdfsd {}"
			"{}{}"							| ["par1"]					| "par1null"						
			"{}{}"							| ["par1","par2"]			| "par1par2"						
			"{}{}"							| ["par1","par2","par3"]	| "par1par2"						
			"{}{} text"						| ["par1"]					| "par1null text"					
			"{}{} text"						| ["par1","par2"]			| "par1par2 text"					
			"{}{} text"						| ["par1","par2","par3"]	| "par1par2 text"					
			"text {}{}"						| ["par1"]					| "text par1null"					
			"text {}{}"						| ["par1","par2"]			| "text par1par2"					
			"text {}{}"						| ["par1","par2","par3"]	| "text par1par2"					
			"text {}{} text"				| ["par1"]					| "text par1null text"			
			"text {}{} text"				| ["par1","par2"]			| "text par1par2 text"			
			"text {}{} text"				| ["par1","par2","par3"]	| "text par1par2 text"			
			"text {}{}{} text"				| ["par1"]					| "text par1nullnull text"			
			"text {}{}{} text"				| ["par1","par2"]			| "text par1par2null text"			
			"text {}{}{} text"				| ["par1","par2","par3"]	| "text par1par2par3 text"			
			"text {}{}{}{} text"			| ["p1"]					| "text p1nullnullnull text"			
			"text {}{}{}{} text"			| ["p1","p2"]				| "text p1p2nullnull text"			
			"text {}{}{}{} text"			| ["p1","p2","p3"]			| "text p1p2p3null text"			
			"text {}{}{}{} text"			| ["p1","p2","p3","p4"]		| "text p1p2p3p4 text"			
	}
}