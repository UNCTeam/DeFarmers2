package teamunc.defarmers2.customsItems;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import teamunc.defarmers2.managers.GameManager;

import java.util.List;

public class ThunderItem extends CustomItem {

    public ThunderItem() {
        super("Thunder", 6, "THUNDER");
    }

    @Override
    public void onClick(CustomItemParams params) {
        Player player = params.getPlayer();
        GameManager gameManager = GameManager.getInstance();
        Location location = player.getLocation();

        location.setY(gameManager.getGameOptions().getPhase3LocationCenter().getY());

        player.getLocation().getWorld().strikeLightning(location);
    }
    @Override
    public @NotNull List<String> getDescription() {
        return List.of("§r§7Éclair la ou est le joueur. Voila.");
    }

    @Override
    public int getDefaultPrice() {
        return 85;
    }

    @Override
    public int getDefaultDurability() {
        return 1;
    }
}
