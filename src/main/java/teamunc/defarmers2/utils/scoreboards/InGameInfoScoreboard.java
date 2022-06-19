package teamunc.defarmers2.utils.scoreboards;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.managers.GameManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InGameInfoScoreboard extends VScoreboard{

    /**
     * @param player the player the scoreboard is for
     */
    public InGameInfoScoreboard(Player player) {
        super(player, ChatColor.RED + "" + ChatColor.BOLD + "UNC DEFARMERS 2");
    }

    @Override
    public List<String> getLines() {

        ArrayList<String> lines = new ArrayList<>();
        Defarmers2 plugin = Defarmers2.getInstance();
        GameManager gameManager = plugin.getGameManager();
        Team team = gameManager.getTeamManager().getTeamOfPlayer(this.getPlayer());

        lines.addAll(
                Arrays.asList(
                        ChatColor.BOLD + "" + ChatColor.GOLD + "-----------------",
                        " ",
                        ChatColor.BOLD + "" + ChatColor.GOLD +"- Phase Actuelle : " + ChatColor.AQUA + gameManager.getGameStates().getState().name(),
                        " ",
                        ChatColor.BOLD + "" + ChatColor.GOLD +"- Temps restant :"
                )
        );
        String tempsRestant = "  §b";
        tempsRestant+=gameManager.getTickActionsManager().getHourLeft() + "h ";
        tempsRestant+=gameManager.getTickActionsManager().getMinuteLeft() + "m ";
        tempsRestant+=gameManager.getTickActionsManager().getSecondLeft() + "s";

        lines.add(tempsRestant);


        // team informations
        if (team != null) {
            lines.add(" ");
            lines.add(ChatColor.BOLD + "" + ChatColor.GOLD + "Team Money : " + ChatColor.AQUA + gameManager.getTeamManager().getTeamMoney(team.getColor().toString()));
            lines.add(ChatColor.BOLD + "" + ChatColor.GOLD + "Team Score : " + ChatColor.AQUA + gameManager.getTeamManager().getTeamScore(team.getColor().toString()));
        }

        return lines;
    }

    @Override
    public void actualise() {
        this.updateLines(this.getLines());
    }
}
