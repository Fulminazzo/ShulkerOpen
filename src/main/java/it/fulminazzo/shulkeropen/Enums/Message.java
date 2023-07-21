package it.fulminazzo.shulkeropen.Enums;

import net.md_5.bungee.api.ChatColor;

public enum Message {
    INVALID_VERSION("&cInvalid version! This plugin only works in 1.11 and above.");

    private final String message;

    Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
