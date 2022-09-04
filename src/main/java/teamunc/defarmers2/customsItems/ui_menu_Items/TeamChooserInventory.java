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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scoreboard.Team;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.managers.CustomItemsManager;
import teamunc.defarmers2.managers.TeamManager;

import java.util.function.Consumer;

public class TeamChooserInventory implements Listener {
    // SINGLETON
    private static TeamChooserInventory instance;
    public static TeamChooserInventory getInstance() {
        if (instance == null) {
            instance = new TeamChooserInventory();
        }
        return instance;
    }
    private TeamChooserInventory() {}

    // You can call this whenever you want to put the items in
    public void initializeItems(Inventory inv, Team team) {
        TeamManager teamManager = TeamManager.getInstance();

        for (Team t : teamManager.getTeams()) {
            if (t.getName().equals(team.getName())) {
                continue;
            }
            ItemStack item = getWoolOfTeam(t);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(t.getColor() + t.getName());
            meta.getPersistentDataContainer().set(CustomItemsManager.getInstance().customItemKey(), PersistentDataType.STRING, t.getName());
            item.setItemMeta(meta);
            inv.addItem(item);
        }
    }

    public ItemStack getWoolOfTeam(Team team) {
        switch (team.getColor()) {
            case RED:
                return new ItemStack(Material.RED_CONCRETE);
            case BLACK:
                return new ItemStack(Material.BLACK_CONCRETE);
            case DARK_BLUE:
                return new ItemStack(Material.BLUE_CONCRETE_POWDER);
            case DARK_GREEN:
                return new ItemStack(Material.GREEN_CONCRETE_POWDER);
            case DARK_AQUA:
                return new ItemStack(Material.CYAN_CONCRETE_POWDER);
            case DARK_RED:
                return new ItemStack(Material.RED_CONCRETE_POWDER);
            case DARK_PURPLE:
                return new ItemStack(Material.PURPLE_CONCRETE_POWDER);
            case GOLD:
                return new ItemStack(Material.ORANGE_CONCRETE);
            case GRAY:
                return new ItemStack(Material.GRAY_CONCRETE);
            case DARK_GRAY:
                return new ItemStack(Material.GRAY_CONCRETE_POWDER);
            case BLUE:
                return new ItemStack(Material.BLUE_CONCRETE);
            case GREEN:
                return new ItemStack(Material.GREEN_CONCRETE);
            case AQUA:
                return new ItemStack(Material.CYAN_CONCRETE);
            case LIGHT_PURPLE:
                return new ItemStack(Material.PURPLE_CONCRETE);
            case YELLOW:
                return new ItemStack(Material.YELLOW_CONCRETE);
            case WHITE:
                return new ItemStack(Material.WHITE_CONCRETE);
        }
        return new ItemStack(Material.STONE);
    }

    public void openInventory(final HumanEntity ent, ITeamChooseAction action) {
        // Create a new inventory, with no owner (as this isn't a real inventory)
        Inventory inv = Bukkit.createInventory(new TeamChooserHolder(action), 27, "Team Chooser");
        ent.openInventory(inv);
        Team team = Defarmers2.getInstance().getGameManager().getTeamManager().getTeamOfPlayer(ent.getName());
        initializeItems(inv, team);
    }

    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        final Player p = (Player) e.getWhoClicked();
        CustomItemsManager customItemsManager = CustomItemsManager.getInstance();
        if (e.getClickedInventory() == null || !(e.getClickedInventory().getHolder() instanceof TeamChooserHolder))
            return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        ItemMeta meta = clickedItem.getItemMeta();

        String teamChoosen = meta.getPersistentDataContainer().get(customItemsManager.customItemKey(), PersistentDataType.STRING);
        ((TeamChooserHolder) e.getClickedInventory().getHolder()).execute(p, teamChoosen);
        p.closeInventory();
    }

        // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().getHolder() instanceof TeamChooserHolder) {
            e.setCancelled(true);
        }
    }
}
