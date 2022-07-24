package teamunc.defarmers2.tickloops;

import teamunc.defarmers2.serializables.GameStates;

/**
 * Phase d'action des armées
 */
public class TickPhase3 extends AbstractTickLoop {

    public TickPhase3() {
        super(GameStates.GameState.PHASE3);
    }

    @Override
    public void actionsEachSecond() {
        // TODO
        System.out.println("TickPhase3 ActionsEachSecond");
    }

    @Override
    public void actionsOnEnd() {
        // TODO
        System.out.println("TickPhase3 ActionsOnEnd");

        // re setuping players
        this.plugin.getGameManager().setupPlayers(true);
    }
}
