package com.kixmc.backpacks.core;

import com.kixmc.backpacks.utils.ChatUtil;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.UUID;

public class BackpackItem {

    public static ItemStack makeUnopened(String id) {

        ItemStack backpack = new ItemStack(Material.valueOf(SimpleBackpacks.get().getConfig().getString("backpack."+id+".material")));
        ItemMeta itemMeta = backpack.getItemMeta();

        itemMeta.getPersistentDataContainer().set(new NamespacedKey(SimpleBackpacks.get(), "kixs-backpacks-new"), PersistentDataType.STRING, "");
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(SimpleBackpacks.get(), "kixs-backpacks-id"), PersistentDataType.STRING, id);
        itemMeta.displayName(ChatUtil.colorize(SimpleBackpacks.get().getConfig().getString("backpack."+id+".name.unopened")));

        ArrayList<Component> lore = new ArrayList<>();

        for (String loreLine : SimpleBackpacks.get().getConfig().getStringList("backpack."+id+".lore.new")) { lore.add(ChatUtil.colorize(loreLine)); }

        itemMeta.lore(lore);
        if(SimpleBackpacks.get().getConfig().getInt("backpack."+id+".custom-model-data",-1) != -1) {
            itemMeta.setCustomModelData(SimpleBackpacks.get().getConfig().getInt("backpack."+id+".custom-model-data"));
        }
        backpack.setItemMeta(itemMeta);
        if(!SimpleBackpacks.get().getConfig().getString("backpack."+id+".item-model", "").isEmpty()) {
            backpack.setData(DataComponentTypes.ITEM_MODEL, Key.key(SimpleBackpacks.get().getConfig().getString("backpack."+id+".item-model")));
        }

        return backpack;
    }

    public static ItemStack makeNew(String id) {

        ItemStack backpack = new ItemStack(Material.valueOf(SimpleBackpacks.get().getConfig().getString("backpack."+id+".material")));
        ItemMeta itemMeta = backpack.getItemMeta();

        itemMeta.getPersistentDataContainer().set(new NamespacedKey(SimpleBackpacks.get(), "kixs-backpacks"), PersistentDataType.BYTE_ARRAY, new byte[0]);
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(SimpleBackpacks.get(), "kixs-backpacks-id"), PersistentDataType.STRING, id);
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(SimpleBackpacks.get(), UUID.randomUUID().toString()), PersistentDataType.STRING, "");

        itemMeta.displayName(ChatUtil.colorize(SimpleBackpacks.get().getConfig().getString("backpack."+id+".name.regular")));

        ArrayList<Component> lore = new ArrayList<>();

        for (String loreLine : SimpleBackpacks.get().getConfig().getStringList("backpack."+id+".lore.empty")) {
            lore.add(ChatUtil.colorize(loreLine.replace("%SLOTS_IN_USE%", "0")).replaceText( TextReplacementConfig.builder().match("%MAX_SLOTS%").replacement(Integer.toString(SimpleBackpacks.get().getConfig().getInt("backpack."+id+".rows") * 9)).build()));
        }

        itemMeta.lore(lore);
        if(SimpleBackpacks.get().getConfig().getInt("backpack."+id+".custom-model-data",-1) != -1) {
            itemMeta.setCustomModelData(SimpleBackpacks.get().getConfig().getInt("backpack."+id+".custom-model-data"));
        }
        backpack.setItemMeta(itemMeta);
        if(!SimpleBackpacks.get().getConfig().getString("backpack."+id+".item-model", "").isEmpty()) {
            backpack.setData(DataComponentTypes.ITEM_MODEL, Key.key(SimpleBackpacks.get().getConfig().getString("backpack."+id+".item-model")));
        }

        return backpack;
    }

}
