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
import java.nio.charset.Charset;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.bytemechanics.commons.functional.LambdaUnchecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 *
 * @author afarre
 */
public class FigletTest {
	
	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> FigletTest >>>> setupSpec");
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
	
	static Stream<Arguments> printDataPack() {
	    return Stream.of(
			Arguments.of("aixo es un primer test", true, "        _                                                        _                          _               _   \n  __ _ (_)__  __  ___     ___  ___   _   _  _ __    _ __   _ __ (_) _ __ ___    ___  _ __  | |_   ___  ___ | |_ \n / _` || |\\ \\/ / / _ \\   / _ \\/ __| | | | || '_ \\  | '_ \\ | '__|| || '_ ` _ \\  / _ \\| '__| | __| / _ \\/ __|| __|\n| (_| || | >  < | (_) | |  __/\\__ \\ | |_| || | | | | |_) || |   | || | | | | ||  __/| |    | |_ |  __/\\__ \\| |_ \n \\__,_||_|/_/\\_\\ \\___/   \\___||___/  \\__,_||_| |_| | .__/ |_|   |_||_| |_| |_| \\___||_|     \\__| \\___||___/ \\__|\n                                                   |_|                                                          "),
			Arguments.of("això es un segón test",true, "        _         __                                                    __           _               _   \n  __ _ (_)__  __  \\_\\     ___  ___   _   _  _ __    ___   ___   __ _   /_/   _ __   | |_   ___  ___ | |_ \n / _` || |\\ \\/ / / _ \\   / _ \\/ __| | | | || '_ \\  / __| / _ \\ / _` | / _ \\ | '_ \\  | __| / _ \\/ __|| __|\n| (_| || | >  < | (_) | |  __/\\__ \\ | |_| || | | | \\__ \\|  __/| (_| || (_) || | | | | |_ |  __/\\__ \\| |_ \n \\__,_||_|/_/\\_\\ \\___/   \\___||___/  \\__,_||_| |_| |___/ \\___| \\__, | \\___/ |_| |_|  \\__| \\___||___/ \\__|\n                                                               |___/                                     "),
			Arguments.of("Això es un 3er test",true,"    _     _         __                                _____               _               _   \n   / \\   (_)__  __  \\_\\     ___  ___   _   _  _ __   |___ /   ___  _ __  | |_   ___  ___ | |_ \n  / _ \\  | |\\ \\/ / / _ \\   / _ \\/ __| | | | || '_ \\    |_ \\  / _ \\| '__| | __| / _ \\/ __|| __|\n / ___ \\ | | >  < | (_) | |  __/\\__ \\ | |_| || | | |  ___) ||  __/| |    | |_ |  __/\\__ \\| |_ \n/_/   \\_\\|_|/_/\\_\\ \\___/   \\___||___/  \\__,_||_| |_| |____/  \\___||_|     \\__| \\___||___/ \\__|\n                                                                                              "),
			Arguments.of("Això es un 4-ªrt test",true,"    _     _         __                                _  _           __ _        _     _               _   \n   / \\   (_)__  __  \\_\\     ___  ___   _   _  _ __   | || |         / _` | _ __ | |_  | |_   ___  ___ | |_ \n  / _ \\  | |\\ \\/ / / _ \\   / _ \\/ __| | | | || '_ \\  | || |_  _____ \\__,_|| '__|| __| | __| / _ \\/ __|| __|\n / ___ \\ | | >  < | (_) | |  __/\\__ \\ | |_| || | | | |__   _||_____||____|| |   | |_  | |_ |  __/\\__ \\| |_ \n/_/   \\_\\|_|/_/\\_\\ \\___/   \\___||___/  \\__,_||_| |_|    |_|               |_|    \\__|  \\__| \\___||___/ \\__|\n                                                                                                           "	),
 			Arguments.of("aixo es un primer test",false,"           _                                                                                _                                    _                     _    \n   __ _   (_)  __  __    ___         ___    ___       _   _    _ __        _ __     _ __   (_)   _ __ ___      ___    _ __      | |_     ___    ___   | |_  \n  / _` |  | |  \\ \\/ /   / _ \\       / _ \\  / __|     | | | |  | '_ \\      | '_ \\   | '__|  | |  | '_ ` _ \\    / _ \\  | '__|     | __|   / _ \\  / __|  | __| \n | (_| |  | |   >  <   | (_) |     |  __/  \\__ \\     | |_| |  | | | |     | |_) |  | |     | |  | | | | | |  |  __/  | |        | |_   |  __/  \\__ \\  | |_  \n  \\__,_|  |_|  /_/\\_\\   \\___/       \\___|  |___/      \\__,_|  |_| |_|     | .__/   |_|     |_|  |_| |_| |_|   \\___|  |_|         \\__|   \\___|  |___/   \\__| \n                                                                          |_|                                                                               "),
			Arguments.of("això es un segón test",false,"           _             __                                                                          __                 _                     _    \n   __ _   (_)  __  __    \\_\\         ___    ___       _   _    _ __        ___     ___     __ _     /_/     _ __       | |_     ___    ___   | |_  \n  / _` |  | |  \\ \\/ /   / _ \\       / _ \\  / __|     | | | |  | '_ \\      / __|   / _ \\   / _` |   / _ \\   | '_ \\      | __|   / _ \\  / __|  | __| \n | (_| |  | |   >  <   | (_) |     |  __/  \\__ \\     | |_| |  | | | |     \\__ \\  |  __/  | (_| |  | (_) |  | | | |     | |_   |  __/  \\__ \\  | |_  \n  \\__,_|  |_|  /_/\\_\\   \\___/       \\___|  |___/      \\__,_|  |_| |_|     |___/   \\___|   \\__, |   \\___/   |_| |_|      \\__|   \\___|  |___/   \\__| \n                                                                                          |___/                                                    "),
			Arguments.of("Això es un 3er test",false,"     _       _             __                                                _____                       _                     _    \n    / \\     (_)  __  __    \\_\\         ___    ___       _   _    _ __       |___ /     ___    _ __      | |_     ___    ___   | |_  \n   / _ \\    | |  \\ \\/ /   / _ \\       / _ \\  / __|     | | | |  | '_ \\        |_ \\    / _ \\  | '__|     | __|   / _ \\  / __|  | __| \n  / ___ \\   | |   >  <   | (_) |     |  __/  \\__ \\     | |_| |  | | | |      ___) |  |  __/  | |        | |_   |  __/  \\__ \\  | |_  \n /_/   \\_\\  |_|  /_/\\_\\   \\___/       \\___|  |___/      \\__,_|  |_| |_|     |____/    \\___|  |_|         \\__|   \\___|  |___/   \\__| \n                                                                                                                                    "),
			Arguments.of("Això es un 4-ªrt test",false,"     _       _             __                                                _  _               __ _            _         _                     _    \n    / \\     (_)  __  __    \\_\\         ___    ___       _   _    _ __       | || |             / _` |   _ __   | |_      | |_     ___    ___   | |_  \n   / _ \\    | |  \\ \\/ /   / _ \\       / _ \\  / __|     | | | |  | '_ \\      | || |_    _____   \\__,_|  | '__|  | __|     | __|   / _ \\  / __|  | __| \n  / ___ \\   | |   >  <   | (_) |     |  __/  \\__ \\     | |_| |  | | | |     |__   _|  |_____|  |____|  | |     | |_      | |_   |  __/  \\__ \\  | |_  \n /_/   \\_\\  |_|  /_/\\_\\   \\___/       \\___|  |___/      \\__,_|  |_| |_|        |_|                     |_|      \\__|      \\__|   \\___|  |___/   \\__| \n                                                                                                                                                     ")
		);
	}
	@ParameterizedTest(name = "When #phrase with compression #compression is converted expected #expected using standard font")
	@MethodSource("printDataPack")
	public void testPrint(final String phrase,final boolean compression,final String result) throws IOException{

		Figlet figlet=new Figlet(Figlet.class.getResourceAsStream("/standard.flf"), Charset.defaultCharset());
		String actual=figlet.print(phrase,compression);

		Assertions.assertNotNull(actual);
		Assertions.assertEquals(result,actual);
	}
	
