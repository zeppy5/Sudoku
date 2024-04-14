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
        startLocation = player.getLocation().add(0, -1, 0);
        cells = new SudokuCell[9][9];
        fillCells();
    }

    public void fillCells() {
        int[][] sudoku = SudokuGenerator.createSudoku();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; i < 9; i++) {
                cells[i][j] = new SudokuCell(startLocation.clone().add(i, 0, j), sudoku[i][j]);

            }
        }
    }

    public void done() {
        managerMap.remove(player);
    }

    public static SudokuManager getInstance(Player player) {
        return managerMap.get(player);
    }

}
