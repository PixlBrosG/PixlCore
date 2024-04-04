package dev.pixlcore.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import dev.pixlcore.utils.Utils;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand extends CommandHandler
{
	@Override
	public boolean IsRunnableByPlayer() { return true; }

	@Override
	public boolean Run(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
	{
		Player player = (Player)sender;
		Location spawn = Utils.GetWarp("spawn");
		
		if (spawn == null)
		{
			player.sendMessage(Utils.ApplyColors("&cSpawn is not set"));
		}
		else
		{
			player.sendMessage(Utils.ApplyColors("&aTeleporting spawn"));
			player.teleport(spawn);
		}
		
		return true;
	}

}
