package me.ihellsmc.infinitystones.forgery.listener;

import me.ihellsmc.infinitystones.util.general.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import static me.ihellsmc.infinitystones.util.general.Color.color;

public class CraftingListener implements Listener {

    /*

    This was meant to be a custom anvil which you could use to remove the infinity stones from your
    item. This is incomplete due to lack of time, aka, I slept too much

     */

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (e.getBlockPlaced().getType() == Material.ANVIL && e.getBlockAgainst().getType() == Material.DIAMOND_BLOCK) {
            e.getBlockPlaced().setType(Material.AIR);
            e.getBlockAgainst().setType(Material.AIR);
            Location loc = e.getBlockPlaced().getLocation();

            loc.getWorld().spawnParticle(Particle.CLOUD, loc, 5);
            loc.getWorld().playSound(loc, Sound.BLOCK_ANVIL_USE, 2F, 0.2F);

            loc.getWorld().dropItem(loc, new ItemBuilder(Material.ANVIL)
                    .name(color("&5Forgery"))
                    .lore(color("&7Place to return your stones from an item."))
                    .addGlow()
                    .build()
            );
        }
    }

}
