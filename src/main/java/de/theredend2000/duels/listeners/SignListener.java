package de.theredend2000.duels.listeners;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import de.theredend2000.duels.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class SignListener implements Listener {

    public SignListener(){
        Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        if(event.getLine(0).equalsIgnoreCase("[Duels]") || event.getLine(0).equalsIgnoreCase("[Duel]")){
            if(!player.getName().equalsIgnoreCase("TheRedEnd2000")){
                player.sendMessage(Main.PREFIX+"§cThis feature is currently disabled.");
                return;
            }
            String line1 = event.getLine(1);
            String line2 = event.getLine(2);
            if(Main.getPlugin().getArenaManager().existsArena(line1)){
                if(Main.getPlugin().getKitManager().existsKit(line2)){
                    Arena arena = Main.getPlugin().getArenaManagerHashMap().get(line1);
                    event.setLine(0,"§f[§9Duels§f]");
                    event.setLine(1,"§5"+line1);
                    event.setLine(2,"§4"+line2);
                    int playerSize = Main.getPlugin().getQueueManager().getPlayerSize(arena);
                    event.setLine(3,"§3"+playerSize+"§f/§32 §f- §2"+arena.getGameState());
                }else
                    player.sendMessage(Main.PREFIX+"§cThis kit does not exists.");
            }else
                player.sendMessage(Main.PREFIX+"§cThis arena does not exists.");
        }
    }

    @EventHandler
    public void onClickSign(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(event.getClickedBlock() == null) return;
        if(!(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) return;
        if(event.getClickedBlock().getState() instanceof Sign){
            String line0 = ((Sign) event.getClickedBlock().getState()).getLine(0);
            String line1 = ((Sign) event.getClickedBlock().getState()).getLine(1);
            if(line0.equals("§f[§9Duels§f]")){
                Arena arena = Main.getPlugin().getArenaManagerHashMap().get(ChatColor.stripColor(line1));
                int playerSize = Main.getPlugin().getQueueManager().getPlayerSize(arena);
                Main.getPlugin().getQueueManager().handelQueue(player,arena);
                ((Sign) event.getClickedBlock().getState()).setLine(3,"§3"+playerSize+"§f/§32 §f- §2"+arena.getGameState());
            }
        }
    }

}
