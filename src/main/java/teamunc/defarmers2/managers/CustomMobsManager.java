package teamunc.defarmers2.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.customsItems.CustomItem;
import teamunc.defarmers2.mobs.DeFarmersEntityFabric;
import teamunc.defarmers2.mobs.DeFarmersEntityType;
import teamunc.defarmers2.serializables.GameStates;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

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
        TeamManager teamManager = Defarmers2.getInstance().getGameManager().getTeamManager();
        for (Team team : teamManager.getTeams()) {
            for (String key : teamManager.getSelectedTeamsMobs(team).keySet()) {
                for (int i = 0; i < teamManager.getSelectedTeamsMobs(team).get(key); i++) {
                    entityFabric.createDeFarmersMob(DeFarmersEntityType.getByName(key), team.getName(), GameStates.GameState.PHASE3, 5);
                }
            }
        }

        // init Targets
        for (Team team : teamManager.getTeams()) {
            for (UUID uuid : teamManager.getMobsSpawnedOfTeam(team.getName())) {
                Mob mob = (Mob) Bukkit.getEntity(uuid);
                if (mob != null) {
                    entityFabric.initMob(mob, team.getName());
                } else Defarmers2.getInstance().getLogger().log(Level.WARNING,"A mob of the team " + team.getName() + " seem to not been spawned correctly");
            }
        }
    }

    // usefull for Focus custom item
    public boolean setTargetOfMobsOfATeamToAnOtherTeam(String sourceTeam, String targetedTeam) {
        boolean mobsFound = false;
        TeamManager teamManager = this.plugin.getGameManager().getTeamManager();
        ArrayList<UUID> mobsSourceTeam = teamManager.getMobsSpawnedOfTeam(sourceTeam);
        ArrayList<UUID> mobsTargetedTeam = teamManager.getMobsSpawnedOfTeam(targetedTeam);

        for (UUID mobUniqueId : mobsSourceTeam) {
            Entity entity = Bukkit.getEntity(mobUniqueId);
            if (entity instanceof Mob) {
                Mob mob = (Mob) entity;

                LivingEntity nearestMob = getNearestLivingEntity(mobsTargetedTeam, mob, sourceTeam);

                if (nearestMob != null) {
                    mob.setTarget(nearestMob);
                    mobsFound = true;
                }
            }
        }
        return mobsFound;
    }

    /**
     * if there is no mob in the list, return the nearest mob of the list of mobs in other teams
     * @param mobsTargetedTeam
     * @param mob
     * @return
     */
    @Nullable
    public LivingEntity getNearestLivingEntity(ArrayList<UUID> mobsTargetedTeam, Mob mob, String teamName) {
        TeamManager teamManager = this.plugin.getGameManager().getTeamManager();

        // if there is no mob in the list, return the nearest mob of the list of mobs in other teams
        if (mobsTargetedTeam == null) {
            mobsTargetedTeam = new ArrayList<>();
            for (Team team : teamManager.getTeams()) {
                if (!team.getName().equals(teamName)) {
                    mobsTargetedTeam.addAll(teamManager.getMobsSpawnedOfTeam(team.getName()));
                }
            }
        }

        // get nearest mob of targeted team
        double distance = Double.MAX_VALUE;
        LivingEntity nearestMob = null;
        for (UUID mobUniqueIdTargetedTeam : mobsTargetedTeam) {
            Entity entityTargetedTeam = Bukkit.getEntity(mobUniqueIdTargetedTeam);
            if (entityTargetedTeam instanceof Mob) {
                Mob mobTargetedTeam = (Mob) entityTargetedTeam;
                double distanceToTargetedTeam = mob.getLocation().distance(mobTargetedTeam.getLocation());
                if (distanceToTargetedTeam < distance) {
                    distance = distanceToTargetedTeam;
                    nearestMob = mobTargetedTeam;
                }
            }
        }
        return nearestMob;
    }

    public void setNewTarget(String name, Mob mob) {
        LivingEntity nearestMob = getNearestLivingEntity(null, mob, name);
        if (nearestMob != null) {
            mob.setTarget(nearestMob);
        }
    }
}
