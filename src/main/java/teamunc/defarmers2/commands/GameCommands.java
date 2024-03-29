package teamunc.defarmers2.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.persistence.PersistentDataType;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.managers.CustomMobsManager;
import teamunc.defarmers2.managers.GameAnnouncer;
import teamunc.defarmers2.managers.GameManager;
import teamunc.defarmers2.mobs.DeFarmersEntityFabric;
import teamunc.defarmers2.mobs.DeFarmersEntityType;
import teamunc.defarmers2.serializables.GameStates;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class GameCommands extends AbstractCommandExecutor {

    public GameCommands(Defarmers2 plugin) {
        super(plugin);
    }
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("defarmers2")) {
            if (args.length == 0) {
                GameAnnouncer.sendMessage(sender,"Defarmers2 Commands:");
                GameAnnouncer.sendMessage(sender,"/defarmers2");
                GameAnnouncer.sendMessage(sender,"/defarmers2 <start>");
                GameAnnouncer.sendMessage(sender,"/defarmers2 <stop>");
                GameAnnouncer.sendMessage(sender,"/defarmers2 <reload>");
                GameAnnouncer.sendMessage(sender,"/defarmers2 <nextphase>");
                GameAnnouncer.sendMessage(sender,"/defarmers2 <deletePlayers>");
                GameAnnouncer.sendMessage(sender,"/defarmers2 <killAllMobsInGame>");
                GameAnnouncer.sendMessage(sender,"/defarmers2 <forceEndMiniGame>");
                GameAnnouncer.sendMessage(sender,"/defarmers2 <setSeed> seed");
            } else {
                if (args[0].equalsIgnoreCase("start")) {
                    if (plugin.getGameManager().getGameStates().getState() == GameStates.GameState.WAITING_FOR_PLAYERS) {
                        GameAnnouncer.sendMessage(sender, "Starting game...");
                        plugin.getGameManager().startGame();
                    } else GameAnnouncer.sendMessage(sender,"Game already started!");
                } else if (args[0].equalsIgnoreCase("stop")) {
                    if (plugin.getGameManager().getGameStates().getState() != GameStates.GameState.WAITING_FOR_PLAYERS) {
                        GameAnnouncer.sendMessage(sender, "Stopping game...");
                        plugin.getGameManager().stopGame();
                    } else GameAnnouncer.sendMessage(sender,"Game already stopped!");
                } else if (args[0].equalsIgnoreCase("reload")) {
                    GameAnnouncer.sendMessage(sender, "Reloading game...");
                    plugin.getGameManager().reloadGame();
                } else if (args[0].equalsIgnoreCase("nextphase")) {
                    if (plugin.getGameManager().getGameStates().getState() != GameStates.GameState.WAITING_FOR_PLAYERS) {
                        GameAnnouncer.sendMessage(sender, "Next phase...");
                        plugin.getGameManager().nextPhase();
                    } else GameAnnouncer.sendMessage(sender,"Game not started!");
                } else if (args[0].equalsIgnoreCase("deletePlayers")) {
                    File BaseFolder = new File(Bukkit.getServer().getWorlds().get(0).getWorldFolder(), "playerdata");

                    for(OfflinePlayer p : Bukkit.getOfflinePlayers())
                    {
                        File playerFile = new File(BaseFolder, p.getUniqueId()+".dat");

                        playerFile.delete();
                    }
                } else if (args[0].equalsIgnoreCase("killAllMobsInGame")) {
                    this.plugin.getGameManager().getCustomMobsManager().clearMobs();
                } else if (args[0].equalsIgnoreCase("forceEndMiniGame")) {
                    this.plugin.getGameManager().forceEndMiniGame();
                } else if (args[0].equalsIgnoreCase("setSeed")) {
                    if (args.length >= 2) {
                        try {
                        this.plugin.getGameManager().setSeed(Long.parseLong(args[1]));

                        GameAnnouncer.sendMessage(sender, "Seed set to " + args[1]);
                        } catch (Exception e) {
                            GameAnnouncer.sendMessage(sender, "Invalid seed!");
                        }
                    } else
                        GameAnnouncer.sendMessage(sender, "need a valid seed !");
                } else if (args[0].equalsIgnoreCase("seed")) {
                    GameAnnouncer.sendMessage(sender, "Seed is " + this.plugin.getGameManager().getSeed());

                } else {
                    GameAnnouncer.sendMessage(sender,"Invalid command.");
                }
            }
        } else if (cmd.getName().equalsIgnoreCase("minigame")) {
            GameManager gameManager = plugin.getGameManager();
            boolean joined = gameManager.joinMiniGame((Player) sender);
            if (joined) {
                GameAnnouncer.sendMessage(sender, "Joined minigame!");
            } else {
                GameAnnouncer.sendMessage(sender, "Failed to join minigame!");
            }
        } else if (cmd.getName().equalsIgnoreCase("itemsinfo")) {
            GameManager gameManager = plugin.getGameManager();
            GameAnnouncer.sendMessage(sender, ChatColor.GOLD + "--- Items list: ---");
            for (String item : gameManager.getGameStates().getItemsList().getItemsListWithPrice().keySet()) {
                GameAnnouncer.sendMessage(sender, "§a" + item + ": " + ChatColor.GOLD + gameManager.getGameStates().getItemsList().getItemsListWithPrice().get(item));
            }
        }
        return true;
    }
}
