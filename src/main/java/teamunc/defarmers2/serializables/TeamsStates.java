package teamunc.defarmers2.serializables;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scoreboard.Team;
import teamunc.defarmers2.customsItems.CustomItem;
import teamunc.defarmers2.customsItems.ui_menu_Items.CustomUIItem;
import teamunc.defarmers2.managers.CustomItemsManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

public class TeamsStates implements Serializable {
    private final HashMap<String,Integer> teamsScores = new HashMap<>();
    private final HashMap<String,Integer> teamsMoneys = new HashMap<>();
    private final HashMap<String,HashMap<String,Integer>> teamsArtefacts = new HashMap<>();
    private final HashMap<String,HashMap<String,Integer>> teamsMobs = new HashMap<>();
    private final HashMap<String,HashMap<GameStates.GameState, Location>> teamsSpawnPerPhase = new HashMap<>();

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

    // team init ?
    public boolean isTeamInit(String teamID){
        return teamsSpawnPerPhase.containsKey(teamID);
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

    //reset team
    public void resetTeam(String teamID){
        teamsScores.remove(teamID);
        teamsMoneys.remove(teamID);
        teamsSpawnPerPhase.remove(teamID);
        teamsArtefacts.remove(teamID);
        teamsMobs.remove(teamID);
    }

    //get all teams
    public Set<Team> getAllTeams(){
        return Bukkit.getScoreboardManager().getMainScoreboard().getTeams();
    }

    //get team spawn location
    public Location getTeamSpawnLocation(String teamID, GameStates.GameState gameState){
        return teamsSpawnPerPhase.get(teamID).get(gameState);
    }

    public Location[] getAllSpawnLocationsForAPhase(GameStates.GameState gameState){
        Location[] locations = new Location[teamsSpawnPerPhase.size()];
        int i = 0;
        for(String teamID : teamsSpawnPerPhase.keySet()){
            locations[i] = teamsSpawnPerPhase.get(teamID).get(gameState);
            i++;
        }
        return locations;
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

    public void addArtefactInTeam(String name, CustomUIItem customUIItem) {
        addStuffToTeam(name, customUIItem, teamsArtefacts);
    }

    private void addStuffToTeam(String name, CustomUIItem customUIItem, HashMap<String, HashMap<String, Integer>> teamsList) {
        if(!teamsList.containsKey(name)){
            teamsList.put(name, new HashMap<>());
        }
        if (!teamsList.get(name).containsKey(customUIItem.getCustomType())) {
            teamsList.get(name).put(customUIItem.getCustomType(), 0);
        }

        teamsList.get(name).put(customUIItem.getCustomType(), teamsList.get(name).get(customUIItem.getCustomType()) + 1);
    }

    public void addMobInTeam(String name, CustomUIItem customUIItem) {
        // if team doesn't exist, create it
        addStuffToTeam(name, customUIItem, teamsMobs);
    }

    public HashMap<String, Integer> getTeamsArtefacts(Team team) {
        return teamsArtefacts.get(team.getName());
    }

    public CustomItem[] getArtefactsInTeam(String name) {
        CustomItem[] customItems = new CustomItem[teamsArtefacts.get(name).size()];
        int i = 0;
        for (String customType : teamsArtefacts.get(name).keySet()) {
            CustomItem customItem = CustomItemsManager.getInstance().getCustomItem(customType);
            customItem.setAmount(teamsArtefacts.get(name).get(customType));
            customItems[i] = customItem;
            i++;
        }
        return customItems;
    }

    public HashMap<String, Integer> getTeamsMobs(Team team) {
        return teamsMobs.get(team.getName());
    }

    public void initArtefactsList(String name) {
        teamsArtefacts.put(name, new HashMap<>());
    }

    public void initMobsList(String name) {
        teamsMobs.put(name, new HashMap<>());
    }
}
