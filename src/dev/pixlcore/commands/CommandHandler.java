package dev.pixlcore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

// TODO: Probably change to interface
public abstract class CommandHandler
{
	public boolean IsRunnableByPlayer() { return false; }
	public boolean IsRunnableByConsole() { return false; }

	public abstract boolean Run(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args);
}
