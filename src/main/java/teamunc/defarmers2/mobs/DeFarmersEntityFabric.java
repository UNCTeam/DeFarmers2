package teamunc.defarmers2.mobs;

import org.bukkit.entity.Mob;
import teamunc.defarmers2.customsItems.CustomItem;

public class DeFarmersEntityFabric {

    // SINGLETON
    private static DeFarmersEntityFabric instance = new DeFarmersEntityFabric();
    private DeFarmersEntityFabric() {}
    public static DeFarmersEntityFabric getInstance() {
        return instance;
    }

    public Mob createDeFarmersMob(DeFarmersEntityType type) {
        System.out.println("Creating mob: " + type.toString());
        return null;
    }
}

