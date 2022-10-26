package blockchain.management;

import blockchain.model.block.BlockMiner;
import blockchain.model.block.Blockchain;
import blockchain.model.chat.Postman;
import blockchain.util.Serializer;
import lombok.AllArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class TasksManager {
    Blockchain bChain;
    public void startBlockchain(int nbOfThreads,
                                int nbOfBlocksToAdd) {

        var totalNbOfBlocks = nbOfBlocksToAdd +
                                bChain.getNbOfBlocks();
        List<Thread> threads = new ArrayList<>(nbOfThreads);

        for (int i = 0; i < nbOfThreads; i++) {
            threads.add(new Thread(
                    new BlockMiner(bChain, totalNbOfBlocks),
                    "#" + (i + 1)
            ));
            threads.get(i).start();
        }
        startPostman();
        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void startPostman() {
        var postman = new Postman();
        var postThread = new Thread(postman);
        postThread.start();
    }

    public static Blockchain retrieveOrCreateBlockchain(String fileName) {
        Blockchain bChain;

        try {
            bChain = (Blockchain) Serializer
                                         .deserialize(fileName);
            System.out.println("Retrieving existing Blockchain");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Creating new Blockchain");
            bChain = Blockchain.getInstance();
        }

        if (!ReviewManager.isBlockchainValid()) {
            System.out.println("Invalid blockchain! " +
                                       "Creating new one");
            bChain = Blockchain.getInstance();
        }
        return bChain;
    }
}