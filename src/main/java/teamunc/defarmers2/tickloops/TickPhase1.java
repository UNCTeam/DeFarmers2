package teamunc.defarmers2.tickloops;

import teamunc.defarmers2.serializables.GameStates;

/**
 * Phase de minage = récoltage des points
 */
public class TickPhase1 extends AbstractTickLoop {

    public TickPhase1() {
        super(GameStates.GameState.PHASE1);
    }

    public void actionsEachSecond() {
        // TODO
        System.out.println("TickPhase1 ActionsEachSecond");
    }

    @Override
    public void actionsOnEnd() {
        // TODO
        System.out.println("TickPhase1 ActionsOnEnd");
    }
}
