package teamunc.defarmers2.customsItems;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import teamunc.defarmers2.managers.TeamManager;

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

        Team team = teamManager.getTeamOfPlayer(player);

        for (UUID uuid : teamManager.getMobsSpawnedOfTeam(team.getName())) {
            Mob mob = (Mob) Bukkit.getEntity(uuid);
            mob.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
            mob.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
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
