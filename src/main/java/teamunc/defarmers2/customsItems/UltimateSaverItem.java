package teamunc.defarmers2.customsItems;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class UltimateSaverItem extends CustomItem {

    public UltimateSaverItem() {
        super("Ultimate Saver", 17, "ULTIMATESAVER");
    }

    @Override
    public void onClick(CustomItemParams params) {
        Player player = params.getPlayer();

        Inventory inv = Bukkit.createInventory(null, 54, "Buy Menu");

        player.openInventory(inv);
    }
}