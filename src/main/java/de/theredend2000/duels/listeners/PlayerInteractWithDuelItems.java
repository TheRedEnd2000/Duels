package de.theredend2000.duels.listeners;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import de.theredend2000.duels.arenas.PlayAgain;
import de.theredend2000.duels.game.GameState;
import de.theredend2000.duels.kits.Kit;
import de.theredend2000.duels.util.MessageKey;
import de.theredend2000.duels.util.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerInteractWithDuelItems implements Listener {

    private MessageManager messageManager;

    public PlayerInteractWithDuelItems(){
        Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
        messageManager = Main.getPlugin().getMessageManager();
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
                                    player.sendMessage(messageManager.getMessage(MessageKey.OPPONENT_ALREADY_LEFT));
                                    return;
                                }
                                if (playAgain.getPlayer1() != null) {
                                    if (playAgain.getPlayer1().equals(player)) {
                                        player.sendMessage(messageManager.getMessage(MessageKey.ALREADY_IN_QUEUE));
                                        return;
                                    }

                                    playAgain.setPlayer2(player);
                                    Kit kit = Main.getPlugin().getArenaKit().get(arena);
                                    arena.setGameState(GameState.WAITING);


                                    Main.getPlugin().getGameManager().endDuelWithPlayAgain(arena);
                                    Main.getPlugin().getGameManager().duelPlayer(playAgain.getPlayer1(), playAgain.getPlayer2(), arena, kit);
                                    playAgain.getPlayer1().sendMessage(messageManager.getMessage(MessageKey.ENTERED_REMATCH).replaceAll("%player%", playAgain.getPlayer2().getDisplayName()));
                                    playAgain.getPlayer2().sendMessage(messageManager.getMessage(MessageKey.ENTERED_REMATCH).replaceAll("%player%", playAgain.getPlayer1().getDisplayName()));
                                    Main.getPlugin().getPlayAgainHashMap().remove(arena);
                                } else {
                                    ArrayList<UUID> players = new ArrayList<>(arena.getPlayerInGame());
                                    players.remove(player.getUniqueId());
                                    Player getRealKiller = Bukkit.getPlayer(players.get(0));
                                    if(getRealKiller == null){
                                        player.sendMessage(messageManager.getMessage(MessageKey.OPPONENT_ALREADY_LEFT));
                                        return;
                                    }
                                    playAgain.setPlayer1(player);
                                    player.sendMessage(messageManager.getMessage(MessageKey.ENTERED_QUEUE_AGAINST).replaceAll("%player%", getRealKiller.getDisplayName()));
                                    getRealKiller.sendMessage(messageManager.getMessage(MessageKey.CHALLENGED_TO_REMATCH).replaceAll("%player%", player.getDisplayName()));
                                }
                            }
                            break;
                    }
                }
            }
        }
    }
}
