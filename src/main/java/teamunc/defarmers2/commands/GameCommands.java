package teamunc.defarmers2.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import teamunc.defarmers2.Defarmers2;

public class GameCommands extends AbstractCommandExecutor {

    public GameCommands(Defarmers2 plugin) {
        super(plugin);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("defarmers2")) {
            if (args.length == 0) {
                plugin.sendMessage(sender,"Defarmers2 Commands:");
                plugin.sendMessage(sender,"/defarmers2");
                plugin.sendMessage(sender,"/defarmers2 <start>");
                plugin.sendMessage(sender,"/defarmers2 <stop>");
                plugin.sendMessage(sender,"/defarmers2 <reload>");
                plugin.sendMessage(sender,"/defarmers2 <test> ()");
            } else {
                if (args[0].equalsIgnoreCase("start")) {
                    plugin.sendMessage(sender,"Starting game...");
                    plugin.getGameManager().startGame();
                } else if (args[0].equalsIgnoreCase("stop")) {
                    plugin.sendMessage(sender,"Stopping game...");
                    plugin.getGameManager().stopGame();
                } else if (args[0].equalsIgnoreCase("reload")) {
                    plugin.sendMessage(sender,"Reloading game...");
                    plugin.getGameManager().reloadGame();
                } else {
                    plugin.sendMessage(sender,"Invalid command.");
                }
            }
        }
        return true;
    }
}
