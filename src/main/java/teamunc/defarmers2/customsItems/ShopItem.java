package teamunc.defarmers2.customsItems;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import teamunc.defarmers2.customsItems.ui_menu_Items.ShopInventory;

import java.util.List;

public class ShopItem extends CustomItem {

    public ShopItem() {
        super("Shop", 1, "SHOP");
    }

    @Override
    public void onClick(CustomItemParams params) {
        Player player = params.getPlayer();


        ShopInventory.getInstance().openInventory(player);
    }

    @Override
    public @NotNull List<String> getDescription() {
        return List.of("§r§7Description", "§r§7juste §cici");
    }

    @Override
    public int getDefaultPrice() {
        return 0;
    }
}
