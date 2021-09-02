package com.pixlcore.main;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
	private Location spawn;

	public void onEnable()
	{
		spawn = new Location(getServer().getWorld("flat"), 9.5f, 4.0f, -182.5f);
		
		for (Player p : getServer().getOnlinePlayers())
			back.put(p, spawn);
	}
	
	public void onDisable()
	{
		back.clear();
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
		else if (cmd.getName().equalsIgnoreCase("discord"))
		{
			TextComponent msg = new TextComponent("&aJoin our discord!");
			msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/kcffr9Tpne"));
			player.spigot().sendMessage(msg);
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("spawn"))
		{
			player.sendMessage(API.color("&aTeleporting to Spawn!"));
			player.teleport(spawn);
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("shop"))
		{
			player.sendMessage(API.color("&aTeleporting to Shop!"));
			player.teleport(new Location(getServer().getWorld("flat"), 43.5f, 4.0f, -185.5f));
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("back"))
		{
			player.sendMessage(API.color("&aTeleporting to last location!"));
			player.teleport(back.get(player));
			return true;
		}
		else if (cmd.getName().equalsIgnoreCase("start"))
		{
			player.sendMessage(API.color("&awelcome to &2&lJunglleSMP!"));
			player.teleport(new Location(getServer().getWorld("world"), 3043.5f, 64.0f, -2436.5f));
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
