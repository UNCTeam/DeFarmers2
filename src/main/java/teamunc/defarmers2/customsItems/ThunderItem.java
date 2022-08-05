package teamunc.defarmers2.customsItems;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ThunderItem extends CustomItem {

    public ThunderItem() {
        super("Thunder", 6, "THUNDER");
    }

    @Override
    public void onClick(CustomItemParams params) {
        Player player = params.getPlayer();

        player.getLocation().getWorld().strikeLightning(player.getLocation().subtract(0, 26, 0));
    }
    @Override
    public @NotNull List<String> getDescription() {
        return List.of("§r§7Éclair la ou est le joueur. Voila.");
    }

    @Override
    public int getDefaultPrice() {
        return 85;
    }
}
