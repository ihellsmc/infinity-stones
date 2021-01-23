package me.ihellsmc.infinitystones.stone.listener;

import me.ihellsmc.infinitystones.InfinityStones;
import me.ihellsmc.infinitystones.stone.Stone;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static me.ihellsmc.infinitystones.util.general.Color.color;

public class StoneListener implements Listener {

    private final InfinityStones instance;

    private final Set<UUID> spaceCooldown = new HashSet<>();
    private final Set<UUID> timeCooldown = new HashSet<>();

    private final HashMap<UUID, Double> healthCache = new HashMap<>();

    public StoneListener(InfinityStones instance) {
        this.instance = instance;

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(instance, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (healthCache.containsKey(player.getUniqueId())) {
                    healthCache.replace(player.getUniqueId(), player.getHealth());
                } else {
                    healthCache.put(player.getUniqueId(), player.getHealth());
                }
            }
        }, 0, 30 * 20);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(instance, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().hasItemMeta()) {
                    player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
                    if (getStones(player.getInventory().getItemInMainHand()).size() == 6) {
                        player.setAllowFlight(true);
                        player.removePotionEffect(PotionEffectType.SPEED);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 8 * 20, 0), false);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 8 * 20, 2), false);
                    } else {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 8 * 20, 0), false);
                    }
                }
            }
        }, 0, 3 * 20);
    }

    /*
    @ Reality Stone: 15% chance on hit to launch a fireball towards your enemy
    @ Mind Stone:    7.5% chance to corrupt a mob on hit, forcing them to kill themselves instantly
    @ Mind Stone:    Resistance to the Mind Stone
    @ Power Stone:   15% chance on hit to throw an enemy across the map and into the air
    @ Power Stone:   5% chance to launch all nearby enemies across the map and into the air
    @ Soul Stone:    20% chance on hit to regain a heart of health
    @ Soul Stone:    Permanent 20% damage resistance
    @ Space Stone:   Right click to teleport to the block you're looking to within 45 blocks (10 second cooldown)
    @ Time Stone:    Press Q to regain your health to a state that it was 30 seconds previously (10 second cooldown)

    @ All 6 Stones:  Double jump (3 second cooldown)
    @ All 6 Stones:  Permanent 60% damage resistance
    @ All 6 Stones:  Permanent speed effect
     */

    ThreadLocalRandom random = ThreadLocalRandom.current();

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {

        if (!(e.getDamager() instanceof Player)) return;
        Player player = (Player) e.getDamager();

        if (!(player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().hasItemMeta())) return;

        for (Stone stone : getStones(player.getInventory().getItemInMainHand())) {

            switch (stone) {
                case REALITY:
                    if (random.nextDouble() <= 0.15) {
                        Fireball fireball = player.launchProjectile(Fireball.class, player.getLocation().getDirection());
                        fireball.setVelocity(fireball.getVelocity().multiply(2));
                    }
                    continue;
                case MIND:
                    if (random.nextDouble() <= 0.075) {
                        if (e.getEntity() instanceof Damageable) {

                            if (e.getEntity() instanceof Player) {
                                if (((Player) e.getEntity()).getInventory().getItemInMainHand() != null && ((Player) e.getEntity()).getInventory().getItemInMainHand().hasItemMeta()) {
                                    if (getStones(((Player) e.getEntity()).getInventory().getItemInMainHand()).contains(Stone.MIND)) continue;
                                }
                            }

                            player.playEffect(e.getEntity().getLocation(), Effect.MAGIC_CRIT, 1);
                            ((Damageable) e.getEntity()).setHealth(0.0);
                        }
                    }
                    continue;
                case POWER:
                    if (random.nextDouble() <= 0.05) {
                        for (Entity entity : player.getNearbyEntities(7, 7, 7)) {
                            Vector v = entity.getLocation().toVector().add(player.getLocation().toVector().multiply(-1)).multiply(1.2).setY(1);
                            Bukkit.getScheduler().scheduleSyncDelayedTask(instance, () -> entity.setVelocity(v), 1);
                        }
                        player.playEffect(player.getLocation(), Effect.EXPLOSION_HUGE, 1);
                    } else if (random.nextDouble() <= 0.2) {
                        Vector v = e.getEntity().getLocation().toVector().add(player.getLocation().toVector().multiply(-1)).multiply(1.2).setY(1);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(instance, () -> e.getEntity().setVelocity(v), 1);
                        player.playEffect(player.getLocation(), Effect.EXPLOSION, 1);
                    }
                    continue;
                case SOUL:
                    if (random.nextDouble() <= 0.20) {
                        if (player.getHealth() < 19) player.setHealth(player.getHealth() + 1);
                        player.playEffect(player.getLocation(), Effect.HEART, 1);
                    }
            }
        }

    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {

        if (!(e.getPlayer().getInventory().getItemInMainHand() != null && e.getPlayer().getInventory().getItemInMainHand().hasItemMeta())) return;

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (getStones(e.getPlayer().getInventory().getItemInMainHand()).contains(Stone.SPACE)) {

                Block block = e.getPlayer().getTargetBlock(null, 45);

                if (spaceCooldown.contains(e.getPlayer().getUniqueId())) {
                    e.getPlayer().sendMessage(color("&8[&5Stones&8] &7There is a &510&7 second cooldown on this ability."));
                } else {

                    Location location = new Location(block.getWorld(), block.getX() + 0.5, block.getY() + 1, block.getZ() + 0.5, e.getPlayer().getLocation().getYaw(), e.getPlayer().getLocation().getPitch());

                    while (location.getBlock().getType() != Material.AIR || location.getBlock().getRelative(0, 1, 0).getType() != Material.AIR) {
                        if (location.getBlockY() == 256) return;
                        location.add(0, 1, 0);
                    }

                    e.getPlayer().teleport(location);
                    e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ENDERMEN_TELEPORT, 1, 1);
                    e.getPlayer().playEffect(e.getPlayer().getLocation(), Effect.WITCH_MAGIC, 1);

                    spaceCooldown.add(e.getPlayer().getUniqueId());
                    Bukkit.getScheduler().scheduleAsyncDelayedTask(instance, () -> {
                        spaceCooldown.remove(e.getPlayer().getUniqueId());
                        e.getPlayer().sendMessage(color("&8[&5Stones&8] &7Your " + Stone.SPACE.getName() + " Stone &7ability is no longer on cooldown!"));
                    }, 10 * 20);

                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        ItemStack item = e.getItemDrop().getItemStack();
        if (item.hasItemMeta()) {

            if (getStones(item).contains(Stone.TIME)) {

                e.setCancelled(true);

                if (timeCooldown.contains(e.getPlayer().getUniqueId())) {
                    e.getPlayer().sendMessage(color("&8[&5Stones&8] &7There is a &530&7 second cooldown on this ability."));
                } else {

                    if (healthCache.containsKey(e.getPlayer().getUniqueId())) {

                        e.getPlayer().setHealth(healthCache.get(e.getPlayer().getUniqueId()));
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 2);
                        e.getPlayer().playEffect(e.getPlayer().getLocation(), Effect.HEART, 1);

                        timeCooldown.add(e.getPlayer().getUniqueId());
                        Bukkit.getScheduler().scheduleAsyncDelayedTask(instance, () -> {
                            timeCooldown.remove(e.getPlayer().getUniqueId());
                            e.getPlayer().sendMessage(color("&8[&5Stones&8] &7Your " + Stone.TIME.getName() + " Stone &7ability is no longer on cooldown!"));
                        }, 10 * 20);

                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent e) {
        Player player = e.getPlayer();
        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) return;

        e.setCancelled(true);
        player.setFlying(false);
        player.setAllowFlight(false);

        if (player.getInventory().getItemInMainHand() != null && player.getInventory().getItemInMainHand().hasItemMeta()) {
            if (getStones(player.getInventory().getItemInMainHand()).size() == 6) {
                player.setVelocity(player.getLocation().getDirection().multiply(1.2).setY(0.8));
                player.playSound(player.getLocation(), Sound.BLOCK_FIRE_EXTINGUISH, 1, 2);
                player.playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);
            }
        }

    }

    private Set<Stone> getStones(ItemStack item) {
        Set<Stone> toReturn = new HashSet<>();
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            for (Stone stone : Stone.values()) {
                for (String str : item.getItemMeta().getLore()) {
                    if (str.contains(stone.getName())) toReturn.add(stone);
                }
            }
        }
        return toReturn;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) { healthCache.remove(e.getPlayer().getUniqueId()); }

}
