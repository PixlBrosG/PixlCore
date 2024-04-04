package dev.pixlcore.utils;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import dev.pixlcore.core.EntryPoint;

public class Utils
{
	public static String ApplyColors(String message)
	{
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	private static Location LoadLocation(String filename, String name)
	{
		File configFile = new File(EntryPoint.GetInstance().getDataFolder(), filename);
		FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

		ConfigurationSection locationConfig = config.getConfigurationSection(name);

		if (locationConfig == null)
			return null;

		World world = EntryPoint.GetInstance().getServer().getWorld(locationConfig.getString("world", "world"));

		double x = locationConfig.getDouble("x", 0.0);
		double y = locationConfig.getDouble("y", 0.0);
		double z = locationConfig.getDouble("z", 0.0);

		float pitch = (float)locationConfig.getDouble("pitch", 0.0);
		float yaw   = (float)locationConfig.getDouble("yaw", 0.0);

		Location location = new Location(world, x, y, z);
		location.setPitch(pitch);
		location.setYaw(yaw);

		return location;
	}

	private static boolean SaveLocation(String filename, String name, Location location)
	{
		File configFile = new File(EntryPoint.GetInstance().getDataFolder(), filename);
		FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

		if (location == null)
		{
			config.set(name, null);
		}
		else
		{
			if (location.getWorld() == null)
				config.set(name + ".world", EntryPoint.GetInstance().getServer().getWorlds().get(0));
			else
				config.set(name + ".world", location.getWorld().getName());

			config.set(name + ".x", location.getX());
			config.set(name + ".y", location.getY());
			config.set(name + ".z", location.getZ());
			config.set(name + ".pitch", location.getPitch());
			config.set(name + ".yaw", location.getYaw());
		}

		try
		{
			config.save(configFile);
			return true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public static Location GetWarp(String warpName)
	{
		return LoadLocation("worlddata.yml", "warps." + warpName);
	}

	public static boolean SetWarp(String warpName, Location location)
	{
		return SaveLocation("worlddata.yml", "warps." + warpName, location);
	}

	public static Location GetHome(UUID playerID)
	{
		return LoadLocation("playerdata.yml", playerID.toString() + ".home");
	}

	public static boolean SetHome(UUID playerID, Location location)
	{
		return SaveLocation("playerdata.yml", playerID.toString() + ".home", location);
	}
}
