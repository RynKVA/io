package com.rynkovoy.iorealization.iodecorators;

import java.io.IOException;
import java.io.OutputStream;

public class BufferedOutputStream extends OutputStream {
    private static final int DEFAULT_BUFFER_CAPACITY = 8192;
    private final OutputStream target;
    private final byte[] buffer;
    private int position;

    public BufferedOutputStream(OutputStream target) {
        this(target, DEFAULT_BUFFER_CAPACITY);
    }

    public BufferedOutputStream(OutputStream target, int capacity) {
        this.target = target;
        buffer = new byte[capacity];
    }

    @Override
    public void write(int targetByte) throws IOException {
        buffer[position] = (byte) targetByte;
        position++;
        if (position == buffer.length) {
            flush();
        }
    }

    @Override
    public void write(byte[] array) throws IOException {
        write(array, 0, array.length);
    }

    @Override
    public void write(byte[] array, int offSet, int length) throws IOException {
        int freeSpace = buffer.length - position;
        if (freeSpace < length) {
            flush();
            if (length >= buffer.length) {
                target.write(array, offSet, length);
                return;
            }
        }
        System.arraycopy(array, offSet, buffer, position, length);
        position += length;
    }

    @Override
    public void flush() throws IOException {
        target.write(buffer, 0, position);
        position = 0;
    }

    @Override
    public void close() throws IOException {
        try (target) {
            flush();
        }
    }
}
