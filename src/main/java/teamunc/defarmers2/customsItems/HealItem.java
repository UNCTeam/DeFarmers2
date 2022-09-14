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

public class HealItem extends CustomItem {

    public HealItem() {
        super("Heal", 13, "HEAL");
    }

    @Override
    public void onClick(CustomItemParams params) {
        Player player = params.getPlayer();
        TeamManager teamManager = TeamManager.getInstance();

        Team team = teamManager.getTeamOfPlayer(player.getName());

        for (UUID uuid : teamManager.getMobsSpawnedOfTeam(team.getName())) {
            Mob mob = (Mob) Bukkit.getEntity(uuid);
            mob.setHealth(mob.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).getValue());
        }
    }
    @Override
    public @NotNull List<String> getDescription() {
        return List.of("§r§7Soigne l’armée de X HP");
    }

    @Override
    public int getDefaultPrice() {
        return 150;
    }

    @Override
    public int getDefaultDurability() {
        return 1;
    }
}
