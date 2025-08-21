package org.goros.discordServerStatus.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

public class EmbedConfigLoader {
    public static ConfigurationSection load(String fileName) {
        File file = new File("plugins/DiscordServerStatus/Embed", fileName + ".yml");
        if (!file.exists()) {
            throw new RuntimeException("Embed config not found: " + file.getAbsolutePath());
        }
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        return yaml;
    }
}
