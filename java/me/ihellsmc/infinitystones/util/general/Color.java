package me.ihellsmc.infinitystones.util.general;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Color {

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static List<String> color(List<String> strings) {
        List<String> translated = new ArrayList<>();
        for (String string : strings)
            translated.add(color(string));

        return translated;
    }

}
