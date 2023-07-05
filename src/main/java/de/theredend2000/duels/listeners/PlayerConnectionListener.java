package de.theredend2000.duels.listeners;

import de.theredend2000.duels.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerConnectionListener implements Listener {

    public PlayerConnectionListener(){
        Bukkit.getPluginManager().registerEvents(this,Main.getPlugin());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        if(Main.getPlugin().getStatsManager().joinedFirstTime(event.getPlayer())){
            Main.getPlugin().getStatsManager().savePlayerDefaultStats(event.getPlayer());
        }
    }

}
