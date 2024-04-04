package dev.pixlcore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import dev.pixlcore.utils.Utils;
import org.jetbrains.annotations.NotNull;

public class DelwarpCommand extends CommandHandler
{
	@Override
	public boolean IsRunnableByPlayer() { return true; }
	@Override
	public boolean IsRunnableByConsole() { return true; }

	@Override
	public boolean Run(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
	{
		if (args.length == 0)
		{
			sender.sendMessage(Utils.ApplyColors("&cPlease specify a warp to delete"));
			return true;
		}
		
		if (Utils.GetWarp(args[0]) == null)
		{
			sender.sendMessage(Utils.ApplyColors("&cWarp &e" + args[0] + " &cdoes not exist"));
			return true;
		}
		
		if (Utils.SetWarp(args[0], null))
		{
			sender.sendMessage(Utils.ApplyColors("&aDeleted warp &e" + args[0]));
		}
		else
		{
			sender.sendMessage(Utils.ApplyColors("&cCouldn't delete warp &e" + args[0]));
		}
		
		return true;
	}
}
