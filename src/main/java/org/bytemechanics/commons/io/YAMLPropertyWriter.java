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
package org.bytemechanics.commons.io;

import java.io.BufferedWriter;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Property writer to yaml
 * @author afarre
 */
public class YAMLPropertyWriter extends FilterWriter{
   
    /**
     * Input property
     */
    public static final class Property implements Comparable<Property>{
        
        /** Property key */
        public final String key;
        /** Property value */
        public final String value;


        public Property(final String _key,final String _value) {
            Objects.requireNonNull(_key,"Property key cannot be null");
            this.key = _key;
            this.value = _value;
        }

        public String getKey() {
            return key;
        }
        public String getValue() {
            return value;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 53 * hash + Objects.hashCode(this.key);
            hash = 53 * hash + Objects.hashCode(this.value);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Property other = (Property) obj;
            if (!Objects.equals(this.key, other.key)) {
                return false;
            }
            return Objects.equals(this.value, other.value);
        }

        @Override
        public int compareTo(final Property _other) {
            return this.key.compareTo(_other.key);
        }
    }

    /**
     * Internal class to store each property tree name with the key and value
     */
    protected static final class YamlEntry{

        
        /** Depth of the key */
        public final int depth;
        /** Property key */
        public final String key;
        /** Property key */
        public final String name;
        /** Property value */
        public final String value;
        /** Flag to indicate if is list */
        public final boolean list;
        /** Flag to indicate that child write has been done */
        public boolean started;

        
        public YamlEntry(final int _depth,final String _key,final String _value) {
            this.depth=_depth;
            this.value=_value;
            this.key=_key;
            this.name=Optional.ofNullable(_key)
                                .filter(ky -> ky.charAt(ky.length()-1)==']')
                                .map(ky -> ky.substring(0,ky.indexOf('[')))
                                .orElse(_key);
            this.list=(_key==null)? true : _key.charAt(_key.length()-1)==']';
            this.started=false;            
        }

        public boolean isList(){
            return this.list;
        }
        public int getTab(){
            return (isList())? depth+1 : depth;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 71 * hash + this.depth;
            hash = 71 * hash + Objects.hashCode(this.key);
            return hash;
        }

        @Override
        public boolean equals(final Object _other) {
            if (this == _other) {
                return true;
            }
            if (_other == null) {
                return false;
            }
            if (getClass() != _other.getClass()) {
                return false;
            }
            final YamlEntry other = (YamlEntry) _other;
            if (this.depth != other.depth) {
                return false;
            }
            return Objects.equals(this.key, other.key);
        }

        @Override
        public String toString() {
            return "YamlEntry{" + "depth=" + depth + ", key=" + key + ", name=" + name + ", value=" + value + ", list=" + list + ", started=" + started + '}';
        }
    }
    
    /** 
     * Flag to indicate if length property must be ignored<br>
     * Example:<br>
     * <pre>
     *   spec.containers[0].ports[0].containerPort: 80
     *   spec.containers[0].ports.length: 1 //do not append
     * </pre>
     */
    private final boolean ignoreListLength;
    /**
     * Cache to store the current tree position
     */
    private final SortedMap<Integer,YamlEntry> entryPathCache;
    
    
    /** 
     * Default constructor with ignoreListLength set true 
     * @param _writer parent reader
     */
    public YAMLPropertyWriter(Writer _writer) {
        this(_writer,true);
    }
    /** 
     * Constructor capable to define ignoreListLength value
     * @param _writer parent reader
     * @param _ignoreListLength ignore list length and do not append it
     */
    public YAMLPropertyWriter(Writer _writer,final boolean _ignoreListLength) {
        super((_writer instanceof BufferedWriter)
                ? (BufferedWriter) _writer
                : new BufferedWriter(_writer));
        this.ignoreListLength=_ignoreListLength;
        entryPathCache = new TreeMap<>();
    }    
    
    
    /**
     * Append to the underlaying stream the correct yaml format for the given fields
     * @param _depth number of tabs to use (double space)
     * @param _name key name
     * @param _value value
     * @param _isList flag to indicate if is a list
     * @throws java.io.IOException if something happens
     */
    protected final void append(final int _depth,final String _name,final String _value,final boolean _isList) throws IOException{
        
        final String cleanName=Objects.toString(_name,"").trim();
        final String cleanValue=Objects.toString(_value,"").trim();

        try{
            //Validations
            if((cleanName.isEmpty())&&(cleanValue.isEmpty()))
                throw new NullPointerException("Both _name and _value cannot be null or empty");
            if((cleanName.isEmpty())&&(!_isList))
                throw new NullPointerException("_value can be null or empty only if _isList is true");
            //Depth tabs
            for(int ic1=0;ic1<_depth;ic1++){
                append("  ");
            }
            if(_isList){
                append("- ");
            }
            if(!cleanName.isEmpty()){
                append(cleanName).append(':');
                if(!cleanValue.isEmpty()){
                    append(' ');
                }
            }
            if(!cleanValue.isEmpty()){
                append(cleanValue);
            }
            append(System.lineSeparator());
        }catch(NullPointerException e){
            throw new IOException(e);
        }
    }
    
