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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.util.List;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 *
 * @author afarre
 */
public class YAMLPropertyWriterTest {
    
    @BeforeAll
    public static void setup() throws IOException {
        System.out.println(">>>>> YAMLPropertyWriterTest >>>> setup");
        try ( InputStream inputStream = YAMLPropertyWriterTest.class.getResourceAsStream("/logging.properties")) {
            LogManager.getLogManager().readConfiguration(inputStream);
        } catch (final IOException e) {
            Logger.getAnonymousLogger().severe("Could not load default logging.properties file");
            Logger.getAnonymousLogger().severe(e.getMessage());
        }
    }

    private static final Path TARGET_PATH=Paths.get("target/tests");
    
    @BeforeEach
    void beforeEachTest(final TestInfo testInfo) throws IOException {
        System.out.println(">>>>> " + this.getClass().getSimpleName() + " >>>> " + testInfo.getTestMethod().map(Method::getName).orElse("Unkown") + "" + testInfo.getTags().toString() + " >>>> " + testInfo.getDisplayName());
        Files.createDirectories(TARGET_PATH);
    }

    
    @Test
    public void newProperty() throws Exception {
        
        YAMLPropertyWriter.Property prop=new YAMLPropertyWriter.Property("my-first-val.my-second-list[2].my-third", "third-val");
        Assertions.assertAll( () ->Assertions.assertEquals("my-first-val.my-second-list[2].my-third", prop.key),
                              () ->Assertions.assertEquals("third-val", prop.value));
    } 
    
    static Stream<Arguments> appendDataPack() {
        return Stream.of(
                Arguments.of(0,"simple-key","simple-value",false,"simple-key: simple-value"),
                Arguments.of(0,"simple-key",null,false,"simple-key:"),
                Arguments.of(0,null,"simple-value",true,"- simple-value"),
                Arguments.of(0,"list-key","list-value",true,"- list-key: list-value"),
                Arguments.of(0,"list-key-commented","list-value #whatever",true,"- list-key-commented: list-value #whatever"),
                Arguments.of(0,"list-value-commented","#whatever",true,"- list-value-commented: #whatever"),
                Arguments.of(0,"list-value-commented"," #whatever ",true,"- list-value-commented: #whatever"),
                Arguments.of(2,"two-tab-key","two-tab-value",false,"    two-tab-key: two-tab-value"),
                Arguments.of(2,"   ","two-tab-value",true,"    - two-tab-value"),
                Arguments.of(3,"two-tab-list-key","two-tab-list-value",true,"      - two-tab-list-key: two-tab-list-value"),
                Arguments.of(3,"two-tab-list-key-commented","#whatever",true,"      - two-tab-list-key-commented: #whatever"),
                Arguments.of(3,"two-tab-list-value-commented","  #whatever  ",true,"      - two-tab-list-value-commented: #whatever"),
                Arguments.of(3,"two-tab-list-value-commented","#whatever",true,"      - two-tab-list-value-commented: #whatever"),
                Arguments.of(3,"two-tab-list-value-commented","   ",true,"      - two-tab-list-value-commented:"),
                Arguments.of(5,"four-tab-list-key-commented","#whatever",true,"          - four-tab-list-key-commented: #whatever"),
                Arguments.of(5,"four-tab-list-value","value",true,"          - four-tab-list-value: value"),
                Arguments.of(5,"four-tab-list-value-commented","#whatever",true,"          - four-tab-list-value-commented: #whatever"),
                Arguments.of(5,"four-tab-list-value-commented","#whatever   ",true,"          - four-tab-list-value-commented: #whatever")
        );
    }

    
    @ParameterizedTest(name = "When append _depth:{0},_name:{1},_value:{2},_isList:{3} should write {4}")
    @MethodSource("appendDataPack")
    public void append(final int _depth,final String _name,final String _value,final boolean _isList,final String _expected) throws IOException, ParseException {
        
        Path actualFilePath=TARGET_PATH.resolve("append-yaml.yml");

        try(Writer writer=Files.newBufferedWriter(actualFilePath,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING,StandardOpenOption.WRITE)){
            YAMLPropertyWriter instance = new YAMLPropertyWriter(writer);
            instance.append(_depth, _name, _value, _isList);
        }
        try(BufferedReader reader=Files.newBufferedReader(actualFilePath)){
            final String actual=reader.readLine();
            Assertions.assertEquals(_expected,actual);
        }
    }
    
    static Stream<Arguments> appendFailureDataPack() {
        return Stream.of(
                Arguments.of(0,null,null,false),
                Arguments.of(1,"  ",null,true),
                Arguments.of(3,null,"",true),
                Arguments.of(1,"  ","",false),
                Arguments.of(3,null,"g",false),
                Arguments.of(3,"","fds",false)
        );
    }

