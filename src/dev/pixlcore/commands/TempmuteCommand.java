package dev.pixlcore.commands;

import java.util.Arrays;

import dev.pixlcore.core.EntryPoint;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.pixlcore.utils.Utils;
import org.jetbrains.annotations.NotNull;

public class TempmuteCommand extends CommandHandler
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
			sender.sendMessage(Utils.ApplyColors("&cPlease specify a player to mute"));
			return true;
		}
		
		Player playerToMute = EntryPoint.GetInstance().getServer().getPlayer(args[0]);
		
		if (playerToMute == null)
		{
			sender.sendMessage(Utils.ApplyColors("&cInvalid player &e" + args[0]));
			return true;
		}
		
		if (EntryPoint.GetInstance().GetMuteManager().IsMuted(playerToMute.getUniqueId()))
		{
			sender.sendMessage(Utils.ApplyColors("&e" + args[0] + " &cis already muted"));
			return true;
		}
		
		if (args.length == 1)
		{
			sender.sendMessage(Utils.ApplyColors("&cPlease specify a duration to mute &e" + playerToMute.getName() + " &cfor"));
			return true;
		}
		
		int duration;
		
		try
		{
			duration = Integer.parseInt(args[1]);
		}
		catch (NumberFormatException e)
		{
			sender.sendMessage(Utils.ApplyColors("&cInvalid integer &e") + args[1]);
			return true;
		}
		
		String reason;
		if (args.length > 2)
			reason = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
		else
			reason = "no specified reason";
		
		EntryPoint.GetInstance().GetMuteManager().MutePlayer(playerToMute.getUniqueId(), sender.getName(), duration * 60, reason);
		playerToMute.sendMessage(Utils.ApplyColors("&cYou have been temporarily muted by &e" + sender.getName() + " &cfor &e") + reason);
		sender.sendMessage(Utils.ApplyColors("&aTemporarily muted &e" + playerToMute.getName() + " &afor &e") + reason);
		
		String muteMessage = Utils.ApplyColors("&e" + playerToMute.getName() + " &chas been temporarily muted by &e" + sender.getName() + " &cfor &e") + reason;
		EntryPoint.GetInstance().getServer().broadcastMessage(muteMessage);
		
		return true;
	}
}
