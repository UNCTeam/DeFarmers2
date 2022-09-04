package teamunc.defarmers2;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import teamunc.defarmers2.commands.GameCommands;
import teamunc.defarmers2.commands.TeamCommands;
import teamunc.defarmers2.customsItems.ui_menu_Items.EffectChooserInventory;
import teamunc.defarmers2.customsItems.ui_menu_Items.ShopInventory;
import teamunc.defarmers2.customsItems.ui_menu_Items.TeamChooserInventory;
import teamunc.defarmers2.eventsListeners.ItemsEvents;
import teamunc.defarmers2.eventsListeners.MobsEvents;
import teamunc.defarmers2.eventsListeners.PlayersEvents;
import teamunc.defarmers2.managers.GameManager;
import teamunc.defarmers2.managers.Manager;
import teamunc.defarmers2.serializables.GameOptions;

public final class Defarmers2 extends JavaPlugin {

    // SINGLETON
    private static Defarmers2 instance = null;
    public static Defarmers2 getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // SINGLETON INIT
        instance = this;

        // init config
        GameOptions.getInstance().initConfig();

        // enable all managers
        Manager.enableAll();

        // register commands
        registerCommands();

        // register listeners
        registerListeners();
    }

    private void registerListeners() {
        this.getServer().getPluginManager().registerEvents(new PlayersEvents(this), this);
        this.getServer().getPluginManager().registerEvents(new ItemsEvents(this), this);
        this.getServer().getPluginManager().registerEvents(ShopInventory.getInstance(), this);
        this.getServer().getPluginManager().registerEvents(TeamChooserInventory.getInstance(), this);
        this.getServer().getPluginManager().registerEvents(EffectChooserInventory.getInstance(), this);
        this.getServer().getPluginManager().registerEvents(new MobsEvents(this), this);
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

    public GameManager getGameManager() {
        return GameManager.getInstance();
    }
}
