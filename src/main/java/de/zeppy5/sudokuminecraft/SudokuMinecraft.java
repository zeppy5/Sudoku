package de.zeppy5.sudokuminecraft;

import de.zeppy5.sudokuminecraft.command.SudokuCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class SudokuMinecraft extends JavaPlugin {

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("sudoku")).setExecutor(new SudokuCommand());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
