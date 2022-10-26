 package blockchain.model.block;

 import blockchain.management.ReviewManager;
 import blockchain.model.chat.Post;
 import blockchain.util.Serializer;
 import lombok.Getter;
 import lombok.NoArgsConstructor;
 import lombok.Setter;

 import java.io.IOException;
 import java.io.ObjectInputStream;
 import java.io.Serial;
 import java.io.Serializable;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.stream.Collectors;

 @NoArgsConstructor
 @Getter
 @Setter
 public class Blockchain implements Serializable {

     @Serial
     private static final long serialVersionUID = 12L;
     private static Blockchain instance;
     private String fileName;
     private long nbOfZeros = 0L;
     private long dataId = 0L;
     private final List<Block> blocks = new ArrayList<>();
     private final List<Post> newPostsOne =
             new ArrayList<>();
     private final List<Post> newPostsTwo =
             new ArrayList<>();

     private List<Post> newPostsCurrent = newPostsOne;

     public static Blockchain getInstance() {
         if (instance == null) {
             instance = new Blockchain();
         }
         return instance;
     }

     @Serial
     private void readObject(ObjectInputStream objIn)
             throws IOException,
             ClassNotFoundException {
         objIn.defaultReadObject();
         instance = this;
     }

     public List<Post> getNewMessages() {
         return new ArrayList<>(
                           newPostsCurrent == newPostsOne ?
                               newPostsTwo : newPostsOne);
     }

     public void addPost(Post newPost) {

         List<Post> postsBeingProcessed =
                          newPostsCurrent == newPostsOne ?
                                newPostsTwo : newPostsOne;
         var success = false;
         var previousBlockMax = postsBeingProcessed
                 .stream()
                 .map(Post::getId)
                 .mapToLong(l -> l)
                 .max()
                 .orElse(0);
         if (previousBlockMax < newPost.getId()) {
             newPostsCurrent.add(newPost);
             success = true;
         }
         if (!success)
             System.out.printf("User %s: Message failed!",
                                  newPost.getPostman());
     }

     private void emptyMessages() {
         if (newPostsCurrent == newPostsOne) {
             newPostsTwo.clear();
             newPostsCurrent = newPostsTwo;
         } else {
             newPostsOne.clear();
             newPostsCurrent = newPostsOne;
         }
     }

     public int getNbOfBlocks() {
         return blocks.size();
     }

     public Block getLastBlock() {
         return blocks.size() > 0 ?
                 blocks.get(blocks.size() - 1)
                 : null;
     }

     public void addBlock(Block b) {
         if (ReviewManager.isNewBlockValid(b)) {
             var update = updateNbOfZeros(b
                            .getGenerationDuration());
             b.setChangeOfZeros(update);
             blocks.add(b);
             emptyMessages();
             serializeBlockchain();
             System.out.printf("Added new block... (%s)%n",
                     b.getMinerId());
         }
     }

     public long getNewDataId() {
         return ++dataId;
     }

     private void serializeBlockchain() {

         try {
             Serializer.serialize(this, fileName);

         } catch (Exception e) {
             e.printStackTrace();
         }
     }

     private int updateNbOfZeros(int generationDuration) {
         var DURATION_LOWER_BOUND = 1; //seconds
         var DURATION_HIGHER_BOUND = 5; //seconds
         if (generationDuration < DURATION_LOWER_BOUND) {
             nbOfZeros++;
             return 1;
         }
         if (generationDuration > DURATION_HIGHER_BOUND) {
             nbOfZeros--;
             return -1;
         }
         return 0;
     }

     @Override
     public String toString() {
         return blocks
                 .stream()
                 .skip(Math.max(
                         0,
                         blocks.size() - 5))
                 .map(String::valueOf)
                 .collect(Collectors.joining("\n"));
     }
 }