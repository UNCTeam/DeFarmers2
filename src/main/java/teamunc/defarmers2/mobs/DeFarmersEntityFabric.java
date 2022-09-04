package teamunc.defarmers2.mobs;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
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
        randomLocation.subtract(0, 3, 0);
        try {
            switch (type) {
                case ZOMBIE:
                    mob = location.getWorld().spawn(randomLocation, org.bukkit.entity.Zombie.class);
                    ((Zombie) mob).setAdult();
                    mob.getEquipment().clear();
                    break;
                case SKELETON:
                    mob = location.getWorld().spawn(randomLocation, org.bukkit.entity.Skeleton.class);
                    mob.getEquipment().setChestplate(null);
                    mob.getEquipment().setLeggings(null);
                    mob.getEquipment().setHelmet(null);
                    mob.getEquipment().setBoots(null);
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
                case ZOMBIFIED_PIGLIN:
                    mob = location.getWorld().spawn(randomLocation, org.bukkit.entity.PigZombie.class);
                    mob.getEquipment().setChestplate(null);
                    mob.getEquipment().setLeggings(null);
                    mob.getEquipment().setHelmet(null);
                    mob.getEquipment().setBoots(null);
                    break;
                case WOLF:
                    mob = location.getWorld().spawn(randomLocation, org.bukkit.entity.Wolf.class);
                    ((Wolf) mob).setCollarColor(DyeColor.valueOf(teamName));
                    mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(mob.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue() * 2);
                    break;
                case EVOKER:
                    mob = location.getWorld().spawn(randomLocation, org.bukkit.entity.Evoker.class);
                    break;
                case FOX:
                    mob = location.getWorld().spawn(randomLocation, org.bukkit.entity.Fox.class);
                    break;
                case CAT:
                    mob = location.getWorld().spawn(randomLocation, org.bukkit.entity.Cat.class);
                    break;
                case RAVAGER:
                    mob = location.getWorld().spawn(randomLocation, org.bukkit.entity.Ravager.class);
                    break;
                case POLAR_BEAR:
                    mob = location.getWorld().spawn(randomLocation, org.bukkit.entity.PolarBear.class);
                    break;
                case ENDERMITE:
                    mob = location.getWorld().spawn(randomLocation, org.bukkit.entity.Endermite.class);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // add it to the spawned Mobs team
        // aply effects
        if (mob != null) {
            mob.setCustomName(ChatColor.valueOf(teamName) + "[" + teamName + "] " + "❤❤❤❤❤❤❤❤❤❤");
            mob.getActivePotionEffects().clear();
            mob.setCustomNameVisible(true);
            mob.setPersistent(true);
            Defarmers2.getInstance().getGameManager().getTeamManager().addMobsSpawnedOfTeam(teamName, mob.getUniqueId());
        }

        return mob;
    }
}

