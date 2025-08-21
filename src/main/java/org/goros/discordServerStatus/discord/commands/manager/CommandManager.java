package org.goros.discordServerStatus.discord.commands.manager;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.goros.discordServerStatus.discord.commands.IpCommand;
import org.goros.discordServerStatus.discord.commands.SetOnlineStatusCommand;
import org.goros.discordServerStatus.discord.commands.interfaces.ICommand;

import java.util.*;
import java.util.regex.Pattern;

public class CommandManager {
    private final Map<String, ICommand> commands = new HashMap<>();

    public CommandManager() {

        addCommand(new SetOnlineStatusCommand());
        addCommand(new IpCommand());
    }

    private void addCommand(ICommand command) {
        commands.put(command.getName().toLowerCase(), command);
    }


    // handle the prefix command
    public void handle(MessageReceivedEvent event, String prefix) {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(prefix), "")
                .split("\\s+");
        String commandName = split[0].toLowerCase();

        if (commands.containsKey(commandName)) {
            List<String> args = Arrays.asList(split).subList(1, split.length);
            commands.get(commandName).execute(event, args);
        }
    }
}