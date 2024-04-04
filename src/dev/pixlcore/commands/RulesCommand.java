package dev.pixlcore.commands;

import java.io.File;
import java.util.List;

import dev.pixlcore.core.EntryPoint;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import dev.pixlcore.utils.Utils;
import org.jetbrains.annotations.NotNull;

public class RulesCommand extends CommandHandler
{
	@Override
	public boolean IsRunnableByPlayer() { return true; }
	@Override
	public boolean IsRunnableByConsole() { return true; }

	@Override
	public boolean Run(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
	{
		File configFile = new File(EntryPoint.GetInstance().getDataFolder(), "config.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
		
		List<String> rules = config.getStringList("rules");
		
		if (rules.isEmpty())
		{
			sender.sendMessage(Utils.ApplyColors("&cThere are currently no defined rules on this server"));
			return true;
		}
		
		sender.sendMessage(Utils.ApplyColors("&4Server Rules:"));
		for (String rule : rules)
		{
			sender.sendMessage(Utils.ApplyColors("&c- &6" + rule));
		}
		
		return true;
	}
}
