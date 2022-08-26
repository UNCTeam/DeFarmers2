package teamunc.defarmers2.managers;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.customsItems.*;
import teamunc.defarmers2.customsItems.ui_menu_Items.CustomUIItem;
import teamunc.defarmers2.serializables.GameOptions;

import java.util.ArrayList;
import java.util.List;

public class CustomItemsManager extends Manager {

    private final NamespacedKey customItemKey = new NamespacedKey(Defarmers2.getInstance(),"customitem");
    private final NamespacedKey customDurabilityKey = new NamespacedKey(Defarmers2.getInstance(),"customdurability");

    // SINGLETON
    private static CustomItemsManager instance;
    public static CustomItemsManager getInstance() {
        return instance;
    }
    private CustomItemsManager() {
        super();
    }

    public static void enable() {
        instance = new CustomItemsManager();
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public int getImportance() {
        return 20;
    }

    public NamespacedKey customItemKey() {
        return customItemKey;
    }

    public boolean isCustomItem(ItemStack item) {
        if (item == null) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        PersistentDataContainer data = meta.getPersistentDataContainer();
        if (data == null) return false;
        return data.has(this.customItemKey(), PersistentDataType.STRING);
    }

    public String getCustomType(PersistentDataContainer data) {
        if (data == null) return null;
        return data.get(this.customItemKey(), PersistentDataType.STRING);
    }

    public void actionOfCustomItem(Player player, ItemStack item) {
        if (!this.isCustomItem(item)) return;

        String type = this.getCustomType(item.getItemMeta().getPersistentDataContainer());
        if (type == null) return;

        CustomItemParams customItemParams = new CustomItemParams(player);
        CustomItem customItem = getCustomItem(type);
        customItem.executeOnClick(customItemParams);

        // durability
        this.setCustomDurability(this.getCustomDurability(item) - customItemParams.getDurabilityUsed(), item);
        if (this.getCustomDurability(item) <= 0) {
            item.setAmount(0);
        }

        // actualise lore
        actualiseLore(item);
    }

    public void actualiseLore(ItemStack item) {

        ItemMeta itemMeta = item.getItemMeta();
        String type = this.getCustomType(itemMeta.getPersistentDataContainer());
        CustomItem customItem = getCustomItem(type);

        if (!type.equals("SHOP")) {
            ArrayList<String> description = new ArrayList<>(customItem.getDescription());

            description.add("§7Durability: " + this.getCustomDurability(item));
            itemMeta.setLore(description);
        }
        item.setItemMeta(itemMeta);
    }

    public int getCustomDurability(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        return meta.getPersistentDataContainer().get(this.customDurabilityKey, PersistentDataType.INTEGER);
    }

    public void setCustomDurability(int newDurability, ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.getPersistentDataContainer().set(this.customDurabilityKey, PersistentDataType.INTEGER, newDurability);
        item.setItemMeta(meta);
    }

    public static CustomItem getCustomItem(String type) {
        // create item from the type and return it from all the custom items
        switch (type) {
            case "SHOP":
                return new ShopItem();
            case "ARMAGEDDON":
                return new ArmageddonItem();
            case "ARMORED":
                return new ArmoredItem();
            case "BEEEEVIL":
                return new BeeeEvilItem();
            case "BEEE":
                return new BeeeItem();
            case "BUFF":
                return new BuffItem();
            case "CARROTONASTICK":
                return new CarrotOnAStickItem();
            case "CONFUSE":
                return new ConfuseItem();
            case "DINNERBONE":
                return new DinnerboneItem();
            case "FOCUS":
                return new FocusItem();
            case "FROZEN":
                return new FrozenItem();
            case "HEAL":
                return new HealItem();
            case "METEORITE":
                return new MeteoriteItem();
            case "SUPERMOB":
                return new SuperMobItem();
            case "THUNDER":
                return new ThunderItem();
            case "ULTIMATESAVER":
                return new UltimateSaverItem();
            case "WALLTHAT":
                return new WallThatItem();
        }
        return null;
    }

    public static String[] getAllCustomItemTypes() {
        return new String[] {
                "SHOP",
                "ARMAGEDDON",
                "ARMORED",
                "BEEEEVIL",
                "BEEE",
                "BUFF",
                "CARROTONASTICK",
                "CONFUSE",
                "DINNERBONE",
                "FOCUS",
                "FROZEN",
                "HEAL",
                "METEORITE",
                "SUPERMOB",
                "THUNDER",
                "ULTIMATESAVER",
                "WALLTHAT"
        };
    }
}
