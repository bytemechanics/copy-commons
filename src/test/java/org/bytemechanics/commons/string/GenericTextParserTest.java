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
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Stream;
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
public class GenericTextParserTest {
	
	@BeforeAll
	public static void setup() throws IOException{
		System.out.println(">>>>> GenericTextParserTest >>>> setupSpec");
		try(InputStream inputStream = GenericTextParserTest.class.getResourceAsStream("/logging.properties")){
			LogManager.getLogManager().readConfiguration(inputStream);
		}catch (final IOException e){
			Logger.getAnonymousLogger().severe("Could not load default logging.properties file");
			Logger.getAnonymousLogger().severe(e.getMessage());
		}
	}
	@BeforeEach
    void beforeEachTest(final TestInfo testInfo) {
        System.out.println(">>>>> "+this.getClass().getSimpleName()+" >>>> "+testInfo.getTestMethod().map(Method::getName).orElse("Unkown")+""+testInfo.getTags().toString()+" >>>> "+testInfo.getDisplayName());
    }
	
	static Stream<Arguments> parseDataPack() {
	    return Stream.of(
			Arguments.of("trues", GenericTextParser.BOOLEAN, null, false),
 			Arguments.of("falses", GenericTextParser.BOOLEAN, null, false),
 			Arguments.of("true"	, GenericTextParser.BOOLEAN, null, true),
 			Arguments.of("false", GenericTextParser.BOOLEAN, null, false),
 			Arguments.of("TRUE"	, GenericTextParser.BOOLEAN, null, true),
 			Arguments.of("FALSE", GenericTextParser.BOOLEAN, null, false),
 			Arguments.of("a", GenericTextParser.CHAR, null, 'a'),
 			Arguments.of("b", GenericTextParser.CHAR, null, 'b'),
 			Arguments.of("c", GenericTextParser.CHAR, null, 'c'),
 			Arguments.of("e", GenericTextParser.CHAR, null, 'e'),
 			Arguments.of("d", GenericTextParser.CHAR, null, 'd'),
 			Arguments.of("3", GenericTextParser.CHAR, null, '3'),
 			Arguments.of("@", GenericTextParser.CHAR, null, '@'),
 			Arguments.of("&", GenericTextParser.CHAR, null, '&'),
 			Arguments.of("/", GenericTextParser.CHAR, null, '/'),
 			Arguments.of("(", GenericTextParser.CHAR, null, '('),
 			Arguments.of("(df", GenericTextParser.CHAR, null, '('),
			Arguments.of(String.valueOf(Short.MIN_VALUE)	, GenericTextParser.SHORT, null, Short.MIN_VALUE),
			Arguments.of("-100", GenericTextParser.SHORT, null, (short)-100),
			Arguments.of("0", GenericTextParser.SHORT, null, (short)0),
			Arguments.of("24125", GenericTextParser.SHORT, null, (short)24125),
 			Arguments.of(String.valueOf(Short.MAX_VALUE)	, GenericTextParser.SHORT, null, Short.MAX_VALUE),
			Arguments.of(String.valueOf(Short.MIN_VALUE)+"$", GenericTextParser.SHORT, "#0$", Short.MIN_VALUE),
 			Arguments.of("-100"+"$", GenericTextParser.SHORT, "#0$", (short)-100),
 			Arguments.of("0"+"$", GenericTextParser.SHORT, "#0$", (short)0),
 			Arguments.of("24125"+"$", GenericTextParser.SHORT, "#0$", (short)24125),
 			Arguments.of(String.valueOf(Short.MAX_VALUE)+"$", GenericTextParser.SHORT, "#0$", Short.MAX_VALUE),
			Arguments.of(String.valueOf(Integer.MIN_VALUE), GenericTextParser.INTEGER, null, Integer.MIN_VALUE),
 			Arguments.of("-100", GenericTextParser.INTEGER, null, -100),
 			Arguments.of("0", GenericTextParser.INTEGER, null, 0),
 			Arguments.of("24125", GenericTextParser.INTEGER, null, 24125),
 			Arguments.of(String.valueOf(Integer.MAX_VALUE), GenericTextParser.INTEGER, null, Integer.MAX_VALUE),
			Arguments.of(String.valueOf(Integer.MIN_VALUE)+"$", GenericTextParser.INTEGER	, "#0$", Integer.MIN_VALUE),
 			Arguments.of("-100"+"$", GenericTextParser.INTEGER	, "#0$", -100),
 			Arguments.of("0"+"$", GenericTextParser.INTEGER	, "#0$", 0),
 			Arguments.of("24125"+"$", GenericTextParser.INTEGER	, "#0$", 24125),
 			Arguments.of(String.valueOf(Integer.MAX_VALUE)+"$", GenericTextParser.INTEGER	, "#0$", Integer.MAX_VALUE),
			Arguments.of(String.valueOf(Long.MIN_VALUE), GenericTextParser.LONG, null, Long.MIN_VALUE),
 			Arguments.of("-100", GenericTextParser.LONG, null, -100l),
 			Arguments.of("0", GenericTextParser.LONG, null, 0l),
 			Arguments.of("24125", GenericTextParser.LONG, null, 24125l),
 			Arguments.of(String.valueOf(Long.MAX_VALUE), GenericTextParser.LONG, null, Long.MAX_VALUE),
			Arguments.of(String.valueOf(Long.MIN_VALUE)+"$"	, GenericTextParser.LONG, "#0$", Long.MIN_VALUE),
 			Arguments.of("-100"+"$", GenericTextParser.LONG, "#0$", -100l),
 			Arguments.of("0"+"$", GenericTextParser.LONG, "#0$", 0l),
 			Arguments.of("24125"+"$", GenericTextParser.LONG, "#0$", 24125l),
 			Arguments.of(String.valueOf(Long.MAX_VALUE)+"$"	, GenericTextParser.LONG, "#0$", Long.MAX_VALUE),
			Arguments.of(String.valueOf(Float.MIN_VALUE)	, GenericTextParser.FLOAT, null, Float.MIN_VALUE),
 			Arguments.of("-100.23", GenericTextParser.FLOAT, null, -100.23f),
 			Arguments.of("0.0"	, GenericTextParser.FLOAT, null, 0.0f),
 			Arguments.of("24125.32435", GenericTextParser.FLOAT, null, 24125.32435f),
 			Arguments.of(String.valueOf(Float.MAX_VALUE)	, GenericTextParser.FLOAT, null, Float.MAX_VALUE),
			Arguments.of(String.valueOf(Float.MIN_VALUE)+"$", GenericTextParser.FLOAT, "#,##0.0#$", Float.MIN_VALUE),
 			Arguments.of("-100.23"+"$", GenericTextParser.FLOAT, "#,##0.0#$", -100.23f),
 			Arguments.of("0.0"+"$", GenericTextParser.FLOAT, "#,##0.0#$", 0.0f),
 			Arguments.of("24,125.32435"+"$", GenericTextParser.FLOAT, "#,##0.0#$", 24125.32435f),
 			Arguments.of(String.valueOf(Float.MAX_VALUE)+"$", GenericTextParser.FLOAT, "#,##0.0#$", Float.MAX_VALUE),
			Arguments.of(String.valueOf(Double.MIN_VALUE), GenericTextParser.DOUBLE, null, Double.MIN_VALUE),
 			Arguments.of("-100.32532", GenericTextParser.DOUBLE, null, -100.32532d),
 			Arguments.of("0.0", GenericTextParser.DOUBLE, null, 0.0d),
 			Arguments.of("24125.242", GenericTextParser.DOUBLE, null, 24125.242d),
 			Arguments.of(String.valueOf(Double.MAX_VALUE), GenericTextParser.DOUBLE, null, Double.MAX_VALUE),
			Arguments.of(String.valueOf(Double.MIN_VALUE)+"$", GenericTextParser.DOUBLE	, "#,##0.0#$", Double.MIN_VALUE),
 			Arguments.of("-100.32532"+"$", GenericTextParser.DOUBLE	, "#,##0.0#$", -100.32532d),
 			Arguments.of("0.0"+"$"		, GenericTextParser.DOUBLE	, "#,##0.0#$", 0.0d),
 			Arguments.of("24,125.242"+"$", GenericTextParser.DOUBLE	, "#,##0.0#$", 24125.242d),
 			Arguments.of(String.valueOf(Double.MAX_VALUE)+"$", GenericTextParser.DOUBLE	, "#,##0.0#$", Double.MAX_VALUE),
			Arguments.of("-231,412,432.432423", GenericTextParser.BIGDECIMAL, null	, new BigDecimal("-231412432.432423")),
 			Arguments.of("-100.32435", GenericTextParser.BIGDECIMAL, null, new BigDecimal("-100.32435")),
 			Arguments.of("0.0"	, GenericTextParser.BIGDECIMAL, null, new BigDecimal("0.0")),
 			Arguments.of("24,125.3211", GenericTextParser.BIGDECIMAL, null, new BigDecimal("24125.3211")),
 			Arguments.of("231,412,432.432423", GenericTextParser.BIGDECIMAL, null, new BigDecimal("231412432.432423")),
			Arguments.of("-231412432.432423"+"$", GenericTextParser.BIGDECIMAL, "###0.0#$", new BigDecimal("-231412432.432423")),
 			Arguments.of("-100.32435"+"$", GenericTextParser.BIGDECIMAL, "###0.0#$", new BigDecimal("-100.32435")),
 			Arguments.of("0.0"+"$"	, GenericTextParser.BIGDECIMAL, "###0.0#$", new BigDecimal("0.0")),
 			Arguments.of("24125.3211"+"$", GenericTextParser.BIGDECIMAL, "###0.0#$", new BigDecimal("24125.3211")),
 			Arguments.of("231412432.432423"+"$", GenericTextParser.BIGDECIMAL, "###0.0#$", new BigDecimal("231412432.432423")),
			Arguments.of("myMessage", GenericTextParser.STRING	, null, "myMessage"),
			Arguments.of(""	, GenericTextParser.STRING	, null, ""),
			Arguments.of("10:15", GenericTextParser.LOCALTIME, null, LocalTime.of(10,15)),
			Arguments.of("10:15:30", GenericTextParser.LOCALTIME, null, LocalTime.of(10,15,30)),
			Arguments.of("10:15:30.123", GenericTextParser.LOCALTIME, null, LocalTime.of(10,15,30,123000000)),
			Arguments.of("10-15", GenericTextParser.LOCALTIME, "HH-mm[-ss]", LocalTime.of(10,15)),
			Arguments.of("10-15-30"	, GenericTextParser.LOCALTIME, "HH-mm[-ss]", LocalTime.of(10,15,30)),
			Arguments.of("2007-12-03", GenericTextParser.LOCALDATE, null, LocalDate.of(2007,12,03)),
			Arguments.of("2007/12/03", GenericTextParser.LOCALDATE, "yyyy/MM/dd", LocalDate.of(2007,12,03)),
			Arguments.of("2007-12-03T10:15"	, GenericTextParser.LOCALDATETIME, null	, LocalDateTime.of(2007,12,03,10,15)),
			Arguments.of("2007-12-03T10:15:30", GenericTextParser.LOCALDATETIME, null, LocalDateTime.of(2007,12,03,10,15,30)),
			Arguments.of("2007/12/03 10-15"	, GenericTextParser.LOCALDATETIME, "yyyy/MM/dd HH-mm[-ss]", LocalDateTime.of(2007,12,03,10,15)),
			Arguments.of("2007/12/03 10-15-30", GenericTextParser.LOCALDATETIME, "yyyy/MM/dd HH-mm[-ss]", LocalDateTime.of(2007,12,03,10,15,30)),
			Arguments.of("ENUM"	, GenericTextParser.ENUM, "org.bytemechanics.commons.string.GenericTextParser", GenericTextParser.ENUM),
			Arguments.of("LOCALDATE", GenericTextParser.ENUM, "org.bytemechanics.commons.string.GenericTextParser", GenericTextParser.LOCALDATE),
			Arguments.of(Paths.get("/etc/bin").toString(), GenericTextParser.PATH, "", Paths.get("/etc/bin")),
			Arguments.of(Paths.get("/home/usr").toString(), GenericTextParser.STRING, "myMessage", Paths.get("/home/usr").toString())
		);
	}
	@ParameterizedTest(name = "When {0} is parsed with {1} using {2} result should be {3}")
	@MethodSource("parseDataPack")
	public void testParse(final String value,final GenericTextParser genericTextParser,final String format,final Object result) throws IOException{

		Optional<Object> actual=genericTextParser.parse((String)value,format);
		
		Assertions.assertNotNull(actual);
		Assertions.assertTrue(actual.isPresent());
		Assertions.assertEquals(result,actual.get());
	}

