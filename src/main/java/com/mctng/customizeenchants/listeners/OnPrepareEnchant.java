package com.mctng.customizeenchants.listeners;

import com.mctng.customizeenchants.CustomizeEnchants;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;

import java.util.Arrays;

public class OnPrepareEnchant implements Listener {


    public OnPrepareEnchant(CustomizeEnchants customizeEnchants) {

    }

    @EventHandler
    public void onPrepareEnchant(PrepareItemEnchantEvent event){
        //System.out.println(Arrays.toString(event.getExpLevelCostsOffered()));
    }
}
