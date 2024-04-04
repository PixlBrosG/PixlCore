package dev.pixlcore.commands;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.pixlcore.core.EntryPoint;
import dev.pixlcore.utils.Utils;

import org.jetbrains.annotations.NotNull;

public class BackCommand extends CommandHandler
{
	@Override
	public boolean IsRunnableByPlayer() { return true; }
	
	@Override
	public boolean Run(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
	{
		Player player = (Player)sender;
		
		HashMap<UUID, Location> previousLocations = EntryPoint.GetInstance().GetPreviousLocations();
		
		if (previousLocations.containsKey(player.getUniqueId()))
		{
			player.sendMessage(Utils.ApplyColors("&aTeleporting to last location!"));
			player.teleport(previousLocations.get(player.getUniqueId()));
		}
		else
		{
			player.sendMessage(Utils.ApplyColors("&cPrevious location not found"));
		}
		
		return true;
	}
}
