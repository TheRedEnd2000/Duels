package de.theredend2000.duels.kits;

import de.theredend2000.duels.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Kit {
    private Main plugin;
    private String file;
    private File folder;
    private FileConfiguration cfg;
    private File cfgFile;

    public Kit(Main plugin, String file){
        this.plugin = plugin;
        this.file = file+".yml";
        folder = new File(plugin.getDataFolder()+"//kits//");
        cfg = null;
        cfgFile = null;
        reload();
    }

    public void reload() {
        if(!folder.exists())
            folder.mkdirs();

        cfgFile = new File(folder,file);
        if(!cfgFile.exists()){
            try {
                cfgFile.createNewFile();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        cfg = YamlConfiguration.loadConfiguration(cfgFile);
    }

    public String getName() {
        return file.replace(".yml", "");
    }

    public FileConfiguration getConfig() {
        if(cfg == null)
            reload();
        return cfg;
    }

    public void saveKit(){
        if(cfg == null || cfgFile == null)
            return;

        try {
            getConfig().save(cfgFile);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void removeKit() {
        if (cfg == null || cfgFile == null)
            return;
        cfgFile.delete();
    }

    public void renameKit(String newName) {
        if (cfg == null || cfgFile == null)
            return;

        File newFile = new File(cfgFile.getParentFile(), newName + ".yml");
        if (cfgFile.exists()) {
            if (newFile.exists()) {
                return;
            }

            if (cfgFile.renameTo(newFile)) {
                cfgFile = newFile;
                file = newFile.getName();
                cfg = YamlConfiguration.loadConfiguration(cfgFile);
            }
        }
    }

    public void setIcon(Material material){
        if(cfg == null || cfgFile == null)
            return;
        getConfig().set("Icon",material.name());
        try {
            getConfig().save(cfgFile);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Material getIcon(){
        if(cfg == null || cfgFile == null)
            return null;
        return Main.getPlugin().getKitMaterial(cfg.getString("Icon")) != null ? Main.getPlugin().getKitMaterial(cfg.getString("Icon")) : Material.getMaterial(Objects.requireNonNull(Main.getPlugin().getConfig().getString("items.default-kit-item")));
    }


}
