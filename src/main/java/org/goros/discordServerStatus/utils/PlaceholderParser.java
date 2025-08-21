package org.goros.discordServerStatus.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;


public class PlaceholderParser {

    /**
     * Checks if PlaceholderAPI is enabled on the server.
     * @return true if PAPI is enabled, false otherwise.
     */
    public static boolean isPAPIEnabled() {
        return Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI");
    }

    /**
     * Parses all PlaceholderAPI placeholders in a given string.
     * If PAPI is not enabled, it returns the original string.
     *
     * @param player The player to parse the placeholders relative to. Can be null for server-wide placeholders.
     * @param text The string containing placeholders.
     * @return The string with PAPI placeholders replaced.
     */
    public static String parse(@Nullable Player player, String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        // If PAPI is installed, ask it to translate the placeholders
        if (isPAPIEnabled()) {
            return PlaceholderAPI.setPlaceholders(player, text);
        }

        // If PAPI is not installed, just return the original text
        return text;
    }
}