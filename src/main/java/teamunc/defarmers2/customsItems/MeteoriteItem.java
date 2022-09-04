package teamunc.defarmers2.customsItems;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import teamunc.defarmers2.managers.GameManager;
import teamunc.defarmers2.serializables.GameStates;
import teamunc.defarmers2.utils.worldEdit.MathsUtils;

import java.util.List;

public class MeteoriteItem extends CustomItem {

    public MeteoriteItem() {
        super("Meteorite", 2, "METEORITE",1);
    }

    @Override
    public void onClick(CustomItemParams params) {
        Player player = params.getPlayer();
        GameManager gameManager = GameManager.getInstance();
        Location location = MathsUtils.getRandomLocation(player.getLocation(), 10);
        location.setY(gameManager.getPhaseSpawn(GameStates.GameState.PHASE3).getY());

        location.getWorld().createExplosion(location, 6F, false);

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
