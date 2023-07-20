package de.theredend2000.duels.inventorys.kitMenus;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import de.theredend2000.duels.inventorys.PlayerMenuUtility;
import de.theredend2000.duels.kits.Kit;
import de.theredend2000.duels.util.ItemBuilder;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class KitListMenu extends KitPaginatedMenu {

    public KitListMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }

    @Override
    public String getMenuName() {
        return "Select Kit";
    }

    @Override
    public int getSlots() {
        return 27;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        String kitName = e.getInventory().getItem(19).getItemMeta().getLocalizedName();
        String opponent = e.getInventory().getItem(22).getItemMeta().getLocalizedName();
        String arena = e.getInventory().getItem(23).getItemMeta().getLocalizedName();
        Player dueler = Bukkit.getPlayer(opponent);
        if(dueler == null){
            p.sendMessage(Main.PREFIX+"§cThe player "+opponent+" is not longer available.");
            p.closeInventory();
            return;
        }

        for(Kit kits : Main.getPlugin().getKitManagerHashMap().values()) {
            if (e.getCurrentItem().getItemMeta().getLocalizedName().equals(kits.getName())) {
                Arena playingArena = Main.getPlugin().getArenaManagerHashMap().get(arena);
                Kit kit = Main.getPlugin().getKitManagerHashMap().get(e.getCurrentItem().getItemMeta().getLocalizedName());
                Main.getPlugin().getInventoryManager().getPlayerDuelMenuMain(p, dueler,playingArena,kit);
            }
        }

        if(e.getCurrentItem().getType().equals(Material.PLAYER_HEAD)){
            if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Left")){
                if (page == 0){
                    p.sendMessage(Main.PREFIX+"§7You are already on the first page.");
                }else{
                    page = page - 1;
                    super.open(opponent,arena,kitName, search);
                }
            }else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Right")){
                if (!((index + 1) >= getKeysArray().size())){
                    page = page + 1;
                    super.open(opponent,arena,kitName, search);
                }else{
                    p.sendMessage(Main.PREFIX+"§7You are already on the last page.");
                }
            }else if (ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Back")){
                Main.getPlugin().getInventoryManager().getPlayerDuelMenuMain(p,dueler,Main.getPlugin().getArenaManagerHashMap().get(arena), Main.getPlugin().getKitManagerHashMap().get(kitName));
            }
        }else if(e.getCurrentItem().getType().equals(Material.OAK_SIGN) && e.getSlot() == 24){
            if(e.getAction().equals(InventoryAction.PICKUP_ALL)){
                new AnvilGUI.Builder()
                        .onClose(stateSnapshot -> {
                            Player player = stateSnapshot.getPlayer();
                            if (!stateSnapshot.getText().isEmpty()) {
                                page = 0;
                                super.open(opponent, arena, kitName, stateSnapshot.getText());
                            } else {
                                player.sendMessage(Main.PREFIX + "§cFailed to read text.");
                            }
                        })
                        .onClick((slot, stateSnapshot) -> {
                            if (slot != AnvilGUI.Slot.OUTPUT) {
                                super.open(opponent, arena, kitName, null);
                            }
                            return Arrays.asList(AnvilGUI.ResponseAction.close());
                        })
                        .text(search != null ? search : "Search")
                        .title("Search for a kit")
                        .plugin(Main.getPlugin())
                        .open(p);
            }else if(e.getAction().equals(InventoryAction.PICKUP_HALF)){
                search = null;
                page = 0;
                super.open(opponent, arena, kitName, null);
            }
        }
    }

    @Override
    public void setMenuItems(String opponent,String arena, String kitName) {
        addMenuBorder(opponent, arena,kitName);
        ArrayList<String> keys = getKeysArray();

        if (keys != null && !keys.isEmpty()) {
            Collections.sort(keys);
            for (int i = 0; i < getMaxItemsPerPage(); i++) {
                index = getMaxItemsPerPage() * page + i;
                if (index >= keys.size()) break;
                if (keys.get(index) != null) {
                    Kit kit = Main.getPlugin().getKitManagerHashMap().get(keys.get(index));
                    Material icon = Main.getPlugin().getKitManager().getKitMaterial(kit.getName());
                    List<String> lore = Main.getPlugin().getKitManager().getLoreFromKit(kit.getName());
                    inventory.addItem(new ItemBuilder(icon).setDisplayname("§6§l" + kit.getName()).setStringListLore(lore).setLocalizedName(keys.get(index)).addItemFlags(ItemFlag.HIDE_ATTRIBUTES).addItemFlags(ItemFlag.HIDE_ENCHANTS).addItemFlags(ItemFlag.HIDE_POTION_EFFECTS).build());
                }
            }
        }else
            inventory.addItem(new ItemBuilder(Material.BARRIER).setDisplayname("§4§lERROR").setLore("§cNo Kits found!","§cPlease check your search,","§cor reset your current search.").build());
    }

    @Override
    public ArrayList<String> getKeysArray() {
        ArrayList<String> keys;
        if (search == null)
            keys = new ArrayList<>(Main.getPlugin().getKitManagerHashMap().keySet());
        else {
            keys = new ArrayList<>();
            for (String name : Main.getPlugin().getKitManagerHashMap().keySet()) {
                if (name.toLowerCase().contains(search.toLowerCase())) {
                    keys.add(name);
                }
            }
        }
        return keys;
    }
}

