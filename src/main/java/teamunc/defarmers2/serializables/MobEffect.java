package teamunc.defarmers2.serializables;

import teamunc.defarmers2.mobs.EnumMobStatue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class MobEffect implements Serializable {
    private final EnumMobStatue statue;
    private int timeLeft;
    private final ArrayList<UUID> mobs;

    public MobEffect(EnumMobStatue statue, int timeLeft, ArrayList<UUID> mobs, UUID triggeringPlayer) {
        this.statue = statue;
        this.timeLeft = timeLeft;
        this.mobs = new ArrayList<>(mobs);
        this.statue.init(mobs, triggeringPlayer);
    }

    /**
     * @return if the effect is timed out
     * true = timed out, need to be removed
     */
    public boolean action() {
        if (timeLeft == 0) {
            this.end();
            return true;
        }
        statue.action(mobs);

        timeLeft--;
        return false;
    }

    public void end() {
        statue.end(mobs);
        mobs.clear();
    }
}
