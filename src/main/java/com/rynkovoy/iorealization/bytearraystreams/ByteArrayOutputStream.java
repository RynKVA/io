package com.rynkovoy.iorealization.bytearraystreams;

import java.io.IOException;
import java.io.OutputStream;

public class ByteArrayOutputStream extends OutputStream {
    private byte[] buffer;
    private int count;
    private boolean isClosed;

    public ByteArrayOutputStream() {
        this(32);
    }

    public ByteArrayOutputStream(int bufferLength) {
        if (bufferLength < 0) {
            throw new IllegalArgumentException("BufferLength: " + bufferLength + " less than zero");
        }
        buffer = new byte[bufferLength];
    }

    @Override
    public void write(byte[] array) throws IOException {
        if (isClosed){
            throw new IOException("Stream is closed!");
        }
        write(array, 0, array.length);
    }

    @Override
    public void write(byte[] array, int offSet, int length) throws IOException {
        if (isClosed){
            throw new IOException("Stream is closed!");
        }
        validationParameters(array.length, offSet, length);
        grow(offSet + length);
        System.arraycopy(array, offSet, buffer, count, length);
        count += length;
    }

    @Override
    public void write(int b) throws IOException {
        if (isClosed){
            throw new IOException("Stream is closed!");
        }
        grow(count + 1);
        buffer[count] = (byte) b;
        count += 1;
    }

    public byte[] toByteArray() throws IOException {
        if (isClosed){
            throw new IOException("Stream is closed!");
        }
        byte[] targetArray = new byte[buffer.length];
        System.arraycopy(buffer, 0, targetArray, 0, buffer.length);
        return targetArray;
    }

    @Override
    public void close() {
        isClosed = true;
    }

    private void grow(int count) {
        int oldLength = buffer.length;
        if (count >= oldLength) {
            byte[] newLengthBuffer = new byte[count];
            System.arraycopy(buffer, 0, newLengthBuffer, 0, this.count);
            buffer = newLengthBuffer;
        }
    }

    private void validationParameters(int arrayLength, int offSet, int length) {
        if (offSet < 0) {
            throw new IndexOutOfBoundsException("OffSet: " + offSet + " less 0");
        } else if (length < 0) {
            throw new IndexOutOfBoundsException("Length: " + length + " less 0");
        }
    }
}
