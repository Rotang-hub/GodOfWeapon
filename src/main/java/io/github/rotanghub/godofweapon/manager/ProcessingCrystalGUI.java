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

import java.util.*;

public class ProcessingCrystalGUI implements Listener
{
    private Manager manager;
    private Inventory processingTab;
    int crystalSlot = 20;
    int dustSlot = 24;
    private Player player;
    ItemStack targetCrystal = null;
    ItemStack targetDust = null;

    int[] buttonSlots = {39, 40, 41};

    public ProcessingCrystalGUI(Manager manager, Player player)
    {
        this.manager = manager;
        this.player = player;
        processingTab = Bukkit.createInventory(player, 54, ChatColor.WHITE + "\u7a7a" + "\u67b6");
        setScreen();
    }

    public Inventory getProcessingTab()
    {
        return processingTab;
    }

    public ItemStack getButton()
    {
        ItemStack button = new ItemStack(Material.MAP);
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + "가공하기");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE + "" + ChatColor.BOLD + "실패: 40%");
        lore.add(ChatColor.RED + "" + ChatColor.BOLD + "홍색 결정체: 30%");
        lore.add(ChatColor.BLUE + "" + ChatColor.BOLD + "청색 결정체: 30%");
        meta.setLore(lore);

        //meta.setCustomModelData(99);
        button.setItemMeta(meta);
        return button;
    }

    public void setButton(ItemStack button)
    {
        for(int slot : buttonSlots)
        {
            getProcessingTab().setItem(slot, button);
        }
    }

    private void setScreen()
    {
        ItemStack button = getButton();

        setButton(button);
    }

    public ItemStack manufacturing(Player player)
    {
        int crystalAmount = targetCrystal.getAmount();
        int dustAmount = targetDust.getAmount();
        ItemStack processedCrystal = new ItemStack(Material.AIR);

        if(crystalAmount >= 1 && dustAmount >= 1)
        {
            this.targetCrystal.setAmount(crystalAmount - 1);
            getProcessingTab().setItem(crystalSlot, this.targetCrystal);
            this.targetDust.setAmount(dustAmount - 1);
            getProcessingTab().setItem(dustSlot, this.targetDust);

            processedCrystal = getProcessedCrystal();

            manager.safeAddItem(player, processedCrystal);
            /*
            Map<Integer, ItemStack> map = player.getInventory().addItem(processedCrystal);
            for(Map.Entry<Integer, ItemStack> items : map.entrySet())
            {
                Item drop = player.getWorld().dropItemNaturally(player.getLocation(), items.getValue());
                drop.setOwner(player.getUniqueId());
            }
             */
        }
        return processedCrystal;
    }

    public ItemStack getProcessedCrystal()
    {
        Random random = new Random();
        int rand  = random.nextInt(100);

        if(0 <= rand && rand <= 39)     //30%
        {
            return new ItemStack(Material.AIR);
        }
        if(40 <= rand && rand <= 69)    //40%
        {
            return manager.getRedCrystal();
        }
        if(70 <= rand && rand <= 99)    //40%
        {
            return manager.getBlueCrystal();
        }
        else
            return new ItemStack(Material.AIR);
    }

    public void sendProcessingResult(Player player, ItemStack item)
    {
        if(item.getType().isAir()) player.sendMessage(ChatColor.BOLD + "무색 결정체를 가공했지만 아무것도 나오지 않았습니다.");
        if(manager.isRedCrystal(item)) player.sendMessage(ChatColor.BOLD + "무색 결정체를 가공하여 " +  ChatColor.RED + "" + ChatColor.BOLD + "홍색 결정체" + ChatColor.WHITE + "" + ChatColor.BOLD + "를 획득했습니다.");
        if(manager.isBlueCrystal(item)) player.sendMessage(ChatColor.BOLD + "무색 결정체를 가공하여 " + ChatColor.BLUE + "" + ChatColor.BOLD + "청색 결정체" + ChatColor.WHITE + "" + ChatColor.BOLD + "를 획득했습니다.");
    }

    public void sendProcessingResult(Player player, List<ItemStack> list)
    {
        int none = 0;
        int red = 0;
        int blue = 0;

        for(ItemStack item : list)
        {
            if(item.getType().isAir()) none++;
            if(manager.isRedCrystal(item)) red++;
            if(manager.isBlueCrystal(item)) blue++;
        }

        player.sendMessage(ChatColor.BOLD + "가공 결과");
        player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + " 홍색 결정체: " + red + "개");
        player.sendMessage(ChatColor.BLUE + "" + ChatColor.BOLD + " 청색 결정체: " + blue + "개");
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if(event.getClickedInventory() == null) return;
        if(event.getClickedInventory().equals(getProcessingTab()))
        {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            ItemStack item = event.getCurrentItem();
            if(item == null || item.getType().equals(Material.AIR)) return;

            if(item.getType().equals(getButton().getType()))
            {
                if(targetCrystal == null || targetDust == null || targetCrystal.getAmount() == 0 || targetDust.getAmount() == 0)
                {
                    player.sendMessage(ChatColor.RED + "가공에 필요한 재료가 부족합니다.");
                    player.playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 7, 1);
                    return;
                }
                if(event.getClick().equals(ClickType.LEFT)) //1회 전환
                {
                    ItemStack result = manufacturing(player);
                    sendProcessingResult(player, result);
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 3, 1.4f);
                }
                if(event.getClick().equals(ClickType.SHIFT_LEFT))   //모두 전환
                {
                    int crystalAmount = targetCrystal.getAmount();
                    int dustAmount = targetDust.getAmount();
                    List<ItemStack> results = new ArrayList<>();

                    if(crystalAmount > dustAmount)
                    {
                        for(int i = 0; i < crystalAmount; i++)
                        {
                            ItemStack result = manufacturing(player);
                            results.add(result);
                        }
                    }
                    else
                    {
                        for(int i = 0; i < dustAmount; i++)
                        {
                            ItemStack result = manufacturing(player);
                            results.add(result);
                        }
                    }
                    sendProcessingResult(player, results);
                    player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE, 3, 1.4f);
                }

            }
            if(event.getClick().equals(ClickType.LEFT))
            {
                if(item.isSimilar(targetCrystal))  //아이템 플레이어 인벤으로 이동
                {
                    manager.safeAddItem(player, item);
                    //player.getInventory().addItem(item);
                    item.setType(Material.AIR);
                    event.getClickedInventory().setItem(crystalSlot, new ItemStack(Material.AIR));
                    targetCrystal = null;
                }
                if(item.isSimilar(targetDust))  //아이템 플레이어 인벤으로 이동
                {
                    manager.safeAddItem(player, item);
                    //player.getInventory().addItem(item);
                    item.setType(Material.AIR);
                    event.getClickedInventory().setItem(dustSlot, new ItemStack(Material.AIR));
                    targetDust = null;
                }
            }
        }

        if(event.getClickedInventory().getType().equals(InventoryType.PLAYER))
        {
            Player player = (Player) event.getWhoClicked();
            Inventory openInv = player.getOpenInventory().getTopInventory();

            if(player != this.player) return;
            if(openInv.equals(processingTab)) event.setCancelled(true);
            ItemStack item = event.getCurrentItem();

            if(item == null || item.getType().equals(Material.AIR)) return;

            if(manager.isCrystal(item) && event.getClick().equals(ClickType.LEFT))    //대상 결정체 교체
            {
                event.setCancelled(true);
                if(targetCrystal != null)
                {
                    manager.safeAddItem(player, targetCrystal);
                    //player.getInventory().addItem(targetCrystal);
                }

                targetCrystal = item.clone();
                getProcessingTab().setItem(crystalSlot, targetCrystal);
                player.getInventory().setItem(event.getSlot(), new ItemStack(Material.AIR));
            }
            if(manager.isDiscolorDust(item) && event.getClick().equals(ClickType.LEFT))    //대상 결정체 교체
            {
                event.setCancelled(true);
                if(targetDust != null)
                {
                    manager.safeAddItem(player, targetDust);
                    //player.getInventory().addItem(targetDust);
                }

                targetDust = item.clone();
                getProcessingTab().setItem(dustSlot, targetDust);
                player.getInventory().setItem(event.getSlot(), new ItemStack(Material.AIR));
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        if(event.getInventory().equals(processingTab))
        {
            Player player = (Player) event.getPlayer();

            if(targetCrystal != null)
            {
                manager.safeAddItem(player, targetCrystal);
                /*
                Map<Integer, ItemStack> drops = player.getInventory().addItem(targetCrystal);
                for(Map.Entry<Integer, ItemStack> entry : drops.entrySet())
                {
                    Item drop = player.getWorld().dropItemNaturally(player.getLocation(), entry.getValue());
                    drop.setOwner(player.getUniqueId());
                }
                 */
                targetCrystal = null;
            }
            if(targetDust != null)
            {
                manager.safeAddItem(player, targetDust);
                /*
                Map<Integer, ItemStack> drops = player.getInventory().addItem(targetDust);
                if(!drops.isEmpty())
                {
                    for(Map.Entry<Integer, ItemStack> entry : drops.entrySet())
                    {
                        Item drop = player.getWorld().dropItemNaturally(player.getLocation(), entry.getValue());
                        drop.setOwner(player.getUniqueId());
                    }
                }
                 */
                targetDust = null;
            }
            event.getHandlers().unregisterAll(this);
        }
    }
}
