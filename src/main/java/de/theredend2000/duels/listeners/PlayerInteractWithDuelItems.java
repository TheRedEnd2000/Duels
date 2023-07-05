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
                                if (playAgain.getPlayer1() != null) {
                                    if (playAgain.getPlayer1().equals(player)) {
                                        player.sendMessage(Main.PREFIX + "§cYou have already clicked this.");
                                        return;
                                    }

                                    playAgain.setPlayer2(player);
                                    Kit kit = Main.getPlugin().getArenaKit().get(arena);
                                    arena.setGameState(GameState.WAITING);

                                    // Make sure to initialize and store the PlayAgain object for the next rematch

                                    Main.getPlugin().getGameManager().endDuelWithPlayAgain(arena);
                                    Main.getPlugin().getGameManager().duelPlayer(playAgain.getPlayer1(), playAgain.getPlayer2(), arena, kit);
                                    playAgain.getPlayer1().sendMessage(Main.PREFIX + "§aYou have successfully entered a rematch with " + playAgain.getPlayer2().getDisplayName());
                                    playAgain.getPlayer2().sendMessage(Main.PREFIX + "§aYou have successfully entered a rematch with " + playAgain.getPlayer1().getDisplayName());
                                } else {
                                    playAgain.setPlayer1(player);
                                    Bukkit.broadcastMessage("Duel want");
                                }
                            }
                            break;
                    }
                }
            }
        }
    }
}
