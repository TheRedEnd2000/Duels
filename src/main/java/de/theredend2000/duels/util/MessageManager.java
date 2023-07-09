package de.theredend2000.duels.util;

import de.theredend2000.duels.Main;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MessageManager {

    private final Main plugin;
    private FileConfiguration messagesConfig;

    public MessageManager() {
        this.plugin = Main.getPlugin();
    }

    public void reloadMessages() {
        String lang = plugin.getConfig().getString("message-lang");
        if (lang == null)
            lang = "en";

        File messagesFile = new File(plugin.getDataFolder(), "messages-" + lang + ".yml");

        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }

        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public String getMessage(MessageKey key) {
        String message = messagesConfig.getString(key.getPath());
        if (message != null) {
            message = ChatColor.translateAlternateColorCodes('&', message);
            return message;
        }
        return "Message not found: " + key.name();
    }
}