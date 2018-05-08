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
import java.net.URL
import java.nio.file.*;
import java.nio.file.InvalidPathException
import java.util.logging.*
import java.time.*
import java.time.format.*
import java.text.*
import java.util.concurrent.TimeUnit;

/**
 * @author afarre
 */
class GenericTextParserSpec extends Specification {

	def setupSpec(){
		println(">>>>> GenericTextParserSpec >>>> setupSpec")
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
	
	
	@Unroll
	def "When #value is parsed with #genericTextParser using #format result should be #result"(){
		println(">>>>> GenericTextParserSpec >>>> When $value is parsed with $genericTextParser using $format result should be $result")
		when:
			def Optional<Object> actual=genericTextParser.parse((String)value,format);
			
		then:
			actual!=null
			actual.isPresent()
			actual.get()==result
		
		where:
			value									| genericTextParser					| format												| result
 			"trues"									| GenericTextParser.BOOLEAN			| null													| false
 			"falses"								| GenericTextParser.BOOLEAN			| null													| false
 			"true"									| GenericTextParser.BOOLEAN			| null													| true
 			"false"									| GenericTextParser.BOOLEAN			| null													| false
 			"TRUE"									| GenericTextParser.BOOLEAN			| null													| true
 			"FALSE"									| GenericTextParser.BOOLEAN			| null													| false
 			"a"										| GenericTextParser.CHAR			| null													| 'a'
 			"b"										| GenericTextParser.CHAR			| null													| 'b'
 			"c"										| GenericTextParser.CHAR			| null													| 'c'
 			"e"										| GenericTextParser.CHAR			| null													| 'e'
 			"d"										| GenericTextParser.CHAR			| null													| 'd'
 			"3"										| GenericTextParser.CHAR			| null													| '3'
 			"@"										| GenericTextParser.CHAR			| null													| '@'
 			"&"										| GenericTextParser.CHAR			| null													| '&'
 			"/"										| GenericTextParser.CHAR			| null													| '/'
 			"("										| GenericTextParser.CHAR			| null													| '('
 			"(df"									| GenericTextParser.CHAR			| null													| '('
			String.valueOf(Short.MIN_VALUE)			| GenericTextParser.SHORT			| null													| Short.MIN_VALUE
 			"-100"									| GenericTextParser.SHORT			| null													| -100
 			"0"										| GenericTextParser.SHORT			| null													| 0
 			"24125"									| GenericTextParser.SHORT			| null													| 24125
 			String.valueOf(Short.MAX_VALUE)			| GenericTextParser.SHORT			| null													| Short.MAX_VALUE
			String.valueOf(Short.MIN_VALUE)+"\$"	| GenericTextParser.SHORT			| "#0\$"												| Short.MIN_VALUE
 			"-100"+"\$"								| GenericTextParser.SHORT			| "#0\$"												| -100
 			"0"+"\$"								| GenericTextParser.SHORT			| "#0\$"												| 0
 			"24125"+"\$"							| GenericTextParser.SHORT			| "#0\$"												| 24125
 			String.valueOf(Short.MAX_VALUE)+"\$"	| GenericTextParser.SHORT			| "#0\$"												| Short.MAX_VALUE
			String.valueOf(Integer.MIN_VALUE)		| GenericTextParser.INTEGER			| null													| Integer.MIN_VALUE
 			"-100"									| GenericTextParser.INTEGER			| null													| -100
 			"0"										| GenericTextParser.INTEGER			| null													| 0
 			"24125"									| GenericTextParser.INTEGER			| null													| 24125
 			String.valueOf(Integer.MAX_VALUE)		| GenericTextParser.INTEGER			| null													| Integer.MAX_VALUE
			String.valueOf(Integer.MIN_VALUE)+"\$"	| GenericTextParser.INTEGER			| "#0\$"												| Integer.MIN_VALUE
 			"-100"+"\$"								| GenericTextParser.INTEGER			| "#0\$"												| -100
 			"0"+"\$"								| GenericTextParser.INTEGER			| "#0\$"												| 0
 			"24125"+"\$"							| GenericTextParser.INTEGER			| "#0\$"												| 24125
 			String.valueOf(Integer.MAX_VALUE)+"\$"	| GenericTextParser.INTEGER			| "#0\$"												| Integer.MAX_VALUE
			String.valueOf(Long.MIN_VALUE)			| GenericTextParser.LONG			| null													| Long.MIN_VALUE
 			"-100"									| GenericTextParser.LONG			| null													| -100
 			"0"										| GenericTextParser.LONG			| null													| 0
 			"24125"									| GenericTextParser.LONG			| null													| 24125
 			String.valueOf(Long.MAX_VALUE)			| GenericTextParser.LONG			| null													| Long.MAX_VALUE
			String.valueOf(Long.MIN_VALUE)+"\$"		| GenericTextParser.LONG			| "#0\$"												| Long.MIN_VALUE
 			"-100"+"\$"								| GenericTextParser.LONG			| "#0\$"												| -100
 			"0"+"\$"								| GenericTextParser.LONG			| "#0\$"												| 0
 			"24125"+"\$"							| GenericTextParser.LONG			| "#0\$"												| 24125
 			String.valueOf(Long.MAX_VALUE)+"\$"		| GenericTextParser.LONG			| "#0\$"												| Long.MAX_VALUE
			String.valueOf(Float.MIN_VALUE)			| GenericTextParser.FLOAT			| null													| Float.MIN_VALUE
 			"-100.23"								| GenericTextParser.FLOAT			| null													| -100.23f
 			"0.0"									| GenericTextParser.FLOAT			| null													| 0.0f
 			"24125.32435"							| GenericTextParser.FLOAT			| null													| 24125.32435f
 			String.valueOf(Float.MAX_VALUE)			| GenericTextParser.FLOAT			| null													| Float.MAX_VALUE
			String.valueOf(Float.MIN_VALUE)+"\$"	| GenericTextParser.FLOAT			| "#,##0.0#\$"											| Float.MIN_VALUE
 			"-100.23"+"\$"							| GenericTextParser.FLOAT			| "#,##0.0#\$"											| -100.23f
 			"0.0"+"\$"								| GenericTextParser.FLOAT			| "#,##0.0#\$"											| 0.0f
 			"24,125.32435"+"\$"						| GenericTextParser.FLOAT			| "#,##0.0#\$"											| 24125.32435f
 			String.valueOf(Float.MAX_VALUE)+"\$"	| GenericTextParser.FLOAT			| "#,##0.0#\$"											| Float.MAX_VALUE
			String.valueOf(Double.MIN_VALUE)		| GenericTextParser.DOUBLE			| null													| Double.MIN_VALUE
 			"-100.32532"							| GenericTextParser.DOUBLE			| null													| -100.32532d
 			"0.0"									| GenericTextParser.DOUBLE			| null													| 0.0d
 			"24125.242"								| GenericTextParser.DOUBLE			| null													| 24125.242d
 			String.valueOf(Double.MAX_VALUE)		| GenericTextParser.DOUBLE			| null													| Double.MAX_VALUE
			String.valueOf(Double.MIN_VALUE)+"\$"	| GenericTextParser.DOUBLE			| "#,##0.0#\$"											| Double.MIN_VALUE
 			"-100.32532"+"\$"						| GenericTextParser.DOUBLE			| "#,##0.0#\$"											| -100.32532d
 			"0.0"+"\$"								| GenericTextParser.DOUBLE			| "#,##0.0#\$"											| 0.0d
 			"24,125.242"+"\$"						| GenericTextParser.DOUBLE			| "#,##0.0#\$"											| 24125.242d
 			String.valueOf(Double.MAX_VALUE)+"\$"	| GenericTextParser.DOUBLE			| "#,##0.0#\$"											| Double.MAX_VALUE
			"-231,412,432.432423"					| GenericTextParser.BIGDECIMAL		| null													| new BigDecimal("-231412432.432423")
 			"-100.32435"							| GenericTextParser.BIGDECIMAL		| null													| new BigDecimal("-100.32435")
 			"0.0"									| GenericTextParser.BIGDECIMAL		| null													| new BigDecimal("0.0")
 			"24,125.3211"							| GenericTextParser.BIGDECIMAL		| null													| new BigDecimal("24125.3211")
 			"231,412,432.432423"					| GenericTextParser.BIGDECIMAL		| null													| new BigDecimal("231412432.432423")
			"-231412432.432423"+"\$"				| GenericTextParser.BIGDECIMAL		| "###0.0#\$"											| new BigDecimal("-231412432.432423")
 			"-100.32435"+"\$"						| GenericTextParser.BIGDECIMAL		| "###0.0#\$"											| new BigDecimal("-100.32435")
 			"0.0"+"\$"								| GenericTextParser.BIGDECIMAL		| "###0.0#\$"											| new BigDecimal("0.0")
 			"24125.3211"+"\$"						| GenericTextParser.BIGDECIMAL		| "###0.0#\$"											| new BigDecimal("24125.3211")
 			"231412432.432423"+"\$"					| GenericTextParser.BIGDECIMAL		| "###0.0#\$"											| new BigDecimal("231412432.432423")
			"myMessage"								| GenericTextParser.STRING			| null													| "myMessage"
			"10:15"									| GenericTextParser.LOCALTIME		| null													| LocalTime.of(10,15)
			"10:15:30"								| GenericTextParser.LOCALTIME		| null													| LocalTime.of(10,15,30)
			"10:15:30.123"							| GenericTextParser.LOCALTIME		| null													| LocalTime.of(10,15,30,123000000)
			"10-15"									| GenericTextParser.LOCALTIME		| "HH-mm[-ss]"											| LocalTime.of(10,15)
			"10-15-30"								| GenericTextParser.LOCALTIME		| "HH-mm[-ss]"											| LocalTime.of(10,15,30)
			"2007-12-03"							| GenericTextParser.LOCALDATE		| null													| LocalDate.of(2007,12,03)
			"2007/12/03"							| GenericTextParser.LOCALDATE		| "yyyy/MM/dd"											| LocalDate.of(2007,12,03)
			"2007-12-03T10:15"						| GenericTextParser.LOCALDATETIME	| null													| LocalDateTime.of(2007,12,03,10,15)
			"2007-12-03T10:15:30"					| GenericTextParser.LOCALDATETIME	| null													| LocalDateTime.of(2007,12,03,10,15,30)
			"2007/12/03 10-15"						| GenericTextParser.LOCALDATETIME	| "yyyy/MM/dd HH-mm[-ss]"								| LocalDateTime.of(2007,12,03,10,15)
			"2007/12/03 10-15-30"					| GenericTextParser.LOCALDATETIME	| "yyyy/MM/dd HH-mm[-ss]"								| LocalDateTime.of(2007,12,03,10,15,30)
			"ENUM"									| GenericTextParser.ENUM			| "org.bytemechanics.commons.string.GenericTextParser"	| GenericTextParser.ENUM
			"LOCALDATE"								| GenericTextParser.ENUM			| "org.bytemechanics.commons.string.GenericTextParser"	| GenericTextParser.LOCALDATE
			"/etc/bin"								| GenericTextParser.PATH			| ""													| Paths.get("/etc/bin")
			"/home/usr"								| GenericTextParser.PATH			| ""													| Paths.get("/home/usr")
	}

	@Unroll
	def "When [#value] value is parsed with #genericTextParser using #format result should be empty optional"(){
		println(">>>>> GenericTextParserSpec >>>> When null $value is parsed with $genericTextParser using $format result should be empty optional")
		when:
			def Optional<Object> actual=genericTextParser.parse((String)value,format);
			
		then:
			actual!=null
			!actual.isPresent()
		
		where:
			value									| genericTextParser					| format												
 			null									| GenericTextParser.BOOLEAN			| null													
 			""										| GenericTextParser.BOOLEAN			| null													
 			null									| GenericTextParser.CHAR			| null													
 			""										| GenericTextParser.CHAR			| null													
			null									| GenericTextParser.SHORT			| null													
 			""										| GenericTextParser.SHORT			| null													
			null									| GenericTextParser.INTEGER			| null													
 			""										| GenericTextParser.INTEGER			| null													
			null									| GenericTextParser.LONG			| null													
 			""										| GenericTextParser.LONG			| null													
			null									| GenericTextParser.FLOAT			| null													
 			""										| GenericTextParser.FLOAT			| null													
			null									| GenericTextParser.DOUBLE			| null													
 			""										| GenericTextParser.DOUBLE			| null													
			null									| GenericTextParser.BIGDECIMAL		| null													
 			""										| GenericTextParser.BIGDECIMAL		| null													
			null									| GenericTextParser.STRING			| null													
			null									| GenericTextParser.LOCALTIME		| null													
			""										| GenericTextParser.LOCALTIME		| null													
			null									| GenericTextParser.LOCALDATETIME	| null													
			""										| GenericTextParser.LOCALDATETIME	| null													
			null									| GenericTextParser.ENUM			| "org.bytemechanics.commons.string.GenericTextParser"	
			""										| GenericTextParser.ENUM			| "org.bytemechanics.commons.string.GenericTextParser"	
			null									| GenericTextParser.PATH			| null
			""										| GenericTextParser.PATH			| null
	}		
	
	@Unroll
	def "When #value is parsed with #genericTextParser using #format should fail with #failure"(){
		println(">>>>> GenericTextParserSpec >>>> When $value is parsed with $genericTextParser using $format should fail with $failure")
		when:
			def Optional<Object> actual=genericTextParser.parse((String)value,format);
			
		then:
			thrown(failure)
		
		where:
			value									| genericTextParser					| format												| failure
			"g"										| GenericTextParser.SHORT			| null													| NumberFormatException.class
			"0.3"									| GenericTextParser.SHORT			| null													| NumberFormatException.class
			Long.MAX_VALUE.toString()				| GenericTextParser.SHORT			| null													| NumberFormatException.class
			"g"										| GenericTextParser.INTEGER			| null													| NumberFormatException.class
			"0.3"									| GenericTextParser.INTEGER			| null													| NumberFormatException.class
 			Long.MAX_VALUE.toString()				| GenericTextParser.INTEGER			| null													| NumberFormatException.class
			"g"										| GenericTextParser.LONG			| null													| NumberFormatException.class
 			"0.3"									| GenericTextParser.LONG			| null													| NumberFormatException.class
 			"99999999999999999999999999999999999999"| GenericTextParser.LONG			| null													| NumberFormatException.class
			"g"										| GenericTextParser.FLOAT			| null													| NumberFormatException.class
 			"-100.d23"								| GenericTextParser.FLOAT			| null													| NumberFormatException.class
 			"g"										| GenericTextParser.DOUBLE			| null													| NumberFormatException.class
 			"-100.d23"								| GenericTextParser.DOUBLE			| null													| NumberFormatException.class
			"g"+"\$"								| GenericTextParser.DOUBLE			| "#,##0.0#\$"											| ParseException.class
 			"0.d0"+"\$"								| GenericTextParser.DOUBLE			| "#,##0.0#\$"											| ParseException.class
			"g"										| GenericTextParser.BIGDECIMAL		| null													| ParseException.class
 			"X-100.32435"							| GenericTextParser.BIGDECIMAL		| null													| ParseException.class
			"g"+"\$"								| GenericTextParser.BIGDECIMAL		| "###0.0#\$"											| ParseException.class
 			"-100.d32435"+"\$"						| GenericTextParser.BIGDECIMAL		| "###0.0#\$"											| ParseException.class
			"s10:15"								| GenericTextParser.LOCALTIME		| null													| DateTimeParseException.class
			"1015:30"								| GenericTextParser.LOCALTIME		| null													| DateTimeParseException.class
			"10:1530.d123"							| GenericTextParser.LOCALTIME		| null													| DateTimeParseException.class
			"1015"									| GenericTextParser.LOCALTIME		| "HH-mm[-ss]"											| DateTimeParseException.class
			"10-f15-3"								| GenericTextParser.LOCALTIME		| "HH-mm[-ss]"											| DateTimeParseException.class
			"2007-1203"								| GenericTextParser.LOCALDATE		| null													| DateTimeParseException.class
			"2007/1203"								| GenericTextParser.LOCALDATE		| "yyyy/MM/dd"											| DateTimeParseException.class
			"2007-12-0310:15"						| GenericTextParser.LOCALDATETIME	| null													| DateTimeParseException.class
			"2007-12-03T1015:30"					| GenericTextParser.LOCALDATETIME	| null													| DateTimeParseException.class
			"200712/03 10-15"						| GenericTextParser.LOCALDATETIME	| "yyyy/MM/dd HH-mm[-ss]"								| DateTimeParseException.class
			"2007/12/03f10-15-30"					| GenericTextParser.LOCALDATETIME	| "yyyy/MM/dd HH-mm[-ss]"								| DateTimeParseException.class
			"ENUMd"									| GenericTextParser.ENUM			| "org.bytemechanics.commons.string.GenericTextParser"	| IllegalArgumentException.class
			"12"									| GenericTextParser.ENUM			| "org.bytemechanics.commons.string.GenericTextParser"	| IllegalArgumentException.class
	}

	@Unroll
	def "When #value is formatted with #genericTextParser using #format result should be #result"(){
		println(">>>>> GenericTextParserSpec >>>> When $value is formatted with $genericTextParser using $format result should be $result")
		when:
			def Optional<String> actual=genericTextParser.format(value,format);
			
		then:
			actual!=null
			actual.isPresent()
			actual.get()==result
		
		where:
			result														| genericTextParser					| format												| value
 			"true"														| GenericTextParser.BOOLEAN			| null													| true
 			"false"														| GenericTextParser.BOOLEAN			| null													| false
 			"a"															| GenericTextParser.CHAR			| null													| 'a'
 			"b"															| GenericTextParser.CHAR			| null													| 'b'
 			"c"															| GenericTextParser.CHAR			| null													| 'c'
 			"e"															| GenericTextParser.CHAR			| null													| 'e'
 			"d"															| GenericTextParser.CHAR			| null													| 'd'
 			"3"															| GenericTextParser.CHAR			| null													| '3'
 			"@"															| GenericTextParser.CHAR			| null													| '@'
 			"&"															| GenericTextParser.CHAR			| null													| '&'
 			"/"															| GenericTextParser.CHAR			| null													| '/'
 			"("															| GenericTextParser.CHAR			| null													| '('
 			"("															| GenericTextParser.CHAR			| null													| '('
			String.valueOf(Short.MIN_VALUE)								| GenericTextParser.SHORT			| null													| Short.MIN_VALUE
 			"-100"														| GenericTextParser.SHORT			| null													| (short)-100
 			"0"															| GenericTextParser.SHORT			| null													| (short)0
 			"24125"														| GenericTextParser.SHORT			| null													| (short)24125
 			String.valueOf(Short.MAX_VALUE)								| GenericTextParser.SHORT			| null													| Short.MAX_VALUE
			String.valueOf(Short.MIN_VALUE)+"\$"						| GenericTextParser.SHORT			| "#0\$"												| Short.MIN_VALUE
 			"-100"+"\$"													| GenericTextParser.SHORT			| "#0\$"												| (short)-100
 			"0"+"\$"													| GenericTextParser.SHORT			| "#0\$"												| (short)0
 			"24125"+"\$"												| GenericTextParser.SHORT			| "#0\$"												| (short)24125
 			String.valueOf(Short.MAX_VALUE)+"\$"						| GenericTextParser.SHORT			| "#0\$"												| Short.MAX_VALUE
			String.valueOf(Integer.MIN_VALUE)							| GenericTextParser.INTEGER			| null													| Integer.MIN_VALUE
 			"-100"														| GenericTextParser.INTEGER			| null													| -100
 			"0"															| GenericTextParser.INTEGER			| null													| 0
 			"24125"														| GenericTextParser.INTEGER			| null													| 24125
 			String.valueOf(Integer.MAX_VALUE)							| GenericTextParser.INTEGER			| null													| Integer.MAX_VALUE
			String.valueOf(Integer.MIN_VALUE)+"\$"						| GenericTextParser.INTEGER			| "#0\$"												| Integer.MIN_VALUE
 			"-100"+"\$"													| GenericTextParser.INTEGER			| "#0\$"												| -100
 			"0"+"\$"													| GenericTextParser.INTEGER			| "#0\$"												| 0
 			"24125"+"\$"												| GenericTextParser.INTEGER			| "#0\$"												| 24125
 			String.valueOf(Integer.MAX_VALUE)+"\$"						| GenericTextParser.INTEGER			| "#0\$"												| Integer.MAX_VALUE
			String.valueOf(Long.MIN_VALUE)								| GenericTextParser.LONG			| null													| Long.MIN_VALUE
 			"-100"														| GenericTextParser.LONG			| null													| -100l
 			"0"															| GenericTextParser.LONG			| null													| 0l
 			"24125"														| GenericTextParser.LONG			| null													| 24125l
 			String.valueOf(Long.MAX_VALUE)								| GenericTextParser.LONG			| null													| Long.MAX_VALUE
			String.valueOf(Long.MIN_VALUE)+"\$"							| GenericTextParser.LONG			| "#0\$"												| Long.MIN_VALUE
 			"-100"+"\$"													| GenericTextParser.LONG			| "#0\$"												| -100l
 			"0"+"\$"													| GenericTextParser.LONG			| "#0\$"												| 0l
 			"24125"+"\$"												| GenericTextParser.LONG			| "#0\$"												| 24125l
 			String.valueOf(Long.MAX_VALUE)+"\$"							| GenericTextParser.LONG			| "#0\$"												| Long.MAX_VALUE
			"-2.31412432E8"												| GenericTextParser.FLOAT			| null													| -231412432.432423f
 			"-100.23"													| GenericTextParser.FLOAT			| null													| -100.23f
 			"0.0"														| GenericTextParser.FLOAT			| null													| (float)0l
 			"24125.324"													| GenericTextParser.FLOAT			| null													| 24125.324f
			Float.MAX_VALUE.toString()									| GenericTextParser.FLOAT			| null													| Float.MAX_VALUE
			"-231,412,432.0"+"\$"										| GenericTextParser.FLOAT			| "#,##0.0#\$"											| -231412432.432423f
 			"-100.23"+"\$"												| GenericTextParser.FLOAT			| "#,##0.0#\$"											| -100.23f
 			"0.0"+"\$"													| GenericTextParser.FLOAT			| "#,##0.0#\$"											| (float)0l
 			"24,125.32"+"\$"											| GenericTextParser.FLOAT			| "#,##0.0#\$"											| 24125.324f
 			"340,282,346,638,528,860,000,000,000,000,000,000,000.0\$"	| GenericTextParser.FLOAT			| "#,##0.0#\$"											| Float.MAX_VALUE
			"-2.31412432432423E8"										| GenericTextParser.DOUBLE			| null													| -231412432.432423d
 			"-100.32532"												| GenericTextParser.DOUBLE			| null													| -100.32532d
 			"0.0"														| GenericTextParser.DOUBLE			| null													| (double)0l
 			"24125.242"													| GenericTextParser.DOUBLE			| null													| 24125.242d
 			Double.MAX_VALUE.toString()									| GenericTextParser.DOUBLE			| null													| Double.MAX_VALUE
			"-231,412,432.43"+"\$"										| GenericTextParser.DOUBLE			| "#,##0.0#\$"											| -231412432.432423d
 			"-100.33"+"\$"												| GenericTextParser.DOUBLE			| "#,##0.0#\$"											| -100.32532d
 			"0.0"+"\$"													| GenericTextParser.DOUBLE			| "#,##0.0#\$"											| (double)0l
 			"24,125.24"+"\$"											| GenericTextParser.DOUBLE			| "#,##0.0#\$"											| 24125.242d
 			"340,282,346,638,528,860,000,000,000,000,000,000,000.0\$"	| GenericTextParser.DOUBLE			| "#,##0.0#\$"											| (double)Float.MAX_VALUE
			"-231,412,432.43"											| GenericTextParser.BIGDECIMAL		| null													| new BigDecimal("-231412432.432423")
 			"-100.32"													| GenericTextParser.BIGDECIMAL		| null													| new BigDecimal("-100.32435")
 			"0.0"														| GenericTextParser.BIGDECIMAL		| null													| new BigDecimal("0.0")
 			"24,125.32"													| GenericTextParser.BIGDECIMAL		| null													| new BigDecimal("24125.3211")
 			"231,412,432.43"											| GenericTextParser.BIGDECIMAL		| null													| new BigDecimal("231412432.432423")
			"-231412432.43"+"\$"										| GenericTextParser.BIGDECIMAL		| "###0.0#\$"											| new BigDecimal("-231412432.432423")
 			"-100.32"+"\$"												| GenericTextParser.BIGDECIMAL		| "###0.0#\$"											| new BigDecimal("-100.32435")
 			"0.0"+"\$"													| GenericTextParser.BIGDECIMAL		| "###0.0#\$"											| new BigDecimal("0.0")
 			"24125.32"+"\$"												| GenericTextParser.BIGDECIMAL		| "###0.0#\$"											| new BigDecimal("24125.3211")
 			"231412432.43"+"\$"											| GenericTextParser.BIGDECIMAL		| "###0.0#\$"											| new BigDecimal("231412432.432423")
			"myMessage"													| GenericTextParser.STRING			| null													| "myMessage"
			"10:15"														| GenericTextParser.LOCALTIME		| null													| LocalTime.of(10,15)
			"10:15:30"													| GenericTextParser.LOCALTIME		| null													| LocalTime.of(10,15,30)
			"10:15:30.123"												| GenericTextParser.LOCALTIME		| null													| LocalTime.of(10,15,30,123000000)
			"10-15-00"													| GenericTextParser.LOCALTIME		| "HH-mm[-ss]"											| LocalTime.of(10,15)
			"10-15-30"													| GenericTextParser.LOCALTIME		| "HH-mm[-ss]"											| LocalTime.of(10,15,30)
			"2007-12-03"												| GenericTextParser.LOCALDATE		| null													| LocalDate.of(2007,12,03)
			"2007/12/03"												| GenericTextParser.LOCALDATE		| "yyyy/MM/dd"											| LocalDate.of(2007,12,03)
			"2007-12-03T10:15"											| GenericTextParser.LOCALDATETIME	| null													| LocalDateTime.of(2007,12,03,10,15)
			"2007-12-03T10:15:30"										| GenericTextParser.LOCALDATETIME	| null													| LocalDateTime.of(2007,12,03,10,15,30)
			"2007/12/03 10-15-00"										| GenericTextParser.LOCALDATETIME	| "yyyy/MM/dd HH-mm[-ss]"								| LocalDateTime.of(2007,12,03,10,15)
			"2007/12/03 10-15-30"										| GenericTextParser.LOCALDATETIME	| "yyyy/MM/dd HH-mm[-ss]"								| LocalDateTime.of(2007,12,03,10,15,30)
			"ENUM"														| GenericTextParser.ENUM			| "org.bytemechanics.commons.string.GenericTextParser"	| GenericTextParser.ENUM
			"LOCALDATE"													| GenericTextParser.ENUM			| "org.bytemechanics.commons.string.GenericTextParser"	| GenericTextParser.LOCALDATE
			"/etc/bin"													| GenericTextParser.PATH			| ""													| Paths.get("/etc/bin")
			"/home/usr"													| GenericTextParser.PATH			| ""													| Paths.get("/home/usr")
	}
	
	@Unroll
	def "When [#value] value is formatted with #genericTextParser using #format result should be empty optional"(){
		println(">>>>> GenericTextParserSpec >>>> When null $value is parsed with $genericTextParser using $format result should be empty optional")
		when:
			def Optional<Object> actual=genericTextParser.format((String)value,format);
			
		then:
			actual!=null
			!actual.isPresent()
		
		where:
			value									| genericTextParser					| format												
 			null									| GenericTextParser.BOOLEAN			| null													
 			null									| GenericTextParser.CHAR			| null													
			null									| GenericTextParser.SHORT			| null													
			null									| GenericTextParser.INTEGER			| null													
			null									| GenericTextParser.LONG			| null													
			null									| GenericTextParser.FLOAT			| null													
			null									| GenericTextParser.DOUBLE			| null													
			null									| GenericTextParser.BIGDECIMAL		| null													
			null									| GenericTextParser.STRING			| null													
			null									| GenericTextParser.LOCALTIME		| null													
			null									| GenericTextParser.LOCALDATETIME	| null													
			null									| GenericTextParser.ENUM			| "org.bytemechanics.commons.string.GenericTextParser"	
			null									| GenericTextParser.PATH			| null													
	}		

	@Unroll
	def "When try to find the appropiate parser for #clazz the result found is #genericTextParser"(){
		println(">>>>> GenericTextParserSpec >>>> When try to find the appropiate parser for $clazz the result found is $genericTextParser")
		when:
			def Optional<GenericTextParser> actual=GenericTextParser.find(clazz);
			
		then:
			actual!=null
			actual.isPresent()==genericTextParser.isPresent()
			actual.get()==genericTextParser.get()
		
		where:
			clazz									| genericTextParser							
 			boolean.class							| Optional.of(GenericTextParser.BOOLEAN)	
 			Boolean.class							| Optional.of(GenericTextParser.BOOLEAN)	
 			char.class								| Optional.of(GenericTextParser.CHAR)			
 			Character.class							| Optional.of(GenericTextParser.CHAR)			
			short.class								| Optional.of(GenericTextParser.SHORT)			
 			Short.class								| Optional.of(GenericTextParser.SHORT)			
			int.class								| Optional.of(GenericTextParser.INTEGER)			
			Integer.class							| Optional.of(GenericTextParser.INTEGER)			
			long.class								| Optional.of(GenericTextParser.LONG)			
 			Long.class								| Optional.of(GenericTextParser.LONG)			
 			float.class								| Optional.of(GenericTextParser.FLOAT)			
 			Float.class								| Optional.of(GenericTextParser.FLOAT)			
			double.class							| Optional.of(GenericTextParser.DOUBLE)			
 			Double.class							| Optional.of(GenericTextParser.DOUBLE)			
			BigDecimal.class						| Optional.of(GenericTextParser.BIGDECIMAL)		
			String.class							| Optional.of(GenericTextParser.STRING)			
			LocalTime.class							| Optional.of(GenericTextParser.LOCALTIME)		
			LocalDateTime.class						| Optional.of(GenericTextParser.LOCALDATETIME)	
			TimeUnit.class							| Optional.of(GenericTextParser.ENUM)			
			Path.class								| Optional.of(GenericTextParser.PATH)			
	}	

	@Unroll
	def "When try to find the appropiate parser for #clazz the result found is empty"(){
		println(">>>>> GenericTextParserSpec >>>> When try to find the appropiate parser for $clazz the result found is empty")
		when:
			def Optional<GenericTextParser> actual=GenericTextParser.find(clazz);
			
		then:
			actual!=null
			!actual.isPresent()
		
		where:
			clazz << [Instant.class,URL.class]
	}

	@Unroll
	def "When try to find the appropiate parser for #value the result found is #genericTextParser"(){
		println(">>>>> GenericTextParserSpec >>>> When try to find the appropiate parser for $value the result found is $genericTextParser")
		when:
			def Optional<GenericTextParser> actual=GenericTextParser.find(value);
			
		then:
			actual!=null
			actual.isPresent()==genericTextParser.isPresent()
			actual.get()==genericTextParser.get()
		
		where:
			value									| genericTextParser							
 			true									| Optional.of(GenericTextParser.BOOLEAN)	
 			Boolean.TRUE							| Optional.of(GenericTextParser.BOOLEAN)	
 			false									| Optional.of(GenericTextParser.BOOLEAN)	
 			Boolean.FALSE							| Optional.of(GenericTextParser.BOOLEAN)	
 			((char)'a')								| Optional.of(GenericTextParser.CHAR)			
 			new Character((char)'j')				| Optional.of(GenericTextParser.CHAR)			
 			((char)'d')								| Optional.of(GenericTextParser.CHAR)			
 			new Character((char)'i')				| Optional.of(GenericTextParser.CHAR)			
 			((char)'z')								| Optional.of(GenericTextParser.CHAR)			
 			new Character((char)'h')				| Optional.of(GenericTextParser.CHAR)			
			((short)1)								| Optional.of(GenericTextParser.SHORT)			
 			Short.valueOf((short)2)					| Optional.of(GenericTextParser.SHORT)			
			((int)1)								| Optional.of(GenericTextParser.INTEGER)			
			Integer.valueOf((int)100)				| Optional.of(GenericTextParser.INTEGER)			
			100l									| Optional.of(GenericTextParser.LONG)			
 			Long.valueOf(324l)						| Optional.of(GenericTextParser.LONG)			
 			3435.4f									| Optional.of(GenericTextParser.FLOAT)			
 			Float.valueOf(3435.4f)					| Optional.of(GenericTextParser.FLOAT)			
			232423532.6576d							| Optional.of(GenericTextParser.DOUBLE)			
 			Double.valueOf(3242151.34d)				| Optional.of(GenericTextParser.DOUBLE)			
			new BigDecimal("-1242352.2314")			| Optional.of(GenericTextParser.BIGDECIMAL)		
			"myMessage"								| Optional.of(GenericTextParser.STRING)			
			LocalTime.now()							| Optional.of(GenericTextParser.LOCALTIME)		
			LocalDateTime.now()						| Optional.of(GenericTextParser.LOCALDATETIME)	
			TimeUnit.NANOSECONDS					| Optional.of(GenericTextParser.ENUM)			
			Paths.get("/etc")						| Optional.of(GenericTextParser.PATH)			
	}	
	
	@Unroll
	def "When try to find the appropiate parser for #value the result found is empty"(){
		println(">>>>> GenericTextParserSpec >>>> When try to find the appropiate parser for $value the result found is empty")
		when:
			def Optional<GenericTextParser> actual=GenericTextParser.find(value);
			
		then:
			actual!=null
			!actual.isPresent()
		
		where:
		value << [Instant.now(),new URL("http://bytemechanics.org")]
	}
}