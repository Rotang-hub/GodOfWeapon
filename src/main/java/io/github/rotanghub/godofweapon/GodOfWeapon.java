package io.github.rotanghub.godofweapon;

import io.github.rotanghub.godofweapon.manager.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class GodOfWeapon extends JavaPlugin
{
    public Manager manager;
    DataConfigManager dataConfigManager = new DataConfigManager(this);
    PlayerDataConfigManager playerDataConfigManager = new PlayerDataConfigManager(this);
    OptionConfigManager optionConfigManager = new OptionConfigManager(this);
    final FileConfiguration data = dataConfigManager.getConfig();
    final FileConfiguration playerData = playerDataConfigManager.getConfig();
    final FileConfiguration option = optionConfigManager.getConfig();

    @Override
    public void onEnable()
    {
        getCommand("gow").setTabCompleter(new CommandTab());

        this.manager = new Manager(this, dataConfigManager, playerDataConfigManager, optionConfigManager);
        getServer().getPluginManager().registerEvents(new GUIManager(manager), this);
        getServer().getPluginManager().registerEvents(new WeaponSkill(manager), this);

        reloadPlugin(getServer().getPluginManager().getPlugin("GOWSystem"));
        reloadPlugin(getServer().getPluginManager().getPlugin("GOWMining"));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if(!(sender instanceof Player))
        {
            return false;
        }
        Player player = (Player) sender;

        if(player.isOp())
        {
            if(label.equalsIgnoreCase("gow"))
            {
                if(args.length == 0)
                {
                    return false;
                }
                if(args[0].equalsIgnoreCase("dagger"))
                {
                    player.getInventory().addItem(manager.getSatelliteDagger());
                }
                if(args[0].equalsIgnoreCase("satellite"))
                {
                    player.getInventory().addItem(manager.getSatelliteSword());
                }
                if(args[0].equalsIgnoreCase("reddwarf"))
                {
                    player.getInventory().addItem(manager.getRedDwarfSword());
                }
                if(args[0].equalsIgnoreCase("herb"))
                {
                    player.getInventory().addItem(manager.getHerbSword());
                }
                if(args[0].equalsIgnoreCase("sonification"))
                {
                    player.getInventory().addItem(manager.getSonificationSword());
                }
                if(args[0].equalsIgnoreCase("sonificationryu"))
                {
                    player.getInventory().addItem(manager.getSonificationSwordRyu());
                }
                if(args[0].equalsIgnoreCase("blind"))
                {
                    player.getInventory().addItem(manager.getBlindSword());
                }
                if(args[0].equalsIgnoreCase("blazing"))
                {
                    player.getInventory().addItem(manager.getBlazingSword());
                }
                if(args[0].equalsIgnoreCase("evolvesatellite"))
                {
                    player.getInventory().addItem(manager.getEvolveSatelliteSword());
                }
                if(args[0].equalsIgnoreCase("countershield"))
                {
                    player.getInventory().addItem(manager.getCounterShield());
                }
                if(args[0].equalsIgnoreCase("crystal"))
                {
                    ItemStack crystal = manager.getCrystal();
                    if(args.length >= 2)
                    {
                        int amount = Integer.parseInt(args[1]);
                        crystal.setAmount(amount);
                    }
                    player.getInventory().addItem(crystal);
                }
                if(args[0].equalsIgnoreCase("redcrystal"))
                {
                    ItemStack redCrystal = manager.getRedCrystal();
                    if(args.length >= 2)
                    {
                        int amount = Integer.parseInt(args[1]);
                        redCrystal.setAmount(amount);
                    }
                    player.getInventory().addItem(redCrystal);
                }
                if(args[0].equalsIgnoreCase("bluecrystal"))
                {
                    ItemStack blueCrystal = manager.getBlueCrystal();
                    if(args.length >= 2)
                    {
                        int amount = Integer.parseInt(args[1]);
                        blueCrystal.setAmount(amount);
                    }
                    player.getInventory().addItem(blueCrystal);
                }
                if(args[0].equalsIgnoreCase("protector"))
                {
                    ItemStack protector = manager.getProtector();
                    if(args.length >= 2)
                    {
                        int amount = Integer.parseInt(args[1]);
                        protector.setAmount(amount);
                    }
                    player.getInventory().addItem(protector);
                }
                if(args[0].equalsIgnoreCase("colordust"))
                {
                    ItemStack dust = manager.getDiscolorDust();
                    if(args.length >= 2)
                    {
                        int amount = Integer.parseInt(args[1]);
                        dust.setAmount(amount);
                    }
                    player.getInventory().addItem(dust);
                }
                if(args[0].equalsIgnoreCase("anvil"))
                {
                    manager.process = !manager.process;
                }
                if(args[0].equalsIgnoreCase("reloadconfig"))
                {
                    dataConfigManager.reloadConfig();
                    dataConfigManager.saveConfig();
                    playerDataConfigManager.reloadConfig();
                    playerDataConfigManager.saveConfig();
                }
                if(args[0].equalsIgnoreCase("gold"))
                {
                    if(args.length < 4)
                    {
                        player.sendMessage(ChatColor.GREEN + "Usage: " + "/gow gold <add | subtract | set> <Player> <Amount>");
                        return false;
                    }

                    if(args[1].equalsIgnoreCase("add"))
                    {
                        Player target = getServer().getPlayer(args[2]);
                        int amount = Integer.parseInt(args[3]);

                        manager.setGold(target, manager.getGold(target) + amount);

                        player.sendMessage(target.getName() + "에게 " + amount + "골드를 지급했습니다.");
                    }
                    if(args[1].equalsIgnoreCase("subtract"))
                    {
                        Player target = getServer().getPlayer(args[2]);
                        int amount = Integer.parseInt(args[3]);
                        if(manager.getGold(target) > amount) manager.setGold(target, manager.getGold(target) - amount);
                        else manager.setGold(target, 0);

                        player.sendMessage(target.getName() + "에게서 " + amount + "골드를 제거했습니다.");
                    }
                    if(args[1].equalsIgnoreCase("set"))
                    {
                        Player target = getServer().getPlayer(args[2]);
                        int amount = Integer.parseInt(args[3]);

                        manager.setGold(target, amount);
                        player.sendMessage(target.getName() + "의 보유 골드를 " + amount + "골드로 설정했습니다.");
                    }
                }
                if(args[0].equalsIgnoreCase("health"))
                {
                    if(args.length < 4)
                    {
                        player.sendMessage(ChatColor.GREEN + "Usage: " + "/gow health <set> <Player> <Amount>");
                        return false;
                    }
                    if(args[1].equalsIgnoreCase("set"))
                    {
                        Player target = getServer().getPlayer(args[2]);
                        int amount = Integer.parseInt(args[3]);

                        manager.setHealth(target, amount);
                        //manager.setCurrentHealth(target, amount);
                        player.sendMessage(target.getName() + "의 최대 체력을 " + amount + "로 설정했습니다.");
                    }
                }
                if(args[0].equalsIgnoreCase("shield"))
                {
                    if(args.length < 4)
                    {
                        player.sendMessage(ChatColor.GREEN + "Usage: " + "/gow shield <set> <Player> <Amount>");
                        return false;
                    }
                    if(args[1].equalsIgnoreCase("set"))
                    {
                        Player target = getServer().getPlayer(args[2]);
                        int amount = Integer.parseInt(args[3]);

                        manager.setShield(target, amount);
                        player.sendMessage(target.getName() + "의 방어력을 " + amount + "로 설정했습니다.");
                    }
                }
                if(args[0].equalsIgnoreCase("healthlimit"))
                {
                    if(args.length < 3)
                    {
                        player.sendMessage(ChatColor.GREEN + "Usage: " + "/gow healthlimit <set> <Amount>");
                        return false;
                    }
                    if(args[1].equalsIgnoreCase("set"))
                    {
                        int limit = Integer.parseInt(args[2]);

                        //manager.setCurrentHealth(target, amount);
                        manager.setHealthLimit(limit);
                        player.sendMessage("최대 체력 제한을 " + limit + "로 설정했습니다.");
                    }
                }
            }
        }
        return super.onCommand(sender, command, label, args);
    }

    public void reloadPlugin(Plugin plugin)
    {
        getServer().getPluginManager().disablePlugin(plugin);
        getServer().getPluginManager().enablePlugin(plugin);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + plugin.getName() + " plugin is reloaded");
    }
}
