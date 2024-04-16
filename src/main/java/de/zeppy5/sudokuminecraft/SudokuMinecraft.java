package de.zeppy5.sudokuminecraft;

import de.zeppy5.sudokuminecraft.command.SudokuCommand;
import de.zeppy5.sudokuminecraft.listener.ChangeBlockListener;
import de.zeppy5.sudokuminecraft.listener.DoneListener;
import de.zeppy5.sudokuminecraft.listener.InventoryListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SudokuMinecraft extends JavaPlugin {

    private static SudokuMinecraft instance;

    @Override
    public void onEnable() {
        instance = this;

        Objects.requireNonNull(getCommand("sudoku")).setExecutor(new SudokuCommand());
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new ChangeBlockListener(),this);
        pluginManager.registerEvents(new DoneListener(), this);
        pluginManager.registerEvents(new InventoryListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static SudokuMinecraft getInstance() {
        return instance;
    }
}
