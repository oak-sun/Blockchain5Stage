package blockchain;

import blockchain.management.ReviewManager;
import blockchain.management.TasksManager;

public class Main {
    public static String fileName =
            "blockchain.data";

    public static void main(String[] args) {

        var bChain = TasksManager
                                 .retrieveOrCreateBlockchain(fileName);
        bChain.setFileName(fileName);
        var manager = new TasksManager(bChain);
        var nbOfBlocksToAdd = 5;
        var nbOfThreads = 6;
        System.out.printf("Adding %d blocks...%n",
                           nbOfBlocksToAdd);
        manager.startBlockchain(nbOfThreads,
                               nbOfBlocksToAdd);
        System.out.println("Is blockchain valid? " +
                            ReviewManager
                            .isBlockchainValid());
        System.out.println();
        System.out.println(bChain);
    }
}



