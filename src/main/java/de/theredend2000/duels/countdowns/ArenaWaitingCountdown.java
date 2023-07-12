package de.theredend2000.duels.countdowns;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import de.theredend2000.duels.game.GameState;
import de.theredend2000.duels.util.MessageKey;
import de.theredend2000.duels.util.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class ArenaWaitingCountdown {

    private HashMap<Arena, Integer> timeStarting;
    private MessageManager messageManager;

    public ArenaWaitingCountdown(){
        timeStarting = new HashMap<>();
        startRunnable();
        messageManager = Main.getPlugin().getMessageManager();
    }

    public void addStartingCountdownForArena(Arena arena){
        int startingtime = Main.getPlugin().getConfig().getInt("game.waiting-time.seconds");
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
                            if(Main.getPlugin().getConfig().getBoolean("game.waiting-time.title")){
                                if (currentTime == 0)
                                    player.sendTitle(messageManager.getMessage(MessageKey.BATTLE_STARTED_TITLE).replaceAll("%seconds%", String.valueOf(currentTime)), messageManager.getMessage(MessageKey.BATTLE_STARTED_SUBTITLE).replaceAll("%seconds%", String.valueOf(currentTime)));
                                else
                                    player.sendTitle(messageManager.getMessage(MessageKey.BATTLE_START_TITLE).replaceAll("%seconds%", String.valueOf(currentTime)), messageManager.getMessage(MessageKey.BATTLE_START_SUBTITLE).replaceAll("%seconds%", String.valueOf(currentTime)));
                            }
                            if(Main.getPlugin().getConfig().getBoolean("game.waiting-time.message")){
                                if (currentTime == 0)
                                    player.sendMessage(messageManager.getMessage(MessageKey.BATTLE_STARTED_MESSAGE).replaceAll("%seconds%", String.valueOf(currentTime)));
                                else
                                    player.sendMessage(messageManager.getMessage(MessageKey.BATTLE_START_MESSAGE).replaceAll("%seconds%", String.valueOf(currentTime)));
                            }
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
