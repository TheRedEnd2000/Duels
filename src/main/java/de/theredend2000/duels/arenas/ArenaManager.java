package de.theredend2000.duels.arenas;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.game.GameState;
import org.apache.logging.log4j.core.appender.rolling.action.IfAll;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class ArenaManager {

    private FileConfiguration arenaYaml;
    private File arenaFile;

    public ArenaManager() {
        arenaFile = new File(Main.getPlugin().getDataFolder(), "arenas.yml");
        arenaYaml = YamlConfiguration.loadConfiguration(arenaFile);
    }

    public FileConfiguration getArenaYaml() {
        return arenaYaml;
    }

    public void saveArenaYaml() {
        try {
            arenaYaml.save(arenaFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveNewArena(String name) {
        arenaYaml.set("Arenas." + name + ".name", name);
        saveArenaYaml();
    }

    public void removeArena(String name) {
        arenaYaml.set("Arenas." + name, null);
        saveArenaYaml();
    }

    public void saveAllArenas() {
        for (Arena arena : Main.getPlugin().getArenaManagerHashMap().values()) {
            arenaYaml.set("Arenas." + arena.getName() + ".name", arena.getName());
            arenaYaml.set("Arenas." + arena.getName() + ".endLocation", arena.getEndSpawn());
            arenaYaml.set("Arenas." + arena.getName() + ".lobbyLocation", arena.getLobbySpawn());
            arenaYaml.set("Arenas." + arena.getName() + ".spawn1", arena.getSpawn1());
            arenaYaml.set("Arenas." + arena.getName() + ".spawn2", arena.getSpawn2());
            arenaYaml.set("Arenas." + arena.getName() + ".pos1",arena.getPos1());
            arenaYaml.set("Arenas." + arena.getName() + ".pos2",arena.getPos2());
            Bukkit.getConsoleSender().sendMessage("§7Arena §a" + arena.getName() + " §7saved.");
            try {
                arenaYaml.save(arenaFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        saveArenaYaml();
    }

    public void loadAllArenas() {
        Bukkit.getConsoleSender().sendMessage("\n§4§lLOADING ARENAS...");
            if (!arenaYaml.contains("Arenas.")) return;
            for (String arenaNames : arenaYaml.getConfigurationSection("Arenas.").getKeys(false)) {
                Location lobby = arenaYaml.getLocation("Arenas." + arenaNames + ".lobbyLocation");
                Location end = arenaYaml.getLocation("Arenas." + arenaNames + ".endLocation");
                Location spawn1 = arenaYaml.getLocation("Arenas." + arenaNames + ".spawn1");
                Location spawn2 = arenaYaml.getLocation("Arenas." + arenaNames + ".spawn2");
                Location pos1 = arenaYaml.getLocation("Arenas." + arenaNames + ".pos1");
                Location pos2 = arenaYaml.getLocation("Arenas." + arenaNames + ".pos2");
                Arena arena = new Arena(arenaNames,canBeEnabled(arenaNames),new ArrayList<UUID>(), lobby,end,spawn1,spawn2,pos1,pos2,canBeEnabled(arenaNames) ? GameState.WAITING : GameState.DISABLED);
                Main.getPlugin().getArenaManagerHashMap().put(arenaNames, arena);
                Bukkit.getConsoleSender().sendMessage("§7Arena §a" + arenaNames + " §7loaded §2successfully§7.");
            }
        Bukkit.getConsoleSender().sendMessage("§2§lALL ARENAS SUCCESSFULLY LOADED!\n");
    }

    public void loadAllArena() {
        Bukkit.getConsoleSender().sendMessage("\n§4§lLOADING ARENAS...");
        if (!arenaYaml.contains("Arenas.")) {
            Bukkit.getConsoleSender().sendMessage("§cNo arenas found in the configuration file.");
            return;
        }

        ConfigurationSection arenasSection = arenaYaml.getConfigurationSection("Arenas.");
        if (arenasSection == null) {
            Bukkit.getConsoleSender().sendMessage("§c'Arenas.' section not found in the configuration file.");
            return;
        }

        for (String arenaName : arenasSection.getKeys(false)) {
            String arenaPath = "Arenas." + arenaName;

            if (!arenaYaml.contains(arenaPath)) {
                Bukkit.getConsoleSender().sendMessage("§cArena data not found for arena: " + arenaName);
                continue;
            }

            Location lobby = arenaYaml.getLocation(arenaPath + ".lobbyLocation");
            Location end = arenaYaml.getLocation(arenaPath + ".endLocation");
            Location spawn1 = arenaYaml.getLocation(arenaPath + ".spawn1");
            Location spawn2 = arenaYaml.getLocation(arenaPath + ".spawn2");
            Location pos1 = arenaYaml.getLocation(arenaPath + ".pos1");
            Location pos2 = arenaYaml.getLocation(arenaPath + ".pos2");

            if (lobby == null || end == null || spawn1 == null || spawn2 == null || pos1 == null || pos2 == null) {
                Bukkit.getConsoleSender().sendMessage("§cInvalid location data for arena: " + arenaName);
                continue;
            }

            boolean canBeEnabled = canBeEnabled(arenaName);
            GameState gameState = canBeEnabled ? GameState.WAITING : GameState.DISABLED;
            Arena arena = new Arena(arenaName, canBeEnabled, new ArrayList<>(), lobby, end, spawn1, spawn2, pos1, pos2, gameState);
            Main.getPlugin().getArenaManagerHashMap().put(arenaName, arena);

            Bukkit.getConsoleSender().sendMessage("§7Arena §a" + arenaName + " §7loaded §2successfully§7.");
        }

        Bukkit.getConsoleSender().sendMessage("§2§lALL ARENAS SUCCESSFULLY LOADED!\n");
    }
    public boolean canBeEnabled(String arenaNames){
        return arenaYaml.getLocation("Arenas." + arenaNames + ".lobbyLocation") != null && arenaYaml.getLocation("Arenas." + arenaNames + ".pos1") != null && arenaYaml.getLocation("Arenas." + arenaNames + ".pos2") != null && arenaYaml.getLocation("Arenas." + arenaNames + ".spawn1") != null && arenaYaml.getLocation("Arenas." + arenaNames + ".spawn2") != null && arenaYaml.getLocation("Arenas." + arenaNames + ".endLocation") != null;
    }

    public void updateArenas(){
        for (Arena arena : Main.getPlugin().getArenaManagerHashMap().values()) {
            arenaYaml.set("Arenas." + arena.getName() + ".name", arena.getName());
            arenaYaml.set("Arenas." + arena.getName() + ".endLocation", arena.getEndSpawn());
            arenaYaml.set("Arenas." + arena.getName() + ".lobbyLocation", arena.getLobbySpawn());
            arenaYaml.set("Arenas." + arena.getName() + ".spawn1", arena.getSpawn1());
            arenaYaml.set("Arenas." + arena.getName() + ".spawn2", arena.getSpawn2());
            arenaYaml.set("Arenas." + arena.getName() + ".pos1", arena.getPos1());
            arenaYaml.set("Arenas." + arena.getName() + ".pos2", arena.getPos2());
            arenaYaml.set("Arenas."+arena.getName()+".currentGameState",arena.getGameState());
            saveArenaYaml();
        }
        Main.getPlugin().getArenaManagerHashMap().clear();
        if (!arenaYaml.contains("Arenas.")) return;
        for (String arenaNames : arenaYaml.getConfigurationSection("Arenas.").getKeys(false)) {
            Location lobby = arenaYaml.getLocation("Arenas." + arenaNames + ".lobbyLocation");
            Location end = arenaYaml.getLocation("Arenas." + arenaNames + ".endLocation");
            Location spawn1 = arenaYaml.getLocation("Arenas." + arenaNames + ".spawn1");
            Location spawn2 = arenaYaml.getLocation("Arenas." + arenaNames + ".spawn2");
            Location pos1 = arenaYaml.getLocation("Arenas." + arenaNames + ".pos1");
            Location pos2 = arenaYaml.getLocation("Arenas." + arenaNames + ".pos2");
            Main.getPlugin().getArenaManagerHashMap().put(arenaNames, new Arena(arenaNames, canBeEnabled(arenaNames), new ArrayList<UUID>(), lobby,end,spawn1,spawn2,pos1,pos2, (canBeEnabled(arenaNames) ? (GameState.valueOf(arenaYaml.getString("Arenas."+arenaNames+".currentGameState")) == GameState.DISABLED ? GameState.WAITING : GameState.valueOf(arenaYaml.getString("Arenas."+arenaNames+".currentGameState"))) : GameState.DISABLED)));
            arenaYaml.set("Arenas."+arenaNames+".currentGameState",null);
            saveArenaYaml();
        }
    }

    public void setArenaMaterial(Arena arena, Material material){
        arenaYaml.set("Arenas."+arena.getName()+".icon",material.name());
        saveArenaYaml();
    }


    public Material getArenaMaterial(Arena arena){
        return Main.getPlugin().getArenaMaterial(arenaYaml.getString("Arenas."+arena.getName()+".icon")) != null ? Main.getPlugin().getArenaMaterial(arenaYaml.getString("Arenas."+arena.getName()+".icon")) : Material.getMaterial(Objects.requireNonNull(Main.getPlugin().getConfig().getString("items.default-arena-item")));
    }
    public boolean playerIsAlreadyInArena(Player player){
        for(Arena arena : Main.getPlugin().getArenaManagerHashMap().values()){
            if(arena.getPlayerInGame().contains(player.getUniqueId())){
                return true;
            }
        }
        return false;
    }

    public boolean existsArena(String arenaName){
        for(Arena arena : Main.getPlugin().getArenaManagerHashMap().values()){
            if(arena.getName().equals(arenaName)){
                return true;
            }
        }
        return false;
    }

    public Arena getPlayerCurrentArena(Player player){
        for(Arena arena : Main.getPlugin().getArenaManagerHashMap().values()){
            if(arena.getPlayerInGame().contains(player.getUniqueId())){
                return arena;
            }
        }
        return null;
    }

    public boolean isLocationWithinArea(Arena arena, Location location) {
        Location pos1 = arena.getPos1();
        Location pos2 = arena.getPos2();

        double minX = Math.min(pos1.getX(), pos2.getX());
        double minY = Math.min(pos1.getY(), pos2.getY());
        double minZ = Math.min(pos1.getZ(), pos2.getZ());
        double maxX = Math.max(pos1.getX(), pos2.getX());
        double maxY = Math.max(pos1.getY(), pos2.getY());
        double maxZ = Math.max(pos1.getZ(), pos2.getZ());

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
    }

    public void removeEntitiesInArena(Arena arena) {
        Location pos1 = arena.getPos1();
        Location pos2 = arena.getPos2();
        World world = pos1.getWorld();
        double minX = Math.min(pos1.getX(), pos2.getX());
        double minY = Math.min(pos1.getY(), pos2.getY());
        double minZ = Math.min(pos1.getZ(), pos2.getZ());
        double maxX = Math.max(pos1.getX(), pos2.getX());
        double maxY = Math.max(pos1.getY(), pos2.getY());
        double maxZ = Math.max(pos1.getZ(), pos2.getZ());

        for (Entity entity : world.getEntities()) {
            Location entityLocation = entity.getLocation();
            double entityX = entityLocation.getX();
            double entityY = entityLocation.getY();
            double entityZ = entityLocation.getZ();

            if (entityX >= minX && entityX <= maxX && entityY >= minY && entityY <= maxY && entityZ >= minZ && entityZ <= maxZ) {
                if (entity.getType() != EntityType.PLAYER && entity.getType() != EntityType.ARMOR_STAND && entity.getType() != EntityType.ITEM_FRAME) {
                    entity.remove();
                }
            }
        }
    }
}