package de.zeppy5.sudokuminecraft.sudoku;

import de.zeppy5.sudokuminecraft.util.SudokuGenerator;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class SudokuManager {

    private static final Map<Player, SudokuManager> managerMap = new HashMap<>();

    private final Player player;

    private final Location startLocation;

    private final SudokuCell[][] cells;

    public SudokuManager(Player player) {
        managerMap.put(player, this);
        this.player = player;
        startLocation = player.getLocation().add(0, -1, 0).getBlock().getLocation();
        cells = new SudokuCell[9][9];
        fillCells();
    }

    public void fillCells() {
        int[][] mat = SudokuGenerator.createSudoku();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j] = new SudokuCell(startLocation.clone().add(i, 0, j), mat[i][j]);

            }
        }
    }

    public void done() {
        managerMap.remove(player);
    }

    public boolean check() {
        for (SudokuCell[] row : cells) {
            for (SudokuCell cell : row) {
                Location vec = cell.getLocation().subtract(startLocation);
                if (!checkIfSafe((int) vec.getX(), (int) vec.getZ(), cell.getNum()))
                    return false;
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

    public static SudokuManager getInstance(Player player) {
        return managerMap.get(player);
    }

}
