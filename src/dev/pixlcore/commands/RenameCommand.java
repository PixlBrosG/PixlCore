package dev.pixlcore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.pixlcore.utils.Utils;
import org.jetbrains.annotations.NotNull;

public class RenameCommand extends CommandHandler
{
	@Override
	public boolean IsRunnableByPlayer() { return true; }

	@Override
	public boolean  Run(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
	{
		if (!(sender instanceof Player player))
			return true;

		if (args.length == 0)
		{
			player.sendMessage(Utils.ApplyColors("Please specify name"));
			return true;
		}
		
		ItemStack heldItem = player.getInventory().getItemInMainHand();
		
		if (heldItem.getType().isAir())
		{
			player.sendMessage(Utils.ApplyColors("&cNo held item to rename!"));
			return true;
		}

		ItemMeta meta = heldItem.getItemMeta();

		if (meta != null)
		{
			meta.setDisplayName(Utils.ApplyColors(String.join(" ", args)));
			heldItem.setItemMeta(meta);
			player.sendMessage(Utils.ApplyColors("&aRenamed item to &r" + meta.getDisplayName()));
		}
		else
		{
			player.sendMessage(Utils.ApplyColors("&cFailed to rename item"));
		}

		return true;
	}

}
