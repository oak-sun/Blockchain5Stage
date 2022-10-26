package blockchain.management;

import blockchain.model.chat.Post;
import blockchain.util.StringUtil;
import java.util.List;
import java.util.stream.Collectors;

public class HashesManager {

    public static String generate(String hashOfPreviousB,
                                  int id,
                                  long timestamp,
                                  int magicNumber,
                                  List<Post> posts,
                                  String minerId) {
        var strPosts = posts
                             .stream()
                             .map(Object::toString)
                            .collect(Collectors
                            .joining("\n"));
        var data = hashOfPreviousB +
                   id +
                   timestamp +
                   strPosts +
                   minerId +
                   magicNumber;
        return StringUtil.applySha256(data);
    }
}
