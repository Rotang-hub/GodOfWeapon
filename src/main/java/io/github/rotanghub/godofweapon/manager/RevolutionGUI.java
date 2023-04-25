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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RevolutionGUI implements Listener
{
    private Manager manager;
    private Inventory revolutionTab;
    int weaponSlot = 22;
    private Player player;
    ItemStack targetItem = null;

    int[] buttonSlots = {39, 40, 41};

    public RevolutionGUI(Manager manager, Player player)
    {
        this.manager = manager;
        this.player = player;
        revolutionTab = Bukkit.createInventory(player, 54, ChatColor.WHITE + "\u7a7a" + "\u5bb6");
        setScreen();
    }

    public Inventory getRevolutionTab()
    {
        return revolutionTab;
    }

    public ItemStack getButton()
    {
        ItemStack button = new ItemStack(Material.MAP);
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + "진화하기");
        meta.setCustomModelData(99);
        button.setItemMeta(meta);
        return button;
    }

    public void setButton(ItemStack button)
    {
        for(int slot : buttonSlots)
        {
            getRevolutionTab().setItem(slot, button);
        }
    }

    public void updateRevolutionButtonLore(ItemStack item, int level)
    {
        ItemStack button = getButton();
        ItemMeta meta = button.getItemMeta();
        List<String> lore = new ArrayList<>();

        if(item == null)
        {
            meta.setLore(null);
            button.setItemMeta(meta);

            setButton(button);
        }
        else if(manager.isSatelliteSword(item) && level == 3)
        {
            lore.add(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "위성검 -> " + ChatColor.GREEN + "" + ChatColor.BOLD + "초섬검");
            lore.add(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "진화 재료: 무색 결정체 30개");
        }
        else if(manager.isSatelliteSword(item) && level == 4)
        {
            lore.add(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "위성검 -> " + ChatColor.AQUA + "" + ChatColor.BOLD + "파동검:출");
            lore.add(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "진화 재료: 홍색 결정체 5개");
        }
        else if(manager.isSatelliteSword(item) && level == 5)
        {
            lore.add(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "위성검 -> " + ChatColor.AQUA + "" + ChatColor.BOLD + "절안검");
            lore.add(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "진화 재료: 홍색 결정체 20개");
        }
        else if(manager.isSatelliteSword(item) && level == 7)
        {
            lore.add(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "위성검 -> " + ChatColor.DARK_RED + "" + ChatColor.BOLD + "홍성검");
            lore.add(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "진화 재료: 홍색 결정체 10개");
        }
        else if(manager.isSonificationSword(item) && level == 1)
        {
            lore.add(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "파동검:출 -> " + ChatColor.AQUA + "" + ChatColor.BOLD + "파동검:류");
            lore.add(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "진화 재료: 홍색 결정체 5개");
        }
        else if(manager.isRedDwarfSword(item) && level == 1)
        {
            lore.add(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "홍성검 -> " + ChatColor.AQUA + "" + ChatColor.BOLD + "염화검");
            lore.add(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "진화 재료: 청색 결정체 20개");
        }

        meta.setLore(lore);
        button.setItemMeta(meta);

        setButton(button);
    }

    private void setScreen()
    {
        ItemStack button = getButton();

        setButton(button);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if(event.getClickedInventory() == null) return;
        if(event.getClickedInventory().equals(getRevolutionTab()))
        {
            event.setCancelled(true);

            if(event.getClick().equals(ClickType.LEFT))
            {
                Player player = (Player) event.getWhoClicked();
                ItemStack item = event.getCurrentItem();

                if(item == null || item.getType().equals(Material.AIR)) return;
                if(item.getType().equals(getButton().getType()))
                {
                    if(targetItem == null)
                    {
                        return;
                    }

                    if(manager.isSatelliteSword(targetItem) && manager.getCurrentSwordLevel(targetItem) == 7)   //위성검7->홍성검
                    {
                        int account = 10;
                        if(manager.hasRedCrystal(player, account))
                        {
                            ItemStack redDwarfSword =  manager.satelliteToRedDwarf(targetItem);
                            targetItem = redDwarfSword;
                            event.getClickedInventory().setItem(weaponSlot, redDwarfSword);
                            manager.consumeRedCrystal(player, account);

                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 3, 1);
                            player.sendMessage(ChatColor.GREEN + "진화에 성공했습니다!");
                        }
                        else
                        {
                            player.playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 3, 1);
                            player.sendMessage(ChatColor.RED + "진화에 필요한 재료를 가지고 있지 않습니다 [필요량:홍색 결정체 " + account + "개]");
                        }
                    }

                    else if(manager.isSatelliteSword(targetItem) && manager.getCurrentSwordLevel(targetItem) == 3)  //위성검3 -> 초섬검
                    {
                        int account = 30;
                        if(manager.hasCrystal(player, account))
                        {
                            ItemStack herbSword =  manager.satelliteToHerb(targetItem);
                            targetItem = herbSword;
                            event.getClickedInventory().setItem(weaponSlot, herbSword);
                            manager.consumeCrystal(player, account);

                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 3, 1);
                            player.sendMessage(ChatColor.GREEN + "진화에 성공했습니다!");
                        }
                        else
                        {
                            player.playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 3, 1);
                            player.sendMessage(ChatColor.RED + "진화에 필요한 재료를 가지고 있지 않습니다 [필요량:무색 결정체 " + account + "개]");
                        }
                    }

                    else if(manager.isSatelliteSword(targetItem) && manager.getCurrentSwordLevel(targetItem) == 4)  //위성검4 -> 파동검
                    {
                        int account = 5;
                        if(manager.hasRedCrystal(player, account))
                        {
                            ItemStack sonificationSword =  manager.satelliteToSonification(targetItem);
                            targetItem = sonificationSword;
                            event.getClickedInventory().setItem(weaponSlot, sonificationSword);
                            manager.consumeRedCrystal(player, account);

                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 3, 1);
                            player.sendMessage(ChatColor.GREEN + "진화에 성공했습니다!");
                        }
                        else
                        {
                            player.playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 3, 1);
                            player.sendMessage(ChatColor.RED + "진화에 필요한 재료를 가지고 있지 않습니다 [필요량:홍색 결정체 " + account + "개]");
                        }
                    }
                    else if(manager.isSatelliteSword(targetItem) && manager.getCurrentSwordLevel(targetItem) == 5)  //위성검5 -> 절안검
                    {
                        int account = 20;
                        if(manager.hasRedCrystal(player, account))
                        {
                            ItemStack blindSword =  manager.satelliteToBlind(targetItem);
                            targetItem = blindSword;
                            event.getClickedInventory().setItem(weaponSlot, blindSword);
                            manager.consumeRedCrystal(player, account);

                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 3, 1);
                            player.sendMessage(ChatColor.GREEN + "진화에 성공했습니다!");
                        }
                        else
                        {
                            player.playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 3, 1);
                            player.sendMessage(ChatColor.RED + "진화에 필요한 재료를 가지고 있지 않습니다 [필요량:홍색 결정체 " + account + "개]");
                        }
                    }
                    else if(manager.isSonificationSword(targetItem) && manager.getCurrentSwordLevel(targetItem) == 1)  //파동검:출1 -> 파동검:류
                    {
                        int account = 5;
                        if(manager.hasRedCrystal(player, account))
                        {
                            ItemStack sonificationSwordRyu =  manager.sonificationToRyu(targetItem);
                            targetItem = sonificationSwordRyu;
                            event.getClickedInventory().setItem(weaponSlot, sonificationSwordRyu);
                            manager.consumeRedCrystal(player, account);

                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 3, 1);
                            player.sendMessage(ChatColor.GREEN + "진화에 성공했습니다!");
                        }
                        else
                        {
                            player.playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 3, 1);
                            player.sendMessage(ChatColor.RED + "진화에 필요한 재료를 가지고 있지 않습니다 [필요량:홍색 결정체 " + account + "개]");
                        }
                    }
                    else if(manager.isRedDwarfSword(targetItem) && manager.getCurrentSwordLevel(targetItem) == 1)  //홍성검1 -> 염화검
                    {
                        int account = 20;
                        if(manager.hasBlueCrystal(player, account))
                        {
                            ItemStack blazingSword =  manager.redDwarfToBlazing(targetItem);
                            targetItem = blazingSword;
                            event.getClickedInventory().setItem(weaponSlot, blazingSword);
                            manager.consumeBlueCrystal(player, account);

                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 3, 1);
                            player.sendMessage(ChatColor.GREEN + "진화에 성공했습니다!");
                        }
                        else
                        {
                            player.playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 3, 1);
                            player.sendMessage(ChatColor.RED + "진화에 필요한 재료를 가지고 있지 않습니다 [필요량:청색 결정체 " + account + "개]");
                        }
                    }
                    else
                    {
                        player.sendMessage(ChatColor.RED + "진화할 수 없는 무기입니다.");
                        player.playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 3, 1);
                        return;
                    }

                    if(manager.isWeapon(targetItem))
                    {
                        updateRevolutionButtonLore(targetItem, manager.getCurrentSwordLevel(targetItem));
                    }
                }

                if(item.isSimilar(targetItem))  //진화대상 아이템 플레이어 인벤으로 이동
                {
                    manager.safeAddItem(player, item);
                    //player.getInventory().addItem(item);
                    item.setType(Material.AIR);
                    event.getClickedInventory().setItem(weaponSlot, new ItemStack(Material.AIR));
                    targetItem = null;

                    updateRevolutionButtonLore(null, 0);
                }
            }
        }

        if(event.getClickedInventory().getType().equals(InventoryType.PLAYER))
        {
            Player player = (Player) event.getWhoClicked();
            Inventory openInv = player.getOpenInventory().getTopInventory();

            if(player != this.player) return;
            if(openInv.equals(revolutionTab)) event.setCancelled(true);
            ItemStack item = event.getCurrentItem();

            if(item == null || item.getType().equals(Material.AIR)) return;

            if(manager.isWeapon(item) && event.getClick().equals(ClickType.LEFT))    //강화대상 아이템 교체
            {
                event.setCancelled(true);
                if(targetItem != null)
                {
                    manager.safeAddItem(player, targetItem);
                    //player.getInventory().addItem(targetItem);
                }

                targetItem = item.clone();
                getRevolutionTab().setItem(weaponSlot, targetItem);
                player.getInventory().setItem(event.getSlot(), new ItemStack(Material.AIR));

                updateRevolutionButtonLore(item, manager.getCurrentSwordLevel(item));
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        if(event.getInventory().equals(revolutionTab))
        {
            Player player = (Player) event.getPlayer();

            if(targetItem != null)
            {
                manager.safeAddItem(player, targetItem);
                /*
                Map<Integer, ItemStack> drops = player.getInventory().addItem(targetItem);
                if(!drops.isEmpty())
                {
                    for(Map.Entry<Integer, ItemStack> entry : drops.entrySet())
                    {
                        Item drop = player.getWorld().dropItemNaturally(player.getLocation(), entry.getValue());
                        drop.setOwner(player.getUniqueId());
                    }
                }
                 */

                targetItem = null;
            }
            event.getHandlers().unregisterAll(this);
        }
    }
}
