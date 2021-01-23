package me.ihellsmc.infinitystones.stone.recipe;

import me.ihellsmc.infinitystones.stone.Stone;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class StonesRecipe {

    public static ShapedRecipe getRecipe(Stone stone) {
        ItemStack item = stone.getItemStack(false);
        ShapedRecipe recipe = new ShapedRecipe(item);
        recipe.shape("^&^", "%@%", "^&^");

        recipe.setIngredient('^', Material.GOLD_INGOT);
        recipe.setIngredient('&', Material.DIAMOND);
        recipe.setIngredient('%', Material.EMERALD);

        switch (stone) {
            case REALITY:
                recipe.setIngredient('@', Material.BLAZE_POWDER);
                break;
            case SPACE:
                recipe.setIngredient('@', Material.EYE_OF_ENDER);
                break;
            case MIND:
                recipe.setIngredient('@', Material.FERMENTED_SPIDER_EYE);
                break;
            case POWER:
                recipe.setIngredient('@', Material.NETHER_STAR);
                break;
            case TIME:
                recipe.setIngredient('@', Material.SPECKLED_MELON);
                break;
            case SOUL:
                recipe.setIngredient('@', Material.GOLDEN_APPLE);
                break;
        }

        return recipe;
    }

}
