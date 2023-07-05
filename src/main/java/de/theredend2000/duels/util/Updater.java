package de.theredend2000.duels.util;

import de.theredend2000.duels.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Updater implements Listener {

    private Main plugin;
    private int key = 0;

    public Updater(Main plugin){
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this,plugin);
        isOutdated();
    }

    public boolean isOutdated(Player player) {
        try {
            HttpURLConnection c = (HttpURLConnection)new URL("https://api.spigotmc.org/legacy/update.php?resource="+key).openConnection();
            String newVersion = new BufferedReader(new InputStreamReader(c.getInputStream())).readLine();
            c.disconnect();
            String oldVersion = plugin.getDescription().getVersion();
            if(!newVersion.equals(oldVersion)) {
                player.sendMessage(Main.PREFIX+"§aThere is a newer version available. Please update your plugin§a. §aVersion: §2§l"+oldVersion+"§6 --> §2§l"+newVersion);
                return true;
            }
        }
        catch(Exception e) {
            player.sendMessage(Main.PREFIX+"§4§lERROR: §cCould not make connection to SpigotMC.org");
            e.printStackTrace();
        }
        return false;
    }
    public boolean isOutdated() {
        boolean updates = plugin.getConfig().getBoolean("check-for-updates");
        if(updates) {
            try {
                HttpURLConnection c = (HttpURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=" + key).openConnection();
                String newVersion = new BufferedReader(new InputStreamReader(c.getInputStream())).readLine();
                c.disconnect();
                String oldVersion = plugin.getDescription().getVersion();
                if (!newVersion.equals(oldVersion)) {
                    Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§cYou do not have the most updated version of §9Duels§c.");
                    Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§cPlease chance the version: §4" + oldVersion + "§6 --> §2§l" + newVersion);
                    return true;
                }
            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§4§lERROR: §cCould not make connection to SpigotMC.org");
                e.printStackTrace();
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        boolean updates = plugin.getConfig().getBoolean("check-for-updates");
        if(updates){
            if(!player.isOp()) return;
            isOutdated(player);
        }
    }

}
