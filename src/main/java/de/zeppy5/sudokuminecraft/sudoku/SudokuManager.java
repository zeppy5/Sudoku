package de.zeppy5.sudokuminecraft.sudoku;

import de.zeppy5.sudokuminecraft.SudokuMinecraft;
import de.zeppy5.sudokuminecraft.util.SudokuGenerator;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.KeybindComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class SudokuManager {

    /*
    IDEAS:
    Sounds
    3x3 Box separators
    Rules & Introduction Book
     */

    private static final Map<String, SudokuManager> managerMap = new HashMap<>();

    private final Player player;

    private final Location startLocation;

    private final SudokuCell[][] cells;

    private int taskID;

    private boolean revealed;

    public SudokuManager(Player player) {
        managerMap.put(player.getUniqueId().toString(), this);
        this.player = player;
        startLocation = player.getLocation().add(0, -1, 0).getBlock().getLocation();
        cells = new SudokuCell[9][9];
        fillCells();
        schedule();
    }

    public void fillCells() {
        int[][] mat = SudokuGenerator.createSudoku();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j] = new SudokuCell(startLocation.clone().add(i, 0, j), mat[i][j]);

            }
        }
    }

    public void endGame() {
        Bukkit.getScheduler().cancelTask(taskID);
        player.sendMessage(ChatColor.GOLD + "Game ends in " + ChatColor.GREEN + "10" + ChatColor.GOLD + " seconds");
        Bukkit.getScheduler().runTaskLater(SudokuMinecraft.getInstance(),
                () -> player.sendMessage(ChatColor.GOLD + "Game ends in " + ChatColor.GREEN + "5" + ChatColor.GOLD + " seconds"),
                20*5);
        Bukkit.getScheduler().runTaskLater(SudokuMinecraft.getInstance(), this::done, 20*10);
    }

    public void done() {
        managerMap.remove(player.getUniqueId().toString());
        Arrays.stream(cells)
                .flatMap(Arrays::stream)
                .map(SudokuCell::getLocation)
                .forEach(location -> location.getBlock().setType(Material.AIR));
    }

    public void reveal() {
        if (revealed) {
            return;
        }

        revealed = true;
        player.sendMessage("Answer");
        endGame();
    }

    public void submit() {
        if (revealed) {
            return;
        }

        if (check()) {
            player.sendMessage(ChatColor.GREEN + "Correct! Well done");
            endGame();
            return;
        }
        TextComponent message = new TextComponent("Your answer is incorrect. Click ");
        message.setColor(net.md_5.bungee.api.ChatColor.GREEN);

        TextComponent command = new TextComponent("here");
        command.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sudoku confirm"));
        command.setColor(net.md_5.bungee.api.ChatColor.GOLD);
        command.setUnderlined(true);

        message.addExtra(command);

        TextComponent messagePart2 = new TextComponent(" to reveal the answer or continue trying");
        messagePart2.setColor(net.md_5.bungee.api.ChatColor.GREEN);

        message.addExtra(messagePart2);

        player.spigot().sendMessage(message);

    }

    public boolean check() {
        for (SudokuCell[] row : cells) {
            for (SudokuCell cell : row) {
                Location vec = cell.getLocation().subtract(startLocation);
                if (!checkIfSafe((int) vec.getX(), (int) vec.getZ(), cell.getNum())) {
                    Objects.requireNonNull(cell.getLocation().getWorld())
                            .spawnParticle(Particle.DRIP_LAVA, cell.getLocation().add(0.5, 1, 0.5), 30, 0.2, 0, 0.2);
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean checkIfSafe(int i, int j, int num) {
        return (num != 0 &&
                isUsedOnceInRow(i, num) &&
                isUsedOnceInCol(j, num) &&
                isUsedOnceInBox(i - i%3, j - j%3, num));
    }

    private boolean isUsedOnceInRow(int i, int num) {
        boolean used = false;
        for (int j = 0; j < 9; j++)
            if (cells[i][j].getNum() == num) {
                if (used)
                    return false;
                used = true;
            }
        return true;
    }

    private boolean isUsedOnceInCol(int j, int num) {
        boolean used = false;
        for (int i = 0; i < 9; i++)
            if (cells[i][j].getNum() == num) {
                if (used)
                    return false;
                used = true;
            }
        return true;
    }

    private boolean isUsedOnceInBox(int rowStart, int colStart, int num) {
        boolean used = false;
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (cells[rowStart+i][colStart+j].getNum() == num) {
                    if (used)
                        return false;
                    used = true;
                }

        return true;
    }

    private void schedule() {
        List<Location> nonGeneratedCells = Arrays.stream(cells)
                .flatMap(Arrays::stream)
                .filter(sudokuCell -> !sudokuCell.isGenerated())
                .map(sudokuCell -> sudokuCell.getLocation().add(0.5, 1, 0.5))
                .collect(Collectors.toList());

        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(SudokuMinecraft.getInstance(), () -> {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    new ComponentBuilder("Press ")
                            .color(net.md_5.bungee.api.ChatColor.GREEN)
                            .append(new KeybindComponent("key.swapOffhand"))
                            .color(net.md_5.bungee.api.ChatColor.GOLD)
                            .append(" to submit your answer")
                            .color(net.md_5.bungee.api.ChatColor.GREEN)
                            .create());

            nonGeneratedCells.forEach(location -> Objects.requireNonNull(location.getWorld())
                    .spawnParticle(Particle.COMPOSTER, location, 5));
        }, 0L, 20);
    }

    public SudokuCell[][] getCells() {
        return cells.clone();
    }

    public static SudokuManager getInstance(Player player) {
        return managerMap.get(player.getUniqueId().toString());
    }

}
