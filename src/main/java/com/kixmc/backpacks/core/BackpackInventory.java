package com.kixmc.backpacks.core;

import com.kixmc.backpacks.utils.ChatUtil;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class BackpackInventory implements InventoryHolder {

    private final Inventory inventory;

    public BackpackInventory(SimpleBackpacks plugin, int size, String title) {
        this.inventory = plugin.getServer().createInventory(this, size, ChatUtil.colorize(title));
    }

    @Override
    public @NotNull Inventory getInventory() {
        return this.inventory;
    }

}
