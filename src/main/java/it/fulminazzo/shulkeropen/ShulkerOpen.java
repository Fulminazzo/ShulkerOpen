package it.fulminazzo.shulkeropen;

import it.fulminazzo.shulkeropen.Enums.Message;
import it.fulminazzo.shulkeropen.Interfaces.IShulkerPlugin;
import it.fulminazzo.shulkeropen.Listeners.PlayerListener;
import it.fulminazzo.shulkeropen.Listeners.ShulkerListener;
import it.fulminazzo.shulkeropen.Managers.ShulkerPlayersManager;
import it.fulminazzo.shulkeropen.Utils.VersionsUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("unchecked")
public final class ShulkerOpen extends JavaPlugin implements IShulkerPlugin {
    private ShulkerPlayersManager shulkerPlayersManager;

    @Override
    public void onEnable() {
        ConsoleCommandSender console = Bukkit.getConsoleSender();
        if (!VersionsUtils.is1_11()) {
            console.sendMessage(Message.INVALID_VERSION.getMessage());
            Bukkit.getPluginManager().disablePlugin(this);
        } else {
            shulkerPlayersManager = new ShulkerPlayersManager();
            Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
            Bukkit.getPluginManager().registerEvents(new ShulkerListener(this), this);
        }
    }

    @Override
    public ShulkerPlayersManager getShulkerPlayersManager() {
        return shulkerPlayersManager;
    }
}
