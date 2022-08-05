/*
 * Copyright 2022 Byte Mechanics.
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

import java.util.Enumeration;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * Enumeration spliterator ORDERED, NONNULL and IMMUTABLE utility class 
 * @param <T> enumeration type
 * @@link https://stackoverflow.com/questions/33242577/how-do-i-turn-a-java-enumeration-into-a-stream
 * @author afarre
 */
public class EnumerationSpliterator<T> implements Spliterator<T>{

    private final Enumeration<T> underlayingEnumeration;
    
    /**
     * Constructor receiving the underlaying enumeration to be converted to Splitterator
     * @param _underlayingEnumeration enumeration
     */
    public EnumerationSpliterator(final Enumeration<T> _underlayingEnumeration){
        this.underlayingEnumeration=Objects.requireNonNull(_underlayingEnumeration,"Cannot spliterate null Enumeration");
    }
    
    
    @Override
    public boolean tryAdvance(final Consumer<? super T> _action) {
        
        boolean reply=false;
        
        if(this.underlayingEnumeration.hasMoreElements()){
            final T val=this.underlayingEnumeration.nextElement();
            if(val!=null){
                _action.accept(val);
                reply=true;
            }
        }
        
        return reply;
    }

    @Override
    public Spliterator<T> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return Long.MAX_VALUE;
    }

    @Override
    public int characteristics() {
        return Spliterator.ORDERED|Spliterator.NONNULL|Spliterator.IMMUTABLE;
    }
}
