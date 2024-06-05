package com.rynkovoy.iorealization.bytearraystreams;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

public class IOByteArrayStreamsITest {
    private byte[] arrayWithElements;
    private byte[] emptyArray;

    @BeforeEach
    void before() {
        arrayWithElements = new byte[]{1, 2, 3, 4, 5};
        emptyArray = new byte[5];
    }

    @Test
    @DisplayName("When use method write and toByteArray, then write 1 byte in buffer, toByteArray return new byte array with all bytes which copied from buffer ")
    void testWriteAndToByteArray() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(5)) {
            out.write(1);
            out.write(2);
            out.write(3);
            byte[] byteArray = out.toByteArray();

            assertEquals(1, byteArray[0]);
            assertEquals(2, byteArray[1]);
            assertEquals(3, byteArray[2]);
        }
    }

    @Test
    @DisplayName("When use method write with parameter byte array, then method copied all bytes from array to buffer")
    void testWriteByArray() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(5)) {
            out.write(arrayWithElements);
            byte[] byteArray = out.toByteArray();

            assertEquals(1, byteArray[0]);
            assertEquals(2, byteArray[1]);
            assertEquals(3, byteArray[2]);
            assertEquals(4, byteArray[3]);
            assertEquals(5, byteArray[4]);
        }
    }

    @Test
    @DisplayName("When use method write with parameters array, offSet and length, then write in buffer bytes from array offSet position on lengths value bytes")
    void testWriteByArrayWithParametersOffSetAndLength() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(5)) {
            out.write(arrayWithElements, 1, 3);
            byte[] byteArray = out.toByteArray();

            assertEquals(2, byteArray[0]);
            assertEquals(3, byteArray[1]);
            assertEquals(4, byteArray[2]);
        }
    }

    @Test
    @DisplayName("When in method write parameter offSet is negative then expect IndexOutOfBoundsException")
    void testValidateParametersOffSet() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(5)) {
            IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class,
                    () -> out.write(arrayWithElements, -1, 3));
            assertEquals("OffSet: -1 less 0", exception.getMessage());
        }
    }

    @Test
    @DisplayName("When in method write parameter length is negative then expect IndexOutOfBoundsException")
    void testValidateParametersLength() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(5)) {
            IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class,
                    () -> out.write(arrayWithElements, 3, -1));
            assertEquals("Length: -1 less 0", exception.getMessage());
        }
    }

    @Test
    @DisplayName("When buffer length less or equals count then buffer length expends to count")
    void testGrowMethod() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(2)) {
            out.write(2);
            out.write(2);
            byte[] byteArray = out.toByteArray();
            assertEquals(2, byteArray.length);

            out.write(2);
            out.write(2);
            byte[] byteArray2 = out.toByteArray();
            assertEquals(4, byteArray2.length);
        }
    }

    @Test
    @DisplayName("When use method read then return byte from target array, return -1 when array is end")
    void testRead() throws IOException {
        try (ByteArrayInputStream in = new ByteArrayInputStream(arrayWithElements)) {

            assertEquals(1, in.read());
            assertEquals(2, in.read());
            assertEquals(3, in.read());
            assertEquals(4, in.read());
            assertEquals(5, in.read());
            assertEquals(-1, in.read());
        }
    }

    @Test
    @DisplayName("When use method read with parameters array, then filling from target array to array in parameters")
    void testReadByArray() throws IOException {
        try (ByteArrayInputStream in = new ByteArrayInputStream(arrayWithElements)) {
            int count = in.read(emptyArray);

            assertEquals(5, count);

            assertEquals(1, emptyArray[0]);
            assertEquals(2, emptyArray[1]);
            assertEquals(3, emptyArray[2]);
            assertEquals(4, emptyArray[3]);
            assertEquals(5, emptyArray[4]);
        }
    }

    @Test
    @DisplayName("When use method read with parameters array, offSet and length, then filing target array from offSet position on lengths value bytes from buffer array")
    void testReadByArrayWithParametersOffSetAndLength() throws IOException {
        try (ByteArrayInputStream in = new ByteArrayInputStream(arrayWithElements)) {
            int count = in.read(emptyArray, 1, 3);

            assertEquals(3, count);
            assertEquals(0, emptyArray[0]);
            assertEquals(1, emptyArray[1]);
            assertEquals(2, emptyArray[2]);
            assertEquals(3, emptyArray[3]);
            assertEquals(0, emptyArray[4]);
        }
    }

    @Test
    @DisplayName("When create stream constructor with parameters offSet and length, then read return bytes from offSet position on lengths value bytes ")
    void testReadFromInputStreamConstructorWithParametersOffSetAndLength() throws IOException {
        try (ByteArrayInputStream in = new ByteArrayInputStream(arrayWithElements, 1, 3)) {

            assertEquals(2, in.read());
            assertEquals(3, in.read());
            assertEquals(4, in.read());
        }
    }

    @Test
    @DisplayName("When create stream constructor with parameters offSet and length and use method read with same parameters, then filling target array from current position parameter array to offSet position on lengths value bytes")
    void testReadByArrayFromInputStreamConstructorWithParametersOffSetAndLength() throws IOException {
        try (ByteArrayInputStream in = new ByteArrayInputStream(arrayWithElements, 1, 3)) {
            int count = in.read(emptyArray, 1, 3);

            assertEquals(3, count);

            assertEquals(0, emptyArray[0]);
            assertEquals(2, emptyArray[1]);
            assertEquals(3, emptyArray[2]);
            assertEquals(4, emptyArray[3]);
        }
    }

    @Test
    @DisplayName("When in method read parameter offSet is negative then expect IndexOutOfBoundsException")
    void testValidationParametersInputStreamOffSet() throws IOException {
        try (ByteArrayInputStream in = new ByteArrayInputStream(arrayWithElements)) {
            IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class,
                    () -> in.read(emptyArray, -1, 2));
            assertEquals("OffSet: -1 less 0", exception.getMessage());
        }
    }

    @Test
    @DisplayName("When in method read parameter length is negative then expect IndexOutOfBoundsException")
    void testValidationParametersInputStreamLength() throws IOException {
        try (ByteArrayInputStream in = new ByteArrayInputStream(arrayWithElements)) {
            IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class,
                    () -> in.read(emptyArray, 1, -1));
            assertEquals("Length: -1 less 0", exception.getMessage());
        }
    }

    @Test
    @DisplayName("When sum of parameters offSet and length more then array length then expect IndexOutOfBoundsException")
    void testValidationParametersInputStreamArrayLength() throws IOException {
        try (ByteArrayInputStream in = new ByteArrayInputStream(arrayWithElements)) {
            IndexOutOfBoundsException exception = assertThrows(IndexOutOfBoundsException.class,
                    () -> in.read(emptyArray, 2, 4));
            assertEquals("Sum of offSet and length: 6 more than arrayLength: 5", exception.getMessage());
        }
    }
}
