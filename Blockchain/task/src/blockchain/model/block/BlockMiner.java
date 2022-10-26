package blockchain.model.block;

import blockchain.management.HashesManager;
import blockchain.model.chat.Post;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
public class BlockMiner implements Runnable {
    private final Blockchain bChain;
    private final int nbOfBlocks;
    private int currentNbOfBlocks;

    private Block generateBlock(int id,
                                String hashOfPreviousB,
                                List<Post> posts,
                                long nbOfZeros) {
        var rand = new Random();
        var magicNumber = rand.nextInt(
                                   Integer.MAX_VALUE);
        var timestamp = System.currentTimeMillis();
        var minerId = Thread
                                .currentThread()
                                 .getName();
        var pattern = String.format("0{%s}.*",
                                         nbOfZeros);
        var hash = HashesManager.generate(hashOfPreviousB,
                id,
                timestamp,
                magicNumber,
                posts,
                minerId);
        while (!hash.matches(pattern)) {
            if (blockHasBeenAdded()) return null;
            magicNumber = rand.nextInt(Integer.MAX_VALUE);
            hash = HashesManager.generate(
                    hashOfPreviousB,
                    id,
                    timestamp,
                    magicNumber,
                    posts,
                    minerId);
        }
        var generationDuration = (int) (System.currentTimeMillis() -
                timestamp)
                / 1000;
        return new Block(
                id,
                hashOfPreviousB,
                timestamp,
                magicNumber,
                generationDuration,
                posts,
                minerId);
    }

    private boolean blockHasBeenAdded() {
        return bChain.getNbOfBlocks() > currentNbOfBlocks;
    }

    @Override
    public void run() {
        currentNbOfBlocks = bChain.getNbOfBlocks();

        while (currentNbOfBlocks < nbOfBlocks) {
            var nbOfZeros = bChain.getNbOfZeros();
            var lastB = bChain.getLastBlock();
            List<Post> posts = bChain.getNewMessages();
            var previousHash = currentNbOfBlocks > 0 ?
                                      lastB.getHash() : "0";
            var id = currentNbOfBlocks > 0 ?
                                lastB.getId() + 1
                                 : 1;
            var b = generateBlock(id,
                    previousHash,
                    posts, nbOfZeros);
            if (b != null) {
                synchronized (BlockMiner.class) {
                    bChain.addBlock(b);
                }
            }
            currentNbOfBlocks = bChain.getNbOfBlocks();
        }
    }
}
