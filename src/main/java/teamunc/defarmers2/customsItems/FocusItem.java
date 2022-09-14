package teamunc.defarmers2.customsItems;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;
import teamunc.defarmers2.customsItems.ui_menu_Items.TeamChooserInventory;
import teamunc.defarmers2.managers.CustomMobsManager;
import teamunc.defarmers2.managers.TeamManager;

import java.util.List;

public class FocusItem extends CustomItem {

    public FocusItem() {
        super("Focus", 15, "FOCUS");
    }

    @Override
    public void onClick(CustomItemParams params) {
        TeamManager teamManager = TeamManager.getInstance();
        CustomMobsManager customMobsManager = CustomMobsManager.getInstance();

        Player player = params.getPlayer();

        TeamChooserInventory.getInstance().openInventory(player, (p,teamName) -> {
            Team team1 = teamManager.getTeamOfPlayer(p.getName());
            Team team2 = teamManager.getTeam(teamName);
            customMobsManager.focusOnATeam(team1,team2);
        });
    }
    @Override
    public @NotNull List<String> getDescription() {
        return List.of("§r§7Ouvre une interface", "§r§7permettant de choisir une armée à attaquer.");
    }

    @Override
    public int getDefaultPrice() {
        return 50;
    }

    @Override
    public int getDefaultDurability() {
        return 3;
    }
}
