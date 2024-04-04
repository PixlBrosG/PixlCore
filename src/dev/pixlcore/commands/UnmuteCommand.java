package dev.pixlcore.commands;

import dev.pixlcore.core.EntryPoint;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.pixlcore.utils.Utils;
import org.jetbrains.annotations.NotNull;

public class UnmuteCommand extends CommandHandler
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
			sender.sendMessage(Utils.ApplyColors("&cPlease specify a player to unmute"));
			return true;
		}
		
		Player playerToUnMute = EntryPoint.GetInstance().getServer().getPlayer(args[0]);
		
		if (playerToUnMute == null)
		{
			sender.sendMessage(Utils.ApplyColors("&cInvalid player &e" + args[0]));
			return true;
		}
		
		if (!EntryPoint.GetInstance().GetMuteManager().IsMuted(playerToUnMute.getUniqueId()))
		{
			sender.sendMessage(Utils.ApplyColors("&e" + args[0] + " &cis not currently muted"));
			return true;
		}
		
		EntryPoint.GetInstance().GetMuteManager().UnMutePlayer(playerToUnMute.getUniqueId());
		sender.sendMessage(Utils.ApplyColors("&e" + playerToUnMute.getName() + " &ais no longer muted"));
		
		return true;
	}
}
