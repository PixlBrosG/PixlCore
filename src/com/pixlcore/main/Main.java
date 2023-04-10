package com.pixlcore.main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Main extends JavaPlugin implements Listener
{
	private HashMap<Player, Location> back = new HashMap<Player, Location>();

	public void onEnable()
	{
		Location spawn = getWarp("spawn");
		for (Player p : getServer().getOnlinePlayers())
			back.put(p, spawn);
	}
	
	public void onDisable()
	{
		back.clear();
	}
	
	private boolean setWarp(String warpName, Location newWarp)
	{
		File configFile = new File(getDataFolder(), "config.yml");
		FileConfiguration config = getConfig();
		
		config.set("warps." + warpName + ".world", newWarp.getWorld().getName());
		config.set("warps." + warpName + ".x", newWarp.getX());
		config.set("warps." + warpName + ".y", newWarp.getY());
		config.set("warps." + warpName + ".z", newWarp.getZ());
		config.set("warps." + warpName + ".pitch", newWarp.getPitch());
		config.set("warps." + warpName + ".yaw", newWarp.getYaw());
		
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
	
	private Location getWarp(String warpName)
	{
		try
		{
			ConfigurationSection warpCfg = getConfig().getConfigurationSection("warps." + warpName);
			
			World world = getServer().getWorld(warpCfg.getString("world"));
			
			double x = warpCfg.getDouble("x");
			double y = warpCfg.getDouble("y");
			double z = warpCfg.getDouble("z");
			
			float pitch = (float)warpCfg.getDouble("pitch");
			float yaw   = (float)warpCfg.getDouble("yaw");
			
			Location warp = new Location(world, x, y, z);
			warp.setPitch(pitch);
			warp.setYaw(yaw);
			
			return warp;
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	private boolean setHome(UUID playerID, Location newWarp)
	{
		File configFile = new File(getDataFolder(), "playerdata.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
		
		String idString = playerID.toString();
		
		config.set(idString + ".home.world", newWarp.getWorld().getName());
		config.set(idString + ".home.x", newWarp.getX());
		config.set(idString + ".home.y", newWarp.getY());
		config.set(idString + ".home.z", newWarp.getZ());
		config.set(idString + ".home.pitch", newWarp.getPitch());
		config.set(idString + ".home.yaw", newWarp.getYaw());
		
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
	
	private Location getHome(UUID playerID)
	{
		try
		{
			File configFile = new File(getDataFolder(), "playerdata.yml");
			FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
			
			ConfigurationSection homeCfg = config.getConfigurationSection(playerID.toString() + ".home");
			
			World world = getServer().getWorld(homeCfg.getString("world"));
			
			double x = homeCfg.getDouble("x");
			double y = homeCfg.getDouble("y");
			double z = homeCfg.getDouble("z");
			
			float pitch = (float)homeCfg.getDouble("pitch");
			float yaw   = (float)homeCfg.getDouble("yaw");
			
			Location warp = new Location(world, x, y, z);
			warp.setPitch(pitch);
			warp.setYaw(yaw);
			
			return warp;
		}
		catch (Exception e)
		{
			return null;
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		Player player = (Player)sender;
		
		if (cmd.getName().equalsIgnoreCase("rename")) // TODO Fix
		{
			if (args.length == 0)
			{
				player.sendMessage(Utils.color("Please specify name"));
				return true;
			}
			
			ItemStack heldItem = player.getInventory().getItemInMainHand();
			
			if (heldItem.getType() == null || heldItem.getType().isAir())
			{
				player.sendMessage(Utils.color("No held item to rename!"));
				return true;
			}
			
			ItemMeta meta = heldItem.getItemMeta();
			meta.setDisplayName(Utils.color(String.join(" ", args)));
			heldItem.setItemMeta(meta);
		}
		else if (cmd.getName().equalsIgnoreCase("invsee"))
		{
			if (args.length == 0)
			{
				player.sendMessage(Utils.color("&cPlease specify a player"));
				return true;
			}
			
			Player src = getServer().getPlayer(args[0]);
			
			if (src == null)
			{
				player.sendMessage(Utils.color("&cPlayer " + args[0] + " does not exist"));
				return true;
			}
			
			player.sendMessage(Utils.color("&aOpening " + args[0] + "'s inventory"));
			player.openInventory(src.getInventory());
		}
		else if (cmd.getName().equalsIgnoreCase("discord"))
		{
			TextComponent msg = new TextComponent(Utils.color("&aJoin our discord!"));
			msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/" + getConfig().getString("discord")));
			player.spigot().sendMessage(msg);
		}
		else if (cmd.getName().equalsIgnoreCase("spawn"))
		{
			Location spawn = getWarp("spawn");
			
			if (spawn == null)
			{
				player.sendMessage(Utils.color("&cSpawn is not set"));
			}
			else
			{
				player.sendMessage(Utils.color("&aTeleporting spawn"));
				player.teleport(spawn);
			}
		}
		else if (cmd.getName().equalsIgnoreCase("setspawn"))
		{
			if (setWarp("spawn", player.getLocation()))
				player.sendMessage(Utils.color("&aSet spawn to current location"));
			else
				player.sendMessage(Utils.color("&cFailed to set spawn!"));
		}
		else if (cmd.getName().equalsIgnoreCase("warp"))
		{
			if (args.length == 0)
			{
				player.sendMessage(Utils.color("&cPlease specify a warp to teleport to"));
				return true;
			}
			
			Location warp = getWarp(args[0]);
			
			if (warp == null)
			{
				player.sendMessage(Utils.color("&cWarp &e" + args[0] + " &cdeos not exist"));
			}
			else
			{
				player.sendMessage(Utils.color("&aTeleporting to &e" + args[0]));
				player.teleport(warp);
			}
		}
		else if (cmd.getName().equalsIgnoreCase("setwarp"))
		{
			if (args.length == 0)
			{
				player.sendMessage(Utils.color("&cPlease specify a warp to set"));
				return true;
			}
			
			if (setWarp(args[0], player.getLocation()))
				player.sendMessage(Utils.color("&aSet warp &e" + args[0] + " &ato current location"));
			else
				player.sendMessage(Utils.color("&cFailed to set warp &e" + args[0] + "&c!"));
		}
		else if (cmd.getName().equalsIgnoreCase("warps"))
		{
			ConfigurationSection warps = getConfig().getConfigurationSection("warps");
			
			player.sendMessage(Utils.color("&cWarps:"));
				
			for (String warpName : warps.getKeys(false))
			{
				if (getWarp(warpName) != null)
				{
					TextComponent msg = new TextComponent(Utils.color("&c- &a" + warpName));
					msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/warp " + warpName));
					player.spigot().sendMessage(msg);
				}
			}
		}
		else if (cmd.getName().equalsIgnoreCase("home"))
		{
			Location home = getHome(player.getUniqueId());
			
			if (home == null)
			{
				player.sendMessage(Utils.color("&cHome not set"));
			}
			else
			{
				player.sendMessage(Utils.color("&aTeleporting to home"));
				player.teleport(home);
			}
		}
		else if (cmd.getName().equalsIgnoreCase("sethome"))
		{
			if (setHome(player.getUniqueId(), player.getLocation()))
				player.sendMessage(Utils.color("&aSet home to current location"));
			else
				player.sendMessage(Utils.color("&cFailed to set home!"));
		}
		else if (cmd.getName().equalsIgnoreCase("back"))
		{
			player.sendMessage(Utils.color("&aTeleporting to last location!"));
			player.teleport(back.get(player));
		}
		
		return true;
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e)
	{
		Player p = e.getEntity();
		back.replace(p, back.get(p), p.getLocation());
	}
	
	@EventHandler
	public void onTeleport(PlayerTeleportEvent e)
	{
		back.replace(e.getPlayer(), e.getFrom());
	}
	
	public void onJoin(PlayerJoinEvent e)
	{
		Location spawn = getWarp("spawn");
		
		back.put(e.getPlayer(), spawn);

		if (!e.getPlayer().hasPlayedBefore())
		{
			e.getPlayer().teleport(spawn);
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e)
	{
		back.remove(e.getPlayer());
	}
	
}
