package it.fulminazzo.shulkeropen.Objects;

import it.fulminazzo.shulkeropen.Interfaces.IShulkerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.UUID;

public class ShulkerPlayer implements IShulkerPlayer {
    private final UUID uuid;
    private ItemStack shulkerBox;
    private Date recentClick;

    public ShulkerPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void toggleShulkerBox() {
        if (this.shulkerBox == null) this.shulkerBox = getItemInMainHand();
        else this.shulkerBox = null;
    }

    @Override
    public boolean checkRecentClicks() {
        if (recentClick != null && new Date().getTime() - recentClick.getTime() <= 500) return true;
        recentClick = new Date();
        return false;
    }

    @Override
    public ItemStack getOpenShulkerBox() {
        return shulkerBox;
    }

    @Override
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }
}