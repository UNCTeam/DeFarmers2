package teamunc.defarmers2.customsItems;

import com.sk89q.worldedit.util.Direction;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import teamunc.defarmers2.utils.worldEdit.ApiWorldEdit;
import teamunc.defarmers2.utils.worldEdit.MathsUtils;

import java.util.List;

public class FrozenItem extends CustomItem {

    public FrozenItem() {
        super("Frozen", 7, "FROZEN",1);
    }

    @Override
    public void onClick(CustomItemParams params) {
        Player player = params.getPlayer();

        ItemStack potion = new ItemStack(Material.LINGERING_POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.addCustomEffect(new PotionEffect(PotionEffectType.SLOW, 150, 2), true);
        potion.setItemMeta(meta);

        Location location = MathsUtils.getNextLocation(Direction.DOWN, player.getLocation(), 32, null);

        ApiWorldEdit.generateCircle(3,location,Material.PACKED_ICE);

        ThrownPotion thrownPotion = (ThrownPotion) player.getWorld().spawnEntity(location.clone().add(0,1,0), EntityType.SPLASH_POTION);
        thrownPotion.setItem(potion);
    }
    @Override
    public @NotNull List<String> getDescription() {
        return List.of("§r§7Description", "§r§7juste §cici");
    }

    @Override
    public int getDefaultPrice() {
        return 65;
    }
}
