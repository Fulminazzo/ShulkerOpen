package it.fulminazzo.shulkeropen.Listeners;

import it.fulminazzo.shulkeropen.ShulkerOpen;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
    private final ShulkerOpen plugin;

    public PlayerListener(ShulkerOpen plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.getShulkerPlayersManager().addPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getShulkerPlayersManager().removePlayer(event.getPlayer());
    }
}