	static Stream<Arguments> nullEmptydataPack() {
	    return Stream.of(
			Arguments.of(null,GenericTextParser.BOOLEAN, null),
			Arguments.of(""	, GenericTextParser.BOOLEAN, null),
			Arguments.of(null, GenericTextParser.CHAR, null),
			Arguments.of(""	, GenericTextParser.CHAR	, null),
			Arguments.of(null, GenericTextParser.SHORT, null),
			Arguments.of(""	, GenericTextParser.SHORT, null),
			Arguments.of(null, GenericTextParser.INTEGER, null),
			Arguments.of(""	, GenericTextParser.INTEGER, null),
			Arguments.of(null, GenericTextParser.LONG, null),
			Arguments.of(""	, GenericTextParser.LONG, null),
			Arguments.of(null, GenericTextParser.FLOAT, null),
			Arguments.of(""	, GenericTextParser.FLOAT, null),
			Arguments.of(null, GenericTextParser.DOUBLE, null),
			Arguments.of(""	, GenericTextParser.DOUBLE, null),
			Arguments.of(null, GenericTextParser.BIGDECIMAL, null),
			Arguments.of(""	, GenericTextParser.BIGDECIMAL, null),
			Arguments.of(null, GenericTextParser.STRING	, null),
			Arguments.of(null, GenericTextParser.LOCALTIME, null),
			Arguments.of(""	, GenericTextParser.LOCALTIME, null),
			Arguments.of(null, GenericTextParser.LOCALDATETIME, null),
			Arguments.of(""	, GenericTextParser.LOCALDATETIME, null),
			Arguments.of(null, GenericTextParser.ENUM, "org.bytemechanics.commons.string.GenericTextParser"),
			Arguments.of(""	, GenericTextParser.ENUM, "org.bytemechanics.commons.string.GenericTextParser"),
			Arguments.of(null, GenericTextParser.PATH, null),
			Arguments.of(""	, GenericTextParser.PATH, null)
		);
	}
	@ParameterizedTest(name = "When [{0}] value is parsed with {1} using {2} result should be empty optional")
	@MethodSource("nullEmptydataPack")
	public void testNullEmptyParse(final String value,final GenericTextParser genericTextParser,final String format) throws IOException{

		Optional<Object> actual=genericTextParser.parse((String)value,format);
		
		Assertions.assertNotNull(actual);
		Assertions.assertFalse(actual.isPresent());
	}	

