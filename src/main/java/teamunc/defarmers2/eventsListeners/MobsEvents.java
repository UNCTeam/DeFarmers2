package teamunc.defarmers2.eventsListeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.scoreboard.Team;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.managers.CustomMobsManager;
import teamunc.defarmers2.managers.GameManager;
import teamunc.defarmers2.managers.TeamManager;
import teamunc.defarmers2.mobs.DeFarmersEntityType;
import teamunc.defarmers2.serializables.GameStates;

import java.util.ArrayList;
import java.util.UUID;

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
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        if (!this.plugin.getGameManager().getGameStates().getState().isInGamePhase()) {
            return;
        }
        if (!(e.getEntity() instanceof Mob) || (e.getFinalDamage() < ((Mob) e.getEntity()).getHealth())) {
            return;
        }

        Entity damager = e.getDamager();

        TeamManager teamManager = this.plugin.getGameManager().getTeamManager();
        GameManager gameManager = this.plugin.getGameManager();

        for (Team team : teamManager.getTeams()) {
            ArrayList<UUID> uuids = new ArrayList<>(teamManager.getMobsSpawnedOfTeam(team.getName()));
            for (UUID uuid : uuids) {
                if (damager.getUniqueId().equals(uuid)) {
                    int score = gameManager.getGameOptions().getCustomMobPrice(DeFarmersEntityType.getByName(e.getEntity().getType().name()));
                    teamManager.addTeamScore(team.getName(), score);
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onEnderTeleport(EntityTeleportEvent event) {
        if (!this.plugin.getGameManager().getGameStates().getState().isInGamePhase()) {
            return;
        }

        event.setCancelled(true);
    }
}
