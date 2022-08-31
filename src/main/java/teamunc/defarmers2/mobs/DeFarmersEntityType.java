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
    GHAST("Ghast", 7),
    PIG_ZOMBIE("PigZombie", 8),
    SLIME("Slime", 9),
    WITCH("Witch", 10),
    WOLF("Wolf", 11),
    BLAZE("Blaze", 12),
    CAVE_SPIDER_JOCKEY("CaveSpiderJockey", 13),
    ENDERMITE("Endermite", 14),
    MAGMA_CUBE("MagmaCube", 15)

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
            if (type.name.equalsIgnoreCase(name) || type.toString().equalsIgnoreCase(name)) {
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
