package de.theredend2000.duels.replay;

import de.theredend2000.duels.arenas.Arena;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Replay {

    private Arena arena;
    private Player player1;
    private Player player2;
    private ArrayList<Player> spectators;

    public Replay(Arena arena, Player player1, Player player2, ArrayList<Player> spectators){
        this.arena = arena;
        this.player1 = player1;
        this.player2 = player2;
        this.spectators = spectators;
    }
}
