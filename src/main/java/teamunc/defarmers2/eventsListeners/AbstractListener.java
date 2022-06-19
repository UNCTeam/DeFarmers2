package teamunc.defarmers2.eventsListeners;

import org.bukkit.event.Listener;
import teamunc.defarmers2.Defarmers2;

public abstract class AbstractListener implements Listener {

    protected Defarmers2 plugin;

    AbstractListener(Defarmers2 plugin) {
        super();
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
