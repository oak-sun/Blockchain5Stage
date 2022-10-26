package blockchain.model.block;
import blockchain.management.HashesManager;
import blockchain.model.chat.Post;
import lombok.Getter;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
public class Block implements Serializable {

    @Serial
    private static final long serialVersionUID = 12L;
    private final int id;
    private final long timeStamp;
    private final int magicNumber;
    private final int generationDuration;
    private String changeOfNbOfZeros;
    private final String minerId;
    private final String hash;
    private final String hashOfPreviousBlock;
    private final List<Post> posts;

    public Block(int id,
                 String hashOfPreviousBlock,
                 long timestamp,
                 int magicNumber,
                 int generationDuration,
                 List<Post> posts,
                 String minerId) {
        this.id = id;
        this.hashOfPreviousBlock = hashOfPreviousBlock;
        this.magicNumber = magicNumber;
        this.generationDuration = generationDuration;
        this.timeStamp = timestamp;
        this.minerId = minerId;
        this.posts = posts;
        this.hash = HashesManager.generate(
                         hashOfPreviousBlock,
                         id,
                         timeStamp,
                         magicNumber,
                         posts,
                         minerId);
    }

    public void setChangeOfZeros(int change) {
        switch (change) {
            case 0 -> this.changeOfNbOfZeros =
                    "N stays the same";
            case 1 -> this.changeOfNbOfZeros =
                    "N was increased by 1";
            case -1 -> this.changeOfNbOfZeros =
                    "N was decreased by 1";
            default -> this.changeOfNbOfZeros = "?";
        }
    }
    @Override
    public String toString() {
        var strPosts = posts.isEmpty() ?
                          "no messages" : posts
                                          .stream()
                                          .map(Objects::toString)
                                         .collect(Collectors
                                                 .joining("\n"));

        return String.format("Block: %n" +
                        "Created by Miner %s%n" +
                        "Id: %s%nTimestamp: %d%n" +
                        "Magic number: %d%n" +
                        "Hash of the previous block: %n%s%n" +
                        "Hash of the block: %n%s%n" +
                        "Block data:%n" +
                        "%s%n" +
                        "Block was generating for %d seconds%n" +
                        "%s%n",
                minerId,
                id,
                timeStamp,
                magicNumber,
                hashOfPreviousBlock,
                hash,
                strPosts,
                generationDuration,
                changeOfNbOfZeros);
    }
}