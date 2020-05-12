package com.vergilprime.angelprotect.utils;

import com.vergilprime.angelprotect.datamodels.APEntity;
import com.vergilprime.angelprotect.datamodels.APPlayer;
import com.vergilprime.angelprotect.datamodels.APTown;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Chat Util
 */

public class C {

    public static final String aqua = ChatColor.AQUA + "";
    public static final String black = ChatColor.BLACK + "";
    public static final String blue = ChatColor.BLUE + "";
    public static final String bold = ChatColor.BOLD + "";
    public static final String daqua = ChatColor.DARK_AQUA + "";
    public static final String dblue = ChatColor.DARK_BLUE + "";
    public static final String dgray = ChatColor.DARK_GRAY + "";
    public static final String dgreen = ChatColor.DARK_GREEN + "";
    public static final String dpurple = ChatColor.DARK_PURPLE + "";
    public static final String dred = ChatColor.DARK_RED + "";
    public static final String gold = ChatColor.GOLD + "";
    public static final String gray = ChatColor.GRAY + "";
    public static final String green = ChatColor.GREEN + "";
    public static final String italic = ChatColor.ITALIC + "";
    public static final String lpurple = ChatColor.LIGHT_PURPLE + "";
    public static final String magic = ChatColor.MAGIC + "";
    public static final String red = ChatColor.RED + "";
    public static final String reset = ChatColor.RESET + "";
    public static final String strikethrough = ChatColor.STRIKETHROUGH + "";
    public static final String underline = ChatColor.UNDERLINE + "";
    public static final String white = ChatColor.WHITE + "";
    public static final String yellow = ChatColor.YELLOW + "";

    public static final String prefix = gray + "[" + aqua + "AP" + gray + "] ";
    public static final String error_prefix = gray + "[" + dred + bold + "!" + gray + "] ";
    public static final String error = red;
    public static final String body = gray;
    public static final String player = yellow;
    public static final String town = blue;
    public static final String item = green;
    public static final String headerText = gold;
    public static final String headerLines = gold;
    public static final String key = gold;
    public static final String value = aqua;
    public static final String runes = green;

    public static final String line = center("-", "-");
    public static final String double_line = center("=", "=");
    public static final String hashtag_line = center("#", "#");

    public static final int chatWidth = 320;

    public static String main(String msg) {
        return prefix + body + msg + body;
    }

    public static String error(String msg) {
        return error_prefix + msg + body;
    }

    public static String usage(String usage) {
        return usage(usage, null);
    }

    public static String usage(String usage, String desc) {
        if (desc == null || desc.length() > 0) {
            desc = "";
        } else {
            desc = " - " + C.item(desc);
        }
        return error("Please use " + C.item(usage) + desc);
    }

    public static String usageList(String usage) {
        return usageList(usage, null);
    }

    public static String usageList(String usage, String desc) {
        if (desc != null && desc.length() > 0) {
            desc = " - " + C.item(desc);
        }
        return error(" • " + C.item(usage) + desc);
    }

    public static String player(String name) {
        return player + name + body;
    }

    public static String player(APPlayer player) {
        return player(player.getName());
    }

    public static String player(Player player) {
        return player(player.getName());
    }

    public static String playerPosessive(APPlayer player) {
        return player(player.getName() + "'s");
    }

    public static String playerPosessive(Player player) {
        return player(player.getName() + "'s");
    }

    public static String town(String name) {
        return town + name + body;
    }

    public static String town(APTown town) {
        return town(town.getTownDisplayName());
    }

    public static String townPosessive(APTown town) {
        return town(town.getTownDisplayName() + "'s");
    }

    public static String entity(APEntity entity) {
        return (entity.isTown() ? town : player) + entity.getName() + body;
    }

    public static String entityPosessive(APEntity entity) {
        return (entity.isTown() ? town : player) + entity.getName() + "'s" + body;
    }

    public static String item(String name) {
        return item + name + body;
    }

    public static String header(String header) {
        return center(headerText + " " + header + " ", headerLines + "-").replace("--", "-=");
    }

    public static String color(String color, String msg) {
        return color + msg + body;
    }

    public static String runes(int number) {
        return color(runes, number + " " + (number == 1 ? "rune" : "runes"));
    }

    public static int getCharWidth(char c, boolean bold) {
        int i = bold ? 1 : 0;
        switch (c) {
            case '!':
            case ',':
            case '.':
            case ':':
            case ';':
            case 'i':
            case '|':
            case '¡':
                return 2 + i;
            case '\'':
            case 'l':
            case 'ì':
            case 'í':
                return 3 + i;
            case ' ':
            case 'I':
            case '[':
            case ']':
            case 't':
            case 'ï':
            case '×':
                return 4 + i;
            case '"':
            case '(':
            case ')':
            case '*':
            case '<':
            case '>':
            case 'f':
            case 'k':
            case '{':
            case '}':
                return 5 + i;
            case '@':
            case '~':
            case '®':
                return 7 + i;
            default:
                return 6 + i;
        }
    }

    public static int getWidth(String msg) {
        boolean skipNext = false;
        boolean bold = false;
        int width = 0;
        for (char c : msg.toCharArray()) {
            if (c == '§') {
                skipNext = true;
                continue;
            }
            if (skipNext) {
                if ("1234567890abcdefkr".contains(c + "")) {
                    bold = false;
                } else if (c == 'l') {
                    bold = true;
                }
                skipNext = false;
                continue;
            }
            width += getCharWidth(c, bold);
        }
        if (width <= 1) {
            new RuntimeException("Width less than 1!").printStackTrace();
            System.err.println("msg: '" + msg + "'");
            System.err.println("bold: " + bold);
            System.err.println("width: " + width);
        }
        return Math.max(width, 1); // If we return less than 1, then there has been some error, prevents infinite loops
    }

    public static String center(String msg, String fill) {
        int fillWidth = getWidth(fill);
        String color = "";
        if (fill.startsWith("§") && fill.split("§").length == 2) {
            // fill starts with a color, and contains only that one color
            color = fill.substring(0, 2);
            fill = fill.substring(2);
        }

        int msgWidth = getWidth(msg);
        int pWidth = 0;
        int sWidth = 0;
        String prefix = "";
        String suffix = "";
        while (pWidth + msgWidth + sWidth + fillWidth < chatWidth) {
            prefix += fill;
            pWidth += fillWidth;
            if (pWidth + msgWidth + sWidth + fillWidth < chatWidth) {
                suffix += fill;
                sWidth += fillWidth;
            }
        }
        return color + prefix + msg + color + suffix;
    }

    public static String colorYAML(String yaml) {
        return colorYAML(yaml, key, value);
    }

    public static String colorYAML(String yaml, int indent) {
        return colorYAML(yaml, key, value, indent);
    }

    public static String colorYAML(String yaml, String colorKey, String colorValue) {
        return colorYAML(yaml, colorKey, colorValue, 0);
    }

    public static String colorYAML(String yaml, String colorKey, String colorValue, int indentLevel) {
        String indent = "";
        for (int i = 0; i < indentLevel; i++) {
            indent += "  ";
        }
        List<String> lines = new ArrayList<>();
        for (String line : yaml.split("\n")) {
            if (line.length() > 0) {
                lines.add(indent + colorKey + line.replaceFirst("(- |: )", "$1" + colorValue));
            }
        }
        return String.join("\n", lines);
    }

}
