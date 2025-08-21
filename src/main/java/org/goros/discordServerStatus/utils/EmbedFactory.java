package org.goros.discordServerStatus.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;


import java.awt.Color;
import java.time.Instant;
import java.util.List;
import java.util.Map;

public class EmbedFactory {
    /**
     * Replaces custom placeholders in a given string from a map.
     * @param text The string containing placeholders.
     * @param placeholders A map of placeholder keys (e.g., "%leaderboard_list%") to their values.
     * @return The string with placeholders replaced.
     */
    private static String replace(String text, Map<String, String> placeholders) {
        if (text == null || placeholders == null) {
            return text;
        }
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            text = text.replace(entry.getKey(), entry.getValue());
        }
        return text;
    }



    /**
     * Creates a MessageEmbed from a configuration section and placeholders.
     * @param config The ConfigurationSection containing the embed template.
     * @param customPlaceholders A map of custom placeholders to replace (e.g., for lists).
     * @param player The player context for PlaceholderAPI. Can be null.
     * @return The built MessageEmbed.
     */
    public static MessageEmbed createEmbed(ConfigurationSection config, Map<String, String> customPlaceholders, @Nullable Player player) {
        if (config == null) {
            // Return a default error embed if the config section is missing
            return new EmbedBuilder()
                    .setTitle("Embed Error")
                    .setDescription("The embed template is missing in the configuration file.")
                    .setColor(Color.RED)
                    .build();
        }

        EmbedBuilder embed = new EmbedBuilder();

        // This helper function will process both custom and PAPI placeholders
        // It's a lambda function for cleaner code
        java.util.function.Function<String, String> processPlaceholders = (text) -> {
            text = replace(text, customPlaceholders);
            text = PlaceholderParser.parse(player, text);
            return text;
        };

        // Title and Description
        embed.setTitle(processPlaceholders.apply(config.getString("title")));
        embed.setDescription(processPlaceholders.apply(config.getString("description")));

        // Color (handles both HEX strings and decimal integers)
        if (config.isString("color")) {
            try {
                embed.setColor(Color.decode(config.getString("color")));
            } catch (NumberFormatException e) {
                embed.setColor(Color.GREEN); // Default color on error
            }
        } else if (config.isInt("color")) {
            embed.setColor(config.getInt("color"));
        }

        // Author
        if (config.isConfigurationSection("author")) {
            ConfigurationSection authorConfig = config.getConfigurationSection("author");
            if(authorConfig.getBoolean("enable", true)) {
                String name = processPlaceholders.apply(authorConfig.getString("name"));
                String  url = authorConfig.getString("url");
                String icon = authorConfig.getString("icon_url");

                embed.setAuthor(
                        name,
                        (url != null && !url.equalsIgnoreCase("disable")) ? processPlaceholders.apply(url): null,
                        (url != null && !url.equalsIgnoreCase("disable")) ? processPlaceholders.apply(icon): null
                        );
            }
        }

        // Fields
        if (config.isList("fields")) {
            List<Map<?, ?>> fields = config.getMapList("fields");
            for (Map<?, ?> field : fields) {
                String name = processPlaceholders.apply((String) field.get("name"));
                String value = processPlaceholders.apply((String) field.get("value"));
                boolean inline = field.containsKey("inline") && Boolean.parseBoolean(field.get("inline").toString());
                embed.addField(name, value, inline);
            }
        }

        // Footer
        if (config.isConfigurationSection("footer")) {
            ConfigurationSection footerConfig = config.getConfigurationSection("footer");
            if (footerConfig.getBoolean("enable", true)) {
                String text = processPlaceholders.apply(footerConfig.getString("text"));
                String icon = footerConfig.getString("icon_url");

                if (text != null) {
                    embed.setFooter(
                            text,
                            (icon != null && !icon.equalsIgnoreCase("enable")) ? processPlaceholders.apply(icon) : null
                    );
                }
            }
        }

        // Thumbnail and Image (Banner)
        if (config.isConfigurationSection("thumbnail")) {
            ConfigurationSection thumbConfig = config.getConfigurationSection("thumbnail");
            if (thumbConfig.getBoolean("enable", true)) {
                String url = processPlaceholders.apply(thumbConfig.getString("url"));
                if (url != null && !url.equalsIgnoreCase("disable")) {
                    embed.setThumbnail(url);
                }
            }
        }

        // Image
        if (config.isConfigurationSection("image")) {
            ConfigurationSection imageConfig = config.getConfigurationSection("image");
            if (imageConfig.getBoolean("enable", true)) {
                String url = processPlaceholders.apply(imageConfig.getString("url"));
                if (url != null && !url.equalsIgnoreCase("disable")) {
                    embed.setImage(url);
                }
            }
        }

        // Timestamp
        if (config.getBoolean("timestamp", false)) {
            embed.setTimestamp(Instant.now());
        }

        return embed.build();
    }
}