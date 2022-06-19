package teamunc.defarmers2.serializables;

import org.bukkit.Location;

import java.io.Serializable;

public class GameOptions implements Serializable {
    private int timeForPhase1inSecond;
    private int timeForPhase2inSecond;
    private int timeForPhase3inSecond;
    private Location lobbyLocation;
    private Location phase1LocationCenter;
    private Location phase2LocationCenter;
    private Location phase3LocationCenter;

    public GameOptions(int timeForPhase1inSecond, int timeForPhase2inSecond, int timeForPhase3inSecond, Location lobbyLocation, Location phase1LocationCenter, Location phase2LocationCenter, Location phase3LocationCenter) {
        this.timeForPhase1inSecond = timeForPhase1inSecond;
        this.timeForPhase2inSecond = timeForPhase2inSecond;
        this.timeForPhase3inSecond = timeForPhase3inSecond;
        this.phase1LocationCenter = phase1LocationCenter;
        this.phase2LocationCenter = phase2LocationCenter;
        this.phase3LocationCenter = phase3LocationCenter;
        this.lobbyLocation = lobbyLocation;
    }

    public int getTimeForPhase(GameStates.GameState phase) {
        switch (phase) {
            case PHASE1:
                return timeForPhase1inSecond;
            case PHASE2:
                return timeForPhase2inSecond;
            case PHASE3:
                return timeForPhase3inSecond;
            default:
                return -1;
        }
    }

    public Location getPhase1LocationCenter() {
        return phase1LocationCenter;
    }

    public Location getPhase2LocationCenter() {
        return phase2LocationCenter;
    }

    public Location getPhase3LocationCenter() {
        return phase3LocationCenter;
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }
}
