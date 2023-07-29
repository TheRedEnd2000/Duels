package de.theredend2000.duels.countdowns;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import de.theredend2000.duels.game.GameState;
import de.theredend2000.duels.kits.Kit;
import de.theredend2000.duels.util.MessageKey;
import de.theredend2000.duels.util.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArenaDurationCountdown {

    private final Map<Arena, Integer> timeDuration;
    private final MessageManager messageManager;
    private int taskId;
    private int maxDuration;

    public ArenaDurationCountdown() {
        timeDuration = new HashMap<>();
        messageManager = Main.getPlugin().getMessageManager();
        taskId = -1;
    }

    public void startDurationCountdown(Arena arena) {
        timeDuration.put(arena, 0);
        maxDuration = Main.getPlugin().getConfig().getInt("game.game-duration");

        if (taskId == -1) {
            taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), this::updateCountdown, 20L, 20L);
        }
    }

    private void updateCountdown() {
        for (Arena arena : timeDuration.keySet()) {
            if (arena.getGameState().equals(GameState.RUNNING)) {
                int currentTime = timeDuration.get(arena);
                int timeLeft = maxDuration-currentTime;
                switch (timeLeft){
                    case 60: case 30: case 10: case 5: case 3: case 2: case 1:
                        for(UUID uuid : arena.getPlayerInGame()){
                            Player player = Bukkit.getPlayer(uuid);
                            if(player == null) continue;
                            player.sendMessage(Main.PREFIX+"§cThe battle ends in "+timeLeft+" second(s).");
                        }
                        break;
                }
                if (currentTime == maxDuration) {
                    ArrayList<Player> players = new ArrayList<>();
                    for(UUID uuid : arena.getPlayerInGame()){
                        Player player = Bukkit.getPlayer(uuid);
                        if(player == null) continue;
                        players.add(player);
                    }
                    Kit kit = Main.getPlugin().getArenaKit().get(arena);
                    Main.getPlugin().getGameManager().timerExpiredGameEnd(players,arena,kit);
                    arena.setGameState(GameState.GAME_END);
                } else {
                    timeDuration.put(arena, currentTime + 1);
                }
            }
        }
    }

    public int getCurrentTime(Arena arena) {
        return timeDuration.getOrDefault(arena, 0);
    }
}
