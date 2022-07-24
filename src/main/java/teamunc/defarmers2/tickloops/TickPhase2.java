package teamunc.defarmers2.tickloops;

import teamunc.defarmers2.serializables.GameStates;

/**
 * Phase d'achat des armées
 */
public class TickPhase2 extends AbstractTickLoop {

    public TickPhase2() {
        super(GameStates.GameState.PHASE2);
    }

    @Override
    public void actionsEachSecond() {
    }

    @Override
    public void actionsOnEnd() {
        // re setuping players
        this.plugin.getGameManager().setupPlayers(true);

        // give Artefact buyed to each teams
        this.plugin.getGameManager().getTeamManager().giveArtefactToTeams();
    }
}
