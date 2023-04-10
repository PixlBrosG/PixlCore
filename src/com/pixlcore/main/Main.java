package com.pixlcore.main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
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
	
	private void setWarp(String warpName, Location newWarp)
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
		}
		catch (IOException e)
		{
			e.printStackTrace();
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
			
			double pitch = warpCfg.getDouble("pitch");
			double yaw = warpCfg.getDouble("yaw");
			
			Location warp = new Location(world, x, y, z);
			warp.setPitch((float)pitch);
			warp.setYaw((float)yaw);
			
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
				player.sendMessage(API.color("Please specify name"));
				return true;
			}
			
			ItemStack heldItem = player.getInventory().getItemInMainHand();
			
			if (heldItem.getType() == null || heldItem.getType().isAir())
			{
				player.sendMessage(API.color("No held item to rename!"));
				return true;
			}
			
			ItemMeta meta = heldItem.getItemMeta();
			meta.setDisplayName(API.color(String.join(" ", args)));
			heldItem.setItemMeta(meta);
			
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("invsee"))
		{
			if (args.length == 0)
			{
				player.sendMessage(API.color("&cPlease specify a player"));
				return true;
			}
			
			Player src = getServer().getPlayer(args[0]);
			
			if (src == null)
			{
				player.sendMessage(API.color("&cPlayer " + args[0] + " does not exist"));
				return true;
			}
			
			player.sendMessage(API.color("&aOpening " + args[0] + "'s inventory"));
			player.openInventory(src.getInventory());
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("discord"))
		{
			TextComponent msg = new TextComponent(API.color("&aJoin our discord!"));
			msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/" + getConfig().getString("discord")));
			player.spigot().sendMessage(msg);
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("spawn"))
		{
			Location spawn = getWarp("spawn");
			
			if (spawn == null)
			{
				player.sendMessage(API.color("&cSpawn is not set"));
			}
			else
			{
				player.sendMessage(API.color("&aTeleporting spawn"));
				player.teleport(spawn);
			}
			
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("setspawn"))
		{
			player.sendMessage(API.color("&aSet spawn to current location"));
			setWarp("spawn", player.getLocation());
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("warp"))
		{
			if (args.length == 0)
			{
				player.sendMessage(API.color("&cPlease specify a warp to teleport to"));
				return true;
			}
			
			Location warp = getWarp(args[0]);
			
			if (warp == null)
			{
				player.sendMessage(API.color("&cWarp deosn't exist"));
			}
			else
			{
				player.sendMessage(API.color("&aTeleporting to warp " + args[0]));
				player.teleport(warp);
			}
			
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("setwarp"))
		{
			if (args.length == 0)
			{
				player.sendMessage(API.color("&cPlease specify a warp to set"));
				return true;
			}
			
			player.sendMessage(API.color("&cSet warp " + args[0]));
			setWarp(args[0], player.getLocation());
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("warps"))
		{
			ConfigurationSection warps = getConfig().getConfigurationSection("warps");
			
			player.sendMessage(API.color("&cWarps:"));
				
			for (String warpName : warps.getKeys(false))
			{
				if (getWarp(warpName) != null)
				{
					TextComponent msg = new TextComponent(API.color("&c- &a" + warpName));
					msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/warp " + warpName));
					player.spigot().sendMessage(msg);
				}
			}
			
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("back"))
		{
			player.sendMessage(API.color("&aTeleporting to last location!"));
			player.teleport(back.get(player));
			return true;
		}
		
		return false;
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
