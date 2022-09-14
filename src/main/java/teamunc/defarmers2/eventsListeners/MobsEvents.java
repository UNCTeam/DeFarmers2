package teamunc.defarmers2.eventsListeners;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.scoreboard.Team;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.managers.GameManager;
import teamunc.defarmers2.managers.TeamManager;
import teamunc.defarmers2.mobs.DeFarmersEntityType;

import java.util.ArrayList;
import java.util.UUID;

import static org.bukkit.event.EventPriority.HIGHEST;
import static teamunc.defarmers2.serializables.GameStates.GameState.PHASE3;

public class MobsEvents extends AbstractListener {

    public MobsEvents(Defarmers2 plugin) {
        super(plugin);
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        if (!this.plugin.getGameManager().getGameStates().getState().isInGamePhase()) {
            return;
        }

        TeamManager teamManager = this.plugin.getGameManager().getTeamManager();

        for (Team team : teamManager.getTeams()) {
            ArrayList<UUID> uuids = new ArrayList<>(teamManager.getMobsSpawnedOfTeam(team.getName()));
            for (UUID uuid : uuids) {
                if (event.getEntity().getUniqueId().equals(uuid)) {
                    teamManager.removeMobSpawnedOfTeam(team.getName(), uuid);
                    teamManager.addTeamLogToTeam(team.getName(), "MOB_DEATH " + event.getEntity().getType().name());
                    break;
                }
            }
        }
    }

    @EventHandler(priority = HIGHEST)
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (!this.plugin.getGameManager().getGameStates().getState().isInGamePhase()) {
            return;
        }
        if (!(e.getEntity() instanceof Mob) || (e.getFinalDamage() < ((Mob) e.getEntity()).getHealth())) {
            return;
        }

        Entity damager = e.getDamager();

        if (damager instanceof Projectile && ((Projectile) damager).getShooter() instanceof Mob) {
            damager = (Mob)((Projectile) damager).getShooter();
        }

        TeamManager teamManager = this.plugin.getGameManager().getTeamManager();
        GameManager gameManager = this.plugin.getGameManager();

        for (Team team : teamManager.getTeams()) {
            ArrayList<UUID> uuids = new ArrayList<>(teamManager.getMobsSpawnedOfTeam(team.getName()));
            for (UUID uuid : uuids) {
                if (damager.getUniqueId().equals(uuid)) {
                    if (uuids.contains(e.getEntity().getUniqueId())) {
                        teamManager.addTeamLogToTeam(team.getName(), "MOB_TEAM_KILL " + e.getEntity().getType().name());
                    } else {
                        int score = gameManager.getGameOptions().getCustomMobPrice(DeFarmersEntityType.getByName(e.getEntity().getType().name()));
                        teamManager.addTeamScore(team.getName(), score);
                        teamManager.addTeamLogToTeam(team.getName(), "MOB_KILL " + e.getEntity().getType().name());
                        break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEnderTeleport(EntityTeleportEvent event) {
        if (this.plugin.getGameManager().getGameStates().getState() != PHASE3) {
            return;
        }

        event.setCancelled(true);
    }
}
