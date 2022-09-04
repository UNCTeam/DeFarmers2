package teamunc.defarmers2.customsItems;

import com.sk89q.worldedit.util.Direction;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import teamunc.defarmers2.managers.TeamManager;
import teamunc.defarmers2.mobs.EnumMobStatue;
import teamunc.defarmers2.serializables.MobEffect;
import teamunc.defarmers2.utils.worldEdit.MathsUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class ConfuseItem extends CustomItem {

    public ConfuseItem() {
        super("Confuse", 14, "CONFUSE",1);
    }

    @Override
    public void onClick(CustomItemParams params) {
        Player player = params.getPlayer();
        TeamManager teamManager = TeamManager.getInstance();

        Location location = MathsUtils.getNextLocation(Direction.DOWN, player.getLocation(), 64, null);
        if (location != null) {
            // get mobs in a rayon of 15 blocks around the location
            Collection<Entity> entities = location.getWorld().getNearbyEntities(location, 15, 15, 15);
            ArrayList<UUID> mobs = new ArrayList<>();
            for (Entity entity : entities) {
                if (entity instanceof Mob && teamManager.getAllSpawnedMobsOfAllTeams().contains(entity.getUniqueId())) {
                    mobs.add(entity.getUniqueId());
                    break;
                }
            }

            // adding MobEffect confuse
            MobEffect mobEffect = new MobEffect(EnumMobStatue.CONFUSE, 20, mobs, player.getUniqueId());
            TeamManager.getInstance().addMobEffect(mobEffect);
        }
    }
    @Override
    public @NotNull List<String> getDescription() {
        return List.of("§r§7Description", "§r§7juste §cici");
    }

    @Override
    public int getDefaultPrice() {
        return 175;
    }
}
