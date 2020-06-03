package com.xpgrapher;

import net.runelite.api.Skill;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.ui.overlay.OverlayPosition;

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
		return 240;
	}

	@ConfigItem(
			position = 1,
			keyName = "graphHeight",
			name = "Graph Height",
			description = "Configures the height of the graph."
	)
	default int graphHeight()
	{
		return 130;
	}

	@ConfigItem(
			position = 2,
			keyName = "resetGraph",
			name = "Reset data",
			description = "Start over with tracking"
	)
	default boolean resetGraph()
	{
		return false;
	}

	@ConfigItem(
			position = 3,
			keyName = "skillToGraph",
			name = "Skill to Graph",
			description = "Choose what skill to graph."
	)
	default Skill skillToGraph()
	{
		return Skill.FLETCHING;
	}

	@ConfigItem(
			position = 4,
			keyName = "overlayPosition",
			name = "Overlay Position",
			description = "Choose where to display the graph"
	)
	default OverlayPosition overlayPosition()
	{
		return OverlayPosition.ABOVE_CHATBOX_RIGHT;
	}

	@ConfigItem(
			position = 5,
			keyName = "goalXPExists",
			name = "Goal XP",
			description = "Set the graph maxiumum to and XP goal"
	)
	default boolean goalXPExists()
	{
		return false;
	}

	@ConfigItem(
			position = 6,
			keyName = "goalXP",
			name = "Goal XP",
			description = "If xp goal is toggled, this will be the graph max XP"
	)
	default int goalXP()
	{
		return 5000000;
	}

	@ConfigItem(
			position = 7,
			keyName = "sessionTimeSet",
			name = "Choose to set a session time or not",
			description = "If this is on, the graph width will be the set time frame. Otherwise it's the entire session."
	)
	default boolean sessionTimeSet()
	{
		return false;
	}

	@ConfigItem(
			position = 8,
			keyName = "sessionLength",
			name = "Session Length (minutes)",
			description = "The width of the graph is the set length of time. Only if 'session time' toggled on."
	)
	default int sessionLength()
	{
		return 1;
	}
}
