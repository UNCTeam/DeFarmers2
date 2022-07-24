package teamunc.defarmers2.customsItems.ui_menu_Items;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.managers.TeamManager;
import teamunc.defarmers2.serializables.GameOptions;

public class CustomUIItem extends ItemStack {


    public CustomUIItem(ItemStack item) {
        super(item);
    }

    public String getCustomType() {
        NamespacedKey customItemKey = Defarmers2.getInstance().getGameManager().getCustomItemsManager().customItemKey();
        return this.getItemMeta().getPersistentDataContainer().get(customItemKey, PersistentDataType.STRING);
    }

    public void buy(@NotNull Team team) {
        TeamManager teamManager = Defarmers2.getInstance().getGameManager().getTeamManager();
        int price = GameOptions.getInstance().getCustomItemPrice(this.getCustomType());

        int Money = teamManager.getTeamMoney(team.getName());
        if (Money < price) {
            return;
        }
        teamManager.setTeamMoney(team.getName(), Money - price);
        teamManager.addArtefactInTeam(team.getName(), this);
    }
}
