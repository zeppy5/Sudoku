package de.zeppy5.sudokuminecraft.listener;

import de.zeppy5.sudokuminecraft.sudoku.SudokuManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class DoneListener implements Listener {

    @EventHandler
    public void onSwapItem(PlayerSwapHandItemsEvent event) {
        SudokuManager instance = SudokuManager.getInstance(event.getPlayer());

        if (instance == null) {
            return;
        }

        event.setCancelled(true);
        instance.submit();
    }

}
