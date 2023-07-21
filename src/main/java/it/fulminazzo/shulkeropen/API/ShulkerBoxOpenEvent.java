package it.fulminazzo.shulkeropen.API;

import it.fulminazzo.shulkeropen.Interfaces.IShulkerPlayer;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ShulkerBoxOpenEvent extends Event implements Cancellable {
    private static final HandlerList handlerList = new HandlerList();
    private final IShulkerPlayer player;
    private final ShulkerBox shulkerBox;
    private boolean cancelled;

    public ShulkerBoxOpenEvent(IShulkerPlayer player, ShulkerBox shulkerBox) {
        this.player = player;
        this.shulkerBox = shulkerBox;
        this.cancelled = false;
    }

    public IShulkerPlayer getPlayer() {
        return player;
    }

    public ShulkerBox getShulkerBox() {
        return shulkerBox;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }
}