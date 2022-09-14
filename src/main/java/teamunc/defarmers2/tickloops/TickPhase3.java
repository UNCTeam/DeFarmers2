package teamunc.defarmers2.tickloops;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.scoreboard.Team;
import teamunc.defarmers2.managers.CustomMobsManager;
import teamunc.defarmers2.managers.GameAnnouncer;
import teamunc.defarmers2.managers.TeamManager;
import teamunc.defarmers2.managers.TickActionsManager;
import teamunc.defarmers2.serializables.GameStates;

import java.util.ArrayList;
import java.util.HashMap;

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

        // effects on mobs
        teamManager.mobEffectsActions();

        if ((tickActionsManager.getSecondLeft() % 2) == 0) {
            customMobsManager.updateMobsInformations();
        }

        // update teams death
        teamManager.setIfTeamsDied();

        //check if a team died and if the game is over
        if (teamManager.isOneTeamOrLessLeft()) {
            tickActionsManager.nextPhase();
        }
    }

    @Override
    public void actionsOnEnd() {
        TeamManager teamManager = this.plugin.getGameManager().getTeamManager();
        HashMap<Integer,ArrayList<Team>> teamClassement = teamManager.getClassement();

        // announce winner and check if there is a draw
        if (teamClassement.get(1).size() == 1) {
            GameAnnouncer.announceWinner(teamClassement.get(1).get(0));
        } else {
            GameAnnouncer.announceDraw(teamClassement.get(1));
        }

        // annonce classement and scores
        GameAnnouncer.announceClassement(teamClassement);

        // re setuping players
        this.plugin.getGameManager().setupPlayers(true, false, false, false);

        // clearing mobs and setting peaceful difficulty
        this.plugin.getGameManager().getCustomMobsManager().clearMobs();

        Bukkit.getWorlds().forEach(world -> world.setDifficulty(Difficulty.PEACEFUL));


    }
}
