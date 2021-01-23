package me.ihellsmc.infinitystones.stone.gui;

import me.ihellsmc.infinitystones.stone.Stone;
import me.ihellsmc.infinitystones.util.general.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static me.ihellsmc.infinitystones.util.general.Color.color;

public class StonesGUI {

    private final Inventory inventory;

    public StonesGUI(Player player) {
        this.inventory = Bukkit.getServer().createInventory(player, 45, color("&5Infinity Stones"));
        this.populateItems();
        player.openInventory(this.inventory);
    }

    private void populateItems() {
        this.inventory.setItem(3, Stone.REALITY.getItemStack(true));
        this.inventory.setItem(5, Stone.SPACE.getItemStack(true));
        this.inventory.setItem(20, Stone.MIND.getItemStack(true));
        this.inventory.setItem(24, Stone.POWER.getItemStack(true));
        this.inventory.setItem(39, Stone.TIME.getItemStack(true));
        this.inventory.setItem(41, Stone.SOUL.getItemStack(true));

        ItemStack all = new ItemBuilder(Material.NETHER_STAR)
                .name("&b&lALL STONES")
                .lore(Arrays.asList(
                        "&7View the legendary perks you will receive when you",
                        "&7have applied all of the stones to an item.",
                        " ",
                        "&7Perks:",
                        "&7  * Double jump ability",
                        "&7  * Permanent 60% damage resistance",
                        "&7  * Permanent speed effect"))
                .build();

        this.inventory.setItem(22, all);

        ItemStack holder = new ItemBuilder(Material.STAINED_GLASS_PANE)
                .color(DyeColor.GRAY)
                .name("&f")
                .build();

        List<ItemStack> contents = new ArrayList<>();

        for (ItemStack item : this.inventory.getContents()) {
            if (item == null || item.getType() == Material.AIR)
                item = holder;

            contents.add(item);
        }

        this.inventory.setContents(contents.toArray(new ItemStack[0]));
    }

}
