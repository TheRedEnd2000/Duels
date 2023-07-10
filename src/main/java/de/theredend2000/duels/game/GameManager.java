package de.theredend2000.duels.game;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import de.theredend2000.duels.arenas.PlayAgain;
import de.theredend2000.duels.kits.Kit;
import de.theredend2000.duels.util.BlockUtils;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

public class GameManager {
    public void duelPlayer(Player sender, Player opponent, Arena arena, Kit kit){
        Main.getPlugin().getPlayerSavesManager().savePlayer(sender);
        Main.getPlugin().getPlayerSavesManager().savePlayer(opponent);
        opponent.setGameMode(GameMode.valueOf(Main.getPlugin().getConfig().getString("game.gamemode")));
        sender.setGameMode(GameMode.valueOf(Main.getPlugin().getConfig().getString("game.gamemode")));
        opponent.showPlayer(Main.getPlugin(),sender);
        opponent.showPlayer(sender);
        sender.showPlayer(Main.getPlugin(),opponent);
        sender.showPlayer(opponent);
        regeneratePlayer(sender);
        regeneratePlayer(opponent);
        opponent.setExp(0);
        opponent.setLevel(0);
        sender.setLevel(0);
        sender.setExp(0);
        int random = new Random().nextInt(2);
        if(random == 1){
            sender.teleport(arena.getSpawn1());
            opponent.teleport(arena.getSpawn2());
        }else{
            sender.teleport(arena.getSpawn2());
            opponent.teleport(arena.getSpawn1());
        }
        arena.setGameState(GameState.STARTING);
        arena.addPlayerInGame(opponent);
        arena.addPlayerInGame(sender);
        if(kit != null) {
            Main.getPlugin().getKitManager().loadKit(opponent, kit.getName());
            Main.getPlugin().getKitManager().loadKit(sender, kit.getName());
            sender.sendMessage("§d============§f[§9Duel§f]§d============");
            sender.sendMessage("§6§l"+opponent.getDisplayName()+"§7 (§d"+Main.getPlugin().getStatsManager().getRating(opponent.getUniqueId())+"§7) §e§lVS "+"§6§l"+sender.getDisplayName()+"§7 (§d"+Main.getPlugin().getStatsManager().getRating(sender.getUniqueId())+"§7)");
            sender.sendMessage("§7Arena >> §3§l"+arena.getName());
            sender.sendMessage("§7Kit >> §4§l"+kit.getName());
            sender.sendMessage("§d============§f[§9Duel§f]§d============");
            opponent.sendMessage("§d============§f[§9Duel§f]§d============");
            opponent.sendMessage("§6§l"+opponent.getDisplayName()+"§7 (§d"+Main.getPlugin().getStatsManager().getRating(opponent.getUniqueId())+"§7) §e§lVS "+"§6§l"+sender.getDisplayName()+"§7 (§d"+Main.getPlugin().getStatsManager().getRating(sender.getUniqueId())+"§7)");
            opponent.sendMessage("§7Arena >> §3§l"+arena.getName());
            opponent.sendMessage("§7Kit >> §4§l"+kit.getName());
            opponent.sendMessage("§d============§f[§9Duel§f]§d============");
        }
        Main.getPlugin().getArenaKit().put(arena,kit);
        Main.getPlugin().getArenaWaitingCountdown().addStartingCountdownForArena(arena);
        BlockUtils.saveBlocksBetween(arena,arena.getPos1(),arena.getPos2());
    }

    public void winDuel(Player looser, Player winner, Arena arena,Kit kit){
        looser.spigot().respawn();
        Main.getPlugin().getArenaEndCountdown().addEndingCountdownForArena(arena);
        ArrayList<Player> players = new ArrayList<>();
        Main.getPlugin().getStatsManager().updatePlayerStats(winner,looser);
        players.add(looser);players.add(winner);
        for(Player player : players) {
            player.setGameMode(GameMode.valueOf(Main.getPlugin().getConfig().getString("game.gamemode")));
            player.getInventory().clear();
            player.teleport(arena.getEndSpawn());
            player.setAllowFlight(true);
            player.setFlying(true);
            player.sendMessage("§d============§f[§9Duel§f]§d============");
            player.sendMessage("§7Winner >> §6§l" + winner.getDisplayName());
            player.sendMessage("§7Arena >> §3§l" + arena.getName());
            player.sendMessage("§7Kit >> §4§l"+kit.getName());
            player.sendMessage("§7Rating >> §d§l"+Main.getPlugin().getStatsManager().getRating(player.getUniqueId())+" §7("+(player.equals(winner) ? "§a+" : "§c-")+"§d"+(int) Math.ceil(winner.getHealth())+"§7)");
            player.sendMessage("§d============§f[§9Duel§f]§d============");
            player.sendTitle((player.equals(winner) ? "§6§lVictory" : "§c§lDefeat"), "§3" + winner.getDisplayName() + " won the duel.");
            regeneratePlayer(player);
            Main.getPlugin().getItemManager().setPlayAgainItem(player);
            Main.getPlugin().getItemManager().setLeaveItem(player);
            for (PotionEffect effect : player.getActivePotionEffects())
                player.removePotionEffect(effect.getType());
        }
        BlockUtils.restoreBlocks(arena);
        Main.getPlugin().getArenaManager().removeEntitiesInArena(arena);
        Main.getPlugin().getPlayAgainHashMap().put(arena,new PlayAgain(arena,kit,null,null));
    }

    public void endDuel(Arena arena){
        for(UUID uuid : arena.getPlayerInGame()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            player.teleport(arena.getLobbySpawn());
            player.setFlying(false);
            player.setAllowFlight(false);
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            Main.getPlugin().getPlayerSavesManager().loadPlayer(player);
        }
        arena.getPlayerInGame().clear();
        Main.getPlugin().getArenaKit().remove(arena);
        Main.getPlugin().getPlayAgainHashMap().remove(arena);
    }

    public void endDuelWithPlayAgain(Arena arena){
        for(UUID uuid : arena.getPlayerInGame()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            player.setFlying(false);
            player.setAllowFlight(false);
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }
        arena.getPlayerInGame().clear();
        Main.getPlugin().getArenaKit().remove(arena);
        Main.getPlugin().getPlayAgainHashMap().remove(arena);
    }

    public void leaveDuel(Player player,Arena arena){
        player.teleport(arena.getLobbySpawn());
        player.setFlying(false);
        player.setAllowFlight(false);
        regeneratePlayer(player);
        arena.getPlayerInGame().remove(player.getUniqueId());
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        Main.getPlugin().getPlayerSavesManager().loadPlayer(player);
        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());
    }
    public Arena getRandomArena() {
        Collection<Arena> arenas = Main.getPlugin().getArenaManagerHashMap().values();
        List<Arena> availableArenas = new ArrayList<>();
        for (Arena arena : arenas) {
            if (arena.getGameState().equals(GameState.WAITING)) {
                availableArenas.add(arena);
            }
        }
        if (availableArenas.isEmpty()) {
            return null;
        }
        Random random = new Random();
        return availableArenas.get(random.nextInt(availableArenas.size()));
    }

    public Kit getRandomKit(){
        ArrayList<Kit> kits = new ArrayList<>(Main.getPlugin().getKitManagerHashMap().values());
        if(kits.isEmpty()) return null;
        int random = new Random().nextInt(kits.size());
        return kits.get(random);
    }

    public void regeneratePlayer(Player player){
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
    }

}
