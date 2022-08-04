package teamunc.defarmers2.eventsListeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.managers.GameManager;
import teamunc.defarmers2.managers.TeamManager;
import teamunc.defarmers2.serializables.GameStates;

public class PlayersEvents extends AbstractListener {

    public PlayersEvents(Defarmers2 plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        GameManager gameManager = this.plugin.getGameManager();

        if (gameManager.getGameStates().getState() != GameStates.GameState.WAITING_FOR_PLAYERS
        && gameManager.getTeamManager().getPlayersInTeams().containsKey(player.getName())) {
            gameManager.resetInGameScoreboard(player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        GameManager gameManager = this.plugin.getGameManager();
        if (gameManager.getGameStates().getState() != GameStates.GameState.WAITING_FOR_PLAYERS
        && gameManager.getTeamManager().getPlayersInTeams().containsKey(player.getName())) {
            gameManager.deleteInGameScoreboard(player);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        GameManager gameManager = this.plugin.getGameManager();
        TeamManager teamManager = gameManager.getTeamManager();
        if (!gameManager.getGameStates().getState().isInGamePhase()
        && teamManager.getPlayersInTeams().containsKey(player.getName())) {
            for (Team team : teamManager.getTeams()) {
                if (team.hasEntry(player.getName())) {
                    Location respawnLocation = teamManager.getTeamSpawnLocation(team.getName(), gameManager.getGameStates().getState());
                    System.out.println("player : " + player.getName() + " is in team : " + team.getName() + " respawning at " + respawnLocation.clone().subtract(0, 3, 0));
                    event.setRespawnLocation(respawnLocation);
                    break;
                }
            }
        }
    }
}
