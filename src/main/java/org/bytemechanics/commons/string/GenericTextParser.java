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

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bytemechanics.commons.functional.LambdaUnchecker;

/**
 * Generic type text parser and formatter
 * @author afarre
 * @since 1.1.0
 * @since 1.3.0 
 */
public enum GenericTextParser{
	
	BOOLEAN(Boolean.class,(value,config) -> value.filter(val -> !val.isEmpty()).map(Boolean::valueOf),boolean.class),
    CHAR(Character.class,(value,config) -> value.filter(val -> !val.isEmpty()).map(val -> val.charAt(0)),char.class),
	/**
	 * Default format: #0
	 * @see DecimalFormat
	 */
	SHORT(Short.class,	(value,config) -> value.filter(val -> !val.isEmpty())
												.map(val -> config.map(conf -> LambdaUnchecker.uncheckedGet(() -> new DecimalFormat(conf, new DecimalFormatSymbols(Locale.ENGLISH)).parse(val)))
												.map(decimal -> decimal.shortValue())
												.orElseGet(() -> Short.valueOf(val))),
						(value,config) -> value.map(val -> config.map(conf -> new DecimalFormat(conf, new DecimalFormatSymbols(Locale.ENGLISH)).format(val))
												.orElseGet(() -> String.valueOf(val))),short.class),
	/**
	 * Default format: #0
	 * @see DecimalFormat
	 */
	INTEGER(Integer.class,(value,config) -> value.filter(val -> !val.isEmpty())
													.map(val -> config.map(conf -> LambdaUnchecker.uncheckedGet(() -> new DecimalFormat(conf, new DecimalFormatSymbols(Locale.ENGLISH)).parse(val)))
													.map(decimal -> decimal.intValue())
													.orElseGet(() -> Integer.valueOf(val))),
						(value,config) -> value.map(val -> config.map(conf -> new DecimalFormat(conf, new DecimalFormatSymbols(Locale.ENGLISH)).format(val))
												.orElseGet(() -> String.valueOf(val))),int.class),
	/**
	 * Default format: #0
	 * @see DecimalFormat
	 */
	LONG(Long.class,(value,config) -> value.filter(val -> !val.isEmpty())
											.map(val -> config.map(conf -> LambdaUnchecker.uncheckedGet(() -> new DecimalFormat(conf, new DecimalFormatSymbols(Locale.ENGLISH)).parse(val)))
											.map(decimal -> decimal.longValue())
											.orElseGet(() -> Long.valueOf(val))),
						(value,config) -> value.map(val -> config.map(conf -> new DecimalFormat(conf, new DecimalFormatSymbols(Locale.ENGLISH)).format(val))
												.orElseGet(() -> String.valueOf(val))),long.class),
	/**
	 * Default format: #0.0
	 * @see DecimalFormat
	 */
	FLOAT(Float.class,(value,config) -> value.filter(val -> !val.isEmpty())
												.map(val -> config.map(conf -> LambdaUnchecker.uncheckedGet(() -> new DecimalFormat(conf, new DecimalFormatSymbols(Locale.ENGLISH)).parse(val)))
												.map(decimal -> decimal.floatValue())
												.orElseGet(() -> Float.valueOf(val))),
						(value,config) -> value.map(val -> config.map(conf -> new DecimalFormat(conf, new DecimalFormatSymbols(Locale.ENGLISH)).format(val))
												.orElseGet(() -> val.toString())),float.class),
	/**
	 * Default format: #0.0
	 * @see DecimalFormat
	 */
	DOUBLE(Double.class,(value,config) -> value.filter(val -> !val.isEmpty())
												.map(val -> config.map(conf -> LambdaUnchecker.uncheckedGet(() -> new DecimalFormat(conf, new DecimalFormatSymbols(Locale.ENGLISH)).parse(val)))
												.map(decimal -> decimal.doubleValue())
												.orElseGet(() -> Double.valueOf(val))),
						(value,config) -> value.map(val -> config.map(conf -> new DecimalFormat(conf, new DecimalFormatSymbols(Locale.ENGLISH)).format(val))
												.orElseGet(() -> val.toString())),double.class),
	/**
	 * Default format: #,##0.0#
	 * @see DecimalFormat
	 */
	BIGDECIMAL(BigDecimal.class,"#,##0.0#",(value,config) -> value.filter(val -> !val.isEmpty())
																	.map(val -> LambdaUnchecker.uncheckedGet(() -> {
																				DecimalFormat format=new DecimalFormat(config.get(), new DecimalFormatSymbols(Locale.ENGLISH));
																				format.setParseBigDecimal(true);
																				return format.parse(val);
																			}))
																	.map(decimal -> (BigDecimal)decimal),
							(value,config) -> value.map(new DecimalFormat(config.get(), new DecimalFormatSymbols(Locale.ENGLISH))::format)),
    STRING(String.class,(value,config) -> value.map(String::valueOf)),
	/**
	 * Default format: DateTimeFormatter.ISO_TIME
	 * @see DateTimeFormatter
	 */
	LOCALTIME(LocalTime.class,(value,config) -> value.filter(val -> !val.isEmpty())
														.map(val -> config.map(conf -> LambdaUnchecker.uncheckedGet(() -> LocalTime.parse(val, DateTimeFormatter.ofPattern(conf,Locale.ENGLISH))))
														.orElseGet(() -> LocalTime.parse(val))),
								(value,config) -> value.map(val -> config.map(conf -> LambdaUnchecker.uncheckedGet(() -> DateTimeFormatter.ofPattern(conf,Locale.ENGLISH).format(val)))
														.orElseGet(() -> val.toString()))),
	/**
	 * Default format: DateTimeFormatter.ISO_DATE
	 * @see DateTimeFormatter
	 */
	LOCALDATE(LocalDate.class,(value,config) -> value.filter(val -> !val.isEmpty())
														.map(val -> config.map(conf -> LambdaUnchecker.uncheckedGet(() -> LocalDate.parse(val, DateTimeFormatter.ofPattern(conf,Locale.ENGLISH))))
														.orElseGet(() -> LocalDate.parse(val))),
								(value,config) -> value.map(val -> config.map(conf -> LambdaUnchecker.uncheckedGet(() -> DateTimeFormatter.ofPattern(conf,Locale.ENGLISH).format(val)))
														.orElseGet(() -> val.toString()))),
	/**
	 * Default format: DateTimeFormatter.ISO_DATE_TIME
	 * @see DateTimeFormatter
	 */
	LOCALDATETIME(LocalDateTime.class,(value,config) -> value.filter(val -> !val.isEmpty())
																.map(val -> config.map(conf -> LambdaUnchecker.uncheckedGet(() -> LocalDateTime.parse(val, DateTimeFormatter.ofPattern(conf,Locale.ENGLISH))))
																.orElseGet(() -> LocalDateTime.parse(val))),
									(value,config) -> value.map(val -> config.map(conf -> LambdaUnchecker.uncheckedGet(() -> DateTimeFormatter.ofPattern(conf,Locale.ENGLISH).format(val)))
															.orElseGet(() -> val.toString()))),
	ENUM(Enum.class,(value,config) -> value.filter(val -> !val.isEmpty())
											.map(val -> LambdaUnchecker.uncheckedGet(() ->  Enum.valueOf((Class<Enum>)Class.forName(config.get()),val))),
					(value,config) -> value.map(val -> val.name())),
	PATH(Path.class,(value,config) -> value.filter(val -> !val.isEmpty())
											.map(Paths::get),
					(value,config) -> value.map(Path::toString)),
	;


