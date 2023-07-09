package de.theredend2000.duels.countdowns;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import de.theredend2000.duels.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class ArenaEndCountdown {

    private HashMap<Arena, Integer> timeEnding;

    public ArenaEndCountdown(){
        timeEnding = new HashMap<>();
        startRunnable();
    }

    public void addEndingCountdownForArena(Arena arena){
        int endingtime = Main.getPlugin().getConfig().getInt("game.ending-time");
        timeEnding.put(arena, endingtime);
    }

    private void startRunnable(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Arena arena : timeEnding.keySet()){
                    if(arena.getGameState().equals(GameState.GAME_END)) {
                        int currentTime = timeEnding.get(arena);
                        for (UUID uuid : arena.getPlayerInGame()) {
                            Player player = Bukkit.getPlayer(uuid);
                            if (player == null) return;
                            player.sendMessage(Main.PREFIX+"§7Battle ends in §e"+currentTime+" seconds§c.");
                            Main.getPlugin().getItemManager().setPlayAgainItem(player);
                            Main.getPlugin().getItemManager().setLeaveItem(player);
                        }
                        if(currentTime > 1 && arena.getPlayerInGame() != null)
                            Main.getPlugin().getSpecialsManager().spawnRandomFirework(arena);
                        if (currentTime == 0) {
                            arena.setGameState(GameState.WAITING);
                            timeEnding.remove(arena);
                            Main.getPlugin().getGameManager().endDuel(arena);
                        }
                        if(arena.getGameState().equals(GameState.GAME_END)) {
                            currentTime--;
                            timeEnding.put(arena, currentTime);
                        }
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(),0,20);
    }

    public int getCurrentTime(Arena arena){
        return timeEnding.get(arena);
    }
}
