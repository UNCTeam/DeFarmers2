package teamunc.defarmers2.customsItems;

import com.sk89q.worldedit.util.Direction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import teamunc.defarmers2.managers.TeamManager;
import teamunc.defarmers2.mobs.DeFarmersEntityFabric;
import teamunc.defarmers2.mobs.DeFarmersEntityType;
import teamunc.defarmers2.serializables.GameStates;
import teamunc.defarmers2.utils.worldEdit.MathsUtils;

import java.util.List;

public class SuperMobItem extends CustomItem {

    public SuperMobItem() {
        super("SuperMob", 12, "SUPERMOB",1);
    }

    @Override
    public void onClick(CustomItemParams params) {
        Player player = params.getPlayer();
        TeamManager teamManager = TeamManager.getInstance();
        Team team = teamManager.getTeamOfPlayer(player);
        DeFarmersEntityFabric deFarmersEntityFabric = DeFarmersEntityFabric.getInstance();
        Mob mob = deFarmersEntityFabric.createDeFarmersMob(DeFarmersEntityType.ZOMBIE, team.getName(), GameStates.GameState.PHASE3, 1, MathsUtils.getNextLocation(Direction.DOWN,player.getLocation(), 32,null).add(0,1,0));
        mob.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 6));
        mob.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 2));
    }
    @Override
    public @NotNull List<String> getDescription() {
        return List.of("§r§7Un super zombie ayant Force 2", "§r§7et Speed 6 apparait dans votre armée");
    }

    @Override
    public int getDefaultPrice() {
        return 120;
    }
}
