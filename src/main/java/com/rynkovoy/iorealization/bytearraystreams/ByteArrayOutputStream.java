package com.rynkovoy.iorealization.bytearraystreams;

import java.io.OutputStream;

public class ByteArrayOutputStream extends OutputStream {
    private byte[] buffer;
    private int count;

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
    public void write(byte[] array) {
        write(array, 0, array.length);
    }

    @Override
    public void write(byte[] array, int offSet, int length) {
        validationParameters(array.length, offSet, length);
        grow(offSet + length);
        System.arraycopy(array, offSet, buffer, count, length);
        count += length;
    }

    @Override
    public void write(int b) {
        grow(count + 1);
        buffer[count] = (byte) b;
        count += 1;
    }

    public byte[] toByteArray() {
        byte[] targetArray = new byte[buffer.length];
        System.arraycopy(buffer, 0, targetArray, 0, buffer.length);
        return targetArray;
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
