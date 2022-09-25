package Games;

import head.bot.Main;
import Server.Server;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import persistence.JsonReader;
import persistence.JsonWriter;
import Server.GameState;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Games extends ListenerAdapter {
    private static final String JSON_STORE = "./Data/Profile.json";
    JsonWriter jsonWriter;
    JsonReader jsonReader;
    Server server;
    GameState state;
    GuildMessageReceivedEvent messageEvent;


    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        server = new Server("Matthew Game");
        state = new GameState("state", false);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        messageEvent = event;

        loadProfile();

        String[] args = event.getMessage().getContentRaw().split("\\s+");

//        if (args[0].equalsIgnoreCase(Main.prefix + "game")) {
//
//            if (args.length == 1) {
//                EmbedBuilder info = new EmbedBuilder();
//                info.setTitle("Which Game Would You Like?");
//                info.addField("", "1. Coin Flip Guess", false);
//                info.setFooter("Choose The Number");
////                event.getChannel().sendMessage(info.build()).queue(message -> message.addReaction("U+1F600"));
////                event.getChannel().sendMessage("Pick One").complete().addReaction("U-1F600").queue();
////                event.getChannel().sendMessage(info.build()).complete().addReaction("1️⃣").queue();
//                event.getChannel().sendMessage(info.build()).queue(message -> {
//                    message.addReaction("1️⃣").queue();
//                    message.addReaction("2️⃣").queue();
//                    message.addReaction("1️⃣").queue();
//                });
//            }
//        }

        if (args[0].equalsIgnoreCase(Main.prefix + "hierarchy")) {

            if (args.length == 1) {
                EmbedBuilder info = new EmbedBuilder();
                info.setTitle("Server hierarchy");
                info.addField("", "1. Alex", false);
                info.addField("", "2. Eric ", false);
                info.addField("", "3. Vaughn", false);
                info.addField("", "3. Ethan", false);
                info.addField("", "4. Lavis", false);
                info.addField("", "5. Andre", false);
                info.addField("", "6. Matthew (Pending", false);
                info.addField("", "7. Ferd (Pending)", false);
                info.addField("", "7. Sanjit ", false);
                info.setFooter("Last Updated 25/09/22");
//                event.getChannel().sendMessage(info.build()).queue(message -> message.addReaction("U+1F600"));
//                event.getChannel().sendMessage("Pick One").complete().addReaction("U-1F600").queue();
//                event.getChannel().sendMessage(info.build()).complete().addReaction("1️⃣").queue();

                event.getChannel().sendMessage(info.build()).queue(message -> {
//                    message.addReaction("1️⃣").queue();
//                    message.addReaction("2️⃣").queue();
//                    message.addReaction("1️⃣").queue();
                });
            }
        }
        saveProfile();
    }

    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event) {

        loadProfile();

        try {
            state = server.getGameState("state");
        } catch (Exception e) {
            System.out.println("gameState not found");
        }

        if (event.getReactionEmote().getName().equals("1️⃣")
                && !event.getMember().getUser().equals(event.getJDA().getSelfUser())
                && !state.getState()) {

            state.setState(true);

            CoinFlipGuess coinGame = new CoinFlipGuess();
            coinGame.CoinFlipStart(server, event);
        }
        saveProfile();
    }


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
