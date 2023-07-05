package de.theredend2000.duels.extramanagers;

import de.theredend2000.duels.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    public void setPlayAgainItem(Player player){
        Material material = Main.getPlugin().getMaterial(Main.getPlugin().getConfig().getString("items.play-again-item.material"));
        String displayname = Main.getPlugin().getConfig().getString("items.play-again-item.displayname").replaceAll("&","§");
        int slot = Main.getPlugin().getConfig().getInt("items.play-again-item.slot");
        List<String> lore = Main.getPlugin().getConfig().getStringList("items.play-again-item.lore");
        List<String> modifiedLore = new ArrayList<>();
        for (String line : lore) {
            String modifiedLine = line.replace("&", "§");
            modifiedLore.add(modifiedLine);
        }

        ItemStack is = new ItemStack(material);
        ItemMeta meta = is.getItemMeta();
        assert meta != null;
        meta.setLore(modifiedLore);
        meta.setDisplayName(displayname);
        meta.setLocalizedName("arena.play-again");
        is.setItemMeta(meta);

        player.getInventory().setItem(slot,is);
    }

    public void setLeaveItem(Player player){
        Material material = Main.getPlugin().getMaterial(Main.getPlugin().getConfig().getString("items.leave-match-item.material"));
        String displayname = Main.getPlugin().getConfig().getString("items.leave-match-item.displayname").replaceAll("&","§");
        int slot = Main.getPlugin().getConfig().getInt("items.leave-match-item.slot");
        List<String> lore = Main.getPlugin().getConfig().getStringList("items.play-again-item.lore");
        List<String> modifiedLore = new ArrayList<>();
        for (String line : lore) {
            String modifiedLine = line.replace("&", "§");
            modifiedLore.add(modifiedLine);
        }

        ItemStack is = new ItemStack(material);
        ItemMeta meta = is.getItemMeta();
        assert meta != null;
        meta.setLore(modifiedLore);
        meta.setDisplayName(displayname);
        meta.setLocalizedName("arena.leave-match");
        is.setItemMeta(meta);

        player.getInventory().setItem(slot,is);
    }

}
