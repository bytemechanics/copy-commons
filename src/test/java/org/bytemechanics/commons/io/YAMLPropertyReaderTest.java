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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.bytemechanics.commons.functional.LambdaUnchecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 *
 * @author afarre
 */
public class YAMLPropertyReaderTest {
    
    @BeforeAll
    public static void setup() throws IOException {
        System.out.println(">>>>> YAMLPropertyReaderTest >>>> setup");
        try ( InputStream inputStream = LambdaUnchecker.class.getResourceAsStream("/logging.properties")) {
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (final IOException e) {
            Logger.getAnonymousLogger().severe("Could not load default logging.properties file");
            Logger.getAnonymousLogger().severe(e.getMessage());
        }
    }

    @BeforeEach
    void beforeEachTest(final TestInfo testInfo) {
        System.out.println(">>>>> " + this.getClass().getSimpleName() + " >>>> " + testInfo.getTestMethod().map(Method::getName).orElse("Unkown") + "" + testInfo.getTags().toString() + " >>>> " + testInfo.getDisplayName());
    }

    
    static Stream<Arguments> yamlLineDataPack() {
        return Stream.of(
                Arguments.of("simple-key:", true, 0,false,"simple-key",null),
                Arguments.of("- list-key:", true, 0,true,"list-key",null),
                Arguments.of("- list-key-commented: #whatever", true, 0,true,"list-key-commented",null),
                Arguments.of("- list-value", true, 0,true,null,"list-value"),
                Arguments.of("- list-value-commented #whatever", true, 0,true,null,"list-value-commented"),
                Arguments.of("- list-value-commented#whatever", true, 0,true,null,"list-value-commented#whatever"),
                Arguments.of("  one-tab-key:", true, 1,false,"one-tab-key",null),
                Arguments.of("    two-tab-key:", true, 2,false,"two-tab-key",null),
                Arguments.of("      - two-tab-list-key:", true, 3,true,"two-tab-list-key",null),
                Arguments.of("      - two-tab-list-key-commented: #whatever", true, 3,true,"two-tab-list-key-commented",null),
                Arguments.of("      - two-tab-list-value", true, 3,true,null,"two-tab-list-value"),
                Arguments.of("      - two-tab-list-value-commented #whatever", true, 3,true,null,"two-tab-list-value-commented"),
                Arguments.of("      - two-tab-list-value-commented#whatever", true, 3,true,null,"two-tab-list-value-commented#whatever"),
                Arguments.of("      three-tab-key:", true, 3,false,"three-tab-key",null),
                Arguments.of("        four-tab-key:",  true,4,false,"four-tab-key",null),
                Arguments.of("          - four-tab-list-key:", true, 5,true,"four-tab-list-key",null),
                Arguments.of("          - four-tab-list-key-commented: #whatever",  true,5,true,"four-tab-list-key-commented",null),
                Arguments.of("          - four-tab-list-value", true, 5,true,null,"four-tab-list-value"),
                Arguments.of("          - four-tab-list-value-commented #whatever", true, 5,true,null,"four-tab-list-value-commented"),
                Arguments.of("          - four-tab-list-value-commented#whatever", true, 5,true,null,"four-tab-list-value-commented#whatever")
        );
    }

    @ParameterizedTest(name = "When {0} parsed should return an Optional(empty:{1}) with an Entry(tabs:{2},listItem:{3},key:{4},value:{5})")
    @MethodSource("yamlLineDataPack")
    public void newEntry(final String _line,final boolean _optionalEmpty,final int _tabs,final boolean _listItem, final String _key, final String _value) throws Exception {
        
        try(StringReader reader=new StringReader("")){
            YAMLPropertyReader instance = new YAMLPropertyReader(reader);
            YAMLPropertyReader.Entry result = instance.new Entry(_line);
            Assertions.assertAll(() -> Assertions.assertNotNull(result,"Failed on "+_line),
                    () -> Assertions.assertEquals(_tabs,result.getTab(),"Failed on "+_line),
                    () -> Assertions.assertEquals(_listItem,result.isListItem(),"Failed on "+_line),
                    () -> Assertions.assertEquals(_key,result.getKey(),"Failed on "+_line),
                    () -> Assertions.assertEquals(_value,result.getValue(),"Failed on "+_line));
        }catch(Exception e){
            Logger.getAnonymousLogger().log(Level.SEVERE, "Failure on line "+_line, e);
            throw e;
        }
    }

    @ParameterizedTest(name = "When {0} parsed should launch UncheckedIOException")
    @ValueSource(strings = {" whatever:a","   whatever:a","-whatever","  -whatever:a"," - :a","-   "})
    public void newEntryFailure(final String _line) throws Exception {
        
        try(StringReader reader=new StringReader("")){
            YAMLPropertyReader instance = new YAMLPropertyReader(reader);
            UncheckedIOException e=Assertions.assertThrows(UncheckedIOException.class,() -> instance.new Entry(_line));
        }catch(Exception e){
            Logger.getAnonymousLogger().info(e.getMessage());
            throw e;
        }
    }

    @Test
    public void readLine() throws IOException, ParseException {
        
        try(Reader reader=Files.newBufferedReader(Paths.get("src/test/resources/parse-yaml-ends.yml"))){
            YAMLPropertyReader instance = new YAMLPropertyReader(reader);
            Optional<YAMLPropertyReader.Entry> firstResult = instance.readLine();
            Assertions.assertAll(() -> Assertions.assertTrue(firstResult.isPresent()),
                    () -> Assertions.assertEquals((Integer)0,firstResult.map(YAMLPropertyReader.Entry::getTab).orElse(0)),
                    () -> Assertions.assertEquals((Boolean)false,firstResult.map(YAMLPropertyReader.Entry::isListItem).orElse(false)),
                    () -> Assertions.assertEquals("apiVersion",firstResult.map(YAMLPropertyReader.Entry::getKey).orElse(null)),
                    () -> Assertions.assertEquals("v1",firstResult.map(YAMLPropertyReader.Entry::getValue).orElse(null)));
            Assertions.assertFalse(instance.readLine().isPresent());
        }
    }

    @Test
    public void stream() throws IOException, ParseException {
        
        YAMLPropertyReader.Property[] values=new YAMLPropertyReader.Property[] {
            new YAMLPropertyReader.Property("apiVersion", "v1"),
            new YAMLPropertyReader.Property("kind", "Pod"),
            new YAMLPropertyReader.Property("metadata.name", "rss-site"),
            new YAMLPropertyReader.Property("metadata.labels.app", "web"),
            new YAMLPropertyReader.Property("spec.containers[0].name", "front-end"),
            new YAMLPropertyReader.Property("spec.containers[0].image", "nginx"),
            new YAMLPropertyReader.Property("spec.containers[0].ports[0].containerPort", "80"),
            new YAMLPropertyReader.Property("spec.containers[0].ports[*].length", "1"),
            new YAMLPropertyReader.Property("spec.containers[1].name", "rss-reader"),
            new YAMLPropertyReader.Property("spec.containers[1].image", "nickchase/rss-php-nginx:v1"),
            new YAMLPropertyReader.Property("spec.containers[1].ports[0].containerPort", "88"),
            new YAMLPropertyReader.Property("spec.containers[1].ports[*].length", "1"),
            new YAMLPropertyReader.Property("spec.containers[1].comments[0]", "not-important"),
            new YAMLPropertyReader.Property("spec.containers[1].comments[1]", "very important"),
            new YAMLPropertyReader.Property("spec.containers[1].comments[*].length", "2"),
            new YAMLPropertyReader.Property("spec.containers[*].length", "2"),
        };
        AtomicInteger cursor=new AtomicInteger(-1);
        
        try(Reader reader=Files.newBufferedReader(Paths.get("src/test/resources/parse-yaml-complete.yml"))){
            YAMLPropertyReader instance = new YAMLPropertyReader(reader);
            Stream<YAMLPropertyReader.Property> result = instance.stream();
            Assertions.assertNotNull(result);
            Assertions.assertFalse(result.isParallel());
            result
                .peek(property -> System.out.println("Property found: ["+property.getKey()+","+property.getValue()+"]"))
                .peek(property -> cursor.incrementAndGet())
                .forEach(property -> Assertions.assertAll(() -> Assertions.assertEquals(values[cursor.get()].getKey(), property.getKey()),
                                                            () -> Assertions.assertEquals(values[cursor.get()].getValue(), property.getValue())));
            Assertions.assertEquals(values.length,cursor.incrementAndGet());
        }
    }

    @Test
    public void streamWithoutLength() throws IOException, ParseException {
        
        YAMLPropertyReader.Property[] values=new YAMLPropertyReader.Property[] {
            new YAMLPropertyReader.Property("apiVersion", "v1"),
            new YAMLPropertyReader.Property("kind", "Pod"),
            new YAMLPropertyReader.Property("metadata.name", "rss-site"),
            new YAMLPropertyReader.Property("metadata.labels.app", "web"),
            new YAMLPropertyReader.Property("spec.containers[0].name", "front-end"),
            new YAMLPropertyReader.Property("spec.containers[0].image", "nginx"),
            new YAMLPropertyReader.Property("spec.containers[0].ports[0].containerPort", "80"),
            new YAMLPropertyReader.Property("spec.containers[1].name", "rss-reader"),
            new YAMLPropertyReader.Property("spec.containers[1].image", "nickchase/rss-php-nginx:v1"),
            new YAMLPropertyReader.Property("spec.containers[1].ports[0].containerPort", "88"),
            new YAMLPropertyReader.Property("spec.containers[1].comments[0]", "not-important"),
            new YAMLPropertyReader.Property("spec.containers[1].comments[1]", "very important"),
        };
        AtomicInteger cursor=new AtomicInteger(-1);
        
        try(Reader reader=Files.newBufferedReader(Paths.get("src/test/resources/parse-yaml-complete.yml"))){
            YAMLPropertyReader instance = new YAMLPropertyReader(reader,false);
            Stream<YAMLPropertyReader.Property> result = instance.stream();
            Assertions.assertNotNull(result);
            Assertions.assertFalse(result.isParallel());
            result
                .peek(property -> System.out.println("Property found: ["+property.getKey()+","+property.getValue()+"]"))
                .peek(property -> cursor.incrementAndGet())
                .forEach(property -> Assertions.assertAll(() -> Assertions.assertEquals(values[cursor.get()].getKey(), property.getKey()),
                                                            () -> Assertions.assertEquals(values[cursor.get()].getValue(), property.getValue())));
            Assertions.assertEquals(values.length,cursor.incrementAndGet());
        }
    }
}
