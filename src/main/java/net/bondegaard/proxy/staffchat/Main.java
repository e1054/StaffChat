package net.bondegaard.proxy.staffchat;

import com.google.common.io.ByteStreams;
import lombok.Getter;
import net.bondegaard.proxy.staffchat.staffchat.AdminChat;
import net.bondegaard.proxy.staffchat.staffchat.StaffChat;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.*;

public final class Main extends Plugin {

    @Getter
    private static Main instance;
    @Getter
    private Configuration config;

    @Override
    public void onEnable() {
        instance = this;

        //Load config
        loadConfig();


        //Commands
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new StaffChat(this));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new AdminChat(this));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void saveConfig() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(getDataFolder(), "config.yml"));
        } catch (Exception err) {
            throw new RuntimeException("Unable to load configuration file", err);
        }
    }
    public void loadConfig() {
        if (!getDataFolder().exists()) {
            getDataFolder().mkdir();
        }
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                try (InputStream is = getResourceAsStream("config.yml");
                     OutputStream os = new FileOutputStream(configFile)) {
                    ByteStreams.copy(is, os);
                }
            } catch (IOException e) {
                throw new RuntimeException("Unable to create configuration file", e);
            }
        }
        try {
            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (Exception err) {
            err.printStackTrace();
        }

    }

    private void load(Object object) {
        getProxy().getPluginManager().registerListener(this, (Listener) object);
    }

}