    @ParameterizedTest(name = "When append _depth:{0},_name:{1},_value:{2},_isList:{3} should fail")
    @MethodSource("appendFailureDataPack")
    public void appendFailure(final int _depth,final String _name,final String _value,final boolean _isList) throws Exception {
        
         Path actualFilePath=TARGET_PATH.resolve("append-failure-yaml.yml");

        try(Writer writer=Files.newBufferedWriter(actualFilePath,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING,StandardOpenOption.WRITE)){
            YAMLPropertyWriter instance = new YAMLPropertyWriter(writer);
            IOException e=Assertions.assertThrows(IOException.class,() -> instance.append(_depth, _name, _value, _isList));
        }catch(IOException e){
            Logger.getAnonymousLogger().info(e.getMessage());
            throw e;
        }
    }

  
    static Stream<Arguments> appendPropertyDataPack() {
        return Stream.of(
            Arguments.of("apiVersion", "v1","apiVersion: v1\n"),
            Arguments.of("kind", "Pod","kind: Pod\n"),
            Arguments.of("metadata.name", "rss-site","metadata:\n  name: rss-site\n"),
            Arguments.of("metadata.labels.app", "web","metadata:\n  labels:\n    app: web\n"),
            Arguments.of("spec.containers[0].name", "front-end","spec:\n  containers:\n    - name: front-end\n"),
            Arguments.of("spec.containers[0].image", "nginx","spec:\n  containers:\n    - image: nginx\n"),
            Arguments.of("spec.containers[0].ports[0].containerPort", "80","spec:\n  containers:\n    - ports:\n        - containerPort: 80\n"),
            Arguments.of("spec.containers[0].ports[*].length", "1","spec:\n  containers:\n    - ports:\n      portsLength: 1\n"),
            Arguments.of("spec.containers[1].name", "rss-reader","spec:\n  containers:\n    - name: rss-reader\n"),
            Arguments.of("spec.containers[1].image", "nickchase/rss-php-nginx:v1","spec:\n  containers:\n    - image: nickchase/rss-php-nginx:v1\n"),
            Arguments.of("spec.containers[1].ports[0].containerPort", "88","spec:\n  containers:\n    - ports:\n        - containerPort: 88\n"),
            Arguments.of("spec.containers[1].ports[*].length", "1","spec:\n  containers:\n    - ports:\n      portsLength: 1\n"),
            Arguments.of("spec.containers[1].comments[0]", "not-important","spec:\n  containers:\n    - comments:\n        - not-important\n"),
            Arguments.of("spec.containers[1].comments[1]", "very important","spec:\n  containers:\n    - comments:\n        - very important\n"),
            Arguments.of("spec.containers[1].comments[*].length", "2","spec:\n  containers:\n    - comments:\n      commentsLength: 2\n"),
            Arguments.of("spec.containers[*].length", "2","spec:\n  containers:\n  containersLength: 2\n")
        );
    }

    @ParameterizedTest(name = "When append _property: [key: {0}, value: {1}] should write: {2}")
    @MethodSource("appendPropertyDataPack")
    public void appendProperty(final String _key,final String _value, final String _expected) throws IOException {
        
        Path actualFilePath=TARGET_PATH.resolve("append-property.yml");
        YAMLPropertyWriter.Property _property=new YAMLPropertyWriter.Property(_key,_value);
        
        try(Writer writer=Files.newBufferedWriter(actualFilePath,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING,StandardOpenOption.WRITE)){
            YAMLPropertyWriter instance = new YAMLPropertyWriter(writer);
            instance.append(_property);
        }
        Assertions.assertTrue(Files.exists(actualFilePath));
        String actual=new String(Files.readAllBytes(actualFilePath),Charset.defaultCharset());
        Assertions.assertNotNull(actual);
        if(_key.endsWith("[*].length")){
            Assertions.assertEquals("", actual);
        }else{
            Assertions.assertEquals(_expected, actual);
        }
    }
    
    @ParameterizedTest(name = "When append _property: [key: {0}, value: {1}] should write: {2}")
    @MethodSource("appendPropertyDataPack")
    public void appendProperty_withLength(final String _key,final String _value, final String _expected) throws IOException {
        
        Path actualFilePath=TARGET_PATH.resolve("append-property.yml");
        YAMLPropertyWriter.Property _property=new YAMLPropertyWriter.Property(_key,_value);
        
        try(Writer writer=Files.newBufferedWriter(actualFilePath,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING,StandardOpenOption.WRITE)){
            YAMLPropertyWriter instance = new YAMLPropertyWriter(writer,false);
            instance.append(_property);
        }
        Assertions.assertTrue(Files.exists(actualFilePath));
        String actual=new String(Files.readAllBytes(actualFilePath),Charset.defaultCharset());
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(_expected, actual);
    }
    
