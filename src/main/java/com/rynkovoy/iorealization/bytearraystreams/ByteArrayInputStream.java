package com.rynkovoy.iorealization.bytearraystreams;

import java.io.IOException;
import java.io.InputStream;

public class ByteArrayInputStream extends InputStream {
    private final byte[] buffer;
    private final int current;
    private int position;
    private boolean isClosed;

    public ByteArrayInputStream(byte[] buffer) {
        this.buffer = buffer;
        position = 0;
        current = buffer.length;
    }

    public ByteArrayInputStream(byte[] buffer, int offSet, int length) {
        this.buffer = buffer;
        this.position = offSet;
        this.current = Math.min((offSet + length), buffer.length);
    }

    @Override
    public int read() throws IOException {
        if (isClosed){
            throw new IOException("Stream is closed!");
        }
        return (position < current) ? (buffer[position++] & 0xFF) : -1;
    }

    @Override
    public int read(byte[] array) throws IOException {
        if (isClosed){
            throw new IOException("Stream is closed!");
        }
        return read(array, 0, array.length);
    }

    @Override
    public int read(byte[] array, int offSet, int length) throws IOException {
        if (isClosed){
            throw new IOException("Stream is closed!");
        }
        validationParameters(array.length, offSet, length);
        int difference = current - position;
        if (position > current) {
            return -1;
        }
        if (length > difference) {
            length = difference;
        }
        if (length == 0) {
            return 0;
        }
        System.arraycopy(buffer, position, array, offSet, length);
        position += length;
        return length;
    }

    @Override
    public void close() {
        isClosed = true;
    }

    private void validationParameters(int arrayLength, int offSet, int length) {
        if (offSet < 0) {
            throw new IndexOutOfBoundsException("OffSet: " + offSet + " less 0");
        } else if (length < 0) {
            throw new IndexOutOfBoundsException("Length: " + length + " less 0");
        } else if ((offSet + length) > arrayLength) {
            throw new IndexOutOfBoundsException("Sum of offSet and length: " + (offSet + length) + " more than arrayLength: " + arrayLength);
        }
    }
}
