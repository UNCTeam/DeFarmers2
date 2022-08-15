package teamunc.defarmers2.customsItems;

import org.bukkit.entity.Player;

public class CustomItemParams {

    private Player player;
    private int durabilityUsed = 1;

    public CustomItemParams(Player player) {
        this.player = player;
    }

    public CustomItemParams(Player player, int durabilityUsed) {
        this.player = player;
        this.durabilityUsed = durabilityUsed;
    }

    public int getDurabilityUsed() {
        return durabilityUsed;
    }

    public Player getPlayer() {
        return player;
    }
}
