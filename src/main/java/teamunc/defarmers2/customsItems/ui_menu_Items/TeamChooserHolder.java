package teamunc.defarmers2.customsItems.ui_menu_Items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class TeamChooserHolder implements InventoryHolder {

    private ITeamChooseAction action;

    public TeamChooserHolder(ITeamChooseAction action) {
        this.action = action;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return null;
    }

    public void execute(Player player, String teamName) {
        action.execute(player, teamName);
    }
}
