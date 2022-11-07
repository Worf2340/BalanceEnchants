package com.mctng.balanceenchants.listeners;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;


public class OnClick implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        System.out.println(event.getAction());
    }
}
