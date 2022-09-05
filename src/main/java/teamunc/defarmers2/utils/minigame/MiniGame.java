package teamunc.defarmers2.utils.minigame;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitScheduler;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.managers.GameAnnouncer;
import teamunc.defarmers2.managers.GameManager;

import java.util.ArrayList;
import java.util.UUID;

public class MiniGame {
    private ArrayList<UUID> players = new ArrayList<>();

    private ArrayList<UUID> cops = new ArrayList<>();
    private ArrayList<UUID> robbers = new ArrayList<>();
    private ArrayList<UUID> robbersJailed = new ArrayList<>();

    private int scheduleId = -1;
    private BukkitScheduler scheduler = Defarmers2.getInstance().getServer().getScheduler();
    private int timeLeft = 0;

    public void addPlayer(Player player) {
        players.add(player.getUniqueId());
    }

    public void tryToStart() {

        if (players.size() >= 2 && !isGameRunning() && scheduleId == -1) {
            // start the game
            timeLeft = 10;
            GameAnnouncer.sendMessageToAllOnlinePlayer(Bukkit.getOnlinePlayers(),"The mini game will start in " + timeLeft + " seconds");
            scheduleId = scheduler.scheduleSyncRepeatingTask(Defarmers2.getInstance(), () -> {
                timeLeft--;
                if (timeLeft <= 5 && timeLeft > 0) {
                    // starting the game in 5
                    GameAnnouncer.sendMessageToAllOnlinePlayer(Bukkit.getOnlinePlayers(),"The mini game will start in " + timeLeft + " seconds");
                } else if (timeLeft == 0) {
                    // starting the game
                    scheduler.cancelTask(scheduleId);
                    this.start();
                }
            }, 0, 20);
        }
    }

    private void start() {
        GameAnnouncer.sendMessageToAllOnlinePlayer(Bukkit.getOnlinePlayers(), "The mini game has started");

        // placing free robbers button at 8 153 8
        Location buttonLocation = Bukkit.getPlayer(players.get(0)).getLocation().getWorld().getBlockAt(8, 153, 8).getLocation();
        buttonLocation.getBlock().setType(Material.STONE_BUTTON);

        // assing a player as cop, other as robbers
        ArrayList<UUID> playersActual = new ArrayList<>(this.players);

        // calculate the number of cops needed (1/3 of the players)
        int copsNeeded = (int) Math.ceil(playersActual.size() / 3.0);

        for (UUID uuid : playersActual) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                if (cops.size() < copsNeeded) {
                    cops.add(uuid);
                    GameAnnouncer.sendMessage(player, "You are a cop !");

                    // give the cop the armor
                    player.getInventory().setHelmet(this.getCopHelmet());
                    player.getInventory().setChestplate(this.getCopChestplate());

                } else {
                    robbers.add(uuid);
                    GameAnnouncer.sendMessage(player, "You are a robber !");

                    // give the robber the armor
                    player.getInventory().setHelmet(this.getRobberHelmet());
                    player.getInventory().setChestplate(this.getRobberChestplate());
                }
            } else {
                players.remove(uuid);
            }
        }

        // setting the time left to 90 seconds
        timeLeft = 90;

        // game loop
        scheduleId = scheduler.scheduleSyncRepeatingTask(Defarmers2.getInstance(), this::gameLoop, 10, 20);
    }

    private void gameLoop() {
        // time left action bar
        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("Time left: " + timeLeft));
        }

        // time left
        timeLeft--;

        if (timeLeft == 20) {
            GameAnnouncer.sendMessageToAllOnlinePlayer(Bukkit.getOnlinePlayers(), "20 seconds left ! Button removed !");

            // removing the button
            Location buttonLocation = Bukkit.getPlayer(players.get(0)).getLocation().getWorld().getBlockAt(8, 153, 8).getLocation();
            buttonLocation.getBlock().setType(Material.AIR);
        }

        // end of the game
        if (!isCopsWin() && timeLeft <= 0) {
            scheduler.cancelTask(scheduleId);
            this.end(false);
        }

    }

    private void end(boolean copsWin) {
        GameAnnouncer.sendMessageToAllOnlinePlayer(Bukkit.getOnlinePlayers(), "The mini game has ended");

        if (copsWin) {
            GameAnnouncer.sendMessageToAllOnlinePlayer(Bukkit.getOnlinePlayers(), "The cops win !");
        } else {
            GameAnnouncer.sendMessageToAllOnlinePlayer(Bukkit.getOnlinePlayers(), "The robbers win !");
        }

        // free the robbers
        freeRobbers();

        // clear inventory
        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.getInventory().clear();
            }
        }

        GameManager.getInstance().endMiniGame();
    }

    public void freeRobbers() {
        for (UUID uuid : robbersJailed) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null) {
                player.teleport(player.getLocation().clone().add(0,5,0));
            }
        }
        robbersJailed.clear();
    }

    public boolean isRobber(Player player) {
        return robbers.contains(player.getUniqueId());
    }

    public boolean isCop(Player player) {
        return cops.contains(player.getUniqueId());
    }

    public void sendRobberToJail(Player player) {
        player.teleport(new Location(player.getWorld(), 8, 150, 8));
        robbersJailed.add(player.getUniqueId());
    }

    public boolean isCopsWin() {
        if (robbersJailed.size() == robbers.size()) {
            scheduler.cancelTask(scheduleId);
            this.end(true);
            return true;
        } else return false;
    }

    public boolean isGameRunning() {
        return !cops.isEmpty();
    }

    public ItemStack getCopHelmet() {
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta meta = (LeatherArmorMeta) helmet.getItemMeta();
        meta.setDisplayName("Cop helmet");
        meta.setUnbreakable(true);

        // set color
        meta.setColor(Color.fromRGB(1, 0, 0));

        helmet.setItemMeta(meta);

        return helmet;
    }
    public ItemStack getCopChestplate() {
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
        meta.setDisplayName("Cop chestplate");
        meta.setUnbreakable(true);

        // set color
        meta.setColor(Color.fromRGB(1, 0, 0));

        chestplate.setItemMeta(meta);

        return chestplate;
    }
    public ItemStack getRobberHelmet() {
        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta meta = (LeatherArmorMeta) helmet.getItemMeta();
        meta.setDisplayName("Robber helmet");
        meta.setUnbreakable(true);

        // set color
        meta.setColor(Color.fromRGB(2, 0, 0));

        helmet.setItemMeta(meta);

        return helmet;
    }
    public ItemStack getRobberChestplate() {
        ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta meta = (LeatherArmorMeta) chestplate.getItemMeta();
        meta.setDisplayName("Robber chestplate");
        meta.setUnbreakable(true);

        // set color
        meta.setColor(Color.fromRGB(2, 0, 0));

        chestplate.setItemMeta(meta);

        return chestplate;
    }

    public Location getButtonFreeJailLocation() {
        Player player = Bukkit.getPlayer(players.get(0));
        if (player != null) {
            return player.getLocation().getWorld().getBlockAt(8, 153, 8).getLocation();
        } else return null;
    }

    public ArrayList<UUID> getPlayers() {
        return players;
    }
}
