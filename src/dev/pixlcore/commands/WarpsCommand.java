package dev.pixlcore.commands;

import java.io.File;

import dev.pixlcore.core.EntryPoint;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import dev.pixlcore.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

public class WarpsCommand extends CommandHandler
{
	@Override
	public boolean IsRunnableByPlayer() { return true; }
	
	@Override
	public boolean Run(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
	{
		Player player = (Player)sender;
		
		File configFile = new File(EntryPoint.GetInstance().getDataFolder(), "worlddata.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
		
		ConfigurationSection warps = config.getConfigurationSection("warps");

		if (warps == null || warps.getKeys(false).isEmpty())
		{
			player.sendMessage(Utils.ApplyColors("&cThis server currently has no warps"));
			return true;
		}
		
		player.sendMessage(Utils.ApplyColors("&cWarps:"));
			
		for (String warpName : warps.getKeys(false))
		{
			if (Utils.GetWarp(warpName) != null)
			{
				TextComponent msg = new TextComponent(Utils.ApplyColors("&c- &a" + warpName));
				msg.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/warp " + warpName));
				player.spigot().sendMessage(msg);
			}
		}
		
		return true;
	}
}
