package de.theredend2000.duels.arenas;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.game.GameState;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class Arena {

    private String name;
    private boolean enabled;
    private ArrayList<UUID> playerInGame;
    private Location lobbySpawn;
    private Location endSpawn;
    private Location spawn1;
    private Location spawn2;
    private Location pos1;
    private Location pos2;
    private GameState gameState;

    public Arena(String name, boolean enabled, ArrayList<UUID> playerInGame, Location lobbySpawn, Location endSpawn, Location spawn1, Location spawn2, Location pos1, Location pos2, GameState gameState) {
        this.name = name;
        this.enabled = enabled;
        this.playerInGame = playerInGame;
        this.lobbySpawn = lobbySpawn;
        this.endSpawn = endSpawn;
        this.spawn1 = spawn1;
        this.spawn2 = spawn2;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.gameState = gameState;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public ArrayList<UUID> getPlayerInGame() {
        return playerInGame;
    }

    public void addPlayerInGame(Player player){
        removePlayerFromAllArenas(player);
        this.playerInGame.add(player.getUniqueId());
    }

    public boolean containsPlayerInGame(Player player){
        return this.playerInGame.contains(player.getUniqueId());
    }

    public void removePlayerInGame(Player player){
        this.playerInGame.remove(player.getUniqueId());
    }

    public void removePlayerFromAllArenas(Player player){
        for(Arena arena : Main.getPlugin().getArenaManagerHashMap().values())
            arena.removePlayerInGame(player);
    }

    public void setPlayerInGame(ArrayList<UUID> playerInGame) {
        this.playerInGame = playerInGame;
    }

    public Location getLobbySpawn() {
        return lobbySpawn;
    }

    public void setLobbySpawn(Location lobbySpawn) {
        this.lobbySpawn = lobbySpawn;
    }

    public Location getEndSpawn() {
        return endSpawn;
    }

    public void setEndSpawn(Location endSpawn) {
        this.endSpawn = endSpawn;
    }

    public Location getSpawn1() {
        return spawn1;
    }

    public void setSpawn1(Location spawn1) {
        this.spawn1 = spawn1;
    }

    public Location getSpawn2() {
        return spawn2;
    }

    public void setSpawn2(Location spawn2) {
        this.spawn2 = spawn2;
    }

    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
