package teamunc.defarmers2.managers;

import org.bukkit.Bukkit;
import teamunc.defarmers2.Defarmers2;

import java.util.ArrayList;

public abstract class Manager {

    // MULTITON
    private static ArrayList<Manager> managers = new ArrayList<Manager>();
    public static void disableAll() {
        for (Manager manager : managers) {
            manager.onDisable();
        }
    }

    protected Defarmers2 plugin;
    protected Manager() {
        this.plugin = Defarmers2.getInstance();
        managers.add(this);
    }

    public static void enableAll() {
        FileManager.enable();
        GameManager.enable();
        TeamManager.enable();
        TickActionsManager.enable();

        // init all
        for (Manager manager : managers) {
            manager.onInit();
        }
    }

    public abstract void onInit();

    public abstract void onDisable();
}
