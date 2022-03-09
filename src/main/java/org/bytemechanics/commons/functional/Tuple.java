/*
 * Copyright 2017 Byte Mechanics.
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
package org.bytemechanics.commons.functional;

import java.util.Objects;
import java.util.function.Function;


/**
 * Immutable object to keep context of two values
 * @author afarre
 * @since 1.3.0
 * @param <A> first value
 * @param <B> second value
 */
public class Tuple<A,B> {

	private final A firstValue;
	private final B secondValue;

	/**
	 * Tuple constructor
	 * @param _first first param 
	 * @param _second second param
	 */
	public Tuple(final A _first,final B _second) {
		this.firstValue = _first;
		this.secondValue = _second;
	}

	/**
	 * Getter for left value
	 * @return left value
	 */
	public A getLeft() {
		return this.left();
	}
	/**
	 * return left value
	 * @return left value
	 */
	public A left() {
		return this.firstValue;
	}
	/**
	 * Getter for right value
	 * @return right value
	 */
	public B getRight() {
		return this.right();
	}
	/**
	 * return right value
	 * @return right value
	 */
	public B right() {
		return this.secondValue;
	}
	
	/**
	 * Create a clone tuple with the left value replaced with the given one
	 * @param _newValue new value to replace in the cloned tuple
	 * @param <C> type of the new left value
	 * @return clone of this tuple with the left value replaced by _newValue
	 */
	public <C> Tuple<C,B> left(final C _newValue){
		return Tuple.of(_newValue,this.secondValue);
	}
	/**
	 * Create a clone tuple with the left value result of the current left value applying the given converter
	 * @param _converter function to convert the left value
	 * @param <C> type of the new left value
	 * @return clone of this tuple with the left value replaced with the result of apply _converter to the current left value
	 * @since 1.7.0
	 */
	public <C> Tuple<C,B> left(final Function<A,C> _converter){
		return Tuple.of(_converter.apply(this.firstValue),this.secondValue);
	}
	/**
	 * Create a clone tuple with the right value replaced with the given one
	 * @param _newValue new value to replace in the cloned tuple
	 * @param <C> type of the new right value
	 * @return clone of this tuple with the right value replaced by _newValue
	 */
	public <C> Tuple<A,C> right(final C _newValue){
		return Tuple.of(this.firstValue,_newValue);
	}
	/**
	 * Create a clone tuple with the right value result of the current right value applying the given converter
	 * @param _converter function to convert the right value
	 * @param <C> type of the new right value
	 * @return clone of this tuple with the right value replaced with the result of apply _converter to the current right value
	 * @since 1.7.0
	 */
	public <C> Tuple<A,C> right(final Function<B,C> _converter){
		return Tuple.of(this.firstValue,_converter.apply(this.secondValue));
	}
	/**
	 * Create a clone tuple with the new given values
	 * @param _left new left value to replace in the cloned tuple
	 * @param _right new right value to replace in the cloned tuple
	 * @param <C> type of the new left value
	 * @param <D> type of the new right value
	 * @return clone of this tuple with the right and the left values replaced
	 */
	public <C,D> Tuple<C,D> with(final C _left,final D _right){
		return Tuple.of(_left,_right);
	}
	/**
	 * Create a clone tuple with the left and right values converted with the given converters
	 * @param _leftConverter function to convert the right value
	 * @param _rightConverter function to convert the right value
	 * @param <C> type of the new left value
	 * @param <D> type of the new right value
	 * @return clone of this tuple with the right and left value replaced with the result of apply _converters to the current values
	 * @since 1.7.0
	 */
	public <C,D> Tuple<C,D> with(final Function<A,C> _leftConverter,final Function<B,D> _rightConverter){
		return Tuple.of(_leftConverter.apply(this.firstValue),_rightConverter.apply(this.secondValue));
	}
	
	/**
	 * Create a new Tuple form scratch with the given values
	 * @param _left left value
	 * @param _right right value
	 * @param <LEFT> type of the new left value
	 * @param <RIGHT> type of the new right value
	 * @return new Tuple form scratch with the given values
	 */
	public static final <LEFT,RIGHT> Tuple<LEFT,RIGHT> of(final LEFT _left,final RIGHT _right){
		return new Tuple<>(_left,_right);
	}

	/**
	 * @see Object#hashCode() 
	 * @return int hashcode representing this tuple
	 */
	@Override
	public int hashCode() {
		int hash = 3;
		hash = 83 * hash + Objects.hashCode(this.firstValue);
		hash = 83 * hash + Objects.hashCode(this.secondValue);
		return hash;
	}

	/**
	 * @see Object#equals(java.lang.Object) 
	 * @param _tuple tuple to compare with
	 * @return true if this tuple is equals to the provided one
	 */
	@Override
	public boolean equals(final Object _tuple) {
		if (this == _tuple) {
			return true;
		}
		if (_tuple == null) {
			return false;
		}
		if (getClass() != _tuple.getClass()) {
			return false;
		}
		final Tuple<?, ?> other = (Tuple<?, ?>) _tuple;
		if (!Objects.equals(this.firstValue, other.firstValue)) {
			return false;
		}
		return Objects.equals(this.secondValue, other.secondValue);
	}

	
	/**
	 * @see Object#toString() 
	 * @return string representation of this tuple
	 */
	@Override
	public String toString() {
		return String.join("","Tuple[firstValue=",String.valueOf(this.firstValue),", secondValue=",String.valueOf(this.secondValue));
	}
}
