package de.theredend2000.duels.queue;

import de.theredend2000.duels.arenas.Arena;
import de.theredend2000.duels.kits.Kit;
import org.bukkit.entity.Player;

public class Queue {

    private Player player1;
    private Player player2;
    private Arena arena;
    private Kit kit;
    private int size;

    public Queue(Player player1, Player player2, Arena arena, Kit kit, int size) {
        this.player1 = player1;
        this.player2 = player2;
        this.arena = arena;
        this.kit = kit;
        this.size = size;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player) {
        this.player1 = player;
        if (player1 == null) {
            this.size = 0;
            return;
        }
        if (player2 == null)
            this.size = 1;
        else
            this.size = 2;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player) {
        this.player2 = player;
        if (player1 == null) {
            this.size = 0;
            return;
        }
        if (player2 == null)
            this.size = 1;
        else
            this.size = 2;
    }

    public Kit getKit() {
        return kit;
    }

    public void setKit(Kit kit) {
        this.kit = kit;
    }

    public Arena getArena() {
        return arena;
    }

    public void setArena(Arena arena) {
        this.arena = arena;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
