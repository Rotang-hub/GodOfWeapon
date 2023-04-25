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

public class EnhanceGUI implements Listener
{
    private Manager manager;
    private Inventory enhanceTab;
    int weaponSlot = 22;
    int[] buttonSlot = {37, 38};
    int[] sellSlot = {42, 43};
    private Player player;
    private int gold = 0;
    private ItemStack targetItem = null;

    public EnhanceGUI(Manager manager, Player player)
    {
        this.manager = manager;
        this.player = player;
        enhanceTab = Bukkit.createInventory(player, 54, ChatColor.WHITE + "\u7a7a" + "\u53ef");
        setScreen();
    }

    public Inventory getEnhanceTab()
    {
        return enhanceTab;
    }

    public ItemStack getEnhanceButton()
    {
        ItemStack button = new ItemStack(Material.MAP);
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + "강화하기");
        meta.setCustomModelData(99);
        button.setItemMeta(meta);

        return button;
    }

    public void setEnhanceButton(ItemStack button)
    {
        for(int slot : buttonSlot)
        {
            getEnhanceTab().setItem(slot, button);
        }
    }

    public void updateEnhanceButtonLore(String type, int level)
    {
        ItemStack button = getEnhanceButton();
        ItemMeta meta = button.getItemMeta();
        List<String> lore = new ArrayList<>();

        if(type == null)
        {
            meta.setLore(null);
            button.setItemMeta(meta);

            setEnhanceButton(button);
        }

        int successProb = manager.getProb(type, level);
        int failProb = 100 - successProb;
        int brokenProb = failProb / 10;

        if(successProb == 0)
        {
            failProb = 0;
            brokenProb = 0;
        }

        lore.add(ChatColor.RESET + "" + ChatColor.GREEN + "" + ChatColor.BOLD + "성공 확률: " + successProb + "%");
        lore.add(ChatColor.RESET + "" + ChatColor.RED + "" + ChatColor.BOLD + "실패 확률: " + (failProb - brokenProb) + "%");
        lore.add(ChatColor.RESET + "" + ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "파괴 확률: " + brokenProb + "%");
        lore.add(ChatColor.RESET + "" + ChatColor.BLUE + "" + ChatColor.BOLD + "스탯 증가량: " + manager.getStatMin(type, level) + "~" + manager.getStatMax(type, level));

        if(type == "SatelliteSword" || type == "SatelliteDagger" || type == "HerbSword" || type == "CounterShield")
        {
            lore.add(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "강화재료: 무색 결정체 " + manager.getConsume(type, level) + "개");
        }
        else if(type == "RedDwarfSword" || type == "SonificationSword" || type == "SonificationSwordRyu" || type == "BlindSword")
        {
            lore.add(ChatColor.RESET + "" + ChatColor.WHITE + ChatColor.BOLD + "강화재료: 홍색 결정체 " + manager.getConsume(type, level) + "개");
        }
        else if(type == "EvolveSatelliteSword" || type == "BlazingSword")
        {
            lore.add(ChatColor.RESET + "" + ChatColor.WHITE + ChatColor.BOLD + "강화재료: 청색 결정체 " + manager.getConsume(type, level) + "개");
        }

        meta.setLore(lore);
        button.setItemMeta(meta);

        setEnhanceButton(button);
    }

    public ItemStack getSellButton()
    {
        ItemStack button = new ItemStack(Material.MAP);
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + "판매하기");
        meta.setCustomModelData(99);
        button.setItemMeta(meta);

        return button;
    }

    public void setSellButton(ItemStack button)
    {
        for(int slot : sellSlot)
        {
            getEnhanceTab().setItem(slot, button);
        }
    }

    public void updateSellButtonLore(String type, int level)
    {
        ItemStack button = getSellButton();
        ItemMeta meta = button.getItemMeta();

        if(type == null)
        {
            meta.setLore(null);
            button.setItemMeta(meta);

            setEnhanceButton(button);
        }

        meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.GOLD + "" + ChatColor.BOLD + "판매가: " + manager.getPrice(type, level) + "골드"));
        button.setItemMeta(meta);

        setSellButton(button);
    }

    private void setScreen()
    {
        ItemStack upgradeButton = getEnhanceButton();
        ItemStack sellButton = getSellButton();
        setEnhanceButton(upgradeButton);
        setSellButton(sellButton);
    }

    public boolean isButtonSlot(int slot)
    {
        for(int s : buttonSlot) if(s == slot) return true;
        return false;
    }

    public boolean isSellSlot(int slot)
    {
        for(int s : sellSlot) if(s == slot) return true;
        return false;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if(event.getClickedInventory() == null) return;
        if(event.getClickedInventory().equals(getEnhanceTab()))
        {
            event.setCancelled(true);

            if(event.getClick().equals(ClickType.LEFT))
            {
                Player player = (Player) event.getWhoClicked();
                ItemStack item = event.getCurrentItem();

                if(item == null || item.getType().equals(Material.AIR)) return;

                int slot = event.getSlot();

                if(isButtonSlot(slot))      //강화버튼 클릭 시
                {
                    if(targetItem == null)
                    {
                        player.sendMessage(ChatColor.RED + "강화할 무기가 없거나 강화할 수 없는 아이템입니다");
                        player.playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 3, 1);
                        return;
                    }

                    if(manager.isSatelliteDagger(targetItem))   //위성단검 강화
                    {
                        ItemStack result = manager.upgradeSatelliteDagger(player, targetItem);

                        if(result == null) return;
                        else
                        {
                            if(result.getType().equals(Material.AIR))
                            {
                                event.getClickedInventory().setItem(weaponSlot, new ItemStack(Material.AIR));
                                targetItem = null;
                                updateEnhanceButtonLore("SatelliteDagger", 0);
                                updateSellButtonLore("SatelliteDagger", 0);
                                return;
                            }
                            if(manager.isSatelliteSword(result))    //위성검 진화 시
                            {
                                event.getClickedInventory().setItem(weaponSlot, result);
                                targetItem = result;
                                updateEnhanceButtonLore("SatelliteSword", 1);
                                updateSellButtonLore("SatelliteSword", 1);
                                return;
                            }
                            event.getClickedInventory().setItem(weaponSlot, result);
                            updateEnhanceButtonLore("SatelliteDagger", manager.getCurrentSwordLevel(result));
                            updateSellButtonLore("SatelliteDagger", manager.getCurrentSwordLevel(result));
                        }
                    }
                    else if(manager.isSatelliteSword(targetItem))   //위성검 강화
                    {
                        ItemStack result = manager.upgradeSatelliteSword(player, targetItem);

                        if(result == null) return;
                        else
                        {
                            if(result.getType().equals(Material.AIR))
                            {
                                event.getClickedInventory().setItem(weaponSlot, new ItemStack(Material.AIR));
                                targetItem = null;
                                updateEnhanceButtonLore("SatelliteSword", 0);
                                updateSellButtonLore("SatelliteSword", 0);
                                return;
                            }
                            if(manager.isEvolveSatelliteSword(result))    //진 위성검 진화 시
                            {
                                event.getClickedInventory().setItem(weaponSlot, result);
                                targetItem = result;
                                updateEnhanceButtonLore("EvolveSatelliteSword", 1);
                                updateSellButtonLore("EvolveSatelliteSword", 1);
                                return;
                            }
                            event.getClickedInventory().setItem(weaponSlot, targetItem);
                            updateEnhanceButtonLore("SatelliteSword", manager.getCurrentSwordLevel(targetItem));
                            updateSellButtonLore("SatelliteSword", manager.getCurrentSwordLevel(targetItem));
                        }
                    }
                    else if(manager.isRedDwarfSword(targetItem))   //홍성검 강화
                    {
                        ItemStack result = manager.upgradeRedDwarfSword(player, targetItem);

                        if(result == null) return;
                        else
                        {
                            if(result.getType().equals(Material.AIR))
                            {
                                event.getClickedInventory().setItem(weaponSlot, new ItemStack(Material.AIR));
                                targetItem = null;
                                updateEnhanceButtonLore("RedDwarfSword", 0);
                                updateSellButtonLore("RedDwarfSword", 0);
                                return;
                            }
                            event.getClickedInventory().setItem(weaponSlot, targetItem);
                            updateEnhanceButtonLore("RedDwarfSword", manager.getCurrentSwordLevel(targetItem));
                            updateSellButtonLore("RedDwarfSword", manager.getCurrentSwordLevel(targetItem));
                        }
                    }
                    else if(manager.isHerbSword(targetItem))   //초섬검 강화
                    {
                        ItemStack result = manager.upgradeHerbSword(player, targetItem);

                        if(result == null) return;
                        else
                        {
                            if(result.getType().equals(Material.AIR))
                            {
                                event.getClickedInventory().setItem(weaponSlot, new ItemStack(Material.AIR));
                                targetItem = null;
                                updateEnhanceButtonLore("HerbSword", 0);
                                updateSellButtonLore("HerbSword", 0);
                                return;
                            }
                            event.getClickedInventory().setItem(weaponSlot, targetItem);
                            updateEnhanceButtonLore("HerbSword", manager.getCurrentSwordLevel(targetItem));
                            updateSellButtonLore("HerbSword", manager.getCurrentSwordLevel(targetItem));
                        }
                    }
                    else if(manager.isSonificationSword(targetItem))   //파동검:출 강화
                    {
                        ItemStack result = manager.upgradeSonificationSword(player, targetItem);

                        if(result == null) return;
                        else
                        {
                            if(result.getType().equals(Material.AIR))
                            {
                                event.getClickedInventory().setItem(weaponSlot, new ItemStack(Material.AIR));
                                targetItem = null;
                                updateEnhanceButtonLore("SonificationSword", 0);
                                updateSellButtonLore("SonificationSword", 0);
                                return;
                            }
                            event.getClickedInventory().setItem(weaponSlot, targetItem);
                            updateEnhanceButtonLore("SonificationSword", manager.getCurrentSwordLevel(targetItem));
                            updateSellButtonLore("SonificationSword", manager.getCurrentSwordLevel(targetItem));
                        }
                    }
                    else if(manager.isEvolveSatelliteSword(targetItem))   //진 위성검 강화
                    {
                        ItemStack result = manager.upgradeEvolveSatelliteSword(player, targetItem);

                        if(result == null) return;
                        else
                        {
                            if(result.getType().equals(Material.AIR))
                            {
                                event.getClickedInventory().setItem(weaponSlot, new ItemStack(Material.AIR));
                                targetItem = null;
                                updateEnhanceButtonLore("EvolveSatelliteSword", 0);
                                updateSellButtonLore("EvolveSatelliteSword", 0);
                                return;
                            }
                            event.getClickedInventory().setItem(weaponSlot, targetItem);
                            updateEnhanceButtonLore("EvolveSatelliteSword", manager.getCurrentSwordLevel(targetItem));
                            updateSellButtonLore("EvolveSatelliteSword", manager.getCurrentSwordLevel(targetItem));
                        }
                    }
                    else if(manager.isSonificationSwordRyu(targetItem))   //파동검:류 강화
                    {
                        ItemStack result = manager.upgradeSonificationSwordRyu(player, targetItem);

                        if(result == null) return;
                        else
                        {
                            if(result.getType().equals(Material.AIR))
                            {
                                event.getClickedInventory().setItem(weaponSlot, new ItemStack(Material.AIR));
                                targetItem = null;
                                updateEnhanceButtonLore("SonificationSwordRyu", 0);
                                updateSellButtonLore("SonificationSwordRyu", 0);
                                return;
                            }
                            event.getClickedInventory().setItem(weaponSlot, targetItem);
                            updateEnhanceButtonLore("SonificationSwordRyu", manager.getCurrentSwordLevel(targetItem));
                            updateSellButtonLore("SonificationSwordRyu", manager.getCurrentSwordLevel(targetItem));
                        }
                    }
                    else if(manager.isBlazingSword(targetItem))   //염화검 강화
                    {
                        ItemStack result = manager.upgradeBlazingSword(player, targetItem);

                        if(result == null) return;
                        else
                        {
                            if(result.getType().equals(Material.AIR))
                            {
                                event.getClickedInventory().setItem(weaponSlot, new ItemStack(Material.AIR));
                                targetItem = null;
                                updateEnhanceButtonLore("BlazingSword", 0);
                                updateSellButtonLore("BlazingSword", 0);
                                return;
                            }
                            event.getClickedInventory().setItem(weaponSlot, targetItem);
                            updateEnhanceButtonLore("BlazingSword", manager.getCurrentSwordLevel(targetItem));
                            updateSellButtonLore("BlazingSword", manager.getCurrentSwordLevel(targetItem));
                        }
                    }
                    else if(manager.isBlindSword(targetItem))   //절안검 강화
                    {
                        ItemStack result = manager.upgradeBlindSword(player, targetItem);

                        if(result == null) return;
                        else
                        {
                            if(result.getType().equals(Material.AIR))
                            {
                                event.getClickedInventory().setItem(weaponSlot, new ItemStack(Material.AIR));
                                targetItem = null;
                                updateEnhanceButtonLore("BlindSword", 0);
                                updateSellButtonLore("BlindSword", 0);
                                return;
                            }
                            event.getClickedInventory().setItem(weaponSlot, targetItem);
                            updateEnhanceButtonLore("BlindSword", manager.getCurrentSwordLevel(targetItem));
                            updateSellButtonLore("BlindSword", manager.getCurrentSwordLevel(targetItem));
                        }
                    }

                    else if(manager.isCounterShield(targetItem))   //반격방패 강화
                    {
                        ItemStack result = manager.upgradeCounterShield(player, targetItem);

                        if(result == null) return;
                        else
                        {
                            if(result.getType().equals(Material.AIR))
                            {
                                event.getClickedInventory().setItem(weaponSlot, new ItemStack(Material.AIR));
                                targetItem = null;
                                updateEnhanceButtonLore("CounterShield", 0);
                                updateSellButtonLore("CounterShield", 0);
                                return;
                            }
                            event.getClickedInventory().setItem(weaponSlot, targetItem);
                            updateEnhanceButtonLore("CounterShield", manager.getCurrentSwordLevel(targetItem));
                            updateSellButtonLore("CounterShield", manager.getCurrentSwordLevel(targetItem));
                        }
                    }
                }

                if(isSellSlot(slot))    //판매버튼 클릭 시
                {
                    if(targetItem == null || !(manager.isWeapon(targetItem)))
                    {
                        player.sendMessage(ChatColor.RED + "판매할 무기가 없거나 판매할 수 없는 아이템입니다.");
                        player.playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 3, 1);
                        return;
                    }

                    manager.sellItem(player, targetItem);

                    event.getClickedInventory().setItem(weaponSlot, new ItemStack(Material.AIR));
                    targetItem = null;
                    updateEnhanceButtonLore("SatelliteDagger", 0);
                    updateSellButtonLore("SatelliteDagger", 0);
                    return;
                }

                if(item.isSimilar(targetItem))  //강화대상 아이템 플레이어 인벤으로 이동
                {
                    manager.safeAddItem(player, item);
                    //player.getInventory().addItem(item);
                    item.setType(Material.AIR);
                    event.getClickedInventory().setItem(weaponSlot, new ItemStack(Material.AIR));
                    targetItem = null;
                    updateEnhanceButtonLore(null, 0);
                    updateSellButtonLore(null, 0);
                }
            }
        }

        if(event.getClickedInventory().getType().equals(InventoryType.PLAYER))
        {
            Player player = (Player) event.getWhoClicked();
            if(player != this.player) return;

            Inventory openInv = player.getOpenInventory().getTopInventory();
            if(openInv.equals(getEnhanceTab())) event.setCancelled(true);

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
                getEnhanceTab().setItem(weaponSlot, targetItem);
                player.getInventory().setItem(event.getSlot(), new ItemStack(Material.AIR));

                if((manager.isSatelliteDagger(targetItem)))
                {
                    updateEnhanceButtonLore("SatelliteDagger", manager.getCurrentSwordLevel(item));
                    updateSellButtonLore("SatelliteDagger", manager.getCurrentSwordLevel(item));
                }
                else if((manager.isSatelliteSword(targetItem)))
                {
                    updateEnhanceButtonLore("SatelliteSword", manager.getCurrentSwordLevel(item));
                    updateSellButtonLore("SatelliteSword", manager.getCurrentSwordLevel(item));
                }
                else if((manager.isRedDwarfSword(targetItem)))
                {
                    updateEnhanceButtonLore("RedDwarfSword", manager.getCurrentSwordLevel(item));
                    updateSellButtonLore("RedDwarfSword", manager.getCurrentSwordLevel(item));
                }
                else if((manager.isHerbSword(targetItem)))
                {
                    updateEnhanceButtonLore("HerbSword", manager.getCurrentSwordLevel(item));
                    updateSellButtonLore("HerbSword", manager.getCurrentSwordLevel(item));
                }
                else if((manager.isSonificationSword(targetItem)))
                {
                    updateEnhanceButtonLore("SonificationSword", manager.getCurrentSwordLevel(item));
                    updateSellButtonLore("SonificationSword", manager.getCurrentSwordLevel(item));
                }
                else if((manager.isEvolveSatelliteSword(targetItem)))
                {
                    updateEnhanceButtonLore("EvolveSatelliteSword", manager.getCurrentSwordLevel(item));
                    updateSellButtonLore("EvolveSatelliteSword", manager.getCurrentSwordLevel(item));
                }
                else if((manager.isSonificationSwordRyu(targetItem)))
                {
                    updateEnhanceButtonLore("SonificationSwordRyu", manager.getCurrentSwordLevel(item));
                    updateSellButtonLore("SonificationSwordRyu", manager.getCurrentSwordLevel(item));
                }
                else if((manager.isBlindSword(targetItem)))
                {
                    updateEnhanceButtonLore("BlindSword", manager.getCurrentSwordLevel(item));
                    updateSellButtonLore("BlindSword", manager.getCurrentSwordLevel(item));
                }
                else if((manager.isBlazingSword(targetItem)))
                {
                    updateEnhanceButtonLore("BlazingSword", manager.getCurrentSwordLevel(item));
                    updateSellButtonLore("BlazingSword", manager.getCurrentSwordLevel(item));
                }

                else if((manager.isCounterShield(targetItem)))
                {
                    updateEnhanceButtonLore("CounterShield", manager.getCurrentSwordLevel(item));
                    updateSellButtonLore("CounterShield", manager.getCurrentSwordLevel(item));
                }

            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        if(event.getInventory().equals(getEnhanceTab()))
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