	/**
	 * Converter definition interface to store all necessary conversion methods and data
	 * @author afarre
	 * @since 1.1.0
	 */
	interface ConverterAdapter<T>{
		/**
		 * Converter target main class
		 * @return converter target main class
		 */
		public Class<T> getTarget();
		/**
		 * Converter target synonims (for example target: Integer - synonim: int)
		 * @return Array of target synonim classes
		 */
		public Class[] getSynonyms();
		/**
		 * Converter default format if no additional format is provided
		 * @return Optional of format in it's string expression
		 */
		public default Optional<String> getFormat(){
			return Optional.empty();
		}
		/**
		 * Retrieves a set of all possible classes for this converter (target + synonims)
		 * @return set of all possible classes for this converter (target + synonims)
		 */
		public default Set<Class> getClasses(){
			return Stream.concat(Stream.of(getTarget()),Stream.of(getSynonyms()))
							.collect(Collectors.toSet());
		}
		/**
		 * Retrieves a bifunction that parses the first parameter given string and parses if with the given format or the default one
		 * @return bifunction that parses the first parameter given string and parses if with the given format or the default one
		 * @see BiFunction
		 */
		public BiFunction<Optional<String>,Optional<String>,Optional<T>> getParser();
		/**
		 * Retrieves a bifunction that converts target object class given as first parameter or any of its synonims with the given format or the default one
		 * @return bifunction that converts target object class given as first parameter or any of its synonims with the given format or the identity
		 * @see BiFunction
		 */
		public default BiFunction<Optional<T>,Optional<String>,Optional<String>> getFormatter(){
			return (value,config) -> value.map(String::valueOf);
		}
	}
	/**
	 * Converter bean
	 * @see ConverterAdapter
	 * @author afarre
	 * @since 1.1.0
	 */
	class Converter<T> implements ConverterAdapter<T>{

