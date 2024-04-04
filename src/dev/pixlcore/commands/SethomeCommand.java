package dev.pixlcore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.pixlcore.utils.Utils;
import org.jetbrains.annotations.NotNull;

public class SethomeCommand extends CommandHandler
{
	public boolean IsRunnableByPlayer() { return true; }
	
	@Override
	public boolean Run(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
	{
		Player player = (Player)sender;
		
		if (Utils.SetHome(player.getUniqueId(), player.getLocation()))
			player.sendMessage(Utils.ApplyColors("&aSet home to current location"));
		else
			player.sendMessage(Utils.ApplyColors("&cFailed to set home!"));
		
		return true;
	}
}
