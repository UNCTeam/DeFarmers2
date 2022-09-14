package teamunc.defarmers2.customsItems.ui_menu_Items;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.managers.CustomItemsManager;
import teamunc.defarmers2.managers.TeamManager;
import teamunc.defarmers2.mobs.DeFarmersEntityType;
import teamunc.defarmers2.serializables.GameOptions;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CustomUIItem extends ItemStack {

    public CustomUIItem(ItemStack item, Team team) {
        super(item);

        setLore(item, team);
    }

    public void setLore(ItemStack item, Team team) {
        // setting lore
        ItemMeta meta = item.getItemMeta();
        int nbActuelle = getNbItem(team);
        List<String> lores = meta.getLore();
        List<String> newLores = Arrays.asList("", "§r§l§cClick to buy this item","Price : " + getPrice(),"§r§l§cYou have : §r§f§a" + nbActuelle);
        if (lores != null) {
            lores.addAll(newLores);
            meta.setLore(lores);
        } else {
            meta.setLore(newLores);
        }
        this.setItemMeta(meta);
    }

    public int getNbItem(Team team) {
        int res = 0;
        if (typeOfCustom() == ItemTypeEnum.CUSTOM_ITEM) {
            HashMap<String, Integer> artefactNumberMap = TeamManager.getInstance().getTeamsArtefacts(team);
            res = artefactNumberMap.getOrDefault(getCustomType(), 0);
        } else if (typeOfCustom() == ItemTypeEnum.CUSTOM_MOB) {
            HashMap<String, Integer> mobNumberMap = TeamManager.getInstance().getSelectedTeamsMobs(team);
            res = mobNumberMap.getOrDefault(getCustomType(), 0);
        }

        return res;
    }

    public String getCustomType() {
        NamespacedKey customItemKey = Defarmers2.getInstance().getGameManager().getCustomItemsManager().customItemKey();
        NamespacedKey customMobKey = Defarmers2.getInstance().getGameManager().getCustomMobsManager().customMobItemKey();

        switch (typeOfCustom()) {
            case CUSTOM_ITEM:
                return this.getItemMeta().getPersistentDataContainer().get(customItemKey, PersistentDataType.STRING);
            case CUSTOM_MOB:
                return this.getItemMeta().getPersistentDataContainer().get(customMobKey, PersistentDataType.STRING);
            default:
                return "Unknown";
        }
    }

    public ItemTypeEnum typeOfCustom() {
        NamespacedKey customItemKey = Defarmers2.getInstance().getGameManager().getCustomItemsManager().customItemKey();
        NamespacedKey customMobKey = Defarmers2.getInstance().getGameManager().getCustomMobsManager().customMobItemKey();

        if (this.getItemMeta().getPersistentDataContainer().has(customItemKey, PersistentDataType.STRING)) {
            return ItemTypeEnum.CUSTOM_ITEM;
        } else if (this.getItemMeta().getPersistentDataContainer().has(customMobKey, PersistentDataType.STRING)) {
            return ItemTypeEnum.CUSTOM_MOB;
        } else {
            return null;
        }
    }

    public int getPrice() {
        if (this.typeOfCustom() == ItemTypeEnum.CUSTOM_ITEM) {
            return GameOptions.getInstance().getCustomItemPrice(this.getCustomType());

        } else if (this.typeOfCustom() == ItemTypeEnum.CUSTOM_MOB) {
            return GameOptions.getInstance().getCustomMobPrice(DeFarmersEntityType.getByName(this.getCustomType()));
        } else {
            return 0;
        }
    }
}
