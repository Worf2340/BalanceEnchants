package com.mctng.balanceenchants;

import com.mctng.balanceenchants.listeners.EnchantItemListener;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class BalanceEnchants extends JavaPlugin {

    private FileConfiguration levelsConfig;
    private FileConfiguration weightsConfig;


    private HashMap<Enchantment, EnchantmentData> enchantmentDataHashMap;

    @Override
    public void onEnable() {
        updateEnchantmentDataHashMap();

        this.getCommand("be").setExecutor(new ReloadCommand(this));
        this.getServer().getPluginManager().registerEvents(new EnchantItemListener(this), this);


    }


    private FileConfiguration getLevelsConfig() {
        return levelsConfig;
    }

    private FileConfiguration getWeightsConfig() {
        return weightsConfig;
    }

    public HashMap<Enchantment, EnchantmentData> getEnchantmentDataHashMap() {
        return enchantmentDataHashMap;
    }

    /**
     * Load the levels.yml file. If the file does not exist, create it based on default file.
     */
    private void createLevelsConfig() {
        File levelsConfigFile = new File(getDataFolder(), "levels.yml");
        if (!levelsConfigFile.exists()) {
            levelsConfigFile.getParentFile().mkdirs();
            saveResource("levels.yml", false);
        }
        levelsConfig = new YamlConfiguration();
        try {
            levelsConfig.load(levelsConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load the weights.yml file. If the file does not exist, create it based on default file.
     */
    private void createWeightsConfig() {
        File weightsConfigFile = new File(getDataFolder(), "weights.yml");
        if (!weightsConfigFile.exists()) {
            weightsConfigFile.getParentFile().mkdirs();
            saveResource("weights.yml", false);
        }
        weightsConfig = new YamlConfiguration();
        try {
            weightsConfig.load(weightsConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Refreshes EnchantmentData map based on file changes.
     */
    public void updateEnchantmentDataHashMap() {
        createLevelsConfig();
        createWeightsConfig();

        this.enchantmentDataHashMap = readConfigs();
    }


    /**
     * Reads levels.yml and weights.yml file, and parses each enchantment's weight and applicable power levels.
     * @return
     */
    public HashMap<Enchantment, EnchantmentData> readConfigs() {
        HashMap<Enchantment, EnchantmentData> enchantmentDataHashMap = new HashMap<>();

        for (Enchantment enchantment : Enchantment.values()) {

            int weight = this.getWeightsConfig().getInt(enchantment.getName());
            HashMap<Integer, int[]> levels = new HashMap<>();

            for (Map<?, ?> powerLevelsMap : getLevelsConfig().getMapList(enchantment.getName())) {
                int powerLevel = (int) powerLevelsMap.keySet().toArray()[0];

                HashMap<String, Integer> enchantmentLevelsMap = (HashMap<String, Integer>) powerLevelsMap.get(powerLevel);
                int minLevel = enchantmentLevelsMap.get("minLevel");
                int maxLevel = enchantmentLevelsMap.get("maxLevel");

                levels.put(powerLevel, new int[]{minLevel, maxLevel});
            }

            enchantmentDataHashMap.put(enchantment, new EnchantmentData(weight, levels));
        }

        return enchantmentDataHashMap;
    }

}
