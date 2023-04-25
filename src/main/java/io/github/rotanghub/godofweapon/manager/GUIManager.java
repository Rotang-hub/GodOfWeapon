package io.github.rotanghub.godofweapon.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;

public class GUIManager implements Listener
{
    Manager manager;

    public GUIManager(Manager manager)
    {
        this.manager = manager;
    }


    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event)
    {
        if(manager.process)
        {
            if(event.getInventory().getType().equals(InventoryType.ANVIL))
            {
                event.setCancelled(true);
                EnhanceGUI gui = new EnhanceGUI(manager, (Player) event.getPlayer());
                Bukkit.getServer().getPluginManager().registerEvents(gui, manager.plugin);
                event.getPlayer().openInventory(gui.getEnhanceTab());
            }

            if(event.getInventory().getType().equals(InventoryType.ENCHANTING))
            {
                event.setCancelled(true);
                RevolutionGUI gui = new RevolutionGUI(manager, (Player) event.getPlayer());
                Bukkit.getServer().getPluginManager().registerEvents(gui, manager.plugin);
                event.getPlayer().openInventory(gui.getRevolutionTab());
            }
        }
    }
}
