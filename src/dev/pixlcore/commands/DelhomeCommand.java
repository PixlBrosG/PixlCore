package dev.pixlcore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.pixlcore.utils.Utils;
import org.jetbrains.annotations.NotNull;

public class DelhomeCommand extends CommandHandler
{
	@Override
	public boolean IsRunnableByPlayer() { return true; }
	
	@Override
	public boolean Run(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
	{
		Player player = (Player)sender;
		
		if (Utils.GetHome(player.getUniqueId()) == null)
		{
			sender.sendMessage(Utils.ApplyColors("&cHome is not set"));
			return true;
		}
		
		if (Utils.SetHome(player.getUniqueId(), null))
		{
			sender.sendMessage(Utils.ApplyColors("&aDeleted home"));
		}
		else
		{
			sender.sendMessage(Utils.ApplyColors("&cCouldn't delete home"));
		}
		
		return true;
	}
}
