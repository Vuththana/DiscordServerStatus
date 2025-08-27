package org.goros.discordServerStatus.listener;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.goros.discordServerStatus.DiscordServerStatus;
import org.goros.discordServerStatus.discord.commands.manager.CommandManager;

public class MinecraftListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        String msg = ":green_circle: " + player.getName() + " joined the server!";
        DiscordSendMessage(msg);
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String msg = ":red_circle: " + player.getName() + " left the server!";
        DiscordSendMessage(msg);
    }


    private void DiscordSendMessage(String content) {
        String channelId = DiscordServerStatus.getInstance().getConfig().getString("discord.minecraft-channel-id");
        JDA jda = DiscordServerStatus.getInstance().getJda();
        jda.getTextChannelById(channelId).sendMessage(content).queue();
    }
}
