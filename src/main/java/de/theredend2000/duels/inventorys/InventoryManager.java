package de.theredend2000.duels.inventorys;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import de.theredend2000.duels.kits.Kit;
import de.theredend2000.duels.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;

import java.util.Collections;
import java.util.Objects;

public class InventoryManager {

    public void getArenaEditMenu(Player player, Arena arena){
        Inventory inventory = Bukkit.createInventory(player,27,"Arena Editor");
        int[] glass = new int[]{0,1,2,3,4,5,6,7,8,9,17,18,19,20,21,22,23,24,25,26};
        for (int i = 0; i<glass.length;i++){inventory.setItem(glass[i], new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        inventory.setItem(4, new ItemBuilder(Material.IRON_SWORD).setDisplayname("§7Arena: §6§l"+arena.getName()).setLore("","§9Information:"
                ,"§7Ending: §3"+(arena.getEndSpawn() == null ? "§cNot set yet!" : arena.getEndSpawn().getBlockX()+", "+arena.getEndSpawn().getBlockY()+", "+arena.getEndSpawn().getBlockZ())
                ,"§7Lobby: §3"+(arena.getLobbySpawn() == null ? "§cNot set yet!" : arena.getLobbySpawn().getBlockX()+", "+arena.getLobbySpawn().getBlockY()+", "+arena.getLobbySpawn().getBlockZ())
                ,"§7Spawn 1: §3"+(arena.getSpawn1() == null ? "§cNot set yet!" : arena.getSpawn1().getBlockX()+", "+arena.getSpawn1().getBlockY()+", "+arena.getSpawn1().getBlockZ())
                ,"§7Spawn 2: §3"+(arena.getSpawn2() == null ? "§cNot set yet!" : arena.getSpawn2().getBlockX()+", "+arena.getSpawn2().getBlockY()+", "+arena.getSpawn2().getBlockZ())
                ,"§7Enabled: "+(arena.isEnabled() ? "§atrue" : "§cfalse"),"§7Game State: §4"+arena.getGameState(),"","§eClick here to update.").addItemFlags(ItemFlag.HIDE_ATTRIBUTES).setLocalizedName(arena.getName()).build());
        inventory.setItem(22, new ItemBuilder(Material.BARRIER).setDisplayname("§cClose").setLocalizedName("arena.edit.close").build());
        inventory.setItem(9,new ItemBuilder(Material.WOODEN_AXE).setDisplayname("§cPos 1").setLore("§7The location of position 1 of the arena.","","§7Set: "+(arena.getPos1() != null ? "§atrue" : "§cfalse"),"§7Location: §6"+(arena.getPos1() == null ? "§cNot set yet!" : arena.getPos1().getBlockX()+", "+arena.getPos1().getBlockY()+", "+arena.getPos1().getBlockZ()),"","§eClick to change.").setLocalizedName("arena.edit.pos1").build());
        inventory.setItem(10, new ItemBuilder(Material.WOODEN_AXE).setDisplayname("§cPos 2").setLore("§7The location of position 2 of the arena.","","§7Set: "+(arena.getPos2() != null ? "§atrue" : "§cfalse"),"§7Location: §6"+(arena.getPos2() == null ? "§cNot set yet!" : arena.getPos2().getBlockX()+", "+arena.getPos2().getBlockY()+", "+arena.getPos2().getBlockZ()),"","§eClick to change.").setLocalizedName("arena.edit.pos2").build());
        inventory.setItem(12, new ItemBuilder(Material.BOW).setDisplayname("§6Ending").setLore("§7The location is for all players in the battle.","§7They will be teleported if one player dies.","","§7Set: "+(arena.getEndSpawn() != null ? "§atrue" : "§cfalse"),"§7Location: §6"+(arena.getEndSpawn() == null ? "§cNot set yet!" : arena.getEndSpawn().getBlockX()+", "+arena.getEndSpawn().getBlockY()+", "+arena.getEndSpawn().getBlockZ()),"","§eClick to change.").setLocalizedName("arena.edit.end").build());
        inventory.setItem(14, new ItemBuilder(Material.BEACON).setDisplayname("§6Lobby").setLore("§7The location is for all players in the battle.","§7They will be teleported after the ending countdown.","","§7Set: "+(arena.getLobbySpawn() != null ? "§atrue" : "§cfalse"),"§7Location: §6"+(arena.getLobbySpawn() == null ? "§cNot set yet!" : arena.getLobbySpawn().getBlockX()+", "+arena.getLobbySpawn().getBlockY()+", "+arena.getLobbySpawn().getBlockZ()),"","§eClick to change.").setLocalizedName("arena.edit.lobby").build());
        inventory.setItem(16,new ItemBuilder(Material.GRASS_BLOCK).setDisplayname("§2Spawn 1").setLore("§7The location is for a random player dueling.","","§7Set: "+(arena.getSpawn1() != null ? "§atrue" : "§cfalse"),"§7Location: §6"+(arena.getSpawn1() == null ? "§cNot set yet!" : arena.getSpawn1().getBlockX()+", "+arena.getSpawn1().getBlockY()+", "+arena.getSpawn1().getBlockZ()),"","§eClick to change.").setLocalizedName("arena.edit.spawn1").build());
        inventory.setItem(17, new ItemBuilder(Material.GRASS_BLOCK).setDisplayname("§2Spawn 2").setLore("§7The location is for a random player dueling.","","§7Set: "+(arena.getSpawn2() != null ? "§atrue" : "§cfalse"),"§7Location: §6"+(arena.getSpawn2() == null ? "§cNot set yet!" : arena.getSpawn2().getBlockX()+", "+arena.getSpawn2().getBlockY()+", "+arena.getSpawn2().getBlockZ()),"","§eClick to change.").setLocalizedName("arena.edit.spawn2").build());
        player.openInventory(inventory);
    }

    public void getPlayerDuelMenuMain(Player player, Player dueler, Arena arena, Kit kit){
        Inventory inventory = Bukkit.createInventory(player,27,"Duel Player");
        boolean showIcons = Main.getPlugin().getConfig().getBoolean("layout.use-icon-items");
        int[] glass1 = new int[]{3,5,12};
        for (int i = 0; i<glass1.length;i++){inventory.setItem(glass1[i], new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayname("§c").setLocalizedName(arena.getName()).build());}
        int[] glass2 = new int[]{14,21,23};
        for (int i = 0; i<glass2.length;i++){inventory.setItem(glass2[i], new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayname("§c").setLocalizedName(kit.getName()).build());}
        int[] limeGlass = new int[]{0,1,2,9,10,11,18,19,20};
        for (int i = 0; i<limeGlass.length;i++){inventory.setItem(limeGlass[i], new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDisplayname("§2§lConfirm").setLocalizedName("duel.main.confirm").build());}
        int[] redGlass = new int[]{6,7,8,15,16,17,24,25,26};
        for (int i = 0; i<redGlass.length;i++){inventory.setItem(redGlass[i], new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§4§lCancel").setLocalizedName("duel.main.cancel").build());}
        inventory.setItem(13,new ItemBuilder(Material.PLAYER_HEAD).setDisplayname("§6§lDuel "+dueler.getDisplayName()).setOwner(dueler.getDisplayName()).setStringListLore(Main.getPlugin().getSpecialsManager().shouldShow(player) ? Main.getPlugin().getSpecialsManager().getStatsOfPlayerAsLore(dueler) : Collections.singletonList("§cStats hidden")).setLocalizedName(dueler.getDisplayName()).build());
        inventory.setItem(4,new ItemBuilder(showIcons ? Main.getPlugin().getArenaManager().getArenaMaterial(arena) : Material.getMaterial(Objects.requireNonNull(Main.getPlugin().getConfig().getString("items.default-arena-item")))).setDisplayname("§2§lArena").setLore("","§7Currently: §d§l"+arena.getName(),"","§eClick to change.").setLocalizedName("duel.main.arena").build());
        inventory.setItem(22,new ItemBuilder(showIcons ? Main.getPlugin().getKitManager().getKitMaterial(kit.getName()) : Material.getMaterial(Objects.requireNonNull(Main.getPlugin().getConfig().getString("items.default-kit-item")))).setDisplayname("§9§lKit").setLore("","§7Currently: §d§l"+kit.getName(),"","§eClick to change.").setLocalizedName("duel.main.kit").build());
        player.openInventory(inventory);
    }

}