    /**
     * Accumulate the current entryPathCache tabulation considering that each list counts double
     * @return number of tabs in entryPathCache counting lists as double
     */
    protected int calculateTabs(){
        return this.entryPathCache.values()
                                 .stream()
                                    .map(entry -> (entry.isList())? 2 : 1)
                                    .collect(Collectors.summingInt(tab -> tab));
    }
    
    /**
     * Write down the given yaml entry
     * @param _yamlEntry property to append
     */
    protected void append(final YamlEntry _yamlEntry){
        
        try {
            final YamlEntry currentEntry=this.entryPathCache.get(_yamlEntry.depth);
            if(!_yamlEntry.equals(currentEntry)){
                //Clear other branch entries
                if(currentEntry!=null){
                    for(int ic1=this.entryPathCache.lastKey();ic1>=_yamlEntry.depth;ic1--){
                        this.entryPathCache.remove(ic1);
                    }
                    if(_yamlEntry.name.equals(currentEntry.name)){
                        this.entryPathCache.put(_yamlEntry.depth, _yamlEntry);
                    }
                }
                if(currentEntry==null||!_yamlEntry.name.equals(currentEntry.name)){
                    final int tab=calculateTabs();
                    //Add new entry to the tree
                    if(_yamlEntry.value==null){
                        this.entryPathCache.put(_yamlEntry.depth, _yamlEntry);
                    }
                    //Append to underlaying stream
                    final int parentDepth=_yamlEntry.depth-1;
                    final YamlEntry parentEntry=this.entryPathCache.get(parentDepth);
                    if(parentEntry!=null){
                        final boolean isLength=("length".equals(_yamlEntry.name))&&(parentEntry.key.endsWith("[*]"));
                        int adjustedTab=(((parentEntry.isList())&&(!parentEntry.started))||(_yamlEntry.key==null))? tab-1 : tab;
                        adjustedTab=(isLength)? adjustedTab-1 : adjustedTab;
                        final String finalName=(isLength)? parentEntry.name+"Length" : _yamlEntry.name;
                        append(adjustedTab,finalName,_yamlEntry.value,((parentEntry.isList())&&(!parentEntry.started)&&(!isLength)));
                        parentEntry.started=true;
                    }else{
                        append(tab,_yamlEntry.name,_yamlEntry.value,false);
                    }
                }
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
    
    /**
     * Return true if ignoreListLength is disabled or the property is not length (ends with [*].length)
     * @param _property property to validate
     * @return true if and only if ignoreListLength is disabled or the property is not length 
     */
    protected final boolean filterLength(final Property _property){
        return (!this.ignoreListLength)||(!_property.key.endsWith("[*].length"));
    }

    /**
     * Divide the given _property into a stream of YamlEntry
     * @param _property property to convert
     * @return stream of YamlEntry corresponding to the given _property
     */
    protected Stream<YamlEntry> splitPath(final Property _property){

        final String[] keyPath=_property.key.split("\\.");
        final AtomicInteger counter=new AtomicInteger();
        
        final Stream<String> keyPathStream;
        final int totalKeys;
        if(_property.key.charAt(_property.key.length()-1)==']'){
            keyPathStream=Stream.concat(Stream.of(keyPath), Stream.of((String)null));
            totalKeys=keyPath.length+1;
        }else{
            totalKeys=keyPath.length;
            keyPathStream=Stream.of(keyPath);
        }
        return keyPathStream
                    .sequential()
                    .map(key -> new YamlEntry(counter.getAndIncrement(), key,(counter.get()==totalKeys)? _property.value : null));
    }
    
    /**
     * Write down all stream elements until end of stream
     * IMPORTANT: its mandatory to provide the stream ordered, otherwise will have unexpected results
     * @param _stream stream of properties to append
     */
    public void write(Stream<Property> _stream){
        _stream.filter(this::filterLength)
                .flatMap(this::splitPath)
                .forEach(this::append);
    }

    /**
     * Write down the given property
     * @param _property property to append
     */
    public void append(final Property _property){
        if(filterLength(_property)){
            splitPath(_property)
                .forEach(this::append);
        }
    }
}
