package com.kixmc.backpacks.commands;

import com.kixmc.backpacks.core.BackpackItem;
import com.kixmc.backpacks.core.SimpleBackpacks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BackpackCommand implements CommandExecutor, TabCompleter {

    public final String noPermission = ChatColor.RED + "你没有权限使用该命令.";
    public final String usage = ChatColor.RED + "/backpacks <get|reload|give> <id> [玩家名]";

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0 || args.length > 3) {

            sender.sendMessage(usage);

            return true;
        }

            switch (args[0].toLowerCase()) {

                case "get":
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(ChatColor.RED + "该命令需要玩家执行!");
                        return true;
                    }

                    Player p = (Player) sender;

                    if (!p.hasPermission("backpacks.getcommand")) {
                        p.sendMessage(noPermission);
                        return true;
                    }

                    if(args.length < 2) {
                        p.sendMessage(usage);
                        return true;
                    }
                    p.getInventory().addItem(BackpackItem.makeUnopened(args[1]));

                    break;

                case "give":

                    if (!sender.hasPermission("backpacks.givecommand")) {
                        sender.sendMessage(noPermission);
                        return true;
                    }

                    if(args.length != 3) {
                        sender.sendMessage(usage);
                        return true;
                    }

                    if(Bukkit.getPlayer(args[2]) == null) {
                        sender.sendMessage(ChatColor.RED + "Unknown player: " + args[2]);
                        return true;
                    }

                    sender.sendMessage(ChatColor.GREEN + "Gave a backpack to " + args[2] + "!");

                    Bukkit.getPlayer(args[2]).getInventory().addItem(BackpackItem.makeUnopened(args[1]));

                    break;

                case "reload":
                    if (!sender.hasPermission("backpacks.reload")) {
                        sender.sendMessage(noPermission);
                        return true;
                    }

                    SimpleBackpacks.get().reloadConfig();

                    sender.sendMessage(ChatColor.GREEN + "KixsSimpleBackpacks已重载!");

                    break;

                default:
                    sender.sendMessage(usage);
                    break;

            }


        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        switch (args.length) {
            case 1 -> {
                return List.of("get", "give", "reload");
            }
            case 2 -> {
                if(!args[0].equalsIgnoreCase("reload")) {
                    return SimpleBackpacks.get().getConfig().getConfigurationSection("backpack").getKeys(false).stream().toList();
                }
            }
            default -> {
                return null;
            }
        }
        return null;
    }
}