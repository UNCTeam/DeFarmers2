package teamunc.defarmers2.managers;

import org.bukkit.Bukkit;
import teamunc.defarmers2.Defarmers2;

import java.util.logging.Level;

public class GameAnnouncer {
    public static void announceTitle(String message) {
        Defarmers2.getInstance().getLogger().log(Level.INFO, "Announcing title: " + message);
        Bukkit.getWorlds().get(0).getPlayers().forEach(player -> player.sendTitle(message, "", 10, 20, 10));

    }
}