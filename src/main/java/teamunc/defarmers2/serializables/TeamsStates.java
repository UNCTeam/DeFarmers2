package teamunc.defarmers2.serializables;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scoreboard.Team;
import teamunc.defarmers2.customsItems.CustomItem;
import teamunc.defarmers2.customsItems.ui_menu_Items.CustomUIItem;
import teamunc.defarmers2.managers.CustomItemsManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class TeamsStates implements Serializable {
    private final ArrayList<TeamInfo> teamsInfos = new ArrayList<>();


    // number of the last team that died in the game (used to determine the winner) (1 = first team dead, 2 = second team dead, etc.)
    private int lastScore = 0;

    /// TEAM MONEY ///
    public void setTeamMoney(String teamID, int money){
        for (TeamInfo teamInfo : teamsInfos) {
            if (teamInfo.getTeamID().equals(teamID)) {
                teamInfo.setMoney(money);
            }
        }
    }
    public int getTeamMoney(String teamID){
        for (TeamInfo teamInfo : teamsInfos) {
            if (teamInfo.getTeamID().equals(teamID)) {
                return teamInfo.getMoney();
            }
        }
        return 0;
    }
    public void addTeamMoney(String teamID, int money){
        for (TeamInfo teamInfo : teamsInfos) {
            if (teamInfo.getTeamID().equals(teamID)) {
                teamInfo.setMoney(teamInfo.getMoney() + money);
            }
        }
    }
    public void removeTeamMoney(String teamID, int money){
        for (TeamInfo teamInfo : teamsInfos) {
            if (teamInfo.getTeamID().equals(teamID)) {
                teamInfo.setMoney(teamInfo.getMoney() - money);
            }
        }
    }

    /// TEAMS SCORE ///
    public int getTeamScore(String teamID){
        for (TeamInfo team : teamsInfos) {
            if (team.getTeamID().equals(teamID)) {
                return team.getScore();
            }
        }
        return 0;
    }
    public void setTeamScore(String teamID, int score){
        for (TeamInfo teamInfo : teamsInfos) {
            if (teamInfo.getTeamID().equals(teamID)) {
                teamInfo.setScore(score);
            }
        }
    }
    public void addTeamScore(String teamID, int score){
        for (TeamInfo team : teamsInfos) {
            if (team.getTeamID().equals(teamID)) {
                team.setScore(team.getScore() + score);
            }
        }
    }
    public void removeTeamScore(String teamID, int score) {
        for (TeamInfo team : teamsInfos) {
            if (team.getTeamID().equals(teamID)) {
                team.setScore(team.getScore() - score);
            }
        }
    }

    /// MANAGE TEAMS ///
    public void resetTeam(String teamID){
        this.teamsInfos.removeIf(team -> team.getTeamID().equals(teamID));
    }
    public boolean isTeamInit(String teamID){
        return teamsInfos.stream().anyMatch(team -> team.getTeamID().equals(teamID));
    }
    public Set<Team> getAllTeams(){
        return Bukkit.getScoreboardManager().getMainScoreboard().getTeams();
    }
    public void addTeam(String teamID, int money, int score, boolean isDead, HashMap<String, Integer> artefacts, HashMap<String, Integer> mobs, HashMap<GameStates.GameState, Location> spawnPerPhase, ArrayList<UUID> customMobsAssociation){
        teamsInfos.add(new TeamInfo(teamID, score, money, isDead, artefacts, mobs, spawnPerPhase, customMobsAssociation));
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
    public void setDeadTeam(String teamName) {
        for (TeamInfo team : teamsInfos) {
            if (team.getTeamID().equals(teamName)) {
                team.setDead(true);
            }
        }
    }

    public boolean isTeamDead(String name) {
        for (TeamInfo team : teamsInfos) {
            if (team.getTeamID().equals(name)) {
                return team.isDead();
            }
        }
        return false;
    }

    /// TEAMS LOCATIONS ///
    public Location getTeamSpawnLocation(String teamID, GameStates.GameState gameState){
        for (TeamInfo teamInfo : teamsInfos) {
            if (teamInfo.getTeamID().equals(teamID)) {
                return teamInfo.getSpawnLocationForAPhase(gameState);
            }
        }
        return null;
    }
    public Location[] getAllSpawnLocationsForAPhase(GameStates.GameState gameState){
        ArrayList<Location> locations = new ArrayList<>();
        for (TeamInfo teamInfo : teamsInfos) {
            locations.add(teamInfo.getSpawnLocationForAPhase(gameState));
        }
        return locations.toArray(new Location[locations.size()]);
    }
    public void setTeamSpawnPerPhase(String teamID, GameStates.GameState phase, Location location){
        for (TeamInfo teamInfo : teamsInfos) {
            if (teamInfo.getTeamID().equals(teamID)) {
                teamInfo.setSpawnLocationForAPhase(phase, location);
            }
        }
    }

    /// TEAMS CUSTOM ITEMS ///
    public void addArtefactInTeam(String name, CustomUIItem customUIItem) {
        for (TeamInfo teamInfo : teamsInfos) {
            if (teamInfo.getTeamID().equals(name)) {
                teamInfo.addArtefact(customUIItem.getCustomType(), customUIItem.getAmount());
            }
        }
    }
    public HashMap<String, Integer> getTeamsArtefacts(Team team) {
        for (TeamInfo teamInfo : teamsInfos) {
            if (teamInfo.getTeamID().equals(team.getName())) {
                return teamInfo.getArtefacts();
            }
        }
        return null;
    }
    public CustomItem[] getArtefactsInTeam(String name) {
        HashMap<String, Integer> artefacts = null;
        for (TeamInfo teamInfo : teamsInfos) {
            if (teamInfo.getTeamID().equals(name)) {
                artefacts = teamInfo.getArtefacts();
            }
        }
        if (artefacts == null) return null;

        CustomItem[] customItems = new CustomItem[artefacts.size()];
        int i = 0;
        for (String customType : artefacts.keySet()) {
            CustomItem customItem = CustomItemsManager.getCustomItem(customType);
            customItem.setAmount(artefacts.get(customType));
            customItems[i] = customItem;
            i++;
        }
        return customItems;
    }

    /// TEAM MOBS ///
    public void addSelectedMobInTeam(String name, CustomUIItem customUIItem) {
        for (TeamInfo teamInfo : teamsInfos) {
            if (teamInfo.getTeamID().equals(name)) {
                teamInfo.addMob(customUIItem.getCustomType(), customUIItem.getAmount());
            }
        }
    }
    public HashMap<String, Integer> getTeamsSelectedMobs(Team team) {
        for (TeamInfo teamInfo : teamsInfos) {
            if (teamInfo.getTeamID().equals(team.getName())) {
                return teamInfo.getMobs();
            }
        }
        return null;
    }

    /// TEAM MOBS SPAWNED ///
    public ArrayList<UUID> getMobsOfATeam(String teamName) {
        for (TeamInfo teamInfo : teamsInfos) {
            if (teamInfo.getTeamID().equals(teamName)) {
                return teamInfo.getCustomMobsAssociation();
            }
        }
        return null;
    }
    public void addCustomMob(String teamName, UUID mob) {
        for (TeamInfo teamInfo : teamsInfos) {
            if (teamInfo.getTeamID().equals(teamName)) {
                teamInfo.addCustomMobAssociation(mob);
            }
        }
    }
    public void removeCustomMob(String teamName, UUID mob) {
        for (TeamInfo teamInfo : teamsInfos) {
            if (teamInfo.getTeamID().equals(teamName)) {
                teamInfo.removeCustomMobAssociation(mob);
            }
        }
    }

    /// OTHER ///
    public int getLastScore() {
        return lastScore;
    }
    public void setLastScore(int number) {
        this.lastScore = number;
    }
}
