package com.mctng.customizeenchants;

import com.mctng.customizeenchants.listeners.OnEnchant;
import com.mctng.customizeenchants.listeners.OnPrepareEnchant;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class CustomizeEnchants extends JavaPlugin {

    private File levelsConfigFile;
    private FileConfiguration levelsConfig;
    private File weightsConfigFile;
    private FileConfiguration weightsConfig;

    @Override
    public void onEnable(){
        createLevelsConfig();
        createWeightsConfig();
        this.getServer().getPluginManager().registerEvents(new OnPrepareEnchant(), this);
        this.getServer().getPluginManager().registerEvents(new OnEnchant(), this);
    }

    public FileConfiguration getLevelsConfig() {
        return levelsConfig;
    }

    public FileConfiguration getWeightsConfig() {
        return weightsConfig;
    }

    private void createLevelsConfig() {
        levelsConfigFile = new File(getDataFolder(), "levels.yml");
        if (!levelsConfigFile.exists()) {
            levelsConfigFile.getParentFile().mkdirs();
            saveResource("levels.yml", false);
        }
        levelsConfig = new YamlConfiguration();
        YamlConfiguration.loadConfiguration(levelsConfigFile);
    }

    private void createWeightsConfig() {
        weightsConfigFile = new File(getDataFolder(), "weights.yml");
        if (!weightsConfigFile.exists()) {
            weightsConfigFile.getParentFile().mkdirs();
            saveResource("weights.yml", false);
        }
        weightsConfig = new YamlConfiguration();
        YamlConfiguration.loadConfiguration(weightsConfigFile);
    }

}
