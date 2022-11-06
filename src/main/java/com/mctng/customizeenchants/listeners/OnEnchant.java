package com.mctng.customizeenchants.listeners;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import java.util.Random;

import java.util.HashMap;
import java.util.Map;

import static org.bukkit.enchantments.Enchantment.*;

public class OnEnchant implements Listener {
    @EventHandler
    public void onEnchant(EnchantItemEvent event){
        Random rand = new Random();
        double r1 = .85 + (1.15 - .85) * rand.nextDouble();
        double r2 = .85 + (1.15 - .85) * rand.nextDouble();
        double randomBonus = 1 + (rand.nextDouble() + rand.nextDouble() - 1) * .15;

        System.out.println(randomBonus);
    }
}
