package de.theredend2000.duels.stats;

import com.google.common.base.Charsets;
import de.theredend2000.duels.Main;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class StatsManager {

    private FileConfiguration statsYaml;
    private File statsFile;

    public StatsManager() {
        statsFile = new File(Main.getPlugin().getDataFolder(), "stats.yml");
        statsYaml = YamlConfiguration.loadConfiguration(statsFile);
    }

    public void saveStatsYaml() {
        try {
            statsYaml.save(statsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePlayerDefaultStats(Player player){
        statsYaml.set("Stats."+player.getUniqueId()+".playerName",player.getName());
        statsYaml.set("Stats."+player.getUniqueId()+".rating", 1000);
        statsYaml.set("Stats."+player.getUniqueId()+".wins",0);
        statsYaml.set("Stats."+player.getUniqueId()+".looses",0);
        statsYaml.set("Stats."+player.getUniqueId()+".kills",0);
        statsYaml.set("Stats."+player.getUniqueId()+".deaths",0);
        statsYaml.set("Stats."+player.getUniqueId()+".k/d",0);
        saveStatsYaml();
    }

    public boolean joinedFirstTime(Player player){
        return !statsYaml.contains("Stats."+player.getUniqueId());
    }

    public int getRating(UUID uuid){
        return statsYaml.getInt("Stats."+uuid+".rating");
    }
    public int getWins(UUID uuid){
        return statsYaml.getInt("Stats."+uuid+".wins");
    }
    public int getLooses(UUID uuid){
        return statsYaml.getInt("Stats."+uuid+".looses");
    }
    public int getKills(UUID uuid){
        return statsYaml.getInt("Stats."+uuid+".kills");
    }
    public int getDeaths(UUID uuid){
        return statsYaml.getInt("Stats."+uuid+".deaths");
    }
    public double getKD(UUID uuid){
        return statsYaml.getDouble("Stats."+uuid+".k/d");
    }
    public void addRating(Player player, int count){
        statsYaml.set("Stats."+player.getUniqueId()+".rating",getRating(player.getUniqueId()) + count);
        saveStatsYaml();
    }
    public void removeRating(Player player, int count){
        statsYaml.set("Stats."+player.getUniqueId()+".rating",getRating(player.getUniqueId()) - count);
        saveStatsYaml();
    }
    public void setRating(Player player, int count){
        statsYaml.set("Stats."+player.getUniqueId()+".rating",count);
        saveStatsYaml();
    }
    public void addWins(Player player, int count){
        statsYaml.set("Stats."+player.getUniqueId()+".wins",getWins(player.getUniqueId()) + count);
        saveStatsYaml();
    }
    public void addLooses(Player player, int count){
        statsYaml.set("Stats."+player.getUniqueId()+".looses",getLooses(player.getUniqueId()) + count);
        saveStatsYaml();
    }
    public void addDeaths(Player player, int count){
        statsYaml.set("Stats."+player.getUniqueId()+".deaths",getDeaths(player.getUniqueId()) + count);
        saveStatsYaml();
        updateKD(player);
    }
    public void addKills(Player player, int count){
        statsYaml.set("Stats."+player.getUniqueId()+".kills",getKills(player.getUniqueId()) + count);
        saveStatsYaml();
        updateKD(player);
    }
    public void setKD(Player player, double count){
        statsYaml.set("Stats."+player.getUniqueId()+".k/d",count);
        saveStatsYaml();
    }

    public void setWins(Player player, double count){
        statsYaml.set("Stats."+player.getUniqueId()+".wins",count);
        saveStatsYaml();
    }
    public void setDeaths(Player player, double count){
        statsYaml.set("Stats."+player.getUniqueId()+".deaths",count);
        saveStatsYaml();
        updateKD(player);
    }
    public void setLooses(Player player, double count){
        statsYaml.set("Stats."+player.getUniqueId()+".looses",count);
        saveStatsYaml();
        updateKD(player);
    }
    public void setKills(Player player, double count){
        statsYaml.set("Stats."+player.getUniqueId()+".kills",count);
        saveStatsYaml();
    }

    public void reloadStats() {
        statsYaml = YamlConfiguration.loadConfiguration(statsFile);
        final InputStream defConfigStream = Main.getPlugin().getResource("stats.yml");
        if (defConfigStream == null) {
            return;
        }
        statsYaml.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
    }

    private void updateKD(Player player) {
        double kills = getKills(player.getUniqueId());
        double deaths = getDeaths(player.getUniqueId());
        double kd = deaths != 0 ? kills / deaths : kills;
        kd = Math.round(kd * 1000.0) / 1000.0;
        setKD(player, kd);
    }

    public void updatePlayerStats(Player winner,Player looser){
        addKills(winner,1);
        addWins(winner,1);
        addRating(winner,(int) Math.ceil(winner.getHealth()));
        addLooses(looser,1);
        addDeaths(looser,1);
        removeRating(looser,(int) Math.ceil(winner.getHealth()));
        updateKD(winner);
        updateKD(looser);
    }

    public List<String> getPlayerNames(){
        List<String> names = new ArrayList<>();
        if(statsYaml.contains("Stats.")) {
            for (String uuids : statsYaml.getConfigurationSection("Stats.").getKeys(false)){
                Collections.addAll(names,statsYaml.getString("Stats."+uuids+".playerName"));
            }
            return names;
        }
        return null;
    }

    public boolean containsPlayer(String name){
        if(statsYaml.contains("Stats.")) {
            for (String uuids : statsYaml.getConfigurationSection("Stats.").getKeys(false)){
                if(name.equals(statsYaml.getString("Stats."+uuids+".playerName"))){
                    return true;
                }
            }
        }
        return false;
    }

    public UUID getPlayerUUID(String playerName){
        if(statsYaml.contains("Stats.")) {
            for (String uuids : statsYaml.getConfigurationSection("Stats.").getKeys(false)){
                if(playerName.equals(statsYaml.getString("Stats."+uuids+".playerName"))){
                    return UUID.fromString(uuids);
                }
            }
        }
        return null;
    }

    public String getPlayerName(UUID uuid){
        if(statsYaml.contains("Stats.")) {
            for (String uuids : statsYaml.getConfigurationSection("Stats.").getKeys(false)){
                if(uuid.equals(UUID.fromString(uuids))){
                    return statsYaml.getString("Stats."+uuids+".playerName");
                }
            }
        }
        return null;
    }

    public String getOption(String value){
        switch (value.toLowerCase()){
            case "rating":
            case "wins":
            case "looses":
            case "kills":
            case "deaths":
            case "k/d":
                return value;
        }
        return null;
    }

    public int getOptionScore(String option,UUID uuid){
        switch (option){
            case "rating":
                return getRating(uuid);
            case "wins":
                return getWins(uuid);
            case "looses":
                return getLooses(uuid);
            case "kills":
                return getKills(uuid);
            case "deaths":
                return getDeaths(uuid);

        }
        return -1;
    }

    public void finishChanges(String symbole,UUID uuid,String option,Player player,int score){
        int currentScore = getOptionScore(option,uuid);
        if(currentScore == -1){
            player.sendMessage(Main.PREFIX+"§cThere was an error.");
        }
        String playerName = getPlayerName(uuid);
        switch (symbole.toLowerCase()){
            case "add":
                statsYaml.set("Stats."+uuid+"."+getOption(option), currentScore+score);
                player.sendMessage(Main.PREFIX+"§7You have §2successfully §aadded §e"+score+" score§7 to the §estatistics "+getOption(option)+" §7of §6"+playerName+"§7.");
                break;
            case "remove":
                statsYaml.set("Stats."+uuid+"."+getOption(option), currentScore-score);
                player.sendMessage(Main.PREFIX+"§7You have §2successfully §cremoved §e"+score+" score§7 from the §estatistics "+getOption(option)+" §7of §6"+playerName+"§7.");
                break;
            case "set":
                statsYaml.set("Stats."+uuid+"."+getOption(option), score);
                player.sendMessage(Main.PREFIX+"§7You have §2successfully §9set §7the §estatistics "+getOption(option)+" §7of §6"+playerName+"§7 to §e"+score+" score§7.");
                break;
            case "reset":
                statsYaml.set("Stats."+uuid+"."+getOption(option), score);
                player.sendMessage(Main.PREFIX+"§7You have §2successfully §4reseted §7the §estatistics "+getOption(option)+" §7of §6"+playerName+"§7.");
                break;
        }
        saveStatsYaml();
        updateKD(player);
    }

}
