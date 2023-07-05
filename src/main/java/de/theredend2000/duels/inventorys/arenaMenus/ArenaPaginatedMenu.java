package de.theredend2000.duels.inventorys.arenaMenus;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.inventorys.PlayerMenuUtility;
import de.theredend2000.duels.util.ItemBuilder;
import org.bukkit.Material;

public abstract class ArenaPaginatedMenu extends ArenaMenu {

    protected int page = 0;
    protected int maxItemsPerPage = 18;
    protected int index = 0;

    public ArenaPaginatedMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }
    public void addMenuBorder(String opponent, String arena, String kit){
        inventory.setItem(18, new ItemBuilder(Material.PLAYER_HEAD).setSkullOwner(Main.getTexture("ZDU5YmUxNTU3MjAxYzdmZjFhMGIzNjk2ZDE5ZWFiNDEwNDg4MGQ2YTljZGI0ZDVmYTIxYjZkYWE5ZGIyZDEifX19")).setDisplayname("§2Left").build());

        inventory.setItem(26, new ItemBuilder(Material.PLAYER_HEAD).setSkullOwner(Main.getTexture("NDJiMGMwN2ZhMGU4OTIzN2Q2NzllMTMxMTZiNWFhNzVhZWJiMzRlOWM5NjhjNmJhZGIyNTFlMTI3YmRkNWIxIn19fQ==")).setDisplayname("§2Right").build());

        inventory.setItem(21, new ItemBuilder(Material.PLAYER_HEAD).setSkullOwner(Main.getTexture("ZDU5YmUxNTU3MjAxYzdmZjFhMGIzNjk2ZDE5ZWFiNDEwNDg4MGQ2YTljZGI0ZDVmYTIxYjZkYWE5ZGIyZDEifX19")).setDisplayname("§eBack").build());
        inventory.setItem(22, new ItemBuilder(Material.PLAYER_HEAD).setOwner(opponent).setDisplayname("§6§l"+opponent).setLocalizedName(opponent).build());
        inventory.setItem(23, new ItemBuilder(Main.getPlugin().getKitManager().getKitMaterial(kit)).setDisplayname("§9§lKit").setLore("","§7Current Kit: §6"+kit).setLocalizedName(kit).build());

        inventory.setItem(19, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayname("§c").setLocalizedName(arena).build());
        inventory.setItem(20, super.FILLER_GLASS);
        inventory.setItem(24, super.FILLER_GLASS);
        inventory.setItem(25, super.FILLER_GLASS);
    }

    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }
}

