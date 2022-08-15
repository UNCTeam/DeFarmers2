package teamunc.defarmers2.customsItems;

import org.bukkit.Bukkit;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import teamunc.defarmers2.customsItems.ui_menu_Items.EffectChooserInventory;
import teamunc.defarmers2.customsItems.ui_menu_Items.TeamChooserInventory;
import teamunc.defarmers2.managers.CustomMobsManager;
import teamunc.defarmers2.managers.TeamManager;

import java.util.List;
import java.util.UUID;

public class BuffItem extends CustomItem {

    public BuffItem() {
        super("Buff", 16, "BUFF",1);
    }

    @Override
    public void onClick(CustomItemParams params) {
        TeamManager teamManager = TeamManager.getInstance();
        CustomMobsManager customMobsManager = CustomMobsManager.getInstance();

        Player player = params.getPlayer();

        EffectChooserInventory.getInstance().openInventory(player, (teamName,effect) -> {
            for (UUID uuid : teamManager.getMobsSpawnedOfTeam(teamName)) {
                Mob mob = (Mob) Bukkit.getEntity(uuid);
                mob.addPotionEffect(effect);
            }
        });
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
