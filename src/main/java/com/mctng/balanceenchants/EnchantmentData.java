package com.mctng.balanceenchants;

import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;

public class EnchantmentData {

    private final int weight;
    private final HashMap<Integer, int[]> levels;


    public EnchantmentData(int weight, HashMap<Integer, int[]> levels) {
        this.weight = weight;
        this.levels = levels;
    }

    public HashMap<Integer, int[]> getLevels() {
        return levels;
    }

    public int getWeight() {
        return weight;
    }

}
