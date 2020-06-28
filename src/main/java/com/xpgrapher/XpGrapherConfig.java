package com.xpgrapher;

import net.runelite.api.Skill;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("xpgrapher")
public interface XpGrapherConfig extends Config
{

	@ConfigItem(
			position = 0,
			keyName = "graphWidth",
			name = "Graph Width",
			description = "Configures the width of the graph."
	)
	default int graphWidth()
	{
		return 200;
	}

	@ConfigItem(
			position = 1,
			keyName = "graphHeight",
			name = "Graph Height",
			description = "Configures the height of the graph."
	)
	default int graphHeight()
	{
		return 100;
	}

	@ConfigItem(
			position = 2,
			keyName = "resetGraph",
			name = "Reset Graph",
			description = "Start over with all data. You will lose all saved graphs."
	)
	default boolean resetGraph() { return false; }

	
}
