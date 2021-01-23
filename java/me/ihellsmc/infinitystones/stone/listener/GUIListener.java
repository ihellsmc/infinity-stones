package me.ihellsmc.infinitystones.stone.listener;

import me.ihellsmc.infinitystones.stone.Stone;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

import static me.ihellsmc.infinitystones.util.general.Color.color;

public class GUIListener implements Listener {

    private final List<ChatColor> order = Arrays.asList(ChatColor.RED, ChatColor.GOLD, ChatColor.YELLOW, ChatColor.GREEN, ChatColor.BLUE, ChatColor.LIGHT_PURPLE);

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if ((e.getWhoClicked() instanceof Player) && e.getClickedInventory() != null && e.getClickedInventory().getTitle() != null &&
                e.getClickedInventory().getTitle().equals(color("&5Infinity Stones"))) {
            /* Infinity Stones GUI */

            e.setCancelled(true);

            if (e.getCurrentItem() == null || !e.getCurrentItem().hasItemMeta() || !e.getCurrentItem().getItemMeta().hasLocalizedName()) return;

            Player player = (Player) e.getWhoClicked();

            for (Stone stone : Stone.values()) {
                if (e.getCurrentItem().getItemMeta().getLocalizedName().equals(stone.getName())) {
                    player.sendMessage(color("&8[&5Stones&8] &7Received " + stone.getName() + "&7 stone."));
                    player.getInventory().addItem(stone.getItemStack(false));
                    player.closeInventory();
                }
            }

        } else if ((e.getWhoClicked() instanceof Player) && e.getClickedInventory() == (e.getWhoClicked().getInventory())) {
            /* Own Inventory */

            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;

            if (e.getCursor() != null) {

                if (e.getCursor().hasItemMeta() && e.getCursor().getItemMeta().hasLocalizedName()) {
                    Stone stone = null;
                    for (Stone s : Stone.values()) {
                        if (e.getCursor().getItemMeta().getLocalizedName().equals(s.getName())) stone = s;
                    }

                    if (stone != null) {

                        ItemMeta meta = e.getCurrentItem().hasItemMeta() ? e.getCurrentItem().getItemMeta() : Bukkit.getItemFactory().getItemMeta(e.getCurrentItem().getType());
                        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
                        for (String s : lore) {
                            if (s.contains(stone.getName())) {
                                e.getWhoClicked().sendMessage(color("&8[&5Stones&8] &7This is already harnessing the " + stone.getName() + " &7stone!"));
                                return;
                            }
                        }

                        e.setCancelled(true);

                        if (lore.isEmpty()) lore.add(color("&7Harnessing the:"));
                        lore.add(color("&7  * " + stone.getName() + " Stone"));
                        meta.setLore(lore);

                        meta.setDisplayName(color(getName(stone, getStones(e.getCurrentItem())) + " &f" + getMaterialName(e.getCurrentItem().getType())));

                        e.getCurrentItem().setItemMeta(meta);

                        e.getWhoClicked().sendMessage(color("&8[&5Stones&8] &7Successfully applied the " + stone.getName() + " &7stone!"));

                        e.setCursor(null);

                    }

                }

            }

        }
    }

    private String getName(Stone toApply, Set<Stone> otherStones) {
        List<Stone> stones = new ArrayList<>(otherStones); stones.add(toApply);
        List<ChatColor> colors = new ArrayList<>();

        for (Stone stone : stones) {
            switch (stone) {
                case REALITY:
                    colors.add(ChatColor.RED);
                    continue;
                case SOUL:
                    colors.add(ChatColor.GOLD);
                    continue;
                case MIND:
                    colors.add(ChatColor.YELLOW);
                    continue;
                case TIME:
                    colors.add(ChatColor.GREEN);
                    continue;
                case SPACE:
                    colors.add(ChatColor.BLUE);
                    continue;
                case POWER:
                    colors.add(ChatColor.LIGHT_PURPLE);
            }
        }

        List<ChatColor> sorted = new ArrayList<>();
        for (ChatColor c : order) {
            if (colors.contains(c)) sorted.add(c);
        }

        switch (sorted.size()) {
            case 1:
                return sorted.get(0) + "Infinity";
            case 2:
                return sorted.get(0) + "Infi" + sorted.get(1) + "nity";
            case 3:
                return sorted.get(0) + "Inf" + sorted.get(1) + "in" + sorted.get(2) + "ity";
            case 4:
                return sorted.get(0) + "In" + sorted.get(1) + "fi" + sorted.get(2) + "ni" + sorted.get(3) + "ty";
            case 5:
                return sorted.get(0) + "I" + sorted.get(1) + "nf" + sorted.get(2) + "in" + sorted.get(3) + "it" + sorted.get(4) + "y";
            case 6:
                return color("&cIn&6f&ei&an&9i&dty");
        }

        return null;
    }

    private String getMaterialName(Material mat) {
        StringBuilder toReturn = new StringBuilder();
        for (String s : mat.toString().split("_")) {
            toReturn.append(s.substring(0, 1).toUpperCase()).append(s.substring(1).toLowerCase()).append(" ");
        }
        return toReturn.toString();
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

}
