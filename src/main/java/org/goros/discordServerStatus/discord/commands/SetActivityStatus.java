package org.goros.discordServerStatus.discord.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.goros.discordServerStatus.discord.commands.interfaces.ICommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class SetActivityStatus implements ICommand {
    private static final Logger log = LoggerFactory.getLogger(SetActivityStatus.class);

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getHelp() {
        return "Sets the bot's online status. \nUsage: `!set status <online|idle|dnd|invisible>`";
    }

    @Override
    public void execute(MessageReceivedEvent event, List<String> args) {
        Member member = event.getMember();

        if(!event.isFromGuild() || member == null) {
            return;
        }

        if(!member.hasPermission(Permission.ADMINISTRATOR)) {
            return;
        }

        if(args.size() < 2 || args.get(0).equalsIgnoreCase("status")) {
            return;
        }

        String desiredActitivity = args.get(1);
        JDA jda = event.getJDA();

        Optional<Activity> newActivityStatus = getActivityStatusFromString(desiredActitivity);

        if (newActivityStatus.isPresent()) {
            Activity newActivity = newActivityStatus.get();
            jda.getPresence().setActivity(newActivity);
            event.getChannel().sendMessage("✅ Bot status has been updated to **" + newActivity.getName() + "**.").queue();
        } else {
            event.getChannel().sendMessage("❌ Invalid Command Usage").queue();
        }
    }

    private Optional<Activity> getActivityStatusFromString(String status) {
        return switch (status.toLowerCase()) {
            case "playing" -> Optional.of(Activity.playing("Something"));
            case "listening" -> Optional.of(Activity.listening("Something"));
            case "watching" -> Optional.of(Activity.watching("Something"));
            default -> Optional.empty();
        };
    }
}
