package Games;

import head.bot.Main;
import Server.Server;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import persistence.JsonReader;
import persistence.JsonWriter;
import Server.Player;
import Server.User;
import Server.GameState;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class CoinFlipGuess extends ListenerAdapter {
    private static final String JSON_STORE = "./Data/Profile.json";
    private Server server;
    private Player player;
    private User user;
    private GameState gameState;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private String side;

    //    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
    public void CoinFlipStart(Server server, GuildMessageReactionAddEvent event) {
        this.server = server;
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        ArrayList<Integer> coin = new ArrayList<>();
        coin.add(1);
        coin.add(2);

        Collections.shuffle(coin);
        event.getChannel().sendMessage("Game has started; Heads or Tails?").queue();

        if (coin.get(0) == 1) {
            side = "heads";
        } else {
            side = "tails";
        }
        System.out.println("The Answer: " + side);

        try {
            server.addPlayer(new Player("coin", side));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        String[] args = event.getMessage().getContentRaw().split("\\s+");

        String userName = event.getAuthor().getName();
        String nickName = event.getMember().getNickname();
        String guess = "";

        String output = null;
        String outputWrong = null;

        loadProfile();

        if (args[0].equalsIgnoreCase(Main.prefix + "guess")) {

            try {
                side = server.getPlayer("coin").getGuess();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                gameState = server.getGameState("state");

            } catch (Exception e) {
                e.printStackTrace();
            }

                try {
                    server.addPlayer(new Player(userName, guess));
                } catch (Exception e) {
                    System.out.println("player cant be added");
                }

                if (args.length == 1) {
                    event.getChannel().sendMessage("You need to guess a side!").queue();
                    return;
                } else {
                    guess = args[1];
                }


                if (!gameState.getState()) {
                    event.getChannel().sendMessage("No game running right now").queue();
                } else {
                    try {
                        player = server.getPlayer(userName);
                        user = server.getUser(event.getAuthor().getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    for (int n = 0; n < 3; n++) {
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    player.setGuess(guess);

                    System.out.println(player.getGuess() + " guess from load");
                    System.out.println("This is the side: " + side);

                    if (player.getGuess() != null && player.getGuess().equalsIgnoreCase(side)) {
                        if (nickName != null) {
                            output = nickName + " is right!";
                        } else {
                            output = userName + " is right!";
                        }
                        event.getChannel().sendMessage(output).queue();
                        user.xpUp();
                    } else {
                        if (nickName != null) {
                            outputWrong = nickName + " is wrong!";
                        } else {
                            outputWrong = userName + " is wrong!";
                        }
                        event.getChannel().sendMessage(outputWrong).queue();
                    }
                    player.setGuess("");
                    try {
                        server.removePlayer(player);
                        server.removePlayer(server.getPlayer("coin"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    gameState.setState(false);
                    saveProfile();
                }
            }
//
//        if (nickName == "yes") {
//            return null;
//        } else if (nickName == "dog") {
//            re
//        }

    }

    // MODIFIES: this
// EFFECTS: loads workroom from file
    public void loadProfile() {
        try {
            server = jsonReader.read();
//            System.out.println("Loaded " + server.getName() + " from " + JSON_STORE);
        } catch (IOException e) {
//            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    private void saveProfile() {
        try {
            jsonWriter.open();
            jsonWriter.write(server);
            jsonWriter.close();
//            System.out.println("Saved " + server.getName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
//            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }
}
