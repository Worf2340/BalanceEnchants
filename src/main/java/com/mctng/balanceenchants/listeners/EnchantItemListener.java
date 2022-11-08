package com.mctng.balanceenchants.listeners;

import com.mctng.balanceenchants.BalanceEnchants;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class EnchantItemListener implements Listener {
    private final BalanceEnchants plugin;

    public EnchantItemListener(BalanceEnchants plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEnchantItem(EnchantItemEvent event){
        event.getEnchantsToAdd().clear();

        double modifiedLevel = Math.round(event.getExpLevelCost() * generateRandomBonus());

        if (modifiedLevel < 1) {
            modifiedLevel = 1;
        }

        ArrayList<Enchantment> selectedEnchantments = selectEnchantments(modifiedLevel, event.getItem());
        HashMap<Enchantment, Integer> enchantmentPowerLevels =
                calculateEnchantmentPowerLevels(modifiedLevel);


        for (Enchantment enchantment : selectedEnchantments) {
            event.getEnchantsToAdd().put(enchantment, enchantmentPowerLevels.get(enchantment));
        }

    }

    /**
     * Returns random double between 0.85 and 1.15 to be used as the random bonus in enchantment calculations
     * @return a random double between 0.85 and 1.15
     */
    private double generateRandomBonus() {
        Random rand = new Random();

        return 1 + (rand.nextDouble() + rand.nextDouble() - 1) * .15;
    }


    /**
     * Returns the possible enchantments for a given item. This takes into account the type of item and uses the
     * enchantment levels in levels.yml.
     * @param item
     * @param modifiedLevel
     * @return a list of possible enchantments
     */
    private ArrayList<Enchantment> getPossibleEnchantments(ItemStack item, double modifiedLevel) {

        ArrayList<Enchantment> possibleEnchantments = new ArrayList<>();
        HashMap<Enchantment, Integer> enchantmentPowerLevels =
                calculateEnchantmentPowerLevels(modifiedLevel);


        for (Enchantment enchantment : Enchantment.values()) {
            if (item.getType() == Material.BOOK || enchantment.canEnchantItem(item)) {
                if (enchantmentPowerLevels.get(enchantment) > 0) {
                    possibleEnchantments.add(enchantment);
                }
            }
        }


        return possibleEnchantments;

    }

    /**
     * Uses the levels.yml file to return the applicable power level for a given enchantment and enchantment level.
     * @param enchantment
     * @param modifiedLevel
     * @return the calculated power level
     */
    private int calculateEnchantmentPowerLevel(Enchantment enchantment, double modifiedLevel) {
        int calculatedPowerLevel = 0;

        HashMap<Integer, int[]> levels = plugin.getEnchantmentDataHashMap().get(enchantment).getLevels();

        for (int powerLevel : levels.keySet()) {
            int minLevel = levels.get(powerLevel)[0];
            int maxLevel = levels.get(powerLevel)[1];

            if (modifiedLevel >= minLevel && modifiedLevel <= maxLevel) {
                calculatedPowerLevel = powerLevel;
            }
         }

        return calculatedPowerLevel;
    }

    /**
     * Gets applicable power levels for all enchantments, based on levels.yml file
     * @param modifiedLevel
     * @return a map of enchantments to power levels
     */
    private HashMap<Enchantment, Integer> calculateEnchantmentPowerLevels(double modifiedLevel) {
        HashMap<Enchantment, Integer> enchantmentPowerLevels = new HashMap<>();

        for (Enchantment enchantment : Enchantment.values()) {
            int powerLevel = calculateEnchantmentPowerLevel(enchantment, modifiedLevel);

            enchantmentPowerLevels.put(enchantment, powerLevel);

        }

        return enchantmentPowerLevels;
    }

    /**
     * Returns list of conflicting enchantments for a given list of enchantments
     * @param enchantments
     * @return
     */
    private ArrayList<Enchantment> calculateConflictingEnchantments(ArrayList<Enchantment> enchantments) {
        ArrayList<Enchantment> conflictingEnchantments = new ArrayList<>();

        for (Enchantment possibleConflictingEnchantment : Enchantment.values()) {
            for (Enchantment enchantment : enchantments) {
                if (enchantment.conflictsWith(possibleConflictingEnchantment) &&
                        !conflictingEnchantments.contains(possibleConflictingEnchantment)) {
                    conflictingEnchantments.add(possibleConflictingEnchantment);
                }
            }
        }

        return conflictingEnchantments;
    }

    // Select multiple enchants via selectEnchant method and probability based on modified enchantment level method

    /**
     * Returns a list of selected enchantments via the selectEnchantment() method. At least one enchantment is always
     * selected. The subsequent enchantments are selected with decreasing probability.
     * @param modifiedLevel
     * @param item
     * @return
     */
    private ArrayList<Enchantment> selectEnchantments(double modifiedLevel, ItemStack item) {
        ArrayList<Enchantment> selectedEnchantments = new ArrayList<>();
        ArrayList<Enchantment> possibleEnchantments = getPossibleEnchantments(item, modifiedLevel);

        // Always Select first enchant
        Enchantment selectedEnchantment = selectEnchantment(possibleEnchantments);

        if (selectedEnchantment != null) {
            selectedEnchantments.add(selectedEnchantment);
        }

        // Books can only get one enchantment
        if (item.getType() == Material.BOOK) {
            return selectedEnchantments;
        }

        // Select further enchants with decreasing probability
        while (true) {

            double p = (modifiedLevel +1)/50;
            Random rand = new Random();

            if (rand.nextDouble() <= p) {

                // Remove conflicting enchants

                Iterator<Enchantment> possibleEnchantsIter = possibleEnchantments.iterator();
                ArrayList<Enchantment> conflictingEnchantments = calculateConflictingEnchantments(selectedEnchantments);

                while (possibleEnchantsIter.hasNext()) {
                    Enchantment possibleEnchantment = possibleEnchantsIter.next();
                    if (conflictingEnchantments.contains(possibleEnchantment)) {
                        possibleEnchantsIter.remove();
                    }
                }

                if (possibleEnchantments.size() == 0) {
                    break;
                }

                selectedEnchantment = selectEnchantment(possibleEnchantments);

                if (selectedEnchantment != null) {
                    selectedEnchantments.add(selectedEnchantment);
                }

                modifiedLevel = modifiedLevel / 2;
            }
            else {
                break;
            }
        }

        return selectedEnchantments;

    }

    /**
     * Returns a single randomly selected enchantment, weighted with weights from weights.yml file.
     * @param possibleEnchantments a list of possible enchantments to select from
     * @return
     */
    private Enchantment selectEnchantment(ArrayList<Enchantment> possibleEnchantments) {
        Enchantment selectedEnchantment;

        int sumWeight = 0;

        for (Enchantment enchantment : possibleEnchantments) {
            sumWeight += this.plugin.getEnchantmentDataHashMap().get(enchantment).getWeight();
        }

        // TODO: SEED
        Random rand = new Random();

        if (sumWeight == 0) {
            return null;
        }

        int w = rand.nextInt(sumWeight);

        for (Enchantment enchantment : possibleEnchantments) {
            w -= this.plugin.getEnchantmentDataHashMap().get(enchantment).getWeight();

            if (w < 0) {
                selectedEnchantment = enchantment;
                return selectedEnchantment;
            }
        }

        return null;
    }

}
