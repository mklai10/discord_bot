package Server;

import head.bot.Main;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class test extends ListenerAdapter {

    @Override
    public void onGuildMessageReceived( GuildMessageReceivedEvent event) {

        String[] args = event.getMessage().getContentRaw().split("\\s+");
        if (args[0].equalsIgnoreCase(Main.prefix + "dm")) {
            event.getAuthor().openPrivateChannel().queue((channel) -> {
                channel.sendMessage("hi").queue();
            });

            System.out.println("here");
        }
    }
}
