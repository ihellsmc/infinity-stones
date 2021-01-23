package me.ihellsmc.infinitystones.stone.command;

import me.ihellsmc.infinitystones.stone.gui.StonesGUI;
import me.ihellsmc.infinitystones.util.framework.Command;
import me.ihellsmc.infinitystones.util.framework.CommandArgs;
import org.bukkit.entity.Player;

public class StonesCommand {

    @Command(name = "stones", inGameOnly = true)
    public void onCommand(CommandArgs cmd) {
        Player player = cmd.getPlayer();
        new StonesGUI(player);
    }

}
