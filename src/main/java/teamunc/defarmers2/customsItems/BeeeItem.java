package teamunc.defarmers2.customsItems;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import teamunc.defarmers2.managers.TeamManager;
import teamunc.defarmers2.mobs.EnumMobStatue;
import teamunc.defarmers2.serializables.MobEffect;

import java.util.List;

public class BeeeItem extends CustomItem {

    public BeeeItem() {
        super("Beee", 9, "BEEE",1);
    }

    @Override
    public void onClick(CustomItemParams params) {
        Player player = params.getPlayer();
        TeamManager teamManager = TeamManager.getInstance();
        Team team = teamManager.getTeamOfPlayer(player.getName());

        // adding MobEffect confuse
        MobEffect mobEffect = new MobEffect(EnumMobStatue.BEEE, 10, teamManager.getMobsSpawnedOfTeam(team.getName()), player.getUniqueId());
        TeamManager.getInstance().addMobEffect(mobEffect);
    }
    @Override
    public @NotNull List<String> getDescription() {
        return List.of("§r§7Description", "§r§7juste §cici");
    }

    @Override
    public int getDefaultPrice() {
        return 65;
    }

}
