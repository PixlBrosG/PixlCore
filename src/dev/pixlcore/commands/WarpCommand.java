package dev.pixlcore.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.pixlcore.utils.Utils;
import org.jetbrains.annotations.NotNull;

public class WarpCommand extends CommandHandler
{
	@Override
	public boolean IsRunnableByPlayer() { return true; }
	
	@Override
	public boolean Run(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
	{
		Player player = (Player)sender;
		
		if (args.length == 0)
		{
			player.sendMessage(Utils.ApplyColors("&cPlease specify a warp to teleport to"));
			return true;
		}
		
		Location warp = Utils.GetWarp(args[0]);
		
		if (warp == null)
		{
			player.sendMessage(Utils.ApplyColors("&cWarp &e" + args[0] + " &cdeos not exist"));
		}
		else
		{
			player.sendMessage(Utils.ApplyColors("&aTeleporting to &e" + args[0]));
			player.teleport(warp);
		}
		
		return true;
	}
}
