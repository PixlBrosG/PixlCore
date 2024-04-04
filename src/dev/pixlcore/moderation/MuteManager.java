package dev.pixlcore.moderation;

import dev.pixlcore.core.EntryPoint;
import dev.pixlcore.utils.Pair;
import dev.pixlcore.utils.Utils;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class MuteManager
{
	private HashMap<UUID, Pair<MutedPlayer, BukkitTask>> m_MutedPlayers;

	public MuteManager()
	{
		m_MutedPlayers = new HashMap<UUID, Pair<MutedPlayer, BukkitTask>>();

		LoadMutedPlayers();
	}

	private void LoadMutedPlayers()
	{
		File configFile = new File(EntryPoint.GetInstance().getDataFolder(), "moderation.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(configFile);

		ConfigurationSection section = cfg.getConfigurationSection("muted");
		if (section == null)
			return;

		for (String key : section.getKeys(false))
		{
			UUID uuid = UUID.fromString(key);
			LoadMutedPlayer(uuid);
		}
	}

	public void LoadMutedPlayer(UUID playerID)
	{
		File configFile = new File(EntryPoint.GetInstance().getDataFolder(), "moderation.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(configFile);

		if (EntryPoint.GetInstance().getServer().getPlayer(playerID) == null)
			return;

		ConfigurationSection mutedPlayerSection = cfg.getConfigurationSection("muted." + playerID.toString());
		if (mutedPlayerSection == null)
			return;

		MutedPlayer mutedPlayer = new MutedPlayer(mutedPlayerSection);

		int duration = mutedPlayer.GetDuration();

		if (duration == 0)
		{
			UnMutePlayer(playerID);
			return;
		}

		if (duration == -1)
		{
			m_MutedPlayers.put(playerID, new Pair<MutedPlayer, BukkitTask>(mutedPlayer, null));
			return;
		}

		BukkitTask unMuteTask = new BukkitRunnable()
		{
			@Override
			public void run()
			{
				UnMutePlayer(playerID);
			}
		}.runTaskLaterAsynchronously(EntryPoint.GetInstance(), duration * 20L);

		m_MutedPlayers.put(playerID, new Pair<MutedPlayer, BukkitTask>(mutedPlayer, unMuteTask));
	}

	public void UnMutePlayer(UUID playerID)
	{
		if (m_MutedPlayers.containsKey(playerID))
		{
			Pair<MutedPlayer, BukkitTask> mutedPlayer = m_MutedPlayers.get(playerID);

			if (mutedPlayer.right != null)
				mutedPlayer.right.cancel();

			m_MutedPlayers.remove(playerID);
		}

		Player p = EntryPoint.GetInstance().getServer().getPlayer(playerID);
		if (p != null)
		{
			p.sendMessage(Utils.ApplyColors("&aYou are no longer muted!"));

			String unMuteMessage = Utils.ApplyColors("&e" + p.getName() + " &ais no longer muted");
			EntryPoint.GetInstance().getServer().broadcastMessage(unMuteMessage);
		}

		File configFile = new File(EntryPoint.GetInstance().getDataFolder(), "moderation.yml");
		FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

		config.set("muted." + playerID.toString(), null);

		try
		{
			config.save(configFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void MutePlayer(UUID playerID, String mutedBy, int duration, String reason)
	{
		MutedPlayer mutedPlayer = new MutedPlayer(mutedBy, reason, duration);

		if (duration == -1)
		{
			m_MutedPlayers.put(playerID, new Pair<MutedPlayer, BukkitTask>(mutedPlayer, null));

			File configFile = new File(EntryPoint.GetInstance().getDataFolder(), "moderation.yml");
			mutedPlayer.Save(configFile, playerID);

			return;
		}

		BukkitTask unMuteTask = new BukkitRunnable()
		{
			@Override
			public void run()
			{
				UnMutePlayer(playerID);
			}
		}.runTaskLaterAsynchronously(EntryPoint.GetInstance(), duration * 20L);

		m_MutedPlayers.put(playerID, new Pair<MutedPlayer, BukkitTask>(mutedPlayer, unMuteTask));

		File configFile = new File(EntryPoint.GetInstance().getDataFolder(), "moderation.yml");
		mutedPlayer.Save(configFile, playerID);
	}

	public boolean IsMuted(UUID playerID)
	{
		return m_MutedPlayers.containsKey(playerID);
	}

	public String GetReason(UUID playerID)
	{
		return m_MutedPlayers.get(playerID).left.GetReason();
	}

	public HashMap<UUID, Pair<MutedPlayer, BukkitTask>> GetMutedPlayers()
	{
		return m_MutedPlayers;
	}
}
