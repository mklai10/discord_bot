package Server;

import head.bot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
//import persistence.JsonReader;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;

public class UserCommands extends ListenerAdapter {
    private static final String JSON_STORE = "./Data/Profile.json";
    private Server server;
    private User user;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private String playerName;
    private String desc;
    private String image;

    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        server = new Server("Server Game");
        user = new User("dog", 1, 10, 10, "", "No Desc");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (args[0].equalsIgnoreCase(Main.prefix + "register")) {
            loadProfile();
            String userName = event.getAuthor().getName();
            String nickName = event.getMember().getNickname();
            this.playerName = userName;
            desc = "No Desc";
            image = "https://cdn.discordapp.com/attachments/703097181845979196/832129981370990593/ajwm5.png";
            try {
                server.addUser(new User(userName, 1, 0, 10, image, desc));
                if (nickName != null) {
                    event.getChannel().sendMessage(nickName + " has been registered").queue();
                } else {
                    event.getChannel().sendMessage(userName + " has been registered").queue();
                }
            } catch (Exception e) {
                if (nickName != null) {
                    event.getChannel().sendMessage(nickName + " has already been registered").queue();
                } else {
                    event.getChannel().sendMessage(userName + " has already been registered").queue();
                }
            }
            saveProfile();
            loadProfile();
        }

        if (args[0].equalsIgnoreCase(Main.prefix + "player")) {
            loadProfile();
            String userName = event.getAuthor().getName();
            String nickName = event.getMember().getNickname();
            String guess = "";
            try {
                server.addPlayer(new Player(userName, guess));
                if (nickName != null) {
                    event.getChannel().sendMessage(nickName + " has been added").queue();
                } else {
                    event.getChannel().sendMessage(userName + " has been added").queue();
                }
            } catch (Exception e) {
                if (nickName != null) {
                    event.getChannel().sendMessage(nickName + " has already been added").queue();
                } else {
                    event.getChannel().sendMessage(userName + " has already been added").queue();
                }
            }
            saveProfile();
        }

        if (args[0].equalsIgnoreCase(Main.prefix + "gamestate")) {
            loadProfile();
            try {
                server.addState(new GameState("state", false));
            } catch (Exception e) {
                System.out.println("state fail");
            }
            saveProfile();
        }

        if (args[0].equalsIgnoreCase(Main.prefix + "false")) {
            loadProfile();
            try {
                GameState test = server.getGameState("state");
                test.setState(false);
            } catch (Exception e) {
                System.out.println("state fail");
            }
            saveProfile();
        }

        if (args[0].equalsIgnoreCase(Main.prefix + "profile")) {
            loadProfile();
            String nickName = event.getMember().getNickname();
            String userName = event.getMessage().getAuthor().getName();
            String img;
            User user = null;
            try {
                user = server.getUser(userName);
            } catch (Exception e) {
                event.getChannel().sendMessage("You have not been registered").queue();
                return;
            }

            if (args.length == 2) {
//                if (args[1].charAt(0) != '@') {
//                    event.getChannel().sendMessage("That person is not valid").queue();
//                    return;
//                }
                String mentionedName = event.getMessage().getMentionedMembers().get(0).getUser().getName();
                System.out.println(mentionedName);
                try {
                    user = server.getUser(mentionedName);
                } catch (Exception e) {
                    event.getChannel().sendMessage("That person is not registered").queue();
                    return;
                }

                img = event.getMessage().getMentionedMembers().get(0).getUser().getAvatarUrl();

                EmbedBuilder info = new EmbedBuilder();
                if (nickName != null) {
                    info.setTitle(mentionedName + "'s Profile");
                } else {
                    info.setTitle(mentionedName + "'s Profile");
                }
                info.addField("", user.getDesc(), false);
                info.setImage(user.getImage());
                info.addField("Level", String.valueOf((user.getLevel())), false);
                if (nickName != null) {
                    info.setFooter(nickName, img);
                } else {
                    info.setFooter(userName, img);
                }
                info.addField("XP", String.valueOf(user.getXp()) + "/" + user.getXpNeeded(), false);

                event.getChannel().sendMessage(info.build()).queue();
                info.clear();
            } else {

                img = event.getAuthor().getAvatarUrl();
                try {
                    user = server.getUser(event.getAuthor().getName());
                } catch (Exception e) {
                    event.getChannel().sendMessage("You have not been registered").queue();
                    return;
                }

                EmbedBuilder info = new EmbedBuilder();
                if (nickName != null) {
                    info.setTitle(nickName + "'s Profile");
                } else {
                    info.setTitle(userName + "'s Profile");
                }
                info.addField("", user.getDesc(), false);
                info.setImage(user.getImage());
                info.addField("Level", String.valueOf((user.getLevel())), false);
                if (nickName != null) {
                    info.setFooter(nickName, img);
                } else {
                    info.setFooter(userName, img);
                }
                info.addField("XP", String.valueOf(user.getXp()) + "/" + user.getXpNeeded(), false);

                event.getChannel().sendMessage(info.build()).queue();
                info.clear();
            }
        }

        if (args[0].equalsIgnoreCase(Main.prefix + "desc")) {
            loadProfile();
            String playerName = event.getAuthor().getName();

            if (args.length == 1) {
                event.getChannel().sendMessage("You need to add a description").queue();
                return;
            }

            try {
                user = server.getUser(playerName);
            } catch (Exception e) {
                event.getChannel().sendMessage("You have not been registered").queue();
            }
            String desc = "";
            for (String arg : args) {
                desc = desc + arg + " ";
            }
            desc = desc.substring(6);
            user.setDesc(desc);
            event.getChannel().sendMessage("Description has been set").queue();
            saveProfile();
        }

        if (args[0].equalsIgnoreCase(Main.prefix + "image")) {
            loadProfile();
            String playerName = event.getAuthor().getName();

            if (args.length == 1) {
                event.getChannel().sendMessage("You need to add a url").queue();
                return;
            }

            try {
                user = server.getUser(playerName);
            } catch (Exception e) {
                event.getChannel().sendMessage("You have not been registered").queue();
            }
            String image = "";
            image = image + args[1];
            user.setImage(image);

            event.getChannel().sendMessage("Image has been set").queue();
            saveProfile();
        }

        if (args[0].equalsIgnoreCase(Main.prefix + "gay?")) {
            loadProfile();

            String name = event.getMember().getUser().getName();
            String userName = event.getMessage().getAuthor().getName();

            try {
                user = server.getUser(userName);

            } catch (Exception e) {
                event.getChannel().sendMessage("You are not registered").queue();
                return;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    public void loadProfile() {
        try {
            server = jsonReader.read();
            System.out.println("Loaded " + server.getName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    private void saveProfile() {
        try {
            jsonWriter.open();
            jsonWriter.write(server);
            jsonWriter.close();
            System.out.println("Saved " + server.getName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }
}
