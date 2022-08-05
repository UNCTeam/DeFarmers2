package teamunc.defarmers2.customsItems.ui_menu_Items;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

public class EffectChooserHolder implements InventoryHolder {

    private IEffectChooserAction action;

    public EffectChooserHolder(IEffectChooserAction action) {
        this.action = action;
    }

    @NotNull
    @Override
    public Inventory getInventory() {
        return null;
    }

    public void execute(String teamName, PotionEffect effectChoosed) {
        action.execute(teamName, effectChoosed);
    }
}
