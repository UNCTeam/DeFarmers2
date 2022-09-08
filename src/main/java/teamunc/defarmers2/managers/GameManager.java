package teamunc.defarmers2.managers;

import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.serializables.GameOptions;
import teamunc.defarmers2.serializables.GameStates;
import teamunc.defarmers2.serializables.SavedGame;
import teamunc.defarmers2.utils.minigame.MiniGame;
import teamunc.defarmers2.utils.scoreboards.InGameInfoScoreboard;
import teamunc.defarmers2.utils.worldEdit.ApiWorldEdit;
import teamunc.defarmers2.utils.worldEdit.DoubleValue;
import teamunc.defarmers2.utils.worldEdit.InGameItemsList;
import teamunc.defarmers2.utils.worldEdit.MathsUtils;

import java.util.*;

public class GameManager extends Manager {

    // States
    private GameStates gameStates;
    private MiniGame miniGame;
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

        // init overworld spawn point
        Bukkit.getWorlds().get(0).setSpawnLocation(this.gameOptions.getLobbyLocation());

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
            this.gameStates.setItemsList(new InGameItemsList(new HashMap<>(Map.of("STONE", new DoubleValue(1, 5)))));
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
        } else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 1000000, 20));
                player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1000000, 20));
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

        this.removeOfflinePlayers();

        //check if teams are empty
        this.getTeamManager().removeEmptyTeams();

        // setting difficulty
        Bukkit.getWorlds().get(0).setDifficulty(Difficulty.HARD);
        Bukkit.getWorlds().get(0).setGameRule(GameRule.DO_MOB_LOOT, true);
        Bukkit.getWorlds().get(0).setGameRule(GameRule.DO_TILE_DROPS, true);
        Bukkit.getWorlds().get(0).setGameRule(GameRule.DO_ENTITY_DROPS, true);
        Bukkit.getWorlds().get(0).setGameRule(GameRule.DO_MOB_SPAWNING, true);

        // setting up phase 1 area
        ApiWorldEdit.managePhase1Area(this.getTeamManager().getTeamSpawns(GameStates.GameState.PHASE1), true);

        // setting up phase 2 area
        ApiWorldEdit.managePhase2Area(this.getTeamManager().getTeamSpawns(GameStates.GameState.PHASE2), true);

        // setting up phase 3 area
        ApiWorldEdit.managePhase3Area(new Location[]{this.gameOptions.getPhase3LocationCenter()}, true);

        setupPlayers(false, false, false, true);

        // apply resistance to all player
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 60, 10));
        }

        // teleport players to phase 1
        teleportPlayers(GameStates.GameState.PHASE1);

        // spawn cows in phase 1
        this.spawnMob(GameStates.GameState.PHASE1,this.gameOptions.getMobCountAtSpawn(), EntityType.COW);

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
        GameAnnouncer.sendMessageToAllOnlinePlayer(Bukkit.getOnlinePlayers(),ChatColor.GOLD + "--- Items list: ---");
        for (String item : this.gameStates.getItemsList().getItemsListWithPrice().keySet()) {
            GameAnnouncer.sendMessageToAllOnlinePlayer(Bukkit.getOnlinePlayers(),"§a" + item + ": "+ ChatColor.GOLD + this.gameStates.getItemsList().getItemsListWithPrice().get(item));
        }

        // start tick loop
        this.eachSecondsTimerID = scheduler.scheduleSyncRepeatingTask(this.plugin, () -> getTickActionsManager().onTick(),  0L, 20L);
    }

    private void spawnMob(GameStates.GameState phase, int mobCountAtSpawn, EntityType entityType) {
        for (Location location : this.getTeamManager().getTeamSpawns(phase)) {
            for (int i = 0; i < mobCountAtSpawn; i++) {
                Mob mob = (Mob) location.getWorld().spawnEntity(location, entityType);
                mob.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40, 20));
            }
        }
    }

    public void stopTickLoop() {
        scheduler.cancelTask(this.eachSecondsTimerID);
    }

    public void stopGame() {
        try {
            // saving stats
            this.getFileManager().saveJson(
                    "SavedGame_" + UUID.randomUUID(),
                    new SavedGame(gameStates, this.getTeamManager().getTeamStates())
            );

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
            this.setupPlayers(true, false, false, false);

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
    public void setupPlayers(boolean invulnerable, boolean flying, boolean nightvision, boolean giveFood) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (this.getTeamManager().getPlayersInTeams().containsKey(player.getName())) {
                player.setFoodLevel(20);
                player.setHealth(20);
                player.setFireTicks(0);
                player.setInvulnerable(false);
                player.setGameMode(GameMode.SURVIVAL);
                if (nightvision) player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 999999, 1));
                else player.removePotionEffect(PotionEffectType.NIGHT_VISION);

                if (invulnerable) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 1000000, 20));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1000000, 20));
                    if (gameStates.getState() == GameStates.GameState.PHASE3) {
                        player.setInvulnerable(true);
                    }
                } else {
                    player.removePotionEffect(PotionEffectType.SATURATION);
                    player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                }
                player.setAllowFlight(flying);
                player.setFlying(flying);
                player.setExp(0);
                player.setLevel(0);
                player.getInventory().clear();

                if (giveFood)
                    player.getInventory().addItem(new ItemStack(Material.COOKED_BEEF, 64));
            }
        }
    }

    public void removeOfflinePlayers() {
        for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            if (!player.isOnline() && this.getTeamManager().getPlayersInTeams().containsKey(player.getName())) {
                this.getTeamManager().leaveTeam(player.getName(),this.getTeamManager().getTeamOfPlayer(player.getName()).getName());
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

    public boolean joinMiniGame(Player player) {
        if (this.miniGame == null) this.miniGame = new MiniGame();

        if (this.gameStates.getState() == GameStates.GameState.WAITING_FOR_PLAYERS && !this.miniGame.isGameRunning()) {
            miniGame.addPlayer(player);
            GameAnnouncer.sendMessageToAllOnlinePlayer(Bukkit.getOnlinePlayers(), "§a" + player.getName() + " a rejoint le mini-jeu ! /minigame pour rejoindre");

            miniGame.tryToStart();

            return true;
        } else {
            return false;
        }
    }

    public boolean isMiniGameRunning() {
        return this.miniGame != null && this.miniGame.isGameRunning();
    }

    public MiniGame getMiniGame() {
        return this.miniGame;
    }

    public void endMiniGame() {
        this.miniGame = null;
    }

    public void forceEndMiniGame() {
        GameAnnouncer.sendMessageToAllOnlinePlayer(Bukkit.getOnlinePlayers(), "§cLe mini-jeu a été forcé à s'arrêter !");
        for (UUID uuid : miniGame.getPlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.teleport(this.getGameOptions().getLobbyLocation().clone().add(0, 1, 0));
            }
            player.getInventory().clear();
        }
        this.miniGame = null;
    }

    public long getSeedOrGenerate() {
        if (this.gameStates.getActualSeed() == -1)
            this.gameStates.setActualSeed(new Random().nextLong());
        return this.gameStates.getActualSeed();
    }

    public void setSeed(Long aLong) {
        this.gameStates.setActualSeed(aLong);
    }
}
