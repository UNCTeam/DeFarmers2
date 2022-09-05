package teamunc.defarmers2.eventsListeners;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.managers.GameManager;
import teamunc.defarmers2.managers.TeamManager;
import teamunc.defarmers2.serializables.GameStates;
import teamunc.defarmers2.utils.minigame.MiniGame;

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
    public void onEntityDamage(EntityDamageByEntityEvent e) {
        GameManager gameManager = this.plugin.getGameManager();

        // If the game is in phase 3, don't allow players to damage each other and mobs.
        if (e.getDamager() instanceof Player && gameManager.getGameStates().getState() == GameStates.GameState.PHASE3) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        GameManager gameManager = this.plugin.getGameManager();
        TeamManager teamManager = gameManager.getTeamManager();
        Team team = teamManager.getTeamOfPlayer(player.getName());

        if (gameManager.getGameStates().getState().isInGamePhase() && team != null) {

            Location respawnLocation = teamManager.getTeamSpawnLocation(team.getName(),gameManager.getGameStates().getState());

            event.setRespawnLocation(respawnLocation.subtract(0, 1, 0));

        } else {
            event.setRespawnLocation(player.getWorld().getSpawnLocation());
        }
    }


    /// COPS AND ROBBERS EVENTS ///

    @EventHandler
    public void onPlayerDamagingAPlayer(EntityDamageByEntityEvent e) {
        GameManager gameManager = this.plugin.getGameManager();
        if (gameManager.isMiniGameRunning()) {
            MiniGame miniGame = gameManager.getMiniGame();
            if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
                if (miniGame.isCop((Player) e.getDamager()) && miniGame.isRobber((Player) e.getEntity())) {
                    e.setCancelled(true);
                    miniGame.sendRobberToJail((Player) e.getEntity());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractWithStoneButton(PlayerInteractEvent e) {
        GameManager gameManager = this.plugin.getGameManager();
        if (gameManager.isMiniGameRunning()) {
            MiniGame miniGame = gameManager.getMiniGame();
            if (e.getClickedBlock() != null && e.getClickedBlock().getLocation().equals(miniGame.getButtonFreeJailLocation())) {
                if (miniGame.isRobber(e.getPlayer())) {
                    miniGame.freeRobbers();
                }
            }
        }
    }

}
