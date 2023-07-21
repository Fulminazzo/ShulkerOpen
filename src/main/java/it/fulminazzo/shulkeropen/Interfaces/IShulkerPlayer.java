package it.fulminazzo.shulkeropen.Interfaces;

import org.bukkit.Bukkit;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public interface IShulkerPlayer {

    default void openShulker() {
        if (checkRecentClicks()) return;
        if (isShulkerOpen()) return;
        Player player = getPlayer();
        if (player == null) return;
        ShulkerBox shulkerBox = getShulkerBox();
        if (shulkerBox == null) return;
        toggleShulkerBox();
        player.openInventory(shulkerBox.getInventory());
    }

    default void saveInventory() {
        if (!isShulkerOpen()) return;
        Player player = getPlayer();
        if (player == null) return;
        Inventory inventory = player.getOpenInventory().getTopInventory();
        if (inventory == null || !inventory.getType().equals(InventoryType.SHULKER_BOX)) return;
        if (!(getOpenShulkerBox().getItemMeta() instanceof BlockStateMeta)) return;
        BlockStateMeta blockStateMeta = (BlockStateMeta) getOpenShulkerBox().getItemMeta();
        ShulkerBox blockState = (ShulkerBox) blockStateMeta.getBlockState();
        blockState.getInventory().setContents(inventory.getContents());
        blockStateMeta.setBlockState(blockState);
        getOpenShulkerBox().setItemMeta(blockStateMeta);
    }

    default ItemStack getItemInMainHand() {
        Player player = getPlayer();
        if (player == null) return null;
        return player.getInventory().getItemInMainHand();
    }

    default ShulkerBox getShulkerBox() {
        ItemStack itemStack = getItemInMainHand();
        if (itemStack == null || !itemStack.getType().name().contains("SHULKER_BOX")) return null;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!(itemMeta instanceof BlockStateMeta)) return null;
        return (ShulkerBox) ((BlockStateMeta) itemMeta).getBlockState();
    }

    default boolean isShulkerOpen() {
        return getOpenShulkerBox() != null;
    }

    default void closeShulker() {
        if (!isShulkerOpen()) return;
        saveInventory();
        toggleShulkerBox();
    }

    ItemStack getOpenShulkerBox();

    void toggleShulkerBox();

    boolean checkRecentClicks();
    
    Player getPlayer();

    UUID getUuid();
}