package teamunc.defarmers2.serializables;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scoreboard.Team;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

public class TeamsStates implements Serializable {
    private HashMap<String,Integer> teamsScores = new HashMap<>();
    private HashMap<String,Integer> teamsMoneys = new HashMap<>();
    private HashMap<String,HashMap<GameStates.GameState, Location>> teamsSpawnPerPhase = new HashMap<>();

    //set team scores
    public void setTeamScore(String teamID, int score){
        teamsScores.put(teamID, score);
    }

    //set team money
    public void setTeamMoney(String teamID, int money){
        teamsMoneys.put(teamID, money);
    }

    //get team score
    public int getTeamScore(String teamID){
        return teamsScores.get(teamID);
    }

    //get team money
    public int getTeamMoney(String teamID){
        return teamsMoneys.get(teamID);
    }

    //add team score
    public void addTeamScore(String teamID, int score){
        teamsScores.put(teamID, teamsScores.get(teamID) + score);
    }

    //add team money
    public void addTeamMoney(String teamID, int money){
        teamsMoneys.put(teamID, teamsMoneys.get(teamID) + money);
    }

    // set team spawn per phase
    public void setTeamSpawnPerPhase(String teamID, GameStates.GameState phase, Location location){
        if(!teamsSpawnPerPhase.containsKey(teamID)){
            teamsSpawnPerPhase.put(teamID, new HashMap<>());
        }
        teamsSpawnPerPhase.get(teamID).put(phase, location);
    }

    //subtract team score
    public void removeTeamScore(String teamID, int score){
        teamsScores.put(teamID, teamsScores.get(teamID) - score);
    }

    //subtract team money
    public void removeTeamMoney(String teamID, int money){
        teamsMoneys.put(teamID, teamsMoneys.get(teamID) - money);
    }

    //reset team scores
    public void resetTeamScores(){
        teamsScores.clear();
    }

    //reset team moneys
    public void resetTeamMoneys(){
        teamsMoneys.clear();
    }

    //init team
    public void initTeam(String teamID, int score, int money, HashMap<GameStates.GameState, Location> SpawnPerPhase){
        teamsScores.put(teamID, score);
        teamsMoneys.put(teamID, money);
        teamsSpawnPerPhase.put(teamID, SpawnPerPhase);
    }

    //reset team
    public void resetTeam(String teamID){
        teamsScores.remove(teamID);
        teamsMoneys.remove(teamID);
        teamsSpawnPerPhase.remove(teamID);
    }

    //get all teams
    public Set<Team> getAllTeams(){
        return Bukkit.getScoreboardManager().getMainScoreboard().getTeams();
    }

    //get team spawn location
    public Location getTeamSpawnLocation(String teamID, GameStates.GameState gameState){
        return teamsSpawnPerPhase.get(teamID).get(gameState);
    }

    public int getTeamIndex(String name) {
        int index = 0;
        for (Team team : getAllTeams()) {
            if (team.getName().equals(name)) {
                return index;
            }
            index++;
        }
        return -1;
    }
}
