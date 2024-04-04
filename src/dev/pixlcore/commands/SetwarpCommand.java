package dev.pixlcore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.pixlcore.utils.Utils;
import org.jetbrains.annotations.NotNull;

public class SetwarpCommand extends CommandHandler
{
	@Override
	public boolean IsRunnableByPlayer() { return true; }
	
	@Override
	public boolean Run(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
	{
		Player player = (Player)sender;
		
		if (args.length == 0)
		{
			player.sendMessage(Utils.ApplyColors("&cPlease specify a warp to set"));
			return true;
		}
		
		if (Utils.SetWarp(args[0], player.getLocation()))
			player.sendMessage(Utils.ApplyColors("&aSet warp &e" + args[0] + " &ato current location"));
		else
			player.sendMessage(Utils.ApplyColors("&cFailed to set warp &e" + args[0] + "&c!"));
		
		return true;
	}
}
