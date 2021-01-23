package me.ihellsmc.infinitystones.stone;

import me.ihellsmc.infinitystones.util.general.ItemBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static me.ihellsmc.infinitystones.util.general.Color.color;

public enum Stone {

    REALITY("&cReality", DyeColor.RED, Collections.singletonList("&7  * 15% chance on hit to launch a fireball towards the enemy")),
    SPACE("&9Space", DyeColor.BLUE, Collections.singletonList("&7  * Right-click to teleport to the block you’re looking at")),
    MIND("&eMind", DyeColor.YELLOW, Arrays.asList("&7  * 7.5% chance on hit to corrupt a mob, causing instant death", "&7  * Resistance to Mind Stone's effects")),
    POWER("&dPower", DyeColor.MAGENTA, Arrays.asList("&7  * 15% chance on hit to launch an enemy", "&7  * 5% chance on hit to launch all enemies")),
    TIME("&aTime", DyeColor.LIME, Collections.singletonList("&7  * Press Q to restore your health to 30 seconds prior")),
    SOUL("&6Soul", DyeColor.ORANGE, Arrays.asList("&7  * 20% chance on hit to regain 0.5 hearts", "&7  * Permanent 20% damage resistance"));

    private final String name;
    private final DyeColor color;
    private final List<String> desc;

    Stone(final String name, final DyeColor color, List<String> desc) {
        this.name = name;
        this.color = color;
        this.desc = desc;
    }

    public String getName() {
        return color(this.name);
    }

    public ItemStack getItemStack(boolean gui) {
        List<String> lore = new ArrayList<>(gui ? Collections.singletonList("&7Click to receive the " + this.getName() + "&7 stone.")
                : Arrays.asList("&7To apply the stone, drag it and drop it onto", "&7the tool you wish to apply it to."));

        if (gui) { lore.add(" "); lore.add("&7Perks:"); lore.addAll(desc); }

        return new ItemBuilder(Material.INK_SACK)
                .dyeColor(this.color)
                .name(this.getName() + " Stone")
                .lore(color(lore))
                .localisedName(this.getName())
                .addGlow()
                .build();
    }

}
