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


/**
 * Immutable object to keep context of two values
 * @author afarre
 * @since 1.3.0
 * @param <A>
 * @param <B>
 */
public class Tuple<A,B> {

	private final A firstValue;
	private final B secondValue;

	/**
	 * Tuple consutructor
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
	
	public <C> Tuple<C,B> left(C _newValue){
		return Tuple.of(_newValue,this.secondValue);
	}
	public <C> Tuple<A,C> right(C _newValue){
		return Tuple.of(this.firstValue,_newValue);
	}
	public <C,D> Tuple<C,D> with(C _left,D _right){
		return Tuple.of(_left,_right);
	}
	
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
