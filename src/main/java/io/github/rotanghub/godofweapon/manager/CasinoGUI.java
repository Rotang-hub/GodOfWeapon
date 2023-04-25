package io.github.rotanghub.godofweapon.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
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
import java.util.HashMap;
import java.util.Map;

public class CasinoGUI implements Listener
{
    Manager manager;
    Inventory casinoTab;
    int price = 2000;
    private Player player;

    int[] buyChipButtonSlot = {19, 20, 21, 28, 29, 30};
    int[] sellChipButtonSlot = {23, 24, 25, 32, 33, 34};

    public CasinoGUI(Manager manager, Player player)
    {
        this.manager = manager;
        this.player = player;
        casinoTab = Bukkit.createInventory(player, 54, ChatColor.WHITE + "\u7a7a" + "\u6687");
        setButton();
    }

    public ItemStack getChip()
    {
        ItemStack chip = new ItemStack(Material.FLINT);
        ItemMeta meta = chip.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GOLD + "[" + ChatColor.WHITE + "RO-CASINO" + ChatColor.GOLD + "]" + ChatColor.WHITE + " CHIP");
        chip.setItemMeta(meta);

        return chip;
    }

    public Inventory getCasinoTab()
    {
        return casinoTab;
    }

    public void setButton()
    {
        for(int slot : buyChipButtonSlot)
        {
           getCasinoTab().setItem(slot, getBuyChipButton());
        }
        for(int slot : sellChipButtonSlot)
        {
            getCasinoTab().setItem(slot, getSellChipButton());
        }
    }

    public ItemStack getBuyChipButton()
    {
        ItemStack button = new ItemStack(Material.MAP);
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + "칩 구매");
        meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "골드를 사용하여 칩을 구매합니다.",
                ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "구매가: " + ChatColor.GOLD + "" + ChatColor.BOLD + price + "Gold"));
        meta.setCustomModelData(99);

        button.setItemMeta(meta);

        return button;
    }

    public ItemStack getSellChipButton()
    {
        ItemStack button = new ItemStack(Material.MAP);
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + "칩 판매");
        meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "칩을 판매합니다.",
                ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "판매가: " + ChatColor.GOLD + "" + ChatColor.BOLD + price + "Gold"));
        meta.setCustomModelData(99);

        button.setItemMeta(meta);

        return button;
    }

    public boolean isBuyChipSlot(int slot)
    {
        for(int s : buyChipButtonSlot) if(s == slot) return true;
        return false;
    }

    public boolean isSellChipSlot(int slot)
    {
        for(int s : sellChipButtonSlot) if(s == slot) return true;
        return false;
    }

    public void buyChip(Player player, int amount)
    {
        int gold = manager.getGold(player);
        int totalPrice = price * amount;

        if(gold < totalPrice)
        {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 3, 2);
            player.sendMessage(ChatColor.RED + "구매에 필요한 골드가 부족합니다.");
            return;
        }

        manager.setGold(player, gold - totalPrice);

        ItemStack chip = getChip();
        chip.setAmount(amount);

        addItems(player, chip);
        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 3, 1.3f);
    }

    public void sellChip(Player player, int amount)
    {
        int gold = manager.getGold(player);
        int totalPrice = price * amount;

        if(!hasItems(player, getChip(), amount))
        {
            return;
        }

        manager.setGold(player, gold + totalPrice);
        removeItems(player, getChip(), amount);

        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON, 3, 1.3f);
    }

    public boolean hasItems(Player player, ItemStack item, int amount)
    {
        Inventory inv = player.getInventory();
        int currentAmount = 0;

        for(ItemStack i : inv.getContents())
        {
            if(i == null || i.getType().equals(Material.AIR))
                continue;
            if(i.getType().equals(item.getType()))
            {
                currentAmount += i.getAmount();
            }
        }

        return amount <= currentAmount;
    }

    public void removeItems(Player player, ItemStack item, int amount)
    {
        Inventory inv = player.getInventory();
        for(ItemStack i : inv.getContents())
        {
            if(i == null || i.getType().equals(Material.AIR))
                continue;
            if(i.getType().equals(item.getType()))
            {
                if(i.getAmount() < amount)
                {
                    amount = amount - i.getAmount();
                    i.setAmount(0);
                }
                else
                {
                    i.setAmount(i.getAmount() - amount);
                    return;
                }
            }
        }
    }

    public void addItems(Player player, ItemStack item)
    {
        manager.safeAddItem(player, item);
        /*
        HashMap<Integer, ItemStack> nope = player.getInventory().addItem(item);
        for(Map.Entry<Integer, ItemStack> entry : nope.entrySet())
        {
            Item drop = player.getWorld().dropItemNaturally(player.getLocation(), entry.getValue());
            drop.setOwner(player.getUniqueId());
        }
         */
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if(event.getClickedInventory() == null) return;
        if(event.getClickedInventory().equals(getCasinoTab()))
        {
            event.setCancelled(true);

            Player player = (Player) event.getWhoClicked();
            ItemStack item = event.getCurrentItem();

            if(item == null || item.getType().equals(Material.AIR)) return;

            int slot = event.getSlot();

            if(isBuyChipSlot(slot))      //구매 버튼 클릭 시
            {
                if(event.getClick().equals(ClickType.LEFT))
                {
                    buyChip(player, 1);
                }
                if(event.getClick().equals(ClickType.SHIFT_LEFT))
                {
                    buyChip(player, 32);
                }
            }
            if(isSellChipSlot(slot))      //판매 버튼 클릭 시
            {
                if(event.getClick().equals(ClickType.LEFT))
                {
                    sellChip(player, 1);
                }
                if(event.getClick().equals(ClickType.SHIFT_LEFT))
                {
                    sellChip(player, 32);
                }
            }
        }

        if(event.getClickedInventory().getType().equals(InventoryType.PLAYER))
        {
            Player player = (Player) event.getWhoClicked();
            Inventory openInv = player.getOpenInventory().getTopInventory();

            if(player != this.player) return;
            if(openInv.equals(getCasinoTab())) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        if(event.getInventory().equals(getCasinoTab()))
        {
            event.getHandlers().unregisterAll(this);
        }
    }
}
