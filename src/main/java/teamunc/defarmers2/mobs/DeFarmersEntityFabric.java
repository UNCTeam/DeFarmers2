package teamunc.defarmers2.mobs;

import org.bukkit.entity.Mob;

public class DeFarmersEntityFabric {

    // SINGLETON
    private static DeFarmersEntityFabric instance = new DeFarmersEntityFabric();

    // CONSTRUCTOR
    private DeFarmersEntityFabric() {
    }

    // METHODS
    public static DeFarmersEntityFabric getInstance() {
        return instance;
    }

    public Mob createDeFarmersMob(DeFarmersEntityType type) {
        return null;
    }
}

