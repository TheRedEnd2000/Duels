package de.theredend2000.duels.listeners;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import de.theredend2000.duels.arenas.PlayAgain;
import de.theredend2000.duels.game.GameState;
import de.theredend2000.duels.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerInteractWithDuelItems implements Listener {

    public PlayerInteractWithDuelItems(){
        Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(Main.getPlugin().getArenaManager().playerIsAlreadyInArena(player)){
            Arena arena = Main.getPlugin().getArenaManager().getPlayerCurrentArena(player);
            if(arena.getGameState().equals(GameState.GAME_END)){
                if(event.getItem() != null && event.getItem().getItemMeta() != null && event.getItem().getItemMeta().hasLocalizedName()){
                    switch (event.getItem().getItemMeta().getLocalizedName()){
                        case "arena.leave-match":
                            player.getInventory().clear();
                            arena.getPlayerInGame().remove(player.getUniqueId());
                            Main.getPlugin().getGameManager().leaveDuel(player,arena);
                            break;
                        case "arena.play-again":
                            if (Main.getPlugin().getPlayAgainHashMap().containsKey(arena)) {
                                PlayAgain playAgain = Main.getPlugin().getPlayAgainHashMap().get(arena);
                                if(arena.getPlayerInGame().size() == 1){
                                    player.sendMessage(Main.PREFIX+"§cYour opponent has already left the duel.");
                                    return;
                                }
                                if (playAgain.getPlayer1() != null) {
                                    if (playAgain.getPlayer1().equals(player)) {
                                        player.sendMessage(Main.PREFIX + "§cYou have already been queued.");
                                        return;
                                    }

                                    playAgain.setPlayer2(player);
                                    Kit kit = Main.getPlugin().getArenaKit().get(arena);
                                    arena.setGameState(GameState.WAITING);


                                    Main.getPlugin().getGameManager().endDuelWithPlayAgain(arena);
                                    Main.getPlugin().getGameManager().duelPlayer(playAgain.getPlayer1(), playAgain.getPlayer2(), arena, kit);
                                    playAgain.getPlayer1().sendMessage(Main.PREFIX + "§aYou have successfully entered a rematch with " + playAgain.getPlayer2().getDisplayName());
                                    playAgain.getPlayer2().sendMessage(Main.PREFIX + "§aYou have successfully entered a rematch with " + playAgain.getPlayer1().getDisplayName());
                                    Main.getPlugin().getPlayAgainHashMap().remove(arena);
                                } else {
                                    ArrayList<UUID> players = new ArrayList<>(arena.getPlayerInGame());
                                    players.remove(player.getUniqueId());
                                    Player getRealKiller = Bukkit.getPlayer(players.get(0));
                                    if(getRealKiller == null){
                                        player.sendMessage(Main.PREFIX+"§cYour opponent has already left the duel.");
                                        return;
                                    }
                                    playAgain.setPlayer1(player);
                                    player.sendMessage(Main.PREFIX+"§aYou have been queue to rematch against "+getRealKiller.getDisplayName());
                                    getRealKiller.sendMessage(Main.PREFIX+"§6"+player.getDisplayName()+" §achallenged you to a rematch.");
                                }
                            }
                            break;
                    }
                }
            }
        }
    }
}
