package de.theredend2000.duels.inventorys.queueMenus;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.inventorys.PlayerMenuUtility;
import de.theredend2000.duels.inventorys.arenaMenus.ArenaMenu;
import de.theredend2000.duels.util.ItemBuilder;
import org.bukkit.Material;

import java.util.ArrayList;

public abstract class QueuePaginatedMenu extends QueueMenu {

    protected int page = 0;
    protected int maxItemsPerPage = 18;
    protected int index = 0;

    public QueuePaginatedMenu(PlayerMenuUtility playerMenuUtility) {
        super(playerMenuUtility);
    }
    public void addMenuBorder(){
        inventory.setItem(18, new ItemBuilder(Material.PLAYER_HEAD).setSkullOwner(Main.getTexture("ZDU5YmUxNTU3MjAxYzdmZjFhMGIzNjk2ZDE5ZWFiNDEwNDg4MGQ2YTljZGI0ZDVmYTIxYjZkYWE5ZGIyZDEifX19")).setLore("§6Page: §7(§b"+(page+1)+"§7/§b"+getMaxPages()+"§7)","","§eClick to scroll.").setDisplayname("§2Left").build());

        inventory.setItem(26, new ItemBuilder(Material.PLAYER_HEAD).setSkullOwner(Main.getTexture("NDJiMGMwN2ZhMGU4OTIzN2Q2NzllMTMxMTZiNWFhNzVhZWJiMzRlOWM5NjhjNmJhZGIyNTFlMTI3YmRkNWIxIn19fQ==")).setLore("§6Page: §7(§b"+(page+1)+"§7/§b"+getMaxPages()+"§7)","","§eClick to scroll.").setDisplayname("§2Right").build());

        inventory.setItem(22, new ItemBuilder(Material.BARRIER).setDisplayname("§4Close").build());

        inventory.setItem(21, super.FILLER_GLASS);
        inventory.setItem(20, super.FILLER_GLASS);
        inventory.setItem(19, super.FILLER_GLASS);
        inventory.setItem(23, super.FILLER_GLASS);
        inventory.setItem(24, super.FILLER_GLASS);
        inventory.setItem(25, super.FILLER_GLASS);
    }

    public int getMaxItemsPerPage() {
        return maxItemsPerPage;
    }

    public int getMaxPages(){
        ArrayList<String> keys = new ArrayList<>(Main.getPlugin().getArenaManagerHashMap().keySet());
        return (int) Math.ceil((double) keys.size() / getMaxItemsPerPage());
    }
}

