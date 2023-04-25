package io.github.rotanghub.godofweapon.manager;

import io.github.rotanghub.godofweapon.GodOfWeapon;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class PlayerDataConfigManager
{
    private GodOfWeapon plugin;
    private FileConfiguration playerDataConfig = null;
    private File configFile = null;

    public PlayerDataConfigManager(GodOfWeapon plugin)
    {
        this.plugin = plugin;
        saveDefaultConfig();
    }

    public void reloadConfig()
    {
        if(this.configFile == null)
        {
            this.configFile =  new File(this.plugin.getDataFolder(), "playerData.yml");
        }

        this.playerDataConfig = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = this.plugin.getResource("playerData.yml");
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
        if(this.configFile == null) this.configFile = new File(this.plugin.getDataFolder(), "playerData.yml");

        if(!this.configFile.exists())
        {
            this.plugin.saveResource("playerData.yml", false);
        }
    }
}
