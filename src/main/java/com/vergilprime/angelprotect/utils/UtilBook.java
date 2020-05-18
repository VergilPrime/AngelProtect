package com.vergilprime.angelprotect.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.List;

public class UtilBook {

    public static class BookBuilder {
        ItemStack item;
        BookMeta meta;
        List<BaseComponent[]> pages = new ArrayList<>();
        ComponentBuilder last = new ComponentBuilder();

        public BookBuilder() {
            item = new ItemStack(Material.WRITTEN_BOOK);
            meta = (BookMeta) item.getItemMeta();
            meta.setAuthor("");
            meta.setTitle("");
        }

        public BookBuilder newPage() {
            pages.add(last.create());
            last = new ComponentBuilder();
            last.color(ChatColor.GRAY);
            return this;
        }

        public BookBuilder add(String msg) {
            last.append(TextComponent.fromLegacyText(C.dgray + msg), ComponentBuilder.FormatRetention.NONE);
            return this;
        }

        public BookBuilder color(ChatColor color) {
            last.color(color);
            return this;
        }

        public BookBuilder underline() {
            last.underlined(true);
            return this;
        }

        public BookBuilder bold() {
            last.bold(true);
            return this;
        }

        public BookBuilder strikethrough() {
            last.strikethrough(true);
            return this;
        }

        public BookBuilder obfuscated() {
            last.obfuscated(true);
            return this;
        }

        public BookBuilder link(String link) {
            last.event(new ClickEvent(ClickEvent.Action.OPEN_URL, link));
            return this;
        }

        public BookBuilder hover(String text) {
            last.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(C.gray + text)));
            return this;
        }

        public BookBuilder gotoPage(int page) {
            last.event(new ClickEvent(ClickEvent.Action.CHANGE_PAGE, "" + page));
            return this;
        }

        public BookBuilder runCommand(String cmd) {
            last.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
            return this;
        }

        public BookBuilder suggestCommand(String cmd) {
            last.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, cmd));
            return this;
        }

        public BookBuilder addLink(String text, String link) {
            add(text);
            color(ChatColor.BLUE);
            underline();
            hover("Open " + link);
            link(link);
            return this;
        }

        public BookBuilder addRunCommand(String text, String cmd) {
            return addRunCommand(text, cmd, null);
        }

        public BookBuilder addRunCommand(String text, String cmd, String hover) {
            add("[" + text + "]");
            if (hover != null && hover.length() > 0) {
                hover = "\n" + hover;
            } else {
                hover = "";
            }
            hover(cmd + hover);
            runCommand(cmd);
            return this;
        }

        public BookBuilder addSuggestCommand(String text, String cmd) {
            return addSuggestCommand(text, cmd, null);
        }

        public BookBuilder addSuggestCommand(String text, String cmd, String hover) {
            add("[" + text + "]");
            if (hover != null && hover.length() > 0) {
                hover = "\n" + hover;
            } else {
                hover = "";
            }
            hover(cmd + hover);
            suggestCommand(cmd);
            return this;
        }

        public BookBuilder addGoto(String text, int page) {
            add(C.cGoto + text);
            hover("Go to page " + page);
            gotoPage(page);
            return this;
        }

        public BookBuilder newline() {
            return add("\n");
        }

        public ItemStack build() {
            pages.add(last.create());
            meta.spigot().setPages(pages);
            pages.remove(pages.size() - 1);
            item.setItemMeta(meta);
            return item.clone();
        }

    }

    public static void sendBook(Player player, List<BaseComponent[]> pages) {
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) item.getItemMeta();
        meta.spigot().setPages(pages);
        meta.setAuthor("");
        meta.setTitle("");
        item.setItemMeta(meta);
        player.openBook(item);
    }

}
