package com.mctng.customizeenchants.listeners;

import com.mctng.customizeenchants.CustomizeEnchants;
import com.mctng.customizeenchants.CustomEnchant;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

import java.util.*;

public class OnEnchant implements Listener {
    private CustomizeEnchants plugin;

    public OnEnchant(CustomizeEnchants plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event){
        double modifiedLevel = Math.round(event.getExpLevelCost() * generateRandomBonus());

        if (modifiedLevel < 1) {
            modifiedLevel = 1;
        }


        ArrayList<String> itemTypes = selectItemTypes(event.getItem().getType());
        ArrayList<CustomEnchant> selectedEnchants = selectEnchants(modifiedLevel, itemTypes);

        System.out.println(event.getItem().getType());

        event.getEnchantsToAdd().clear();

        for (CustomEnchant enchant: selectedEnchants) {
            System.out.println(enchant.getName() + " " + enchant.getPowerLevel());
            event.getEnchantsToAdd().put(Enchantment.getByName(enchant.getName()), enchant.getPowerLevel());
        }

    }

    private double generateRandomBonus() {
        Random rand = new Random();
        double r1 = .85 + (1.15 - .85) * rand.nextDouble();
        double r2 = .85 + (1.15 - .85) * rand.nextDouble();

        return 1 + (rand.nextDouble() + rand.nextDouble() - 1) * .15;
    }

    // Calculate valid enchants based on item type and enchantment level
    private ArrayList<CustomEnchant> calculateValidEnchants(ArrayList<String> applicableTypes, double modifiedLevel) {
        ArrayList<CustomEnchant> applicableEnchants = new ArrayList<>();

        for (String type : applicableTypes) {
            String[] applicableEnchantsList = plugin.getItemEnchantsMap().get(type);
            for (String applicableEnchantString : applicableEnchantsList) {
                HashMap<Integer, int[]> levels = new HashMap<>();
                int weight = plugin.getWeightsConfig().getInt(applicableEnchantString);
                int selectedPowerLevel = 0;

                for (Map<?, ?> enchantMap : plugin.getLevelsConfig().getMapList(applicableEnchantString)) {
                    int powerLevel = (int) enchantMap.keySet().toArray()[0];
                    HashMap<String, Integer> levelsMap = (HashMap<String, Integer>) enchantMap.get(powerLevel);
                    int minLevel = levelsMap.get("minLevel");
                    int maxLevel = levelsMap.get("maxLevel");

                    if (modifiedLevel >= minLevel && modifiedLevel <= maxLevel) {
                        selectedPowerLevel = powerLevel;
                    }

                }

                if (selectedPowerLevel > 0) {
                    applicableEnchants.add(new CustomEnchant(applicableEnchantString, weight, selectedPowerLevel));
                }
            }
        }

        return applicableEnchants;
    }

    // Calculate which enchants conflict with the given list of enchants
    private ArrayList<String> calculateConflictingEnchants(ArrayList<CustomEnchant> enchants) {
        ArrayList<String> conflictingEnchants = new ArrayList<>();
        for (CustomEnchant enchant : enchants) {
            for (String conflictingEnchant : this.plugin.getConflictingEnchantsMap().get(enchant.getName())) {
                if (!conflictingEnchants.contains(conflictingEnchant)) {
                    conflictingEnchants.add(conflictingEnchant);
                }
            }
        }

        return conflictingEnchants;
    }

    // Select multiple enchants via selectEnchant method and probability based on modified enchantment level method
    private ArrayList<CustomEnchant> selectEnchants(double modifiedLevel, ArrayList<String> itemTypes) {
        ArrayList<CustomEnchant> selectedEnchants = new ArrayList<>();

        ArrayList<CustomEnchant> applicableEnchants = calculateValidEnchants(itemTypes, modifiedLevel);

        // Select first enchant
        selectedEnchants.add(selectEnchant(applicableEnchants));

        // Select further enchants with decreasing probability
        while (true) {
            double p = (modifiedLevel +1)/50;
            Random rand = new Random();

            if (rand.nextDouble() <= p) {

                // Remove conflicting enchants
                ArrayList<String> conflictingEnchants = calculateConflictingEnchants(selectedEnchants);
                applicableEnchants.removeIf(applicableEnchant -> conflictingEnchants.contains(applicableEnchant.getName()));

                if (applicableEnchants.size() == 0) {
                    break;
                }
                selectedEnchants.add(selectEnchant(applicableEnchants));
                modifiedLevel = modifiedLevel / 2;
            }
            else {
                break;
            }
        }

        return selectedEnchants;

    }

    // Select single enchant randomly based on weights
    private CustomEnchant selectEnchant(ArrayList<CustomEnchant> applicableEnchants) {
        int sumWeight = 0;
        for (CustomEnchant enchant : applicableEnchants) {
            sumWeight += enchant.getWeight();
        }

        Random rand = new Random();
        int w = rand.nextInt(sumWeight);

        CustomEnchant selectedEnchant = null;

        for (CustomEnchant enchant : applicableEnchants) {
            w -= enchant.getWeight();
            if (w < 0) {
                selectedEnchant = enchant;
                break;
            }
        }

        return selectedEnchant;
    }

    private ArrayList<String> selectItemTypes(Material item) {

        String itemName = item.toString();
        ArrayList<String> itemTypes = new ArrayList<>();

        itemTypes.add("ALL");

        for (String enchantType : this.plugin.getItemTypesMap().keySet()) {
            for (String typeIdentifier : this.plugin.getItemTypesMap().get(enchantType)) {
                if (itemName.contains(typeIdentifier)) {
                    itemTypes.add(enchantType);
                }
            }
        }

        return itemTypes;
    }

}
