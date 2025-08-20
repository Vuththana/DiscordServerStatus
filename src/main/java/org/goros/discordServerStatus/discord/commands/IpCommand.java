package org.goros.discordServerStatus.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class IpCommand {
    private final String serverName;
    private final String ipAddressJava;
    private final String ipAddressBedrock;

    public IpCommand (String serverName, String ipAddressJava, String ipAddressBedrock) {
        this.serverName = serverName;
        this.ipAddressJava = ipAddressJava;
        this.ipAddressBedrock = ipAddressBedrock;
    }

    public void handleIpCommand(MessageReceivedEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(serverName + "IP Address");
        embed.setColor(Color.BLUE);
        embed.setDescription("Here are the IP addresses for both platform:");

        embed.addField("Java: ", ipAddressJava, false);
        embed.addField("Java: ", ipAddressBedrock, false);
    }
}
