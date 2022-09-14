package teamunc.defarmers2.managers;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import teamunc.defarmers2.Defarmers2;
import teamunc.defarmers2.serializables.GameStates;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class GameAnnouncer {

    private static final String prefix = "§8[§6Defarmers2§8]§r ";

    private static void sendTitle(String message) {
        Defarmers2.getInstance().getLogger().log(Level.INFO, "Announcing title: " + message);
        Bukkit.getWorlds().get(0).getPlayers().forEach(player -> player.sendTitle(message, "", 10, 20, 10));

    }

    private static void sendTitle(String message, String subtitle, int fadeIn, int stay, int fadeOut) {
        Defarmers2.getInstance().getLogger().log(Level.INFO, "Announcing subtitle: " + message);
        Bukkit.getWorlds().get(0).getPlayers().forEach(player -> player.sendTitle(message, subtitle, fadeIn, stay, fadeOut));

    }

    public static void announceLoose(String loose) {
        sendMessageToAllOnlinePlayer(Bukkit.getOnlinePlayers(),loose + " sort du jeu !");
    }

    public static void announceWinner(Team winner) {
        //annouce the winner
        GameAnnouncer.sendTitle(winner.getName() + " a gagné !", "", 10, 20, 10);
    }

    public static void announceDraw(ArrayList<Team> teams) {
        String teamsNames = teams.stream().map(Team::getName).collect(Collectors.joining(", "));

        //annouce the winner
        GameAnnouncer.sendTitle("Egalité entre " + teamsNames, "", 10, 20, 10);
    }

    public static void sendMessageToAllOnlinePlayer(Collection<? extends Player> onlinePlayers, String s) {
        Defarmers2.getInstance().getLogger().log(Level.INFO, "Sending message to all online players: " + s);
        onlinePlayers.forEach(player -> player.sendMessage(prefix + s));
    }

    public static void sendMessage(CommandSender player, String message) {
        Defarmers2.getInstance().getLogger().log(Level.INFO, "Sending message to " + player.getName() + ": " + message);
        player.sendMessage(prefix + message);
    }

    public static void announceClassement(HashMap<Integer, ArrayList<Team>> teamClassement) {
        TeamManager teamManager = Defarmers2.getInstance().getGameManager().getTeamManager();
        //annouce the winner
        sendMessageToAllOnlinePlayer(Bukkit.getOnlinePlayers(),"-- Classement : --");
        teamClassement.forEach((key, value) -> {
            String teamsNames = value.stream().map(Team::getName).collect(Collectors.joining(", "));
            int score = teamManager.getTeamScore(value.get(0).getName());

            sendMessageToAllOnlinePlayer(Bukkit.getOnlinePlayers(),"§6" + key + "§r : " + teamsNames + " - " + score);
        });
    }

    public static void announceNextPhase(GameStates.GameState nextPhase) {
        switch (nextPhase) {
            case PHASE1:
                sendTitle("せ", "", 40, 60, 40);
                break;
            case PHASE2:
                sendTitle("き","", 40, 60, 40);
                break;
            case PHASE3:
                sendTitle("く","", 40, 60, 40);
                break;
            case END_GAME:
                sendTitle("け","", 40, 60, 40);
                break;
        }
    }
}
