package io.github.rotanghub.godofweapon.manager;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class UpgradeData
{
    int level = 0;
    FileConfiguration config = Bukkit.getServer().spigot().getConfig();

    public UpgradeData(Manager manager, int level)
    {
        this.level = level;
        //this.config = manager.getConfig();
    }

    private int data = config.getInt("Sword.upgrade." + level + ".data");
    private int consume = config.getInt("Sword.upgrade." + level + ".consume");
    private int prob = config.getInt("Sword.upgrade." + level + ".prob");
    private int statMin = config.getInt("Sword.upgrade." + level + ".statMin");
    private int statMax = config.getInt("Sword.upgrade." + level + ".statMax");
    private int price = config.getInt("Sword.upgrade." + level + ".price");

    public int getData() { return data; }
    public int getConsume() { return consume; }
    public int getProb() { return prob; }
    public int getStatMin() { return statMin; }
    public int getStatMax() { return statMax; }
    public int getPrice() { return price; }
}
