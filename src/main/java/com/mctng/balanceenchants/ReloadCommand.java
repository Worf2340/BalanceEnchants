package com.mctng.balanceenchants;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Objects;

public class ReloadCommand implements CommandExecutor {

    BalanceEnchants plugin;

    public ReloadCommand(BalanceEnchants plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if (args.length != 1) {
            commandSender.sendMessage(ChatColor.RED + "Invalid Command!");
            return true;
        }
        if (!Objects.equals(args[0], "reload")) {
            commandSender.sendMessage(ChatColor.RED + "Invalid Command!");
            return true;
        }

        this.plugin.createWeightsConfig();
        this.plugin.createLevelsConfig();

        commandSender.sendMessage(ChatColor.GREEN + "Reloaded enchantment weights and levels. 2");

        return true;
    }
}
