package de.theredend2000.duels.countdowns;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import de.theredend2000.duels.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class ArenaWaitingCountdown {

    private HashMap<Arena, Integer> timeStarting;

    public ArenaWaitingCountdown(){
        timeStarting = new HashMap<>();
        startRunnable();
    }

    public void addStartingCountdownForArena(Arena arena){
        int startingtime = Main.getPlugin().getConfig().getInt("game.waiting-time");
        timeStarting.put(arena, startingtime);
    }

    private void startRunnable(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Arena arena : timeStarting.keySet()){
                    if(arena.getGameState().equals(GameState.STARTING)) {
                        int currentTime = timeStarting.get(arena);
                        for (UUID uuid : arena.getPlayerInGame()) {
                            Player player = Bukkit.getPlayer(uuid);
                            if (player == null) return;
                            if (currentTime == 0)
                                player.sendTitle("§2§lFight", "§bKill your opponent.");
                            else
                                player.sendTitle("§6§l" + currentTime, "§bGet ready for battle.");
                        }
                        if (currentTime == 0) {
                            arena.setGameState(GameState.RUNNING);
                            timeStarting.remove(arena);
                        }
                        if(arena.getGameState().equals(GameState.STARTING)) {
                            currentTime--;
                            timeStarting.put(arena, currentTime);
                        }
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(),0,20);
    }

    public int getCurrentTime(Arena arena){
        return timeStarting.get(arena);
    }
}