	static Stream<Arguments> failureDataPack() {
	    return Stream.of(
			Arguments.of("g", GenericTextParser.SHORT, null	, NumberFormatException.class),
			Arguments.of("0.3", GenericTextParser.SHORT	, null, NumberFormatException.class),
			Arguments.of(String.valueOf(Long.MAX_VALUE), GenericTextParser.SHORT	, null, NumberFormatException.class),
			Arguments.of("g", GenericTextParser.INTEGER	, null, NumberFormatException.class),
			Arguments.of("0.3", GenericTextParser.INTEGER, null, NumberFormatException.class),
 			Arguments.of(String.valueOf(Long.MAX_VALUE), GenericTextParser.INTEGER, null, NumberFormatException.class),
			Arguments.of("g", GenericTextParser.LONG, null, NumberFormatException.class),
 			Arguments.of("0.3", GenericTextParser.LONG	, null, NumberFormatException.class),
 			Arguments.of("99999999999999999999999999999999999999", GenericTextParser.LONG, null													, NumberFormatException.class),
			Arguments.of("g", GenericTextParser.FLOAT	, null, NumberFormatException.class),
 			Arguments.of("-100.d23", GenericTextParser.FLOAT	, null, NumberFormatException.class),
 			Arguments.of("g", GenericTextParser.DOUBLE	, null, NumberFormatException.class),
 			Arguments.of("-100.d23"	, GenericTextParser.DOUBLE	, null, NumberFormatException.class),
			Arguments.of("g$", GenericTextParser.DOUBLE	, "#,##0.0#$", ParseException.class),
 			Arguments.of("0.d0$", GenericTextParser.DOUBLE	, "#,##0.0#$", ParseException.class),
			Arguments.of("g", GenericTextParser.BIGDECIMAL, null, ParseException.class),
 			Arguments.of("X-100.32435", GenericTextParser.BIGDECIMAL, null, ParseException.class),
			Arguments.of("g$", GenericTextParser.BIGDECIMAL	, "###0.0#$", ParseException.class),
 			Arguments.of("-100.d32435$", GenericTextParser.BIGDECIMAL, "###0.0#$", ParseException.class),
			Arguments.of("s10:15", GenericTextParser.LOCALTIME, null, DateTimeParseException.class),
			Arguments.of("1015:30", GenericTextParser.LOCALTIME	, null, DateTimeParseException.class),
			Arguments.of("10:1530.d123", GenericTextParser.LOCALTIME, null, DateTimeParseException.class),
			Arguments.of("1015", GenericTextParser.LOCALTIME, "HH-mm[-ss]", DateTimeParseException.class),
			Arguments.of("10-f15-3", GenericTextParser.LOCALTIME, "HH-mm[-ss]", DateTimeParseException.class),
			Arguments.of("2007-1203", GenericTextParser.LOCALDATE, null, DateTimeParseException.class),
			Arguments.of("2007/1203", GenericTextParser.LOCALDATE, "yyyy/MM/dd", DateTimeParseException.class),
			Arguments.of("2007-12-0310:15", GenericTextParser.LOCALDATETIME	, null, DateTimeParseException.class),
			Arguments.of("2007-12-03T1015:30", GenericTextParser.LOCALDATETIME	, null, DateTimeParseException.class),
			Arguments.of("200712/03 10-15", GenericTextParser.LOCALDATETIME	, "yyyy/MM/dd HH-mm[-ss]", DateTimeParseException.class),
			Arguments.of("2007/12/03f10-15-30", GenericTextParser.LOCALDATETIME	, "yyyy/MM/dd HH-mm[-ss]", DateTimeParseException.class),
			Arguments.of("ENUMd", GenericTextParser.ENUM, "org.bytemechanics.commons.string.GenericTextParser", IllegalArgumentException.class),
			Arguments.of("12", GenericTextParser.ENUM, "org.bytemechanics.commons.string.GenericTextParser", IllegalArgumentException.class)
		);
	}
	@ParameterizedTest(name = "When {0} is parsed with {1} using {2} should fail with {3}")
	@MethodSource("failureDataPack")
	@SuppressWarnings("ThrowableResultIgnored")
	public void testParseFailure(final String value,final GenericTextParser genericTextParser,final String format,final Class<? extends Throwable> failure) throws IOException{

		Assertions.assertThrows(failure,
								() -> genericTextParser.parse((String)value,format));
	}

