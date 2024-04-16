package de.zeppy5.sudokuminecraft.listener;

import de.zeppy5.sudokuminecraft.SudokuMinecraft;
import de.zeppy5.sudokuminecraft.sudoku.SudokuManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class InventoryListener implements Listener {

    @EventHandler
    public void onChangeSlot(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        SudokuManager instance = SudokuManager.getInstance(player);

        if (instance == null) {
            return;
        }

        event.setCancelled(true);
        instance.setStandardInv();
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        SudokuManager instance = SudokuManager.getInstance(event.getPlayer());

        if (instance == null) {
            return;
        }

        event.setCancelled(true);
        Bukkit.getScheduler().runTask(SudokuMinecraft.getInstance(), instance::setStandardInv);
    }

}
