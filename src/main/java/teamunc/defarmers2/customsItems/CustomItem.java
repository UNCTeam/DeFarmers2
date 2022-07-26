package teamunc.defarmers2.customsItems;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import teamunc.defarmers2.Defarmers2;

import java.util.List;
import java.util.Objects;

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
    public String getCustomType() {
        return Objects.requireNonNull(this.getItemMeta()).getPersistentDataContainer().get(this.customItemKey,PersistentDataType.STRING);
    }
    public abstract void onClick(CustomItemParams params);

    @NotNull
    public abstract List<String> getDescription();
}
