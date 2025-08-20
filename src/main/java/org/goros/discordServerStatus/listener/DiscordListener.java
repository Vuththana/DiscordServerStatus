package org.goros.discordServerStatus.listener;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.goros.discordServerStatus.discord.commands.IpCommand;

import java.awt.*;


public class DiscordListener extends ListenerAdapter {

    // We will get this from getConfig()
    private final String channelId;
    private final String prefix;
    private final IpCommand ipCommand;

    public DiscordListener(String channelId, String prefix, String serverName, String ipAddressJava, String ipAddressBedrock) {
        this.channelId = channelId;
        this.prefix = prefix;
        this.ipCommand = new IpCommand(serverName, ipAddressJava, ipAddressBedrock);
    }
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || !event.getChannel().getId().equals(channelId)) {
            return;
        }
        String message = event.getMessage().getContentDisplay();


        // Handle the ip command
        if (message.startsWith(prefix + "ip")) {
            ipCommand.handleIpCommand(event);
        }
    }
}
