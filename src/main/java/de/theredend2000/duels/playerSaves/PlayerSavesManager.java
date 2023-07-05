package de.theredend2000.duels.playerSaves;

import de.theredend2000.duels.Main;
import org.bukkit.entity.Player;

public class PlayerSavesManager {

    public void savePlayer(Player player){
        if(!containsPlayer(player)){
            PlayerSaves ps = new PlayerSaves(player,player.getLevel(),player.getExp(),player.getFoodLevel(),player.getHealth(),player.getInventory().getStorageContents(),player.getInventory().getArmorContents(),player.getInventory().getExtraContents(),player.getGameMode());
            Main.getPlugin().getPlayerSavesHashMap().put(player,ps);
        }
    }

    public void loadPlayer(Player player){
        if(containsPlayer(player)){
            PlayerSaves ps = Main.getPlugin().getPlayerSavesHashMap().get(player);
            player.setLevel(ps.getLevel());
            player.setExp(ps.getExp());
            player.setFoodLevel(ps.getFoodLevel());
            player.setHealth(ps.getHealth());
            player.getInventory().setStorageContents(ps.getPlayerInventory());
            player.getInventory().setArmorContents(ps.getArmorContents());
            player.getInventory().setExtraContents(ps.getExtraContents());
            player.setGameMode(ps.getGameMode());
            Main.getPlugin().getPlayerSavesHashMap().remove(player);
        }
    }

    public boolean containsPlayer(Player player){
        return Main.getPlugin().getPlayerSavesHashMap().containsKey(player);
    }

}
