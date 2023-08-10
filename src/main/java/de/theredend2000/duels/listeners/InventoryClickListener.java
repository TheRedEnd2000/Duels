package de.theredend2000.duels.listeners;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import de.theredend2000.duels.commands.DuelCommand;
import de.theredend2000.duels.game.GameState;
import de.theredend2000.duels.inventorys.arenaMenus.ArenaListMenu;
import de.theredend2000.duels.inventorys.arenaMenus.ArenaMenu;
import de.theredend2000.duels.inventorys.kitMenus.KitListMenu;
import de.theredend2000.duels.inventorys.kitMenus.KitMenu;
import de.theredend2000.duels.inventorys.queueMenus.QueueMenu;
import de.theredend2000.duels.kits.Kit;
import de.theredend2000.duels.util.MessageKey;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class InventoryClickListener implements Listener {

    public InventoryClickListener(){
        Bukkit.getPluginManager().registerEvents(this, Main.getPlugin());
    }

    @EventHandler
    public void onClickInventory(InventoryClickEvent event){
        if(event.getWhoClicked() instanceof Player){
            Player player = (Player) event.getWhoClicked();
            if(event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null){
                InventoryHolder holder = event.getInventory().getHolder();
                if (holder instanceof KitMenu) {
                    event.setCancelled(true);
                    if (event.getCurrentItem() == null) {
                        return;
                    }
                    KitMenu menu = (KitMenu) holder;
                    menu.handleMenu(event);
                }
                if (holder instanceof QueueMenu) {
                    event.setCancelled(true);
                    if (event.getCurrentItem() == null) {
                        return;
                    }
                    QueueMenu menu = (QueueMenu) holder;
                    menu.handleMenu(event);
                }
                if (holder instanceof ArenaMenu) {
                    event.setCancelled(true);
                    if (event.getCurrentItem() == null) {
                        return;
                    }
                    ArenaMenu menu = (ArenaMenu) holder;
                    menu.handleMenu(event);
                }
                if(event.getView().getTitle().equals("Arena Editor")){
                    event.setCancelled(true);
                    Arena arena = Main.getPlugin().getArenaManagerHashMap().get(event.getInventory().getItem(4).getItemMeta().getLocalizedName());
                    if(!arena.getGameState().equals(GameState.WAITING)){
                        player.sendMessage(Main.PREFIX+"§cYou can only edit the arena while in waiting state.");
                        return;
                    }
                    if(event.getCurrentItem().getItemMeta().hasLocalizedName()){
                        switch (event.getCurrentItem().getItemMeta().getLocalizedName()){
                            case "arena.edit.close":
                                player.closeInventory();
                                break;
                            case "arena.edit.spawn1":
                                arena.setSpawn1(player.getLocation());
                                player.sendMessage(Main.getPlugin().getMessageManager().getMessage(MessageKey.ARENA_EDIT_ACTION).replaceAll("%action%","Spawn 1").replaceAll("%arena%",arena.getName()));
                                Main.getPlugin().getArenaManager().updateArenas();
                                player.closeInventory();
                                break;
                            case "arena.edit.spawn2":
                                arena.setSpawn2(player.getLocation());
                                player.sendMessage(Main.PREFIX+"§7You have §asuccessfully §7set the §2Spawn 2 §7for the arena §e"+arena.getName()+"§7.");
                                Main.getPlugin().getArenaManager().updateArenas();
                                player.closeInventory();
                                break;
                            case "arena.edit.pos1":
                                arena.setPos1(player.getLocation());
                                player.sendMessage(Main.PREFIX+"§7You have §asuccessfully §7set the §2Position 1 §7for the arena §e"+arena.getName()+"§7.");
                                Main.getPlugin().getArenaManager().updateArenas();
                                player.closeInventory();
                                break;
                            case "arena.edit.pos2":
                                arena.setPos2(player.getLocation());
                                player.sendMessage(Main.PREFIX+"§7You have §asuccessfully §7set the §2Positon 2 §7for the arena §e"+arena.getName()+"§7.");
                                Main.getPlugin().getArenaManager().updateArenas();
                                player.closeInventory();
                                break;
                            case "arena.edit.lobby":
                                arena.setLobbySpawn(player.getLocation());
                                player.sendMessage(Main.PREFIX+"§7You have §asuccessfully §7set the §6lobby§7 for the arena §e"+arena.getName()+"§7.");
                                Main.getPlugin().getArenaManager().updateArenas();
                                player.closeInventory();
                                break;
                            case "arena.edit.end":
                                arena.setEndSpawn(player.getLocation());
                                player.sendMessage(Main.PREFIX+"§7You have §asuccessfully §7set the §6ending§7 for the arena §e"+arena.getName()+"§7.");
                                Main.getPlugin().getArenaManager().updateArenas();
                                player.closeInventory();
                                break;
                        }
                    }
                    Main.getPlugin().getArenaManager().updateArenas();
                }else if(event.getView().getTitle().equals("Duel Player")){
                    event.setCancelled(true);
                    Arena arena = Main.getPlugin().getArenaManagerHashMap().get(event.getInventory().getItem(3).getItemMeta().getLocalizedName());
                    Kit kit = Main.getPlugin().getKitManagerHashMap().get(event.getInventory().getItem(14).getItemMeta().getLocalizedName());
                    Player opponent = Bukkit.getPlayer(event.getInventory().getItem(13).getItemMeta().getLocalizedName());
                    if(opponent == null){
                        player.sendMessage(Main.PREFIX+"§cThe player "+event.getInventory().getItem(13).getItemMeta().getLocalizedName()+" was not found.");
                        player.closeInventory();
                        return;
                    }
                    if(event.getCurrentItem().getItemMeta().hasLocalizedName()){
                        switch (event.getCurrentItem().getItemMeta().getLocalizedName()){
                            case "duel.main.cancel":
                                player.closeInventory();
                                break;
                            case "duel.main.confirm":
                                new DuelCommand().sendRequest(player,opponent,arena,kit);
                                player.closeInventory();
                                break;
                            case "duel.main.arena":
                                new ArenaListMenu(Main.getPlayerMenuUtility(player)).open(opponent.getDisplayName(), arena.getName(),kit.getName(),null);
                                break;
                            case "duel.main.kit":
                                new KitListMenu(Main.getPlayerMenuUtility(player)).open(opponent.getDisplayName(), arena.getName(),kit.getName(),null);
                                break;
                        }
                    }
                }
                if(Main.getPlugin().getArenaManager().playerIsAlreadyInArena(player)){
                    Arena arena = Main.getPlugin().getArenaManager().getPlayerCurrentArena(player);
                    if(arena.getGameState().equals(GameState.GAME_END)){
                        event.setCancelled(true);
                    }
                }
            }
        }
    }


}
