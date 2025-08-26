package org.goros.discordServerStatus.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.goros.discordServerStatus.discord.commands.interfaces.ICommand;
import org.goros.discordServerStatus.utils.EmbedConfigLoader;
import org.goros.discordServerStatus.utils.EmbedFactory;

import java.awt.*;
import java.util.List;

public class IpCommand implements ICommand {
    @Override
    public String getName() {
        return "ip";
    }

    @Override
    public String getHelp() {
        return "Shows the Minecraft server IP embed.";
    }

    @Override
    public void execute(MessageReceivedEvent event, List<String> args) {
        try {
            ConfigurationSection config = EmbedConfigLoader.load("IP");

            MessageEmbed embed = EmbedFactory.createEmbed(config, null, null);

            event.getChannel().sendMessageEmbeds(embed).queue();
        } catch (Exception e) {
            e.printStackTrace();
            event.getChannel().sendMessage("❌ Could not load the IP embed. Check Embed/IP.yml.").queue();
        }
    }
}
