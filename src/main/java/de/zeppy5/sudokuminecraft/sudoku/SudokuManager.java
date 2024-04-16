package de.zeppy5.sudokuminecraft.sudoku;

import de.zeppy5.sudokuminecraft.SudokuMinecraft;
import de.zeppy5.sudokuminecraft.util.SudokuGenerator;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.KeybindComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.stream.Collectors;

public class SudokuManager {

    /*
    IDEAS:
    Sounds
    3x3 Box separators
    Rules & Introduction Book
    Give options in inv
     */

    private static final Map<String, SudokuManager> managerMap = new HashMap<>();

    private final Player player;

    private final Location startLocation;

    private final SudokuCell[][] cells;

    private int taskID;

    private boolean revealed;

    private int[][] solution;

    private final List<ArmorStand> linesList = new ArrayList<>();

    public SudokuManager(Player player) {
        managerMap.put(player.getUniqueId().toString(), this);
        this.player = player;
        player.playNote(player.getLocation(), Instrument.BIT, Note.flat(1, Note.Tone.C));
        startLocation = player.getLocation().add(0, -1, 0).getBlock().getLocation();
        cells = new SudokuCell[9][9];
        setStandardInv();
        fillCells();
        spawnLines();
        schedule();
    }

    public void spawnLines() {
        Location l1 = startLocation.clone().add(3, -0.1, 0.0);
        for (double i = 0; i < 9D; i = i + 0.4) {
            ArmorStand armorStand = (ArmorStand) Objects.requireNonNull(l1.getWorld()).spawnEntity(l1, EntityType.ARMOR_STAND);
            armorStand.setGravity(false);
            armorStand.setSmall(true);
            armorStand.setVisible(false);
            armorStand.setInvulnerable(true);
            armorStand.setPersistent(true);
            Objects.requireNonNull(armorStand.getEquipment()).setHelmet(new ItemStack(Material.BLACK_CONCRETE));
            linesList.add(armorStand);
            l1.add(0, 0, 0.4);
        }

        Location l2 = startLocation.clone().add(6, -0.1, 0.0);
        for (double i = 0; i < 9D; i = i + 0.4) {
            ArmorStand armorStand = (ArmorStand) Objects.requireNonNull(l2.getWorld()).spawnEntity(l2, EntityType.ARMOR_STAND);
            armorStand.setGravity(false);
            armorStand.setSmall(true);
            armorStand.setVisible(false);
            armorStand.setInvulnerable(true);
            armorStand.setPersistent(true);
            Objects.requireNonNull(armorStand.getEquipment()).setHelmet(new ItemStack(Material.BLACK_CONCRETE));
            linesList.add(armorStand);
            l2.add(0, 0, 0.4);
        }

        Location l3 = startLocation.clone().add(0, -0.1, 3);
        for (double i = 0; i < 9D; i = i + 0.4) {
            ArmorStand armorStand = (ArmorStand) Objects.requireNonNull(l3.getWorld()).spawnEntity(l3, EntityType.ARMOR_STAND);
            armorStand.setGravity(false);
            armorStand.setSmall(true);
            armorStand.setVisible(false);
            armorStand.setInvulnerable(true);
            armorStand.setPersistent(true);
            Objects.requireNonNull(armorStand.getEquipment()).setHelmet(new ItemStack(Material.BLACK_CONCRETE));
            linesList.add(armorStand);
            l3.add(0.4, 0, 0);
        }

        Location l4 = startLocation.clone().add(0, -0.1, 6);
        for (double i = 0; i < 9D; i = i + 0.4) {
            ArmorStand armorStand = (ArmorStand) Objects.requireNonNull(l4.getWorld()).spawnEntity(l4, EntityType.ARMOR_STAND);
            armorStand.setGravity(false);
            armorStand.setSmall(true);
            armorStand.setVisible(false);
            armorStand.setInvulnerable(true);
            armorStand.setPersistent(true);
            Objects.requireNonNull(armorStand.getEquipment()).setHelmet(new ItemStack(Material.BLACK_CONCRETE));
            linesList.add(armorStand);
            l4.add(0.4, 0, 0);
        }
    }

    public void fillCells() {
        List<int[][]> sudoku = SudokuGenerator.createSudoku();
        int[][] mat = sudoku.get(0);
        solution = sudoku.get(1);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j] = new SudokuCell(startLocation.clone().add(i, 0, j), mat[i][j]);

            }
        }
    }

    public void endGame() {
        player.sendMessage(ChatColor.GOLD + "Game ends in " + ChatColor.GREEN + "10" + ChatColor.GOLD + " seconds");
        Bukkit.getScheduler().runTaskLater(SudokuMinecraft.getInstance(),
                () -> player.sendMessage(ChatColor.GOLD + "Game ends in " + ChatColor.GREEN + "5" + ChatColor.GOLD + " seconds"),
                20*5);
        Bukkit.getScheduler().runTaskLater(SudokuMinecraft.getInstance(), this::done, 20*10);
    }

    public void done() {
        Bukkit.getScheduler().cancelTask(taskID);
        player.getInventory().clear();
        managerMap.remove(player.getUniqueId().toString());
        linesList.forEach(Entity::remove);
        Arrays.stream(cells)
                .flatMap(Arrays::stream)
                .map(SudokuCell::getLocation)
                .forEach(location -> location.getBlock().setType(Material.AIR));
    }

    public void reveal() {
        if (revealed) {
            return;
        }

        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);
        revealed = true;

        player.sendMessage(ChatColor.GRAY + "Revealed the answer");
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j].setNum(solution[i][j]);
            }
        }

        endGame();
    }

    public void submit() {
        if (revealed) {
            return;
        }

        if (check()) {
            revealed = true;
            player.sendMessage(ChatColor.GREEN + "Correct! Well done");
            endGame();
            player.playNote(player.getLocation(), Instrument.CHIME, Note.flat(1, Note.Tone.C));
            Bukkit.getScheduler().runTaskLater(SudokuMinecraft.getInstance(),
                    () -> player.playNote(player.getLocation(), Instrument.CHIME, Note.flat(1, Note.Tone.E)),
                    5);
            Bukkit.getScheduler().runTaskLater(SudokuMinecraft.getInstance(),
                    () -> player.playNote(player.getLocation(), Instrument.CHIME, Note.flat(2, Note.Tone.G)),
                    10);
            return;
        }

        player.playNote(player.getLocation(), Instrument.DIDGERIDOO, Note.flat(1, Note.Tone.C));
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

    public void setStandardInv() {
        for (int i = 0; i < 9; i++) {
            ItemStack item = new ItemStack(SudokuCell.getMaterialByNum(i+1));
            ItemMeta meta = item.getItemMeta();
            assert meta != null;
            meta.setDisplayName(ChatColor.GREEN + SudokuCell.getMaterialByNum(i+1).toString().replace("_CONCRETE", ""));
            item.setItemMeta(meta);
            player.getInventory().setItem(i, item);
        }
    }

    public static SudokuManager getInstance(Player player) {
        return managerMap.get(player.getUniqueId().toString());
    }

}
