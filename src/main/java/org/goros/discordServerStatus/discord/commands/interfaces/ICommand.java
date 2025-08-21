package org.goros.discordServerStatus.discord.commands.interfaces;


import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import java.util.List;

public interface ICommand {
    /**
     * Gets the name or trigger of the command.
     * @return The command name (e.g., "set").
     */
    String getName();

    /**
     * Gets the help string for the command.
     * @return A brief description of what the command does.
     */
    String getHelp();

    /**
     * The logic that executes when the command is called.
     * @param event The JDA message event.
     * @param args The command arguments (message split by spaces).
     */
    void execute(MessageReceivedEvent event, List<String> args);
}
