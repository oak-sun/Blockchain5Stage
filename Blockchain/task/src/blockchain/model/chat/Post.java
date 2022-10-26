package blockchain.model.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.io.Serial;
import java.io.Serializable;
import java.security.PublicKey;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Post implements Serializable {

    String postman;
    String text;

    LocalDateTime dateTime;
    long id;

    byte[] signature;

    PublicKey publicKey;

    @Serial
    private static final long serialVersionUID = 12L;

    @Override
    public String toString() {
        return String.format("%s: %s",
                postman,
                text);
    }
}