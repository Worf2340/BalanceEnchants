package com.mctng.customizeenchants;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public class ReloadCommand implements CommandExecutor {

    CustomizeEnchants plugin;

    public ReloadCommand(CustomizeEnchants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (args.length != 1 | !Objects.equals(args[0], "reload")) {
            commandSender.sendMessage(ChatColor.RED + "Invalid Command!");
        }

        this.plugin.createWeightsConfig();
        this.plugin.createLevelsConfig();

        commandSender.sendMessage(ChatColor.GREEN + "Reloaded enchantment weights and levels.");

        return true;
    }
}