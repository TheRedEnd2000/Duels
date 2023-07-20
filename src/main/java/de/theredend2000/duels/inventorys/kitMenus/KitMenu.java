package de.theredend2000.duels.inventorys.kitMenus;

import de.theredend2000.duels.inventorys.PlayerMenuUtility;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class KitMenu implements InventoryHolder {

    protected PlayerMenuUtility playerMenuUtility;
    protected Inventory inventory;
    protected String search;
    protected ItemStack FILLER_GLASS = makeItem(Material.GRAY_STAINED_GLASS_PANE, " ");

    public KitMenu(PlayerMenuUtility playerMenuUtility) {
        this.playerMenuUtility = playerMenuUtility;
    }

    public abstract String getMenuName();
    public abstract int getSlots();
    public abstract ArrayList<String> getKeysArray();
    public abstract void handleMenu(InventoryClickEvent e);
    public abstract void setMenuItems(String opponent, String arena, String kit);

    public void open(String opponent, String arena, String kit,String search) {
        this.search = search;
        inventory = Bukkit.createInventory(this, getSlots(), getMenuName());

        this.setMenuItems(opponent, arena, kit);

        playerMenuUtility.getOwner().openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public ItemStack makeItem(Material material, String displayName, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemMeta.setLore(Arrays.asList(lore));
        item.setItemMeta(itemMeta);
        return item;
    }
}