		private final Class<T> target;
		private final Class[] synonyms;
		private final String defaultConfiguration;
		private final BiFunction<Optional<String>,Optional<String>,Optional<T>> parser;
		private final BiFunction<Optional<T>,Optional<String>,Optional<String>> formatter;
		
		public Converter(final Class<T> _target,final String _defaultConfiguration,final BiFunction<Optional<String>,Optional<String>,Optional<T>> _parser,final Class... _synonyms){
			this(_target,_defaultConfiguration, _parser, null, _synonyms);
		}
		public Converter(final Class<T> _target,final BiFunction<Optional<String>,Optional<String>,Optional<T>> _parser,final Class... _synonyms){
			this(_target,null, _parser, null, _synonyms);
		}
		public Converter(final Class<T> _target,final BiFunction<Optional<String>,Optional<String>,Optional<T>> _parser,final BiFunction<Optional<T>,Optional<String>,Optional<String>> _formatter,final Class... _synonyms){
			this(_target, null, _parser, _formatter, _synonyms);
		}
		public Converter(final Class<T> _target,final String _defaultConfiguration,final BiFunction<Optional<String>,Optional<String>,Optional<T>> _parser,final BiFunction<Optional<T>,Optional<String>,Optional<String>> _formatter,final Class... _synonyms){
			this.target=_target;
			this.defaultConfiguration=_defaultConfiguration;
			this.parser=_parser;
			this.formatter=_formatter;
			this.synonyms=_synonyms;
		}
		
		/**
		 * @see ConverterAdapter#getTarget() 
		 */
		@Override
		public Class<T> getTarget() {
			return this.target;
		}
		/**
		 * @see ConverterAdapter#getFormat() 
		 */
		@Override
		public Optional<String> getFormat(){
			return Optional.ofNullable(this.defaultConfiguration);
		}
		/**
		 * @see ConverterAdapter#getSynonyms() 
		 */
		@Override
		public Class[] getSynonyms() {
			return this.synonyms;
		}
		/**
		 * @see ConverterAdapter#getParser() 
		 */
		@Override
		public BiFunction<Optional<String>, Optional<String>, Optional<T>> getParser() {
			return this.parser;
		}
		/**
		 * @see ConverterAdapter#getFormatter()
		 */
		@Override
		public BiFunction<Optional<T>, Optional<String>, Optional<String>> getFormatter() {
			return Optional.ofNullable(this.formatter)
					.orElseGet(ConverterAdapter.super::getFormatter);
		}
	}

