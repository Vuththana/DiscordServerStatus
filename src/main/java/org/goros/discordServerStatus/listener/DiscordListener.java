package org.goros.discordServerStatus.listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.goros.discordServerStatus.discord.commands.IpCommand;
import org.goros.discordServerStatus.discord.commands.manager.CommandManager;
import org.jetbrains.annotations.Nullable;

import java.awt.*;


public class DiscordListener extends ListenerAdapter {

    // We will get this from getConfig()
    private final String channelId;
    private final String prefix;
    private CommandManager commandManager;

    public DiscordListener(String channelId, String prefix) {
        this.channelId = channelId;
        this.prefix = prefix;
        this.commandManager = new CommandManager();
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        commandManager.handle(event, prefix);

        String message = event.getMessage().getContentDisplay();

        // Help
        if(message.startsWith(prefix + "help")) {
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle("List of all help commands:");
            embed.setColor(Color.GREEN);
            embed.addField("!ip",  "to display the ip address of the server.", false);
            embed.addField("!set status <online|dnd|idle|invisible>",  "to set online status of the bot", false);
            event.getChannel().sendMessageEmbeds(embed.build()).queue();
        }

        // Handle other strict commands only in the allowed channel
        if (!event.getChannel().getId().equals(channelId)) {
            return;
        }

    }
}