	static Stream<Arguments> lengthDataPack() {
	    return Stream.of(
			Arguments.of("aixo es un primer test", true),
			Arguments.of("això es un segón test",true),
			Arguments.of("Això es un 3er test",true),
			Arguments.of("Això es un 4-ªrt test",true),
 			Arguments.of("aixo es un primer test",false),
			Arguments.of("això es un segón test",false),
			Arguments.of("Això es un 3er test",false),
			Arguments.of("Això es un 4-ªrt test",false)
		);
	}
	@ParameterizedTest(name = "When {0} with compression {1} calculate the length must be the same as looking for first newline")
	@MethodSource("lengthDataPack")
	public void testLength(final String phrase,final boolean compression) throws IOException{

		Figlet figlet=new Figlet(Figlet.class.getResourceAsStream("/standard.flf"), Charset.defaultCharset());
		int actual=figlet.length(phrase,compression);

		Assertions.assertNotNull(actual);
		Assertions.assertEquals(actual,figlet.print(phrase,compression).indexOf('\n'));
	}
	
	static Stream<Arguments> compressedDataPack() {
	    return Stream.of(
			Arguments.of("aixo es un primer test", true, '-', "----------------------------------------------------------------------------------------------------------------"),
			Arguments.of("això es un segón test", true, '_', "_________________________________________________________________________________________________________"),
			Arguments.of("Això es un 3er test", true, 'X', "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"),
			Arguments.of("Això es un 4-ªrt test", true, '-', "-----------------------------------------------------------------------------------------------------------"),
 			Arguments.of("aixo es un primer test", false, 'c', "cccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc"),
			Arguments.of("això es un segón test", false, '-', "---------------------------------------------------------------------------------------------------------------------------------------------------"),
			Arguments.of("Això es un 3er test", false, '-', "------------------------------------------------------------------------------------------------------------------------------------"),
			Arguments.of("Això es un 4-ªrt test", false, '_', "_____________________________________________________________________________________________________________________________________________________")
		);
	}
	@ParameterizedTest(name = "When {0} with compression {1} renders as line with {2} must return {3}")
	@MethodSource("compressedDataPack")
	public void testCompressed(final String phrase,final boolean compression,final char character,final String line) throws IOException{

		Figlet figlet=new Figlet(Figlet.class.getResourceAsStream("/standard.flf"), Charset.defaultCharset());
		String actual=figlet.line(phrase,compression,(char)character);

		Assertions.assertNotNull(actual);
		Assertions.assertEquals(line,actual);
	}

	@Test
	@DisplayName("When wrong font is provided raises a NoFigletFontFormatException")
	public void testNoFont() throws IOException{

		Figlet.NoFigletFontFormatException error=Assertions.assertThrows(Figlet.NoFigletFontFormatException.class,
																			() -> new Figlet(Figlet.class.getResourceAsStream("/logging.properties"), Charset.defaultCharset()));
		Assertions.assertEquals("Input has not figlet font file format (.flf)",error.getMessage());
	}
}
