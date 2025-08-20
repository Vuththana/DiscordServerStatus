package org.goros.discordServerStatus.discord.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopBalCommand {
    private Economy economy;
    public TopBalCommand() {
        if (!setupEconomy()) {
            // Log an error to the server console so you know it failed on startup
            Bukkit.getLogger().severe("Failed to setup Vault economy. TopBal command will not work.");
        }
    }

    private boolean setupEconomy() {
        // Check if Vault is available
        RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return economy != null;
    }

    public void handleTopBal(MessageReceivedEvent event) {
        if(economy == null) {
            event.getChannel().sendMessage("Vault economy is not initialized properly.");
            return;
        }
        try {
            // Fetch top players
            List<Map.Entry<String, Double>> topPlayers = getTopPlayers(10); // we are getting only top 10 players

            String leaderboardTitle = Bukkit.getPluginManager()
                    .getPlugin("DiscordServerStatus")
                    .getConfig()
                    .getString("leaderboard.title", "Default Leaderboard Title");
            EmbedBuilder embed = new EmbedBuilder();
            embed.setTitle(leaderboardTitle);
            embed.setColor(Color.GREEN);

            StringBuilder leaderboard = new StringBuilder();
            int rank = 1;
            for(Map.Entry<String, Double> entry: topPlayers) {
                leaderboard.append(rank)
                        .append(". ")
                        .append(entry.getKey())
                        .append(": " + "$")
                        .append(entry.getValue())
                        .append("\n");
                rank++;
            }
            embed.setDescription(leaderboard.toString());

            event.getChannel().sendMessageEmbeds(embed.build()).queue();
        } catch (Exception e) {
            event.getChannel().sendMessage("Failed to retreive or send leaderboards: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<Map.Entry<String, Double>> getTopPlayers(int limit) {
        Map<String, Double> playerBalances = new HashMap<>();
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if(economy.hasAccount(offlinePlayer)) {
                double balance = economy.getBalance(offlinePlayer);
                playerBalances.put(offlinePlayer.getName(), balance);
            }
        }
        List<Map.Entry<String, Double>> sortedPlayers = new ArrayList<>(playerBalances.entrySet());
        sortedPlayers.sort((entry1, entry2)->entry2.getValue().compareTo(entry1.getValue()));

        return sortedPlayers.subList(0, Math.min(limit, sortedPlayers.size()));
    }
}
