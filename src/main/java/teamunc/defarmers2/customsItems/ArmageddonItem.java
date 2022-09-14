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
import java.util.Random;

public class ArmageddonItem extends CustomItem {

    public ArmageddonItem() {
        super("Armageddon", 11, "ARMAGEDDON");
    }

    @Override
    public void onClick(CustomItemParams params) {
        Random r = new Random();
        int random = r.nextInt(4) + 5;
        GameManager gameManager = GameManager.getInstance();

        Location locCenter = gameManager.getPhaseSpawn(GameStates.GameState.PHASE3).clone();

        for (int i = 0; i < random; i++) {
            Location locRdm = MathsUtils.getRandomLocation(locCenter, 55);
            locRdm.getWorld().createExplosion(locRdm, 6F, false);
        }
    }
    @Override
    public @NotNull List<String> getDescription() {
        return List.of("§r§7Des explosions spawn", "§r§7aléatoirement dans la map.");
    }

    @Override
    public int getDefaultPrice() {
        return 165;
    }

    @Override
    public int getDefaultDurability() {
        return 1;
    }
}
