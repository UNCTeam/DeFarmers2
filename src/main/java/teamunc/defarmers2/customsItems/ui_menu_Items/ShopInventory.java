package teamunc.defarmers2.customsItems.ui_menu_Items;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Team;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.customsItems.CustomItem;
import teamunc.defarmers2.managers.CustomItemsManager;
import teamunc.defarmers2.managers.CustomMobsManager;
import teamunc.defarmers2.managers.TeamManager;
import teamunc.defarmers2.mobs.DeFarmersEntityType;
import teamunc.defarmers2.serializables.GameOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ShopInventory implements Listener {

    // SINGLETON
    private static ShopInventory instance;
    public static ShopInventory getInstance() {
        if (instance == null) {
            instance = new ShopInventory();
        }
        return instance;
    }
    private ShopInventory() {}

    // You can call this whenever you want to put the items in
    public void initializeItems(Inventory inv, Team team) {


        // Add all existing custom ui items to the inventory (if they are enabled)
        for (String type : CustomItemsManager.getAllCustomItemTypes()) {
            CustomItem customItem = CustomItemsManager.getCustomItem(type);
            if (GameOptions.getInstance().isCustomItemEnabled(type) && type != "SHOP") {
                inv.addItem(new CustomUIItem(customItem,team));
            }
        }

        // Add all mobs custom ui item to the inventory (if they are enabled)
        int i = 27;
        for (DeFarmersEntityType type : CustomMobsManager.getAllCustomMobsTypes()) {
            ItemStack item = type.getMobHeadItem();
            if (GameOptions.getInstance().isCustomMobEnabled(type)) {
                inv.setItem(i, new CustomUIItem(item,team));
                i++;
            }
        }
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent) {
        // Create a new inventory, with no owner (as this isn't a real inventory), a size of nine, called example
        Inventory inv = Bukkit.createInventory(new ShopHolder(), 54, ChatColor.WHITE + "\uF80B섥");
        ent.openInventory(inv);
        Team team = Defarmers2.getInstance().getGameManager().getTeamManager().getTeamOfPlayer(ent.getName());
        initializeItems(inv, team);
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

        // create a new custom ui item from the clicked item just to buy it and update lore
        final Team team = TeamManager.getInstance().getTeamOfPlayer(p.getName());

        if (new CustomUIItem(clickedItem,team).typeOfCustom() == ItemTypeEnum.CUSTOM_ITEM) {
            String type = CustomItemsManager.getInstance().getCustomType(clickedItem.getItemMeta().getPersistentDataContainer());
            CustomItem customItem = CustomItemsManager.getCustomItem(type);
            Defarmers2.getInstance().getGameManager().getTeamManager().buy(team,new CustomUIItem(clickedItem,team));
            e.setCurrentItem(new CustomUIItem(customItem,team));
        } else {
            CustomUIItem item = new CustomUIItem(clickedItem,team);
            Defarmers2.getInstance().getGameManager().getTeamManager().buy(team,item);
            e.setCurrentItem(item);
        }




    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().getHolder() instanceof ShopHolder) {
            e.setCancelled(true);
        }
    }
}
