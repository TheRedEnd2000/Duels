package de.theredend2000.duels.arenas;

import de.theredend2000.duels.kits.Kit;
import org.bukkit.entity.Player;

public class PlayAgain {

    private Arena arena;
    private Kit kit;
    private Player player1;
    private Player player2;

    public PlayAgain(Arena arena, Kit kit, Player player1, Player player2){
        this.arena = arena;
        this.kit = kit;
        this.player1 = player1;
        this.player2 = player2;
    }

    public Arena getArena() {
        return arena;
    }

    public Kit getKit() {
        return kit;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public void setKit(Kit kit) {
        this.kit = kit;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }
}
