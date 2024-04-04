package dev.pixlcore.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.pixlcore.utils.Utils;
import org.jetbrains.annotations.NotNull;

public class HomeCommand extends CommandHandler
{
	@Override
	public boolean IsRunnableByPlayer() { return true; }
	
	@Override
	public boolean Run(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
	{
		Player player = (Player)sender;
		Location home = Utils.GetHome(player.getUniqueId());
		
		if (home == null)
		{
			player.sendMessage(Utils.ApplyColors("&cHome not set"));
		}
		else
		{
			player.sendMessage(Utils.ApplyColors("&aTeleporting to home"));
			player.teleport(home);
		}
		
		return true;
	}
}