	static Stream<Arguments> formatDataPack() {
	    return Stream.of(
 			Arguments.of("false", GenericTextParser.BOOLEAN, null, false),
 			Arguments.of("true"	, GenericTextParser.BOOLEAN, null, true),
 			Arguments.of("false", GenericTextParser.BOOLEAN, null, false),
 			Arguments.of("a", GenericTextParser.CHAR, null, 'a'),
 			Arguments.of("b", GenericTextParser.CHAR, null, 'b'),
 			Arguments.of("c", GenericTextParser.CHAR, null, 'c'),
 			Arguments.of("e", GenericTextParser.CHAR, null, 'e'),
 			Arguments.of("d", GenericTextParser.CHAR, null, 'd'),
 			Arguments.of("3", GenericTextParser.CHAR, null, '3'),
 			Arguments.of("@", GenericTextParser.CHAR, null, '@'),
 			Arguments.of("&", GenericTextParser.CHAR, null, '&'),
 			Arguments.of("/", GenericTextParser.CHAR, null, '/'),
 			Arguments.of("(", GenericTextParser.CHAR, null, '('),
			Arguments.of(String.valueOf(Short.MIN_VALUE)	, GenericTextParser.SHORT, null, Short.MIN_VALUE),
			Arguments.of("-100", GenericTextParser.SHORT, null, (short)-100),
			Arguments.of("0", GenericTextParser.SHORT, null, (short)0),
			Arguments.of("24125", GenericTextParser.SHORT, null, (short)24125),
 			Arguments.of(String.valueOf(Short.MAX_VALUE)	, GenericTextParser.SHORT, null, Short.MAX_VALUE),
			Arguments.of(String.valueOf(Short.MIN_VALUE)+"$", GenericTextParser.SHORT, "#0$", Short.MIN_VALUE),
 			Arguments.of("-100"+"$", GenericTextParser.SHORT, "#0$", (short)-100),
 			Arguments.of("0"+"$", GenericTextParser.SHORT, "#0$", (short)0),
 			Arguments.of("24125"+"$", GenericTextParser.SHORT, "#0$", (short)24125),
 			Arguments.of(String.valueOf(Short.MAX_VALUE)+"$", GenericTextParser.SHORT, "#0$", Short.MAX_VALUE),
			Arguments.of(String.valueOf(Integer.MIN_VALUE), GenericTextParser.INTEGER, null, Integer.MIN_VALUE),
 			Arguments.of("-100", GenericTextParser.INTEGER, null, -100),
 			Arguments.of("0", GenericTextParser.INTEGER, null, 0),
 			Arguments.of("24125", GenericTextParser.INTEGER, null, 24125),
 			Arguments.of(String.valueOf(Integer.MAX_VALUE), GenericTextParser.INTEGER, null, Integer.MAX_VALUE),
			Arguments.of(String.valueOf(Integer.MIN_VALUE)+"$", GenericTextParser.INTEGER	, "#0$", Integer.MIN_VALUE),
 			Arguments.of("-100"+"$", GenericTextParser.INTEGER	, "#0$", -100),
 			Arguments.of("0"+"$", GenericTextParser.INTEGER	, "#0$", 0),
 			Arguments.of("24125"+"$", GenericTextParser.INTEGER	, "#0$", 24125),
 			Arguments.of(String.valueOf(Integer.MAX_VALUE)+"$", GenericTextParser.INTEGER	, "#0$", Integer.MAX_VALUE),
			Arguments.of(String.valueOf(Long.MIN_VALUE), GenericTextParser.LONG, null, Long.MIN_VALUE),
 			Arguments.of("-100", GenericTextParser.LONG, null, -100l),
 			Arguments.of("0", GenericTextParser.LONG, null, 0l),
 			Arguments.of("24125", GenericTextParser.LONG, null, 24125l),
 			Arguments.of(String.valueOf(Long.MAX_VALUE), GenericTextParser.LONG, null, Long.MAX_VALUE),
			Arguments.of(String.valueOf(Long.MIN_VALUE)+"$"	, GenericTextParser.LONG, "#0$", Long.MIN_VALUE),
 			Arguments.of("-100"+"$", GenericTextParser.LONG, "#0$", -100l),
 			Arguments.of("0"+"$", GenericTextParser.LONG, "#0$", 0l),
 			Arguments.of("24125"+"$", GenericTextParser.LONG, "#0$", 24125l),
 			Arguments.of(String.valueOf(Long.MAX_VALUE)+"$"	, GenericTextParser.LONG, "#0$", Long.MAX_VALUE),
			Arguments.of(String.valueOf(Float.MIN_VALUE)	, GenericTextParser.FLOAT, null, Float.MIN_VALUE),
 			Arguments.of("-100.23", GenericTextParser.FLOAT, null, -100.23f),
 			Arguments.of("0.0"	, GenericTextParser.FLOAT, null, 0.0f),
 			Arguments.of("24125.324", GenericTextParser.FLOAT, null, 24125.32435f),
 			Arguments.of(String.valueOf(Float.MAX_VALUE)	, GenericTextParser.FLOAT, null, Float.MAX_VALUE),
			Arguments.of("0.0$", GenericTextParser.FLOAT, "#,##0.0#$", Float.MIN_VALUE),
 			Arguments.of("-100.23"+"$", GenericTextParser.FLOAT, "#,##0.0#$", -100.23f),
 			Arguments.of("0.0"+"$", GenericTextParser.FLOAT, "#,##0.0#$", 0.0f),
 			Arguments.of("24,125.32"+"$", GenericTextParser.FLOAT, "#,##0.0#$", 24125.32435f),
 			Arguments.of("340,282,346,638,528,860,000,000,000,000,000,000,000.0$", GenericTextParser.FLOAT, "#,##0.0#$", Float.MAX_VALUE),
			Arguments.of(String.valueOf(Double.MIN_VALUE), GenericTextParser.DOUBLE, null, Double.MIN_VALUE),
 			Arguments.of("-100.32532", GenericTextParser.DOUBLE, null, -100.32532d),
 			Arguments.of("0.0", GenericTextParser.DOUBLE, null, 0.0d),
 			Arguments.of("24125.242", GenericTextParser.DOUBLE, null, 24125.242d),
 			Arguments.of(String.valueOf(Double.MAX_VALUE), GenericTextParser.DOUBLE, null, Double.MAX_VALUE),
			Arguments.of("0.0$", GenericTextParser.DOUBLE	, "#,##0.0#$", Double.MIN_VALUE),
 			Arguments.of("-100.33"+"$", GenericTextParser.DOUBLE	, "#,##0.0#$", -100.32532d),
 			Arguments.of("0.0"+"$"		, GenericTextParser.DOUBLE	, "#,##0.0#$", 0.0d),
 			Arguments.of("24,125.24"+"$", GenericTextParser.DOUBLE	, "#,##0.0#$", 24125.242d),
 			Arguments.of("179,769,313,486,231,570,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000,000.0$", GenericTextParser.DOUBLE	, "#,##0.0#$", Double.MAX_VALUE),
			Arguments.of("-231,412,432.43", GenericTextParser.BIGDECIMAL, null	, new BigDecimal("-231412432.432423")),
 			Arguments.of("-100.32", GenericTextParser.BIGDECIMAL, null, new BigDecimal("-100.32435")),
 			Arguments.of("0.0"	, GenericTextParser.BIGDECIMAL, null, new BigDecimal("0.0")),
 			Arguments.of("24,125.32", GenericTextParser.BIGDECIMAL, null, new BigDecimal("24125.3211")),
 			Arguments.of("231,412,432.43", GenericTextParser.BIGDECIMAL, null, new BigDecimal("231412432.432423")),
			Arguments.of("-231412432.43"+"$", GenericTextParser.BIGDECIMAL, "###0.0#$", new BigDecimal("-231412432.432423")),
 			Arguments.of("-100.32"+"$", GenericTextParser.BIGDECIMAL, "###0.0#$", new BigDecimal("-100.32435")),
 			Arguments.of("0.0"+"$"	, GenericTextParser.BIGDECIMAL, "###0.0#$", new BigDecimal("0.0")),
 			Arguments.of("24125.32"+"$", GenericTextParser.BIGDECIMAL, "###0.0#$", new BigDecimal("24125.3211")),
 			Arguments.of("231412432.43"+"$", GenericTextParser.BIGDECIMAL, "###0.0#$", new BigDecimal("231412432.432423")),
			Arguments.of("myMessage", GenericTextParser.STRING	, null, "myMessage"),
			Arguments.of(""	, GenericTextParser.STRING	, null, ""),
			Arguments.of("10:15", GenericTextParser.LOCALTIME, null, LocalTime.of(10,15)),
			Arguments.of("10:15:30", GenericTextParser.LOCALTIME, null, LocalTime.of(10,15,30)),
			Arguments.of("10:15:30.123", GenericTextParser.LOCALTIME, null, LocalTime.of(10,15,30,123000000)),
			Arguments.of("10-15-00", GenericTextParser.LOCALTIME, "HH-mm[-ss]", LocalTime.of(10,15)),
			Arguments.of("10-15-30"	, GenericTextParser.LOCALTIME, "HH-mm[-ss]", LocalTime.of(10,15,30)),
			Arguments.of("2007-12-03", GenericTextParser.LOCALDATE, null, LocalDate.of(2007,12,03)),
			Arguments.of("2007/12/03", GenericTextParser.LOCALDATE, "yyyy/MM/dd", LocalDate.of(2007,12,03)),
			Arguments.of("2007-12-03T10:15"	, GenericTextParser.LOCALDATETIME, null	, LocalDateTime.of(2007,12,03,10,15)),
			Arguments.of("2007-12-03T10:15:30", GenericTextParser.LOCALDATETIME, null, LocalDateTime.of(2007,12,03,10,15,30)),
			Arguments.of("2007/12/03 10-15-00"	, GenericTextParser.LOCALDATETIME, "yyyy/MM/dd HH-mm[-ss]", LocalDateTime.of(2007,12,03,10,15)),
			Arguments.of("2007/12/03 10-15-30", GenericTextParser.LOCALDATETIME, "yyyy/MM/dd HH-mm[-ss]", LocalDateTime.of(2007,12,03,10,15,30)),
			Arguments.of("ENUM"	, GenericTextParser.ENUM, "org.bytemechanics.commons.string.GenericTextParser", GenericTextParser.ENUM),
			Arguments.of("LOCALDATE", GenericTextParser.ENUM, "org.bytemechanics.commons.string.GenericTextParser", GenericTextParser.LOCALDATE),
			Arguments.of(Paths.get("/etc/bin").toString(), GenericTextParser.PATH, "", Paths.get("/etc/bin")),
			Arguments.of(Paths.get("/home/usr").toString(), GenericTextParser.STRING, "myMessage", Paths.get("/home/usr").toString())
		);
	}
	@ParameterizedTest(name = "When {0} is formatted with {1} using {2} result should be {3}")
	@MethodSource("formatDataPack")
	public void testFormat(final String result,final GenericTextParser genericTextParser,final String format,final Object value) throws IOException{

		Optional<String> actual=genericTextParser.format(value,format);
		
		Assertions.assertNotNull(actual);
		Assertions.assertTrue(actual.isPresent());
		Assertions.assertEquals(result,actual.get());
	}

