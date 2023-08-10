package de.theredend2000.duels.listeners;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import de.theredend2000.duels.extramanagers.SpecialsManager;
import de.theredend2000.duels.game.GameState;
import de.theredend2000.duels.kits.Kit;
import de.theredend2000.duels.util.MessageKey;
import de.theredend2000.duels.util.MessageManager;
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
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.util.Vector;

import java.util.*;

public class GameListeners implements Listener {

    private final HashMap<Block, Arena> blockList;
    private MessageManager messageManager;

    public GameListeners(){
        blockList = new HashMap<>();
        Bukkit.getPluginManager().registerEvents(this,Main.getPlugin());
        messageManager = Main.getPlugin().getMessageManager();
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
                player.sendMessage(messageManager.getMessage(MessageKey.CANNOT_BUILD_OUT_OF_ARENA));
            }
        }
    }

    @EventHandler
    public void onFluidFlow(BlockFromToEvent event) {
        Block fromBlock = event.getBlock();
        Block toBlock = event.getToBlock();

        if (fromBlock.isLiquid() && toBlock.getType() == Material.AIR) {
            Location fromLocation = fromBlock.getLocation();
            Location toLocation = toBlock.getLocation();

            boolean fromWithinArena = Main.getPlugin().getArenaManager().isWithinArena(fromLocation);
            boolean toWithinArena = Main.getPlugin().getArenaManager().isWithinArena(toLocation);

            if (fromWithinArena && !toWithinArena) {
                event.setCancelled(true);
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
                    player.sendMessage(messageManager.getMessage(MessageKey.CANNOT_BREAK_BLOCKS_OUTSIDE_ARENA));
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
        if(Main.getPlugin().getArenaManager().playerIsAlreadyInArena(player)) {
            Arena arena = Main.getPlugin().getArenaManager().getPlayerCurrentArena(player);
            if (!arena.getGameState().equals(GameState.RUNNING))
                event.setCancelled(true);
            if (arena.getGameState().equals(GameState.RUNNING) || arena.getGameState().equals(GameState.GAME_END)){
                if (player.getHealth() - event.getFinalDamage() <= 0 && player.getInventory().getItemInOffHand().getType() != Material.TOTEM_OF_UNDYING && player.getInventory().getItemInHand().getType() != Material.TOTEM_OF_UNDYING) {
                    for (UUID uuid : arena.getPlayerInGame()) {
                        Player p = Bukkit.getPlayer(uuid);
                        if (p == null) continue;
                        if(event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK)
                            p.sendMessage(Main.PREFIX + Main.getPlugin().getSpecialsManager().getDeathMessage(event.getCause(), player));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDamageBy(EntityDamageByEntityEvent event){
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();
        if(Main.getPlugin().getArenaManager().playerIsAlreadyInArena(player) && Main.getPlugin().getArenaManager().playerIsAlreadyInArena(damager)){
            Arena arena = Main.getPlugin().getArenaManager().getPlayerCurrentArena(player);
            if(arena.getGameState().equals(GameState.RUNNING) || arena.getGameState().equals(GameState.GAME_END)) {
                if (player.getHealth() - event.getFinalDamage() <= 0 && player.getInventory().getItemInOffHand().getType() != Material.TOTEM_OF_UNDYING && player.getInventory().getItemInHand().getType() != Material.TOTEM_OF_UNDYING) {
                    damager.sendMessage(Main.PREFIX+Main.getPlugin().getSpecialsManager().getDeathMessageWithKiller(event.getCause(),damager,player));
                    player.sendMessage(Main.PREFIX+Main.getPlugin().getSpecialsManager().getDeathMessageWithKiller(event.getCause(),damager,player));
                }
            }
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
                blockList.entrySet().stream().filter(entry -> entry.getValue().equals(arena)).forEach(entry -> {entry.getKey().setType(Material.AIR);blockList.remove(entry.getKey());});
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
    public void onPlayerChat(PlayerChatEvent event){
        Player player = event.getPlayer();
        if (Main.getPlugin().getArenaManager().playerIsAlreadyInArena(player)) {
            Arena arena = Main.getPlugin().getArenaManager().getPlayerCurrentArena(player);
            event.setCancelled(true);
            if(!Main.getPlugin().getConfig().getBoolean("messages.player-chat-message.enabled")){
                player.sendMessage(messageManager.getMessage(MessageKey.CHAT_DISABLED));
                return;
            }
            for(UUID uuid : arena.getPlayerInGame()){
                Player p = Bukkit.getPlayer(uuid);
                if(p == null) return;
                String layout = Main.getPlugin().getConfig().getString("messages.player-chat-message.layout").replaceAll("&","§").replaceAll("%player%",player.getDisplayName()).replaceAll("%message%",event.getMessage());
                p.sendMessage(Main.PREFIX+layout);
            }
        }
    }

    @EventHandler
    public void onExplode(BlockExplodeEvent event) {
        if(Main.getPlugin().getArenaManager().isWithinArena(event.getBlock().getLocation())) {
            List<Block> blocksToRemove = new ArrayList<>();
            for (Block block : event.blockList()) {
                if (!blockList.containsKey(block)) {
                    blocksToRemove.add(block);
                }
            }
            event.blockList().removeAll(blocksToRemove);
        }
    }

    @EventHandler
    public void onExplodeEntity(EntityExplodeEvent event) {
        if(Main.getPlugin().getArenaManager().isWithinArena(event.getLocation())) {
            List<Block> blocksToRemove = new ArrayList<>();
            for (Block block : event.blockList()) {
                if (!blockList.containsKey(block)) {
                    blocksToRemove.add(block);
                }
            }
            event.blockList().removeAll(blocksToRemove);
        }
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        if(Main.getPlugin().getArenaManager().isWithinArena(event.getBlock().getLocation())) {
            if (event.getCause() == BlockIgniteEvent.IgniteCause.EXPLOSION) {
                event.setCancelled(true);
            }
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
                winner.sendMessage(messageManager.getMessage(MessageKey.PLAYER_LEAVE_GAME).replaceAll("%player%",player.getDisplayName()));
            }
            if(arena.getGameState().equals(GameState.GAME_END)){
                event.setQuitMessage(null);
                Main.getPlugin().getGameManager().leaveDuel(player,arena);
            }
        }
    }

    @EventHandler
    public void onDisabled(PluginDisableEvent event){
        if(event.getPlugin().equals(Main.getPlugin())){
            Main.getPlugin().getGameManager().endAllDuelsWhenClosing();
            Main.getPlugin().getArenaManager().saveAllArenas();
        }
    }

}
