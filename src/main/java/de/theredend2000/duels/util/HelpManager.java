package de.theredend2000.duels.util;

import de.theredend2000.duels.Main;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class HelpManager {

    public static void sendPlayerDefaultHelp(Player player){
        player.sendMessage(Main.PREFIX+"§9§lDuels §3§lrunning on version §e§l"+Main.getPlugin().getDescription().getVersion());
        player.sendMessage(" ");
        player.sendMessage("§a> §6§l/duels arena help \n  §bList of all the commands of the arena section.");
        player.sendMessage("§a> §6§l/duels kit help \n  §bList of all the commands of the kit section.");
        player.sendMessage("§a> §6§l/duels reload \n  §bReloads the config.yml and the stats.yml file.");
        player.sendMessage("§a> §6§l/duels stats help \n  §bList of all the commands of the stats section.");
    }

    public static void sendPlayerArenaHelp(Player player){
        player.sendMessage(Main.PREFIX+"§9§lDuels §3§lrunning on version §e§l"+Main.getPlugin().getDescription().getVersion());
        player.sendMessage(" ");
        player.sendMessage("§a> §6§l/duels arena create <name> \n  §bCreates a new arena.");
        player.sendMessage("§a> §6§l/duels arena delete <name> \n  §bDeletes an existing arena.");
        player.sendMessage("§a> §6§l/duels arena edit <name> \n  §bOpens the arena edit menu for this arena.");
        player.sendMessage("§a> §6§l/duels arena list \n  §bLists all existing arenas.");
        player.sendMessage("§a> §6§l/duels arena setIcon <name> \n  §bSets the holding item as icon for the arena.");
    }

    public static void sendPlayerKitHelp(Player player){
        player.sendMessage(Main.PREFIX+"§9§lDuels §3§lrunning on version §e§l"+Main.getPlugin().getDescription().getVersion());
        player.sendMessage(" ");
        player.sendMessage("§a> §6§l/duels kit save <name> \n  §bSaves your current inventory as kit.");
        player.sendMessage("§a> §6§l/duels kit delete <name> \n  §bDeletes an existing kit.");
        player.sendMessage("§a> §6§l/duels kit edit <name> \n  §bEdit an existing kit with your current inventory.");
        player.sendMessage("§a> §6§l/duels kit list \n  §bLists all existing kits.");
        player.sendMessage("§a> §6§l/duels kit load <name> \n  §bLoad the content of the kit in your inventory.");
        player.sendMessage("§a> §6§l/duels kit rename <name> <newName> \n  §bRenames an existing kit.");
        player.sendMessage("§a> §6§l/duels kit setIcon \n  §bSets the holding item as icon for the kit.");
    }

    public static void sendPlayerStatsHelp(Player player) {
        TextComponent message = new TextComponent(Main.PREFIX + "§9§lDuels §3§lrunning on version §e§l" + Main.getPlugin().getDescription().getVersion());
        message.addExtra("\n\n§aNote: Hover over '<option>' to see them all.\n");
        message.addExtra("§a> §6§l/duels stats add <player> ");
        message.addExtra(getOptionText());
        message.addExtra(" §6§l<score> \n  §bAdds the following score to option of the player.\n");
        message.addExtra("§a> §6§l/duels stats set <player> ");
        message.addExtra(getOptionText());
        message.addExtra(" §6§l<score> \n  §bSets the following score to option of the player.\n");
        message.addExtra("§a> §6§l/duels stats remove <player> ");
        message.addExtra(getOptionText());
        message.addExtra(" §6§l<score> \n  §bRemoves the following score to option of the player.\n");
        message.addExtra("§a> §6§l/duels stats get <player>");
        message.addExtra("\n  §bGets the following score from option of the player.\n");
        message.addExtra("§a> §6§l/duels stats reset <player> ");
        message.addExtra(getOptionText());
        message.addExtra("\n  §bResets the following score from option of the player.\n");
        message.addExtra("§4§lSTATS CHANGING IS NOT RECOMMENDED");

        player.spigot().sendMessage(message);
    }

    private static TextComponent getOptionText(){
        TextComponent clickme = new TextComponent("§6§l<option>§6§l");
        clickme.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§6§lAvailable Options:\n§3- rating\n§3- kills\n§3- looses\n§3- deaths\n§3- wins\n§3- k/d")));
        return clickme;
    }

    public static void sendPlayerDuelHelp(Player player){
        player.sendMessage(Main.PREFIX+"§9§lDuels §3§lrunning on version §e§l"+Main.getPlugin().getDescription().getVersion());
        player.sendMessage(" ");
        player.sendMessage("§a> §6§l/duel \n  §bOpens an queue inventory with all available arenas.");
        player.sendMessage("§a> §6§l/duel <player> \n  §bOpens an duel inventory where you can configure the duel and send a request.");
    }

}
