package teamunc.defarmers2.tickloops;

import teamunc.defarmers2.serializables.GameStates;

public class TickEndGame extends AbstractTickLoop {

    public TickEndGame() {
        super(GameStates.GameState.END_GAME);
    }

    @Override
    public void actionsEachSecond() {

    }

    @Override
    public void actionsOnEnd() {

    }
}
