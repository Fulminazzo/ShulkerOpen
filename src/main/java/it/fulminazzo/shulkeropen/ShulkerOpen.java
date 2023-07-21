package it.fulminazzo.shulkeropen;

import it.fulminazzo.shulkeropen.Enums.Message;
import it.fulminazzo.shulkeropen.Listeners.ShulkerListener;
import it.fulminazzo.shulkeropen.Utils.VersionsUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public final class ShulkerOpen extends JavaPlugin {

    @Override
    public void onEnable() {
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        if (!VersionsUtils.is1_11()) {
            console.sendMessage(Message.INVALID_VERSION.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        } else Bukkit.getPluginManager().registerEvents(new ShulkerListener(this), this);
    }
}
