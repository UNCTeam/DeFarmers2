package teamunc.defarmers2.mobs;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import teamunc.defarmers2.Defarmers2;

public enum DeFarmersEntityType {

    ZOMBIE("Zombie", 1),
    SKELETON("Skeleton", 2),
    SPIDER("Spider", 3),
    CAVE_SPIDER("CaveSpider", 4),
    CREEPER("Creeper", 5),
    ENDERMAN("Enderman", 6),
    FOX("Fox", 17),
    ZOMBIFIED_PIGLIN("ZombifiedPiglin", 8),
    CAT("Cat", 16),
    RAVAGER("Ravager", 19),
    WOLF("Wolf", 11),
    ENDERMITE("Endermite", 14),
    WITCH("Witch", 10),
    POLAR_BEAR("PolarBear", 20),
    ;


    private String name;
    private int customModelData;
    DeFarmersEntityType(String name, int customModelData) {
        this.name = name;
        this.customModelData = customModelData;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static DeFarmersEntityType getByName(String name) {
        for (DeFarmersEntityType type : DeFarmersEntityType.values()) {
            if (type.name.equalsIgnoreCase(name) || type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No enum constant teamunc.defarmers2.mobs.DeFarmersEntityType." + name);
    }

    public ItemStack getMobHeadItem() {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(this.customModelData);
        meta.setDisplayName(this.name);
        NamespacedKey customMobKey = Defarmers2.getInstance().getGameManager().getCustomMobsManager().customMobItemKey();
        meta.getPersistentDataContainer().set(customMobKey, PersistentDataType.STRING, this.toString());

        item.setItemMeta(meta);

        return item;
    }
}
