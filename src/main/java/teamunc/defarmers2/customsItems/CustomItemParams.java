package teamunc.defarmers2.customsItems;

import org.bukkit.entity.Player;

public class CustomItemParams {

    private Player player;

    public CustomItemParams(Player player) {
        this.player = player;


    }

    public Player getPlayer() {
        return player;
    }
}
