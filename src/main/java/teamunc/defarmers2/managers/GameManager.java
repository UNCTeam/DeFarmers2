package teamunc.defarmers2.managers;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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

    // Options
    private GameOptions gameOptions;

    // InGameInfoScoreboards
    private ArrayList<InGameInfoScoreboard> inGameInfoScoreboards;

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

        // load save or create if not found
        if (this.getFileManager().fileExists("gameStates")) {
            this.gameStates = this.getFileManager().loadJson("gameStates", GameStates.class);
        } else {
            this.gameStates = new GameStates();
            this.getFileManager().saveJson("gameStates",this.gameStates);
        }

        if (this.getFileManager().fileExists("gameOptions")) {
            this.gameOptions = this.getFileManager().loadJson("gameOptions", GameOptions.class);
        } else {
            this.gameOptions = new GameOptions(
                    30,
                    10,
                    30,
                    new Location(Bukkit.getWorlds().get(0), 0, 100, 0),
                    new Location(Bukkit.getWorlds().get(0),100, 100, 100),
                    new Location(Bukkit.getWorlds().get(0), 200, 100, 200),
                    new Location(Bukkit.getWorlds().get(0), 500, 100, 500)
            );

            this.getFileManager().saveJson("gameOptions",this.gameOptions);
        }

        if (this.getFileManager().fileExists("default_itemList")) {
            this.gameStates.setItemsList(this.getFileManager().loadJson("default_itemList", InGameItemsList.class));
        } else {
            this.gameStates.setItemsList(new InGameItemsList(new HashMap<>(Map.of("STONE",1))));
            this.getFileManager().saveJson("default_itemList",this.gameStates.getItemsList());
        }
    }

    @Override
    public void onDisable() {
        // save
        this.getFileManager().saveJson("gameStates",this.gameStates);
    }

    public GameStates getGameStates() {
        return gameStates;
    }


    public void startGame() {
        gameStates.setState(GameStates.GameState.PHASE1);
        gameStates.setTimeInSecondPastInThisPhase(0);

        // setting up team location
        this.getTeamManager().setupTeamSpawn();

        // setting up phase 1 area
        ApiWorldEdit.setupPhase1Area(this.getTeamManager().getTeamSpawns(GameStates.GameState.PHASE1));

        // setting up phase 2 area
        ApiWorldEdit.setupPhase2Area(this.getTeamManager().getTeamSpawns(GameStates.GameState.PHASE2));

        // setting up phase 3 area
        ApiWorldEdit.setupPhase3Area(new Location[]{this.gameOptions.getPhase3LocationCenter()});

        setupPlayers();

        // teleport players to phase 1
        teleportPlayers(GameStates.GameState.PHASE1);

        // apply inGameInfoScoreboards
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (this.getTeamManager().getPlayersInTeams().containsKey(player.getName())) {
                inGameInfoScoreboards.add(new InGameInfoScoreboard(player));
            }
        }

        // start tick loop
        this.eachSecondsTimerID = scheduler.scheduleSyncRepeatingTask(this.plugin, () -> getTickActionsManager().onTick(),  0L, 20L);
    }

    public void stopTickLoop() {
        scheduler.cancelTask(this.eachSecondsTimerID);
    }

    public void stopGame() {
        // saving stats
        this.getFileManager().saveJson("GameStats_Save_" + UUID.randomUUID(),this.getTeamManager().getTeamStates());

        // reseting gameStates
        gameStates.reset();


        // deleting scoreboards
        DeleteAllScoreboard();

        // reseting TeamManager
        this.getTeamManager().reset();

        // teleport players to lobby
        teleportPlayers(GameStates.GameState.WAITING_FOR_PLAYERS);

        // stop tick loop
        stopTickLoop();

    }

    /**
     * apply various effects to players
     */
    public void setupPlayers() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (this.getTeamManager().getPlayersInTeams().containsKey(player.getName())) {
                player.setFoodLevel(20);
                player.setHealth(20);
                player.setFireTicks(0);
                player.setGameMode(GameMode.SURVIVAL);
                player.setExp(0);
                player.setLevel(0);
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

    public void generateItemGamePrice() {

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
            default:
                return this.gameOptions.getLobbyLocation();
        }
    }

    public void teleportPlayers(GameStates.GameState state) {
        // teleportation au point de spawn exact si on est pas en jeu
        if (state == GameStates.GameState.WAITING_FOR_PLAYERS) {
            this.getTeamManager().teleportToExactSpawn(state);
        }
        // teleportation au point de spawn de chaque team si on est en jeu
        else {
            this.getTeamManager().teleportToTeamSpawn(state);
        }
    }

    public void test(CommandSender sender, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            int arg = Integer.parseInt(args[1]);

            // 100 blocks d'espace entre chaque point de spawn
            // avec 10 blocks de décalage pour chaque team
            int radius = 22*arg + 15;

            Location[] locations = MathsUtils.getCircle(player.getLocation(), radius, arg);

            ApiWorldEdit.placePlatform(locations, 50);

            ApiWorldEdit.placeBlocks(locations, Material.DIAMOND_BLOCK);
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
}
