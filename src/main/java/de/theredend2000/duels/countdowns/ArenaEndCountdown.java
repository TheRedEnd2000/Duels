package de.theredend2000.duels.countdowns;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import de.theredend2000.duels.game.GameState;
import de.theredend2000.duels.util.MessageKey;
import de.theredend2000.duels.util.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArenaEndCountdown {

    private final Map<Arena, Integer> timeEnding;
    private final MessageManager messageManager;
    private int taskId;

    public ArenaEndCountdown() {
        timeEnding = new HashMap<>();
        messageManager = Main.getPlugin().getMessageManager();
        taskId = -1;
    }

    public void addEndingCountdownForArena(Arena arena) {
        int endingTime = Main.getPlugin().getConfig().getInt("game.ending-time.seconds");
        timeEnding.put(arena, endingTime);

        if (taskId == -1) {
            taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), this::updateCountdown, 20L, 20L);
        }
    }

    private void updateCountdown() {
        for (Arena arena : timeEnding.keySet()) {
            if (arena.getGameState().equals(GameState.GAME_END)) {
                int currentTime = timeEnding.get(arena);
                for (UUID uuid : arena.getPlayerInGame()) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player == null) continue;

                    if (Main.getPlugin().getConfig().getBoolean("game.ending-time.title")) {
                        player.sendTitle(
                                messageManager.getMessage(MessageKey.BATTLE_END_TITLE).replaceAll("%seconds%", String.valueOf(currentTime)),
                                messageManager.getMessage(MessageKey.BATTLE_END_SUBTITLE).replaceAll("%seconds%", String.valueOf(currentTime))
                        );
                    }

                    if (Main.getPlugin().getConfig().getBoolean("game.ending-time.message")) {
                        if (currentTime == 0) {
                            player.sendMessage(messageManager.getMessage(MessageKey.BATTLE_ENDED_MESSAGE).replaceAll("%seconds%", String.valueOf(currentTime)));
                        } else {
                            player.sendMessage(messageManager.getMessage(MessageKey.BATTLE_END_MESSAGE).replaceAll("%seconds%", String.valueOf(currentTime)));
                        }
                    }

                    Main.getPlugin().getItemManager().setPlayAgainItem(player);
                    Main.getPlugin().getItemManager().setLeaveItem(player);
                }

                if (currentTime > 1 && arena.getPlayerInGame() != null) {
                    Main.getPlugin().getSpecialsManager().spawnRandomFirework(arena);
                }

                if (currentTime == 0) {
                    arena.setGameState(GameState.WAITING);
                    timeEnding.remove(arena);
                    Main.getPlugin().getGameManager().endDuel(arena);
                } else {
                    timeEnding.put(arena, currentTime - 1);
                }
            }
        }

        if (timeEnding.isEmpty() && taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
            taskId = -1;
        }
    }

    public int getCurrentTime(Arena arena) {
        return timeEnding.getOrDefault(arena, 0);
    }
}
