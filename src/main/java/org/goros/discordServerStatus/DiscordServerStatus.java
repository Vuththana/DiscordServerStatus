package org.goros.discordServerStatus;

import net.dv8tion.jda.api.JDA;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.goros.discordServerStatus.discord.BotRegistry.BotRegistry;
import org.goros.discordServerStatus.listener.DiscordListener;

public final class DiscordServerStatus extends JavaPlugin {
    private JDA jda;
    private String botToken;
    private String channelId;
    private String serverName;
    private String ipAddressJava;
    private String ipAddressBedrock;
    private Economy economy;

    // Setting up Econcomy
    public boolean setUpEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if(rsp == null) {
            getLogger().warning("Economy plugin not found, economy features disabled");
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        botToken = getConfig().getString("discord.bot-token");
        channelId = getConfig().getString("discord.channel-id");
        String prefix = getConfig().getString("discord.prefix", "!");
        serverName = getConfig().getString("server.server-name");
        ipAddressJava = getConfig().getString("server.ip-address-java");
        ipAddressBedrock = getConfig().getString("server.ip-address-bedrock");


        // Setup economy if Vault is present
        if(!setUpEconomy()) {
            getLogger().info("Economy features will be skipped");
        }
        //Validate the config values
        if (botToken == null || botToken.isEmpty() || channelId == null || channelId.isEmpty()) {
            getLogger().severe("Bot token or channel ID is missing in the config.");
            getServer().getPluginManager().disablePlugin(this); // Disable plugin if config values are null
            return;
        }

        BotRegistry registry = new BotRegistry(botToken);

        registry.startBot();
        registry.getJda().addEventListener(new DiscordListener(channelId, prefix, serverName, ipAddressJava, ipAddressBedrock));

    }

    @Override
    public void onDisable() {
        if(jda != null) {
            jda.shutdown();
            getLogger().info("Discord bot disconnected");
        }
    }

    public JDA getJda() {return jda;};
    public Economy economy() { return economy; };
    public String getBotToken() { return botToken; }
    public String getChannelId() {
        return channelId;
    }
    public String getServerName() { return serverName; }
    public String getIpAddressJava() { return ipAddressJava; }
    public String getIpAddressBedrock() { return ipAddressBedrock; }

}
