package de.theredend2000.duels.commands;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import de.theredend2000.duels.game.GameState;
import de.theredend2000.duels.kits.Kit;
import de.theredend2000.duels.util.BlockUtils;
import de.theredend2000.duels.util.HelpManager;
import de.theredend2000.duels.util.MessageKey;
import de.theredend2000.duels.util.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class DuelsCommand implements CommandExecutor, TabCompleter {

    private MessageManager messageManager;

    public DuelsCommand(){
        messageManager = Main.getPlugin().getMessageManager();
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if (Main.getPlugin().getConfig().getBoolean("permissions.need-duels-command-permission")) {
                if (!player.hasPermission((Objects.requireNonNull(Main.getPlugin().getConfig().getString("permissions.duels-command-permission"))))) {
                    player.sendMessage(messageManager.getMessage(MessageKey.PERMISSION_ERROR));
                    return false;
                }
            }
            if(args.length == 0){
                HelpManager.sendPlayerDefaultHelp(player);
                return false;
            }
            if(args[0].equalsIgnoreCase("arena")){
                if(args[1].equalsIgnoreCase("create") && args.length == 3){
                    try {
                        String name = args[2];
                        if(isValidInput(name)) {
                            if (!Main.getPlugin().getArenaManagerHashMap().containsKey(name)) {
                                Main.getPlugin().getArenaManagerHashMap().put(name, new Arena(name, false, new ArrayList<UUID>(), null, null, null, null, null, null, GameState.DISABLED));
                                Main.getPlugin().getArenaManager().saveNewArena(name);
                                player.sendMessage(messageManager.getMessage(MessageKey.ARENA_CREATED).replaceAll("%arena_name%",name));
                            } else
                                player.sendMessage(messageManager.getMessage(MessageKey.ARENA_ALREADY_EXIST).replaceAll("%arena_name%",name));
                        } else
                            player.sendMessage(messageManager.getMessage(MessageKey.TYPE_NAME_CONTENT).replaceAll("%type%","Arena"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else if(args[1].equalsIgnoreCase("delete") && args.length == 3){
                    try {
                        String name = args[2];
                        if(Main.getPlugin().getArenaManagerHashMap().containsKey(name)) {
                            Main.getPlugin().getArenaManagerHashMap().remove(name);
                            Main.getPlugin().getArenaManager().removeArena(name);
                            player.sendMessage(messageManager.getMessage(MessageKey.ARENA_DELETED).replaceAll("%arena_name%",name));
                        }else
                            player.sendMessage(messageManager.getMessage(MessageKey.ARENA_NOT_FOUND));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else if(args[1].equalsIgnoreCase("edit") && args.length == 3){
                    try {
                        String name = args[2];
                        if(Main.getPlugin().getArenaManagerHashMap().containsKey(name)) {
                            Arena arena = Main.getPlugin().getArenaManagerHashMap().get(name);
                            Main.getPlugin().getInventoryManager().getArenaEditMenu(player,arena);
                        }else
                            player.sendMessage(messageManager.getMessage(MessageKey.ARENA_NOT_FOUND));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else if(args[1].equalsIgnoreCase("list") && args.length == 2){
                    player.sendMessage("§d=============§6§lArenas§d=============");
                    for(Arena arena : Main.getPlugin().getArenaManagerHashMap().values()){
                        player.sendMessage("§b- §7Arena: §6§l"+arena.getName());
                    }
                    player.sendMessage("§d=============§6§lArenas§d=============");
                }else if(args[1].equalsIgnoreCase("help") && args.length == 2){
                    //HelpManager.sendPlayerArenaHelp(player);
                    HelpManager.openHelpBookPlayer(player);
                }else if(args[1].equalsIgnoreCase("setIcon") && args.length == 3){
                    try {
                        String name = args[2];
                        if(Main.getPlugin().getArenaManagerHashMap().containsKey(name)) {
                            Arena arena = Main.getPlugin().getArenaManagerHashMap().get(name);
                            if(player.getItemInHand().getItemMeta() != null){
                                Material icon = player.getInventory().getItemInMainHand().getType();
                                Main.getPlugin().getArenaManager().setArenaMaterial(arena,icon);
                                player.sendMessage(messageManager.getMessage(MessageKey.ARENA_ICON_SET).replaceAll("%arena_name%",name).replaceAll("%icon%",icon.name()));
                            }else
                                player.sendMessage(messageManager.getMessage(MessageKey.ARENA_ICON_FAIL));
                        }else
                            player.sendMessage(messageManager.getMessage(MessageKey.ARENA_NOT_FOUND));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else
                    HelpManager.sendPlayerArenaHelp(player);
            }else if(args[0].equalsIgnoreCase("kit")){
                if(args[1].equalsIgnoreCase("save") && args.length == 3){
                    try {
                        String name = args[2];
                        if(isValidInput(name)) {
                            if (!Main.getPlugin().getKitManagerHashMap().containsKey(name)) {
                                Main.getPlugin().getKitManager().saveKit(player, name);
                                player.sendMessage(messageManager.getMessage(MessageKey.KIT_SAVED).replaceAll("%kit_name%",name));
                            } else
                                player.sendMessage(messageManager.getMessage(MessageKey.KIT_ALREADY_EXIST));
                        }else
                            player.sendMessage(messageManager.getMessage(MessageKey.TYPE_NAME_CONTENT).replaceAll("%type%","Kit"));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else if(args[1].equalsIgnoreCase("delete") && args.length == 3){
                    try {
                        String name = args[2];
                        if(Main.getPlugin().getKitManagerHashMap().containsKey(name)) {
                            Main.getPlugin().getKitManager().removeKit(name);
                            player.sendMessage(messageManager.getMessage(MessageKey.KIT_DELETED).replaceAll("%kit_name%",name));
                        }else
                            player.sendMessage(messageManager.getMessage(MessageKey.KIT_NOT_FOUND));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else if(args[1].equalsIgnoreCase("list") && args.length == 2){
                    player.sendMessage("§d=============§6§lKits§d=============");
                    for(Kit kit : Main.getPlugin().getKitManagerHashMap().values()){
                        player.sendMessage("§b- §7Kit: §6§l"+kit.getName());
                    }
                    player.sendMessage("§d=============§6§lKits§d=============");
                }else if(args[1].equalsIgnoreCase("help") && args.length == 2){
                    HelpManager.sendPlayerKitHelp(player);
                }else if(args[1].equalsIgnoreCase("rename") && args.length == 4){
                    String oldName = args[2];
                    String newName = args[3];
                    if(isValidInput(newName)) {
                        if (Main.getPlugin().getKitManagerHashMap().containsKey(oldName)) {
                            if (!Main.getPlugin().getKitManagerHashMap().containsKey(newName)) {
                                Main.getPlugin().getKitManager().renameKit(oldName, newName);
                                player.sendMessage(messageManager.getMessage(MessageKey.KIT_RENAME).replaceAll("%kit_old%",oldName).replaceAll("%kit_new%",newName));
                            } else
                                player.sendMessage(messageManager.getMessage(MessageKey.KIT_RENAME_EXIST_NEW).replaceAll("%kit_new%",newName));
                        } else
                            player.sendMessage(messageManager.getMessage(MessageKey.KIT_ALREADY_EXIST));
                    }else
                        player.sendMessage(Main.PREFIX + "§cKit names can only contain §e'Letters (a) | Numbers (1) | Underscores (_)  | Hyphen (-)'§c.");
                }else if(args[1].equalsIgnoreCase("load") && args.length == 3){
                    String name = args[2];
                    if(Main.getPlugin().getKitManagerHashMap().containsKey(name)) {
                        Main.getPlugin().getKitManager().loadKit(player, name);
                        player.sendMessage(messageManager.getMessage(MessageKey.KIT_LOAD).replaceAll("%kit_name%",name));
                    }else
                        player.sendMessage(messageManager.getMessage(MessageKey.KIT_NOT_FOUND));
                }else if(args[1].equalsIgnoreCase("edit") && args.length == 3){
                    String name = args[2];
                    if(Main.getPlugin().getKitManagerHashMap().containsKey(name)) {
                        Main.getPlugin().getKitManagerHashMap().remove(name);
                        Main.getPlugin().getKitManager().saveKit(player,name);
                        player.sendMessage(messageManager.getMessage(MessageKey.KIT_EDIT).replaceAll("%kit_name%",name));
                    }else
                        player.sendMessage(messageManager.getMessage(MessageKey.KIT_NOT_FOUND));
                }else if(args[1].equalsIgnoreCase("setIcon") && args.length == 3){
                    try {
                        String name = args[2];
                        if(Main.getPlugin().getKitManagerHashMap().containsKey(name)) {
                            Kit kit = Main.getPlugin().getKitManagerHashMap().get(name);
                            if(player.getItemInHand().getItemMeta() != null){
                                Material icon = player.getInventory().getItemInMainHand().getType();
                                Main.getPlugin().getKitManager().setKitMaterial(kit.getName(),icon);
                                player.sendMessage(messageManager.getMessage(MessageKey.KIT_ICON_SET).replaceAll("%kit_name%",name).replaceAll("%icon%", icon.name()));
                            }else
                                player.sendMessage(messageManager.getMessage(MessageKey.KIT_ICON_FAIL));
                        }else
                            player.sendMessage(messageManager.getMessage(MessageKey.KIT_NOT_FOUND));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else
                    HelpManager.sendPlayerKitHelp(player);
            }else if(args[0].equalsIgnoreCase("reload") && args.length == 1){
                Main.getPlugin().reloadConfig();
                player.sendMessage(messageManager.getMessage(MessageKey.RELOAD_FILE_SUCCESS).replaceAll("%file%","config"));
                Main.getPlugin().getStatsManager().reloadStats();
                player.sendMessage(messageManager.getMessage(MessageKey.RELOAD_FILE_SUCCESS).replaceAll("%file%","stats"));
            }else if(args[0].equalsIgnoreCase("stats")){
                if(args.length == 5) {
                    if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("set")) {
                        String playerName = args[2];
                        if (!Main.getPlugin().getStatsManager().containsPlayer(playerName)) {
                            player.sendMessage(messageManager.getMessage(MessageKey.PLAYER_NOT_EXIST));
                            return false;
                        }
                        UUID playerUUID = Main.getPlugin().getStatsManager().getPlayerUUID(playerName);
                        String option = Main.getPlugin().getStatsManager().getOption(args[3]);
                        if (option == null) {
                            player.sendMessage(messageManager.getMessage(MessageKey.OPTION_INVADABLE));
                            return false;
                        }
                        if (option.equalsIgnoreCase("k/d")) {
                            player.sendMessage(messageManager.getMessage(MessageKey.CANT_USE_OPTION_HERE));
                            return false;
                        }
                        try {
                            int score = Integer.parseInt(args[4]);
                            Main.getPlugin().getStatsManager().finishChanges(args[1], playerUUID, option, player, score);
                        } catch (NumberFormatException e) {
                            player.sendMessage(messageManager.getMessage(MessageKey.USE_NUMBER_AS_SCORE));
                        }
                    } else
                        HelpManager.sendPlayerStatsHelp(player);
                }else if(args.length == 3){
                    if(args[1].equalsIgnoreCase("get")){
                        String playerName = args[2];
                        if(!Main.getPlugin().getStatsManager().containsPlayer(playerName)){
                            player.sendMessage(messageManager.getMessage(MessageKey.PLAYER_NOT_EXIST));
                            return false;
                        }
                        UUID playerUUID = Main.getPlugin().getStatsManager().getPlayerUUID(playerName);
                        player.sendMessage(messageManager.getMessage(MessageKey.GET_STATS_FROM_PLAYER).replaceAll("%player%",playerName));
                        player.sendMessage("§6>> §7Rating: §d"+Main.getPlugin().getStatsManager().getRating(playerUUID));
                        player.sendMessage("§6>> §7Wins: §d"+Main.getPlugin().getStatsManager().getWins(playerUUID));
                        player.sendMessage("§6>> §7Loses: §d"+Main.getPlugin().getStatsManager().getLoses(playerUUID));
                        player.sendMessage("§6>> §7Kills: §d"+Main.getPlugin().getStatsManager().getKills(playerUUID));
                        player.sendMessage("§6>> §7Deaths: §d"+Main.getPlugin().getStatsManager().getDeaths(playerUUID));
                        player.sendMessage("§6>> §7K/D: §d"+Main.getPlugin().getStatsManager().getKD(playerUUID));
                    }else
                        HelpManager.sendPlayerStatsHelp(player);
                }else if(args.length == 2){
                    if(args[1].equalsIgnoreCase("help")){
                        HelpManager.sendPlayerStatsHelp(player);
                    }else
                        HelpManager.sendPlayerStatsHelp(player);
                }else if(args.length == 4){
                    if(args[1].equalsIgnoreCase("reset")){
                        String playerName = args[2];
                        if(!Main.getPlugin().getStatsManager().containsPlayer(playerName)){
                            player.sendMessage(messageManager.getMessage(MessageKey.PLAYER_NOT_EXIST));
                            return false;
                        }
                        UUID playerUUID = Main.getPlugin().getStatsManager().getPlayerUUID(playerName);
                        String option = Main.getPlugin().getStatsManager().getOption(args[3]);
                        if(option == null){
                            player.sendMessage(messageManager.getMessage(MessageKey.OPTION_INVADABLE));
                            return false;
                        }
                        if(option.equals("rating")) {
                            int rating = Main.getPlugin().getConfig().getInt("stats.rating-default");
                            Main.getPlugin().getStatsManager().finishChanges(args[1],playerUUID,option,player,rating);
                        } else
                            Main.getPlugin().getStatsManager().finishChanges(args[1],playerUUID,option,player,0);
                    }else
                        HelpManager.sendPlayerStatsHelp(player);
                } else
                    HelpManager.sendPlayerStatsHelp(player);
            }else
                HelpManager.sendPlayerDefaultHelp(player);
        }else
            sender.sendMessage(messageManager.getMessage(MessageKey.ONLY_PLAYERS_ERROR));
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (Main.getPlugin().getConfig().getBoolean("permissions.need-duels-command-permission")) {
            if (!sender.hasPermission((Objects.requireNonNull(Main.getPlugin().getConfig().getString("permissions.duels-command-permission"))))) {
                return null;
            }
        }
        if(args.length == 1){
            String[] text = {"arena","kit","reload","stats"};
            ArrayList<String> tabs = new ArrayList<>();
            Collections.addAll(tabs,text);
            return tabs;
        }else if(args.length == 2){
            if(args[0].equalsIgnoreCase("arena")) {
                String[] text = {"create", "delete","list","edit","setIcon","help"};
                ArrayList<String> tabs = new ArrayList<>();
                Collections.addAll(tabs, text);
                return tabs;
            }else if(args[0].equalsIgnoreCase("kit")){
                String[] text = {"save", "delete","list","rename","load","edit","setIcon","help"};
                ArrayList<String> tabs = new ArrayList<>();
                Collections.addAll(tabs, text);
                return tabs;
            }else if(args[0].equalsIgnoreCase("stats")){
                String[] text = {"set", "add","remove","reset","get","help"};
                ArrayList<String> tabs = new ArrayList<>();
                Collections.addAll(tabs, text);
                return tabs;
            }
        }else if(args.length == 3){
            if(args[0].equalsIgnoreCase("arena")) {
                if (args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("edit") || args[1].equalsIgnoreCase("setIcon")) {
                    ArrayList<String> tabs = new ArrayList<>();
                    for (Arena arena : Main.getPlugin().getArenaManagerHashMap().values()) {
                        Collections.addAll(tabs, arena.getName());
                    }
                    return tabs.isEmpty() ? Collections.singletonList("-") : tabs;
                }else if(args[1].equalsIgnoreCase("create")){
                    return Collections.singletonList("<name>");
                }
            }else if(args[0].equalsIgnoreCase("kit")) {
                if (args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("load") || args[1].equalsIgnoreCase("edit") || args[1].equalsIgnoreCase("rename") || args[1].equalsIgnoreCase("setIcon")) {
                    ArrayList<String> tabs = new ArrayList<>();
                    for (Kit kit : Main.getPlugin().getKitManagerHashMap().values()) {
                        Collections.addAll(tabs, kit.getName());
                    }
                    return tabs.isEmpty() ? Collections.singletonList("-") : tabs;
                }else if(args[1].equalsIgnoreCase("save")){
                    return Collections.singletonList("<name>");
                }
            }else if(args[0].equalsIgnoreCase("stats")) {
                if (args[1].equalsIgnoreCase("set") || args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("reset") || args[1].equalsIgnoreCase("get")) {
                    ArrayList<String> tabs = new ArrayList<>(Main.getPlugin().getStatsManager().getPlayerNames());
                    return tabs.isEmpty() ? Collections.singletonList("-") : tabs;
                }
            }
        }else if(args.length == 4){
            if(args[0].equalsIgnoreCase("kit")) {
                if(args[1].equalsIgnoreCase("rename")){
                    return Collections.singletonList("<newName>");
                }
            }else if(args[0].equalsIgnoreCase("stats")) {
                String[] text = {"rating", "wins","loses","kills","deaths"};
                ArrayList<String> tabs = new ArrayList<>();
                Collections.addAll(tabs, text);
                if(args[1].equalsIgnoreCase("get"))
                    tabs.add("k/d");
                return tabs;
            }
        }
        return null;
    }

    public boolean isValidInput(String input) {
        String pattern = "^[a-zA-Z0-9_-]+$";
        return input.matches(pattern);
    }
}
