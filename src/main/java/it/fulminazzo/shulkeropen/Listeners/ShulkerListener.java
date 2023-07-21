package it.fulminazzo.shulkeropen.Listeners;

import it.fulminazzo.shulkeropen.API.ShulkerBoxOpenEvent;
import it.fulminazzo.shulkeropen.Interfaces.IShulkerPlayer;
import it.fulminazzo.shulkeropen.Interfaces.IShulkerPlayersManager;
import it.fulminazzo.shulkeropen.Interfaces.IShulkerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class ShulkerListener implements Listener {
    private final IShulkerPlugin plugin;

    public ShulkerListener(IShulkerPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR)) return;
        getPlayer(event.getPlayer()).ifPresent(p -> {
            ShulkerBoxOpenEvent shulkerBoxOpenEvent = new ShulkerBoxOpenEvent(p, p.getShulkerBox());
            Bukkit.getPluginManager().callEvent(shulkerBoxOpenEvent);
            if (shulkerBoxOpenEvent.isCancelled()) return;
            p.openShulker();
        });
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent event) {
        getPlayer((Player) event.getWhoClicked()).ifPresent(p -> {
            if (!p.isShulkerOpen()) return;
            ItemStack shulkerBox = p.getOpenShulkerBox();
            if ((event.getCursor() != null && event.getCursor().equals(shulkerBox)) ||
                    (event.getCurrentItem() != null && event.getCurrentItem().equals(shulkerBox)))
                event.setCancelled(true);
            p.saveInventory();
        });
    }

    @EventHandler
    public void onPlayerClick(InventoryDragEvent event) {
        getPlayer((Player) event.getWhoClicked()).ifPresent(p -> {
            if (!p.isShulkerOpen()) return;
            ItemStack shulkerBox = p.getOpenShulkerBox();
            if (event.getCursor() != null && event.getCursor().equals(shulkerBox)) event.setCancelled(true);
            p.saveInventory();
        });
    }

    @EventHandler
    public void onPlayerPlace(BlockPlaceEvent event) {
        getPlayer(event.getPlayer()).ifPresent(p -> {
            if (p.isShulkerOpen()) event.setCancelled(true);
        });
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        getPlayer(event.getPlayer()).ifPresent(p -> {
            if (p.isShulkerOpen()) event.setCancelled(true);
        });
    }

    @EventHandler
    public void onPlayerClose(InventoryCloseEvent event) {
        getPlayer((Player) event.getPlayer()).ifPresent(IShulkerPlayer::closeShulker);
    }

    private Optional<IShulkerPlayer> getPlayer(Player player) {
        return Optional.ofNullable(((IShulkerPlayersManager) plugin.getShulkerPlayersManager()).getPlayer(player.getUniqueId()));
    }
}
