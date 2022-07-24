package teamunc.defarmers2.customsItems.ui_menu_Items;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class ShopHolder implements InventoryHolder {

    private final Inventory inventory;

    public ShopHolder(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
