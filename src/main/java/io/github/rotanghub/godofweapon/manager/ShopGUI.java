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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopGUI implements Listener
{
    Manager manager;
    Inventory shopTab;
    private Player player;

    public ShopGUI(Manager manager, Player player)
    {
        this.manager = manager;
        this.player = player;
        shopTab = Bukkit.createInventory(player, 54, ChatColor.WHITE + "\u7a7a" + "\u52a0");
        setButton();
    }

    public Inventory getShopTab()
    {
        return shopTab;
    }

    public void setButton()
    {
        getShopTab().setItem(0, getShopIronPickaxe());
        getShopTab().setItem(1, getShopDagger());
        getShopTab().setItem(2, getShopCounterShield());
        getShopTab().setItem(4, getShopCrystal());
        getShopTab().setItem(5, getShopRedCrystal());
        getShopTab().setItem(6, getShopProtector());
        getShopTab().setItem(8, getShopDiscolorDust());

        getShopTab().setItem(18, getShopBasalt());
        getShopTab().setItem(19, getShopCoal());
        getShopTab().setItem(20, getShopIron());
        getShopTab().setItem(21, getShopGold());
        getShopTab().setItem(22, getShopDiamond());
    }

    public ItemStack getShopCrystal()
    {
        ItemStack crystal = manager.getCrystal();
        addPriceTag(crystal);

        return crystal;
    }

    public ItemStack getShopRedCrystal()
    {
        ItemStack redCrystal = manager.getRedCrystal();
        addPriceTag(redCrystal);
        return redCrystal;
    }

    public ItemStack getShopIronPickaxe()
    {
        ItemStack ironPickaxe = new ItemStack(Material.IRON_PICKAXE);
        addPriceTag(ironPickaxe);
        return ironPickaxe;
    }

    public ItemStack getShopDagger()
    {
        ItemStack dagger = manager.getSatelliteDagger();
        addPriceTag(dagger);
        return dagger;
    }

    public ItemStack getShopCounterShield()
    {
        ItemStack shield = manager.getCounterShield();
        addPriceTag(shield);
        return shield;
    }

    public ItemStack getShopProtector()
    {
        ItemStack protector = manager.getProtector();
        addPriceTag(protector);
        return protector;
    }

    public ItemStack getShopDiscolorDust()
    {
        ItemStack dust = manager.getDiscolorDust();
        addPriceTag(dust);
        return dust;
    }

    public ItemStack getShopBasalt()
    {
        ItemStack basalt = new ItemStack(Material.BASALT);
        addPriceTag(basalt);
        return basalt;
    }

    public ItemStack getShopCoal()
    {
        ItemStack coal = new ItemStack(Material.COAL);
        addPriceTag(coal);
        return coal;
    }

    public ItemStack getShopIron()
    {
        ItemStack iron = new ItemStack(Material.RAW_IRON);
        addPriceTag(iron);
        return iron;
    }

    public ItemStack getShopGold()
    {
        ItemStack gold = new ItemStack(Material.RAW_GOLD);
        addPriceTag(gold);
        return gold;
    }

    public ItemStack getShopDiamond()
    {
        ItemStack diamond = new ItemStack(Material.DIAMOND);
        addPriceTag(diamond);
        return diamond;
    }

    public int getBuyingPrice(ItemStack item)
    {
        if(item.getType().equals(Material.IRON_PICKAXE)) return 3000;
        if(manager.isSatelliteDagger(item)) return 2000;
        if(manager.isCounterShield(item)) return 100000;
        if(manager.isCrystal(item)) return 1000;
        if(manager.isRedCrystal(item)) return  3000;
        if(manager.isProtector(item)) return 50000;
        if(manager.isDiscolorDust(item)) return 2000;
        else return 0;
    }

    public int getSellPrice(ItemStack item)
    {
        Material type = item.getType();
        if(manager.isCrystal(item)) return 1000;
        if(type.equals(Material.BASALT)) return 100;
        if(type.equals(Material.COAL)) return 300;
        if(type.equals(Material.RAW_IRON)) return 700;
        if(type.equals(Material.RAW_GOLD)) return 1000;
        if(type.equals(Material.DIAMOND)) return 1200;
        if(manager.isDiscolorDust(item)) return 1200;
        else return 0;
    }

    public ItemStack addPriceTag(ItemStack item)
    {
        ItemMeta meta = item.getItemMeta();
        List<String> lore;

        if(!meta.hasLore()) lore = new ArrayList<>();
        else lore = meta.getLore();

        int buyingPrice = getBuyingPrice(item);
        int sellPrice = getSellPrice(item);

        if(buyingPrice == 0) lore.add(ChatColor.RESET + "" + ChatColor.DARK_RED + "" + ChatColor.BOLD + "구매불가");
        else lore.add(ChatColor.RESET + "" + ChatColor.GREEN + "" + ChatColor.BOLD + "[좌클릭] 구매가: " + buyingPrice + "골드");

        if(sellPrice == 0) lore.add(ChatColor.RESET + "" + ChatColor.DARK_RED + "" + ChatColor.BOLD + "판매불가");
        else lore.add(ChatColor.RESET + "" + ChatColor.GREEN + "" + ChatColor.BOLD + "[우클릭] 판매가: " + sellPrice + "골드");

        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }

    public void buyItem(Player player, ItemStack item, int amount)
    {
        int gold = manager.getGold(player);
        int price = getBuyingPrice(item) * amount;

        if(gold < price)
        {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 3, 2);
            player.sendMessage(ChatColor.RED + "구매에 필요한 골드가 부족합니다.");
            return;
        }

        if(item.isSimilar(getShopIronPickaxe()))
        {
            manager.setGold(player, gold - (price / amount));
            ItemStack ironPickaxe =  new ItemStack(Material.IRON_PICKAXE);

            addItems(player, ironPickaxe);
        }
        else if(manager.isSatelliteDagger(item))
        {
            manager.setGold(player, gold - (price / amount));

            addItems(player, manager.getSatelliteDagger());
        }
        else if(manager.isCounterShield(item))
        {
            manager.setGold(player, gold - (price / amount));

            addItems(player, manager.getCounterShield());
        }
        else if(item.isSimilar(getShopCrystal()))
        {
            manager.setGold(player, gold - price);
            ItemStack crystal = manager.getCrystal();
            crystal.setAmount(amount);

            addItems(player, crystal);
        }
        else if(item.isSimilar(getShopRedCrystal()))
        {
            manager.setGold(player, gold - price);
            ItemStack redCrystal = manager.getRedCrystal();
            redCrystal.setAmount(amount);

            addItems(player, redCrystal);
        }
        else if(item.isSimilar(getShopProtector()))
        {
            manager.setGold(player, gold - price);
            ItemStack protector = manager.getProtector();
            protector.setAmount(amount);

            addItems(player, protector);
        }
        else if(item.isSimilar(getShopDiscolorDust()))
        {
            manager.setGold(player, gold - price);
            ItemStack dust = manager.getDiscolorDust();
            dust.setAmount(amount);

            addItems(player, dust);
        }
        else
        {
            return;
        }
        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_NETHERITE, 3, 1.3f);
    }

    public void sellItem(Player player, ItemStack item, int amount)
    {
        int gold = manager.getGold(player);
        int price = getSellPrice(item) * amount;

        if(!hasItems(player, item, amount))
        {
            if(getItemAmount(player, item) != 0)
            {
                amount = getItemAmount(player, item);
                price = getSellPrice(item) * amount;
            }
            else
            {
                return;
            }
        }

        if(item.isSimilar(getShopCrystal()))
        {
            manager.setGold(player, gold + price);
            manager.consumeCrystal(player, amount);
        }
        else if(item.isSimilar(getShopBasalt()))
        {
            manager.setGold(player, gold + price);
            removeItems(player, new ItemStack(Material.BASALT), amount);
        }
        else if(item.isSimilar(getShopCoal()))
        {
            manager.setGold(player, gold + price);
            removeItems(player, new ItemStack(Material.COAL), amount);
        }
        else if(item.isSimilar(getShopIron()))
        {
            manager.setGold(player, gold + price);
            removeItems(player, new ItemStack(Material.RAW_IRON), amount);
        }
        else if(item.isSimilar(getShopGold()))
        {
            manager.setGold(player, gold + price);
            removeItems(player, new ItemStack(Material.RAW_GOLD), amount);
        }
        else if(item.isSimilar(getShopDiamond()))
        {
            manager.setGold(player, gold + price);
            removeItems(player, new ItemStack(Material.DIAMOND), amount);
        }
        else if(item.isSimilar(getShopDiscolorDust()))
        {
            manager.setGold(player, gold + price);
            removeItems(player, new ItemStack(Material.RAW_COPPER), amount);
        }
        else
        {
            return;
        }
        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_IRON, 3, 1.3f);
    }

    public boolean hasItems(Player player, ItemStack item, int amount)
    {
        return amount <= getItemAmount(player, item);
    }

    public int getItemAmount(Player player, ItemStack item)
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

        return currentAmount;
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
        if(event.getClickedInventory().equals(getShopTab()))
        {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack item = event.getCurrentItem();
            if(item == null || item.getType().equals(Material.AIR)) return;

            if(event.getClick().equals(ClickType.LEFT))
            {
                buyItem(player, item, 1);
            }
            if(event.getClick().equals(ClickType.RIGHT))
            {
                sellItem(player, item, 1);
            }
            if(event.getClick().equals(ClickType.SHIFT_LEFT))
            {
                buyItem(player, item, 32);
            }
            if(event.getClick().equals(ClickType.SHIFT_RIGHT))
            {
                sellItem(player, item, 32);
            }
        }

        if(event.getClickedInventory().getType().equals(InventoryType.PLAYER))
        {
            Player player = (Player) event.getWhoClicked();
            Inventory openInv = player.getOpenInventory().getTopInventory();

            if(player != this.player) return;
            if(openInv.equals(getShopTab())) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        if(event.getInventory().equals(getShopTab()))
        {
            event.getHandlers().unregisterAll(this);
        }
    }
}
