package teamunc.defarmers2.managers;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import teamunc.defarmers2.serializables.GameOptions;
import teamunc.defarmers2.serializables.GameStates;
import teamunc.defarmers2.utils.scoreboards.InGameInfoScoreboard;
import teamunc.defarmers2.utils.worldEdit.ApiWorldEdit;
import teamunc.defarmers2.utils.worldEdit.InGameItemsList;
import teamunc.defarmers2.utils.worldEdit.MathsUtils;

import java.util.*;

public class GameManager extends Manager {

    // States
    private GameStates gameStates;

    // InGameInfoScoreboards
    private ArrayList<InGameInfoScoreboard> inGameInfoScoreboards;

    // game options
    private GameOptions gameOptions;

    private BukkitScheduler scheduler = plugin.getServer().getScheduler();

    // SINGLETON
    private static GameManager instance;
    private int eachSecondsTimerID;

    public static GameManager getInstance() {
        return instance;
    }
    private GameManager() {
        super();
    }

    public static void enable() {
        instance = new GameManager();
    }

    @Override
    public void onInit() {
        instance.inGameInfoScoreboards = new ArrayList<>();

        // config
        instance.gameOptions = GameOptions.getInstance();

        // load save or create if not found
        if (this.getFileManager().fileExists("gameStates")) {
            this.gameStates = this.getFileManager().loadJson("gameStates", GameStates.class);
        } else {
            this.gameStates = new GameStates();
            this.getFileManager().saveJson("gameStates",this.gameStates);
        }

        if (this.getFileManager().fileExists("default_itemList")) {
            this.gameStates.setItemsList(this.getFileManager().loadJson("default_itemList", InGameItemsList.class));
        } else {
            this.gameStates.setItemsList(new InGameItemsList(new HashMap<>(Map.of("STONE",1))));
            this.getFileManager().saveJson("default_itemList",this.gameStates.getItemsList());
        }

        // reload tick loop if game is running
        if (this.gameStates.getState() != GameStates.GameState.WAITING_FOR_PLAYERS) {
            this.eachSecondsTimerID = scheduler.scheduleSyncRepeatingTask(
                    this.plugin,
                    () -> getTickActionsManager().onTick(),
                    0L,
                    20L
            );

            // reload scoreboard
            for (Player player : Bukkit.getOnlinePlayers()) {
                if(this.getTeamManager().getPlayersInTeams().containsKey(player.getName())) {
                    this.resetInGameScoreboard(player);
                }
            }
        }

    }

    @Override
    public void onDisable() {
        // save
        this.getFileManager().saveJson("gameStates",this.gameStates);
    }

    @Override
    public int getImportance() {
        return 0;
    }

    public GameStates getGameStates() {
        return gameStates;
    }


