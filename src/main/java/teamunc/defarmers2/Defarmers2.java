package teamunc.defarmers2;

import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import teamunc.defarmers2.commands.GameCommands;
import teamunc.defarmers2.commands.TeamCommands;
import teamunc.defarmers2.managers.FileManager;
import teamunc.defarmers2.managers.GameManager;
import teamunc.defarmers2.managers.Manager;

public final class Defarmers2 extends JavaPlugin {

    private final String prefix = "§8[§6Defarmers2§8]§r ";

    // SINGLETON
    private static Defarmers2 instance = null;
    public static Defarmers2 getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // SINGLETON INIT
        instance = this;

        // register commands
        registerCommands();

        // enable all managers
        Manager.enableAll();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Manager.disableAll();
    }

    public void registerCommands() {
        getCommand("defarmers2").setExecutor(new GameCommands(this));
        getCommand("team").setExecutor(new TeamCommands(this));

    }

    public void sendMessage(CommandSender player, String message) {
        player.sendMessage(prefix + message);
    }

    public void broadcastMessage(String message) {
        for (Player player : getServer().getOnlinePlayers()) {
            player.sendMessage(prefix + message);
        }
    }

    public GameManager getGameManager() {
        return GameManager.getInstance();
    }
}
