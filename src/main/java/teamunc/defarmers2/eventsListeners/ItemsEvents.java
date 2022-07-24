package teamunc.defarmers2.eventsListeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import teamunc.defarmers2.Defarmers2;

public class ItemsEvents extends AbstractListener {

    public ItemsEvents(Defarmers2 plugin) {
        super(plugin);
    }

    @EventHandler
    public void onItemUsed(PlayerInteractEvent e) {

        if (e.getAction() == Action.RIGHT_CLICK_AIR) {
            // variables
            ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
            Player player = e.getPlayer();

            if (!this.plugin.getGameManager().getCustomItemsManager().isCustomItem(item)) return;

            // action of the item
            this.plugin.getGameManager().getCustomItemsManager().actionOfCustomItem(player, item);

        }
    }
}
