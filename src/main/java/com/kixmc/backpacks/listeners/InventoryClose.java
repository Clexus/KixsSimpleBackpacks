package com.kixmc.backpacks.listeners;

import com.kixmc.backpacks.contents.ItemHandler;
import com.kixmc.backpacks.core.BackpackInventory;
import com.kixmc.backpacks.core.SimpleBackpacks;
import com.kixmc.backpacks.utils.BackpackUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class InventoryClose implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!(BackpackUtils.isBackpack(e.getPlayer().getInventory().getItemInMainHand()) && e.getInventory().getHolder(false) instanceof BackpackInventory))
            return;

        ItemHandler.store(e.getPlayer().getInventory().getItemInMainHand(), e.getInventory().getContents());
    }

}
