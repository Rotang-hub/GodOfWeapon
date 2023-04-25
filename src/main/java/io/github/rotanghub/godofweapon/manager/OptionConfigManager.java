package io.github.rotanghub.godofweapon.manager;

import io.github.rotanghub.godofweapon.GodOfWeapon;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class OptionConfigManager
{
    private GodOfWeapon plugin;
    private FileConfiguration playerDataConfig = null;
    private File configFile = null;

    public OptionConfigManager(GodOfWeapon plugin)
    {
        this.plugin = plugin;
        saveDefaultConfig();
    }

    public void reloadConfig()
    {
        if(this.configFile == null)
        {
            this.configFile =  new File(this.plugin.getDataFolder(), "option.yml");
        }

        this.playerDataConfig = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = this.plugin.getResource("option.yml");
        if(defaultStream != null)
        {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.playerDataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig()
    {
        if(this.playerDataConfig == null)
        {
            reloadConfig();
        }

        return this.playerDataConfig;
    }

    public void saveConfig()
    {
        if(this.playerDataConfig == null || this.configFile == null) return;

        try {
            this.getConfig().save(this.configFile);
        } catch (IOException e)
        {
            this.plugin.getLogger().log(Level.SEVERE, "Can not save file!",  e);
        }
    }

    public void saveDefaultConfig()
    {
        if(this.configFile == null) this.configFile = new File(this.plugin.getDataFolder(), "option.yml");

        if(!this.configFile.exists())
        {
            this.plugin.saveResource("option.yml", false);
        }
    }
}
