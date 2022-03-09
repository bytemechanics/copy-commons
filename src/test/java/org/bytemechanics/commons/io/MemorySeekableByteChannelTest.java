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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.bytemechanics.commons.functional.LambdaUnchecker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 *
 * @author afarre
 */
@DisplayName("MemorySeekableByteChannel test case")
public class MemorySeekableByteChannelTest {

    @BeforeAll
    public static void setup() throws IOException {
        System.out.println(">>>>> MemorySeekableByteChannelTest >>>> setup");
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

    private static final byte[] DATA1 = "this is the content".getBytes(Charset.defaultCharset());
    private static final byte[] DATA2 = "this is the content".getBytes(Charset.defaultCharset());

    /**
     * Test of isOpen method, of class MemorySeekableByteChannel.
     */
    @Test
    public void testIsOpen() throws IOException {
        try ( InputStream inputData = new ByteArrayInputStream(DATA1)) {
            MemorySeekableByteChannel instance = new MemorySeekableByteChannel(inputData);
            Assertions.assertTrue(instance.isOpen());
            instance.close();
            Assertions.assertFalse(instance.isOpen());
        }
    }

    /**
     * Test of position method, of class MemorySeekableByteChannel.
     */
    @Test
    public void testPosition() throws Exception {
        try ( InputStream inputData = new ByteArrayInputStream(DATA1)) {
            MemorySeekableByteChannel instance = new MemorySeekableByteChannel(inputData);
            Assertions.assertEquals(0l, instance.position());
            instance.position(10l);
            Assertions.assertEquals(10l, instance.position());
            Assertions.assertThrows(IllegalArgumentException.class, () -> instance.position(-1));
            Assertions.assertEquals(10l, instance.position());
            instance.position(3l);
            Assertions.assertEquals(3l, instance.position());
            instance.close();
            Assertions.assertThrows(ClosedChannelException.class, () -> instance.position(2));
            Assertions.assertThrows(ClosedChannelException.class, () -> instance.position());
        }
    }

    /**
     * Test of read method, of class MemorySeekableByteChannel.
     */
    @Test
    public void testRead() throws Exception {

        ByteBuffer buffer;
        byte[] actualBuffer;

        try ( InputStream inputData = new ByteArrayInputStream(DATA1)) {
            MemorySeekableByteChannel instance = new MemorySeekableByteChannel(inputData);
            buffer = ByteBuffer.allocate(DATA1.length + 10);
            buffer.mark();
            Assertions.assertEquals(DATA1.length, instance.read(buffer));
            actualBuffer = new byte[DATA1.length];
            buffer.reset();
            buffer.get(actualBuffer);
            Assertions.assertArrayEquals(DATA1, actualBuffer);

            buffer = ByteBuffer.allocate(DATA1.length + 10);
            buffer.mark();
            instance.position(10);
            Assertions.assertEquals(DATA1.length-10, instance.read(buffer));
            actualBuffer = new byte[DATA1.length - 10];
            buffer.reset();
            buffer.get(actualBuffer);
            Assertions.assertArrayEquals(Arrays.copyOfRange(DATA1, 10, DATA1.length), actualBuffer);

            buffer = ByteBuffer.allocate(DATA1.length + 10);
            instance.position(100);
            Assertions.assertEquals(0, instance.read(buffer));

            instance.close();
            Assertions.assertThrows(ClosedChannelException.class, () -> instance.read(ByteBuffer.allocate(DATA1.length + 10)));
        }
    }

    /**
     * Test of write method, of class MemorySeekableByteChannel.
     */
    @Test
    public void testWrite() throws Exception {

        ByteBuffer buffer;
        byte[] actualBuffer;

        try ( InputStream inputData = new ByteArrayInputStream(DATA1)) {
            MemorySeekableByteChannel instance = new MemorySeekableByteChannel(inputData);
            buffer = ByteBuffer.wrap(DATA2);
            Assertions.assertEquals(DATA2.length, instance.write(buffer), "Should write the same length as the created buffer (DATA2)");
            buffer = ByteBuffer.allocate(DATA2.length);
            buffer.mark();
            Assertions.assertEquals(0, instance.read(buffer), "Should read 0 because the channel position is at the end of the channel");
            instance.position(0);
            buffer = ByteBuffer.allocate(DATA2.length);
            buffer.mark();
            Assertions.assertEquals(DATA2.length, instance.read(buffer), "Should read the same length that the written buffer (DATA2)");
            actualBuffer = new byte[DATA2.length];
            buffer.reset();
            buffer.get(actualBuffer);
            Assertions.assertArrayEquals(DATA2, actualBuffer, "The read content should be the same that the written buffer (DATA2)");
            Assertions.assertEquals(DATA2.length, instance.position(), "Should has positioned at the end of the channel (DATA2.length)");

            buffer = ByteBuffer.wrap(DATA1);
            Assertions.assertEquals(DATA1.length, instance.write(buffer), "Should write the same length as the created buffer (DATA1)");
            Assertions.assertEquals(DATA2.length + DATA1.length, instance.size(), "Should has the same length of the concatenated buffer written DATA2 and DATA1");
            Assertions.assertEquals(DATA2.length + DATA1.length, instance.position(), "Should has positioned at the end of the channel (DATA2.length+DATA1.length)");

            buffer = ByteBuffer.allocate(DATA2.length + DATA1.length);
            buffer.mark();
            instance.position(0);
            Assertions.assertEquals(DATA2.length + DATA1.length, instance.read(buffer), "Should read the same content that written since the position is at 0 (DATA2.length+DATA1.length)");
            actualBuffer = new byte[DATA2.length + DATA1.length];
            buffer.reset();
            buffer.get(actualBuffer);
            byte[] expectedBuffer = new byte[DATA2.length + DATA1.length];
            System.arraycopy(DATA2, 0, expectedBuffer, 0, DATA2.length);
            System.arraycopy(DATA1, 0, expectedBuffer, DATA2.length, DATA1.length);
            Assertions.assertArrayEquals(expectedBuffer, actualBuffer, "Should have the same content as DATA2 + DATA1");

            instance.close();
            Assertions.assertThrows(ClosedChannelException.class, () -> instance.write(ByteBuffer.wrap(DATA2)), "Should fail because the channel is already closed");
        }
    }

    /**
     * Test of truncate method, of class MemorySeekableByteChannel.
     */
    @Test
    public void testTruncate() throws Exception {
        
        ByteBuffer buffer;
        byte[] actualBuffer;

        try ( InputStream inputData = new ByteArrayInputStream(DATA1)) {
            MemorySeekableByteChannel instance = new MemorySeekableByteChannel(inputData);
            buffer = ByteBuffer.allocate(DATA1.length + 10);
            buffer.mark();
            Assertions.assertEquals(DATA1.length, instance.read(buffer), "Should have the same lenght than DATA1");
            actualBuffer = new byte[DATA1.length];
            buffer.reset();
            buffer.get(actualBuffer);
            Assertions.assertArrayEquals(DATA1, actualBuffer, "Should have the same content than DATA1");

            instance.position(20);
            Assertions.assertSame(instance, instance.truncate(15), "Should return the same channel instance");
            Assertions.assertEquals(15l, instance.position(), "should return position 15 as when we truncated to 15 the position was at 20");

            buffer = ByteBuffer.allocate(DATA1.length + 10);
            buffer.mark();
            Assertions.assertEquals(0, instance.read(buffer), "Should read only -1 elements because we truncated the channel to size 15 and the current position was on 20 therefore should be moved to 15");
            instance.position(0);
            buffer = ByteBuffer.allocate(DATA1.length + 10);
            buffer.mark();
            Assertions.assertEquals(15, instance.read(buffer), "Should read only 15 elements because we truncated the channel to size 15 and relocated position to 0");
            actualBuffer = new byte[15];
            buffer.reset();
            buffer.get(actualBuffer);
            Assertions.assertArrayEquals(Arrays.copyOfRange(DATA1, 0, 15), actualBuffer);

            Assertions.assertThrows(IllegalArgumentException.class, () -> instance.truncate(-1), "Should fail because position is negative");
            instance.close();
            Assertions.assertThrows(ClosedChannelException.class, () -> instance.truncate(2), "Should fail because the channel is already closed");
        }
    }

    /**
     * Test of size method, of class MemorySeekableByteChannel.
     */
    @Test
    public void testSize() throws Exception {
        try ( InputStream inputData = new ByteArrayInputStream(DATA1)) {
            MemorySeekableByteChannel instance = new MemorySeekableByteChannel(inputData);
            Assertions.assertEquals(DATA1.length, instance.size());
            instance.close();
            Assertions.assertThrows(ClosedChannelException.class, () -> instance.size());
        }
    }

    /**
     * Test of close method, of class MemorySeekableByteChannel.
     */
    @Test
    public void testClose() throws Exception {
        try ( InputStream inputData = new ByteArrayInputStream(DATA1)) {
            MemorySeekableByteChannel instance = new MemorySeekableByteChannel(inputData);
            Assertions.assertTrue(instance.isOpen());
            instance.close();
            Assertions.assertFalse(instance.isOpen());
        }
    }

}
