package de.theredend2000.duels.util;

import de.theredend2000.duels.Main;
import de.theredend2000.duels.util.MessageKey;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class MessageManager {

    private final Main plugin;
    private FileConfiguration messagesConfig;

    public MessageManager() {
        this.plugin = Main.getPlugin();
        reloadMessages();
    }

    public void reloadMessages() {
        String lang = plugin.getConfig().getString("message-lang");
        if (lang == null)
            lang = "en";

        File messagesFolder = new File(plugin.getDataFolder(), "messages");
        if (!messagesFolder.exists()) {
            messagesFolder.mkdirs();
        }

        File messagesFile = new File(messagesFolder, "messages-" + lang + ".yml");
        if (!messagesFile.exists()) {
            try {
                InputStream in = plugin.getResource("messageFiles/messages-" + lang + ".yml");
                if (in != null) {
                    Files.copy(in, messagesFile.toPath());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public String getMessage(MessageKey key) {
        String message = messagesConfig.getString(key.getPath());
        if (message != null) {
            message = ChatColor.translateAlternateColorCodes('&', Main.PREFIX + message);
            return message;
        }
        return "Message not found: " + key.name();
    }
}