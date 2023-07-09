package de.theredend2000.duels.extramanagers;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.arenas.Arena;
import de.theredend2000.duels.stats.StatsManager;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.*;

public class SpecialsManager {


    public String getDeathMessage(EntityDamageEvent.DamageCause damageCause,Player death){
        switch (damageCause){
            case FALL: case FIRE: case LAVA: case VOID: case MAGIC: case POISON: case WITHER: case DROWNING: case FIRE_TICK: case LIGHTNING: case PROJECTILE: case SUFFOCATION: case FALLING_BLOCK: case BLOCK_EXPLOSION: case ENTITY_EXPLOSION:
                return getStringOutOfConfig(damageCause.name()).replaceAll("%death%", death.getDisplayName()).replaceAll("&","§");
            default:
                return "§6"+death.getDisplayName()+" §7died.";
        }
    }

    public String getDeathMessageWithKiller(EntityDamageEvent.DamageCause damageCause,Player killer,Player death){
        if (damageCause == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            return getStringOutOfConfig(damageCause.name()).replaceAll("%death%", death.getDisplayName()).replaceAll("%killer%", killer.getDisplayName()).replaceAll("&", "§");
        }
        return "§6" + death.getDisplayName() + "§fdied.";
    }

    private String getStringOutOfConfig(String damageCause){
        return Main.getPlugin().getConfig().getString("messages.deaths."+damageCause.toLowerCase());
    }

    public List<String> getStatsOfPlayerAsLore(Player player){
        StatsManager statsManager = Main.getPlugin().getStatsManager();
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add("§3Stats of "+player.getDisplayName());
        lore.add("§7Rating: §d"+statsManager.getRating(player.getUniqueId()));
        lore.add("§7Wins: §d"+statsManager.getWins(player.getUniqueId()));
        lore.add("§7Losses: §d"+statsManager.getLosses(player.getUniqueId()));
        lore.add("§7Kills: §d"+statsManager.getKills(player.getUniqueId()));
        lore.add("§7Deaths: §d"+statsManager.getDeaths(player.getUniqueId()));
        lore.add("§7K/D: §d"+statsManager.getKD(player.getUniqueId()));
        return lore;
    }

    public boolean shouldShow(Player player){
        boolean enabled = Main.getPlugin().getConfig().getBoolean("layout.show-stats-on-playerhead.enabled");
        if(enabled){
            String type = Main.getPlugin().getConfig().getString("layout.show-stats-on-playerhead.type");
            if(type == null) return false;
            switch (Objects.requireNonNull(type).toUpperCase()){
                case "ALL":
                    return true;
                case "OPERATOR":
                    return player.isOp();
                case "PERMISSION":
                    return player.hasPermission(Objects.requireNonNull(Main.getPlugin().getConfig().getString("permissions.show-stats-on-playerhead-permission")));
                case "NONE": default:
                    return false;
            }
        }
        return false;
    }

    public void spawnRandomFirework(Arena arena){
        if(!Main.getPlugin().getConfig().getBoolean("game.end-firework.enabled")) return;
        int count = Main.getPlugin().getConfig().getInt("game.end-firework.count");
        for (int i = 0; i < count; i++) {
            spawnFirework(getRandomLocation(arena));
        }
    }

    private Location getRandomLocation(Arena arena){
        Location pos1 = arena.getPos1();
        Location pos2 = arena.getPos2();
        double minX = Math.min(pos1.getX(), pos2.getX());
        double minY = Math.min(pos1.getY(), pos2.getY());
        double minZ = Math.min(pos1.getZ(), pos2.getZ());
        double maxX = Math.max(pos1.getX(), pos2.getX());
        double maxY = Math.max(pos1.getY(), pos2.getY());
        double maxZ = Math.max(pos1.getZ(), pos2.getZ());

        Random random = new Random();
        double randomX = minX + (random.nextDouble() * (maxX - minX));
        double randomY = minY + (random.nextDouble() * (maxY - minY));
        double randomZ = minZ + (random.nextDouble() * (maxZ - minZ));

        return new Location(arena.getEndSpawn().getWorld(), randomX, randomY, randomZ);
    }

    private void spawnFirework(Location location) {
        Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        FireworkEffect.Builder effectBuilder = FireworkEffect.builder();

        int colorCount = new Random().nextInt(4) + 1;
        for (int i = 0; i < colorCount; i++) {
            Color color = getRandomFireworkColor();
            effectBuilder.withColor(color);
        }

        FireworkEffect.Type effectType = getRandomFireworkEffectType();
        effectBuilder.with(effectType);

        fireworkMeta.addEffect(effectBuilder.build());
        int power = Main.getPlugin().getConfig().getInt("game.end-firework.power");
        fireworkMeta.setPower(power);

        firework.setFireworkMeta(fireworkMeta);
    }

    private Color getRandomFireworkColor() {
        Color[] colors = {
                Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.LIME,
                Color.BLUE, Color.FUCHSIA, Color.PURPLE, Color.NAVY, Color.WHITE
        };

        int randomIndex = new Random().nextInt(colors.length);
        return colors[randomIndex];
    }

    private FireworkEffect.Type getRandomFireworkEffectType() {
        FireworkEffect.Type[] effectTypes = FireworkEffect.Type.values();

        int randomIndex = new Random().nextInt(effectTypes.length);
        return effectTypes[randomIndex];
    }

}
