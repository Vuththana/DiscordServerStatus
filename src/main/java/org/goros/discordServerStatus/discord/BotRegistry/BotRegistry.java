package org.goros.discordServerStatus.discord.BotRegistry;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class BotRegistry {

    private JDA jda;
    private final String botToken;
    private final String onlineStatus;

    public BotRegistry (String botToken, String onlineStatus) {
        this.botToken = botToken;
        this.onlineStatus = onlineStatus;
    }

    private OnlineStatus getOnlineStatusFromString() {
        if(onlineStatus == null) {
            return OnlineStatus.ONLINE;
        }

        switch(onlineStatus.toLowerCase()) {
            case "idle":
                return OnlineStatus.IDLE;
            case "dnd":
            case "do_not_disturb":
                return OnlineStatus.DO_NOT_DISTURB;
            case "invisible":
                return OnlineStatus.INVISIBLE;
            default:
                return OnlineStatus.ONLINE;
        }
    }

    public void startBot() {
        try {
            jda = JDABuilder.createDefault(botToken)
                    .enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                    .setStatus(getOnlineStatusFromString())
                    .build()
                    .awaitReady();
            System.out.println("✅ Bot Started Successfully!");
        } catch(Exception e) {
            System.err.println("❌ Bot failed to start: "+ e.getMessage());
            e.printStackTrace();
        }
    }

    public JDA getJda() {
        return jda;
    }
}
