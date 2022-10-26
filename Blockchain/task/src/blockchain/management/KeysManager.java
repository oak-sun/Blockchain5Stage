package blockchain.management;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class KeysManager {

    public static KeyPair generateKeyPair()
                              throws NoSuchAlgorithmException,
                                    NoSuchProviderException {
        var keyGen = KeyPairGenerator
                .getInstance("RSA");
        var secRndm = SecureRandom
                .getInstance("SHA1PRNG",
                        "SUN");
        keyGen.initialize(1024, secRndm);
        return keyGen.generateKeyPair();
    }
}
