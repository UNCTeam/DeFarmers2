package teamunc.defarmers2.tickloops;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.managers.CustomMobsManager;
import teamunc.defarmers2.managers.GameAnnouncer;
import teamunc.defarmers2.managers.TeamManager;
import teamunc.defarmers2.managers.TickActionsManager;
import teamunc.defarmers2.serializables.GameStates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        TeamManager teamManager = this.plugin.getGameManager().getTeamManager();
        TickActionsManager tickActionsManager = this.plugin.getGameManager().getTickActionsManager();

        if ((tickActionsManager.getSecondLeft() % 2) == 0) {
            customMobsManager.updateMobsTargets();
        }

        //check if a team died and if the game is over
        teamManager.checkIfTeamsDied();
        if (!teamManager.isMoreThanOneTeamAlive()) {
            //annouce the winner
            GameAnnouncer.announceTitle(teamManager.getWinnerTeamIfGameOver().getName() + " a gagné !", "", 10, 20, 10);

            tickActionsManager.nextPhase();
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
