package de.theredend2000.duels;

import de.theredend2000.duels.arenas.Arena;
import de.theredend2000.duels.arenas.ArenaManager;
import de.theredend2000.duels.arenas.PlayAgain;
import de.theredend2000.duels.commands.DuelCommand;
import de.theredend2000.duels.commands.DuelsCommand;
import de.theredend2000.duels.countdowns.ArenaEndCountdown;
import de.theredend2000.duels.countdowns.ArenaWaitingCountdown;
import de.theredend2000.duels.extramanagers.ScoreboardManagers;
import de.theredend2000.duels.extramanagers.SpecialsManager;
import de.theredend2000.duels.game.GameManager;
import de.theredend2000.duels.inventorys.InventoryManager;
import de.theredend2000.duels.inventorys.PlayerMenuUtility;
import de.theredend2000.duels.extramanagers.ItemManager;
import de.theredend2000.duels.kits.Kit;
import de.theredend2000.duels.kits.KitManager;
import de.theredend2000.duels.listeners.*;
import de.theredend2000.duels.playerSaves.PlayerSaves;
import de.theredend2000.duels.playerSaves.PlayerSavesManager;
import de.theredend2000.duels.queue.Queue;
import de.theredend2000.duels.queue.QueueManager;
import de.theredend2000.duels.stats.StatsManager;
import de.theredend2000.duels.util.BlockUtils;
import de.theredend2000.duels.util.HelpManager;
import de.theredend2000.duels.util.MessageManager;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public final class Main extends JavaPlugin {

    private static Main plugin;
    public static final String PREFIX = "§f[§9Duels§f] §r";
    private HashMap<String, Arena> arenaManagerHashMap;
    private HashMap<Arena, PlayAgain> playAgainHashMap;
    private HashMap<Player, PlayerSaves> playerSavesHashMap;
    private HashMap<Arena,Kit> arenaKit;
    private ArrayList<World> bannedWorlds;
    private HashMap<String, Kit> kitManagerHashMap;
    private HashMap<Arena, Queue> queueManagerHashMap;
    private HashMap<Player, Player> duelRequests;
    private HashMap<Player, Integer> duelRequestTime;
    private ArenaManager arenaManager;
    private GameManager gameManager;
    private InventoryManager inventoryManager;
    private ArenaWaitingCountdown arenaWaitingCountdown;
    private SpecialsManager specialsManager;
    private ArenaEndCountdown arenaEndCountdown;
    private PlayerSavesManager playerSavesManager;
    private KitManager kitManager;
    private ItemManager itemManager;
    private ScoreboardManagers scoreboardManagers;
    private StatsManager statsManager;
    private QueueManager queueManager;
    private MessageManager messageManager;

    @Override
    public void onEnable() {
        plugin = this;
        Bukkit.getServer().getScheduler().runTask(Main.getPlugin(), () -> {
            Bukkit.getConsoleSender().sendMessage("\n");
            Bukkit.getConsoleSender().sendMessage("§6§lDUELS BY TheRedEnd2000 v" + getDescription().getVersion());
            Bukkit.getConsoleSender().sendMessage("\n");
            initConfiguration();
            initManagers();
            initCommands();
            initListeners();
            initExtras();
            initBannedWorlds();
            arenaManager.loadAllArena();
            kitManager.loadAllKits();
        });
    }

    @Override
    public void onDisable() {
        arenaManager.saveAllArenas();
        BlockUtils.restoreAllBlocks();
    }

    private void initManagers(){
        messageManager = new MessageManager();
        scoreboardManagers = new ScoreboardManagers();
        scoreboardManagers.updateScoreboard();
        arenaManager = new ArenaManager();
        gameManager = new GameManager();
        inventoryManager = new InventoryManager();
        kitManager = new KitManager();
        itemManager = new ItemManager();
        statsManager = new StatsManager();
        queueManager = new QueueManager();
        playerSavesManager = new PlayerSavesManager();
        specialsManager = new SpecialsManager();
        new HelpManager();
        Bukkit.getConsoleSender().sendMessage("§aAll Managers loaded.");
    }

    private void initListeners(){
        new SignListener();
        new PlayerInteractWithDuelItems();
        new InventoryClickListener();
        new GameListeners();
        new OtherListeners();
        new PlayerConnectionListener();
        Bukkit.getConsoleSender().sendMessage("§aAll Listeners loaded.");
    }

    private void initConfiguration(){
        this.saveDefaultConfig();
        Bukkit.getConsoleSender().sendMessage("§aAll Configurations loaded.");
    }

    private void initCommands(){
        getCommand("duels").setExecutor(new DuelsCommand());
        getCommand("duel").setExecutor(new DuelCommand());
        new DuelCommand().checkTime();
        Bukkit.getConsoleSender().sendMessage("§aAll Commands loaded.");
    }

    private void initExtras(){
        new BlockUtils();
        arenaKit = new HashMap<>();
        bannedWorlds = new ArrayList<>();
        duelRequests = new HashMap<>();
        duelRequestTime = new HashMap<>();
        kitManagerHashMap = new HashMap<>();
        arenaManagerHashMap = new HashMap<>();
        arenaWaitingCountdown = new ArenaWaitingCountdown();
        arenaEndCountdown = new ArenaEndCountdown();
        playAgainHashMap = new HashMap<>();
        queueManagerHashMap = new HashMap<>();
        playerSavesHashMap = new HashMap<>();
        Bukkit.getConsoleSender().sendMessage("§aAll Extras loaded.");
    }

    private void initBannedWorlds(){
        for(World worlds : Bukkit.getWorlds()){
            if(getConfig().getStringList("lists.banned-worlds").contains(worlds.getName())){
                bannedWorlds.add(worlds);
            }
        }
    }

    private static final HashMap<Player, PlayerMenuUtility> playerMenuUtilityMap = new HashMap<>();
    public static PlayerMenuUtility getPlayerMenuUtility(Player p) {
        PlayerMenuUtility playerMenuUtility;
        if (!(playerMenuUtilityMap.containsKey(p))) {

            playerMenuUtility = new PlayerMenuUtility(p);
            playerMenuUtilityMap.put(p, playerMenuUtility);

            return playerMenuUtility;
        } else {
            return playerMenuUtilityMap.get(p);
        }
    }

    public Material getArenaMaterial(String materialString) {
        try {
            return Material.getMaterial(Objects.requireNonNull(materialString));
        } catch (Exception ex) {
            return Material.getMaterial(Objects.requireNonNull(getConfig().getString("items.default-arena-item")));
        }
    }

    public Material getKitMaterial(String materialString) {
        try {
            return Material.getMaterial(Objects.requireNonNull(materialString));
        } catch (Exception ex) {
            return Material.getMaterial(Objects.requireNonNull(getConfig().getString("items.default-kit-item")));
        }
    }

    public Material getMaterial(String materialString) {
        try {
            return Material.getMaterial(Objects.requireNonNull(materialString));
        } catch (Exception ex) {
            return Material.STONE;
        }
    }

    public static String getTexture(String texture){
        String prefix = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUv";
        texture = prefix+texture;
        return texture;
    }

    public static Main getPlugin() {
        return plugin;
    }

    public HashMap<String, Arena> getArenaManagerHashMap() {
        return arenaManagerHashMap;
    }

    public HashMap<String, Kit> getKitManagerHashMap() {
        return kitManagerHashMap;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public ArenaWaitingCountdown getArenaWaitingCountdown() {
        return arenaWaitingCountdown;
    }

    public ArenaEndCountdown getArenaEndCountdown() {
        return arenaEndCountdown;
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }

    public HashMap<Player, Player> getDuelRequests() {
        return duelRequests;
    }

    public HashMap<Player, Integer> getDuelRequestTime() {
        return duelRequestTime;
    }

    public HashMap<Arena, Kit> getArenaKit() {
        return arenaKit;
    }

    public ScoreboardManagers getScoreboardManagers() {
        return scoreboardManagers;
    }

    public ArrayList<World> getBannedWorlds() {
        return bannedWorlds;
    }

    public HashMap<Arena, PlayAgain> getPlayAgainHashMap() {
        return playAgainHashMap;
    }

    public HashMap<Arena, Queue> getQueueManagerHashMap() {
        return queueManagerHashMap;
    }

    public QueueManager getQueueManager() {
        return queueManager;
    }

    public HashMap<Player, PlayerSaves> getPlayerSavesHashMap() {
        return playerSavesHashMap;
    }

    public PlayerSavesManager getPlayerSavesManager() {
        return playerSavesManager;
    }

    public SpecialsManager getSpecialsManager() {
        return specialsManager;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }
}
