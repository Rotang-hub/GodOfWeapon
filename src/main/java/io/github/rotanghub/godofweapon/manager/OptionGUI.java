package io.github.rotanghub.godofweapon.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class OptionGUI implements Listener
{
    Manager manager;
    Inventory optionTab;
    private Player player;

    int[] dmgMsgButtonSlot = {0, 1, 2, 9, 10, 11};

    public OptionGUI(Manager manager, Player player)
    {
        this.manager = manager;
        this.player = player;
        optionTab = Bukkit.createInventory(player, 54, ChatColor.WHITE + "\u7a7a" + "\u4f3d");
        setDmgMsgButton();
    }

    public Inventory getOptionTab()
    {
        return optionTab;
    }

    public void setDmgMsgButton()
    {
        setDmgMsgButton(getDmgMsgButton());

        updateDmgMsgButtonLore(player);
    }

    public void setDmgMsgButton(ItemStack button)
    {
        for(int slot : dmgMsgButtonSlot)
        {
            getOptionTab().setItem(slot, button);
        }
    }

    public ItemStack getDmgMsgButton()
    {
        ItemStack button = new ItemStack(Material.MAP);
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + "데미지 알림");
        meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "채팅창에 자신이 상대방에게 입힌 데미지를 표시합니다.",
                                    ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "현재 상태: " + manager.isAllowDmgMsg(player))); //on/off
        meta.setCustomModelData(99);

        button.setItemMeta(meta);

        return button;
    }

    public void updateDmgMsgButtonLore(Player player)
    {
        ItemStack button = getDmgMsgButton();
        ItemMeta meta = button.getItemMeta();
        boolean dmgMsg = manager.isAllowDmgMsg(player);
        meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "채팅창에 자신이 상대방에게 입힌 데미지를 표시합니다.",
                ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "현재 상태: " + dmgMsg)); //on/off
        button.setItemMeta(meta);

        setDmgMsgButton(button);
    }

    public boolean isDmgMsgSlot(int slot)
    {
        for(int s : dmgMsgButtonSlot) if(s == slot) return true;
        return false;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if(event.getClickedInventory() == null) return;
        if(event.getClickedInventory().equals(getOptionTab()))
        {
            event.setCancelled(true);

            if(event.getClick().equals(ClickType.LEFT))
            {
                Player player = (Player) event.getWhoClicked();
                ItemStack item = event.getCurrentItem();

                if(item == null || item.getType().equals(Material.AIR)) return;

                int slot = event.getSlot();

                //if(item.getType().equals(getDmgMsgButton().getType()))     //데미지 확인 버튼 클릭 시
                if(isDmgMsgSlot(slot))
                {
                    boolean allowDmgMsg = manager.isAllowDmgMsg(player);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 7, 1);
                    manager.setAllowDmgMsg(player, !allowDmgMsg);
                    updateDmgMsgButtonLore(player);     //버튼 업데이트
                }
            }
        }
        if(event.getClickedInventory().getType().equals(InventoryType.PLAYER))
        {
            Player player = (Player) event.getWhoClicked();
            Inventory openInv = player.getOpenInventory().getTopInventory();

            if(player != this.player) return;
            if (openInv.equals(getOptionTab())) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        if(event.getInventory().equals(getOptionTab()))
        {
            event.getHandlers().unregisterAll(this);
        }
    }
}
