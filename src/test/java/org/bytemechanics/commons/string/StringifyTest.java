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
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.bytemechanics.commons.functional.LambdaUnchecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 *
 * @author afarre
 */
public class StringifyTest {
		
	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> StringifyTest >>>> setupSpec");
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
	

	@Test
	public void testStacktraceNull(){
		Assertions.assertEquals("null",Stringify.toString((Throwable)null));
	}
	@Test
	public void testStacktrace(){
		String actual=Stringify.toString(new NullPointerException());
		String expected="java.lang.NullPointerException"+System.lineSeparator()+
							"	at org.bytemechanics.commons.string.StringifyTest.testStacktrace(StringifyTest.java:64)"+System.lineSeparator();
		Assertions.assertTrue(actual.startsWith(expected));
		Assertions.assertTrue(actual.length()>expected.length());
	}
	@Test
	public void testDurationNull(){
		Assertions.assertEquals("null",Stringify.toString(null,null));
	}
	@Test
	public void testDuration(){
		String actual=Stringify.toString(Duration.ZERO,"HH-mm-ss");
		Assertions.assertEquals("00-00-00",actual);
	}
	@Test
	public void testDurationFailure(){
		String actual=Stringify.toString(Duration.ZERO,DateTimeFormatter.ISO_INSTANT.toString());
		Assertions.assertEquals("<unable to print stacktrace: Unknown pattern letter: P>",actual);
	}
	@Test
	public void testDurationNoFormat(){
		String actual=Stringify.toString(Duration.ZERO,null);
		Assertions.assertEquals("00:00:00",actual);
	}
}
