package teamunc.defarmers2.customsItems;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FocusItem extends CustomItem {

    public FocusItem() {
        super("Focus", 15, "FOCUS");
    }

    @Override
    public void onClick(CustomItemParams params) {
        Player player = params.getPlayer();

        Inventory inv = Bukkit.createInventory(null, 54, "Buy Menu");

        player.openInventory(inv);
    }
    @Override
    public @NotNull List<String> getDescription() {
        return List.of("§r§7Description", "§r§7juste §cici");
    }
}
