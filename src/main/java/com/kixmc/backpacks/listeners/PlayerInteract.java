package com.kixmc.backpacks.listeners;

import com.kixmc.backpacks.contents.ItemHandler;
import com.kixmc.backpacks.core.BackpackInventory;
import com.kixmc.backpacks.core.SimpleBackpacks;
import com.kixmc.backpacks.core.BackpackItem;
import com.kixmc.backpacks.utils.BackpackUtils;
import com.kixmc.backpacks.utils.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class PlayerInteract implements Listener {
    FileConfiguration config = SimpleBackpacks.get().getConfig();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent e) {

        if (!e.hasItem()) return;

        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            ItemStack is = e.getPlayer().getInventory().getItemInMainHand();

            
            String id = BackpackUtils.getId(is);
            if (BackpackUtils.isUnopenedBackpack(is)) {

                is.setAmount(is.getAmount() - 1);

                e.getPlayer().getInventory().addItem(BackpackItem.makeNew(id));

                String unboxMsg = config.getString("backpack."+id+".messages.unboxed");
                if (!unboxMsg.isEmpty()) {
                    e.getPlayer().sendMessage(ChatUtil.colorize(config.getString("backpack."+id+".messages.unboxed")));
                }

                return;
            }

            if (BackpackUtils.isBackpack(is)) {

                ItemMeta im = is.getItemMeta();

                is.setType(Material.valueOf(config.getString("backpack."+id+".material")));

                if (BackpackUtils.hasKey(is, "kixs-backpacks-custom-name", PersistentDataType.STRING)) {
                    im.displayName(ChatUtil.colorize(config.getString("backpack."+id+".name.renamed").replace("%CUSTOM_NAME%", im.getPersistentDataContainer().get(new NamespacedKey(SimpleBackpacks.get(), "kixs-backpacks-custom-name"), PersistentDataType.STRING))));
                }

                is.setItemMeta(im);

                ArrayList<ItemStack> contents = ItemHandler.get(is);

                Inventory backpack = new BackpackInventory(SimpleBackpacks.get(), config.getInt("backpack."+id+".rows") * 9, config.getString("backpack."+id+".gui-title")).getInventory();

                ArrayList<ItemStack> itemOverflow = new ArrayList<>();

                boolean canHoldShulkerBoxes = config.getBoolean("backpack."+id+".allow-shulker-boxes-in-backpacks");

                for (ItemStack itemStack : contents) {
                    if(!canHoldShulkerBoxes && itemStack.getType().toString().contains("SHULKER_BOX")) {
                        itemOverflow.add(itemStack);
                        continue;
                    }
                    if (backpack.addItem(itemStack).isEmpty()) continue;
                    itemOverflow.add(itemStack);
                }

                for (ItemStack itemStack : itemOverflow) {
                    e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), itemStack);
                }

                e.getPlayer().openInventory(backpack);

            }

        }
    }

}