package blockchain.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class StringUtil {
    public static String applySha256(String input){
        try {
            /* Applies sha256 to our input */
            var hash =  MessageDigest
                    .getInstance("SHA-256")
                    .digest(input
                            .getBytes(StandardCharsets.UTF_8));
            var hexString = new StringBuilder();
            for (byte elem: hash) {
                var hex = Integer.toHexString(0xff & elem);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

}