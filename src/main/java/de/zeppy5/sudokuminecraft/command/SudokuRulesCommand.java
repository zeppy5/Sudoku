package de.zeppy5.sudokuminecraft.command;

import de.zeppy5.sudokuminecraft.sudoku.BookGenerator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SudokuRulesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "You have to be a player!");
            return false;
        }
        Player player = (Player) sender;
        new BookGenerator(player).openBook();

        return false;
    }
}
