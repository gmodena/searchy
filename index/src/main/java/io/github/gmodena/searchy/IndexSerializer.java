package io.github.gmodena.searchy;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class IndexSerializer {
    /**
     * Serialize an Index object to a byte array.
     *
     * @param index
     * @return
     * @throws IOException
     */
    public static byte[] serialize(Index index) throws IOException {
        try (ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
             ObjectOutputStream out = new ObjectOutputStream(byteOut)) {
            out.writeObject(index);
            out.close();
            return byteOut.toByteArray();
        }
    }

    /**
     * Serialize an Index object to a gzip compressed file.
     *
     * @param index    The Index object to serialize.
     * @param fileName The name of the file to write the serialized object to.
     * @throws IOException If an I/O error occurs.
     */
    public static void serialize(Index index, String fileName) throws IOException {
        try (FileOutputStream fileOut = new FileOutputStream(fileName);
             GZIPOutputStream gzipOut = new GZIPOutputStream(fileOut);
             ObjectOutputStream out = new ObjectOutputStream(gzipOut)) {
            out.writeObject(index);
        }
    }

    /**
     * Deserialize an Index object from a byte array.
     *
     * @param bytes The byte array to read the serialized object from.
     * @return The deserialized Index object.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the Index class is not found.
     */
    public static Index deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes);
             ObjectInputStream in = new ObjectInputStream(byteIn)) {
            return (Index) in.readObject();
        }
    }

    /**
     * Deserialize an Index object from a file.
     *
     * @param fileName The name of the file to read the serialized object from.
     * @return The deserialized Index object.
     * @throws IOException            If an I/O error occurs.
     * @throws ClassNotFoundException If the Index class is not found.
     */
    public static Index deserialize(String fileName) throws IOException, ClassNotFoundException {
        try (FileInputStream fileIn = new FileInputStream(fileName);
             GZIPInputStream gzipIn = new GZIPInputStream(fileIn);
             ObjectInputStream in = new ObjectInputStream(gzipIn)) {
            return (Index) in.readObject();
        }
    }
}