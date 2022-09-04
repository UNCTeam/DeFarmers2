package teamunc.defarmers2.customsItems.ui_menu_Items;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.managers.CustomItemsManager;
import teamunc.defarmers2.managers.TeamManager;

public class EffectChooserInventory implements Listener {
    // SINGLETON
    private static EffectChooserInventory instance;
    public static EffectChooserInventory getInstance() {
        if (instance == null) {
            instance = new EffectChooserInventory();
        }
        return instance;
    }
    private EffectChooserInventory() {}

    // You can call this whenever you want to put the items in
    public void initializeItems(Inventory inv) {
        ItemStack sugar = new ItemStack(Material.SUGAR);
        ItemMeta metaSugar = sugar.getItemMeta();
        metaSugar.setDisplayName("§c§lSpeed 2");
        sugar.setItemMeta(metaSugar);
        inv.addItem(sugar);

        ItemStack force = new ItemStack(Material.BLAZE_POWDER);
        ItemMeta metaForce = force.getItemMeta();
        metaForce.setDisplayName("§c§lForce 2");
        force.setItemMeta(metaForce);
        inv.addItem(force);

        ItemStack resistance = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        ItemMeta metaResistance = resistance.getItemMeta();
        metaResistance.setDisplayName("§c§lResistance 2");
        resistance.setItemMeta(metaResistance);
        inv.addItem(resistance);
    }

    public void openInventory(final HumanEntity ent, IEffectChooserAction action) {
        // Create a new inventory, with no owner (as this isn't a real inventory)
        Inventory inv = Bukkit.createInventory(new EffectChooserHolder(action), InventoryType.HOPPER, "Effect Chooser");
        ent.openInventory(inv);
        initializeItems(inv);
    }

    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        final Player p = (Player) e.getWhoClicked();
        TeamManager teamManager = TeamManager.getInstance();
        if (e.getClickedInventory() == null || !(e.getClickedInventory().getHolder() instanceof EffectChooserHolder))
            return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        String teamName = teamManager.getTeamOfPlayer(p.getName()).getName();

        PotionEffect potionEffect = null;
        switch (clickedItem.getType()) {
            case SUGAR:
                potionEffect = new PotionEffect(PotionEffectType.SPEED, 20 * 15, 2);
                break;
            case BLAZE_POWDER:
                potionEffect = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 15, 2);
                break;
            case CHAINMAIL_CHESTPLATE:
                potionEffect = new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20 * 15, 2);
                break;
        }
        ((EffectChooserHolder) e.getClickedInventory().getHolder()).execute(teamName, potionEffect);
        p.closeInventory();
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().getHolder() instanceof EffectChooserHolder) {
            e.setCancelled(true);
        }
    }

}
