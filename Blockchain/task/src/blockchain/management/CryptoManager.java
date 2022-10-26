package blockchain.management;

import blockchain.model.chat.Post;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;

public class CryptoManager {
    public static boolean verify(Post p) {

        Signature sign;
        try {
            sign = Signature
                    .getInstance("SHA1withRSA");
            sign.initVerify(p.getPublicKey());
            var data = p.getPostman() +
                    p.getText() +
                    p.getDateTime().toString() +
                    p.getId();
            sign.update(
                    data.getBytes());
            return sign.verify(
                    p.getSignature());
        } catch (NoSuchAlgorithmException |
                 InvalidKeyException |
                 SignatureException e) {

            e.printStackTrace();
        }
        return false;
    }
}