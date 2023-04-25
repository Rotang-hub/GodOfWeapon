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
import java.util.List;

public class StatGUI implements Listener
{
    Manager manager;
    Inventory statTab;
    private Player player;

    int[] shieldButtonSlot = {0, 1, 2, 9, 10, 11};
    int[] hpButtonSlot = {3, 4, 5, 12, 13, 14};

    public StatGUI(Manager manager, Player player)
    {
        this.manager = manager;
        this.player = player;
        statTab = Bukkit.createInventory(player, 54, ChatColor.WHITE + "\u7a7a" + "\u8857");
        setButton();
    }

    public Inventory getStatTab()
    {
        return statTab;
    }

    public void setButton()
    {
        setShieldButton(getShieldButton());
        setHpButton(getHpButton());

        updateShieldButtonLore(player);
        updateHpButtonLore(player);
    }

    public void setShieldButton(ItemStack button)
    {
        for(int slot : shieldButtonSlot)
        {
            getStatTab().setItem(slot, button);
        }
    }

    public void setHpButton(ItemStack button)
    {
        for(int slot : hpButtonSlot)
        {
            getStatTab().setItem(slot, button);
        }
    }

    public ItemStack getShieldButton()
    {
        ItemStack button = new ItemStack(Material.MAP);
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + "방어력");
        meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "클릭 시 골드를 소비하여 방어력을 상승시킵니다."));
        meta.setCustomModelData(99);
        button.setItemMeta(meta);

        return button;
    }

    public ItemStack getHpButton()
    {
        ItemStack button = new ItemStack(Material.MAP);
        ItemMeta meta = button.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + "체력");
        meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "클릭 시 골드를 소비하여 최대체력을 상승시킵니다."));
        meta.setCustomModelData(99);

        button.setItemMeta(meta);

        return button;
    }

    public void updateHpButtonLore(Player player)
    {
        ItemStack button = getHpButton();
        ItemMeta meta = button.getItemMeta();
        int health = manager.getHealth(player);
        meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "클릭 시 골드를 소비하여 최대체력을 상승시킵니다.",
                ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "최대체력 상승량: " + ChatColor.DARK_RED + "" + ChatColor.BOLD + health + "->" + (health + 5),
                ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "필요골드: " + ChatColor.GOLD + "" + ChatColor.BOLD + "5000"));
        button.setItemMeta(meta);

        setHpButton(button);
    }

    public void updateShieldButtonLore(Player player)
    {
        ItemStack button = getShieldButton();
        ItemMeta meta = button.getItemMeta();
        int shield = manager.getShield(player);
        int gold = 0;

        if(shield >= 0 && shield <= 19) gold = 5000;
        if(shield >= 20 && shield <= 39) gold = 10000;
        meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "클릭 시 골드를 소비하여 방어력을 상승시킵니다.",
                ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "방어력 상승량: " + ChatColor.BLUE + "" + ChatColor.BOLD + shield + "->" + (shield + 1),
                ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "필요골드: " + ChatColor.GOLD + "" + ChatColor.BOLD + gold));

        if(shield >= 40)
        {
            meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "클릭 시 골드를 소비하여 방어력을 상승시킵니다.",
                    ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "방어력 상승량: " + ChatColor.BLUE + "" + ChatColor.BOLD + shield + "->" + (shield),
                    ChatColor.RESET + "" + ChatColor.BLUE + "" + ChatColor.BOLD + "방어력이 최대로 강화되었습니다."));
        }
        button.setItemMeta(meta);

        setShieldButton(button);
    }

    public boolean isHpSlot(int slot)
    {
        for(int s : hpButtonSlot) if(s == slot) return true;
        return false;
    }
    public boolean isShieldSlot(int slot)
    {
        for(int s : shieldButtonSlot) if(s == slot) return true;
        return false;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if(event.getClickedInventory() == null) return;
        if(event.getClickedInventory().equals(getStatTab()))
        {
            event.setCancelled(true);

            if(event.getClick().equals(ClickType.LEFT))
            {
                Player player = (Player) event.getWhoClicked();
                ItemStack item = event.getCurrentItem();

                if (item == null || item.getType().equals(Material.AIR)) return;

                int slot = event.getSlot();

                //if (item.getType().equals(getShieldButton().getType()))     //방어력 버튼 클릭 시
                if(isShieldSlot(slot))
                {
                    int gold = manager.getGold(player);
                    int shield = manager.getShield(player);

                    if(shield >= 0 && shield <= 19)     //방여력 수치 0~19
                    {
                        if(gold >= 5000)
                        {
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 1);
                            player.sendMessage(ChatColor.GREEN + "방어력이 상승했습니다. [" + shield + " -> " + (shield + 1) + "]");

                            manager.setShield(player, shield + 1);
                            manager.setGold(player, manager.getGold(player) - 5000);    //골드 소비
                            updateShieldButtonLore(player);
                        }
                        else
                        {
                            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 3, 1);
                            player.sendMessage(ChatColor.RED + "스탯 상승에 필요한 골드가 부족합니다.");
                        }
                    }

                    else if(shield >= 20 && shield <= 39)     //방여력 수치 20~39
                    {
                        if(gold >= 10000)
                        {
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 1);
                            player.sendMessage(ChatColor.GREEN + "방어력이 상승했습니다. [" + shield + " -> " + (shield + 1) + "]");

                            manager.setShield(player, shield + 1);
                            manager.setGold(player, manager.getGold(player) - 10000);    //골드 소비
                            updateShieldButtonLore(player);
                        }
                        else
                        {
                            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 3, 1);
                            player.sendMessage(ChatColor.RED + "스탯 상승에 필요한 골드가 부족합니다.");
                        }
                    }

                    else if(shield >= 40)      //방어력 수치 40이상
                    {
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 1);
                        player.sendMessage(ChatColor.BLUE + "방어력이 이미 최대로 강화되었습니다.");
                    }
                }
                //if (item.getType().equals(getHpButton().getType()))     //체력 버튼 클릭 시
                if(isHpSlot(slot))
                {
                    if(manager.getHealth(player) < manager.getHealthLimit())
                    {
                        int gold = manager.getGold(player);

                        if(gold >= 5000)
                        {
                            int hp = manager.getHealth(player);
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 1);
                            player.sendMessage(ChatColor.GREEN + "최대 체력이 상승했습니다. [" + hp + " -> " + (hp + 5) + "]");

                            manager.setHealth(player, hp + 5);   //체력 5증가
                            manager.setGold(player, manager.getGold(player) - 5000);    //골드 소비
                            updateHpButtonLore(player);     //버튼 업데이트
                        }
                        else
                        {
                            player.playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 3, 1);
                            player.sendMessage(ChatColor.RED + "스탯 상승에 필요한 골드가 부족합니다.");
                        }
                    }
                    else
                    {
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3, 1);
                        player.sendMessage(ChatColor.BLUE + "체력이 이미 최대로 강화되었습니다.");
                    }
                }
            }
        }
        if(event.getClickedInventory().getType().equals(InventoryType.PLAYER))
        {
            Player player = (Player) event.getWhoClicked();
            Inventory openInv = player.getOpenInventory().getTopInventory();

            if(player != this.player) return;
            if (openInv.equals(getStatTab())) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        if(event.getInventory().equals(getStatTab()))
        {
            event.getHandlers().unregisterAll(this);
        }
    }
}
