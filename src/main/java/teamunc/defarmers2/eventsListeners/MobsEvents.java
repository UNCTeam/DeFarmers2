package teamunc.defarmers2.eventsListeners;

import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.scoreboard.Team;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.managers.CustomMobsManager;
import teamunc.defarmers2.managers.TeamManager;
import teamunc.defarmers2.serializables.GameStates;

import java.util.ArrayList;
import java.util.UUID;

public class MobsEvents extends AbstractListener {

    public MobsEvents(Defarmers2 plugin) {
        super(plugin);
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent event) {
        if (this.plugin.getGameManager().getGameStates().getState() == GameStates.GameState.WAITING_FOR_PLAYERS) {
            return;
        }

        TeamManager teamManager = this.plugin.getGameManager().getTeamManager();

        for (Team team : teamManager.getTeams()) {
            ArrayList<UUID> uuids = new ArrayList<>(teamManager.getMobsSpawnedOfTeam(team.getName()));
            for (UUID uuid : uuids) {
                if (event.getEntity().getUniqueId().equals(uuid)) {
                    teamManager.removeMobSpawnedOfTeam(team.getName(), uuid);
                }
            }
        }
    }
}
