package teamunc.defarmers2.customsItems;

import org.bukkit.Bukkit;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import teamunc.defarmers2.managers.TeamManager;

import java.util.List;
import java.util.UUID;

public class DinnerboneItem extends CustomItem {

    public DinnerboneItem() {
        super("Dinnerbone", 8, "DINNERBONE");
    }

    @Override
    public void onClick(CustomItemParams params) {
        Player player = params.getPlayer();
        TeamManager teamManager = TeamManager.getInstance();

        Team team = teamManager.getTeamOfPlayer(player.getName());

        for (UUID uuid : teamManager.getMobsSpawnedOfTeam(team.getName())) {
            Mob mob = (Mob) Bukkit.getEntity(uuid);
            mob.setCustomName("Dinnerbone");
        }


    }
    @Override
    public @NotNull List<String> getDescription() {
        return List.of("§r§7Boom, les mobs à l’envers !");
    }

    @Override
    public int getDefaultPrice() {
        return 5;
    }

    @Override
    public int getDefaultDurability() {
        return 1;
    }

}
