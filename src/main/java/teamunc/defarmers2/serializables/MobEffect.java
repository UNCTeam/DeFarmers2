package teamunc.defarmers2.serializables;

import teamunc.defarmers2.mobs.EnumMobStatue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public class MobEffect implements Serializable {
    private final EnumMobStatue statue;
    private int timeLeft;
    private final ArrayList<UUID> mobs;

    public MobEffect(EnumMobStatue statue, int timeLeft, ArrayList<UUID> mobs) {
        this.statue = statue;
        this.timeLeft = timeLeft;
        this.mobs = mobs;
        this.statue.init(mobs);
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
        System.out.println("Effect on " + mobs + " Time left : " + timeLeft);
        statue.action(mobs);

        timeLeft--;
        return false;
    }

    public void end() {
        statue.end(mobs);
        mobs.clear();
    }
}
