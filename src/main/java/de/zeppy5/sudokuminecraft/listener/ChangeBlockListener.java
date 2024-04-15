package de.zeppy5.sudokuminecraft.listener;

import de.zeppy5.sudokuminecraft.sudoku.SudokuCell;
import de.zeppy5.sudokuminecraft.sudoku.SudokuManager;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.*;
import java.util.stream.Collectors;

public class ChangeBlockListener implements Listener {
    
    @EventHandler
    public void onChange(PlayerInteractEvent event) {
        SudokuManager instance = SudokuManager.getInstance(event.getPlayer());

        if (instance == null) {
            return;
        }

        List<Location> nonGeneratedCells = Arrays.stream(instance.getCells())
                .flatMap(Arrays::stream)
                .filter(sudokuCell -> !sudokuCell.isGenerated())
                .map(SudokuCell::getLocation)
                .collect(Collectors.toList());

        if (!(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK)) {
            return;
        }

        if (!nonGeneratedCells.contains(Objects.requireNonNull(event.getClickedBlock()).getLocation())) {
            event.setCancelled(true);
            return;
        }

        if (SudokuCell.getNumByMaterial(event.getMaterial()) == 0) {
            event.setCancelled(true);
            return;
        }

        Location blockLoc = event.getClickedBlock().getLocation();

        event.setCancelled(true);

        SudokuCell cell = Arrays.stream(instance.getCells())
                .flatMap(Arrays::stream)
                .map(sudokuCell -> Map.entry(sudokuCell, sudokuCell.getLocation()))
                .filter(location -> location.getValue().getBlock().getLocation().equals(blockLoc.getBlock().getLocation()))
                .findFirst().get().getKey();
        cell.setMaterial(event.getMaterial());

    }
    
}
