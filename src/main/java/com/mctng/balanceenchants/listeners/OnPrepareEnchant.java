package com.mctng.balanceenchants.listeners;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;

import java.sql.SQLSyntaxErrorException;
import java.util.Arrays;

public class OnPrepareEnchant implements Listener {

    @EventHandler
    public void onPrepareEnchant(PrepareItemEnchantEvent event) {
        //System.out.println(event.isCancelled());
        System.out.println("Prepared");
        System.out.println(event.getItem().getType());
        for (EnchantmentTarget target: EnchantmentTarget.values()) {
            if (target.includes(event.getItem())) {
                System.out.println(target);
            }
        }

        System.out.println(Enchantment.DAMAGE_ALL.canEnchantItem(event.getItem()));

        //System.out.println(event.getItem());

       // System.out.println(Arrays.toString(event.getExpLevelCostsOffered()));

        event.getExpLevelCostsOffered()[0] = 1;
        event.getExpLevelCostsOffered()[1] = 10;
        event.getExpLevelCostsOffered()[2] = 30;
    }
}