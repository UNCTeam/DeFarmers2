package teamunc.defarmers2.customsItems;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import teamunc.defarmers2.Defarmers2;

public abstract class CustomItem extends ItemStack {

    protected NamespacedKey customItemKey;

    public CustomItem(String name, int customModelData, String type) {
        super(new ItemStack(Material.CARROT_ON_A_STICK, 1));
        ItemMeta meta = this.getItemMeta();
        meta.setCustomModelData(customModelData);
        meta.setDisplayName("§r§l§c" + name);
        this.customItemKey = new NamespacedKey(Defarmers2.getInstance(),"customitem");
        PersistentDataContainer data = meta.getPersistentDataContainer();
        data.set(this.customItemKey, PersistentDataType.STRING,type);
        this.setItemMeta(meta);

    }

    public abstract void onClick(CustomItemParams params);

}
