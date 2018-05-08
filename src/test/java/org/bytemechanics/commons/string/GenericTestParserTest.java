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
package org.bytemechanics.commons.string;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author afarre
 */
public class GenericTestParserTest {
	
	@BeforeClass
	public static void setup() throws IOException{
		System.out.println(">>>>> GenericTestParserTest >>>> setupSpec");
		final InputStream inputStream = GenericTextParser.class.getResourceAsStream("/logging.properties");
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

	@Test
	public void find_int(){
		System.out.println(">>>>> GenericTestParserTest >>>> find(int.class)");
		
		Optional<GenericTextParser> actual=GenericTextParser.find(int.class);
		
		Assert.assertNotNull(actual);
		Assert.assertTrue(actual.isPresent());
		Assert.assertEquals(GenericTextParser.INTEGER,actual.get());
	}
}
