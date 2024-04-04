package dev.pixlcore.core;

import dev.pixlcore.commands.*;

import dev.pixlcore.moderation.MuteManager;
import dev.pixlcore.utils.Utils;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.Serial;
import java.util.HashMap;
import java.util.UUID;

public class EntryPoint extends JavaPlugin implements Listener
{
	private static EntryPoint s_Instance;

	final private HashMap<UUID, Location> m_PreviousLocations = new HashMap<>();

	private MuteManager m_MuteManager;

	private final HashMap<String, CommandHandler> m_Commands = new HashMap<>()
	{
		@Serial
		private static final long serialVersionUID = -7498714754971256795L;

		{
			put("rename",   new RenameCommand());
			put("invsee",   new InvseeCommand());
			put("discord",  new DiscordCommand());
			put("setspawn", new SetspawnCommand());
			put("delspawn", new DelspawnCommand());
			put("spawn",    new SpawnCommand());
			put("setwarp",  new SetwarpCommand());
			put("delwarp",  new DelwarpCommand());
			put("warps",    new WarpsCommand());
			put("warp",     new WarpCommand());
			put("sethome",  new SethomeCommand());
			put("delhome",  new DelhomeCommand());
			put("home",     new HomeCommand());
			put("back",     new BackCommand());
			put("mute",     new MuteCommand());
			put("tempmute", new TempmuteCommand());
			put("unmute",   new UnmuteCommand());
			put("rules",    new RulesCommand());
		}
	};

	public static EntryPoint GetInstance()
	{
		return s_Instance;
	}

	@Override
	public void onEnable()
	{
		s_Instance = this;

		m_MuteManager = new MuteManager();

		if (!new File(getDataFolder(), "config.yml").exists())
		{
			getConfig().options().copyDefaults();
			saveConfig();
		}

		// Setting to null is okay
		Location spawnLocation = Utils.GetWarp("spawn");
		for (Player player : getServer().getOnlinePlayers())
			m_PreviousLocations.put(player.getUniqueId(), spawnLocation);

		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable()
	{
	}

	public HashMap<UUID, Location> GetPreviousLocations()
	{
		return m_PreviousLocations;
	}

	public MuteManager GetMuteManager()
	{
		return m_MuteManager;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args)
	{
		CommandHandler command = m_Commands.get(cmd.getName().toLowerCase());

		if (command == null)
			return false;

		if (command.IsRunnableByPlayer() && sender instanceof Player)
		{
			sender.sendMessage(Utils.ApplyColors("&cThis command cannot be run by players"));
			return false;
		}

		if (command.IsRunnableByConsole() && !(sender instanceof Player))
		{
			sender.sendMessage(Utils.ApplyColors("&cThis command cannot be run by console"));
			return false;
		}

		return command.Run(sender, cmd, label, args);
	}

	@EventHandler
	public void OnDeath(PlayerDeathEvent event)
	{
		Player player = event.getEntity();
		m_PreviousLocations.put(player.getUniqueId(), player.getLocation());
	}

	@EventHandler
	public void OnJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		UUID playerID = player.getUniqueId();

		Location spawnLocation = Utils.GetWarp("spawn");

		if (spawnLocation != null)
		{
			m_PreviousLocations.put(playerID, spawnLocation);

			if (!player.hasPlayedBefore())
				player.teleport(spawnLocation);
		}

		m_MuteManager.LoadMutedPlayer(playerID);
	}

	public void OnQuit(PlayerQuitEvent event)
	{
		UUID playerID = event.getPlayer().getUniqueId();

		m_PreviousLocations.remove(playerID);
		m_MuteManager.GetMutedPlayers().remove(playerID);
	}

	@EventHandler
	public void OnChat(AsyncPlayerChatEvent event)
	{
		Player player = event.getPlayer();
		UUID playerID = player.getUniqueId();

		if (m_MuteManager.IsMuted(playerID))
		{
			event.setCancelled(true);

			String reason = m_MuteManager.GetReason(playerID);
			player.sendMessage(Utils.ApplyColors("&cYou are currently muted for &e") + reason);
		}
	}

	@EventHandler
	public void OnCommandPreprocess(PlayerCommandPreprocessEvent event)
	{
		Player player = event.getPlayer();
		UUID playerID = player.getUniqueId();

		String command = event.getMessage().toLowerCase();

		if (command.startsWith("/say") || command.startsWith("/me"))
		{
			event.setCancelled(true);

			String reason = m_MuteManager.GetReason(playerID);
			player.sendMessage(Utils.ApplyColors("&cYou are currently muted for &e") + reason);
		}
	}
}
