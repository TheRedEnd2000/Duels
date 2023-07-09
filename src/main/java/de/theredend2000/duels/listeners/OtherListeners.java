package de.theredend2000.duels.listeners;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class OtherListeners implements Listener {

    public OtherListeners(){
        Bukkit.getPluginManager().registerEvents(this,Main.getPlugin());
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().substring(1);
        Player player = event.getPlayer();
        if(Main.getPlugin().getArenaManager().playerIsAlreadyInArena(player)){
            if (Main.getPlugin().getConfig().getStringList("lists.blacklisted-commands").contains(command)) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(Main.PREFIX+"§cYou can't use this command right now. Please wait until the game ends.");
            }
        }
    }
}
