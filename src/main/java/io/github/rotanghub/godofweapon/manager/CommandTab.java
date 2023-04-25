package io.github.rotanghub.godofweapon.manager;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandTab implements TabCompleter
{
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args)
    {
        List<String> list = Arrays.asList("dagger", "satellite", "evolvesatellite", "reddwarf", "herb", "sonification", "sonificationryu", "blazing", "blind", "countershield",
                "crystal", "redcrystal", "bluecrystal", "protector", "colordust", "gold", "health", "shield", "healthlimit", "reloadconfig");
        List<String> goldTab = Arrays.asList("set", "add", "subtract");
        List<String> healthTab = Arrays.asList("set");
        List<String> shieldTab = Arrays.asList("set");
        List<String> healthLimitTab = Arrays.asList("set");
        List<String> result = new ArrayList<>();

        if(args.length == 1)
        {
            String input = args[0].toLowerCase();

            for(String s : list)
            {
                if(s.startsWith(input.toLowerCase()))
                {
                    result.add(s);
                }
            }

            return result;
        }

        if(args.length >= 3)
        {
            if(args[0].equalsIgnoreCase("gold") || args[0].equalsIgnoreCase("health") || args[0].equalsIgnoreCase("shield"))
            {
                String input = args[2].toLowerCase();

                for(Player player : Bukkit.getServer().getOnlinePlayers())
                {
                    if(player.getName().startsWith(input.toLowerCase()))
                    {
                        result.add(player.getName());
                    }
                }
                return result;
            }
        }

        if(args.length == 2);
        {
            if(args[0].equalsIgnoreCase("gold"))
            {
                String input = args[1].toLowerCase();

                for(String s : goldTab)
                {
                    if(s.startsWith(input.toLowerCase()))
                    {
                        result.add(s);
                    }
                }
                return result;
            }

            if(args[0].equalsIgnoreCase("health"))
            {
                String input = args[1].toLowerCase();

                for(String s : healthTab)
                {
                    if(s.startsWith(input.toLowerCase()))
                    {
                        result.add(s);
                    }
                }
                return result;
            }

            if(args[0].equalsIgnoreCase("shield"))
            {
                String input = args[1].toLowerCase();

                for(String s : shieldTab)
                {
                    if(s.startsWith(input.toLowerCase()))
                    {
                        result.add(s);
                    }
                }
                return result;
            }

            if(args[0].equalsIgnoreCase("healthlimit"))
            {
                String input = args[1].toLowerCase();

                for(String s : healthLimitTab)
                {
                    if(s.startsWith(input.toLowerCase()))
                    {
                        result.add(s);
                    }
                }
                return result;
            }
        }

        return null;
    }
}
