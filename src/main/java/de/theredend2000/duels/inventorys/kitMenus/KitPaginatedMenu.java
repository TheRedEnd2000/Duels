package de.theredend2000.duels.inventorys.kitMenus;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.inventorys.PlayerMenuUtility;
import de.theredend2000.duels.util.ItemBuilder;
import org.bukkit.Material;

public abstract class KitPaginatedMenu extends KitMenu {

    protected int page = 0;
    protected int maxItemsPerPage = 18;
    protected int index = 0;

    public KitPaginatedMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }
    public void addMenuBorder(String opponent, String arena, String kit){
        inventory.setItem(18, new ItemBuilder(Material.PLAYER_HEAD).setSkullOwner(Main.getTexture("ZDU5YmUxNTU3MjAxYzdmZjFhMGIzNjk2ZDE5ZWFiNDEwNDg4MGQ2YTljZGI0ZDVmYTIxYjZkYWE5ZGIyZDEifX19")).setDisplayname("§2Left").build());

        inventory.setItem(26, new ItemBuilder(Material.PLAYER_HEAD).setSkullOwner(Main.getTexture("NDJiMGMwN2ZhMGU4OTIzN2Q2NzllMTMxMTZiNWFhNzVhZWJiMzRlOWM5NjhjNmJhZGIyNTFlMTI3YmRkNWIxIn19fQ==")).setDisplayname("§2Right").build());

        inventory.setItem(21, new ItemBuilder(Material.PLAYER_HEAD).setSkullOwner(Main.getTexture("ZDU5YmUxNTU3MjAxYzdmZjFhMGIzNjk2ZDE5ZWFiNDEwNDg4MGQ2YTljZGI0ZDVmYTIxYjZkYWE5ZGIyZDEifX19")).setDisplayname("§eBack").build());
        inventory.setItem(22, new ItemBuilder(Material.PLAYER_HEAD).setOwner(opponent).setDisplayname("§6§l"+opponent).setLocalizedName(opponent).build());
        inventory.setItem(23, new ItemBuilder(Main.getPlugin().getArenaManager().getArenaMaterial(Main.getPlugin().getArenaManagerHashMap().get(arena))).setDisplayname("§2§lArena").setLore("","§7Current Arena: §6"+arena).setLocalizedName(arena).build());

        inventory.setItem(19, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayname("§c").setLocalizedName(kit).build());
        inventory.setItem(20, super.FILLER_GLASS);
        inventory.setItem(24, super.FILLER_GLASS);
        inventory.setItem(25, super.FILLER_GLASS);
    }

    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }
}

