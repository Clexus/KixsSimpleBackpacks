package com.kixmc.backpacks.listeners;

import com.kixmc.backpacks.core.BackpackInventory;
import com.kixmc.backpacks.core.SimpleBackpacks;
import com.kixmc.backpacks.utils.BackpackUtils;
import com.kixmc.backpacks.utils.ChatUtil;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

public class InventoryClick implements Listener {
    FileConfiguration config = SimpleBackpacks.get().getConfig();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(InventoryClickEvent e) {
        for(String id : config.getConfigurationSection("backpack").getKeys(false)) {
            if(e.getInventory().getHolder(false) instanceof BackpackInventory) {
                if(e.getClick() == ClickType.NUMBER_KEY) e.setCancelled(true);
                if(e.getCurrentItem() == null) return;
                if(e.getCurrentItem().getType().toString().contains("SHULKER_BOX") && !config.getBoolean("backpack."+id+".allow-shulker-boxes-in-backpacks")) e.setCancelled(true);
            }

            if (BackpackUtils.isBackpack(e.getCurrentItem())) {
                if(e.getInventory().getHolder(false) instanceof BackpackInventory) e.setCancelled(true);
                if(e.getInventory().getType() == InventoryType.SHULKER_BOX) {
                    if(e.getClick() == ClickType.NUMBER_KEY) e.setCancelled(true);
                    if(e.getClickedInventory().getType() == InventoryType.SHULKER_BOX) return; // allow taking backpacks out of shulker boxes in case of settings change
                    if (!config.getBoolean("backpack."+id+".allow-backpacks-in-shulker-boxes")) e.setCancelled(true);
                }
            }

            if (!(e.getWhoClicked() instanceof Player)) return;

            if (e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR || e.getInventory().getType() != InventoryType.ANVIL) return;

            if (e.getSlotType() == InventoryType.SlotType.RESULT && BackpackUtils.isUnopenedBackpack(e.getCurrentItem())) e.setCancelled(true);
        }
    }

}