	private final ConverterAdapter converter;
    
    <T> GenericTextParser(final Class<T> _class,BiFunction<Optional<String>,Optional<String>,Optional<T>> _parser,final Class... _synonyms){
		this.converter=new Converter(_class, _parser, _synonyms);
    }
    <T> GenericTextParser(final Class<T> _class,BiFunction<Optional<String>,Optional<String>,Optional<T>> _parser,BiFunction<Optional<T>,Optional<String>,Optional<String>> _formatter,final Class... _synonyms){
		this.converter=new Converter(_class, _parser, _formatter, _synonyms);
    }
    <T> GenericTextParser(final Class<T> _class,final String _defaultConfiguration,BiFunction<Optional<String>,Optional<String>,Optional<T>> _parser,BiFunction<Optional<T>,Optional<String>,Optional<String>> _formatter,final Class... _synonyms){
		this.converter=new Converter(_class,_defaultConfiguration,_parser, _formatter, _synonyms);
    }

	/**
	 * Convert the given object to string with default format
	 * @param _object object to format
	 * @return an optional of the object converted to string (if the given object can not be converted, its not the correct type, throws an exception)
	 */
	public Optional<String> format(final Object _object){
		return format(_object,null);
	}
	/**
	 * Convert the given object to string with the given format if not null, otherwise uses default format
	 * @param _object object to format
	 * @param _format format to use to convert object to string (can be null)
	 * @return an optional of the object converted to string (if the given object can not be converted, its not the correct type, throws an exception)
	 */
	public Optional<String> format(final Object _object,final String _format){
		return	(Optional<String>)this.converter
										.getFormatter()
											.apply(Optional.ofNullable(_object)
													,Optional.ofNullable(_format)
																.filter(value -> !value.isEmpty())
																.map(Optional::of)
																.orElseGet(this.converter::getFormat));
	}
	/**
	 * Convert the given string to the intended _class usign default format
	 * @param <T> type of the class
	 * @param _class Class which we want once converted
	 * @param _string String to convert
	 * @return an optional of the string converted to object (if the given string can not be converted or enum target or its not castable to _class, throws an exception)
	 * @throws ClassCastException if the enum target its not castable to the given _class
	 * @throws DateTimeParseException if is parsing a date/time or datetime
	 * @throws NumberFormatException if is parsing a non parseable numeric
	 */
	public <T> Optional<T> parse(final Class<T> _class,final String _string){
		return parse(_string)
					.map(val -> (T)val);
	}
	/**
	 * Convert the given string to enum target usign default format
	 * @param _string String to convert
	 * @return an optional of the string converted to  enum target (if the given string can not be converted, throws an exception)
	 * @throws DateTimeParseException if is parsing a date/time or datetime
	 * @throws NumberFormatException if is parsing a non parseable numeric
	 */
	public Optional<Object> parse(final String _string){
		return parse(_string,null);
	}
	/**
	 * Convert the given string to the intended _class usign the given format if not null, otherwise uses default format
	 * @param <T> type of the class
	 * @param _class Class which we want once converted
	 * @param _string String to convert
	 * @param _format format to use to convert object to string (can be null)
	 * @return an optional of the string converted to _class (if the given string can not be converted or enum target its not castable to _class, throws an exception)
	 * @throws ClassCastException if the enum target its not castable to the given _class
	 * @throws DateTimeParseException if is parsing a date/time or datetime
	 * @throws NumberFormatException if is parsing a non parseable numeric
	 */
	public <T> Optional<T> parse(final Class<T> _class,final String _string,final String _format){
		return parse(_string,_format)
					.map(val -> (T)val);
	}
	/**
	 * Convert the given string to enum target usign the given format if not null, otherwise uses default format
	 * @param _string String to convert
	 * @param _format format to use to convert object to string (can be null)
	 * @return an optional of the string converted to enum target (if the given string can not be converted, throws an exception)
	 * @throws DateTimeParseException if is parsing a date/time or datetime
	 * @throws NumberFormatException if is parsing a non parseable numeric
	 */
	public Optional<Object> parse(final String _string,final String _format){
		return	(Optional<Object>)this.converter
										.getParser()
											.apply(Optional.ofNullable(_string)
																
													,Optional.ofNullable(_format)
																.filter(value -> !value.isEmpty())
																.map(Optional::of)
																.orElseGet(this.converter::getFormat));
	}
	
	
	/**
	 * Try to convert the given object to string usign default format
	 * @param _object object to format
	 * @return an optional of the object converted to string (if the given object can not be converted, its not the correct type, throws an exception)
	 * @since 1.2.0
	 */
	public static Optional<GenericTextParser> find(final Object _object){
		return Optional.of(_object)
					.map(Object::getClass)
					.flatMap(GenericTextParser::find);
	}
	/**
	 * Try to return an available parser for the given class
	 * @param _class class to parse
	 * @return an optional of the appropiate converter or an empty optional
	 * @since 1.2.0
	 */
	public static Optional<GenericTextParser> find(final Class _class){
		return Optional.of(_class)
					.flatMap(valueClass -> Stream.of(values())
												.filter(converter -> converter.converter
																					.getClasses()
																						.stream()
																							.anyMatch(clazz -> ((Class)clazz).isAssignableFrom(_class)))
												.findAny());
	}
	
