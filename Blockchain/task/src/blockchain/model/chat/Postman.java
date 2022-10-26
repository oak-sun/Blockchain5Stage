package blockchain.model.chat;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Postman implements Runnable {
    private final List<Map.Entry<String,
            String>> textPosts =
            List.of(
                    Map.entry("Oxana", "Hey, I'm first!"),
                    Map.entry("Olga", "It's not fair!"),
                    Map.entry("Olga", "You always will be first" +
                            " because it is your blockchain!"),
                    Map.entry("Olga", "Anyway, thank you for this " +
                            "amazing chat."),
                    Map.entry("Egor", "You're welcome :)"),
                    Map.entry("Fedor", "Hey Tom, nice chat"),
                    Map.entry("Fedor", "How did you do that?"),
                    Map.entry("Oxana", "Well, I asked Vladimir"),
                    Map.entry("Oxana", "You know him, right?"),
                    Map.entry("Olga", "Blah blah blah"),
                    Map.entry("Nikita", "Really? Oh wow"),
                    Map.entry("Svyatoslav", "Yeah and blah blah blah...")
            );

    @Override
    public void run() {
        var rand = new Random();
        textPosts.forEach(

                post -> {
                    try {
                        TimeUnit.SECONDS
                                .sleep(
                                        rand.nextInt(4));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Chatbot
                            .getChatbot(post.getKey())
                            .sendPost(post.getValue());
                }
        );
    }
}