    public void startGame() {
        gameStates.setState(GameStates.GameState.PHASE1);
        gameStates.setTimeLeftInThisPhase(gameOptions.getTimeForPhase(GameStates.GameState.PHASE1));

        // setting up team location
        this.getTeamManager().setupTeams();

        // setting difficulty
        Bukkit.getWorlds().get(0).setDifficulty(Difficulty.HARD);

        // setting up phase 1 area
        ApiWorldEdit.managePhase1Area(this.getTeamManager().getTeamSpawns(GameStates.GameState.PHASE1), true);

        // setting up phase 2 area
        ApiWorldEdit.managePhase2Area(this.getTeamManager().getTeamSpawns(GameStates.GameState.PHASE2), true);

        // setting up phase 3 area
        ApiWorldEdit.managePhase3Area(new Location[]{this.gameOptions.getPhase3LocationCenter()}, true);

        setupPlayers(false);

        // apply resistance to all player
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 10));
        }

        // teleport players to phase 1
        teleportPlayers(GameStates.GameState.PHASE1);

        // little particle effect
        for (Location location : this.getTeamManager().getTeamSpawns(GameStates.GameState.PHASE1)) {
            location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location, 10);
        }

        // apply inGameInfoScoreboards
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (this.getTeamManager().getPlayersInTeams().containsKey(player.getName())) {
                inGameInfoScoreboards.add(new InGameInfoScoreboard(player));
            }
        }

        // inform players of items list and price
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(ChatColor.GOLD + "--- Items list: ---");
            for (String item : this.gameStates.getItemsList().getItemsListWithPrice().keySet()) {
                player.sendMessage("§a" + item + ": "+ ChatColor.GOLD + this.gameStates.getItemsList().getItemsListWithPrice().get(item));
            }
        }

        // start tick loop
        this.eachSecondsTimerID = scheduler.scheduleSyncRepeatingTask(this.plugin, () -> getTickActionsManager().onTick(),  0L, 20L);
    }

    public void stopTickLoop() {
        scheduler.cancelTask(this.eachSecondsTimerID);
    }

    public void stopGame() {
        try {
            // saving stats
            this.getFileManager().saveJson("TeamStates_Save_" + UUID.randomUUID(), this.getTeamManager().getTeamStates());

            // reseting gameStates
            gameStates.reset();

            // reseting inGamePlatform
            // phase 1 area
            ApiWorldEdit.managePhase1Area(this.getTeamManager().getTeamSpawns(GameStates.GameState.PHASE1), false);
            // phase 2 area
            ApiWorldEdit.managePhase2Area(this.getTeamManager().getTeamSpawns(GameStates.GameState.PHASE2), false);
            // phase 3 area
            ApiWorldEdit.managePhase3Area(new Location[]{this.gameOptions.getPhase3LocationCenter()}, false);

            // deleting scoreboards
            DeleteAllScoreboard();

            // re setuping players
            this.setupPlayers(true);

            // teleport players to lobby
            this.teleportPlayers(GameStates.GameState.WAITING_FOR_PLAYERS);

            // reseting TeamManager
            this.getTeamManager().reset();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // stop tick loop
            this.stopTickLoop();
        }
    }

    /**
     * apply various effects to players
     */
    public void setupPlayers(boolean invulnerable) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (this.getTeamManager().getPlayersInTeams().containsKey(player.getName())) {
                player.setFoodLevel(20);
                player.setHealth(20);
                player.setFireTicks(0);
                player.setGameMode(GameMode.SURVIVAL);
                player.setInvulnerable(invulnerable);
                player.setExp(0);
                player.setLevel(0);
                player.getInventory().clear();
            }
        }
    }

    public void reloadGame() {
        // stopping
        this.stopGame();

        // starting game
        startGame();
    }

    public TeamManager getTeamManager() {
        return TeamManager.getInstance();
    }

    public FileManager getFileManager() {
        return FileManager.getInstance();
    }

    public TickActionsManager getTickActionsManager() {
        return TickActionsManager.getInstance();
    }

    public GameOptions getGameOptions() {
        return this.gameOptions;
    }

    public Location getPhaseSpawn(GameStates.GameState state) {
        switch (state) {
            case PHASE1:
                return this.gameOptions.getPhase1LocationCenter();
            case PHASE2:
                return this.gameOptions.getPhase2LocationCenter();
            case PHASE3:
                return this.gameOptions.getPhase3LocationCenter();
            case END_GAME:
                return this.gameOptions.getEndGameLocationCenter();
            default:
                return this.gameOptions.getLobbyLocation();
        }
    }

    public void teleportPlayers(GameStates.GameState state) {
        // teleportation au point de spawn exact si on est pas en jeu
        if (!state.isInGamePhase()) {
            this.getTeamManager().teleportToExactSpawn(state);
        }
        // teleportation au point de spawn de chaque team si on est en jeu
        else {
            this.getTeamManager().teleportToTeamSpawn(state);
        }
    }

    public void ActualiseInGameScoreboard() {
        this.inGameInfoScoreboards.forEach(InGameInfoScoreboard::actualise);
    }

    public void DeleteAllScoreboard() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.deleteInGameScoreboard(player);
        }
    }

    public void resetInGameScoreboard(Player player) {
        deleteInGameScoreboard(player);
        inGameInfoScoreboards.add(new InGameInfoScoreboard(player));
    }

    public void deleteInGameScoreboard(Player player) {
        ArrayList<InGameInfoScoreboard> scoreboards = new ArrayList<>(this.inGameInfoScoreboards);
        for (InGameInfoScoreboard inGameInfoScoreboard : scoreboards) {
            if (inGameInfoScoreboard.getPlayer().equals(player)) {
                inGameInfoScoreboard.delete();
                inGameInfoScoreboards.remove(inGameInfoScoreboard);
            }
        }
    }

    public CustomItemsManager getCustomItemsManager() {
        return CustomItemsManager.getInstance();
    }

    public CustomMobsManager getCustomMobsManager() {
        return CustomMobsManager.getInstance();
    }

    public void nextPhase() {
        this.getTickActionsManager().nextPhase();
    }
}
