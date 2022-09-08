package teamunc.defarmers2.tickloops;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import teamunc.defarmers2.serializables.GameStates;

/**
 * Phase de minage = récoltage des points
 */
public class TickPhase1 extends AbstractTickLoop {

    public TickPhase1() {
        super(GameStates.GameState.PHASE1);
    }

    public void actionsEachSecond() {
    }

    @Override
    public void actionsOnEnd() {

        // calculate money of each teams from players inventories
        this.plugin.getGameManager().getTeamManager().calculateMoneyOfTeams();

        // gamerule
        Bukkit.getWorlds().get(0).setGameRule(GameRule.DO_MOB_LOOT, false);
        Bukkit.getWorlds().get(0).setGameRule(GameRule.DO_TILE_DROPS, false);
        Bukkit.getWorlds().get(0).setGameRule(GameRule.DO_ENTITY_DROPS, false);
        Bukkit.getWorlds().get(0).setGameRule(GameRule.DO_MOB_SPAWNING, false);

        // re setuping players
        this.plugin.getGameManager().setupPlayers(true,false, true, false);

        // giving players shop item
        this.plugin.getGameManager().getTeamManager().giveShopItem();
    }
}
