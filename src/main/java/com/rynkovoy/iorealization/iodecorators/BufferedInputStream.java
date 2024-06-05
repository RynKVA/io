package com.rynkovoy.iorealization.iodecorators;

import java.io.IOException;
import java.io.InputStream;

public class BufferedInputStream extends InputStream {
    private final static int DEFAULT_BUFFER_CAPACITY = 8192;
    private final InputStream target;
    private final byte[] buffer;
    private int position;
    private int count;

    public BufferedInputStream(InputStream target) {
        this.target = target;
        buffer = new byte[DEFAULT_BUFFER_CAPACITY];
    }

    public BufferedInputStream(InputStream target, int bufferSize) {
        this.target = target;
        buffer = new byte[bufferSize];
    }

    @Override
    public int read() throws IOException {
        if (position >= count) {
            count = target.read(buffer);
            position = 0;
        }
        if (count == -1) {
            return -1;
        }
        byte targetByte = buffer[position];
        position++;
        return targetByte & 0xFF;
    }

    @Override
    public int read(byte[] array) throws IOException {
        return read(array, 0, array.length);
    }

    @Override
    public int read(byte[] array, int offSet, int length) throws IOException {
        count = target.read(buffer);
        if (buffer.length < length && count > 0) {
            if (count <= length) {
                System.arraycopy(buffer, 0, array, offSet, count);
                read(array, offSet + count, length - count);
            }
            return length;
        }
        System.arraycopy(buffer, 0, array, offSet, length);
        return length;
    }

    @Override
    public void close() throws IOException {
        target.close();
    }
}
