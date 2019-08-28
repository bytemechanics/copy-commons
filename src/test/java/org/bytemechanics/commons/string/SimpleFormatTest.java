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
package org.bytemechanics.commons.string;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.function.Supplier;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.bytemechanics.commons.functional.LambdaUnchecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 *
 * @author afarre
 */
public class SimpleFormatTest {
	
	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> SimpleFormatTest >>>> setupSpec");
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
	@BeforeEach
    void beforeEachTest(final TestInfo testInfo) {
        System.out.println(">>>>> "+this.getClass().getSimpleName()+" >>>> "+testInfo.getTestMethod().map(Method::getName).orElse("Unkown")+""+testInfo.getTags().toString()+" >>>> "+testInfo.getDisplayName());
    }
	
	static Stream<Arguments> dataPack() {
	    return Stream.of(
			Arguments.of("message without args", new Object[]{}, "message without args"),
			Arguments.of("message without args",new Object[]{"fsdf"}, "message without args"),
			Arguments.of("message without args", new Object[]{1}, "message without args"),
			Arguments.of("message with 1:{} arg", new Object[]{}, "message with 1:null arg"),
			Arguments.of("message with 1:{} arg", new Object[]{"fsdf"}, "message with 1:fsdf arg"),
			Arguments.of("message with 1:{} arg", new Object[]{1}, "message with 1:1 arg"),
			Arguments.of("message with 1:{} arg", new Object[]{"fsdf",2}, "message with 1:fsdf arg"),
			Arguments.of("message with 1:{} arg", new Object[]{"fsdf","fsdfsd"}, "message with 1:fsdf arg"),
			Arguments.of("message with arg 1:{}", new Object[]{}, "message with arg 1:null"),
			Arguments.of("message with arg 1:{}", new Object[]{"fsdf"}, "message with arg 1:fsdf"),
			Arguments.of("message with arg 1:{}", new Object[]{1}, "message with arg 1:1"),
			Arguments.of("message with arg 1:{}", new Object[]{"fsdf",2}, "message with arg 1:fsdf"),
			Arguments.of("message with arg 1:{}", new Object[]{"fsdf","fsdfsd"}, "message with arg 1:fsdf"),
			Arguments.of("message with arg 1:{},2:{}", new Object[]{}, "message with arg 1:null,2:null"),
			Arguments.of("message with arg 1:{},2:{}", new Object[]{"fsdf"}, "message with arg 1:fsdf,2:null"),
			Arguments.of("message with arg 1:{},2:{}", new Object[]{1}, "message with arg 1:1,2:null"),
			Arguments.of("message with arg 1:{},2:{}", new Object[]{"fsdf",2}, "message with arg 1:fsdf,2:2"),
			Arguments.of("message with arg 1:{},2:{}", new Object[]{"fsdf","fsdfsd"}, "message with arg 1:fsdf,2:fsdfsd"),
			Arguments.of("{} message with arg 1:,2:{}", new Object[]{}, "null message with arg 1:,2:null"),
			Arguments.of("{} message with arg 2:{}"	, new Object[]{"fsdf"}, "fsdf message with arg 2:null"),
			Arguments.of("{} message with arg 2:{}"	, new Object[]{1}, "1 message with arg 2:null"),
			Arguments.of("{} message with arg 2:{}"	, new Object[]{"fsdf",2}, "fsdf message with arg 2:2"),
			Arguments.of("{} message with arg 2:{}"	, new Object[]{"fsdf","fsdfsd"}, "fsdf message with arg 2:fsdfsd"),
			Arguments.of("message with arg 1:{},2:{}", new Object[]{}, "message with arg 1:null,2:null"),
			Arguments.of("message with arg 1:{},2:{}", new Object[]{"fsdf {}"}, "message with arg 1:fsdf {},2:null"),
			Arguments.of("message with arg 1:{},2:{}", new Object[]{1}, "message with arg 1:1,2:null"),
			Arguments.of("message with arg 1:{},2:{}", new Object[]{"fsdf {}",2}, "message with arg 1:fsdf {},2:2"),
			Arguments.of("message with arg 1:{},2:{}", new Object[]{"fsdf {}","fsdfsd {}"}, "message with arg 1:fsdf {},2:fsdfsd {}"),
			Arguments.of("{}{}"	, new Object[]{"par1"}, "par1null"),
			Arguments.of("{}{}"	, new Object[]{"par1","par2"}, "par1par2"),						
			Arguments.of("{}{}"	, new Object[]{"par1","par2","par3"}, "par1par2"),						
			Arguments.of("{}{} text", new Object[]{"par1"}, "par1null text"	),				
			Arguments.of("{}{} text", new Object[]{"par1","par2"}, "par1par2 text"),					
			Arguments.of("{}{} text", new Object[]{"par1","par2","par3"}, "par1par2 text"),					
			Arguments.of("text {}{}", new Object[]{"par1"}, "text par1null"),					
			Arguments.of("text {}{}", new Object[]{"par1","par2"}, "text par1par2"),					
			Arguments.of("text {}{}", new Object[]{"par1","par2","par3"}, "text par1par2"),					
			Arguments.of("text {}{} text"		, new Object[]{"par1"}, "text par1null text"),
			Arguments.of("text {}{} text"		, new Object[]{"par1","par2"}, "text par1par2 text"	),		
			Arguments.of("text {}{} text"		, new Object[]{"par1","par2","par3"}, "text par1par2 text"	),		
			Arguments.of("text {}{}{} text"		, new Object[]{"par1"}, "text par1nullnull text"	),		
			Arguments.of("text {}{}{} text"		, new Object[]{"par1","par2"}, "text par1par2null text"),			
			Arguments.of("text {}{}{} text"		, new Object[]{"par1","par2","par3"}, "text par1par2par3 text"),			
			Arguments.of("text {}{}{}{} text"	, new Object[]{"p1"}, "text p1nullnullnull text"),			
			Arguments.of("text {}{}{}{} text"	, new Object[]{"p1","p2"}, "text p1p2nullnull text"),			
			Arguments.of("text {}{}{}{} text"	, new Object[]{"p1","p2","p3"}, "text p1p2p3null text"),			
			Arguments.of("text {}{}{}{} text"	, new Object[]{"p1","p2","p3","p4"}, "text p1p2p3p4 text"),			
			Arguments.of("text {{}}{}{}{} text"	, new Object[]{"p1","p2","p3","p4"}, "text {p1}p2p3p4 text"),			
			Arguments.of("text {}{{}}{}{} text"	, new Object[]{"p1","p2","p3","p4"}, "text p1{p2}p3p4 text"),			
			Arguments.of("text {}{}{{}}{} text"	, new Object[]{"p1","p2","p3","p4"}, "text p1p2{p3}p4 text"),			
			Arguments.of("text {}{}{}{{}} text"	, new Object[]{"p1","p2","p3","p4"}, "text p1p2p3{p4} text")
		);
	}

	@ParameterizedTest(name = "When {0} is formatted with {1} result should be {2}")
	@MethodSource("dataPack")
	public void testFormat(final String message,final Object[] arguments,final String result){

		String actual=SimpleFormat.format(message,(Object[])arguments);
		Assertions.assertEquals(result,actual);
	}

	@ParameterizedTest(name = "When {0} is formatted with {1} result should be {2}")
	@MethodSource("dataPack")
	public void testSupplier(final String message,final Object[] arguments,final String result){

		Supplier<String> actual=SimpleFormat.supplier(message,(Object[])arguments);
		Assertions.assertNotNull(actual);
		Assertions.assertEquals(result,actual.get());
	}

}
