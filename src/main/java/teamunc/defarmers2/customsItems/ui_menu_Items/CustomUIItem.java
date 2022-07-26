package teamunc.defarmers2.customsItems.ui_menu_Items;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.managers.TeamManager;
import teamunc.defarmers2.mobs.DeFarmersEntityType;
import teamunc.defarmers2.serializables.GameOptions;

import java.util.Arrays;
import java.util.Objects;

public class CustomUIItem extends ItemStack {

    public CustomUIItem(ItemStack item) {
        super(item);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§r§fBuy " + meta.getDisplayName());

        meta.setLore(Arrays.asList("", "§r§l§cClick to buy this item","Price : " + getPrice()));

        item.setItemMeta(meta);
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

    public void buy(@NotNull Team team) {

        TeamManager teamManager = Defarmers2.getInstance().getGameManager().getTeamManager();
        int price = getPrice();


        int Money = teamManager.getTeamMoney(team.getName());
        if (Money < price) {
            return;
        }

        teamManager.setTeamMoney(team.getName(), Money - price);
        if (this.typeOfCustom() == ItemTypeEnum.CUSTOM_ITEM)
            teamManager.addArtefactInTeam(team.getName(), this);
        else if (this.typeOfCustom() == ItemTypeEnum.CUSTOM_MOB)
                teamManager.addAMobInTeam(team.getName(), this);
    }

    public int getPrice() {
        System.out.println(this.typeOfCustom());
        System.out.println(this.getCustomType());
        if (this.typeOfCustom() == ItemTypeEnum.CUSTOM_ITEM) {
            return GameOptions.getInstance().getCustomItemPrice(this.getCustomType());

        } else if (this.typeOfCustom() == ItemTypeEnum.CUSTOM_MOB) {
            return GameOptions.getInstance().getCustomMobPrice(DeFarmersEntityType.getByName(this.getCustomType()));
        } else {
            return 0;
        }
    }
}
