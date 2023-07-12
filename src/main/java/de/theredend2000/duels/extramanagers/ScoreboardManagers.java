package de.theredend2000.duels.extramanagers;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import de.theredend2000.duels.game.GameState;
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
                                    objective.getScore("§9  ➤ §a§l"+showArrowIndicator(player,getOpponent) +" §9"+ getOpponent.getDisplayName()+" §4"+getOpponent.getHealth()).setScore(7);
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
                        }
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(),0,20);
    }

    private String getHeartString(double health) {
        int maxHealth = 20;
        int hearts = (int) Math.ceil(health / 2.0);

        StringBuilder heartString = new StringBuilder();
        for (int i = 0; i < maxHealth / 2; i++) {
            if (i < hearts) {
                heartString.append("§c❤");
            } else {
                heartString.append("§7❤");
            }
        }
        return heartString.toString();
    }

    public String showArrowIndicator(Player player, Player target) {
        Location playerLocation = player.getLocation();
        Location targetLocation = target.getLocation();

        double deltaX = targetLocation.getX() - playerLocation.getX();
        double deltaZ = targetLocation.getZ() - playerLocation.getZ();

        double angle = Math.atan2(deltaZ, deltaX) * 180 / Math.PI;
        angle += 180; // Um den Winkel in den Bereich von 0 bis 360 Grad zu bringen

        if (angle >= 22.5 && angle < 67.5) {
            return "↗";
        } else if (angle >= 67.5 && angle < 112.5) {
            return "→";
        } else if (angle >= 112.5 && angle < 157.5) {
            return "↘";
        } else if (angle >= 157.5 && angle < 202.5) {
            return "↓";
        } else if (angle >= 202.5 && angle < 247.5) {
            return "↙";
        } else if (angle >= 247.5 && angle < 292.5) {
            return "←";
        } else if (angle >= 292.5 && angle < 337.5) {
            return "↖";
        } else {
            return "↑";
        }
    }


    private float normalizeYaw(float yaw) {
        yaw %= 360;
        if (yaw < -180) {
            yaw += 360;
        } else if (yaw > 180) {
            yaw -= 360;
        }
        return yaw;
    }

    private float getYawBetweenLocations(Location from, Location to) {
        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();
        float yaw = (float) Math.toDegrees(Math.atan2(dz, -dx));
        return normalizeYaw(yaw);
    }

}
