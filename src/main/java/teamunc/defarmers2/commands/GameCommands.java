package teamunc.defarmers2.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.persistence.PersistentDataType;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.managers.CustomMobsManager;
import teamunc.defarmers2.managers.GameAnnouncer;
import teamunc.defarmers2.mobs.DeFarmersEntityFabric;
import teamunc.defarmers2.mobs.DeFarmersEntityType;
import teamunc.defarmers2.serializables.GameStates;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class GameCommands extends AbstractCommandExecutor {

    public GameCommands(Defarmers2 plugin) {
        super(plugin);
    }

    private ArrayList<UUID> mobsSpawned = new ArrayList<>();
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
                GameAnnouncer.sendMessage(sender,"/defarmers2 <test> <(fight)>");
            } else {
                if (args[0].equalsIgnoreCase("start")) {
                    if (plugin.getGameManager().getGameStates().getState() == GameStates.GameState.WAITING_FOR_PLAYERS) {
                        GameAnnouncer.sendMessage(sender, "Starting game...");
                        plugin.getGameManager().startGame();
                    } else GameAnnouncer.sendMessage(sender,"Game already started!");
                } else if (args[0].equalsIgnoreCase("stop")) {
                    GameAnnouncer.sendMessage(sender, "Stopping game...");
                    plugin.getGameManager().stopGame();
                } else if (args[0].equalsIgnoreCase("reload")) {
                    GameAnnouncer.sendMessage(sender, "Reloading game...");
                    plugin.getGameManager().reloadGame();
                } else if (args[0].equalsIgnoreCase("nextphase")) {
                    GameAnnouncer.sendMessage(sender, "Next phase...");
                    plugin.getGameManager().nextPhase();
                } else if (args[0].equalsIgnoreCase("test")) {
                    if (args.length == 1) {
                        if (sender instanceof Player) {
                            Location loc = ((Player) sender).getLocation();
                            loc.getWorld().spawn(loc, Zombie.class, (entity) -> {
                                entity.setCustomName("Test");
                                entity.setCustomNameVisible(true);
                                this.mobsSpawned.add(entity.getUniqueId());
                            });
                        }
                    } else if (args.length == 2) {
                        ArrayList<UUID> mobs = (ArrayList<UUID>) this.mobsSpawned.clone();
                        for (UUID uuid : mobs) {
                            Mob mob = (Mob) Bukkit.getEntity(uuid);
                            if (mob != null) {
                                List<UUID> uuidsWoutThisOne = this.mobsSpawned.stream().filter(uuidTarget -> !uuidTarget.equals(uuid)).collect(Collectors.toList());
                                Collections.shuffle(uuidsWoutThisOne);
                                Mob mobTarget = (Mob) Bukkit.getEntity(uuidsWoutThisOne.get(0));
                                if (mobTarget != null) {
                                    mob.setTarget(mobTarget);
                                } else this.mobsSpawned.remove(uuidsWoutThisOne.get(0));
                            } else this.mobsSpawned.remove(uuid);
                        }
                    }
                } else {
                    GameAnnouncer.sendMessage(sender,"Invalid command.");
                }
            }
        }
        return true;
    }
}
