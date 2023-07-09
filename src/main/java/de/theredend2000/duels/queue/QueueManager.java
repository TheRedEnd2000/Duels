package de.theredend2000.duels.queue;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class QueueManager {

    public QueueManager(){
        sendQueueMessage();
    }

    public boolean isInQueue(Player player) {
        for (Arena arena : Main.getPlugin().getArenaManagerHashMap().values()) {
            if (Main.getPlugin().getQueueManagerHashMap().containsKey(arena)) {
                Queue queueManager = Main.getPlugin().getQueueManagerHashMap().get(arena);
                if (queueManager.getPlayer1() != null && queueManager.getPlayer1().equals(player))
                    return true;
                if (queueManager.getPlayer2() != null && queueManager.getPlayer2().equals(player))
                    return true;
            }
        }
        return false;
    }

    public boolean isInThatQueue(Player player, Arena arena) {
        if (Main.getPlugin().getQueueManagerHashMap().containsKey(arena)) {
            Queue queueManager = Main.getPlugin().getQueueManagerHashMap().get(arena);
            if (queueManager.getPlayer1() != null && queueManager.getPlayer1().equals(player))
                return true;
            if (queueManager.getPlayer2() != null && queueManager.getPlayer2().equals(player))
                return true;
        }
        return false;
    }

    public void addToQueue(Player player, Arena arena) {
        if (Main.getPlugin().getQueueManagerHashMap().containsKey(arena)) {
            Queue queueManager = Main.getPlugin().getQueueManagerHashMap().get(arena);
            if (queueManager.getPlayer1() == null)
                queueManager.setPlayer1(player);
            else if (queueManager.getPlayer2() == null)
                queueManager.setPlayer2(player);
            checkGameStart(arena);
        } else {
            Main.getPlugin().getQueueManagerHashMap().put(arena, new Queue(player, null, arena, Main.getPlugin().getGameManager().getRandomKit(), 1));
        }
    }

    public void removeFromQueue(Player player, Arena arena) {
        if (isInThatQueue(player, arena)) {
            Queue queueManager = Main.getPlugin().getQueueManagerHashMap().get(arena);

            if (queueManager.getPlayer1() != null && queueManager.getPlayer1().equals(player)) {
                queueManager.setPlayer1(null);
            } else if (queueManager.getPlayer2() != null && queueManager.getPlayer2().equals(player)) {
                queueManager.setPlayer2(null);
            }

            // Überprüfen, ob beide Spieler in der Warteschlange null sind
            if (queueManager.getPlayer1() == null && queueManager.getPlayer2() == null) {
                // Entfernen Sie die Arena aus der HashMap
                Main.getPlugin().getQueueManagerHashMap().remove(arena);
            }
        }
    }

    public void removeFromAllQueues(Player player) {
        for (Arena arena : Main.getPlugin().getArenaManagerHashMap().values()) {
            removeFromQueue(player, arena);
        }
    }

    public void checkGameStart(Arena arena){
        if(Main.getPlugin().getQueueManagerHashMap().get(arena).getPlayer1() != null && Main.getPlugin().getQueueManagerHashMap().get(arena).getPlayer2() != null){
            Player player1 = Main.getPlugin().getQueueManagerHashMap().get(arena).getPlayer1();
            Player player2 = Main.getPlugin().getQueueManagerHashMap().get(arena).getPlayer2();
            player1.sendMessage(Main.PREFIX+"§aPlayer found. §7You are dueling §e"+player2.getDisplayName());
            player2.sendMessage(Main.PREFIX+"§aMatch entered. §7You are dueling §e"+player2.getDisplayName());
            Main.getPlugin().getGameManager().duelPlayer(player1,player2,arena,Main.getPlugin().getGameManager().getRandomKit());
            Main.getPlugin().getQueueManagerHashMap().remove(arena);
        }
    }

    public int getPlayerSize(Arena arena){
        int playerSize;
        if(Main.getPlugin().getQueueManagerHashMap().get(arena) != null)
            playerSize = Main.getPlugin().getQueueManagerHashMap().get(arena).getSize();
        else
            playerSize = 0;
        return playerSize;
    }

    public void handelQueue(Player player,Arena arena){
        if (Main.getPlugin().getQueueManager().isInThatQueue(player, arena)) {
            Main.getPlugin().getQueueManager().removeFromQueue(player, arena);
            player.sendMessage(Main.PREFIX + "§7You §cleft §7the queue for the arena §e" + arena.getName());
        } else {
            if (!Main.getPlugin().getQueueManager().isInQueue(player)) {
                Main.getPlugin().getQueueManager().addToQueue(player, arena);
                player.sendMessage(Main.PREFIX + "§7You §ajoined §7the queue for the arena §e" + arena.getName());
            } else {
                player.sendMessage(Main.PREFIX + "§cYou are already in a Queue.");
            }
        }
    }

    private void sendQueueMessage() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
            @Override
            public void run() {
                if(!Main.getPlugin().getConfig().getBoolean("layout.send-actionbar-when-queue")) return;
                for (Queue queue : Main.getPlugin().getQueueManagerHashMap().values()) {
                    Player player1 = queue.getPlayer1();
                    Player player2 = queue.getPlayer2();

                    if (player1 != null) {
                        player1.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent(Main.PREFIX+"§7You are currently in the §dqueue §7for the arena §e"+queue.getArena().getName()));
                    }
                    if (player2 != null) {
                        player2.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent(Main.PREFIX+"§7You are currently in the §dqueue §7for the arena §e"+queue.getArena().getName()));
                    }
                }
            }
        },0,20);
    }

}