	static Stream<Arguments> nulldataPack() {
	    return Stream.of(
			Arguments.of(GenericTextParser.BOOLEAN, null),
			Arguments.of(GenericTextParser.CHAR, null),
			Arguments.of(GenericTextParser.SHORT, null),
			Arguments.of(GenericTextParser.INTEGER, null),
			Arguments.of(GenericTextParser.LONG, null),
			Arguments.of(GenericTextParser.FLOAT, null),
			Arguments.of(GenericTextParser.DOUBLE, null),
			Arguments.of(GenericTextParser.BIGDECIMAL, null),
			Arguments.of(GenericTextParser.STRING	, null),
			Arguments.of(GenericTextParser.LOCALTIME, null),
			Arguments.of(GenericTextParser.LOCALDATETIME, null),
			Arguments.of(GenericTextParser.ENUM, "org.bytemechanics.commons.string.GenericTextParser"),
			Arguments.of(GenericTextParser.PATH, null)
		);
	}
	@ParameterizedTest(name = "When null value is formatted with {0} using {1} result should be empty optional")
	@MethodSource("nulldataPack")
	public void testNullEmptylFormat(final GenericTextParser genericTextParser,final String format) throws IOException{

		Optional<String> actual=genericTextParser.format(null,format);
		
		Assertions.assertNotNull(actual);
		Assertions.assertFalse(actual.isPresent());
	}


