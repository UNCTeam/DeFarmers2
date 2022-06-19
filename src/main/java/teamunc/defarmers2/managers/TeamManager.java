package teamunc.defarmers2.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import teamunc.defarmers2.serializables.GameStates;
import teamunc.defarmers2.serializables.TeamsStates;
import teamunc.defarmers2.utils.worldEdit.MathsUtils;

import java.util.HashMap;
import java.util.Set;

public class TeamManager extends Manager{


    // SINGLETON
    private static TeamManager instance;
    public static TeamManager getInstance() {
        return instance;
    }
    private TeamManager() {
        super();
    }

    public static void enable() {
        instance = new TeamManager();
    }

    // State
    private TeamsStates teamsStates;

    @Override
    public void onInit() {
        // load save or create if not found
        if (plugin.getGameManager().getFileManager().fileExists("teamsStates")) {
            this.teamsStates = plugin.getGameManager().getFileManager().loadJson("teamsStates", TeamsStates.class);
        } else {
            this.teamsStates = new TeamsStates();
            plugin.getGameManager().getFileManager().saveJson("teamsStates",this.teamsStates);
        }
    }

    @Override
    public void onDisable() {
        // save
        plugin.getGameManager().getFileManager().saveJson("teamsStates",this.teamsStates);
    }


    public TeamsStates getTeamStates() {
        return teamsStates;
    }

    public Team getTeam(String teamColor) {
        for (Team team : getTeamList()) {
            if (team.getName().equalsIgnoreCase(teamColor)) {
                return team;
            }
        }
        return null;
    }

    public Team getTeamOfPlayer(Player player) {
        for (Team team : getTeamList()) {
            if (team.hasEntry(player.getName())) {
                return team;
            }
        }
        return null;
    }

    public void addTeam(String teamColor) {
        Team team = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(teamColor);
        team.setColor(ChatColor.valueOf(teamColor));
        team.setAllowFriendlyFire(false);
        team.setCanSeeFriendlyInvisibles(true);
        team.setPrefix(ChatColor.valueOf(teamColor) + "[" + teamColor + "] ");
        HashMap<GameStates.GameState, Location> teamSpawn = new HashMap<>();
        this.getTeamStates().initTeam(teamColor, 0, 0,teamSpawn);
    }

    public void removeTeam(String teamColor) {
        Team team = getTeam(teamColor);
        if (team != null) {
            team.unregister();
            this.getTeamStates().resetTeam(teamColor);
        }
    }

    public void joinTeam(String playerName, String teamColor) {
        Team team = getTeam(teamColor);
        if (team != null) {
            team.addEntry(playerName);
        }
    }

    public void leaveTeam(String playerName, String teamColor) {
        Team team = getTeam(teamColor);
        if (team != null) {
            team.removeEntry(playerName);
        }
    }
    public Set<Team> getTeamList() {
        return Bukkit.getScoreboardManager().getMainScoreboard().getTeams();
    }

    // get all players in teams
    public HashMap<String, String> getPlayersInTeams() {
        HashMap<String, String> playersInTeams = new HashMap<>();
        for (Team team : getTeamList()) {
            for (String entry : team.getEntries()) {
                playersInTeams.put(entry, team.getName());
            }
        }
        return playersInTeams;
    }

    public Integer getTeamMoney(String teamColor) {
        Team team = getTeam(teamColor);
        if (team != null) {
            return this.teamsStates.getTeamMoney(teamColor);
        }
        return null;
    }

    public void setTeamMoney(String teamColor, Integer money) {
        Team team = getTeam(teamColor);
        if (team != null) {
            this.teamsStates.setTeamMoney(teamColor, money);
        }
    }

    public void addTeamMoney(String teamColor, Integer money) {
        Team team = getTeam(teamColor);
        if (team != null) {
            this.teamsStates.addTeamMoney(teamColor, money);
        }
    }

    public void removeTeamScore(String teamColor, Integer score) {
        Team team = getTeam(teamColor);
        if (team != null) {
            this.teamsStates.removeTeamScore(teamColor, score);
        }
    }

