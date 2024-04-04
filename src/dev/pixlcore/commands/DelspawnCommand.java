package dev.pixlcore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import dev.pixlcore.utils.Utils;
import org.jetbrains.annotations.NotNull;

public class DelspawnCommand extends CommandHandler
{
	@Override
	public boolean IsRunnableByPlayer() { return true; }
	@Override
	public boolean IsRunnableByConsole() { return true; }

	@Override
	public boolean Run(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
	{
		if (Utils.GetWarp("spawn") == null)
		{
			sender.sendMessage(Utils.ApplyColors("&cSpawn is not set"));
			return true;
		}
		
		if (Utils.SetWarp("spawn", null))
		{
			sender.sendMessage(Utils.ApplyColors("&aDeleted spawn"));
		}
		else
		{
			sender.sendMessage(Utils.ApplyColors("&cCouldn't delete spawn"));
		}
		
		return true;
	}
}
