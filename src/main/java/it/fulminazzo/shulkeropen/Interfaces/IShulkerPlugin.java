package it.fulminazzo.shulkeropen.Interfaces;

public interface IShulkerPlugin {
    <M extends IShulkerPlayersManager> M getShulkerPlayersManager();
}
