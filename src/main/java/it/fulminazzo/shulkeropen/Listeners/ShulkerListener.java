package it.fulminazzo.shulkeropen.Listeners;

import it.fulminazzo.shulkeropen.API.ShulkerBoxOpenEvent;
import org.bukkit.Bukkit;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class ShulkerListener implements Listener {
    private final JavaPlugin plugin;
    private final List<UUID> openShulkers;
    private final HashMap<UUID, Date> recentClicks;

    public ShulkerListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.openShulkers = new ArrayList<>();
        this.recentClicks = new HashMap<>();
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        if (!event.getAction().equals(Action.RIGHT_CLICK_AIR)) return;
        Player player = event.getPlayer();
        getShulkerBox(player).ifPresent(shulkerBox -> {
            Date recentClick = recentClicks.getOrDefault(player.getUniqueId(), null);
            if (recentClick != null && (new Date().getTime() - recentClick.getTime()) / 1000 * 20 <= 10) return;
            restorePlayer(player);
            recentClicks.put(player.getUniqueId(), new Date());
            ShulkerBoxOpenEvent shulkerBoxOpenEvent = new ShulkerBoxOpenEvent(player, shulkerBox);
            Bukkit.getPluginManager().callEvent(shulkerBoxOpenEvent);
            if (shulkerBoxOpenEvent.isCancelled()) return;
            this.openShulkers.add(player.getUniqueId());
            player.openInventory(shulkerBox.getInventory());
        });
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!this.openShulkers.contains(player.getUniqueId())) return;
        saveInventory(player);
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> saveInventory(player));
        if (event.getAction().equals(InventoryAction.HOTBAR_SWAP)) {
            event.setCancelled(true);
            return;
        }
        handleInventoryEvent(event, event.getCurrentItem(), event.getCursor());
        for (Inventory inventory : Arrays.asList(player.getInventory(), event.getClickedInventory(), event.getInventory())) {
            if (inventory == null) continue;
            if (event.isCancelled()) return;
            try {handleInventoryEvent(event, inventory.getItem(event.getSlot()));}
            catch (IndexOutOfBoundsException ignored) {}
            if (event.isCancelled()) return;
            try {handleInventoryEvent(event, inventory.getItem(event.getRawSlot()));}
            catch (IndexOutOfBoundsException ignored) {}
        }
    }

    @EventHandler
    public void onPlayerClick(InventoryDragEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!this.openShulkers.contains(player.getUniqueId())) return;
        saveInventory(player);
        handleInventoryEvent(event, event.getOldCursor(), event.getCursor());
    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event) {
        ItemStack itemStack = event.getItemDrop().getItemStack();
        Player player = event.getPlayer();
        if (itemStack.getType().name().contains("SHULKER_BOX")) {
            saveInventory(player);
            player.closeInventory();
        }
    }

    @EventHandler
    public void onPlayerChangeSlot(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (this.openShulkers.contains(player.getUniqueId())) {
            saveInventory(player);
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory() == null || !event.getInventory().getType().equals(InventoryType.SHULKER_BOX)) return;
        restorePlayer((Player) event.getPlayer());
    }

    @EventHandler
    public void onPlayerPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (this.openShulkers.contains(player.getUniqueId())) {
            saveInventory(player);
            event.setCancelled(true);
        }
    }

    private void handleInventoryEvent(Cancellable event, ItemStack... itemStacks) {
        for (ItemStack itemStack : itemStacks) {
            if (itemStack == null || !itemStack.getType().name().contains("SHULKER_BOX")) continue;
            event.setCancelled(true);
            return;
        }
    }

    private void restorePlayer(Player player) {
        UUID uuid = player.getUniqueId();
        if (!openShulkers.contains(uuid)) return;
        openShulkers.remove(uuid);
        saveInventory(player);
    }

    private void saveInventory(Player player) {
        Inventory inventory = player.getOpenInventory().getTopInventory();
        if (inventory == null) return;
        getShulkerBox(player).ifPresent(shulkerBox -> {
            shulkerBox.getInventory().setContents(inventory.getContents());
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            BlockStateMeta itemMeta = (BlockStateMeta) itemStack.getItemMeta();
            itemMeta.setBlockState(shulkerBox);
            itemStack.setItemMeta(itemMeta);
        });
    }

    private Optional<ShulkerBox> getShulkerBox(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack == null || !itemStack.getType().name().contains("SHULKER_BOX")) return Optional.empty();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof BlockStateMeta)) return Optional.empty();
        return Optional.of((ShulkerBox) ((BlockStateMeta) itemMeta).getBlockState());
    }
}
