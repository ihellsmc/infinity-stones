package me.ihellsmc.infinitystones;

import me.ihellsmc.infinitystones.forgery.listener.CraftingListener;
import me.ihellsmc.infinitystones.stone.Stone;
import me.ihellsmc.infinitystones.stone.command.StonesCommand;
import me.ihellsmc.infinitystones.stone.listener.GUIListener;
import me.ihellsmc.infinitystones.stone.listener.StoneListener;
import me.ihellsmc.infinitystones.stone.recipe.StonesRecipe;
import me.ihellsmc.infinitystones.util.framework.CommandFramework;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class InfinityStones extends JavaPlugin {

    @Override
    public void onEnable() {
        CommandFramework framework = new CommandFramework(this);
        framework.registerCommands(new StonesCommand());

        for (Stone stone : Stone.values()) { this.getServer().addRecipe(StonesRecipe.getRecipe(stone)); }

        getServer().getPluginManager().registerEvents(new GUIListener(), this);
        getServer().getPluginManager().registerEvents(new StoneListener(this), this);
        getServer().getPluginManager().registerEvents(new CraftingListener(), this);

        getLogger().info("Enabled. Like Merlin, mentally enabled.");
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
        getLogger().info("Disabled. Again, like Merlin, but this time physically.");
    }

}
