package dev.pixlcore.commands;

import dev.pixlcore.core.EntryPoint;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.pixlcore.utils.Utils;
import org.jetbrains.annotations.NotNull;

public class InvseeCommand extends CommandHandler
{
	@Override
	public boolean IsRunnableByPlayer() { return true; }
	
	@Override
	public boolean Run(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
	{
		Player player = (Player)sender;
		
		if (args.length == 0)
		{
			player.sendMessage(Utils.ApplyColors("&cPlease specify a player"));
			return true;
		}
		
		Player src = EntryPoint.GetInstance().getServer().getPlayer(args[0]);
		
		if (src == null)
		{
			player.sendMessage(Utils.ApplyColors("&cPlayer " + args[0] + " does not exist"));
			return true;
		}
		
		player.sendMessage(Utils.ApplyColors("&aOpening " + args[0] + "'s inventory"));
		player.openInventory(src.getInventory());
		
		return true;
	}

}
