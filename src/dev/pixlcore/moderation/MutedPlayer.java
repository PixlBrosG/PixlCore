package dev.pixlcore.moderation;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.UUID;

public class MutedPlayer
{
	private final String m_Reason;
	private final String m_MutedBy;
	private final LocalTime m_StartTime;
	private final int m_Duration; // -1 for permanent

	public MutedPlayer(ConfigurationSection config)
	{
		m_Reason = config.getString("reason", "undefined");
		m_MutedBy = config.getString("muted_by", "undefined");
		m_StartTime = LocalTime.parse(config.getString("start_time", ""));
		m_Duration = config.getInt("duration", 0);
	}

	public MutedPlayer(String mutedBy, String reason, int duration)
	{
		m_Reason = reason;
		m_MutedBy = mutedBy;
		m_StartTime = LocalTime.now();
		m_Duration = duration;
	}

	public void Save(File configFile, UUID playerID)
	{
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(configFile);

		cfg.set("muted." + playerID + ".reason", m_Reason);
		cfg.set("muted." + playerID + ".muted_by", m_MutedBy);
		cfg.set("muted." + playerID + ".start_time", m_StartTime.toString());
		cfg.set("muted." + playerID + ".duration", m_Duration);

		try
		{
			cfg.save(configFile);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public int GetDuration()
	{
		if (m_Duration == -1)
			return -1;

		int timeSince = (int)Duration.between(m_StartTime, LocalTime.now()).getSeconds();

		if (timeSince > m_Duration)
			return 0;

		return m_Duration - timeSince;
	}

	public String GetReason()
	{
		return m_Reason;
	}
}
