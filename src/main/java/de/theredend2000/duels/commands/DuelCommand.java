package de.theredend2000.duels.commands;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import de.theredend2000.duels.game.GameState;
import de.theredend2000.duels.inventorys.queueMenus.QueueListMenu;
import de.theredend2000.duels.kits.Kit;
import de.theredend2000.duels.util.HelpManager;
import de.theredend2000.duels.util.MessageKey;
import de.theredend2000.duels.util.MessageManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.swing.*;
import java.util.*;

public class DuelCommand implements CommandExecutor, TabCompleter {
    private Main plugin;
    private MessageManager messageManager;

    public DuelCommand() {
        plugin = Main.getPlugin();
        messageManager = plugin.getMessageManager();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if(Main.getPlugin().getConfig().getBoolean("permissions.need-duel-command-permission")){
                if(!player.hasPermission((Objects.requireNonNull(Main.getPlugin().getConfig().getString("permissions.duel-command-permission"))))) {
                    player.sendMessage(messageManager.getMessage(MessageKey.PERMISSION_ERROR));
                    return false;
                }
            }
            if (Main.getPlugin().getBannedWorlds().contains(player.getWorld())) {
                player.sendMessage(messageManager.getMessage(MessageKey.BANNED_WORLD_ERROR));
                return false;
            }
            if (Main.getPlugin().getArenaManager().playerIsAlreadyInArena(player)) {
                player.sendMessage(messageManager.getMessage(MessageKey.ALREADY_IN_DUEL_ERROR));
                return false;
            }
            Arena playingArena = Main.getPlugin().getGameManager().getRandomArena();
            if(playingArena == null){
                if(player.isOp() || player.hasPermission((Objects.requireNonNull(Main.getPlugin().getConfig().getString("permissions.duel-command-permission")))))
                    player.sendMessage(messageManager.getMessage(MessageKey.NO_ARENAS_AVAILABLE_WITH_PERMISSION));
                else
                    player.sendMessage(messageManager.getMessage(MessageKey.NO_ARENAS_AVAILABLE));
                return false;
            }
            Kit kit = Main.getPlugin().getGameManager().getRandomKit();
            if(kit == null){
                if(player.isOp() || player.hasPermission((Objects.requireNonNull(Main.getPlugin().getConfig().getString("permissions.duel-command-permission")))))
                    player.sendMessage(messageManager.getMessage(MessageKey.NO_KITS_AVAILABLE_WITH_PERMISSION));
                else
                    player.sendMessage(messageManager.getMessage(MessageKey.NO_KITS_AVAILABLE));
                return false;
            }
            if(args.length == 0){
                new QueueListMenu(Main.getPlayerMenuUtility(player)).open();
            }else if (args.length == 1) {
                Player opponent = Bukkit.getPlayer(args[0]);
                if (opponent == null) {
                    player.sendMessage(messageManager.getMessage(MessageKey.PLAYER_NOT_AVAILABLE));
                    return false;
                }
               if (opponent.equals(player)) {
                    player.sendMessage(messageManager.getMessage(MessageKey.CANNOT_DUEL_YOURSELF));
                    return false;
                }
                if (Main.getPlugin().getBannedWorlds().contains(opponent.getWorld())) {
                    player.sendMessage(messageManager.getMessage(MessageKey.BANNED_WORLD_ERROR));
                    return false;
                }
                if (Main.getPlugin().getDuelRequests().containsKey(player) || Main.getPlugin().getDuelRequests().containsValue(opponent)) {
                    player.sendMessage(messageManager.getMessage(MessageKey.DUEL_REQUEST_ALREADY_SENT).replaceAll("%opponent%",opponent.getDisplayName()));
                    return false;
                }
                if (Main.getPlugin().getArenaManager().playerIsAlreadyInArena(opponent)) {
                    player.sendMessage(messageManager.getMessage(MessageKey.OPPONENT_ALREADY_IN_DUEL_ERROR).replaceAll("%opponent%",opponent.getDisplayName()));
                    return false;
                }

                if (playingArena.getGameState().equals(GameState.WAITING)) {
                    Main.getPlugin().getInventoryManager().getPlayerDuelMenuMain(player, opponent, playingArena, kit);
                } else {
                    player.sendMessage(messageManager.getMessage(MessageKey.ARENA_UNAVAILABLE));
                }
            } else if (args[0].equalsIgnoreCase("accept") && args.length == 4) {
                Player opponent = Bukkit.getPlayer(args[1]);
                if (opponent == null) {
                    player.sendMessage(messageManager.getMessage(MessageKey.NO_ARENAS_AVAILABLE));
                    return false;
                }
                if (!Main.getPlugin().getDuelRequests().containsKey(opponent) || !Main.getPlugin().getDuelRequests().containsValue(player)) {
                    player.sendMessage(messageManager.getMessage(MessageKey.NO_DUEL_REQUEST).replaceAll("%opponent%",opponent.getDisplayName()));
                    return false;
                }
                if (Main.getPlugin().getArenaManager().playerIsAlreadyInArena(player)) {
                    player.sendMessage(messageManager.getMessage(MessageKey.ALREADY_IN_DUEL_ERROR));
                    return false;
                }
                if (Main.getPlugin().getArenaManager().playerIsAlreadyInArena(opponent)) {
                    player.sendMessage(messageManager.getMessage(MessageKey.OPPONENT_ALREADY_IN_DUEL).replaceAll("%opponent%",opponent.getDisplayName()));
                    return false;
                }

                String arenaName = args[2];
                String kitName = args[3];

                Arena playingArena2 = Main.getPlugin().getArenaManagerHashMap().get(arenaName);
                Kit kit2 = Main.getPlugin().getKitManagerHashMap().get(kitName);

                if (playingArena2 != null && playingArena2.getGameState().equals(GameState.WAITING)) {
                    Main.getPlugin().getGameManager().duelPlayer(player, opponent, playingArena2, kit2);
                    Main.getPlugin().getDuelRequests().remove(opponent);
                    Main.getPlugin().getDuelRequests().values().remove(player);
                    Main.getPlugin().getDuelRequestTime().remove(opponent);
                } else {
                    player.sendMessage(messageManager.getMessage(MessageKey.ARENA_UNAVAILABLE));
                }
            } else if (args[0].equalsIgnoreCase("deny") && args.length == 2) {
                Player opponent = Bukkit.getPlayer(args[1]);
                if (opponent == null) {
                    player.sendMessage(messageManager.getMessage(MessageKey.NO_ARENAS_AVAILABLE));
                    return false;
                }
                if (!Main.getPlugin().getDuelRequests().containsKey(opponent) || !Main.getPlugin().getDuelRequests().containsValue(player)) {
                    player.sendMessage(messageManager.getMessage(MessageKey.NO_DUEL_REQUEST).replaceAll("%opponent%",opponent.getDisplayName()));
                    return false;
                }

                Main.getPlugin().getDuelRequests().remove(opponent);
                Main.getPlugin().getDuelRequests().values().remove(player);
                Main.getPlugin().getDuelRequestTime().remove(opponent);
                sendDuelRequestDeniedMessage(player, opponent);
            }else
                HelpManager.sendPlayerDuelHelp(player);
        } else
            sender.sendMessage(messageManager.getMessage(MessageKey.ONLY_PLAYERS_ERROR));
        return false;
    }

    public void sendRequest(Player sender, Player opponent, Arena arena, Kit kit) {
        sender.sendMessage(messageManager.getMessage(MessageKey.DUEL_REQUEST_SENT).replaceAll("%opponent%",opponent.getDisplayName()));
        Main.getPlugin().getDuelRequests().put(sender, opponent);
        Main.getPlugin().getDuelRequestTime().put(sender,30);
        opponent.sendMessage("§d-=-=-=-=-=-=§9Duel Request§d-=-=-=-=-=-=");
        opponent.sendMessage("§e" + sender.getDisplayName() + " §7wants to duel you.");
        opponent.sendMessage("§7Arena: §6" + arena.getName());
        opponent.sendMessage("§7Kit: §4" + kit.getName());

        TextComponent acceptComponent = new TextComponent("§7|----------- §a§l[Accept] §5§l----- ");
        TextComponent denyComponent = new TextComponent("§c§l[Deny] §7-----------|");

        acceptComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel accept " + sender.getName() + " " + arena.getName() + " " + kit.getName()));
        acceptComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§aAccept the duel request from §e" + sender.getDisplayName() + "§a.")));

        denyComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/duel deny " + sender.getName()));
        denyComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§cDeny the duel request from §e" + sender.getDisplayName() + "§c.")));

        TextComponent messageComponent = new TextComponent("");
        messageComponent.addExtra(acceptComponent);
        messageComponent.addExtra(denyComponent);

        opponent.spigot().sendMessage(messageComponent);
        opponent.sendMessage("§d-=-=-=-=-=-=§9Duel Request§d-=-=-=-=-=-=");
    }

    public void sendDuelRequestDeniedMessage(Player sender, Player opponent) {
        opponent.sendMessage(messageManager.getMessage(MessageKey.DUEL_REQUEST_DENIED));
        sender.sendMessage(messageManager.getMessage(MessageKey.DUEL_REQUEST_DENIED_BY_SENDER));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length >= 3) {
            return Collections.singletonList(" ");
        }
        return null;
    }

    public void checkTime() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Map<Player, Integer> duelRequestTime = Main.getPlugin().getDuelRequestTime();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (duelRequestTime.containsKey(player)) {
                        int currentTime = duelRequestTime.get(player);
                        if (currentTime == 0) {
                            duelRequestTime.remove(player);
                            Main.getPlugin().getDuelRequests().remove(player);
                            player.sendMessage(messageManager.getMessage(MessageKey.DUEL_REQUEST_EXPIRED));
                            continue;
                        }
                        duelRequestTime.put(player, currentTime - 1);
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(), 0, 20);
    }
}
