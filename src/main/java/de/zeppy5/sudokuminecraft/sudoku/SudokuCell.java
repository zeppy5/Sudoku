package de.zeppy5.sudokuminecraft.sudoku;

import org.bukkit.Location;
import org.bukkit.Material;

public class SudokuCell {

    private final Location loc;
    private Material material;
    private int num;
    private boolean generated = true;

    public SudokuCell(Location loc, int num) {
        this.loc = loc;
        this.num = num;
        this.material = getMaterialByNum(num);
        if (num == 0)
            generated = false;
        fill();
    }

    public static Material getMaterialByNum(int num) {
        return switch (num) {
            case 1 -> Material.RED_CONCRETE;
            case 2 -> Material.ORANGE_CONCRETE;
            case 3 -> Material.MAGENTA_CONCRETE;
            case 4 -> Material.LIGHT_BLUE_CONCRETE;
            case 5 -> Material.YELLOW_CONCRETE;
            case 6 -> Material.LIME_CONCRETE;
            case 7 -> Material.PINK_CONCRETE;
            case 8 -> Material.GRAY_CONCRETE;
            case 9 -> Material.PURPLE_CONCRETE;
            default -> Material.AIR;
        };
    }

    public static int getNumByMaterial(Material material) {
        return switch (material) {
            case RED_CONCRETE -> 1;
            case ORANGE_CONCRETE -> 2;
            case MAGENTA_CONCRETE -> 3;
            case LIGHT_BLUE_CONCRETE -> 4;
            case YELLOW_CONCRETE -> 5;
            case LIME_CONCRETE -> 6;
            case PINK_CONCRETE -> 7;
            case GRAY_CONCRETE -> 8;
            case PURPLE_CONCRETE -> 9;
            default -> 0;
        };
    }

    public void setMaterial(Material material) {
        this.material = material;
        this.num = getNumByMaterial(material);
        loc.getBlock().setType(material);
    }

    public void fill() {
        loc.getBlock().setType(material);
    }

    public int getNum() {
        return num;
    }

    public Location getLocation() {
        return loc.clone();
    }

    public boolean isGenerated() {
        return generated;
    }
}
