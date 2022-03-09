/*
 * Copyright 2021 Byte Mechanics.
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

import java.io.BufferedReader;
import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Simple yaml parser to convert to key-value pairs
 * 
 * Important notes:
 * <ul>
 *   <li>Multi-document yaml not supported (please avoid the triple-hyphens syntax)</li>
 * </ul>
 * @author afarre
 */
public class YAMLPropertyReader extends FilterReader {

    /**
     * Returned property
     */
    public static final class Property {

        /** Property key */
        private final String key;
        /** Property value */
        private final String value;

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
            hash = 43 * hash + Objects.hashCode(this.key);
            hash = 43 * hash + Objects.hashCode(this.value);
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
    }

    /** 
     * Flag to indicate if an additional property must be added to indicate the property list length<br>
     * Example:<br>
     * <pre>
     *   spec.containers[0].ports[0].containerPort: 80
         spec.containers[0].ports.length: 1
     * </pre>
     */
    private final boolean appendListLength;
    
    /** 
     * Default constructor with appendListLength set true 
     * @param _reader parent reader
     */
    public YAMLPropertyReader(Reader _reader) {
        this(_reader, true);
    }
    /** 
     * Constructor capable to define appendListLength value
     * @param _reader parent reader
     * @param _appendListLength append list length as an additional property
     */
    public YAMLPropertyReader(Reader _reader,final boolean _appendListLength) {
        super((_reader instanceof BufferedReader)
                ? (BufferedReader) _reader
                : new BufferedReader(_reader));
        this.appendListLength=_appendListLength;
    }

    /**
     * Internal single line entry readed object
     */
    protected class Entry {

        /** tabs read */
        private final int tab;
        /** if has the list indicator */
        private final boolean listItem;
        /** entry key */
        private final String key;
        /** entry value */
        private final String value;
        

        public int getTab() {
            return tab;
        }
        public boolean isListItem() {
            return listItem;
        }
        public String getKey() {
            return key;
        }
        public String getValue() {
            return value;
        }
        
        /** 
         * Parse the provided line and convert to Entry
         * @param _line text line to parse
         * @throws UncheckedIOException if cannot parse the provided _line
         */
        public Entry(final String _line){
            
            boolean entrylistItem=false;
            int entryTab = 0;
            String entryKey=null;
            String entryValue;

            try {
                int ic1 = 0;
                for (; (ic1 < _line.length() - 1) && (_line.charAt(ic1) == ' '); ic1++) {
                    final char nextChar=_line.charAt(++ic1);
                    if (' ' != nextChar)
                        throw new IOException("Wrong tab at character " + ic1 + ", tabs must be double blank");
                    entryTab++;
                }
                if ('-' == _line.charAt(ic1)) {
                    if (' ' != _line.charAt(ic1+1))
                        throw new IOException("Wrong character at " + ic1 + " list items must have blank between - and the key/value");
                    entrylistItem=true;
                    ic1+=2;
                }
                int keyValSeparator=_line.indexOf(':',ic1);
                if(keyValSeparator!=-1){
                    entryKey=_line.substring(ic1,keyValSeparator).trim();
                    if(entryKey.isEmpty())
                        throw new IOException("Key cannot be empty at character " + ic1 + ", keys must have some content distinct from blank");
                    ic1=keyValSeparator+1;
                }
                int commentsSeparator=_line.indexOf('#',ic1);
                int valueEnd=_line.length();
                if((commentsSeparator!=-1)&&(' '==_line.charAt(commentsSeparator-1))){
                    valueEnd=commentsSeparator-1;
                }
                entryValue=_line.substring(ic1,valueEnd).trim();
                if(entryValue.isEmpty()){
                    if(entryKey==null)
                        throw new IOException("Key and value cannot be empty at character " + ic1);
                    entryValue=null;
                }
                this.tab=entryTab;
                this.listItem=entrylistItem;
                this.key=entryKey;
                this.value=entryValue;
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }
    }

