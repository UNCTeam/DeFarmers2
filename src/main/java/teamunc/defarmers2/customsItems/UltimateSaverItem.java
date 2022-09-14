package teamunc.defarmers2.customsItems;

import org.bukkit.Bukkit;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import teamunc.defarmers2.managers.TeamManager;
import teamunc.defarmers2.serializables.GameStates;

import java.util.List;
import java.util.UUID;

public class UltimateSaverItem extends CustomItem {

    public UltimateSaverItem() {
        super("Ultimate Saver", 17, "ULTIMATESAVER");
    }

    @Override
    public void onClick(CustomItemParams params) {
        TeamManager teamManager = TeamManager.getInstance();

        for (Team team : teamManager.getTeams()) {
            for (UUID uuid : teamManager.getMobsSpawnedOfTeam(team.getName())) {
                Mob mob = (Mob) Bukkit.getEntity(uuid);
                mob.teleport(teamManager.getTeamSpawnLocation(team.getName(), GameStates.GameState.PHASE3).clone().subtract(0, 3, 0));
            }
        }


    }
    @Override
    public @NotNull List<String> getDescription() {
        return List.of("§r§7Toutes les armées sont tp", "§r§7à leur point de spawn.");
    }

    @Override
    public int getDefaultPrice() {
return 100;
    }

    @Override
    public int getDefaultDurability() {
        return 1;
    }
}
