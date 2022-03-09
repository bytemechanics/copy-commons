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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Creates a seekable byte channel stored in memory
 * @author afarre
 * @since 1.8.0
 */
public class MemorySeekableByteChannel implements SeekableByteChannel {

    private static final Logger logger = Logger.getLogger(MemorySeekableByteChannel.class.getName());
    private static final int MEMORY_CHUNK_SIZE = 2048;

    /**
     * List of data buffers slots
     */
    private final List<byte[]> chunks;
    /**
     * Current data buffer slot
     */
    private int currentChunk;
    /**
     * Current position inside current data buffer slot
     */
    private int currentChunkPosition;
    /**
     * Current total size
     */
    private long currentSize;
    /**
     * Absolute position
     */
    private long position;
    /**
     * flag to indicate channel closed
     */
    private boolean closed;
    /**
     * Write lock for multithreading
     */
    private final Object writeLock = new Object();
    /**
     * Read lock for multithreading
     */
    private final Object readLock = new Object();

    public MemorySeekableByteChannel() {
        this.chunks = new ArrayList<>();
        this.currentChunk = 0;
        this.currentChunkPosition = 0;
        this.closed = false;
        this.currentSize = 0l;
        this.position = 0l;
    }

    public MemorySeekableByteChannel(final InputStream _inputStream) {
        this();
        try ( InputStream input = new BufferedInputStream(_inputStream)) {
            logger.finest("init::read-stream::begin");
            int read;
            final byte[] buffer = new byte[MEMORY_CHUNK_SIZE];
            read = input.read(buffer);
            while (read > -1) {
                logger.log(Level.FINEST, "init::read-stream::read::{0}::bytes", read);
                this.chunks.add(Arrays.copyOf(buffer, read));
                this.currentChunk=this.chunks.size()-1;
                this.currentSize += read;
                read = input.read(buffer);
            }
            logger.log(Level.FINEST, "init::read-stream::end::{0}", this.currentSize);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public boolean isOpen() {
        return !this.closed;
    }

    @Override
    public long position() throws IOException {
        if (this.closed) {
            throw new ClosedChannelException();
        }
        return this.position;
    }

    @Override
    public SeekableByteChannel position(final long _newPosition) throws IOException {

        logger.log(Level.FINEST, "position::{0}::begin", _newPosition);
        if (this.closed) {
            throw new ClosedChannelException();
        }
        if (_newPosition < 0l) {
            throw new IllegalArgumentException("Position can not be negative [" + _newPosition + "]");
        }
        if (_newPosition < this.currentSize) {
            if (_newPosition < this.position) {
                logger.log(Level.FINEST, "position::new-position::{0}::is-lower-than::{1}::restart-from-beginning", new Object[]{_newPosition, this.currentSize});
                this.currentChunk = 0;
                this.currentChunkPosition = 0;
                this.position=0l;
            }
            logger.log(Level.FINEST, "position::traverse::from::{0}::to::{1}",new Object[]{this.position, _newPosition});
            while (this.position < _newPosition) {
                final int chunkSize = this.chunks.get(this.currentChunk).length;
                if (this.position + chunkSize < _newPosition) {
                    logger.log(Level.FINEST, "position::{0}/{1}::chunk::{2}::with::{3}::not-enought::jump",new Object[]{this.position,_newPosition, this.currentChunk,chunkSize});
                    this.position += chunkSize;
                    this.currentChunk++;
                } else {
                    logger.log(Level.FINEST, "position::{0}/{1}::chunk::{2}::with::{3}::enought::positionate",new Object[]{this.position,_newPosition, this.currentChunk,chunkSize});
                    this.currentChunkPosition = (int) (_newPosition - this.position);
                    this.position = _newPosition;
                }
            }
        } else {
            logger.log(Level.FINEST, "position::new-position::{0}::is-greater-than::{1}::go-to-position", new Object[]{_newPosition, this.currentSize});
            this.position = _newPosition;
        }
        logger.log(Level.FINEST, "position::{0}::end (currentChunk:{1},currentChunkPosition:{2})", new Object[]{_newPosition, this.currentChunk, this.currentChunkPosition});

        return this;
    }

    @Override
    public int read(ByteBuffer _destiny) throws IOException {

        final int maxBytes = _destiny.remaining();
        int reply = 0;
        final long lastKnownSize = this.currentSize;
        long newPosition = this.position;
        int newCurrentChunk = this.currentChunk;
        int newCurrentChunkPosition = this.currentChunkPosition;

        logger.log(Level.FINEST, "read::{0}::at::{1}/{2}({3}/{4}:{5})::begin", new Object[]{maxBytes,this.position,lastKnownSize,this.currentChunk+1, this.chunks.size(),this.currentChunkPosition});
        if (this.closed) {
            throw new ClosedChannelException();
        }
        if (newPosition < this.currentSize) {
            synchronized (readLock) {
                while ((reply < maxBytes) && (newPosition < lastKnownSize)) {
                    if (newCurrentChunk < this.chunks.size()) {
                        final byte[] chunk = this.chunks.get(newCurrentChunk);
                        final int chunkReadableLength = chunk.length - newCurrentChunkPosition;
                        final int pendingToWrite = maxBytes - reply;
                        if (pendingToWrite > chunkReadableLength) {
                            _destiny.put(chunk, newCurrentChunkPosition, chunkReadableLength);
                            newCurrentChunk++;
                            newCurrentChunkPosition = 0;
                            newPosition += chunkReadableLength;
                            reply += chunkReadableLength;
                        } else {
                            _destiny.put(chunk, newCurrentChunkPosition, pendingToWrite);
                            newCurrentChunkPosition = newCurrentChunkPosition + pendingToWrite;
                            newPosition += pendingToWrite;
                            reply += pendingToWrite;
                        }
                    }
                }
                this.position = newPosition;
                this.currentChunk = (newCurrentChunk < this.chunks.size()) ? newCurrentChunk : newCurrentChunk - 1;
                this.currentChunkPosition = newCurrentChunkPosition;
            }
        } else {
            reply = 0;
        }
        logger.log(Level.FINEST, "read::{0}::at::{1}/{2}({3}/{4}:{5}):::end::{6}", new Object[]{maxBytes,this.position,lastKnownSize,this.currentChunk+1, this.chunks.size(),this.currentChunkPosition,reply});

        return reply;
    }

    protected static final int copy(ByteBuffer _source, byte[] _destiny, final int _from, final int _to) {

        final int reply = _to - _from;

        final byte[] sourceBytes = new byte[reply];
        _source.get(sourceBytes);
        System.arraycopy(sourceBytes, 0, _destiny, _from, _to);

        return reply;
    }

    @Override
    public int write(final ByteBuffer _source) throws IOException {

        final int maxBytes = _source.remaining();
        int reply = 0;
        long newPosition = this.position;
        long newCurrentSize = this.currentSize;
        int newCurrentChunk = this.currentChunk;
        int newCurrentChunkPosition = this.currentChunkPosition;

        logger.finest("write::begin");
        if (this.closed) {
            throw new ClosedChannelException();
        }
        synchronized (writeLock) {
            if (newPosition >= newCurrentSize) {
                logger.fine("write::starting-position::after-end::elonging-channel");
                while (newPosition >= newCurrentSize) {
                    final long pendingSize = maxBytes-(newCurrentSize-this.currentSize);
                    final int addedSize=(pendingSize < MEMORY_CHUNK_SIZE)? (int) pendingSize : MEMORY_CHUNK_SIZE;
                    this.chunks.add(new byte[addedSize]);
                    newCurrentSize += addedSize;
                }
            }
            while (reply < maxBytes) {
                final long pendingSize = maxBytes - reply;
                final byte[] chunk = this.chunks.get(newCurrentChunk);
                final int chunkWritableLength = chunk.length - newCurrentChunkPosition;
                if (pendingSize < chunkWritableLength) {
                    logger.log(Level.FINEST, "write::ending::{0} (pendingSize:{1},chunkWritableLength:{2})", new Object[]{reply, pendingSize, chunkWritableLength});
                    final int from = newCurrentChunkPosition;
                    final int to = (int) (newCurrentChunkPosition + pendingSize);
                    final int writeLength = copy(_source, chunk, from, to);
                    reply += writeLength;
                    newPosition += writeLength;
                    newCurrentChunkPosition = to;
                } else {
                    if (newCurrentChunkPosition < chunk.length) {
                        logger.log(Level.FINEST, "write::copying::{0} (amount:{1},chunk:{2},from:{3})", new Object[]{reply, chunk.length, newCurrentChunk, newCurrentChunkPosition});
                        final int from = newCurrentChunkPosition;
                        final int to = chunk.length;
                        final int writeLength = copy(_source, chunk, from, to);
                        reply += writeLength;
                        newPosition += writeLength;
                        newCurrentChunkPosition = to;
                    } else {
                        if (newCurrentChunk + 1 == this.chunks.size()) {
                            logger.log(Level.FINEST, "write::elonging::{0} (amount:{1},at:{2})", new Object[]{reply, MEMORY_CHUNK_SIZE, newCurrentChunk});
                            this.chunks.add(new byte[MEMORY_CHUNK_SIZE]);
                            newCurrentChunk++;
                            newCurrentChunkPosition = 0;
                            newCurrentSize += MEMORY_CHUNK_SIZE;
                        } else {
                            newCurrentChunk++;
                            newCurrentChunkPosition = 0;
                        }
                    }
                }
            }
            this.position = newPosition;
            this.currentSize = newCurrentSize;
            this.currentChunk = newCurrentChunk;
            this.currentChunkPosition = newCurrentChunkPosition;
            logger.log(Level.FINEST, "write::end::{0} (position:{1},currentChunk:{2},currentChunkPosition:{3},size:{4})", new Object[]{reply, this.position, this.currentChunk, this.currentChunkPosition, this.currentSize});
        }

        return reply;
    }

    @Override
    public SeekableByteChannel truncate(long _size) throws IOException {

        logger.log(Level.FINEST, "truncate::{0}::begin", _size);
        if (this.closed) {
            throw new ClosedChannelException();
        }
        if (_size < 0l) {
            throw new IllegalArgumentException("Position can not be negative [" + _size + "]");
        }
        if (_size < this.currentSize) {
            synchronized (writeLock) {
                if (this.position > _size) {
                    position(_size);
                }
                for (int ic1 = this.chunks.size() - 1; ic1 > this.currentChunk; ic1--) {
                    this.chunks.remove(ic1);
                }
                final byte[] lastChunk = Arrays.copyOf(this.chunks.get(this.currentChunk), this.currentChunkPosition);
                this.chunks.set(this.currentChunk, lastChunk);
                this.currentSize = _size;
            }
        }
        logger.log(Level.FINEST, "truncate::{0}::end (position:{1},currentChunk:{2},currentChunkPosition:{3})", new Object[]{_size, this.position, this.currentChunk, this.currentChunkPosition});

        return this;
    }

    @Override
    public long size() throws IOException {
        if (this.closed) {
            throw new ClosedChannelException();
        }
        return this.currentSize;
    }

    @Override
    public void close() throws IOException {
        this.chunks.clear();
        this.closed = true;
    }
}
