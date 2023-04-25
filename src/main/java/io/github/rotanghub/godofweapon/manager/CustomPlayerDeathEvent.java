package io.github.rotanghub.godofweapon.manager;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CustomPlayerDeathEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();

    private Player player;
    private Entity killer;

    public CustomPlayerDeathEvent(Player player, Entity killer)
    {
        this.player = player;
        this.killer = killer;
    }

    public Player getPlayer()
    {
        return this.player;
    }

    public Entity getKiller()
    {
        return this.killer;
    }

    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}
