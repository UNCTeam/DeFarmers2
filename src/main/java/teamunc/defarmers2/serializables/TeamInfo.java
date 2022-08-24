package teamunc.defarmers2.serializables;

import org.bukkit.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class TeamInfo implements Serializable {
    private String teamID;
    private int score;
    private int money;
    private int classement;
    private boolean isDead;
    private HashMap<String, Integer> artefacts;
    private HashMap<String, Integer> mobs;
    private HashMap<GameStates.GameState, Location> spawnPerPhase;
    private ArrayList<UUID> customMobsAssociation;

    public TeamInfo(String teamID, int score, int money, boolean isDead, HashMap<String, Integer> artefacts, HashMap<String, Integer> mobs, HashMap<GameStates.GameState, Location> spawnPerPhase, ArrayList<UUID> customMobsAssociation) {
        this.teamID = teamID;
        this.score = score;
        this.money = money;
        this.isDead = isDead;
        this.artefacts = artefacts;
        this.mobs = mobs;
        this.spawnPerPhase = spawnPerPhase;
        this.customMobsAssociation = customMobsAssociation;
        this.classement = 0;
    }

    public int getClassement() {
        return classement;
    }

    public void setClassement(int classement) {
        this.classement = classement;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public void setArtefacts(HashMap<String, Integer> artefacts) {
        this.artefacts = artefacts;
    }

    public void addArtefact(String artefact, int amount) {
        if (artefacts.containsKey(artefact)) {
            artefacts.put(artefact, artefacts.get(artefact) + amount);
        } else {
            artefacts.put(artefact, amount);
        }
    }

    public void setMobs(HashMap<String, Integer> mobs) {
        this.mobs = mobs;
    }

    public void addMob(String mob, int amount) {
        if (mobs.containsKey(mob)) {
            mobs.put(mob, mobs.get(mob) + amount);
        } else {
            mobs.put(mob, amount);
        }
    }

    public void setSpawnPerPhase(HashMap<GameStates.GameState, Location> spawnPerPhase) {
        this.spawnPerPhase = spawnPerPhase;
    }

    public void setCustomMobsAssociation(ArrayList<UUID> customMobsAssociation) {
        this.customMobsAssociation = customMobsAssociation;
    }

    public String getTeamID() {
        return teamID;
    }

    public int getScore() {
        return score;
    }

    public int getMoney() {
        return money;
    }

    public boolean isDead() {
        return isDead;
    }

    public HashMap<String, Integer> getArtefacts() {
        return artefacts;
    }

    public HashMap<String, Integer> getMobs() {
        return mobs;
    }

    public Location getSpawnLocationForAPhase(GameStates.GameState gameState) {
        return spawnPerPhase.get(gameState);
    }

    public void setSpawnLocationForAPhase(GameStates.GameState gameState, Location location) {
        if (spawnPerPhase.containsKey(gameState)) {
            spawnPerPhase.put(gameState, location);
        } else {
            spawnPerPhase.put(gameState, location);
        }
    }

    public HashMap<GameStates.GameState, Location> getSpawnPerPhase() {
        return spawnPerPhase;
    }

    public ArrayList<UUID> getCustomMobsAssociation() {
        return customMobsAssociation;
    }

    public void addCustomMobAssociation(UUID mob) {
        customMobsAssociation.add(mob);
    }

    public void removeCustomMobAssociation(UUID mob) {
        customMobsAssociation.remove(mob);
    }
}
