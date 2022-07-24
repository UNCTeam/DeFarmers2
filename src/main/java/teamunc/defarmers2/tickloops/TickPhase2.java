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
        // TODO
        System.out.println("TickPhase2 ActionsEachSecond");
    }

    @Override
    public void actionsOnEnd() {
        // re setuping players
        this.plugin.getGameManager().setupPlayers();

        // give Artefact buyed to each teams
        this.plugin.getGameManager().getTeamManager().giveArtefactToTeams();
    }
}
