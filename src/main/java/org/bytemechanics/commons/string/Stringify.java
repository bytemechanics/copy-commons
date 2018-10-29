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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * To string utility converter
 * @author afarre
 */
public final class Stringify {

	/**
	 * Prints stacktrace as string (null protected).
	 * @param _stacktrace exception to generate stacktrace
	 * @return Stacktrace in string format and if any error happens return a text with the error
	 */
	public static final String toString(final Throwable _stacktrace){

		if(_stacktrace==null)
			return "null";
		try(StringWriter stringWriter = new StringWriter();
				PrintWriter printWriter=new PrintWriter(stringWriter)){
			_stacktrace.printStackTrace(printWriter);
			return stringWriter.toString();
		} catch (Exception e) {
			return "<unable to print stacktrace: "+String.valueOf(e.getMessage())+">";
		}
	}

	/**
	 * Format duration as string (null protected)
	 * @param _duration exception to generate stacktrace
	 * @param _pattern pattern to use if null uses ISO_TIME
	 * @return duration in format provided or string with an error if failed
	 */
	public static final String toString(final Duration _duration,final String _pattern){

		try{
			if(_duration==null)
				return "null";
			final DateTimeFormatter formatter=Optional.ofNullable(_pattern)
												.map(DateTimeFormatter::ofPattern)
												.orElse(DateTimeFormatter.ISO_TIME);
			return formatter.format(LocalTime.MIDNIGHT.plus(_duration));
		}catch(Exception e){
			return "<unable to print stacktrace: "+String.valueOf(e.getMessage())+">";
		}
	}
}
