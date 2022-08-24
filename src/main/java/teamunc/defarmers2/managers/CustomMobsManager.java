package teamunc.defarmers2.managers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Mob;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.mobs.DeFarmersEntityFabric;
import teamunc.defarmers2.mobs.DeFarmersEntityType;
import teamunc.defarmers2.serializables.GameStates;

import java.util.ArrayList;
import java.util.Arrays;
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
    }

    public void updateMobsTargets() {
        GameManager gameManager = Defarmers2.getInstance().getGameManager();
        TeamManager teamManager = gameManager.getTeamManager();
        GameStates gameStates = gameManager.getGameStates();

        for (Team team : teamManager.getTeams()) {
            ArrayList<UUID> mobsSpawnedOfTeam = (ArrayList<UUID>) teamManager.getMobsSpawnedOfTeam(team.getName()).clone();
            for (UUID uuid : mobsSpawnedOfTeam) {
                Entity NextTarget = null;
                Mob mob = (Mob) Bukkit.getEntity(uuid);

                if (mob == null) {
                    // main entity is dead
                    removeMobFromTheGame(uuid, team.getName());
                } else {
                    if (!gameStates.hasMobTargeting(uuid)) {
                        // set a new first target
                        NextTarget = getNearestLivingEntity(null, mob, team.getName());
                        gameStates.setMobTargeting(uuid, NextTarget.getUniqueId());
                    } else {
                        UUID targetedUuid = gameStates.getMobTargeting(uuid);
                        Entity targetedEntity = Bukkit.getEntity(targetedUuid);

                        if (targetedEntity != null) {
                            NextTarget = targetedEntity;
                        } else {
                            // targeted entity is dead
                            gameStates.removeMobTargeting(targetedUuid);
                            NextTarget = getNearestLivingEntity(null, mob, team.getName());
                        }
                    }
                }

                if (NextTarget != null) {
                    mob.setTarget((LivingEntity) NextTarget);
                }
            }
        }
    }

    public void removeMobFromTheGame(UUID mobUuid, String teamName) {
        GameManager gameManager = Defarmers2.getInstance().getGameManager();
        TeamManager teamManager = gameManager.getTeamManager();
        GameStates gameStates = gameManager.getGameStates();

        if (gameStates.hasMobTargeting(mobUuid))
            gameStates.removeMobTargeting(mobUuid);

        if (teamName == null) {
            for (Team team : teamManager.getTeams()) {
                if (teamManager.getMobsSpawnedOfTeam(team.getName()).contains(mobUuid)) {
                    teamName = team.getName();
                    break;
                }
            }
        }

        teamManager.removeMobSpawnedOfTeam(teamName, mobUuid);
    }

    public void focusOnATeam(Team team, Team teamCible) {
        GameManager gameManager = Defarmers2.getInstance().getGameManager();
        TeamManager teamManager = gameManager.getTeamManager();

        ArrayList<UUID> mobsCibles = teamManager.getMobsSpawnedOfTeam(teamCible.getName());

        for (UUID uuid : teamManager.getMobsSpawnedOfTeam(team.getName())) {
            Entity entity = Bukkit.getEntity(uuid);
            if (entity instanceof Mob) {
                Mob mob = (Mob) entity;
                Mob newTarget = getNearestLivingEntity(mobsCibles, mob, team.getName());
                if (newTarget != null) {
                    gameManager.getGameStates().setMobTargeting(uuid, newTarget.getUniqueId());
                }
            }
        }
    }

    /**
     * if there is no mob in the list, return the nearest mob of the list of mobs in other teams
     * @param mobsTargetedTeam
     * @param mob
     * @return
     */
    @Nullable
    public Mob getNearestLivingEntity(ArrayList<UUID> mobsTargetedTeam, Mob mob, String teamName) {
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
        Mob nearestMob = null;

        if (mobsTargetedTeam.size() > 0) {
            double distance = Double.MAX_VALUE;
            for (UUID uuid : mobsTargetedTeam) {
                Entity entity = Bukkit.getEntity(uuid);
                if (entity instanceof Mob) {
                    Mob mobTargeted = (Mob) entity;
                    double newDistance = mob.getLocation().distance(mobTargeted.getLocation());
                    if (newDistance < distance) {
                        distance = newDistance;
                        nearestMob = mobTargeted;
                    }
                }
            }
        }

        return nearestMob;
    }

    public UUID getTargetUUID(String name, Mob mob) {
        return null;
    }
}
