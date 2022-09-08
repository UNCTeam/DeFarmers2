package teamunc.defarmers2.customsItems;

import com.sk89q.worldedit.util.Direction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import teamunc.defarmers2.managers.TeamManager;
import teamunc.defarmers2.mobs.EnumMobStatue;
import teamunc.defarmers2.serializables.MobEffect;
import teamunc.defarmers2.utils.worldEdit.MathsUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class ArmoredItem extends CustomItem {

    public ArmoredItem() {
        super("Armored", 4, "ARMORED",1);
    }

    @Override
    public void onClick(CustomItemParams params) {
        Player player = params.getPlayer();
        TeamManager teamManager = TeamManager.getInstance();

        Team team = teamManager.getTeamOfPlayer(player.getName());
        Location location = MathsUtils.getNextLocation(Direction.DOWN, player.getLocation(), 64, null);
        if (location != null) {
            // get mobs in a rayon of 15 blocks around the location
            Collection<Entity> entities = location.getWorld().getNearbyEntities(location, 10, 10, 10);
            int i = 0;
            for (Entity entity : entities) {
                if (i >= 10) break;
                if (entity instanceof Mob && teamManager.getMobsSpawnedOfTeam(team.getName()).contains(entity.getUniqueId())) {
                    Mob mob = (Mob) entity;
                    mob.getEquipment().setHelmet(new ItemStack(Material.IRON_HELMET));
                    mob.getEquipment().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
                    i++;
                }
            }
        }
    }
    @Override
    public @NotNull List<String> getDescription() {
        return List.of("§r§7L’armée se voit équipée d’une superbe", "§r§7armure pour le reste de la partie.");
    }

    @Override
    public int getDefaultPrice() {
        return 125;
    }
}