    @Test
    public void write() throws IOException {
        
        YAMLPropertyWriter.Property[] values=new YAMLPropertyWriter.Property[] {
            new YAMLPropertyWriter.Property("apiVersion", "v1"),
            new YAMLPropertyWriter.Property("kind", "Pod"),
            new YAMLPropertyWriter.Property("metadata.name", "rss-site"),
            new YAMLPropertyWriter.Property("metadata.labels.app", "web"),
            new YAMLPropertyWriter.Property("spec.containers[0].name", "front-end"),
            new YAMLPropertyWriter.Property("spec.containers[0].image", "nginx"),
            new YAMLPropertyWriter.Property("spec.containers[0].ports[0].containerPort", "80"),
            new YAMLPropertyWriter.Property("spec.containers[0].ports[1].containerPort", "90"),
            new YAMLPropertyWriter.Property("spec.containers[0].ports[*].length", "2"),
            new YAMLPropertyWriter.Property("spec.containers[1].name", "rss-reader"),
            new YAMLPropertyWriter.Property("spec.containers[1].image", "nickchase/rss-php-nginx:v1"),
            new YAMLPropertyWriter.Property("spec.containers[1].ports[0].containerPort", "88"),
            new YAMLPropertyWriter.Property("spec.containers[1].ports[*].length", "1"),
            new YAMLPropertyWriter.Property("spec.containers[1].comments[0]", "not-important"),
            new YAMLPropertyWriter.Property("spec.containers[1].comments[1]", "very important"),
            new YAMLPropertyWriter.Property("spec.containers[1].comments[*].length", "2"),
            new YAMLPropertyWriter.Property("spec.containers[*].length", "2"),
        };
        Path expectedFilePath=Paths.get("src/test/resources/write-yaml-complete.yml");
        Path actualFilePath=TARGET_PATH.resolve("write-yaml-complete.yml");
        
        try(Writer writer=Files.newBufferedWriter(actualFilePath,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING,StandardOpenOption.WRITE)){
            YAMLPropertyWriter instance = new YAMLPropertyWriter(writer);
            instance.write(Stream.of(values));
        }
        Assertions.assertTrue(Files.exists(actualFilePath));
        List<String> actualLines=Files.readAllLines(actualFilePath);
        Assertions.assertNotNull(actualLines);
        List<String> expectedLines=Files.readAllLines(expectedFilePath);
        Assertions.assertEquals(expectedLines.size(), actualLines.size());
        Assertions.assertEquals(expectedLines, actualLines);
    }

    @Test
    public void write_considerLength() throws IOException {
        
        YAMLPropertyWriter.Property[] values=new YAMLPropertyWriter.Property[] {
            new YAMLPropertyWriter.Property("apiVersion", "v1"),
            new YAMLPropertyWriter.Property("kind", "Pod"),
            new YAMLPropertyWriter.Property("metadata.name", "rss-site"),
            new YAMLPropertyWriter.Property("metadata.labels.app", "web"),
            new YAMLPropertyWriter.Property("spec.containers[0].name", "front-end"),
            new YAMLPropertyWriter.Property("spec.containers[0].image", "nginx"),
            new YAMLPropertyWriter.Property("spec.containers[0].ports[0].containerPort", "80"),
            new YAMLPropertyWriter.Property("spec.containers[0].ports[1].containerPort", "90"),
            new YAMLPropertyWriter.Property("spec.containers[0].ports[*].length", "2"),
            new YAMLPropertyWriter.Property("spec.containers[1].name", "rss-reader"),
            new YAMLPropertyWriter.Property("spec.containers[1].image", "nickchase/rss-php-nginx:v1"),
            new YAMLPropertyWriter.Property("spec.containers[1].ports[0].containerPort", "88"),
            new YAMLPropertyWriter.Property("spec.containers[1].ports[*].length", "1"),
            new YAMLPropertyWriter.Property("spec.containers[1].comments[0]", "not-important"),
            new YAMLPropertyWriter.Property("spec.containers[1].comments[1]", "very important"),
            new YAMLPropertyWriter.Property("spec.containers[1].comments[*].length", "2"),
            new YAMLPropertyWriter.Property("spec.containers[*].length", "2"),
        };
        Path expectedFilePath=Paths.get("src/test/resources/write-yaml-complete-length.yml");
        Path actualFilePath=TARGET_PATH.resolve("write-yaml-complete-length.yml");
        
        try(Writer writer=Files.newBufferedWriter(actualFilePath,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING,StandardOpenOption.WRITE)){
            YAMLPropertyWriter instance = new YAMLPropertyWriter(writer,false);
            instance.write(Stream.of(values));
        }
        Assertions.assertTrue(Files.exists(actualFilePath));
        List<String> actualLines=Files.readAllLines(actualFilePath);
        Assertions.assertNotNull(actualLines);
        List<String> expectedLines=Files.readAllLines(expectedFilePath);
        Assertions.assertEquals(expectedLines.size(), actualLines.size());
        Assertions.assertEquals(expectedLines, actualLines);
    }
}
