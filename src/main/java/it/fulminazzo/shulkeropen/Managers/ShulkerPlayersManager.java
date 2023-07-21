package it.fulminazzo.shulkeropen.Managers;

import it.fulminazzo.shulkeropen.Interfaces.IShulkerPlayer;
import it.fulminazzo.shulkeropen.Interfaces.IShulkerPlayersManager;
import it.fulminazzo.shulkeropen.Objects.ShulkerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ShulkerPlayersManager implements IShulkerPlayersManager {
    private final List<ShulkerPlayer> players;

    public ShulkerPlayersManager() {
        this.players = Bukkit.getOnlinePlayers().stream().map(p -> new ShulkerPlayer(p.getUniqueId())).collect(Collectors.toList());
    }

    public void addPlayer(Player player) {
        this.players.add(new ShulkerPlayer(player.getUniqueId()));
    }

    public void removePlayer(Player player) {
        this.players.removeIf(u -> u.getUuid().equals(player.getUniqueId()));
    }

    @Override
    public IShulkerPlayer getPlayer(UUID uuid) {
        return this.players.stream().filter(p -> p.getUuid().equals(uuid)).findAny().orElse(null);
    }
}