	/**
	 * Try to convert the given object to string usign default format
	 * @param _object object to format
	 * @return an optional of the object converted to string (if the given object can not be converted, its not the correct type, throws an exception)
	 */
	public static final Optional<String> toFormattedString(final Object _object){
		return toFormattedString(_object,null);
	}
	/**
	 * Try to convert the given object to string usign the given format if not null, otherwise uses default format
	 * @param _object object to format
	 * @param _format format to use to convert object to string (can be null)
	 * @return an optional of the object converted to string (if the given object can not be converted, its not the correct type, throws an exception)
	 */
	public static final Optional<String> toFormattedString(final Object _object,final String _format){
		return Optional.ofNullable(_object)
					.flatMap(GenericTextParser::find)
					.flatMap(converter -> converter.format(_object, _format));
	}

	/**
	 * Try to convert the given string to the intended _class usign default format
	 * @param <T> type of the class
	 * @param _class Class which we want once converted
	 * @param _string String to convert
	 * @return an optional of the string converted to _class if any suitable converter found for the given _class
	 * @throws DateTimeParseException if is parsing a date/time or datetime
	 * @throws NumberFormatException if is parsing a non parseable numeric
	 */
	public static final <T> Optional<T> toValue(final Class<T> _class,final String _string){
		return toValue(_class,_string,null);
	}
	/**
	 * Try to convert the given string to the intended _class usign the given format if not null, otherwise uses default format
	 * @param <T> type of the class
	 * @param _class Class which we want once converted
	 * @param _string String to convert
	 * @param _format format to use to convert object to string (can be null)
	 * @return an optional of the string converted to _class if any suitable converter found for the given _class
	 * @throws DateTimeParseException if is parsing a date/time or datetime
	 * @throws NumberFormatException if is parsing a non parseable numeric
	 */
	public static final  <T> Optional<T> toValue(final Class<T> _class,final String _string,final String _format){
		return Optional.ofNullable(_string)
					.flatMap(object -> find(_class))
					.flatMap(converter -> converter.parse(_string, _format))
					.map(value -> (T)value);
	}
}
