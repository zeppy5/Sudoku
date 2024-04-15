package de.zeppy5.sudokuminecraft.command;

import de.zeppy5.sudokuminecraft.sudoku.SudokuManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SudokuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You have to be a player!");
            return false;
        }
        Player player = (Player) sender;

        SudokuManager instance = SudokuManager.getInstance(player);

        if (instance != null && args.length == 1 && args[0].equalsIgnoreCase("confirm")) {
            instance.reveal();
            return false;
        }

        if (instance != null) {
            sender.sendMessage(ChatColor.RED + "You are already in a game!");
            return false;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("confirm")) {
            return false;
        }

        new SudokuManager(player);

        return false;
    }
}
