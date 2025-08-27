package org.goros.discordServerStatus.discord.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.goros.discordServerStatus.DiscordServerStatus;
import org.goros.discordServerStatus.discord.commands.interfaces.ICommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class SetActivityStatus implements ICommand {
    private String activityStatus;

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getHelp() {
        return "Sets the bot's online status. \nUsage: `!set activity <playing|watching|listening>`";
    }

    @Override
    public void execute(MessageReceivedEvent event, List<String> args) {
        Member member = event.getMember();

        if(!event.isFromGuild() || member == null) {
            return;
        }

        if(!member.hasPermission(Permission.ADMINISTRATOR)) {
            event.getChannel().sendMessage("You do not have permission").queue();
            return;
        }

        if(args.size() < 2 || !args.get(0).equalsIgnoreCase("activity")) {
            event.getChannel().sendMessage("❌ Invalid Command Usage: " + getHelp()).queue();
            return;
        }

        String desiredActitivity = args.get(1);
        JDA jda = event.getJDA();

        Optional<Activity> newActivityStatus = getActivityStatusFromString(desiredActitivity);

        if (newActivityStatus.isPresent()) {
            Activity newActivity = newActivityStatus.get();
            jda.getPresence().setActivity(newActivity);
            event.getChannel().sendMessage("✅ Bot status has been updated to **" + newActivity.getType() + "**.").queue();
        } else {
            event.getChannel().sendMessage("Invalid status. Please use one of: `playing` `listening` `watching`.").queue();
        }
    }

    private Optional<Activity> getActivityStatusFromString(String activity) {
        FileConfiguration config = DiscordServerStatus.getInstance().getConfig();
        String section = config.getString("discord.activity-status");
        return switch (activity.toLowerCase()) {
            case "playing" -> Optional.of(Activity.playing(section));
            case "listening" -> Optional.of(Activity.listening(section));
            case "watching" -> Optional.of(Activity.watching(section));
            default -> Optional.empty();
        };
    }
}
