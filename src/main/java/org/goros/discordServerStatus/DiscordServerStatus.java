package org.goros.discordServerStatus;

import net.dv8tion.jda.api.JDA;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.goros.discordServerStatus.discord.BotRegistry.BotRegistry;
import org.goros.discordServerStatus.listener.DiscordListener;

import java.io.File;

public final class DiscordServerStatus extends JavaPlugin {
    private JDA jda;
    private String botToken;
    private String channelId;
    private String onlineStatus;
    private File ipFile;
    private FileConfiguration ipConfig;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        createIpConfig();
        botToken = getConfig().getString("discord.bot-token");
        channelId = getConfig().getString("discord.channel-id");
        String prefix = getConfig().getString("discord.prefix", "!");
        onlineStatus = getConfig().getString("discord.online-status");

        //Validate the config values
        if (botToken == null || botToken.isEmpty() || channelId == null || channelId.isEmpty()) {
            getLogger().severe("Bot token or channel ID is missing in the config.");
            getServer().getPluginManager().disablePlugin(this); // Disable plugin if config values are null
            return;
        }

        BotRegistry registry = new BotRegistry(botToken, onlineStatus);

        registry.startBot();
        registry.getJda().addEventListener(new DiscordListener(channelId, prefix));

    }

    @Override
    public void onDisable() {
        if (jda != null) {
            jda.shutdownNow(); // ✅ ensures all OkHttp threads stop
            jda = null;
            getLogger().info("Discord bot disconnected.");
        }
    }
    private void createIpConfig() {
        ipFile = new File(getDataFolder(), "Embed/IP.yml");
        if (!ipFile.exists()) {
            saveResource("embed/ip.yml", false); // copy from JAR
        }
        ipConfig = YamlConfiguration.loadConfiguration(ipFile);
    }

    public FileConfiguration getIpConfig() {
        return ipConfig;
    }

    public JDA getJda() {return jda;};
    public String getBotToken() { return botToken; }
    public String getChannelId() {
        return channelId;
    }

}