	static Stream<Arguments> findDataPack() {
	    return Stream.of(
			Arguments.of(boolean.class	, Optional.of(GenericTextParser.BOOLEAN)),
 			Arguments.of(Boolean.class	, Optional.of(GenericTextParser.BOOLEAN)),
 			Arguments.of(char.class		, Optional.of(GenericTextParser.CHAR)),		
 			Arguments.of(Character.class, Optional.of(GenericTextParser.CHAR)),		
			Arguments.of(short.class	, Optional.of(GenericTextParser.SHORT)),			
 			Arguments.of(Short.class	, Optional.of(GenericTextParser.SHORT)),		
			Arguments.of(int.class		, Optional.of(GenericTextParser.INTEGER)	),		
			Arguments.of(Integer.class	, Optional.of(GenericTextParser.INTEGER)	),		
			Arguments.of(long.class		, Optional.of(GenericTextParser.LONG)),		
 			Arguments.of(Long.class		, Optional.of(GenericTextParser.LONG)),			
 			Arguments.of(float.class	, Optional.of(GenericTextParser.FLOAT)),			
 			Arguments.of(Float.class	, Optional.of(GenericTextParser.FLOAT)),			
			Arguments.of(double.class	, Optional.of(GenericTextParser.DOUBLE)),			
 			Arguments.of(Double.class	, Optional.of(GenericTextParser.DOUBLE)),			
			Arguments.of(BigDecimal.class, Optional.of(GenericTextParser.BIGDECIMAL)),		
			Arguments.of(String.class	, Optional.of(GenericTextParser.STRING)),			
			Arguments.of(LocalTime.class, Optional.of(GenericTextParser.LOCALTIME)),		
			Arguments.of(LocalDateTime.class, Optional.of(GenericTextParser.LOCALDATETIME)),	
			Arguments.of(TimeUnit.class	, Optional.of(GenericTextParser.ENUM)),			
			Arguments.of(Path.class		, Optional.of(GenericTextParser.PATH))
		);
	}
	@ParameterizedTest(name = "When try to find the appropiate parser for {0} the result found is {1}")
	@MethodSource("findDataPack")
	public void testFind(final Class clazz,final Optional<GenericTextParser> genericTextParser) throws IOException{

		Optional<GenericTextParser> actual=GenericTextParser.find(clazz);
		
		Assertions.assertNotNull(actual);
		Assertions.assertTrue(actual.isPresent());
		Assertions.assertEquals(genericTextParser,actual);
	}

