package com.mctng.balanceenchants;

public class CustomEnchant {
    private String name;
    private int weight;
    private int powerLevel;

    public CustomEnchant(String name, int weight, int powerLevel) {
        this.name = name;
        this.powerLevel = powerLevel;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }


    public int getPowerLevel() {
        return powerLevel;
    }
}
