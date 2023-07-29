package de.theredend2000.duels.util;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.BlockData;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockUtils {
    private static HashMap<Arena, HashMap<Location, BlockState>> blockList;
    private static List<Arena> arenas;

    public BlockUtils() {
        blockList = new HashMap<>();
        arenas = new ArrayList<>();
    }

    public static void saveBlocksBetween(Arena arena, Location pos1, Location pos2) {
        HashMap<Location, BlockState> arenaBlocks = new HashMap<>();

        int minX = Math.min(pos1.getBlockX(), pos2.getBlockX());
        int minY = Math.min(pos1.getBlockY(), pos2.getBlockY());
        int minZ = Math.min(pos1.getBlockZ(), pos2.getBlockZ());
        int maxX = Math.max(pos1.getBlockX(), pos2.getBlockX());
        int maxY = Math.max(pos1.getBlockY(), pos2.getBlockY());
        int maxZ = Math.max(pos1.getBlockZ(), pos2.getBlockZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Location location = new Location(pos1.getWorld(), x, y, z);
                    Block block = location.getBlock();
                    BlockState blockState = block.getState();

                    // Speichern des ursprünglichen Blockzustands
                    arenaBlocks.put(location, blockState);
                }
            }
        }

        blockList.put(arena, arenaBlocks);
        arenas.add(arena);
    }

    public static void restoreBlocks(Arena arena) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!blockList.containsKey(arena)) {
                    return;
                }

                HashMap<Location, BlockState> arenaBlocks = blockList.get(arena);
                List<Location> blocksToRemove = new ArrayList<>();

                for (Map.Entry<Location, BlockState> entry : arenaBlocks.entrySet()) {
                    Location location = entry.getKey();
                    BlockState blockState = entry.getValue();
                    Block block = location.getBlock();

                    blockState.update(true, false);
                    block.setBlockData(blockState.getBlockData(), false);

                    blocksToRemove.add(location);
                }

                for (Location location : blocksToRemove) {
                    arenaBlocks.remove(location);
                }

                blockList.remove(arena);
                arenas.remove(arena);
                Bukkit.broadcastMessage("regen");
            }
        }.runTaskLater(Main.getPlugin(),20L);
    }

    public static void restoreAllBlocks() {
        for (Arena arena : arenas) {
            restoreBlocks(arena);
        }
    }
}
