package teamunc.defarmers2.managers;

import teamunc.defarmers2.Defarmers2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.logging.Level;

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
        CustomItemsManager.enable();

        // sort all managers by priority (lowest first)
        managers.sort(Comparator.comparingInt(Manager::getImportance));

        for (Manager manager : managers) {
            Defarmers2.getInstance().getLogger().log(Level.INFO,"Enabling " + manager.getClass().getSimpleName() + " Importance : " + manager.getImportance());

            manager.onInit();
        }
    }

    public abstract void onInit();

    public abstract void onDisable();

    public abstract int getImportance();
}
