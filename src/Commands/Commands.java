package Commands;


import head.bot.Main;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter {
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (args[0].equalsIgnoreCase(Main.prefix + "info")) {
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle("😳 Bot's Information 😳");
            info.setDescription("Bot for poops and giggles");
            info.setFooter("Created by people", event.getMember().getUser().getAvatarUrl());

            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage(info.build()).queue();
            info.clear();
        }

        if (args[0].equalsIgnoreCase(Main.prefix + "amoogus")) {
            event.getChannel().sendTyping().queue();
            event.getChannel().sendMessage("😳").queue();
        }


        if (args[0].equalsIgnoreCase(Main.prefix + "pfp")) {
            String name = event.getMember().getNickname();
            String img = event.getMessage().getMentionedMembers().get(0).getUser().getAvatarUrl();
            if (name == null) {
                String name1 = event.getMessage().getMentionedMembers().get(0).getEffectiveName();
                event.getChannel().sendMessage(  name1 + "'s pfp").queue();
            } else {
                event.getChannel().sendMessage(name + "'s pfp").queue();
            }
            event.getChannel().sendMessage(img).queue();
        }

        if (args[0].equalsIgnoreCase(Main.prefix + "datejoined")) {
//            String date = "2018-12-30T06:00:00Z";
//            OffsetDateTime dt = OffsetDateTime.parse(date);
//            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
//            System.out.println(fmt.format(dt));

            String name = event.getMessage().getMentionedMembers().get(0).getEffectiveName();
            String time = event.getMessage().getMentionedMembers().get(0).getTimeJoined().toString();
            event.getChannel().sendMessage(name + " joined on " + time).queue();
        }

        if (args[0].equalsIgnoreCase(Main.prefix + "owner")) {
            int size = event.getChannel().getMembers().size();
            String name = "This code doesnt work";

            for (int i = 0; i < size; i++) {
                System.out.println(event.getChannel().getMembers().get(i).getEffectiveName());
                if (event.getChannel().getMembers().get(i).isOwner()) {
                     name = event.getChannel().getMembers().get(i).getEffectiveName();
                } else {
                    name = "Eric";
                }

            }
            event.getChannel().sendMessage(name + " is the owner").queue();
        }


        if (args[0].equalsIgnoreCase(Main.prefix + "help")) {
            String random = "%amoogus - epic yes \n";
            String profile = "%register - registers your profile into the system \n" +
                    "%profile - opens your profile \n" +
                    "%desc [whatever you want] - change your description (without brackets) \n" +
                    "%image [image url] - change your profile image (without brackets) \n";
            String game = "%game - game selector \n" +
                    "%guess (heads or tails) - puts in your guess for the game \n";
            EmbedBuilder info = new EmbedBuilder();
            info.setTitle("Help Page");
            info.setDescription("Bot Commands That Can Be Used");
            info.addField("Random Commands", random, false);
            info.addField("Profile Commands", profile, false);
            info.addField("Game Commands", game, false);
            event.getChannel().sendMessage(info.build()).queue();
            info.clear();
        }


    }
}
