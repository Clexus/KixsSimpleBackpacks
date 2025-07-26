package com.kixmc.backpacks.contents;

import com.kixmc.backpacks.core.SimpleBackpacks;
import com.kixmc.backpacks.utils.BackpackUtils;
import com.kixmc.backpacks.utils.ChatUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;

public class ItemHandler {

    public static void store(ItemStack backpack, List<ItemStack> contents) {

        if (!backpack.hasItemMeta()) return;

        ItemMeta itemMeta = backpack.getItemMeta();
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();

        String id = BackpackUtils.getId(backpack);

        if (!data.has(new NamespacedKey(SimpleBackpacks.get(), "kixs-backpacks"), PersistentDataType.BYTE_ARRAY)) {
            data.set(new NamespacedKey(SimpleBackpacks.get(), "kixs-backpacks"), PersistentDataType.BYTE_ARRAY, new byte[0]);
        }

        if (contents.isEmpty()) {
            data.set(new NamespacedKey(SimpleBackpacks.get(), "kixs-backpacks"), PersistentDataType.BYTE_ARRAY, new byte[0]);

            ArrayList<Component> lore = new ArrayList<>();

            for (String loreLine : SimpleBackpacks.get().getConfig().getStringList("backpack."+id+".lore.empty")) {
                lore.add(ChatUtil.colorize(loreLine.replace("%SLOTS_IN_USE%", "0")).replaceText( TextReplacementConfig.builder().match("%MAX_SLOTS%").replacement(Integer.toString(SimpleBackpacks.get().getConfig().getInt("backpack."+id+".rows") * 9)).build()));
            }

            backpack.setItemMeta(itemMeta);
            backpack.lore(lore);

            return;
        }


            byte @NotNull [] bytes = ItemStack.serializeItemsAsBytes(contents);

            data.set(new NamespacedKey(SimpleBackpacks.get(), "kixs-backpacks"), PersistentDataType.BYTE_ARRAY, bytes);

            ArrayList<Component> contentsPreview = new ArrayList<>();

            int previewSize = SimpleBackpacks.get().getConfig().getInt("backpack."+id+".lore.preview-slots-size");
            int counter = 0;
            for (int i = 0; i < previewSize; i++) {
                try {
                    contents.get(i);
                } catch (Exception ignored) { continue; }

                if (contents.get(i).getType() == Material.AIR) return;

                counter++;
                contentsPreview.add(ChatUtil.colorize(SimpleBackpacks.get().getConfig().getString("backpack."+id+".lore.contents-preview").replace("%ITEM_AMOUNT%", Integer.toString(contents.get(i).getAmount()))).replaceText(TextReplacementConfig.builder()
                                .match("%ITEM_NAME%").replacement(contents.get(i).effectiveName())
                        .build()));

            }

            if (contents.size() > previewSize) {
                for (String loreLine : SimpleBackpacks.get().getConfig().getStringList("backpack."+id+".lore.preview-overflow")) { contentsPreview.add(ChatUtil.colorize(loreLine).replaceText( TextReplacementConfig.builder().match("%REMAINING_CONTENTS_SLOT_COUNT%").replacement(Integer.toString((contents.size() - counter))).build())); }
            }

            ArrayList<Component> lore = new ArrayList<>();

            int index = 0;
            for (String loreLine : SimpleBackpacks.get().getConfig().getStringList("backpack."+id+".lore.storing")) {
                if (loreLine.contains("%CONTENTS_PREVIEW%")) {
                    lore.addAll(index, contentsPreview);
                    continue;
                }
                lore.add(ChatUtil.colorize(loreLine.replace("%SLOTS_IN_USE%", Integer.toString(contents.size())).replace("%MAX_SLOTS%", Integer.toString(SimpleBackpacks.get().getConfig().getInt("backpack."+id+".rows") * 9))));
                index++;
            }
            backpack.setItemMeta(itemMeta);
            backpack.lore(lore);

    }

    public static ArrayList<ItemStack> get(ItemStack backpack) {

        if (!backpack.hasItemMeta()) new ArrayList<ItemStack>();

        ItemMeta itemMeta = backpack.getItemMeta();
        PersistentDataContainer data = itemMeta.getPersistentDataContainer();

        ArrayList<ItemStack> items = new ArrayList<>();

        byte[] encodedItems = data.get(new NamespacedKey(SimpleBackpacks.get(), "kixs-backpacks"), PersistentDataType.BYTE_ARRAY);

        if (encodedItems != null && encodedItems.length > 0) {
            items.addAll(List.of(ItemStack.deserializeItemsFromBytes(encodedItems)));
        }

        return items;
    }

}