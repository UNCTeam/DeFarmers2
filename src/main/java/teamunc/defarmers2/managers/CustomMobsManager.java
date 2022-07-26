package teamunc.defarmers2.managers;

import org.bukkit.NamespacedKey;
import org.bukkit.scoreboard.Team;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.customsItems.CustomItem;
import teamunc.defarmers2.mobs.DeFarmersEntityFabric;
import teamunc.defarmers2.mobs.DeFarmersEntityType;

public class CustomMobsManager extends Manager{

    // SINGLETON
    private static CustomMobsManager instance;
    public static CustomMobsManager getInstance() {
        return instance;
    }
    private CustomMobsManager() {
        super();
    }

    public static void enable() {
        instance = new CustomMobsManager();
    }

    private DeFarmersEntityFabric entityFabric = DeFarmersEntityFabric.getInstance();
    private final NamespacedKey customMobItemKey = new NamespacedKey(Defarmers2.getInstance(),"custommobitem");


    public static DeFarmersEntityType[] getAllCustomMobsTypes() {
        return DeFarmersEntityType.values();
    }

    public NamespacedKey customMobItemKey() {
        return customMobItemKey;
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public int getImportance() {
        return 21;
    }

    public void spawnCustomMobsForEachTeam() {
        for (Team team : Defarmers2.getInstance().getGameManager().getTeamManager().getTeams()) {
            for (DeFarmersEntityType type : getAllCustomMobsTypes()) {
                    entityFabric.createDeFarmersMob(type);
            }
        }
    }
}