	static Stream<Class> findNotFoundDataPack() {
	    return Stream.of(Instant.class,URL.class);
	}
	@ParameterizedTest(name = "When try to find the appropiate parser for {0} the result found is empty")
	@MethodSource("findNotFoundDataPack")
	public void testFindNotFound(final Class clazz) throws IOException{

		Optional<GenericTextParser> actual=GenericTextParser.find(clazz);
		
		Assertions.assertNotNull(actual);
		Assertions.assertFalse(actual.isPresent());
	}

	@SuppressWarnings("UnnecessaryBoxing")
	static Stream<Arguments> findByInstanceDataPack() {
	    return Stream.of(
			Arguments.of(true, Optional.of(GenericTextParser.BOOLEAN)),
 			Arguments.of(Boolean.TRUE, Optional.of(GenericTextParser.BOOLEAN)),	
 			Arguments.of(false, Optional.of(GenericTextParser.BOOLEAN)),	
 			Arguments.of(Boolean.FALSE, Optional.of(GenericTextParser.BOOLEAN)),	
 			Arguments.of(((char)'a'), Optional.of(GenericTextParser.CHAR)),		
 			Arguments.of(new Character((char)'j'), Optional.of(GenericTextParser.CHAR)),			
 			Arguments.of(((char)'d')			, Optional.of(GenericTextParser.CHAR)),		
 			Arguments.of(new Character((char)'i'), Optional.of(GenericTextParser.CHAR)),			
 			Arguments.of(((char)'z'), Optional.of(GenericTextParser.CHAR)),			
 			Arguments.of(new Character((char)'h'), Optional.of(GenericTextParser.CHAR)),			
			Arguments.of(((short)1)	, Optional.of(GenericTextParser.SHORT)),
 			Arguments.of(Short.valueOf((short)2), Optional.of(GenericTextParser.SHORT)),			
			Arguments.of(((int)1)	, Optional.of(GenericTextParser.INTEGER)	),
			Arguments.of(Integer.valueOf((int)100), Optional.of(GenericTextParser.INTEGER)),		
			Arguments.of(100l, Optional.of(GenericTextParser.LONG)),
 			Arguments.of(Long.valueOf(324l), Optional.of(GenericTextParser.LONG)),		
 			Arguments.of(3435.4f, Optional.of(GenericTextParser.FLOAT)),	
 			Arguments.of(Float.valueOf(3435.4f), Optional.of(GenericTextParser.FLOAT)),		
			Arguments.of(232423532.6576d, Optional.of(GenericTextParser.DOUBLE)),		
 			Arguments.of(Double.valueOf(3242151.34d), Optional.of(GenericTextParser.DOUBLE)),
			Arguments.of(new BigDecimal("-1242352.2314"), Optional.of(GenericTextParser.BIGDECIMAL)),
			Arguments.of("myMessage", Optional.of(GenericTextParser.STRING)),
			Arguments.of(LocalTime.now(), Optional.of(GenericTextParser.LOCALTIME)),	
			Arguments.of(LocalDateTime.now(), Optional.of(GenericTextParser.LOCALDATETIME)),
			Arguments.of(TimeUnit.NANOSECONDS, Optional.of(GenericTextParser.ENUM)),
			Arguments.of(Paths.get("/etc"), Optional.of(GenericTextParser.PATH))
		);
	}
	@ParameterizedTest(name = "When try to find the appropiate parser for {0} the result found is {1}")
	@MethodSource("findByInstanceDataPack")
	public void testFindByInstance(final Object value,final Optional<GenericTextParser> genericTextParser) throws IOException{

		Optional<GenericTextParser> actual=GenericTextParser.find(value);
		
		Assertions.assertNotNull(actual);
		Assertions.assertTrue(actual.isPresent());
		Assertions.assertEquals(genericTextParser,actual);
	}
	static Stream<Object> findNotFoundValueDataPack() throws MalformedURLException {
	    return Stream.of(Instant.now(),new URL("http://bytemechanics.org"));
	}
	@ParameterizedTest(name = "When try to find the appropiate parser for {0} the result found is empty")
	@MethodSource("findNotFoundValueDataPack")
	public void testFindNotFoundValue(final Object value) throws IOException{

		Optional<GenericTextParser> actual=GenericTextParser.find(value);
		
		Assertions.assertNotNull(actual);
		Assertions.assertFalse(actual.isPresent());
	}
}
