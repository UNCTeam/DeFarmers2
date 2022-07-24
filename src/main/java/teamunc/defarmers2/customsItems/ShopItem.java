package teamunc.defarmers2.customsItems;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.customsItems.ui_menu_Items.CustomUIItem;
import teamunc.defarmers2.customsItems.ui_menu_Items.ShopInventory;
import teamunc.defarmers2.managers.CustomItemsManager;
import teamunc.defarmers2.serializables.GameOptions;

public class ShopItem extends CustomItem {

    public ShopItem() {
        super("Shop", 1, "SHOP");
    }

    @Override
    public void onClick(CustomItemParams params) {
        Player player = params.getPlayer();

        ShopInventory inv = new ShopInventory();

        inv.openInventory(player);
    }
}
