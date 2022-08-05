package teamunc.defarmers2.customsItems;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import teamunc.defarmers2.utils.worldEdit.MathsUtils;

import java.util.List;

public class MeteoriteItem extends CustomItem {

    public MeteoriteItem() {
        super("Meteorite", 2, "METEORITE");
    }

    @Override
    public void onClick(CustomItemParams params) {
        Player player = params.getPlayer();

        Location location = MathsUtils.getRandomLocation(player.getLocation(), 10).subtract(0, 26, 0);

        location.getWorld().createExplosion(location, 8F, false);

    }
    @Override
    public @NotNull List<String> getDescription() {
        return List.of("§r§7Déclenche une explosion qui apparaitra", "§r§7aléatoirement autour du joueur l’ayant lancé.");
    }

    @Override
    public int getDefaultPrice() {
        return 100;
    }
}
