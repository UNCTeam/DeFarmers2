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
    }

    @Override
    public void actionsOnEnd() {

        // calculate money of each teams from players inventories
        this.plugin.getGameManager().getTeamManager().calculateMoneyOfTeams();

        // re setuping players
        this.plugin.getGameManager().setupPlayers(true);

        // giving players shop item
        this.plugin.getGameManager().getTeamManager().giveShopItem();
    }
}
