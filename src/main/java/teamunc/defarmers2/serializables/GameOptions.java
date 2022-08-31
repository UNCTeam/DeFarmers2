package teamunc.defarmers2.serializables;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.managers.CustomItemsManager;
import teamunc.defarmers2.managers.CustomMobsManager;
import teamunc.defarmers2.mobs.DeFarmersEntityType;


public class GameOptions {

    private FileConfiguration config = Defarmers2.getInstance().getConfig();

    // SINGLETON
    private static GameOptions instance = new GameOptions();
    public static GameOptions getInstance() {
        return instance;
    }
    private GameOptions() {
    }

    public void initConfig() {
        config.addDefault("timeForPhase1inSecond", 900);
        config.addDefault("timeForPhase2inSecond", 90);
        config.addDefault("timeForPhase3inSecond", 900);
        config.addDefault("timeForEndGameinSecond", 180);

        config.addDefault("lobbyLocation", new Location(Bukkit.getWorlds().get(0), 0, 150, 0));

        config.addDefault("phase1LocationCenter", new Location(Bukkit.getWorlds().get(0), 0, 300, 0));
        config.addDefault("phase2LocationCenter", new Location(Bukkit.getWorlds().get(0), 0, 40, 0));
        config.addDefault("phase3LocationCenter", new Location(Bukkit.getWorlds().get(0), 0, 0, 0));

        config.addDefault("endGameLocationCenter", new Location(Bukkit.getWorlds().get(0), 0, 150, 0));

        config.addDefault("starting-money", 0);


        for (String type : CustomItemsManager.getAllCustomItemTypes()) {
            config.addDefault("custom-item." + type + ".price", CustomItemsManager.getCustomItem(type).getDefaultPrice());
            config.addDefault("custom-item." + type + ".enabled", true);
        }

        for (DeFarmersEntityType type : CustomMobsManager.getAllCustomMobsTypes()) {
            config.addDefault("custom-mob." + type.toString() + ".price", 10);
            config.addDefault("custom-mob." + type.toString() + ".enabled", true);
        }

        config.options().copyDefaults(true);
        Defarmers2.getInstance().saveConfig();
    }

    public int getTimeForPhase(GameStates.GameState phase) {
        switch (phase) {
            case PHASE1:
                return config.getInt("timeForPhase1inSecond");
            case PHASE2:
                return config.getInt("timeForPhase2inSecond");
            case PHASE3:
                return config.getInt("timeForPhase3inSecond");
            case END_GAME:
                return config.getInt("timeForEndGameinSecond");
            default:
                return -1;
        }
    }

    public Location getPhase1LocationCenter() {
        return config.getLocation("phase1LocationCenter");
    }

    public Location getPhase2LocationCenter() {
        return config.getLocation("phase2LocationCenter");
    }

    public Location getPhase3LocationCenter() {
        return config.getLocation("phase3LocationCenter");
    }

    public Location getLobbyLocation() {
        return config.getLocation("lobbyLocation");
    }

    public Location getEndGameLocationCenter() {
        return config.getLocation("endGameLocationCenter");
    }

    public int getStartingMoney() {
        return config.getInt("starting-money");
    }

    public boolean isCustomItemEnabled(String type) {
        return config.getBoolean("custom-item." + type + ".enabled");
    }

    public int getCustomItemPrice(String type) {
        return config.getInt("custom-item." + type + ".price");
    }

    public boolean isCustomMobEnabled(DeFarmersEntityType type) {
        return config.getBoolean("custom-mob." + type.toString() + ".enabled");
    }

    public int getCustomMobPrice(DeFarmersEntityType type) {
        return config.getInt("custom-mob." + type.toString() + ".price");
    }
}
