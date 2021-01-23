package me.ihellsmc.infinitystones.util.general;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder {

    private ItemStack is;

    public ItemBuilder(Material mat) {
        is = new ItemStack(mat);
    }

    public ItemBuilder(ItemStack is) {
        this.is = is;
    }

    public ItemBuilder amount(int amount) {
        is.setAmount(amount);
        return this;
    }

    public ItemBuilder name(String name) {
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(String name) {
        ItemMeta meta = is.getItemMeta();
        List<String> lore = meta.getLore();

        if (lore == null) {
            lore = new ArrayList<>();
        }

        lore.add(ChatColor.translateAlternateColorCodes('&', name));
        meta.setLore(lore);

        is.setItemMeta(meta);

        return this;
    }

    public ItemBuilder lore(String... lore) {
        List<String> toSet = new ArrayList<>();
        ItemMeta meta = is.getItemMeta();

        for (String string : lore) {
            toSet.add(ChatColor.translateAlternateColorCodes('&', string));
        }

        meta.setLore(toSet);
        is.setItemMeta(meta);

        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        List<String> toSet = new ArrayList<>();
        ItemMeta meta = is.getItemMeta();

        for (String string : lore) {
            toSet.add(ChatColor.translateAlternateColorCodes('&', string));
        }

        meta.setLore(toSet);
        is.setItemMeta(meta);

        return this;
    }

    public ItemBuilder durability(int durability) {
        is.setDurability((short) durability);
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment, int level) {
        is.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment) {
        is.addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemBuilder type(Material material) {
        is.setType(material);
        return this;
    }

    public ItemBuilder clearLore() {
        ItemMeta meta = is.getItemMeta();

        meta.setLore(new ArrayList<>());
        is.setItemMeta(meta);

        return this;
    }

    public ItemBuilder clearEnchantments() {
        for (Enchantment e : is.getEnchantments().keySet()) {
            is.removeEnchantment(e);
        }

        return this;
    }

    public ItemBuilder localisedName(String ln) {
        ItemMeta meta = is.getItemMeta();
        meta.setLocalizedName(ln);
        is.setItemMeta(meta);
        return this;
    }

    public ItemStack build() {
        return is;
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder color(DyeColor color) {
        this.durability(color.getWoolData());

        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder dyeColor(DyeColor color) {
        this.durability(color.getDyeData());

        return this;
    }

    public ItemBuilder addGlow() {
        this.enchantment(Enchantment.WATER_WORKER);
        ItemMeta meta = is.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        is.setItemMeta(meta);

        return this;
    }
}
