package com.mctng.customizeenchants;

import com.mctng.customizeenchants.listeners.OnEnchant;
import com.mctng.customizeenchants.listeners.OnPrepareEnchant;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public final class CustomizeEnchants extends JavaPlugin {

    private File levelsConfigFile;
    private FileConfiguration levelsConfig;
    private File weightsConfigFile;
    private FileConfiguration weightsConfig;
    private HashMap<String, String[]> itemEnchantsMap;
    private HashMap<String, String[]> itemTypesMap;
    private HashMap<String, String[]> conflictingEnchantsMap;

    @Override
    public void onEnable() {
        createLevelsConfig();
        createWeightsConfig();
        this.itemEnchantsMap = generateItemEnchantsMap();
        this.itemTypesMap = generateItemTypesMap();
        this.conflictingEnchantsMap = generateConflictingEnchantsMap();
        this.getCommand("ce").setExecutor(new ReloadCommand(this));
        this.getServer().getPluginManager().registerEvents(new OnPrepareEnchant(this), this);
        this.getServer().getPluginManager().registerEvents(new OnEnchant(this), this);


    }

    public FileConfiguration getLevelsConfig() {
        return levelsConfig;
    }

    public FileConfiguration getWeightsConfig() {
        return weightsConfig;
    }

    public void createLevelsConfig() {
        levelsConfigFile = new File(getDataFolder(), "levels.yml");
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

    public void createWeightsConfig() {
        weightsConfigFile = new File(getDataFolder(), "weights.yml");
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



    private HashMap<String, String[]> generateItemTypesMap() {
        this.itemTypesMap = new HashMap<>();

        this.itemTypesMap.put("ARMOR_ALL", new String[]{"HELMET", "CHESTPLATE",
                "LEGGINGS", "BOOTS"});
        this.itemTypesMap.put("ARMOR_BOOTS", new String[]{"BOOTS"});
        this.itemTypesMap.put("ARMOR_HELMET", new String[]{"HELMET"});

        this.itemTypesMap.put("SWORD", new String[]{"SWORD"});

        this.itemTypesMap.put("BOW", new String[]{"BOW"});

        this.itemTypesMap.put("TOOL", new String[]{"PICKAXE", "SPADE", "AXE"});

        this.itemTypesMap.put("ROD", new String[]{"FISHING_ROD"});

        return itemTypesMap;
    }
    private HashMap<String, String[]> generateItemEnchantsMap() {
        this.itemEnchantsMap = new HashMap<>();

        this.itemEnchantsMap.put("ARMOR_ALL", new String[]{"PROTECTION_ENVIRONMENTAL", "PROTECTION_EXPLOSIONS",
        "PROTECTION_FALL", "PROTECTION_FIRE", "PROTECTION_PROJECTILE", "THORNS"});
        this.itemEnchantsMap.put("ARMOR_BOOTS", new String[]{"PROTECTION_FALL"});
        this.itemEnchantsMap.put("ARMOR_HELMET", new String[]{"OXYGEN", "WATER_WORKER"});

        this.itemEnchantsMap.put("SWORD", new String[]{"DAMAGE_ALL", "DAMAGE_ARTHROPODS", "KNOCKBACK", "DAMAGE_UNDEAD",
                "FIRE_ASPECT"});

        this.itemEnchantsMap.put("BOW", new String[]{"ARROW_DAMAGE", "ARROW_FIRE", "ARROW_KNOCKBACK",
                "ARROW_INFINITE"});

        this.itemEnchantsMap.put("TOOL", new String[]{"DIG_SPEED", "LOOT_BONUS_BLOCKS", "SILK_TOUCH"});

        this.itemEnchantsMap.put("ROD", new String[]{"LUCK", "LURE"});

        this.itemEnchantsMap.put("ALL", new String[]{"DURABILITY"});

        return itemEnchantsMap;
    }

    private HashMap<String, String[]> generateConflictingEnchantsMap() {
        conflictingEnchantsMap = new HashMap<>();

        // ARMOR
        conflictingEnchantsMap.put("PROTECTION_ENVIRONMENTAL", new String[]{"PROTECTION_ENVIRONMENTAL",
                "PROTECTION_FIRE", "PROTECTION_PROJECTILE", "PROTECTION_EXPLOSIONS"});
            conflictingEnchantsMap.put("PROTECTION_FALL", new String[]{"PROTECTION_FALL"});
        conflictingEnchantsMap.put("PROTECTION_FIRE", new String[]{"PROTECTION_FIRE", "PROTECTION_ENVIRONMENTAL",
                "PROTECTION_PROJECTILE", "PROTECTION_EXPLOSIONS"});
        conflictingEnchantsMap.put("PROTECTION_PROJECTILE", new String[]{"PROTECTION_PROJECTILE",
                "PROTECTION_ENVIRONMENTAL", "PROTECTION_FIRE", "PROTECTION_EXPLOSIONS"});
        conflictingEnchantsMap.put("WATER_WORKER", new String[]{"WATER_WORKER"});
        conflictingEnchantsMap.put("PROTECTION_EXPLOSIONS", new String[]{"PROTECTION_EXPLOSIONS",
                "PROTECTION_ENVIRONMENTAL", "PROTECTION_FIRE", "PROTECTION_PROJECTILE"});
        conflictingEnchantsMap.put("OXYGEN", new String[]{"OXYGEN"});
        conflictingEnchantsMap.put("THORNS", new String[]{"THORNS"});

        // SWORD
        conflictingEnchantsMap.put("DAMAGE_ALL", new String[]{"DAMAGE_ALL", "DAMAGE_ARTHROPODS", "DAMAGE_UNDEAD"});
        conflictingEnchantsMap.put("DAMAGE_ARTHROPODS", new String[]{"DAMAGE_ARTHROPODS", "DAMAGE_ALL",
                "DAMAGE_UNDEAD"});
        conflictingEnchantsMap.put("KNOCKBACK", new String[]{"KNOCKBACK"});
        conflictingEnchantsMap.put("DAMAGE_UNDEAD", new String[]{"DAMAGE_UNDEAD", "DAMAGE_ALL", "DAMAGE_ARTHROPODS"});
        conflictingEnchantsMap.put("FIRE_ASPECT", new String[]{"FIRE_ASPECT"});
        conflictingEnchantsMap.put("LOOT_BONUS_MOBS", new String[]{"LOOT_BONUS_MOBS"});

        // BOW
        conflictingEnchantsMap.put("ARROW_DAMAGE", new String[]{"ARROW_DAMAGE"});
        conflictingEnchantsMap.put("ARROW_FIRE", new String[]{"ARROW_FIRE"});
        conflictingEnchantsMap.put("ARROW_KNOCKBACK", new String[]{"ARROW_KNOCKBACK"});
        conflictingEnchantsMap.put("ARROW_INFINITE", new String[]{"ARROW_INFINITE"});

        // TOOL
        conflictingEnchantsMap.put("DIG_SPEED", new String[]{"DIG_SPEED"});
        conflictingEnchantsMap.put("LOOT_BONUS_BLOCKS", new String[]{"LOOT_BONUS_BLOCKS", "SILK_TOUCH"});
        conflictingEnchantsMap.put("SILK_TOUCH", new String[]{"SILK_TOUCH", "LOOT_BONUS_BLOCKS"});

        // ROD
        conflictingEnchantsMap.put("LUCK", new String[]{"LUCK"});
        conflictingEnchantsMap.put("LURE", new String[]{"LURE"});

        // ALL
        conflictingEnchantsMap.put("DURABILITY", new String[]{"DURABILITY"});

        return conflictingEnchantsMap;
    }

    public HashMap<String, String[]> getConflictingEnchantsMap() {
        return conflictingEnchantsMap;
    }

    public HashMap<String, String[]> getItemEnchantsMap() {
        return itemEnchantsMap;


    }


    public HashMap<String, String[]> getItemTypesMap() {
        return itemTypesMap;
    }


}
