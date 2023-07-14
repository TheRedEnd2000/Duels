package de.theredend2000.duels.extramanagers;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import de.theredend2000.duels.game.GameState;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.UUID;

public class ScoreboardManagers {

    public void updateScoreboard(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player : Bukkit.getOnlinePlayers()){
                    if(Main.getPlugin().getArenaManager().playerIsAlreadyInArena(player)) {
                        Arena arena = Main.getPlugin().getArenaManager().getPlayerCurrentArena(player);
                        if (Main.getPlugin().getConfig().getBoolean("game.scoreboard")) {
                            ArrayList<UUID> players = new ArrayList<>(arena.getPlayerInGame());
                            players.remove(player.getUniqueId());
                            Player getOpponent = Bukkit.getPlayer(players.get(0));
                            if(getOpponent == null) continue;
                            Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
                            Objective objective = board.registerNewObjective("duel", "dummy");
                            objective.setDisplayName("§9Duels");
                            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                            GameState gameState = arena.getGameState();
                            switch (gameState) {
                                case STARTING:
                                    objective.getScore("§a").setScore(9);
                                    objective.getScore("§7Starting in:").setScore(8);
                                    objective.getScore("§6  ➤ " + Main.getPlugin().getArenaWaitingCountdown().getCurrentTime(arena)+" seconds").setScore(7);
                                    objective.getScore("§b").setScore(6);
                                    objective.getScore("§7Opponent:").setScore(5);
                                    objective.getScore("§9  ➤ " + getOpponent.getDisplayName()).setScore(4);
                                    objective.getScore("§2").setScore(3);
                                    objective.getScore("§7Arena:").setScore(2);
                                    objective.getScore("§6  ➤ " + arena.getName()).setScore(1);
                                    objective.getScore("§d-=-=-=-=-=-=-=-=-=-").setScore(0);
                                    break;
                                case RUNNING:
                                    objective.getScore("§b").setScore(9);
                                    objective.getScore("§7Opponent:").setScore(8);
                                    objective.getScore("§9  ➤ §a§l"+showArrowIndicator(player,getOpponent)+getDistance(player,getOpponent) +"m §9"+ getOpponent.getDisplayName()+" §4"+getHealthString(getOpponent.getHealth())).setScore(7);
                                    objective.getScore("§c").setScore(6);
                                    objective.getScore("§7Kit").setScore(5);
                                    objective.getScore("§6  ➤ " + Main.getPlugin().getArenaKit().get(arena).getName()).setScore(4);
                                    objective.getScore("§2").setScore(3);
                                    objective.getScore("§7Arena:").setScore(2);
                                    objective.getScore("§6  ➤ " + arena.getName()).setScore(1);
                                    objective.getScore("§d-=-=-=-=-=-=-=-=-=-").setScore(0);
                                    break;
                                case GAME_END:
                                    objective.getScore("§a").setScore(6);
                                    objective.getScore("§7Ending in:").setScore(5);
                                    objective.getScore("§6  ➤ " + Main.getPlugin().getArenaEndCountdown().getCurrentTime(arena)+" seconds").setScore(4);
                                    objective.getScore("§b").setScore(3);
                                    objective.getScore("§7Arena:").setScore(2);
                                    objective.getScore("§6  ➤ " + arena.getName()).setScore(1);
                                    objective.getScore("§d-=-=-=-=-=-=-=-=-=-").setScore(0);
                                    break;
                            }
                            player.setScoreboard(board);
                            String arrowIndicator = showArrowIndicator(player, getOpponent);
                            sendActionBar(player, arrowIndicator);
                        }
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(),0,5);
    }

    public String getHealthString(double health) {
        return String.format("%.1f", health).replace(",", ".");
    }

    public String getDistance(Player player, Player target){
        double distance = player.getLocation().distance(target.getLocation());
        return String.format("%.1f", distance).replace(",", ".");
    }


    public void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
    }

    public String showArrowIndicator(Player player, Player target) {
        Location playerLocation = player.getLocation();
        Location targetLocation = target.getLocation();

        double deltaX = targetLocation.getX() - playerLocation.getX();
        double deltaZ = targetLocation.getZ() - playerLocation.getZ();

        double angle = Math.atan2(-deltaX, deltaZ);
        double degrees = Math.toDegrees(angle) + 180;

        int sector = (int) Math.round(degrees / 45.0) % 8;

        switch (sector) {
            case 0:
                return "↑";
            case 1:
                return "↗";
            case 2:
                return "→";
            case 3:
                return "↘";
            case 4:
                return "↓";
            case 5:
                return "↙";
            case 6:
                return "←";
            case 7:
                return "↖";
            default:
                return "↑";
        }
    }
}
