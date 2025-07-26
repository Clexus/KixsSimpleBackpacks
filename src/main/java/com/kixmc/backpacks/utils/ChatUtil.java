package com.kixmc.backpacks.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtil {

    public final static char COLOR_CHAR = ChatColor.COLOR_CHAR;

    // Colorizes messages with preset colorcodes (&) and if using 1.16+, applies hex values via "&#hexvalue"
    public static Component colorize(String text) {
        String[][] formatMap = {
                {"0", "black"}, {"1", "dark_blue"}, {"2", "dark_green"}, {"3", "dark_aqua"},
                {"4", "dark_red"}, {"5", "dark_purple"}, {"6", "gold"}, {"7", "gray"},
                {"8", "dark_gray"}, {"9", "blue"}, {"a", "green"}, {"b", "aqua"},
                {"c", "red"}, {"d", "light_purple"}, {"e", "yellow"}, {"f", "white"},
                {"k", "obf"}, {"l", "b"}, {"m", "st"},
                {"n", "u"}, {"o", "i"}, {"r", "reset"}
        };

        Pattern pattern = Pattern.compile("[§&]x([§&][0-9a-fA-F]){6}");
        Matcher matcher = pattern.matcher(text);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String hex = matcher.group().replaceAll("[§&]x|[§&]", "");
            matcher.appendReplacement(result, "<#" + hex + ">");
        }
        matcher.appendTail(result);
        text = result.toString();

        for (String[] entry : formatMap) {
            text = text.replaceAll("[§&]" + entry[0], "<" + entry[1] + ">");
        }

        return MiniMessage.miniMessage().deserialize(text).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE);
    }

    public static String translateHexColorCodes(String startTag, String endTag, String message) {

        final Pattern hexPattern = Pattern.compile(startTag + "([A-Fa-f0-9]{6})" + endTag);
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);

        while (matcher.find()) {

            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x" + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1) + COLOR_CHAR
                    + group.charAt(2) + COLOR_CHAR + group.charAt(3) + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5));

        }

        return matcher.appendTail(buffer).toString();
    }

}