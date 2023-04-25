package io.github.rotanghub.godofweapon.manager;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class Manager
{
    Plugin plugin;
    private DataConfigManager dataConfigManager;
    private PlayerDataConfigManager playerDataConfigManager;
    private OptionConfigManager optionConfigManager;
    private FileConfiguration dataConfig;
    private FileConfiguration playerDataConfig;
    private FileConfiguration optionConfig;


    public boolean process = true;

    private double attackSpeed = -1.4;

    public Manager(Plugin plugin, DataConfigManager dataConfigManager, PlayerDataConfigManager playerDataConfigManager, OptionConfigManager optionConfigManager)
    {
        this.plugin = plugin;
        this.dataConfigManager = dataConfigManager;
        this.playerDataConfigManager = playerDataConfigManager;
        this.optionConfigManager = optionConfigManager;
        this.dataConfig = dataConfigManager.getConfig();
        this.playerDataConfig = playerDataConfigManager.getConfig();
        this.optionConfig = optionConfigManager.getConfig();
    }

    public DataConfigManager getDataConfigManager()
    {
        return dataConfigManager;
    }

    public PlayerDataConfigManager getPlayerDataConfigManager()
    {
        return playerDataConfigManager;
    }

    public ItemStack getCrystal()
    {
        ItemStack crystal = new ItemStack(Material.WHITE_DYE, 1);
        ItemMeta meta = crystal.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + "" + "무색 결정체");
        meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "무기의 강화에 사용되는 아이템입니다"));
        crystal.setItemMeta(meta);

        return crystal;
    }

    public ItemStack getRedCrystal()
    {
        ItemStack crystal = new ItemStack(Material.RED_DYE, 1);
        ItemMeta meta = crystal.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.DARK_RED + "" + ChatColor.BOLD + "홍색 결정체");
        meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "무기의 강화에 사용되는 아이템입니다"));
        crystal.setItemMeta(meta);

        return crystal;
    }

    public ItemStack getBlueCrystal()
    {
        ItemStack crystal = new ItemStack(Material.BLUE_DYE, 1);
        ItemMeta meta = crystal.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BLUE + "" + ChatColor.BOLD + "청색 결정체");
        meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "무기의 강화에 사용되는 아이템입니다"));
        crystal.setItemMeta(meta);

        return crystal;
    }

    public ItemStack getProtector()
    {
        ItemStack protector = new ItemStack(Material.STRING, 1);
        ItemMeta meta = protector.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GREEN + "" + ChatColor.BOLD + "보호주");
        meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "사용 시 무기의 파괴를 방지합니다."));
        protector.setItemMeta(meta);

        return protector;
    }

    public ItemStack getCasinoChip()
    {
        ItemStack chip = new ItemStack(Material.FLINT, 1);
        ItemMeta meta = chip.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + "[RO-CASINO] CHIP");
        chip.setItemMeta(meta);

        return chip;
    }

    public ItemStack getDiscolorDust()
    {
        ItemStack dust = new ItemStack(Material.RAW_COPPER, 1);
        ItemMeta meta = dust.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + "변색가루");
        meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "무색 결정체의 가공에 사용되는 아이템입니다."));
        dust.setItemMeta(meta);

        return dust;
    }

    public ItemStack getSatelliteDagger()
    {
        ItemStack dagger = new ItemStack(Material.WOODEN_SWORD);
        int damage = 3;
        setAttackAttribute(dagger, damage);

        ItemMeta meta = dagger.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + "" + getName("SatelliteDagger", 1) + " 1단계");
        meta.setCustomModelData(getData("SatelliteDagger", 1));
        meta.setUnbreakable(true);
        dagger.setItemMeta(meta);

        return dagger;
    }

    public ItemStack getSatelliteSword()
    {
        ItemStack sword = new ItemStack(Material.WOODEN_HOE);
        setAttackAttribute(sword, 0);

        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + "" + getName("SatelliteSword", 1) + " 1단계");
        meta.setCustomModelData(getData("SatelliteSword", 1));
        meta.setUnbreakable(true);
        sword.setItemMeta(meta);

        return sword;
    }

    public ItemStack getRedDwarfSword()
    {
        ItemStack sword = new ItemStack(Material.WOODEN_AXE);
        setAttackAttribute(sword, 0);
        ItemMeta meta = sword.getItemMeta();

        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + "" + getName("RedDwarfSword", 1) + " 1단계");

        meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "공격 시 " + getCritProb(1) + "%" + "의 확률로 무기 공격력의",
                ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "1.2배의 데미지를 입힙니다."));

        meta.setCustomModelData(getData("RedDwarfSword", 1));
        meta.setUnbreakable(true);
        sword.setItemMeta(meta);

        return sword;
    }

    public ItemStack getHerbSword()
    {
        ItemStack sword = new ItemStack(Material.WOODEN_SHOVEL);
        setAttackAttribute(sword, 0);

        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + "" + getName("HerbSword", 1) + " 1단계");

        meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "무기를 손에 들고 우클릭 시" ,
                ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "체력의 " + getHeal(1) + "%를 회복합니다. (쿨타임:" + getCoolTime("HerbSword") + "초)"));

        meta.setCustomModelData(getData("HerbSword", 1));
        meta.setUnbreakable(true);
        sword.setItemMeta(meta);

        return sword;
    }

    public ItemStack getSonificationSword()
    {
        ItemStack sword = new ItemStack(Material.STONE_SWORD);
        setAttackAttribute(sword, 0);

        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + "" + getName("SonificationSword", 1) + " 1단계");

        meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "무기를 손에 들고 우클릭 시" ,
                ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "전방으로 음파를 날립니다. (쿨타임:" + getCoolTime("SonificationSword") + "초)"));

        meta.setCustomModelData(getData("SonificationSword", 1));
        meta.setUnbreakable(true);
        sword.setItemMeta(meta);

        return sword;
    }

    public ItemStack getSonificationSwordRyu()
    {
        ItemStack sword = new ItemStack(Material.STONE_AXE);
        setAttackAttribute(sword, 0);

        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + "" + getName("SonificationSwordRyu", 1) + " 1단계");

        meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "무기를 우클릭 시 바닥에 검을 꽂아",
                ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + getDuration("SonificationSwordRyu", 1) + "초간 무기 공격력의 30%를 입히는 ",
                        ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "공간을 생성합니다. (쿨타임:" + getCoolTime("SonificationSwordRyu") + "초)"));

        meta.setCustomModelData(getData("SonificationSwordRyu", 1));
        meta.setUnbreakable(true);
        sword.setItemMeta(meta);

        return sword;
    }

    public ItemStack getEvolveSatelliteSword()
    {
        ItemStack sword = new ItemStack(Material.STONE_HOE);
        setAttackAttribute(sword, 0);

        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + "" + getName("EvolveSatelliteSword", 1) + " 1단계");

        meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "무기를 손에 들고 우클릭 시" ,
                ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "전방으로 짧게 대쉬합니다 (쿨타임:" + getCoolTime("EvolveSatelliteSword") + "초)"));

        meta.setCustomModelData(getData("EvolveSatelliteSword", 1));
        meta.setUnbreakable(true);
        sword.setItemMeta(meta);

        return sword;
    }

    public ItemStack getBlindSword()
    {
        ItemStack sword = new ItemStack(Material.STONE_SHOVEL);
        setAttackAttribute(sword, 0);

        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + "" + getName("BlindSword", 1) + " 1단계");

        meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "무기를 손에 들고 상대방을 우클릭 시" ,
                ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "상대방의 시야를 " + getDuration("BlindSword", 1) +
                        "초 동안 감소시킵니다. (쿨타임:" + getCoolTime("BlindSword") + "초)"));

        meta.setCustomModelData(getData("BlazingSword", 1));
        meta.setUnbreakable(true);
        sword.setItemMeta(meta);

        return sword;
    }

    public ItemStack getBlazingSword()
    {
        ItemStack sword = new ItemStack(Material.IRON_SWORD);
        setAttackAttribute(sword, 0);

        ItemMeta meta = sword.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + "" + getName("BlazingSword", 1) + " 1단계");

        meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "무기를 손에 들고 상대방을 우클릭 시" ,
                ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "2초마다 무기 공격력의 50%를 ",
                ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + getHit(1) + "회 입히는 블꽃을 부착합니다. (쿨타임:" + getCoolTime("BlazingSword") + "초)"));

        meta.setCustomModelData(getData("BlazingSword", 1));
        meta.setUnbreakable(true);
        sword.setItemMeta(meta);

        return sword;
    }

    public ItemStack getCounterShield()
    {
        ItemStack shield = new ItemStack(Material.SHIELD);

        ItemMeta meta = shield.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + "" + getName("CounterShield", 1) + " 1단계");

        meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "공격 방어에 성공 시 받은 공격력의 " + getCounter(1) + "%를 반사하고 ",
                ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "상대방을 " + getKnockBack(1) + "만큼 넉백시킵니다. (쿨타임:" + getCoolTime("CounterShield") + "초)"));

        meta.setCustomModelData(getData("CounterShield", 1));
        meta.setUnbreakable(true);
        shield.setItemMeta(meta);

        return shield;
    }

    public int getMaxLevel(String type)
    {
        return dataConfig.getInt(type + ".maxLevel");
    }

    public String getName(String type, int level)
    {
        return dataConfig.getString(type + ".upgrade." + level + ".name");
    }

    public int getData(String type, int level)
    {
        return dataConfig.getInt(type + ".upgrade." + level + ".data");
    }

    public int getConsume(String type, int level)
    {
        return dataConfig.getInt(type + ".upgrade." + level + ".consume");
    }

    public int getProb(String type, int level)
    {
        return dataConfig.getInt(type + ".upgrade." + level + ".prob");
    }

    public int getStatMin(String type, int level)
    {
        return dataConfig.getInt(type + ".upgrade." + level + ".statMin");
    }

    public int getStatMax(String type, int level)
    {
        return dataConfig.getInt(type + ".upgrade." + level + ".statMax");
    }

    public int getPrice(String type, int level)
    {
        return dataConfig.getInt(type + ".upgrade." + level + ".price");
    }

    public int getCritProb(int level)
    {
        return dataConfig.getInt("RedDwarfSword.upgrade." + level + ".critProb");
    }

    public int getHeal(int level)
    {
        return dataConfig.getInt("HerbSword.upgrade." + level + ".heal");
    }

    public int getCounter(int level)
    {
        return dataConfig.getInt("CounterShield.upgrade." + level + ".counter");
    }

    public int getKnockBack(int level)
    {
        return dataConfig.getInt("CounterShield.upgrade." + level + ".knockback");
    }

    public int getDuration(String type, int level)
    {
        return dataConfig.getInt(type + ".upgrade." + level + ".duration");
    }

    public int getHit(int level)
    {
        return dataConfig.getInt("BlazingSword.upgrade." + level + ".hit");
    }
    public int getCoolTime(String type)
    {
        return dataConfig.getInt(type + ".cooltime");
    }

    public ItemStack upgradeSatelliteDagger(Player player, ItemStack item)  //위성단검
    {
        if(isSatelliteDagger(item))
        {
            int level = getCurrentSwordLevel(item);
            int maxLevel = getMaxLevel("SatelliteDagger");

            String name = getName("SatelliteDagger", level);
            int data = getData("SatelliteDagger", level);
            int consume = getConsume("SatelliteDagger", level);
            int prob = getProb("SatelliteDagger", level);
            int statMin = getStatMin("SatelliteDagger", level);
            int statMax = getStatMax("SatelliteDagger", level);

            if(!hasCrystal(player, consume))
            {
                player.sendMessage(ChatColor.RED + "강화에 필요한 재료를 가지고 있지 않습니다. [필요량:무색 결정체 " + consume + "개]");
                player.playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 5, 1);
                return null;
            }

            consumeCrystal(player, consume);    //결정체 소비

            if(!isSuccess(prob))
            {
                if(hasProtector(player, 1))
                {
                    player.sendMessage(ChatColor.RED + "강화에 실패하여 보호주가 1개 소모됩니다.");
                    player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 5, 1);
                    consumeProtector(player, 1);
                    return item;
                }

                if(isBroken())  //10%
                {
                    player.sendMessage(ChatColor.RED + "강화에 실패하여 아이템이 파괴되었습니다.");
                    player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 5, 0.4f);
                    item.setType(Material.AIR);
                }
                else
                {
                    int downStat = downgradeWeapon("SatelliteDagger", item);

                    player.sendMessage(ChatColor.RED + "강화에 실패했습니다. [스탯 감소량: -" + downStat + "]");
                    player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 5, 0.4f);
                }
                return item;
            }

            int stat = getUpgradeStat(statMin, statMax);

            if(level == maxLevel)
            {
                item = daggerToSatellite(item);

                addAttackAttribute(item, stat);
                ItemMeta meta = item.getItemMeta();

                meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + "" + getName("SatelliteSword", 1) + " 1단계");
                meta.setCustomModelData(getData("SatelliteSword", 1));
                item.setItemMeta(meta);

                updateLore(item, "SatelliteSword", 1);

                player.sendMessage(ChatColor.GREEN + "강화에 성공했습니다. [스탯 증가량: +" + stat + "]");
                player.sendMessage(ChatColor.GREEN + "위성단검 -> 위성검 진화에 성공했습니다!");
                player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 5, 1);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5, 1);

                return item;
            }

            addAttackAttribute(item, stat);
            ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + name + " " + (level + 1) + "단계");
            meta.setCustomModelData(data);
            item.setItemMeta(meta);

            updateLore(item, "SatelliteDagger", level + 1);

            player.sendMessage(ChatColor.GREEN + "강화에 성공했습니다. [스탯 증가량: +" + stat + "]");
            player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 5, 1);

            return item;
        }
        return null;
    }

    public ItemStack upgradeSatelliteSword(Player player, ItemStack item)   //위성검
    {
        if(isSatelliteSword(item))
        {
            int level = getCurrentSwordLevel(item);
            int maxLevel = getMaxLevel("SatelliteSword");

            String name = getName("SatelliteSword", level);
            int data = getData("SatelliteSword", level);
            int consume = getConsume("SatelliteSword", level);
            int prob = getProb("SatelliteSword", level);
            int statMin = getStatMin("SatelliteSword", level);
            int statMax = getStatMax("SatelliteSword", level);

            /*
            if(level == maxLevel)
            {
                player.sendMessage(ChatColor.RED + "이미 최대로 강화된 무기입니다.");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 5, 1);
                return null;
            }
             */

            if(!hasCrystal(player, consume))
            {
                player.sendMessage(ChatColor.RED + "강화에 필요한 재료를 가지고 있지 않습니다. [필요량:무색 결정체 " + consume + "개]");
                player.playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 5, 1);
                return null;
            }

            consumeCrystal(player, consume);    //결정체 소비

            if(!isSuccess(prob))
            {
                if(hasProtector(player, 1))
                {
                    player.sendMessage(ChatColor.RED + "강화에 실패하여 보호주가 1개 소모됩니다.");
                    player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 5, 1);
                    consumeProtector(player, 1);
                    return item;
                }

                if(isBroken())  //10%
                {
                    player.sendMessage(ChatColor.RED + "강화에 실패하여 아이템이 파괴되었습니다.");
                    player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 5, 0.4f);
                    item.setType(Material.AIR);
                }
                else
                {
                    int downStat = downgradeWeapon("SatelliteSword", item);

                    player.sendMessage(ChatColor.RED + "강화에 실패했습니다. [스탯 감소량: -" + downStat + "]");
                    player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 5, 0.4f);
                }
                return item;
            }

            int stat = getUpgradeStat(statMin, statMax);

            if(level == maxLevel)
            {
                item = satelliteToEvolve(item);

                addAttackAttribute(item, stat);
                ItemMeta meta = item.getItemMeta();

                meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + "" + getName("EvolveSatelliteSword", 1) + " 1단계");
                meta.setCustomModelData(getData("EvolveSatelliteSword", 1));
                item.setItemMeta(meta);

                updateLore(item, "EvolveSatelliteSword", 1);

                player.sendMessage(ChatColor.GREEN + "강화에 성공했습니다. [스탯 증가량: +" + stat + "]");
                player.sendMessage(ChatColor.GREEN + "위성검 -> 진 위성검 진화에 성공했습니다!");
                player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 5, 1);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 5, 1);

                return item;
            }

            addAttackAttribute(item, stat);

            ItemMeta meta = item.getItemMeta();

            //이름*모델데이터
            meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + name + " " + (level + 1) + "단계");
            meta.setCustomModelData(data);
            item.setItemMeta(meta);

            updateLore(item, "SatelliteSword", level + 1);

            player.sendMessage(ChatColor.GREEN + "강화에 성공했습니다. [스탯 증가량: +" + stat + "]");
            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 5, 1);

            return item;
        }
        return null;
    }

    public ItemStack upgradeRedDwarfSword(Player player, ItemStack item)    //홍성검
    {
        if(isRedDwarfSword(item))
        {
            int level = getCurrentSwordLevel(item);
            int maxLevel = getMaxLevel("RedDwarfSword");

            String name = getName("RedDwarfSword", level);
            int data = getData("RedDwarfSword", level);
            int consume = getConsume("RedDwarfSword", level);
            int prob = getProb("RedDwarfSword", level);
            int statMin = getStatMin("RedDwarfSword", level);
            int statMax = getStatMax("RedDwarfSword", level);

            if(level == maxLevel)
            {
                player.sendMessage(ChatColor.RED + "이미 최대로 강화된 무기입니다.");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 5, 1);
                return null;
            }

            if(!hasRedCrystal(player, consume))
            {
                player.sendMessage(ChatColor.RED + "강화에 필요한 재료를 가지고 있지 않습니다. [필요량:홍색 결정체 " + consume + "개]");
                player.playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 5, 1);
                return null;
            }

            consumeRedCrystal(player, consume);     //결정체 소비

            if(!isSuccess(prob))
            {
                if(hasProtector(player, 1))
                {
                    player.sendMessage(ChatColor.RED + "강화에 실패하여 보호주가 1개 소모됩니다.");
                    player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 5, 1);
                    consumeProtector(player, 1);
                    return item;
                }

                if(isBroken())  //10%
                {
                    player.sendMessage(ChatColor.RED + "강화에 실패하여 아이템이 파괴되었습니다.");
                    player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 5, 0.4f);
                    item.setType(Material.AIR);
                }
                else
                {
                    int downStat = downgradeWeapon("RedDwarfSword", item);

                    player.sendMessage(ChatColor.RED + "강화에 실패했습니다. [스탯 감소량: -" + downStat + "]");
                    player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 5, 0.4f);
                }
                return item;
            }

            int stat = getUpgradeStat(statMin, statMax);

            addAttackAttribute(item, stat);
            ItemMeta meta = item.getItemMeta();

            //이름*모델데이터
            meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + name + " " + (level + 1) + "단계");
            meta.setCustomModelData(data);
            item.setItemMeta(meta);

            updateLore(item, "RedDwarfSword", level + 1);   //lore 업데이트

            player.sendMessage(ChatColor.GREEN + "강화에 성공했습니다. [스탯 증가량: +" + stat + "]");
            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 5, 1);

            return item;
        }
        return null;
    }

    public ItemStack upgradeHerbSword(Player player, ItemStack item)   //초섬검
    {
        if(isHerbSword(item))
        {
            int level = getCurrentSwordLevel(item);
            int maxLevel = getMaxLevel("HerbSword");

            String name = getName("HerbSword", level);
            int data = getData("HerbSword", level);
            int consume = getConsume("HerbSword", level);
            int prob = getProb("HerbSword", level);
            int statMin = getStatMin("HerbSword", level);
            int statMax = getStatMax("HerbSword", level);

            if(level == maxLevel)
            {
                player.sendMessage(ChatColor.RED + "이미 최대로 강화된 무기입니다.");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 5, 1);
                return null;
            }

            if(!hasCrystal(player, consume))
            {
                player.sendMessage(ChatColor.RED + "강화에 필요한 재료를 가지고 있지 않습니다. [필요량:무색 결정체 " + consume + "개]");
                player.playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 5, 1);
                return null;
            }

            consumeCrystal(player, consume);    //결정체 소비

            if(!isSuccess(prob))
            {
                if(hasProtector(player, 1))
                {
                    player.sendMessage(ChatColor.RED + "강화에 실패하여 보호주가 1개 소모됩니다.");
                    player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 5, 1);
                    consumeProtector(player, 1);
                    return item;
                }

                if(isBroken())  //10%
                {
                    player.sendMessage(ChatColor.RED + "강화에 실패하여 아이템이 파괴되었습니다.");
                    player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 5, 0.4f);
                    item.setType(Material.AIR);
                }
                else
                {
                    int downStat = downgradeWeapon("HerbSword", item);

                    player.sendMessage(ChatColor.RED + "강화에 실패했습니다. [스탯 감소량: -" + downStat + "]");
                    player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 5, 0.4f);
                }
                return item;
            }

            int stat = getUpgradeStat(statMin, statMax);
            addAttackAttribute(item, stat);
            ItemMeta meta = item.getItemMeta();

            //이름*모델데이터
            meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + name + " " + (level + 1) + "단계");
            meta.setCustomModelData(data);
            item.setItemMeta(meta);

            updateLore(item, "HerbSword", level + 1);

            player.sendMessage(ChatColor.GREEN + "강화에 성공했습니다. [스탯 증가량: +" + stat + "]");
            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 5, 1);

            return item;
        }
        return null;
    }

    public ItemStack upgradeSonificationSword(Player player, ItemStack item)   //음화검
    {
        if(isSonificationSword(item))
        {
            int level = getCurrentSwordLevel(item);
            int maxLevel = getMaxLevel("SonificationSword");

            String name = getName("SonificationSword", level);
            int data = getData("SonificationSword", level);
            int consume = getConsume("SonificationSword", level);
            int prob = getProb("SonificationSword", level);
            int statMin = getStatMin("SonificationSword", level);
            int statMax = getStatMax("SonificationSword", level);

            if(level == maxLevel)
            {
                player.sendMessage(ChatColor.RED + "이미 최대로 강화된 무기입니다.");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 5, 1);
                return null;
            }

            if(!hasRedCrystal(player, consume))
            {
                player.sendMessage(ChatColor.RED + "강화에 필요한 재료를 가지고 있지 않습니다. [필요량:홍색 결정체 " + consume + "개]");
                player.playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 5, 1);
                return null;
            }

            consumeRedCrystal(player, consume);    //홍색 결정체 소비

            if(!isSuccess(prob))
            {
                if(hasProtector(player, 1))
                {
                    player.sendMessage(ChatColor.RED + "강화에 실패하여 보호주가 1개 소모됩니다.");
                    player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 5, 1);
                    consumeProtector(player, 1);
                    return item;
                }

                if(isBroken())  //10%
                {
                    player.sendMessage(ChatColor.RED + "강화에 실패하여 아이템이 파괴되었습니다.");
                    player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 5, 0.4f);
                    item.setType(Material.AIR);
                }
                else
                {
                    int downStat = downgradeWeapon("SonificationSword", item);

                    player.sendMessage(ChatColor.RED + "강화에 실패했습니다. [스탯 감소량: -" + downStat + "]");
                    player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 5, 0.4f);
                }
                return item;
            }

            int stat = getUpgradeStat(statMin, statMax);
            addAttackAttribute(item, stat);
            ItemMeta meta = item.getItemMeta();

            //이름*모델데이터
            meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + name + " " + (level + 1) + "단계");
            meta.setCustomModelData(data);
            item.setItemMeta(meta);

            updateLore(item, "SonificationSword", level + 1);

            player.sendMessage(ChatColor.GREEN + "강화에 성공했습니다. [스탯 증가량: +" + stat + "]");
            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 5, 1);

            return item;
        }
        return null;
    }

    public ItemStack upgradeEvolveSatelliteSword(Player player, ItemStack item)   //진 위성검
    {
        if(isEvolveSatelliteSword(item))
        {
            int level = getCurrentSwordLevel(item);
            int maxLevel = getMaxLevel("EvolveSatelliteSword");

            String name = getName("EvolveSatelliteSword", level);
            int data = getData("EvolveSatelliteSword", level);
            int consume = getConsume("EvolveSatelliteSword", level);
            int prob = getProb("EvolveSatelliteSword", level);
            int statMin = getStatMin("EvolveSatelliteSword", level);
            int statMax = getStatMax("EvolveSatelliteSword", level);

            if(level == maxLevel)
            {
                player.sendMessage(ChatColor.RED + "이미 최대로 강화된 무기입니다.");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 5, 1);
                return null;
            }

            if(!hasBlueCrystal(player, consume))
            {
                player.sendMessage(ChatColor.RED + "강화에 필요한 재료를 가지고 있지 않습니다. [필요량:청색 결정체 " + consume + "개]");
                player.playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 5, 1);
                return null;
            }

            consumeBlueCrystal(player, consume);    //결정체 소비

            if(!isSuccess(prob))
            {
                if(hasProtector(player, 1))
                {
                    player.sendMessage(ChatColor.RED + "강화에 실패하여 보호주가 1개 소모됩니다.");
                    player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 5, 1);
                    consumeProtector(player, 1);
                    return item;
                }

                if(isBroken())  //10%
                {
                    player.sendMessage(ChatColor.RED + "강화에 실패하여 아이템이 파괴되었습니다.");
                    player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 5, 0.4f);
                    item.setType(Material.AIR);
                }
                else
                {
                    int downStat = downgradeWeapon("EvolveSatelliteSword", item);

                    player.sendMessage(ChatColor.RED + "강화에 실패했습니다. [스탯 감소량: -" + downStat + "]");
                    player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 5, 0.4f);
                }
                return item;
            }

            int stat = getUpgradeStat(statMin, statMax);
            addAttackAttribute(item, stat);
            ItemMeta meta = item.getItemMeta();

            //이름*모델데이터
            meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + name + " " + (level + 1) + "단계");
            meta.setCustomModelData(data);
            item.setItemMeta(meta);

            updateLore(item, "EvolveSatelliteSword", level + 1);

            player.sendMessage(ChatColor.GREEN + "강화에 성공했습니다. [스탯 증가량: +" + stat + "]");
            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 5, 1);

            return item;
        }
        return null;
    }

    public ItemStack upgradeSonificationSwordRyu(Player player, ItemStack item)   //파동검:류
    {
        if(isSonificationSwordRyu(item))
        {
            int level = getCurrentSwordLevel(item);
            int maxLevel = getMaxLevel("SonificationSwordRyu");

            String name = getName("SonificationSwordRyu", level);
            int data = getData("SonificationSwordRyu", level);
            int consume = getConsume("SonificationSwordRyu", level);
            int prob = getProb("SonificationSwordRyu", level);
            int statMin = getStatMin("SonificationSwordRyu", level);
            int statMax = getStatMax("SonificationSwordRyu", level);

            if(level == maxLevel)
            {
                player.sendMessage(ChatColor.RED + "이미 최대로 강화된 무기입니다.");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 5, 1);
                return null;
            }

            if(!hasRedCrystal(player, consume))
            {
                player.sendMessage(ChatColor.RED + "강화에 필요한 재료를 가지고 있지 않습니다. [필요량:홍색 결정체 " + consume + "개]");
                player.playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 5, 1);
                return null;
            }

            consumeRedCrystal(player, consume);    //결정체 소비

            if(!isSuccess(prob))
            {
                if(hasProtector(player, 1))
                {
                    player.sendMessage(ChatColor.RED + "강화에 실패하여 보호주가 1개 소모됩니다.");
                    player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 5, 1);
                    consumeProtector(player, 1);
                    return item;
                }

                if(isBroken())  //10%
                {
                    player.sendMessage(ChatColor.RED + "강화에 실패하여 아이템이 파괴되었습니다.");
                    player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 5, 0.4f);
                    item.setType(Material.AIR);
                }
                else
                {
                    int downStat = downgradeWeapon("SonificationSwordRyu", item);

                    player.sendMessage(ChatColor.RED + "강화에 실패했습니다. [스탯 감소량: -" + downStat + "]");
                    player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 5, 0.4f);
                }
                return item;
            }

            int stat = getUpgradeStat(statMin, statMax);
            addAttackAttribute(item, stat);
            ItemMeta meta = item.getItemMeta();

            //이름*모델데이터
            meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + name + " " + (level + 1) + "단계");
            meta.setCustomModelData(data);
            item.setItemMeta(meta);

            updateLore(item, "SonificationSwordRyu", level + 1);

            player.sendMessage(ChatColor.GREEN + "강화에 성공했습니다. [스탯 증가량: +" + stat + "]");
            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 5, 1);

            return item;
        }
        return null;
    }

    public ItemStack upgradeBlazingSword(Player player, ItemStack item)   //진 위성검
    {
        if(isBlazingSword(item))
        {
            int level = getCurrentSwordLevel(item);
            int maxLevel = getMaxLevel("BlazingSword");

            String name = getName("BlazingSword", level);
            int data = getData("BlazingSword", level);
            int consume = getConsume("BlazingSword", level);
            int prob = getProb("BlazingSword", level);
            int statMin = getStatMin("BlazingSword", level);
            int statMax = getStatMax("BlazingSword", level);

            if(level == maxLevel)
            {
                player.sendMessage(ChatColor.RED + "이미 최대로 강화된 무기입니다.");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 5, 1);
                return null;
            }

            if(!hasBlueCrystal(player, consume))
            {
                player.sendMessage(ChatColor.RED + "강화에 필요한 재료를 가지고 있지 않습니다. [필요량:청색 결정체 " + consume + "개]");
                player.playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 5, 1);
                return null;
            }

            consumeBlueCrystal(player, consume);    //결정체 소비

            if(!isSuccess(prob))
            {
                if(hasProtector(player, 1))
                {
                    player.sendMessage(ChatColor.RED + "강화에 실패하여 보호주가 1개 소모됩니다.");
                    player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 5, 1);
                    consumeProtector(player, 1);
                    return item;
                }

                if(isBroken())  //10%
                {
                    player.sendMessage(ChatColor.RED + "강화에 실패하여 아이템이 파괴되었습니다.");
                    player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 5, 0.4f);
                    item.setType(Material.AIR);
                }
                else
                {
                    int downStat = downgradeWeapon("BlazingSword", item);

                    player.sendMessage(ChatColor.RED + "강화에 실패했습니다. [스탯 감소량: -" + downStat + "]");
                    player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 5, 0.4f);
                }
                return item;
            }

            int stat = getUpgradeStat(statMin, statMax);
            addAttackAttribute(item, stat);
            ItemMeta meta = item.getItemMeta();

            //이름*모델데이터
            meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + name + " " + (level + 1) + "단계");
            meta.setCustomModelData(data);
            item.setItemMeta(meta);

            updateLore(item, "BlazingSword", level + 1);

            player.sendMessage(ChatColor.GREEN + "강화에 성공했습니다. [스탯 증가량: +" + stat + "]");
            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 5, 1);

            return item;
        }
        return null;
    }

    public ItemStack upgradeBlindSword(Player player, ItemStack item)   //진 위성검
    {
        if(isBlindSword(item))
        {
            int level = getCurrentSwordLevel(item);
            int maxLevel = getMaxLevel("BlindSword");

            String name = getName("BlindSword", level);
            int data = getData("BlindSword", level);
            int consume = getConsume("BlindSword", level);
            int prob = getProb("BlindSword", level);
            int statMin = getStatMin("BlindSword", level);
            int statMax = getStatMax("BlindSword", level);

            if(level == maxLevel)
            {
                player.sendMessage(ChatColor.RED + "이미 최대로 강화된 무기입니다.");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 5, 1);
                return null;
            }

            if(!hasRedCrystal(player, consume))
            {
                player.sendMessage(ChatColor.RED + "강화에 필요한 재료를 가지고 있지 않습니다. [필요량:홍색 결정체 " + consume + "개]");
                player.playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 5, 1);
                return null;
            }

            consumeRedCrystal(player, consume);    //결정체 소비

            if(!isSuccess(prob))
            {
                if(hasProtector(player, 1))
                {
                    player.sendMessage(ChatColor.RED + "강화에 실패하여 보호주가 1개 소모됩니다.");
                    player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 5, 1);
                    consumeProtector(player, 1);
                    return item;
                }

                if(isBroken())  //10%
                {
                    player.sendMessage(ChatColor.RED + "강화에 실패하여 아이템이 파괴되었습니다.");
                    player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 5, 0.4f);
                    item.setType(Material.AIR);
                }
                else
                {
                    int downStat = downgradeWeapon("BlindSword", item);

                    player.sendMessage(ChatColor.RED + "강화에 실패했습니다. [스탯 감소량: -" + downStat + "]");
                    player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 5, 0.4f);
                }
                return item;
            }

            int stat = getUpgradeStat(statMin, statMax);
            addAttackAttribute(item, stat);
            ItemMeta meta = item.getItemMeta();

            //이름*모델데이터
            meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + name + " " + (level + 1) + "단계");
            meta.setCustomModelData(data);
            item.setItemMeta(meta);

            updateLore(item, "BlindSword", level + 1);

            player.sendMessage(ChatColor.GREEN + "강화에 성공했습니다. [스탯 증가량: +" + stat + "]");
            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 5, 1);

            return item;
        }
        return null;
    }

    public ItemStack upgradeCounterShield(Player player, ItemStack item)
    {
        if(isCounterShield(item))
        {
            int level = getCurrentSwordLevel(item);
            int maxLevel = getMaxLevel("CounterShield");

            String name = getName("CounterShield", level);
            int data = getData("CounterShield", level);
            int consume = getConsume("CounterShield", level);
            int prob = getProb("CounterShield", level);

            if(level == maxLevel)
            {
                player.sendMessage(ChatColor.RED + "이미 최대로 강화된 무기입니다.");
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 5, 1);
                return null;
            }

            if(!hasCrystal(player, consume))
            {
                player.sendMessage(ChatColor.RED + "강화에 필요한 재료를 가지고 있지 않습니다. [필요량:무색 결정체 " + consume + "개]");
                player.playSound(player.getLocation(), Sound.ENTITY_LLAMA_SPIT, 5, 1);
                return null;
            }

            consumeCrystal(player, consume);    //결정체 소비

            if(!isSuccess(prob))
            {
                if(hasProtector(player, 1))
                {
                    player.sendMessage(ChatColor.RED + "강화에 실패하여 보호주가 1개 소모됩니다.");
                    player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 5, 1);
                    consumeProtector(player, 1);
                    return item;
                }

                if(isBroken())  //10%
                {
                    player.sendMessage(ChatColor.RED + "강화에 실패하여 아이템이 파괴되었습니다.");
                    player.playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 5, 0.4f);
                    item.setType(Material.AIR);
                }
                else
                {
                    downgradeWeapon("CounterShield", item);

                    player.sendMessage(ChatColor.RED + "강화에 실패했습니다.");
                    player.playSound(player.getLocation(), Sound.BLOCK_CHAIN_PLACE, 5, 0.4f);
                }
                return item;
            }
            ItemMeta meta = item.getItemMeta();

            //이름*모델데이터
            meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + name + " " + (level + 1) + "단계");
            meta.setCustomModelData(data);
            item.setItemMeta(meta);

            updateLore(item, "CounterShield", level + 1);

            player.sendMessage(ChatColor.GREEN + "강화에 성공했습니다.");
            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 5, 1);

            return item;
        }
        return null;
    }

    public int downgradeWeapon(String type, ItemStack item)
    {
        int level = getCurrentSwordLevel(item);

        if(level == 1) return 0;

        String name = getName(type, level);

        if(isCounterShield(item))
        {
            updateLore(item, type, level - 1);

            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + name + " " + (level - 1) + "단계");
            item.setItemMeta(meta);

            return 0;
        }

        updateLore(item, type, level - 1);

        int stat = getUpgradeStat(getStatMin(type, level - 1), getStatMax(type, level - 1));
        addAttackAttribute(item, stat * (-1));

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.BOLD + name + " " + (level - 1) + "단계");

        item.setItemMeta(meta);

        return stat;
    }

    public void updateLore(ItemStack item, String type, int level)
    {
        int coolTime = getCoolTime(type);
        ItemMeta meta = item.getItemMeta();

        if(isSatelliteDagger(item))
        {
            if(level == 3)
            {
                meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "무기를 손에 들고 우클릭 시",
                    ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "이동속도가 잠시 증가합니다 (쿨타임:" + coolTime + "초)"));
            }
            else
            {
                meta.setLore(null);
            }
        }
        if(isSatelliteSword(item))
        {
            if(level <= 2)
            {
                meta.setLore(null);
            }
            else
            {
                meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "무기를 손에 들고 우클릭 시" ,
                        ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "전방으로 짧게 대쉬합니다 (쿨타임:" + coolTime + "초)"));
            }
        }
        if(isEvolveSatelliteSword(item))
        {
            meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "무기를 손에 들고 우클릭 시" ,
                    ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "전방으로 짧게 대쉬합니다 (쿨타임:" + coolTime + "초)"));
        }
        if(isRedDwarfSword(item))
        {
            int critProb = getCritProb(level);
            meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "공격 시 " + critProb + "%" + "의 확률로 무기 공격력의",
                    ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "1.2배의 데미지를 입힙니다"));
        }
        if(isHerbSword(item))
        {
            int heal = getHeal(level);
            meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "무기를 손에 들고 우클릭 시" ,
                    ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "체력의 " + heal + "%를 회복합니다. (쿨타임:" + coolTime + "초)"));
        }
        if(isSonificationSword(item))
        {
            meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "무기를 손에 들고 우클릭 시" ,
                    ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "전방으로 음파를 날립니다. (쿨타임:" + coolTime + "초)"));
        }
        if(isSonificationSwordRyu(item))
        {
            int duration =  getDuration("SonificationSwordRyu", level);
            meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "무기를 우클릭 시 바닥에 검을 꽂아",
                    ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + duration + "초간 무기 공격력의 30%를 입히는 ",
                    ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "공간을 생성합니다. (쿨타임:" + coolTime + "초)"));
        }
        if(isBlindSword(item))
        {
            int duration =  getDuration("BlindSword", level);
            meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "무기를 손에 들고 상대방을 우클릭 시" ,
                    ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "상대방의 시야를 " + duration +
                            "초 동안 감소시킵니다. (쿨타임:" + coolTime + "초)"));
        }
        if(isBlazingSword(item))
        {
            int hit = getHit(level);
            meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "무기를 손에 들고 상대방을 우클릭 시" ,
                    ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "2초마다 무기 공격력의 50%를 ",
                    ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + level + "회 입히는 불꽃을 부착합니다. (쿨타임:" + coolTime + "초)"));
        }
        if(isCounterShield(item))
        {
            int counter = getCounter(level);
            int knockBack = getKnockBack(level);
            meta.setLore(Arrays.asList(ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "" + "공격 방어에 성공 시 받은 공격력의 " + counter + "%를 반사하고 ",
                    ChatColor.RESET + "" + ChatColor.WHITE + "" + ChatColor.BOLD + "상대방을 " + knockBack + "만큼 넉백시킵니다. (쿨타임:" + coolTime + "초)"));
        }

        item.setItemMeta(meta);
    }

    public void setAttackAttribute(ItemStack item, int stat)
    {
        ItemMeta meta = item.getItemMeta();

        AttributeModifier attackModifier = new AttributeModifier(UUID.randomUUID(), "generic.attackDamage",
                stat, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE, attackModifier);

        //공속 어트리뷰트
        AttributeModifier speedModifier = new AttributeModifier(UUID.randomUUID(), "generic.attackSpeed",
                this.attackSpeed, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND);
        meta.removeAttributeModifier(Attribute.GENERIC_ATTACK_SPEED);
        meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED, speedModifier);

        item.setItemMeta(meta);
    }

    public void addAttackAttribute(ItemStack item, int stat)
    {
        int currentStat = getWeaponDamage(item);
        setAttackAttribute(item, stat + currentStat);
    }

    public boolean isSatelliteDagger(ItemStack item)
    {
        String name = getName("SatelliteDagger", 1);
        return (item.getType().equals(getSatelliteDagger().getType())
                && item.hasItemMeta()
                && item.getItemMeta().getDisplayName().contains(name));
    }

    public boolean isSatelliteSword(ItemStack item)
    {
        String name = getName("SatelliteSword", 1);
        return (item.getType().equals(getSatelliteSword().getType())
                && item.hasItemMeta()
                && item.getItemMeta().getDisplayName().contains(name));
    }

    public boolean isRedDwarfSword(ItemStack item)
    {
        String name = getName("RedDwarfSword", 1);
        return (item.getType().equals(getRedDwarfSword().getType())
                && item.hasItemMeta()
                && item.getItemMeta().getDisplayName().contains(name));
    }

    public boolean isHerbSword(ItemStack item)
    {
        String name = getName("HerbSword", 1);
        return (item.getType().equals(getHerbSword().getType())
                && item.hasItemMeta()
                && item.getItemMeta().getDisplayName().contains(name));
    }

    public boolean isSonificationSword(ItemStack item)
    {
        String name = getName("SonificationSword", 1);
        return (item.getType().equals(getSonificationSword().getType())
                && item.hasItemMeta()
                && (item.getItemMeta().getDisplayName().contains(name) || item.getItemMeta().getDisplayName().contains("파동검")));
    }

    public boolean isEvolveSatelliteSword(ItemStack item)
    {
        String name = getName("EvolveSatelliteSword", 1);
        return (item.getType().equals(getEvolveSatelliteSword().getType())
                && item.hasItemMeta()
                && item.getItemMeta().getDisplayName().contains(name));
    }

    public boolean isSonificationSwordRyu(ItemStack item)
    {
        String name = getName("SonificationSwordRyu", 1);
        return (item.getType().equals(getSonificationSwordRyu().getType())
                && item.hasItemMeta()
                && item.getItemMeta().getDisplayName().contains(name));
    }

    public boolean isBlazingSword(ItemStack item)
    {
        String name = getName("BlazingSword", 1);
        return (item.getType().equals(getBlazingSword().getType())
                && item.hasItemMeta()
                && item.getItemMeta().getDisplayName().contains(name));
    }

    public boolean isBlindSword(ItemStack item)
    {
        String name = getName("BlindSword", 1);
        return (item.getType().equals(getBlindSword().getType())
                && item.hasItemMeta()
                && item.getItemMeta().getDisplayName().contains(name));
    }

    public boolean isCounterShield(ItemStack item)
    {
        String name = getName("CounterShield", 1);
        return (item.getType().equals(getCounterShield().getType())
                && item.hasItemMeta()
                && item.getItemMeta().getDisplayName().contains(name));
    }

    public boolean isWeapon(ItemStack item)
    {
        return (isSatelliteDagger(item) || isSatelliteSword(item) || isRedDwarfSword(item) || isHerbSword(item) || isSonificationSword(item) || isEvolveSatelliteSword(item) ||
                isSonificationSwordRyu(item) || isBlazingSword(item) || isBlindSword(item) || isCounterShield(item));
    }

    public boolean hasCrystal(Player player, int amount)
    {
        Inventory inv = player.getInventory();
        int currentAmount = 0;

        for(ItemStack item : inv.getContents())
        {
            if(item == null || item.getType().equals(Material.AIR))
                continue;
            if(isCrystal(item))
            {
                currentAmount += item.getAmount();
            }
        }

        return amount <= currentAmount;
    }

    public boolean hasRedCrystal(Player player, int amount)
    {
        Inventory inv = player.getInventory();
        int currentAmount = 0;

        for(ItemStack item : inv.getContents())
        {
            if(item == null || item.getType().equals(Material.AIR))
                continue;
            if(isRedCrystal(item))
            {
                currentAmount += item.getAmount();
            }
        }

        return amount <= currentAmount;
    }

    public boolean hasBlueCrystal(Player player, int amount)
    {
        Inventory inv = player.getInventory();
        int currentAmount = 0;

        for(ItemStack item : inv.getContents())
        {
            if(item == null || item.getType().equals(Material.AIR))
                continue;
            if(isBlueCrystal(item))
            {
                currentAmount += item.getAmount();
            }
        }

        return amount <= currentAmount;
    }

    public boolean hasProtector(Player player, int amount)
    {
        Inventory inv = player.getInventory();
        int currentAmount = 0;

        for(ItemStack item : inv.getContents())
        {
            if(item == null || item.getType().equals(Material.AIR))
                continue;
            if(isProtector(item))
            {
                currentAmount += item.getAmount();
            }
        }

        return amount <= currentAmount;
    }

    public boolean isCrystal(ItemStack item)
    {
        if(item.hasItemMeta())
        {
            ItemMeta meta = item.getItemMeta();
            String name = meta.getDisplayName();
            if(name.equalsIgnoreCase(getCrystal().getItemMeta().getDisplayName()) || item.getType().equals(getCrystal().getType()))
            {
                return true;
            }
        }
        return false;
    }

    public boolean isRedCrystal(ItemStack item)
    {
        if(item.hasItemMeta())
        {
            ItemMeta meta = item.getItemMeta();
            String name = meta.getDisplayName();
            if(name.equalsIgnoreCase(getRedCrystal().getItemMeta().getDisplayName()) || item.getType().equals(getRedCrystal().getType()))
            {
                return true;
            }
        }
        return false;
    }

    public boolean isBlueCrystal(ItemStack item)
    {
        if(item.hasItemMeta())
        {
            ItemMeta meta = item.getItemMeta();
            String name = meta.getDisplayName();
            if(name.equalsIgnoreCase(getBlueCrystal().getItemMeta().getDisplayName()) || item.getType().equals(getBlueCrystal().getType()))
            {
                return true;
            }
        }
        return false;
    }

    public boolean isProtector(ItemStack item)
    {
        if(item.hasItemMeta())
        {
            ItemMeta meta = item.getItemMeta();
            String name = meta.getDisplayName();
            if(name.equalsIgnoreCase(getProtector().getItemMeta().getDisplayName()) || item.getType().equals(getProtector().getType()))
            {
                return true;
            }
        }
        return false;
    }

    public boolean isDiscolorDust(ItemStack item)
    {
        if(item.hasItemMeta())
        {
            ItemMeta meta = item.getItemMeta();
            String name = meta.getDisplayName();
            if(name.equalsIgnoreCase(getDiscolorDust().getItemMeta().getDisplayName()) || item.getType().equals(getDiscolorDust().getType()))
            {
                return true;
            }
        }
        return false;
    }

    public void consumeCrystal(Player player, int amount)
    {
        Inventory inv = player.getInventory();
        for(ItemStack item : inv.getContents())
        {
            if(item == null || item.getType().equals(Material.AIR))
                continue;
            if(isCrystal(item))
            {
                if(item.getAmount() < amount)
                {
                    amount = amount - item.getAmount();
                    item.setAmount(0);
                }
                else
                {
                    item.setAmount(item.getAmount() - amount);
                    return;
                }
            }
        }
    }

    public void consumeRedCrystal(Player player, int amount)
    {
        Inventory inv = player.getInventory();
        for(ItemStack item : inv.getContents())
        {
            if(item == null || item.getType().equals(Material.AIR))
                continue;
            if(isRedCrystal(item))
            {
                if(item.getAmount() < amount)
                {
                    amount = amount - item.getAmount();
                    item.setAmount(0);
                }
                else
                {
                    item.setAmount(item.getAmount() - amount);
                    return;
                }
            }
        }
    }

    public void consumeBlueCrystal(Player player, int amount)
    {
        Inventory inv = player.getInventory();
        for(ItemStack item : inv.getContents())
        {
            if(item == null || item.getType().equals(Material.AIR))
                continue;
            if(isBlueCrystal(item))
            {
                if(item.getAmount() < amount)
                {
                    amount = amount - item.getAmount();
                    item.setAmount(0);
                }
                else
                {
                    item.setAmount(item.getAmount() - amount);
                    return;
                }
            }
        }
    }

    public void consumeProtector(Player player, int amount)
    {
        Inventory inv = player.getInventory();
        for(ItemStack item : inv.getContents())
        {
            if(item == null || item.getType().equals(Material.AIR))
                continue;
            if(isProtector(item))
            {
                if(item.getAmount() < amount)
                {
                    amount = amount - item.getAmount();
                    item.setAmount(0);
                }
                else
                {
                    item.setAmount(item.getAmount() - amount);
                    return;
                }
            }
        }
    }

    public int getCurrentSwordLevel(ItemStack sword)
    {
        String name = sword.getItemMeta().getDisplayName();
        String nameToInt = name.replaceAll("[^\\d]", "");
        int level = Integer.parseInt(nameToInt);
        return level;
    }

    public int getWeaponDamage(ItemStack weapon)
    {
        ItemMeta meta = weapon.getItemMeta();
        double currentStat = 0;

        for(AttributeModifier modifier : meta.getAttributeModifiers(Attribute.GENERIC_ATTACK_DAMAGE))
        {
            if(modifier == null)
                continue;
            currentStat += modifier.getAmount();
        }

        return (int) Math.round(currentStat);
    }

    public boolean isSuccess(int prob)
    {
        int randInt = new Random().nextInt(100) + 1;    //1~100
        return randInt <= prob;
    }

    public boolean isBroken()   //10%
    {
        int randInt = new Random().nextInt(100) + 1;
        return randInt <= 10;
    }

    public int getUpgradeStat(int min, int max)
    {
        int stat = new Random().nextInt(max - min + 1) + min;
        return stat;
    }

    //-----------------진화

    public ItemStack daggerToSatellite(ItemStack dagger)    //유성단검 -> 유성검
    {
        ItemStack sword = getSatelliteSword();

        ItemMeta swordMeta = sword.getItemMeta();
        swordMeta.setLore(null);
        sword.setItemMeta(swordMeta);

        int damage = getWeaponDamage(dagger);
        setAttackAttribute(sword, damage);

        return sword;
    }

    public ItemStack satelliteToRedDwarf(ItemStack satellite)   //유성검 -> 홍성검
    {
        ItemStack redDwarf = getRedDwarfSword();

        int damage = getWeaponDamage(satellite);
        setAttackAttribute(redDwarf, damage);

        return redDwarf;
    }

    public ItemStack satelliteToHerb(ItemStack satellite)   //유성검 -> 초섬검
    {
        ItemStack herb = getHerbSword();

        int damage = getWeaponDamage(satellite);
        setAttackAttribute(herb, damage);

        return herb;
    }

    public ItemStack satelliteToSonification(ItemStack satellite)   //위성검 -> 파동검:출
    {
        ItemStack sonification = getSonificationSword();

        int damage = getWeaponDamage(satellite);
        setAttackAttribute(sonification, damage);

        return sonification;
    }

    public ItemStack sonificationToRyu(ItemStack sonification)   //파동검:출 -> 파동검:류
    {
        ItemStack sonificationRyu = getSonificationSwordRyu();

        int damage = getWeaponDamage(sonification);
        setAttackAttribute(sonificationRyu, damage);

        return sonificationRyu;
    }

    public ItemStack satelliteToEvolve(ItemStack satellite)   //위성검 -> 진 위성검
    {
        ItemStack evolve = getEvolveSatelliteSword();

        int damage = getWeaponDamage(satellite);
        setAttackAttribute(evolve, damage);

        return evolve;
    }

    public ItemStack redDwarfToBlazing(ItemStack redDwarf)   //홍성검 -> 염화검
    {
        ItemStack blazing = getBlazingSword();

        int damage = getWeaponDamage(redDwarf);
        setAttackAttribute(blazing, damage);

        return blazing;
    }

    public ItemStack satelliteToBlind(ItemStack satellite)   //위성검 -> 절안검
    {
        ItemStack blind = getBlindSword();

        int damage = getWeaponDamage(satellite);
        setAttackAttribute(blind, damage);

        return blind;
    }

    //--------------------골드

    public int getGold(Player player)
    {
        String name = player.getName();
        int gold = playerDataConfig.getInt("Players." + name + ".gold");
        return gold;
    }

    public void setGold(Player player, int gold)
    {
        String name = player.getName();
        playerDataConfig.set("Players." + name + ".gold", gold);

        playerDataConfigManager.saveConfig();
    }

    public void sellItem(Player player, ItemStack item)
    {
        int level = getCurrentSwordLevel(item);
        int currentGold = getGold(player);
        int price = 0;

        if(isSatelliteDagger(item)) price = getPrice("SatelliteDagger", level);
        if(isSatelliteSword(item)) price = getPrice("SatelliteSword", level);
        if(isRedDwarfSword(item)) price = getPrice("RedDwarfSword", level);
        if(isHerbSword(item)) price = getPrice("HerbSword", level);
        if(isSonificationSword(item)) price = getPrice("SonificationSword", level);
        if(isEvolveSatelliteSword(item)) price = getPrice("EvolveSatelliteSword", level);
        if(isBlazingSword(item)) price = getPrice("BlazingSword", level);
        if(isBlindSword(item)) price = getPrice("BlindSword", level);
        if(isSonificationSwordRyu(item)) price = getPrice("SonificationSwordRyu", level);

        if(isCounterShield(item)) price = getPrice("CounterShield", level);

        setGold(player, currentGold + price);
        player.sendMessage(ChatColor.GOLD + "무기를 판매했습니다. [판매가: " + price + "골드]");
        player.playSound(player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 5, 1);
    }

    //---------------------체력

    public int getHealth(Player player)
    {
        String name = player.getName();
        int health = playerDataConfig.getInt("Players." + name + ".hp");
        return health;
    }

    public int getCurrentHealth(Player player)
    {
        String name = player.getName();
        int health = playerDataConfig.getInt("Players." + name + ".currentHp");
        return health;
    }

    public void addCurrentHealth(Player player, int health, Player damager)
    {
        int currentHp = getCurrentHealth(player);

        if(currentHp + health >= getHealth(player))     //최대체력을 초과해서 체력을 얻을 시
        {
            setCurrentHealth(player, getHealth(player));
            playerDataConfigManager.saveConfig();

            return;
        }
        if(currentHp + health <= 0) //입은 데미지로 인해 체력이 0 이하일 시
        {
            killPlayerByPlayer(player, damager);
            setDeath(player, getDeath(player) + 1);

            setCurrentHealth(player, getHealth(player));
            playerDataConfigManager.saveConfig();

            return;
        }
        setCurrentHealth(player, currentHp + health);
        playerDataConfigManager.saveConfig();
    }

    public void setHealth(Player player, int health)
    {
        String name = player.getName();
        playerDataConfig.set("Players." + name + ".hp", health);

        playerDataConfigManager.saveConfig();
    }

    public void setCurrentHealth(Player player, int health)
    {
        String name = player.getName();
        playerDataConfig.set("Players." + name + ".currentHp", health);

        playerDataConfigManager.saveConfig();
    }

    public void killPlayerByPlayer(Player player, Player damager)
    {
        String subtitle = "";

        if(damager != null)
        {
            subtitle = "by " + damager.getName();

            if(getGold(player) >= 500)     //골드뺏기
            {
                setGold(damager, getGold(damager) + 500);
                setGold(player, getGold(player) - 500);
            }
            else
            {
                int gold = getGold(player);

                setGold(damager, getGold(damager) + gold);
                setGold(player, getGold(player) - gold);
            }
            setKill(damager, getKill(damager) + 1);

            player.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "DEFEAT", subtitle, 0, 60, 20);

            Firework fw = (Firework) player.getWorld().spawnEntity(player.getLocation(), EntityType.FIREWORK);
            FireworkMeta fwMeta = fw.getFireworkMeta();
            fwMeta.setPower(2);
            fwMeta.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).build());
            fw.setFireworkMeta(fwMeta);
            fw.detonate();

            damager.sendTitle(ChatColor.GREEN + "" + ChatColor.BOLD + "KILL", player.getName(), 0, 60, 20);
            damager.getServer().broadcastMessage(ChatColor.BOLD + "" + ChatColor.GOLD + damager.getName() + "" + ChatColor.WHITE + "님이 " +
                    ChatColor.RED + player.getName() + ChatColor.WHITE + "님을 죽였습니다!");
        }

        Location spawn = player.getWorld().getSpawnLocation();
        player.teleport(spawn);

        player.sendTitle(ChatColor.RED + "" + ChatColor.BOLD + "DEFEAT", subtitle, 0, 60, 20);
        player.playSound(spawn, Sound.ENTITY_ALLAY_DEATH, 5, 1);

        CustomPlayerDeathEvent event = new CustomPlayerDeathEvent(player, damager);
        plugin.getServer().getPluginManager().callEvent(event);
    }

    public int getHealthLimit()
    {
        int limit = optionConfig.getInt("Options.healthLimit");
        return limit;
    }

    public void setHealthLimit(int limit)
    {
        optionConfig.set("Options.healthLimit", limit);
    }

    //-------------------------- 방어력

    public int getShield(Player player)
    {
        String name = player.getName();
        int shield = playerDataConfig.getInt("Players." + name + ".shield");
        return shield;
    }

    public void setShield(Player player, int amount)
    {
        String name = player.getName();
        playerDataConfig.set("Players." + name + ".shield", amount);

        playerDataConfigManager.saveConfig();
    }

    //----------------------------킬뎃

    public int getKill(Player player)
    {
        String name = player.getName();
        int kill = playerDataConfig.getInt("Players." + name + ".kill");
        return kill;
    }

    public void setKill(Player player, int kill)
    {
        String name = player.getName();
        playerDataConfig.set("Players." + name + ".kill", kill);

        playerDataConfigManager.saveConfig();
    }

    public int getDeath(Player player)
    {
        String name = player.getName();
        int death = playerDataConfig.getInt("Players." + name + ".death");
        return death;
    }

    public void setDeath(Player player, int death)
    {
        String name = player.getName();
        playerDataConfig.set("Players." + name + ".death", death);

        playerDataConfigManager.saveConfig();
    }

    //-------------------옵션

    public boolean isAllowDmgMsg(Player player)
    {
        String name = player.getName();
        boolean allowDmgMsg = playerDataConfig.getBoolean("Players." + name + ".allowDmgMsg");
        return allowDmgMsg;
    }

    public void setAllowDmgMsg(Player player, boolean allowDmgMsg)
    {
        String name = player.getName();
        playerDataConfig.set("Players." + name + ".allowDmgMsg", allowDmgMsg);

        playerDataConfigManager.saveConfig();
    }

    //----------------------편의성

    public void safeAddItem(Player player, ItemStack item)
    {
        Map<Integer, ItemStack> drops = player.getInventory().addItem(item);
        if(!drops.isEmpty())
        {
            for(Map.Entry<Integer, ItemStack> entry : drops.entrySet())
            {
                if(entry.getValue().getType().isAir()) continue;

                Item drop = player.getWorld().dropItemNaturally(player.getLocation(), entry.getValue());
                drop.setOwner(player.getUniqueId());
            }
        }
    }
}
