package teamunc.defarmers2.commands;

import org.bukkit.command.CommandExecutor;
import teamunc.defarmers2.Defarmers2;

public abstract class AbstractCommandExecutor implements CommandExecutor {
    protected final Defarmers2 plugin;

    public AbstractCommandExecutor(Defarmers2 plugin) {
        this.plugin = plugin;
    }
}
