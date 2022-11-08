package com.mctng.balanceenchants;

import java.util.HashMap;

/**
 * Data storage class for enchantments map. Stores the weight and map of power levels to enchantment levels.
 */
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