    public Integer getTeamScore(String teamColor) {
        Team team = getTeam(teamColor);
        if (team != null) {
            return this.teamsStates.getTeamScore(teamColor);
        }
        return null;
    }

    public void setTeamScore(String teamColor, Integer score) {
        Team team = getTeam(teamColor);
        if (team != null) {
            this.teamsStates.setTeamScore(teamColor, score);
        }
    }

    public void addTeamScore(String teamColor, Integer score) {
        Team team = getTeam(teamColor);
        if (team != null) {
            this.teamsStates.addTeamScore(teamColor, score);
        }
    }

    public void removeTeamMoney(String teamColor, Integer score) {
        Team team = getTeam(teamColor);
        if (team != null) {
            this.teamsStates.removeTeamMoney(teamColor, score);
        }
    }

    public void teleportToExactSpawn(GameStates.GameState state) {
        for (Team team : this.getTeamStates().getAllTeams()) {
            if (team != null) {
                for (String playerName : team.getEntries()) {
                    Player player = Bukkit.getPlayer(playerName);
                    if (player != null) {
                        Bukkit.getPlayer(playerName).teleport(this.plugin.getGameManager().getPhaseSpawn(state));
                    }
                }
            }
        }
    }

    // setup team spawn
    public void setupTeamSpawn() {
        int nbTeam = this.getTeamStates().getAllTeams().size();

        // phase 1
        Location center1 = this.plugin.getGameManager().getPhaseSpawn(GameStates.GameState.PHASE1);
        int radius1 = 22*nbTeam + 15;
        Location[] locations1 = MathsUtils.getCircle(center1, radius1, nbTeam);

        for (Team team : this.getTeamStates().getAllTeams()) {
            if (team != null) {
                this.getTeamStates().setTeamSpawnPerPhase(team.getName(), GameStates.GameState.PHASE1, locations1[this.getTeamStates().getTeamIndex(team.getName())]);
            }
        }

        // phase 2
        Location center2 = this.plugin.getGameManager().getPhaseSpawn(GameStates.GameState.PHASE2);
        int radius2 = 22*nbTeam + 15;
        Location[] locations2 = MathsUtils.getCircle(center2, radius2, nbTeam);

        for (Team team : this.getTeamStates().getAllTeams()) {
            if (team != null) {
                this.getTeamStates().setTeamSpawnPerPhase(team.getName(), GameStates.GameState.PHASE2, locations2[this.getTeamStates().getTeamIndex(team.getName())]);
            }
        }

        // phase 3
        Location center3 = this.plugin.getGameManager().getPhaseSpawn(GameStates.GameState.PHASE3);
        int radius3 = nbTeam + 90;
        Location[] locations3 = MathsUtils.getCircle(center3, radius3, nbTeam);

        for (Team team : this.getTeamStates().getAllTeams()) {
            if (team != null) {
                this.getTeamStates().setTeamSpawnPerPhase(team.getName(), GameStates.GameState.PHASE3, locations3[this.getTeamStates().getTeamIndex(team.getName())]);
            }
        }

    }

    // getting all team's spawn for a given phase
    public Location[] getTeamSpawns(GameStates.GameState state) {
        int nbTeam = this.getTeamStates().getAllTeams().size();
        Location[] locations = new Location[nbTeam];
        int i = 0;
        for (Team team : this.getTeamStates().getAllTeams()) {
            if (team != null) {
                locations[i] = this.getTeamStates().getTeamSpawnLocation(team.getName(), state);
                i++;
            }
        }
        return locations;
    }


    // teleport to team spawn
    public void teleportToTeamSpawn(GameStates.GameState state) {
        for (Team team : this.getTeamStates().getAllTeams()) {
            if (team != null) {
                for (String playerName : team.getEntries()) {
                    Player player = Bukkit.getPlayer(playerName);
                    if (player != null) {
                        Bukkit.getPlayer(playerName).teleport(this.teamsStates.getTeamSpawnLocation(team.getName(), state));
                    }
                }
            }
        }
    }

    public void reset() {
        teamsStates.getAllTeams().stream().forEach(team -> teamsStates.resetTeam(team.getName()));
    }
}
