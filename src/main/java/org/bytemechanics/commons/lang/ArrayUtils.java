package org.bytemechanics.commons.lang;

import java.util.Arrays;

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

/**
 * Array utilities
 * @author afarre
 * @since 1.4.0
 */
public class ArrayUtils {
	
	/**
	 * Concatenate two arrays of the same type
	 * @param <T> Type of the concatenated array
	 * @param _first First array
	 * @param _second Second array
	 * @return array with the two arrays provided concatenated, if one of them is null return the other if both are null return null
	 */
	public static final <T> T[] concat(final T[] _first,final T[] _second){
		
		if(_first==null){
			if(_second==null){
				return null;
			}else{
				return _second;
			}
		}else if(_second==null){
			return _first;
		}else{
			final T[] reply = Arrays.copyOf(_first, _first.length + _second.length);
			System.arraycopy(_second, 0, reply, _first.length, _second.length);		
			return reply;		
		}
	}
}
