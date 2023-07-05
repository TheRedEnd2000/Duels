package de.theredend2000.duels.playerSaves;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PlayerSaves {

    private Player player;
    private int level, foodLevel;
    private float exp;
    private double health;
    private ItemStack[] armorContents,playerInventory,extraContents;
    private GameMode gameMode;

    public PlayerSaves(Player player, int level, float exp, int foodLevel, double health, ItemStack[] playerInventory, ItemStack[] armorContents, ItemStack[] extraContents, GameMode gameMode){
        this.player = player;
        this.level = level;
        this.exp = exp;
        this.foodLevel = foodLevel;
        this.health = health;
        this.playerInventory = playerInventory;
        this.armorContents = armorContents;
        this.extraContents = extraContents;
        this.gameMode = gameMode;
    }

    public Player getPlayer() {
        return player;
    }

    public double getHealth() {
        return health;
    }

    public float getExp() {
        return exp;
    }

    public ItemStack[] getExtraContents() {
        return extraContents;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public int getFoodLevel() {
        return foodLevel;
    }

    public int getLevel() {
        return level;
    }

    public ItemStack[] getPlayerInventory() {
        return playerInventory;
    }

    public ItemStack[] getArmorContents() {
        return armorContents;
    }
}
