package de.zeppy5.sudokuminecraft.sudoku;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

public class BookGenerator {

    private final Player player;

    public BookGenerator(Player player) {
        this.player = player;
    }

    public void openBook() {
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) item.getItemMeta();
        assert meta != null;

        meta.setTitle("Blank");
        meta.setAuthor("Server");
        meta.setPages(getPages());

        item.setItemMeta(meta);
        player.openBook(item);
    }

    private List<String> getPages() {
        List<String> pageList = new ArrayList<>();

        pageList.add(String.valueOf(ChatColor.GREEN) + ChatColor.UNDERLINE + "Sudoku Game Rules \n"
                + ChatColor.RESET + ChatColor.AQUA + "You must fill the empty blocks with the blocks in your inventory," +
                " so that a color does not occur multiple times in a row," +
                " in a column and in a 3x3 box (separated by the black lines).\n" +
                "The green particles indicate modifiable blocks.");
        pageList.add(ChatColor.AQUA + "In order to complete the game follow the instruction provided in the actionbar.\n" +
                "If your answer is correct a jingle will sound," +
                " otherwise you are given the option to reveal the answer or to try again.");
        pageList.add(ChatColor.AQUA + "Once the game is completed it will stop automatically.\n"
                + ChatColor.RED + ChatColor.BOLD + "This is a game involving logic which may vary in complexity!");

        return pageList;
    }

}
