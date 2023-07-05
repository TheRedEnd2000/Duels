package de.theredend2000.duels.commands;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import de.theredend2000.duels.game.GameState;
import de.theredend2000.duels.kits.Kit;
import de.theredend2000.duels.util.BlockUtils;
import de.theredend2000.duels.util.HelpManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.*;

public class DuelsCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if (Main.getPlugin().getConfig().getBoolean("permissions.need-duels-command-permission")) {
                if (!player.hasPermission((Objects.requireNonNull(Main.getPlugin().getConfig().getString("permissions.duels-command-permission"))))) {
                    player.sendMessage(Main.PREFIX + "§cYou do not have the right permission to use this command.");
                    return false;
                }
            }
            if(args.length == 0){
                HelpManager.sendPlayerDefaultHelp(player);
                return true;
            }
            if(args[0].equalsIgnoreCase("arena")){
                if(args[1].equalsIgnoreCase("create") && args.length == 3){
                    try {
                        String name = args[2];
                        if(isValidInput(name)) {
                            if (!Main.getPlugin().getArenaManagerHashMap().containsKey(name)) {
                                Main.getPlugin().getArenaManagerHashMap().put(name, new Arena(name, false, new ArrayList<UUID>(), null, null, null, null, null, null, GameState.DISABLED));
                                Main.getPlugin().getArenaManager().saveNewArena(name);
                                player.sendMessage(Main.PREFIX + "§7The arena §e" + name + " §7was §2created §asuccessfully§7.");
                            } else
                                player.sendMessage(Main.PREFIX + "§cThis arena does already exist.");
                        } else
                            player.sendMessage(Main.PREFIX + "§cArena names can only contain §e'Letters (a) | Numbers (1) | Underscores (_)  | Hyphen (-)'§c.");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else if(args[1].equalsIgnoreCase("delete") && args.length == 3){
                    try {
                        String name = args[2];
                        if(Main.getPlugin().getArenaManagerHashMap().containsKey(name)) {
                            Main.getPlugin().getArenaManagerHashMap().remove(name);
                            Main.getPlugin().getArenaManager().removeArena(name);
                            player.sendMessage(Main.PREFIX+"§7The arena §e"+name+" §7was §cdeleted §asuccessfully§7.");
                        }else
                            player.sendMessage(Main.PREFIX+"§cThere is no arena with this name.");
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
                            player.sendMessage(Main.PREFIX+"§cThere is no arena with this name.");
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
                    HelpManager.sendPlayerArenaHelp(player);
                }else if(args[1].equalsIgnoreCase("setIcon") && args.length == 3){
                    try {
                        String name = args[2];
                        if(Main.getPlugin().getArenaManagerHashMap().containsKey(name)) {
                            Arena arena = Main.getPlugin().getArenaManagerHashMap().get(name);
                            if(player.getItemInHand().getItemMeta() != null){
                                Material icon = player.getInventory().getItemInMainHand().getType();
                                Main.getPlugin().getArenaManager().setArenaMaterial(arena,icon);
                                player.sendMessage(Main.PREFIX+"§7The §2icon §7for the arena §e"+arena.getName()+" §7was set to §6"+icon.name()+"§7.");
                            }else
                                player.sendMessage(Main.PREFIX+"§cPlease hold the item you want as icon in your hand.");
                        }else
                            player.sendMessage(Main.PREFIX+"§cThere is no arena with this name.");
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
                                player.sendMessage(Main.PREFIX + "§7Your current inventory was §asuccessfully §2saved §7as kit §e" + name + "§7.");
                            } else
                                player.sendMessage(Main.PREFIX + "§cThis Kit does already exists.");
                        }else
                            player.sendMessage(Main.PREFIX + "§cKit names can only contain §e'Letters (a) | Numbers (1) | Underscores (_)  | Hyphen (-)'§c.");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else if(args[1].equalsIgnoreCase("delete") && args.length == 3){
                    try {
                        String name = args[2];
                        if(Main.getPlugin().getKitManagerHashMap().containsKey(name)) {
                            Main.getPlugin().getKitManager().removeKit(name);
                            player.sendMessage(Main.PREFIX+"§7The kit §e"+name+" §7was §cdeleted §asuccessfully§7.");
                        }else
                            player.sendMessage(Main.PREFIX+"§cThis Kit does not exist.");
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
                                player.sendMessage(Main.PREFIX + "§7The Kit §e" + oldName + " §7was §asuccessfully §2renamed §7to §6" + newName + "§7.");
                            } else
                                player.sendMessage(Main.PREFIX + "§cThere is already a kit called " + newName + ".");
                        } else
                            player.sendMessage(Main.PREFIX + "§cThis Kit does not exist.");
                    }else
                        player.sendMessage(Main.PREFIX + "§cKit names can only contain §e'Letters (a) | Numbers (1) | Underscores (_)  | Hyphen (-)'§c.");
                }else if(args[1].equalsIgnoreCase("load") && args.length == 3){
                    String name = args[2];
                    if(Main.getPlugin().getKitManagerHashMap().containsKey(name)) {
                        Main.getPlugin().getKitManager().loadKit(player, name);
                        player.sendMessage(Main.PREFIX + "§7The Kit §e" + name + " §7was §2loaded §asuccessfully§7.");
                    }else
                        player.sendMessage(Main.PREFIX+"§cThis Kit does not exist.");
                }else if(args[1].equalsIgnoreCase("edit") && args.length == 3){
                    String name = args[2];
                    if(Main.getPlugin().getKitManagerHashMap().containsKey(name)) {
                        Main.getPlugin().getKitManagerHashMap().remove(name);
                        Main.getPlugin().getKitManager().saveKit(player,name);
                        player.sendMessage(Main.PREFIX + "§7The Kit §e" + name + " §7was §2edited §asuccessfully§7.");
                    }else
                        player.sendMessage(Main.PREFIX+"§cThis Kit does not exist.");
                }else if(args[1].equalsIgnoreCase("setIcon") && args.length == 3){
                    try {
                        String name = args[2];
                        if(Main.getPlugin().getKitManagerHashMap().containsKey(name)) {
                            Kit kit = Main.getPlugin().getKitManagerHashMap().get(name);
                            if(player.getItemInHand().getItemMeta() != null){
                                Material icon = player.getInventory().getItemInMainHand().getType();
                                Main.getPlugin().getKitManager().setKitMaterial(kit.getName(),icon);
                                player.sendMessage(Main.PREFIX+"§7The §2icon §7for the kit §e"+kit.getName()+" §7was set to §6"+icon.name()+"§7.");
                            }else
                                player.sendMessage(Main.PREFIX+"§cPlease hold the item you want as icon in your hand.");
                        }else
                            player.sendMessage(Main.PREFIX+"§cThere is no kit with this name.");
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else
                    HelpManager.sendPlayerKitHelp(player);
            }else if(args[0].equalsIgnoreCase("reload") && args.length == 1){
                Main.getPlugin().reloadConfig();
                player.sendMessage(Main.PREFIX+"§7The §3config.yml file §7was §ereloaded §asuccessfully§7.");
                Main.getPlugin().getStatsManager().reloadStats();
                player.sendMessage(Main.PREFIX+"§7The §3stats.yml file§7 was §ereloaded §asuccessfully§7.");
            }else if(args[0].equalsIgnoreCase("stats")){
                if(args.length == 5) {
                    if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove") || args[1].equalsIgnoreCase("set")) {
                        String playerName = args[2];
                        if (!Main.getPlugin().getStatsManager().containsPlayer(playerName)) {
                            player.sendMessage(Main.PREFIX + "§cThis player does not exist.");
                            return false;
                        }
                        UUID playerUUID = Main.getPlugin().getStatsManager().getPlayerUUID(playerName);
                        String option = Main.getPlugin().getStatsManager().getOption(args[3]);
                        if (option == null) {
                            player.sendMessage(Main.PREFIX + "§cYou option is invadable. Use one from the following options: §e rating, wins, looses, kills, deaths");
                            return false;
                        }
                        if (option.equalsIgnoreCase("k/d")) {
                            player.sendMessage(Main.PREFIX + "§cYou cant use this option here.");
                            return false;
                        }
                        try {
                            int score = Integer.parseInt(args[4]);
                            Main.getPlugin().getStatsManager().finishChanges(args[1], playerUUID, option, player, score);
                        } catch (NumberFormatException e) {
                            player.sendMessage(Main.PREFIX + "§cPlease us a number as last args.");
                        }
                    } else
                        HelpManager.sendPlayerStatsHelp(player);
                }else if(args.length == 4){
                    if(args[1].equalsIgnoreCase("get")){
                        String playerName = args[2];
                        if(!Main.getPlugin().getStatsManager().containsPlayer(playerName)){
                            player.sendMessage(Main.PREFIX+"§cThis player does not exist.");
                            return false;
                        }
                        UUID playerUUID = Main.getPlugin().getStatsManager().getPlayerUUID(playerName);
                        String option = Main.getPlugin().getStatsManager().getOption(args[3]);
                        if(option == null){
                            player.sendMessage(Main.PREFIX+"§cYou option is invadable. Use one from the following options: §e rating, wins, looses, kills, deaths");
                            return false;
                        }
                        int score = Main.getPlugin().getStatsManager().getOptionScore(option,playerUUID);
                        if(option.equals("k/d"))
                            player.sendMessage(Main.PREFIX+"§7The §escore §7of §6"+playerName+" §7in the §estatistic "+option+" §7is §e"+Main.getPlugin().getStatsManager().getKD(playerUUID)+"§7.");
                        else
                            player.sendMessage(Main.PREFIX+"§7The §escore §7of §6"+playerName+" §7in the §estatistic "+option+" §7is §e"+score+"§7.");
                    }else if(args[1].equalsIgnoreCase("reset")){
                        String playerName = args[2];
                        if(!Main.getPlugin().getStatsManager().containsPlayer(playerName)){
                            player.sendMessage(Main.PREFIX+"§cThis player does not exist.");
                            return false;
                        }
                        UUID playerUUID = Main.getPlugin().getStatsManager().getPlayerUUID(playerName);
                        String option = Main.getPlugin().getStatsManager().getOption(args[3]);
                        if(option == null){
                            player.sendMessage(Main.PREFIX+"§cYou option is invadable. Use one from the following options: §e rating, wins, looses, kills, deaths");
                            return false;
                        }
                        if(option.equals("rating")) {
                            int rating = Main.getPlugin().getConfig().getInt("stats.rating-default");
                            Main.getPlugin().getStatsManager().finishChanges(args[1],playerUUID,option,player,rating);
                        } else
                            Main.getPlugin().getStatsManager().finishChanges(args[1],playerUUID,option,player,0);
                    }else
                        HelpManager.sendPlayerStatsHelp(player);
                }else if(args.length == 2){
                    if(args[1].equalsIgnoreCase("help")){
                        HelpManager.sendPlayerStatsHelp(player);
                    }else
                        HelpManager.sendPlayerStatsHelp(player);
                }else
                    HelpManager.sendPlayerStatsHelp(player);
            }else
                HelpManager.sendPlayerDefaultHelp(player);
        }else
            sender.sendMessage(Main.PREFIX+"§cOnly player can use this command.");
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
                String[] text = {"rating", "wins","looses","kills","deaths"};
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
