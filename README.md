# ShulkerOpen
ShulkerOpen is yet another backpack plugin that allows to open Shulker Boxes by right clicking on air.

## Dupe Bugs
The plugin has been heavily tested and many patches have been applied to prevent duplication bugs.
However, nothing is perfect and there might be still some mods or Minecraft updates that break it. You can contact me to let me know and I will work to correct them.

## API
ShulkerOpen offers an API in case the developer wants to include it in its plugin (for example in a Core for a game mode).
<br>
First of all, you need to create a class to keep track of the players:
```java
package it.fulminazzo.testplugin.Objects;

import it.fulminazzo.shulkeropen.Interfaces.IShulkerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Date;
import java.util.UUID;

public class PluginPlayer implements IShulkerPlayer {
    private ItemStack shulkerBox;
    private Date recentClick;
    private final UUID uuid;

    public PluginPlayer(UUID uuid) {
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
```
This class is required to check every player that is opening a Shulker Box and to save it (in order to prevent some [Dupe Bugs](#dupe-bugs)). 
Keep in mind that you will require a **PlayersManager** to save every instance of the custom players (therefore you should implement a **Listener** to add a new instance of **PluginPlayer** on login and remove it on logout).
Also, the **PlayersManager** should implement **IShulkerPlayersManager**:
```java
package it.fulminazzo.testplugin.Managers;

import it.fulminazzo.shulkeropen.Interfaces.IShulkerPlayer;
import it.fulminazzo.shulkeropen.Interfaces.IShulkerPlayersManager;
import it.fulminazzo.testplugin.Objects.PluginPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayersManager implements IShulkerPlayersManager {
    private final List<PluginPlayer> players;
    
    public PlayersManager() {
        this.players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        if (player != null) this.players.add(new PluginPlayer(player.getUniqueId()));
    }
    
    public void removePlayer(Player player) {
        if (player != null) this.players.removeIf(p -> p.getUuid().equals(player.getUniqueId()));
    }
    
    @Override
    public IShulkerPlayer getPlayer(UUID uuid) {
        return uuid == null ? null : players.stream().filter(p -> p.getUuid().equals(uuid)).findAny().orElse(null);
    }
}
```
Finally, you can create your JavaPlugin class and implement **IShulkerPlugin**.
<br>
In here you will be able to register a new **ShulkerListener** listener that will be responsible of the Shulker actions (right click on air, move items in inventory, close shulker and more).
```java
package it.fulminazzo.testplugin;

import it.fulminazzo.shulkeropen.Interfaces.IShulkerPlugin;
import it.fulminazzo.shulkeropen.Listeners.ShulkerListener;
import it.fulminazzo.testplugin.Managers.PlayersManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class TestPlugin extends JavaPlugin implements IShulkerPlugin {
    private PlayersManager playersManager;

    @Override
    public void onEnable() {
        playersManager = new PlayersManager();
        Bukkit.getOnlinePlayers().forEach(playersManager::addPlayer);
        
        // Register events for Shulker Boxes.
        Bukkit.getPluginManager().registerEvents(new ShulkerListener(this), this);
    }

    @Override
    public PlayersManager getShulkerPlayersManager() {
        return playersManager;
    }
}
```
You can even create your own listener and listen to the **ShulkerBoxOpenEvent** event (called when opening a shulker box).