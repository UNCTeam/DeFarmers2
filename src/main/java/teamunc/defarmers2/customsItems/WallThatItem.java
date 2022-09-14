package teamunc.defarmers2.customsItems;

import com.sk89q.worldedit.util.Direction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.utils.worldEdit.ApiWorldEdit;
import teamunc.defarmers2.utils.worldEdit.MathsUtils;

import java.util.List;

public class WallThatItem extends CustomItem {

    public WallThatItem() {
        super("Wall That", 5, "WALLTHAT");
    }

    @Override
    public void onClick(CustomItemParams params) {
        Player player = params.getPlayer();

        Direction direction = MathsUtils.getCardinalDirection(player);

        Location center = MathsUtils.getNextLocation(Direction.DOWN, player.getLocation(), 64, null);

        ApiWorldEdit.generateWall(4, 4, center.clone().add(0,1,0), Material.TINTED_GLASS, direction);

        Bukkit.getScheduler().runTaskLater(Defarmers2.getInstance(),() -> {
            ApiWorldEdit.generateWall(4, 4, center.clone().add(0,1,0), Material.AIR, direction);
        }, 15*20);

    }
    @Override
    public @NotNull List<String> getDescription() {
        return List.of("§r§7Description", "§r§7juste §cici");
    }

    @Override
    public int getDefaultPrice() {
        return 20;
    }

    @Override
    public int getDefaultDurability() {
        return 1;
    }
}
