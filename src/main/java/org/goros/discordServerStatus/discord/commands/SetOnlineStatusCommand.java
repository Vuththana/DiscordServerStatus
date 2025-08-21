package org.goros.discordServerStatus.discord.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.goros.discordServerStatus.discord.commands.interfaces.ICommand;


import java.util.List;
import java.util.Optional;

public class SetOnlineStatusCommand implements ICommand {
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
            event.getChannel().sendMessage("❌ You do not have permission to use this command.").queue();
            return;
        }

        // We expect "!set status <value>", so we need at least 2 arguments.
        if (args.size() < 2 || !args.get(0).equalsIgnoreCase("status")) {
            event.getChannel().sendMessage("Invalid command usage. " + getHelp()).queue();
            return;
        }

        String desiredStatus = args.get(1);
        JDA jda = event.getJDA();

        Optional<OnlineStatus> newStatusOptional = getOnlineStatusFromString(desiredStatus);

        if(newStatusOptional.isPresent()) {
            OnlineStatus newStatus = newStatusOptional.get();
            jda.getPresence().setStatus(newStatus);
            event.getChannel().sendMessage("✅ Bot status has been updated to **" + newStatus.getKey().toUpperCase() + "**.").queue();
        } else {
            event.getChannel().sendMessage("❌ Invalid status. Please use one of: `online`, `idle`, `dnd`, `invisible`.").queue();
        }
    }

    private Optional<OnlineStatus> getOnlineStatusFromString(String status) {
        return switch (status.toLowerCase()) {
            case "online" -> Optional.of(OnlineStatus.ONLINE);
            case "idle" -> Optional.of(OnlineStatus.IDLE);
            case "dnd", "do_not_disturb" -> Optional.of(OnlineStatus.DO_NOT_DISTURB);
            case "invisible" -> Optional.of(OnlineStatus.INVISIBLE);
            default -> Optional.empty();
        };
    }
}
