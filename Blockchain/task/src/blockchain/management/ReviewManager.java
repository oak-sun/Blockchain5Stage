package blockchain.management;

import blockchain.model.block.Block;
import blockchain.model.block.Blockchain;
import blockchain.model.chat.Post;
import java.util.List;
import java.util.Objects;

public class ReviewManager {

    public static boolean isBlockchainValid() {
        var bChain = Blockchain.getInstance();
        var b = bChain.getBlocks();

        if (isBlockchainEmpty()) {
            return true;
        }

        if (!isFirstBlockValid(b.get(0))) {
            return false;
        }

        for (int i = 1; i < b.size(); i++) {

            if (hasWrongPreviousHash(b.get(i),
                    b.get(i - 1))) {
                return false;
            }

            if (hasOneOfThePostsAnInvalidId(b.get(i),
                    i - 1)) {
                return false;
            }

            if (hasOneOfThePostsAnInvalidSignature(b.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNewBlockValid(Block b) {

        if (isBlockchainEmpty()) {
            return isFirstBlockValid(b);
        }

        var lastB = Blockchain
                         .getInstance()
                         .getLastBlock();
        if (hasWrongPreviousHash(b, lastB)) {
            return false;
        }
        var indexOfLastB = Blockchain
                .getInstance()
                .getNbOfBlocks()
                - 1;
        if (hasOneOfThePostsAnInvalidId(b,
                indexOfLastB)) {
            return false;
        }

        if (hasOneOfThePostsAnInvalidSignature(b)) {
            return false;
        }
        return !hasWrongNumberOfZerosInHash(b);
    }
    private static boolean isBlockchainEmpty() {
        var b = Blockchain
                          .getInstance()
                           .getBlocks();
        return b.size() == 0;
    }
    private static boolean isFirstBlockValid(Block b) {
        return "0".equals(b
                .getHashOfPreviousBlock());
    }

    private static boolean hasOneOfThePostsAnInvalidId(Block b,
                                                       int indexOfPreviousB) {
        if (indexOfPreviousB == 0) {
            return false;
        }
        List<Block> blocks = Blockchain
                         .getInstance()
                          .getBlocks();
        var previousBMax = blocks.get(indexOfPreviousB)
                .getPosts()
                .stream()
                .map(Post::getId)
                .mapToLong(l -> l)
                .max();
        if (previousBMax.isPresent()) {
            return b
                    .getPosts()
                    .stream()
                    .anyMatch(p ->
                            p.getId() <= previousBMax
                                              .getAsLong());
        }
        return hasOneOfThePostsAnInvalidId(b,
                indexOfPreviousB - 1);
    }

    private static boolean hasWrongPreviousHash(Block b,
                                                Block previousB) {
        return !Objects
                .equals(previousB.getHash(), b.
                        getHashOfPreviousBlock());
    }

    private static boolean hasOneOfThePostsAnInvalidSignature(Block b) {
        return b
                .getPosts()
                .stream()
                .anyMatch(p -> !CryptoManager.verify(p));
    }
    private static boolean hasWrongNumberOfZerosInHash(Block b) {
        var nbOfZeros = Blockchain
                             .getInstance()
                             .getNbOfZeros();
        return !b
                .getHash()
                .matches(String.format("0{%s}.*",
                        nbOfZeros));
    }
}