package teamunc.defarmers2.mobs;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.customsItems.CustomItem;
import teamunc.defarmers2.serializables.GameStates;
import teamunc.defarmers2.utils.worldEdit.MathsUtils;

import java.util.ArrayList;
import java.util.UUID;

public class DeFarmersEntityFabric {

    // SINGLETON
    private static DeFarmersEntityFabric instance = new DeFarmersEntityFabric();
    private DeFarmersEntityFabric() {}
    public static DeFarmersEntityFabric getInstance() {
        return instance;
    }

    public Mob createDeFarmersMob(DeFarmersEntityType type, String teamName, GameStates.GameState phase, int radius, Location location) {
        if (location == null) location = Defarmers2.getInstance().getGameManager().getTeamManager().getTeamStates().getTeamSpawnLocation(teamName, phase);
        Mob mob = null;

        if (location == null || location.getWorld() == null) {
            Defarmers2.getInstance().getLogger().warning("Location is null or world is null");
            return null;
        }

        // create a new random location within the radius
        Location randomLocation = MathsUtils.getRandomLocation(location, radius);

        try {
            switch (type) {
                case ZOMBIE:
                    mob = location.getWorld().spawn(randomLocation, org.bukkit.entity.Zombie.class);
                    break;
                case SKELETON:
                    mob = location.getWorld().spawn(randomLocation, org.bukkit.entity.Skeleton.class);
                    break;
                case SPIDER:
                    mob = location.getWorld().spawn(randomLocation, org.bukkit.entity.Spider.class);
                    break;
                case CAVE_SPIDER:
                    mob = location.getWorld().spawn(randomLocation, org.bukkit.entity.CaveSpider.class);
                    break;
                case CREEPER:
                    mob = location.getWorld().spawn(randomLocation, org.bukkit.entity.Creeper.class);
                    break;
                case ENDERMAN:
                    mob = location.getWorld().spawn(randomLocation, org.bukkit.entity.Enderman.class);
                    break;
                case GHAST:
                    mob = location.getWorld().spawn(randomLocation, org.bukkit.entity.Ghast.class);
                    break;
                case PIG_ZOMBIE:
                    mob = location.getWorld().spawn(randomLocation, org.bukkit.entity.PigZombie.class);
                    break;
                case SLIME:
                    mob = location.getWorld().spawn(randomLocation, org.bukkit.entity.Slime.class);
                    break;
                case WITCH:
                    mob = location.getWorld().spawn(randomLocation, org.bukkit.entity.Witch.class);
                    break;
                case WOLF:
                    mob = location.getWorld().spawn(randomLocation, org.bukkit.entity.Wolf.class);
                    break;
                case BLAZE:
                    mob = location.getWorld().spawn(randomLocation, org.bukkit.entity.Blaze.class);
                    break;
                case CAVE_SPIDER_JOCKEY:
                    mob = location.getWorld().spawn(randomLocation, org.bukkit.entity.CaveSpider.class);
                    mob.addPassenger(location.getWorld().spawn(randomLocation, org.bukkit.entity.Skeleton.class));
                    break;
                case ENDERMITE:
                    mob = location.getWorld().spawn(randomLocation, org.bukkit.entity.Endermite.class);
                    break;
                case MAGMA_CUBE:
                    mob = location.getWorld().spawn(randomLocation, org.bukkit.entity.MagmaCube.class);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // add it to the spawned Mobs team
        // aply effects
        if (mob != null) {
            mob.setCustomName(ChatColor.valueOf(teamName) + "[" + teamName + "] " + "❤❤❤❤❤❤❤❤❤❤");
            mob.setCustomNameVisible(true);
            mob.setPersistent(true);
            Defarmers2.getInstance().getGameManager().getTeamManager().addMobsSpawnedOfTeam(teamName, mob.getUniqueId());
        }

        return mob;
    }
}

