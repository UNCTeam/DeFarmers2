package teamunc.defarmers2.customsItems.ui_menu_Items;

import org.bukkit.Bukkit;
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
        HashMap<String, Integer> artefactNumberMap = TeamManager.getInstance().getTeamsArtefacts(team);
        HashMap<String, Integer> mobNumberMap = TeamManager.getInstance().getTeamsMobs(team);

        // Add all existing custom ui items to the inventory (if they are enabled)
        for (String type : CustomItemsManager.getAllCustomItemTypes()) {
            CustomItem item = CustomItemsManager.getInstance().getCustomItem(type);

            if (GameOptions.getInstance().isCustomItemEnabled(type) && type != "SHOP") {
                int price = GameOptions.getInstance().getCustomItemPrice(type);
                int nbActuelle = artefactNumberMap.getOrDefault(type, 0);
                this.initLoreAndTitle(item, price, nbActuelle);
                inv.addItem(new CustomUIItem(item));
            }
        }

        // Add all mobs custom ui item to the inventory (if they are enabled)
        int i = 27;
        for (DeFarmersEntityType type : CustomMobsManager.getAllCustomMobsTypes()) {
            ItemStack item = type.getMobHeadItem();

            if (GameOptions.getInstance().isCustomMobEnabled(type)) {
                int price = GameOptions.getInstance().getCustomMobPrice(type);
                int nbActuelle = mobNumberMap.getOrDefault(type.toString(), 0);
                this.initLoreAndTitle(item, price, nbActuelle);
                inv.setItem(i, new CustomUIItem(item));
                i++;
            }
        }
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent) {
        // Create a new inventory, with no owner (as this isn't a real inventory), a size of nine, called example
        Inventory inv = Bukkit.createInventory(new ShopHolder(null), 54, "Buy Menu");
        ent.openInventory(inv);
        Team team = Defarmers2.getInstance().getGameManager().getTeamManager().getTeamOfPlayer((Player) ent);
        initializeItems(inv, team);
    }

    private CustomItem initLoreAndTitle(CustomItem item, int price, int nbActuelle) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§r§f§cBuy " + meta.getDisplayName());

        List<String> lore = Arrays.asList("", "§r§l§cClick to buy this item", "Price : §r§f§a" + price, "§r§l§cYou have : §r§f§a" + nbActuelle);
        System.out.println(item.getDescription());
        //lore.addAll(item.getDescription());
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack initLoreAndTitle(ItemStack item, int price, int nbActuelle) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§r§f§cBuy " + meta.getDisplayName());

        List<String> lore = Arrays.asList("", "§r§l§cClick to buy this item", "Price : §r§f§a" + price, "§r§l§cYou have : §r§f§a" + nbActuelle);
        //lore.addAll(item.getItemMeta().getLore());
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
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
