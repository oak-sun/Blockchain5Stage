package blockchain.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Serializer {
    public static void serialize(Object obj,
                                 String fileName)
                                 throws IOException {
        var objOut = new ObjectOutputStream(
                         new BufferedOutputStream(
                        new FileOutputStream(fileName)));
        objOut.writeObject(obj);
        objOut.close();
    }
    public static Object deserialize(String fileName)
            throws IOException,
            ClassNotFoundException {

        var objIn = new ObjectInputStream(
                new BufferedInputStream(
                        new FileInputStream(fileName)));
        var obj = objIn.readObject();
        objIn.close();
        return obj;
    }
}