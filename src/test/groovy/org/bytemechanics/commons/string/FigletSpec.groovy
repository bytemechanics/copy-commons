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

package org.bytemechanics.commons.string

import spock.lang.*
import spock.lang.Specification
import org.bytemechanics.commons.string.*
import java.util.function.*
import java.io.*
import java.net.URL
import java.nio.file.*;
import java.nio.file.InvalidPathException
import java.util.logging.*
import java.time.*
import java.time.format.*
import java.text.*
import java.util.concurrent.TimeUnit;
import java.nio.charset.Charset;

/**
 *
 * @author afarre
 */
class FigletSpec extends Specification {

		def setupSpec(){
		println(">>>>> FigletSpec >>>> setupSpec")
		final InputStream inputStream = GenericTextParser.class.getResourceAsStream("/logging.properties")
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
	def "When #phrase with compression #compression is converted expected #expected using #standard font"(){
		println(">>>>> FigletSpec >>>> When $phrase with compression $compression is converted expected $expected using $standard font")
		when:
			def figlet=new Figlet(Figlet.class.getResourceAsStream(standard), Charset.defaultCharset());
			def actual=figlet.print(phrase,compression)
			
		then:
			actual!=null
			actual==expected
		
		where:
			phrase						| compression	| expected												
 			"aixo es un primer test"	| true			| "        _                                                        _                          _               _   \n  __ _ (_)__  __  ___     ___  ___   _   _  _ __    _ __   _ __ (_) _ __ ___    ___  _ __  | |_   ___  ___ | |_ \n / _` || |\\ \\/ / / _ \\   / _ \\/ __| | | | || '_ \\  | '_ \\ | '__|| || '_ ` _ \\  / _ \\| '__| | __| / _ \\/ __|| __|\n| (_| || | >  < | (_) | |  __/\\__ \\ | |_| || | | | | |_) || |   | || | | | | ||  __/| |    | |_ |  __/\\__ \\| |_ \n \\__,_||_|/_/\\_\\ \\___/   \\___||___/  \\__,_||_| |_| | .__/ |_|   |_||_| |_| |_| \\___||_|     \\__| \\___||___/ \\__|\n                                                   |_|                                                          "
			"això es un segón test"		| true			| "        _         __                                                    __           _               _   \n  __ _ (_)__  __  \\_\\     ___  ___   _   _  _ __    ___   ___   __ _   /_/   _ __   | |_   ___  ___ | |_ \n / _` || |\\ \\/ / / _ \\   / _ \\/ __| | | | || '_ \\  / __| / _ \\ / _` | / _ \\ | '_ \\  | __| / _ \\/ __|| __|\n| (_| || | >  < | (_) | |  __/\\__ \\ | |_| || | | | \\__ \\|  __/| (_| || (_) || | | | | |_ |  __/\\__ \\| |_ \n \\__,_||_|/_/\\_\\ \\___/   \\___||___/  \\__,_||_| |_| |___/ \\___| \\__, | \\___/ |_| |_|  \\__| \\___||___/ \\__|\n                                                               |___/                                     "		
			"Això es un 3er test"		| true			| "    _     _         __                                _____               _               _   \n   / \\   (_)__  __  \\_\\     ___  ___   _   _  _ __   |___ /   ___  _ __  | |_   ___  ___ | |_ \n  / _ \\  | |\\ \\/ / / _ \\   / _ \\/ __| | | | || '_ \\    |_ \\  / _ \\| '__| | __| / _ \\/ __|| __|\n / ___ \\ | | >  < | (_) | |  __/\\__ \\ | |_| || | | |  ___) ||  __/| |    | |_ |  __/\\__ \\| |_ \n/_/   \\_\\|_|/_/\\_\\ \\___/   \\___||___/  \\__,_||_| |_| |____/  \\___||_|     \\__| \\___||___/ \\__|\n                                                                                              "
			"Això es un 4-ªrt test"		| true			| "    _     _         __                                _  _           __ _        _     _               _   \n   / \\   (_)__  __  \\_\\     ___  ___   _   _  _ __   | || |         / _` | _ __ | |_  | |_   ___  ___ | |_ \n  / _ \\  | |\\ \\/ / / _ \\   / _ \\/ __| | | | || '_ \\  | || |_  _____ \\__,_|| '__|| __| | __| / _ \\/ __|| __|\n / ___ \\ | | >  < | (_) | |  __/\\__ \\ | |_| || | | | |__   _||_____||____|| |   | |_  | |_ |  __/\\__ \\| |_ \n/_/   \\_\\|_|/_/\\_\\ \\___/   \\___||___/  \\__,_||_| |_|    |_|               |_|    \\__|  \\__| \\___||___/ \\__|\n                                                                                                           "			
 			"aixo es un primer test"	| false			| "           _                                                                                _                                    _                     _    \n   __ _   (_)  __  __    ___         ___    ___       _   _    _ __        _ __     _ __   (_)   _ __ ___      ___    _ __      | |_     ___    ___   | |_  \n  / _` |  | |  \\ \\/ /   / _ \\       / _ \\  / __|     | | | |  | '_ \\      | '_ \\   | '__|  | |  | '_ ` _ \\    / _ \\  | '__|     | __|   / _ \\  / __|  | __| \n | (_| |  | |   >  <   | (_) |     |  __/  \\__ \\     | |_| |  | | | |     | |_) |  | |     | |  | | | | | |  |  __/  | |        | |_   |  __/  \\__ \\  | |_  \n  \\__,_|  |_|  /_/\\_\\   \\___/       \\___|  |___/      \\__,_|  |_| |_|     | .__/   |_|     |_|  |_| |_| |_|   \\___|  |_|         \\__|   \\___|  |___/   \\__| \n                                                                          |_|                                                                               "
			"això es un segón test"		| false			| "           _             __                                                                          __                 _                     _    \n   __ _   (_)  __  __    \\_\\         ___    ___       _   _    _ __        ___     ___     __ _     /_/     _ __       | |_     ___    ___   | |_  \n  / _` |  | |  \\ \\/ /   / _ \\       / _ \\  / __|     | | | |  | '_ \\      / __|   / _ \\   / _` |   / _ \\   | '_ \\      | __|   / _ \\  / __|  | __| \n | (_| |  | |   >  <   | (_) |     |  __/  \\__ \\     | |_| |  | | | |     \\__ \\  |  __/  | (_| |  | (_) |  | | | |     | |_   |  __/  \\__ \\  | |_  \n  \\__,_|  |_|  /_/\\_\\   \\___/       \\___|  |___/      \\__,_|  |_| |_|     |___/   \\___|   \\__, |   \\___/   |_| |_|      \\__|   \\___|  |___/   \\__| \n                                                                                          |___/                                                    "
			"Això es un 3er test"		| false			| "     _       _             __                                                _____                       _                     _    \n    / \\     (_)  __  __    \\_\\         ___    ___       _   _    _ __       |___ /     ___    _ __      | |_     ___    ___   | |_  \n   / _ \\    | |  \\ \\/ /   / _ \\       / _ \\  / __|     | | | |  | '_ \\        |_ \\    / _ \\  | '__|     | __|   / _ \\  / __|  | __| \n  / ___ \\   | |   >  <   | (_) |     |  __/  \\__ \\     | |_| |  | | | |      ___) |  |  __/  | |        | |_   |  __/  \\__ \\  | |_  \n /_/   \\_\\  |_|  /_/\\_\\   \\___/       \\___|  |___/      \\__,_|  |_| |_|     |____/    \\___|  |_|         \\__|   \\___|  |___/   \\__| \n                                                                                                                                    "
			"Això es un 4-ªrt test"		| false			| "     _       _             __                                                _  _               __ _            _         _                     _    \n    / \\     (_)  __  __    \\_\\         ___    ___       _   _    _ __       | || |             / _` |   _ __   | |_      | |_     ___    ___   | |_  \n   / _ \\    | |  \\ \\/ /   / _ \\       / _ \\  / __|     | | | |  | '_ \\      | || |_    _____   \\__,_|  | '__|  | __|     | __|   / _ \\  / __|  | __| \n  / ___ \\   | |   >  <   | (_) |     |  __/  \\__ \\     | |_| |  | | | |     |__   _|  |_____|  |____|  | |     | |_      | |_   |  __/  \\__ \\  | |_  \n /_/   \\_\\  |_|  /_/\\_\\   \\___/       \\___|  |___/      \\__,_|  |_| |_|        |_|                     |_|      \\__|      \\__|   \\___|  |___/   \\__| \n                                                                                                                                                     "
			
			standard="/standard.flf"
	}

	@Unroll
	def "When #phrase with compression #compression calculate the length must be the same as looking for first newline"(){
		println(">>>>> FigletSpec >>>> When $phrase with compression $compression calculate the length must be the same as looking for first newline")

		setup:
			def figlet=new Figlet(Figlet.class.getResourceAsStream(standard), Charset.defaultCharset());
			
		when:
			def actual=figlet.length(phrase,compression)
			
		then:
			actual!=null
			actual==figlet.print(phrase,compression).indexOf('\n')
		
		where:
			phrase						| compression		
 			"aixo es un primer test"	| true			
			"això es un segón test"		| true			
			"Això es un 3er test"		| true			
			"Això es un 4-ªrt test"		| true			
 			"aixo es un primer test"	| false			
			"això es un segón test"		| false			
			"Això es un 3er test"		| false			
			"Això es un 4-ªrt test"		| false			
			
			standard="/standard.flf"
	}

	@Unroll
	def "When #phrase with compression #compression renders as line with #character must return #line"(){
		println(">>>>> FigletSpec >>>> When $phrase with compression $compression renders as line with $character must return $line")

		setup:
			def figlet=new Figlet(Figlet.class.getResourceAsStream(standard), Charset.defaultCharset());
		
		when:
			def actual=figlet.line(phrase,compression,(char)character)
			
		then:
			actual!=null
			actual==line
		
		where:
			phrase						| compression	| character	| line									
 			"aixo es un primer test"	| true			| '-'		| "------------------------------------------------------------------------------------------------------------------------------------------------------------"
			"això es un segón test"		| true			| '_'		| "___________________________________________________________________________________________________________________________________________________"
			"Això es un 3er test"		| true			| 'X'		| "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
			"Això es un 4-ªrt test"		| true			| '-'		| "-----------------------------------------------------------------------------------------------------------------------------------------------------"
 			"aixo es un primer test"	| false			| 'c'		| "cccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc"
			"això es un segón test"		| false			| '-'		| "---------------------------------------------------------------------------------------------------------------------------------------------------"
			"Això es un 3er test"		| false			| '-'		| "------------------------------------------------------------------------------------------------------------------------------------"
			"Això es un 4-ªrt test"		| false			| '_'		| "_____________________________________________________________________________________________________________________________________________________"
			
			standard="/standard.flf"
	}
}

