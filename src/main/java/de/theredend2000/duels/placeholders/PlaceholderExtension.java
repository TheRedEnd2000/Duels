package de.theredend2000.duels.placeholders;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import de.theredend2000.duels.extramanagers.ScoreboardManagers;
import de.theredend2000.duels.game.GameState;
import de.theredend2000.duels.kits.Kit;
import de.theredend2000.duels.stats.StatsManager;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlaceholderExtension extends PlaceholderExpansion {

    @Override
    public String getAuthor() {
        return "theredend2000";
    }

    @Override
    public String getIdentifier() {
        return "duels";
    }

    @Override
    public String getVersion() {
        return Main.getPlugin().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if(params.equalsIgnoreCase("game_arena")){
            Arena arena = Main.getPlugin().getArenaManager().getPlayerCurrentArena(player);
            if(arena == null) return "§cNot available";
            return String.valueOf(arena.getName());
        }else if(params.equalsIgnoreCase("game_kit")){
            Arena arena = Main.getPlugin().getArenaManager().getPlayerCurrentArena(player);
            if(arena == null) return "§cNot available";
            Kit kit = Main.getPlugin().getArenaKit().get(arena);
            return String.valueOf(kit.getName());
        }else if(params.equalsIgnoreCase("game_player_rating")){
            if(Main.getPlugin().getArenaManager().playerIsAlreadyInArena(player)) {
                return String.valueOf(Main.getPlugin().getStatsManager().getRating(player.getUniqueId()));
            }else
                return "§cNot available";
        }else if(params.equalsIgnoreCase("game_player_name")){
            if(Main.getPlugin().getArenaManager().playerIsAlreadyInArena(player)) {
                return player.getDisplayName();
            }else
                return "§cNot available";
        }else if(params.equalsIgnoreCase("game_player_health")){
            if(Main.getPlugin().getArenaManager().playerIsAlreadyInArena(player)) {
                return String.valueOf(Main.getPlugin().getScoreboardManagers().getHealthString(player.getHealth()));
            }else
                return "§cNot available";
        }else if(params.equalsIgnoreCase("game_opponent_rating")){
            if(Main.getPlugin().getArenaManager().playerIsAlreadyInArena(player)) {
                Arena arena = Main.getPlugin().getArenaManager().getPlayerCurrentArena(player);
                Player opponent = Main.getPlugin().getArenaManager().getOpponent(arena,player);
                if(opponent != null){
                    return String.valueOf(Main.getPlugin().getStatsManager().getRating(opponent.getUniqueId()));
                }else
                    return "§cNot available";
            }else
                return "§cNot available";
        }else if(params.equalsIgnoreCase("game_opponent_name")){
            if(Main.getPlugin().getArenaManager().playerIsAlreadyInArena(player)) {
                Arena arena = Main.getPlugin().getArenaManager().getPlayerCurrentArena(player);
                Player opponent = Main.getPlugin().getArenaManager().getOpponent(arena,player);
                if(opponent != null){
                    return opponent.getDisplayName();
                }else
                    return "§cNot available";
            }else
                return "§cNot available";
        }else if(params.equalsIgnoreCase("game_opponent_health")){
            if(Main.getPlugin().getArenaManager().playerIsAlreadyInArena(player)) {
                Arena arena = Main.getPlugin().getArenaManager().getPlayerCurrentArena(player);
                Player opponent = Main.getPlugin().getArenaManager().getOpponent(arena,player);
                if(opponent != null){
                    return String.valueOf(Main.getPlugin().getScoreboardManagers().getHealthString(opponent.getHealth()));
                }else
                    return "§cNot available";
            }else
                return "§cNot available";
        }else if(params.equalsIgnoreCase("game_starting_seconds")){
            if(Main.getPlugin().getArenaManager().playerIsAlreadyInArena(player)) {
                Arena arena = Main.getPlugin().getArenaManager().getPlayerCurrentArena(player);
                int seconds = Main.getPlugin().getArenaWaitingCountdown().getCurrentTime(arena);
                return String.valueOf(Main.getPlugin().getArenaWaitingCountdown().getCurrentTime(arena));
            }else
                return "§cNot available";
        }else if(params.equalsIgnoreCase("game_ending_seconds")){
            if(Main.getPlugin().getArenaManager().playerIsAlreadyInArena(player)) {
                Arena arena = Main.getPlugin().getArenaManager().getPlayerCurrentArena(player);
                return String.valueOf(Main.getPlugin().getArenaEndCountdown().getCurrentTime(arena));
            }else
                return "§cNot available";
        }

        return null;
    }

}
