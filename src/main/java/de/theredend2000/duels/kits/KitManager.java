package de.theredend2000.duels.kits;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KitManager {

    public KitManager(){

    }

    public void saveKit(Player player, String name){
        Kit kit = new Kit(Main.getPlugin(), name);
        String[] values = Serialization.invToBase64(player.getInventory());
        kit.getConfig().set("inv", values[0]);
        kit.getConfig().set("armor", values[1]);
        kit.saveKit();
        Main.getPlugin().getKitManagerHashMap().put(name,kit);
    }

    public void removeKit(String name){
        Kit cfg = new Kit(Main.getPlugin(), name);
        cfg.removeKit();
        Main.getPlugin().getKitManagerHashMap().remove(name);
    }

    public boolean existsKit(String kitName){
        for(Kit kits : Main.getPlugin().getKitManagerHashMap().values()){
            if(kits.getName().equals(kitName)){
                return true;
            }
        }
        return false;
    }

    public void renameKit(String oldName,String newName){
        Kit cfg = new Kit(Main.getPlugin(), oldName);
        cfg.renameKit(newName);
        Main.getPlugin().getKitManagerHashMap().remove(oldName);
        Main.getPlugin().getKitManagerHashMap().put(newName,cfg);
    }

    public void loadAllKits(){
        boolean sendMessages = Main.getPlugin().getConfig().getBoolean("messages.send-kit-loading-message");
        if(sendMessages)
            Bukkit.getConsoleSender().sendMessage("\n§4§lLOADING KITS...");
        for(String name : listKitNames()){
            Kit kit = new Kit(Main.getPlugin(), name);
            Main.getPlugin().getKitManagerHashMap().put(name, kit);
            if(sendMessages)
                Bukkit.getConsoleSender().sendMessage("§7Kit §a" + kit.getName() + " §7loaded §2successfully§7.");
        }
        if(sendMessages)
            Bukkit.getConsoleSender().sendMessage("§2§lALL KITS SUCCESSFULLY LOADED!\n");
    }

    public void loadKit(Player player, String name){
        Kit kit = new Kit(Main.getPlugin(),name);
        String[] values = new String[]{kit.getConfig().getString("inv"), kit.getConfig().getString("armor")};
        ItemStack[][] items = Serialization.base64toInv(values);
        player.getInventory().clear();
        player.getInventory().setContents(items[0]);
        player.getInventory().setArmorContents(items[1]);
    }

    public List<String> getLoreFromKit(String name){
        Kit kit = new Kit(Main.getPlugin(),name);
        String[] values = new String[]{kit.getConfig().getString("inv"), kit.getConfig().getString("armor")};
        ItemStack[][] items = Serialization.base64toInv(values);
        return combineItemsInLore(items);
    }

    public List<String> combineItemsInLore(ItemStack[][] items) {
        Map<Material, Integer> itemCountMap = new HashMap<>();
        List<String> lore = new ArrayList<>();
        boolean hasArmor = false;
        for (ItemStack[] itemArray : items) {
            for (ItemStack item : itemArray) {
                if (item != null && item.getType() != Material.AIR && isArmorPiece(item)) {
                    processArmorItem(item, itemCountMap);
                    hasArmor = true;
                }
            }
        }

        lore.add("§3§lArmor:");
        if (!hasArmor) {
            lore.add("  §4§lNONE");
        } else {
            addArmorItemsToLore(itemCountMap, lore);
        }
        lore.add("");
        lore.add("§3§lItems:");
        itemCountMap.clear();
        for (ItemStack[] itemArray : items) {
            for (ItemStack item : itemArray) {
                if (item != null && item.getType() != Material.AIR && !isArmorPiece(item)) {
                    processItem(item, itemCountMap);
                }
            }
        }
        addItemsToLore(itemCountMap, lore);
        lore.add("");
        lore.add("§eClick to select.");

        return lore;
    }

    private boolean isArmorPiece(ItemStack item) {
        Material itemType = item.getType();
        return itemType.name().endsWith("_HELMET") || itemType.name().endsWith("_CHESTPLATE") ||
                itemType.name().endsWith("_LEGGINGS") || itemType.name().endsWith("_BOOTS");
    }

    private void processItem(ItemStack item, Map<Material, Integer> itemCountMap) {
        Material itemType = item.getType();
        int itemCount = itemCountMap.getOrDefault(itemType, 0);
        itemCount += item.getAmount();
        itemCountMap.put(itemType, itemCount);
    }

    private void processArmorItem(ItemStack item, Map<Material, Integer> itemCountMap) {
        Material itemType = item.getType();
        int itemCount = itemCountMap.getOrDefault(itemType, 0);
        itemCount += 1;
        itemCountMap.put(itemType, itemCount);
    }

    private void addItemsToLore(Map<Material, Integer> itemCountMap, List<String> lore) {
        for (Map.Entry<Material, Integer> entry : itemCountMap.entrySet()) {
            Material itemType = entry.getKey();
            int itemCount = entry.getValue();
            String itemName = itemType.toString().replace("_", " ").toLowerCase();
            itemName = capitalizeFirstLetter(itemName);
            String loreLine = "  §7- §d" + itemName + " §8(" + itemCount + "x)";
            lore.add(loreLine);
        }
    }

    private void addArmorItemsToLore(Map<Material, Integer> itemCountMap, List<String> lore) {
        for (Map.Entry<Material, Integer> entry : itemCountMap.entrySet()) {
            Material itemType = entry.getKey();
            String itemName = itemType.toString().replace("_", " ").toLowerCase();
            itemName = capitalizeFirstLetter(itemName);
            String loreLine = "  §7- §d" + itemName;
            lore.add(loreLine);
        }
    }

    private String capitalizeFirstLetter(String input) {
        StringBuilder sb = new StringBuilder();
        String[] words = input.split(" ");
        for (String word : words) {
            if (word.length() > 0) {
                sb.append(Character.toUpperCase(word.charAt(0)));
                sb.append(word.substring(1).toLowerCase());
                sb.append(" ");
            }
        }
        return sb.toString().trim();
    }

    public void setKitMaterial(String name, Material material){
        Kit kit = new Kit(Main.getPlugin(),name);
        kit.setIcon(material);
    }

    public Material getKitMaterial(String name){
        Kit kit = new Kit(Main.getPlugin(),name);
        return kit.getIcon();
    }

    public List<String> listKitNames() {
        List<String> kitNames = new ArrayList<>();

        File folder = new File(Main.getPlugin().getDataFolder()+"//kits//");

        if (folder.exists() && folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".yml")) {
                        String kitName = file.getName().replace(".yml", "");
                        kitNames.add(kitName);
                    }
                }
            }
        }
        return kitNames;
    }

}
