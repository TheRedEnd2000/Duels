package de.theredend2000.duels.inventorys.queueMenus;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import de.theredend2000.duels.game.GameState;
import de.theredend2000.duels.inventorys.PlayerMenuUtility;
import de.theredend2000.duels.kits.Kit;
import de.theredend2000.duels.queue.Queue;
import de.theredend2000.duels.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.Collections;

public class QueueListMenu extends QueuePaginatedMenu {

    public QueueListMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Select Match";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        ArrayList<Arena> keys = new ArrayList<>(Main.getPlugin().getArenaManagerHashMap().values());

        for (Arena arena : Main.getPlugin().getArenaManagerHashMap().values()) {
            if (e.getCurrentItem().getItemMeta().getLocalizedName().equals(arena.getName())) {
                if (Main.getPlugin().getQueueManager().isInThatQueue(p, arena)) {
                    Main.getPlugin().getQueueManager().removeFromQueue(p, arena);
                    p.sendMessage(Main.PREFIX + "§7You §cleft §7the queue for the arena §e" + arena.getName());
                } else {
                    if (!Main.getPlugin().getQueueManager().isInQueue(p)) {
                        Main.getPlugin().getQueueManager().addToQueue(p, arena);
                        p.sendMessage(Main.PREFIX + "§7You §ajoined §7the queue for the arena §e" + arena.getName());
                    } else {
                        p.sendMessage(Main.PREFIX + "§cYou are already in a Queue.");
                    }
                }
            }
        }
        for(Player player : Bukkit.getOnlinePlayers()){
            if(player.getOpenInventory().getTitle().equalsIgnoreCase("Select Match")){
                super.open();
            }
        }
        if(e.getCurrentItem().getType().equals(Material.PLAYER_HEAD)){
            if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Left")){
                if (page == 0){
                    p.sendMessage(Main.PREFIX+"§7You are already on the first page.");
                }else{
                    page = page - 1;
                    super.open();
                }
            }else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Right")){
                if (!((index + 1) >= keys.size())){
                    page = page + 1;
                    super.open();
                }else{
                    p.sendMessage(Main.PREFIX+"§7You are already on the last page.");
                }
            }
        }else if (e.getCurrentItem().getType().equals(Material.BARRIER)){
            if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Close")){
                p.closeInventory();
            }
        }
    }

    @Override
    public void setMenuItems() {
        addMenuBorder();
        ArrayList<String> keys = new ArrayList<>(Main.getPlugin().getArenaManagerHashMap().keySet());

        if(keys != null && !keys.isEmpty()) {
            Collections.sort(keys);
            for(int i = 0; i < getMaxItemsPerPage(); i++) {
                index = getMaxItemsPerPage() * page + i;
                if(index >= keys.size()) break;
                if (keys.get(index) != null){
                    Arena arena = Main.getPlugin().getArenaManagerHashMap().get(keys.get(index));
                    Material icon = Main.getPlugin().getArenaManager().getArenaMaterial(arena);
                    int playerSize = Main.getPlugin().getQueueManager().getPlayerSize(arena);
                    if(arena.isEnabled() && arena.getGameState().equals(GameState.WAITING))
                        inventory.addItem(new ItemBuilder(icon).setDisplayname("§6§l"+arena.getName()).setLore("","§7Players: §6"+playerSize+"§b/§62","§7Arena: §2"+arena.getName(),"§7Kit: §9Random","","§eClick to enter.").setLocalizedName(keys.get(index)).addItemFlags(ItemFlag.HIDE_ATTRIBUTES).addItemFlags(ItemFlag.HIDE_ENCHANTS).addItemFlags(ItemFlag.HIDE_POTION_EFFECTS).build());
                }
            }
        }
    }
}

