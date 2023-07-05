package de.theredend2000.duels.listeners;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import de.theredend2000.duels.game.GameState;
import de.theredend2000.duels.kits.Kit;
import net.minecraft.world.entity.projectile.FishingHook;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementDisplay;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;

import java.util.*;

public class GameListeners implements Listener {

    private final HashMap<Block, Arena> blockList;

    public GameListeners(){
        blockList = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this,Main.getPlugin());
    }

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event){
        Player player = event.getPlayer();
        if(Main.getPlugin().getArenaManager().playerIsAlreadyInArena(player)) {
            Arena arena = Main.getPlugin().getArenaManager().getPlayerCurrentArena(player);
            if(arena.getGameState() != GameState.RUNNING){
                event.setCancelled(true);
                return;
            }
            if(Main.getPlugin().getArenaManager().isLocationWithinArea(arena,event.getBlockPlaced().getLocation())){
                blockList.put(event.getBlockPlaced(),arena);
            }else{
                event.setCancelled(true);
                player.sendMessage(Main.PREFIX+"§cYou cannot build out of the arena.");
            }
        }
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent event){
        Player player = event.getPlayer();
        if(Main.getPlugin().getArenaManager().playerIsAlreadyInArena(player)){
            Arena arena = Main.getPlugin().getArenaManager().getPlayerCurrentArena(player);
            if(arena.getGameState() != GameState.RUNNING){
                event.setCancelled(true);
                return;
            }
            if(blockList.containsKey(event.getBlock()) && blockList.get(event.getBlock()).equals(arena)) {
                if(Main.getPlugin().getArenaManager().isLocationWithinArea(arena,event.getBlock().getLocation())){
                    event.setCancelled(false);
                    blockList.keySet().removeIf(block -> block.equals(event.getBlock()));
                }else{
                    event.setCancelled(true);
                    player.sendMessage(Main.PREFIX+"§cYou cannot break out of the arena.");
                }
            }else
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event){
        Player player = event.getPlayer();
        if(Main.getPlugin().getArenaManager().playerIsAlreadyInArena(player)){
            Arena arena = Main.getPlugin().getArenaManager().getPlayerCurrentArena(player);
            if(arena.getGameState().equals(GameState.STARTING)) {
                if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockZ() != event.getTo().getBlockZ())
                    event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if(Main.getPlugin().getArenaManager().playerIsAlreadyInArena(player)){
            Arena arena = Main.getPlugin().getArenaManager().getPlayerCurrentArena(player);
            if(!arena.getGameState().equals(GameState.RUNNING))
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(Main.getPlugin().getArenaManager().playerIsAlreadyInArena(player)){
            Arena arena = Main.getPlugin().getArenaManager().getPlayerCurrentArena(player);
            for (String materialString : Main.getPlugin().getConfig().getStringList("lists.allowed-items")) {
                Material material = Material.getMaterial(materialString.toUpperCase());
                if(event.getItem() == null) return;
                if (arena.getGameState().equals(GameState.RUNNING) || event.getItem().getType().equals(material)) {
                    event.setCancelled(false);
                    return;
                }

                if (arena.getGameState().equals(GameState.STARTING)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        Player killer = event.getEntity().getKiller();
        if(Main.getPlugin().getArenaManager().playerIsAlreadyInArena(player)){
            Arena arena = Main.getPlugin().getArenaManager().getPlayerCurrentArena(player);
            if(arena.getGameState().equals(GameState.RUNNING)){
                if(killer == null){
                    ArrayList<UUID> players = new ArrayList<>(arena.getPlayerInGame());
                    players.remove(player.getUniqueId());
                    Player getRealKiller = Bukkit.getPlayer(players.get(0));
                    if(getRealKiller == null) return;
                    killer = getRealKiller;
                }
                player.setHealth(player.getMaxHealth());
                event.getDrops().clear();
                event.setDroppedExp(0);
                event.setDeathMessage(null);
                Kit kit = Main.getPlugin().getArenaKit().get(arena);
                Main.getPlugin().getGameManager().winDuel(player,killer,arena,kit);
                arena.setGameState(GameState.GAME_END);
                //blockList.entrySet().stream().filter(entry -> entry.getValue().equals(arena)).forEach(entry -> {entry.getKey().setType(Material.AIR);blockList.remove(entry.getKey());});
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event){
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (Main.getPlugin().getArenaManager().playerIsAlreadyInArena(player)) {
                Arena arena = Main.getPlugin().getArenaManager().getPlayerCurrentArena(player);
                if (arena.getGameState() != GameState.RUNNING)
                    event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        Player player = (Player) event.getPlayer();
        if (Main.getPlugin().getArenaManager().playerIsAlreadyInArena(player)) {
            Arena arena = Main.getPlugin().getArenaManager().getPlayerCurrentArena(player);
            event.setCancelled(true);
            for(UUID uuid : arena.getPlayerInGame()){
                Player p = Bukkit.getPlayer(uuid);
                if(p == null) return;
                p.sendMessage(event.getMessage());
            }
        }
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent event) {
        List<Block> blocksToRemove = new ArrayList<>();
        for (Block block : event.blockList()) {
            if (!blockList.containsKey(block)) {
                blocksToRemove.add(block);
            }
        }
        event.blockList().removeAll(blocksToRemove);
    }

    @EventHandler
    public void onExplodeEntity(EntityExplodeEvent event) {
        List<Block> blocksToRemove = new ArrayList<>();
        for (Block block : event.blockList()) {
            if (!blockList.containsKey(block)) {
                blocksToRemove.add(block);
            }
        }
        event.blockList().removeAll(blocksToRemove);
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if (event.getCause() == BlockIgniteEvent.IgniteCause.EXPLOSION) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        Player player = event.getPlayer();
        Player winner;
        if(Main.getPlugin().getArenaManager().playerIsAlreadyInArena(player)){
            Arena arena = Main.getPlugin().getArenaManager().getPlayerCurrentArena(player);
            if(arena.getGameState().equals(GameState.STARTING) || arena.getGameState().equals(GameState.RUNNING)){
                ArrayList<UUID> players = new ArrayList<>(arena.getPlayerInGame());
                players.remove(player.getUniqueId());
                Player getRealKiller = Bukkit.getPlayer(players.get(0));
                if(getRealKiller == null) return;
                winner = getRealKiller;
                event.setQuitMessage(null);
                Main.getPlugin().getGameManager().leaveDuel(player,arena);
                Kit kit = Main.getPlugin().getArenaKit().get(arena);
                Main.getPlugin().getGameManager().winDuel(player,winner,arena,kit);
                arena.setGameState(GameState.GAME_END);
                winner.sendMessage(Main.PREFIX+"§e"+player.getDisplayName()+"§c left the game. §6You win.");
            }
            if(arena.getGameState().equals(GameState.GAME_END)){
                event.setQuitMessage(null);
                Main.getPlugin().getGameManager().leaveDuel(player,arena);
            }
        }
    }

}
