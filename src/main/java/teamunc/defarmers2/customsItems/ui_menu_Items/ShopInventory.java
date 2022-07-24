package teamunc.defarmers2.customsItems.ui_menu_Items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;
import teamunc.defarmers2.customsItems.CustomItem;
import teamunc.defarmers2.managers.CustomItemsManager;
import teamunc.defarmers2.managers.TeamManager;
import teamunc.defarmers2.serializables.GameOptions;

import java.util.Arrays;

public class ShopInventory implements Listener {
    private final Inventory inv;

    public ShopInventory() {
        // Create a new inventory, with no owner (as this isn't a real inventory), a size of nine, called example
        inv = Bukkit.createInventory(new ShopHolder(null), 54, "Buy Menu");

        // Put the items into the inventory
        initializeItems();
    }

    // You can call this whenever you want to put the items in
    public void initializeItems() {
        // Add all existing custom ui items to the inventory (if they are enabled)
        for (String type : CustomItemsManager.getAllCustomItemTypes()) {
            CustomItem item = CustomItemsManager.getInstance().getCustomItem(type);
            if (GameOptions.getInstance().isCustomItemEnabled(type) && type != "SHOP") {
                inv.addItem(new CustomUIItem(item));
            }
        }
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }

    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        final Player p = (Player) e.getWhoClicked();
        if (e.getClickedInventory() == null || !(e.getClickedInventory().getHolder() instanceof ShopHolder)) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;


        final Team team = TeamManager.getInstance().getTeamOfPlayer(p);

        // verify its a custom ui item
        CustomUIItem item = new CustomUIItem(clickedItem);

        System.out.println("Clicked on item :" + item.getType());

        item.buy(team);
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().getHolder() instanceof ShopHolder) {
            e.setCancelled(true);
        }
    }
}
