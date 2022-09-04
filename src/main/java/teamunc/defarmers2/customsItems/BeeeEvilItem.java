package teamunc.defarmers2.customsItems;

import com.sk89q.worldedit.util.Direction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import teamunc.defarmers2.managers.CustomMobsManager;
import teamunc.defarmers2.managers.GameManager;
import teamunc.defarmers2.managers.TeamManager;
import teamunc.defarmers2.mobs.EnumMobStatue;
import teamunc.defarmers2.serializables.GameStates;
import teamunc.defarmers2.serializables.MobEffect;
import teamunc.defarmers2.utils.worldEdit.MathsUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class BeeeEvilItem extends CustomItem {

    public BeeeEvilItem() {
        super("Beee but the evil one", 10, "BEEEEVIL",1);
    }

    @Override
    public void onClick(CustomItemParams params) {
        Player player = params.getPlayer();
        TeamManager teamManager = TeamManager.getInstance();

        Location location = MathsUtils.getNextLocation(Direction.DOWN, player.getLocation(), 64, null);
        if (location != null) {
            // get mobs in a rayon of 15 blocks around the location
            Collection<Entity> entities = location.getWorld().getNearbyEntities(location, 5, 5, 5);
            ArrayList<UUID> mobs = new ArrayList<>();
            for (Entity entity : entities) {
                if (entity instanceof Mob && teamManager.getAllSpawnedMobsOfAllTeams().contains(entity.getUniqueId())) {
                    mobs.add(entity.getUniqueId());
                }
            }

            // adding MobEffect confuse
            MobEffect mobEffect = new MobEffect(EnumMobStatue.BEEE_EVIL, 10, mobs, player.getUniqueId());
            TeamManager.getInstance().addMobEffect(mobEffect);
        }
    }
    @Override
    public @NotNull List<String> getDescription() {
        return List.of("§r§7Description", "§r§7juste §cici");
    }

    @Override
    public int getDefaultPrice() {
        return 180;
    }
}
