package de.theredend2000.duels.inventorys.arenaMenus;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import de.theredend2000.duels.game.GameState;
import de.theredend2000.duels.inventorys.PlayerMenuUtility;
import de.theredend2000.duels.kits.Kit;
import de.theredend2000.duels.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.Collections;

public class ArenaListMenu extends ArenaPaginatedMenu {

    public ArenaListMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Select Arena";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        String kitName = e.getInventory().getItem(23).getItemMeta().getLocalizedName();
        String opponent = e.getInventory().getItem(22).getItemMeta().getLocalizedName();
        String arena = e.getInventory().getItem(19).getItemMeta().getLocalizedName();
        Player dueler = Bukkit.getPlayer(opponent);
        if(dueler == null){
            p.sendMessage(Main.PREFIX+"§cThe player "+opponent+" is not longer available.");
            p.closeInventory();
            return;
        }

        ArrayList<Arena> keys = new ArrayList<>(Main.getPlugin().getArenaManagerHashMap().values());
        for(Arena kits : Main.getPlugin().getArenaManagerHashMap().values()) {
            if (e.getCurrentItem().getItemMeta().getLocalizedName().equals(kits.getName())) {
                Arena playingArena = Main.getPlugin().getArenaManagerHashMap().get(e.getCurrentItem().getItemMeta().getLocalizedName());
                Kit kit = Main.getPlugin().getKitManagerHashMap().get(kitName);
                Main.getPlugin().getInventoryManager().getPlayerDuelMenuMain(p, dueler,playingArena,kit);
            }
        }

        if(e.getCurrentItem().getType().equals(Material.PLAYER_HEAD)){
            if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Left")){
                if (page == 0){
                    p.sendMessage(Main.PREFIX+"§7You are already on the first page.");
                }else{
                    page = page - 1;
                    super.open(opponent,arena,kitName);
                }
            }else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Right")){
                if (!((index + 1) >= keys.size())){
                    page = page + 1;
                    super.open(opponent,arena,kitName);
                }else{
                    p.sendMessage(Main.PREFIX+"§7You are already on the last page.");
                }
            }else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Back")){
                Main.getPlugin().getInventoryManager().getPlayerDuelMenuMain(p,dueler,Main.getPlugin().getArenaManagerHashMap().get(arena), Main.getPlugin().getKitManagerHashMap().get(kitName));
            }
        }
    }

    @Override
    public void setMenuItems(String opponent,String arenaName, String kitName) {
        addMenuBorder(opponent, arenaName,kitName);
        ArrayList<String> keys = new ArrayList<>(Main.getPlugin().getArenaManagerHashMap().keySet());

        if(keys != null && !keys.isEmpty()) {
            Collections.sort(keys);
            for(int i = 0; i < getMaxItemsPerPage(); i++) {
                index = getMaxItemsPerPage() * page + i;
                if(index >= keys.size()) break;
                if (keys.get(index) != null){
                    Arena arena = Main.getPlugin().getArenaManagerHashMap().get(keys.get(index));
                    Material icon = Main.getPlugin().getArenaManager().getArenaMaterial(arena);
                    if(arena.isEnabled() && arena.getGameState().equals(GameState.WAITING))
                        inventory.addItem(new ItemBuilder(icon).setDisplayname("§6§l"+arena.getName()).setLore("","§eClick to select").setLocalizedName(keys.get(index)).addItemFlags(ItemFlag.HIDE_ATTRIBUTES).addItemFlags(ItemFlag.HIDE_ENCHANTS).addItemFlags(ItemFlag.HIDE_POTION_EFFECTS).build());
                }
            }
        }
    }
}

