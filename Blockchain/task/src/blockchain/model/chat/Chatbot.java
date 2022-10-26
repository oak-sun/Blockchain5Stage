package blockchain.model.chat;

import blockchain.management.KeysManager;
import blockchain.model.block.Blockchain;
import lombok.Getter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Chatbot {
    private static final Map<String, Chatbot> users =
            new HashMap<>();
    private final String name;
    private final Blockchain bChain;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    private Chatbot(String name) {
        this.name = name;
        this.bChain = Blockchain
                          .getInstance();
        users.put(name, this);
        createKeyPair();
    }

    public static Chatbot getChatbot(String name) {
        return users
                .getOrDefault(name,
                        new Chatbot(name));
    }

    public void createKeyPair() {
        try {
            var keyPair = KeysManager
                                   .generateKeyPair();
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();

        } catch (NoSuchAlgorithmException |
                 NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    public void sendPost(String text) {
        byte[] signature;
        try {
            var dateTime = LocalDateTime.now();
            var id = bChain.getNewDataId();
            signature = signMessage(name +
                    text +
                    dateTime +
                    id);
            var p = new Post(name,
                               text,
                               dateTime,
                               id,
                               signature,
                              publicKey);

            bChain.addPost(p);
        } catch (NoSuchAlgorithmException |
                 InvalidKeyException |
                 SignatureException e) {
            e.printStackTrace();
        }
    }
    private byte[] signMessage(String data)
                               throws NoSuchAlgorithmException,
                                      InvalidKeyException,
                                      SignatureException {
        var rsa = Signature
                .getInstance("SHA1withRSA");
        rsa.initSign(privateKey);
        rsa.update(data.getBytes());
        return rsa.sign();
    }
}