    /** 
     * Read and parse next available line with actual content (ignoring commnets) and parses to Entry.
     * @return Optional empty if there are no more lines to read or 
     * @throws ParseException if cannot parse the provided _line
     */
    protected Optional<Entry> readLine() throws ParseException {

        Optional<Entry> reply=Optional.empty();
        
        try {
            Optional<Entry> parsedLine=Optional.empty();
            Optional<String> readLine;
            do{
                readLine=Optional.ofNullable(((BufferedReader) this.in).readLine());
                parsedLine=readLine
                                .filter(line -> !line.trim().isEmpty())
                                .filter(line -> !line.trim().startsWith("#"))
                                .map(Entry::new);
            }while((readLine.isPresent())&&(!parsedLine.isPresent()));
            reply=(readLine.isPresent())? parsedLine : reply;
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        
        return reply;
    }

    /**
     * Stream properties populating them ordered
     * @return Stream of Properties
     */
    public Stream<Property> stream() {
        
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<Property>() {
                  
                    /**
                     * Internal class Key to have a detailed key info
                     */
                    class Key{
                        /** key name */
                        String key=null;
                        /** key depth (equivalent to entry.tab) */
                        int depth=0;
                        /** key list counter to keep an index of the items in the key list */
                        int listCounter=-1;
                        
                        public Key(final String _key,final int _depth,final int _listCounter){
                            this.key=_key;
                            this.depth=_depth;
                            this.listCounter=_listCounter;
                        }
                        
                        /**
                         * Key non null check
                         * @return true if key is not null
                         */
                        public boolean keyNonNull(){
                            return this.key!=null;
                        }

                        /**
                         * Builds a key into string key path
                         * @return key path in string format
                         */
                        @Override
                        public String toString(){
                            return (listCounter>-1)? String.join("",key,"[",String.valueOf(listCounter),"]") : key;
                        }
                    }
                    /** Class only used to render length keys */
                    class LengthKey extends Key{

                        public LengthKey(String _key, int _depth, int _listCounter) {
                            super(_key, _depth, _listCounter);
                        }
                        
                        /**
                         * Builds a key into string key path
                         * @return key path in string format
                         */
                        @Override
                        public String toString(){
                            return key.concat("[*]");
                        }
                    }
                    
                    private final List<Key> keys=new ArrayList<>();
                    private final Queue<Property> propertyBuffer = new LinkedList<>();

                    /**
                     * Adds an additional property length to return buffer if appendListLength is true
                     */
                    private void addPropertyLength(){
                        
                        if(appendListLength){
                            final Key current=keys.get(keys.size()-1);
                            if(current.listCounter>-1){
                                Stream<Key> tempStream=Stream.<Key>concat(keys.stream().limit(keys.size()-1), Stream.of(new LengthKey(current.key,current.depth,-1)));
                                tempStream=Stream.concat(tempStream, Stream.of(new Key("length",0, -1)));
                                final String lengthKey=writeKeyPath(tempStream);
                                propertyBuffer.offer(new Property(lengthKey,String.valueOf(current.listCounter+1)));
                            }
                        }
                    }
                    

                    /**
                     * Return the last key of the entry
                     * @param _entry entry to convert to key
                     * @return last entry key converted to Key object 
                     */
                    private Key lastKey(final Entry _entry){
                        //purge ended keys
                        while((!keys.isEmpty())&&(keys.get(keys.size()-1).depth>_entry.tab )){
                            addPropertyLength();
                            keys.remove(keys.size()-1);
                        }
                        //recover last key
                        return (keys.isEmpty())? null : keys.get(keys.size()-1);
                    }
                   
                    /**
                     * Builds complete key path concatenating all key paths separated by dot
                     * @param _keys stream keys
                     * @return complete key path concatenating all key paths separated by dot
                     */
                    private String writeKeyPath(final Stream<Key> _keys){
                        return _keys.filter(Key::keyNonNull)
                                    .map(Key::toString)
                                    .collect(Collectors.joining("."));
                    }
                    
                    /** Read the next property */
                    public void read(){
                        
                        try {
                            Entry entry = null;
                            do{
                                entry = readLine()
                                            .orElse(null);
                                if(entry!=null){
                                    if(entry.value==null){
                                        final Key lastKey=lastKey(entry);
                                        if(lastKey==null){
                                            keys.add(new Key(entry.key,entry.tab,(entry.listItem)? 0 : -1));
                                        }else{
                                            if(lastKey.depth==entry.tab){
                                                if(entry.listItem){
                                                    lastKey.listCounter++;
                                                }else{
                                                    addPropertyLength();
                                                    keys.remove(keys.size()-1);
                                                    keys.add(new Key(entry.key,entry.tab,-1));
                                                }
                                            }else if(lastKey.depth<entry.tab){
                                                if(entry.listItem){
                                                    lastKey.listCounter++;
                                                }else{
                                                    keys.add(new Key(entry.key,entry.tab,(entry.listItem)? 0 : -1));
                                                }
                                            }
                                        }
                                    }else{
                                        if(entry.listItem){
                                            final Key lastKey=lastKey(entry);
                                            if(lastKey!=null){
                                                lastKey.listCounter++;
                                            }
                                        }
                                    }
                                }
                            }while((entry!=null)&&(entry.value==null));
                            if(entry!=null){
                                final String replyKey;
                                replyKey=writeKeyPath(Stream.concat(keys.stream(), Stream.of(new Key(entry.key,entry.tab, -1))));
                                this.propertyBuffer.offer(new Property(replyKey,entry.value));
                            }else{
                                while(!keys.isEmpty()){
                                    addPropertyLength();
                                    keys.remove(keys.size()-1);
                                }
                            }
                        } catch (ParseException e) {
                            throw new UncheckedIOException(new IOException(e));
                        }
                    }
                    
                    /** Return true if this splitterator has more elements */
                    @Override
                    public boolean hasNext() {
                        read();
                        return !this.propertyBuffer.isEmpty();
                    }

                    /** Return next element in spriterator */
                    @Override
                    public Property next() {
                        
                        if(this.propertyBuffer.isEmpty()){
                            read();
                        }
                        return this.propertyBuffer.poll();
                    }
                }, Spliterator.ORDERED | Spliterator.NONNULL | Spliterator.IMMUTABLE),false);
    }
}
