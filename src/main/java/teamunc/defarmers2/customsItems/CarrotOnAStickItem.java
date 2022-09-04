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
import teamunc.defarmers2.managers.TeamManager;
import teamunc.defarmers2.mobs.EnumMobStatue;
import teamunc.defarmers2.serializables.MobEffect;
import teamunc.defarmers2.utils.worldEdit.MathsUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class CarrotOnAStickItem extends CustomItem {

    public CarrotOnAStickItem() {
        super("Carrot on a Stick", 3, "CARROTONASTICK",5);
    }

    @Override
    public void onClick(CustomItemParams params) {
        Player player = params.getPlayer();
        TeamManager teamManager = TeamManager.getInstance();
        Team team = teamManager.getTeamOfPlayer(player.getName());

        // adding MobEffect confuse
        MobEffect mobEffect = new MobEffect(EnumMobStatue.FOLLOWING_PLAYER, 10, teamManager.getMobsSpawnedOfTeam(team.getName()), player.getUniqueId());
        TeamManager.getInstance().addMobEffect(mobEffect);
    }
    @Override
    public @NotNull List<String> getDescription() {
        return List.of("§r§7Description", "§r§7juste §cici");
    }

    @Override
    public int getDefaultPrice() {
        return 50;
    }
}
