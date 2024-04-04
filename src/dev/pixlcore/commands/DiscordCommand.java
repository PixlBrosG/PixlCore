package dev.pixlcore.commands;

import java.io.File;

import dev.pixlcore.core.EntryPoint;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import dev.pixlcore.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.NotNull;

public class DiscordCommand extends CommandHandler
{
	@Override
	public boolean IsRunnableByPlayer() { return true; }
	
	@Override
	public boolean Run(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
	{
		File configFile = new File(EntryPoint.GetInstance().getDataFolder(), "config.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);
		
		Player player = (Player)sender;
		
		String url = "https://discord.gg/" + config.getString("discord");
		
		TextComponent msg = new TextComponent(Utils.ApplyColors("&aJoin our discord!"));
		msg.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));
		player.spigot().sendMessage(msg);
		
		return true;
	}

}
