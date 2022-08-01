package teamunc.defarmers2.tickloops;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.entity.Mob;
import org.bukkit.scoreboard.Team;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.managers.CustomMobsManager;
import teamunc.defarmers2.managers.TeamManager;
import teamunc.defarmers2.serializables.GameStates;

import java.util.UUID;

/**
 * Phase d'action des armées
 */
public class TickPhase3 extends AbstractTickLoop {

    public TickPhase3() {
        super(GameStates.GameState.PHASE3);
    }

    @Override
    public void actionsEachSecond() {
        CustomMobsManager customMobsManager = this.plugin.getGameManager().getCustomMobsManager();
        TeamManager teamManager = this.gameManager.getTeamManager();

        if (this.plugin.getGameManager().getTickActionsManager().getSecondLeft() % 2 == 0) {
            for (Team team : teamManager.getTeams()) {
                for (UUID uuid : teamManager.getMobsSpawnedOfTeam(team.getName())) {
                    Mob mob = (Mob) Bukkit.getEntity(uuid);
                    if (mob != null) {
                        customMobsManager.setNewTarget(team.getName(), mob);
                    }
                }

                teamManager.getDeadTeamScoreIfTeamIsDead(team.getName());
            }
        }
    }

    @Override
    public void actionsOnEnd() {

        // re setuping players
        this.plugin.getGameManager().setupPlayers(true);

        // clearing mobs and setting peaceful difficulty
        Bukkit.getWorlds().get(0).setDifficulty(Difficulty.PEACEFUL);
    }
}
