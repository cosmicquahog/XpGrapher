package com.xpgrapher;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.ArrayList;

@Slf4j
@PluginDescriptor(
	name = "XP Grapher"
)
public class XpGrapherPlugin extends Plugin {

	@Inject
	private Client client;

	@Inject
	private XpGrapherConfig config;

	@Provides
	XpGrapherConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(XpGrapherConfig.class);
	}

	private int tickNum = 0;

	private Skill skillToGraph = Skill.FLETCHING;
	private int skillXP;

	public int width = 200;
	public int height = 100;

	public ArrayList<Integer> xpList = new ArrayList<Integer>();
	public ArrayList<Integer[]> graphPoints = new ArrayList<Integer[]>();

	private int fletchXpPerHour = 700000;
	private double fletchXpPerMinute = (double)fletchXpPerHour/60;
	private double fletchXpPerSecond = fletchXpPerMinute/60;
	private double fletchXpPerTick = fletchXpPerSecond*0.61;

	@com.google.inject.Inject
	private XpGrapherOverlay overlay;

	@com.google.inject.Inject
	private OverlayManager overlayManager;

	@Override
	public void startUp()
	{
		overlayManager.add(overlay);
	}

	@Override
	public void shutDown()
	{
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onGameTick(GameTick tick)
	{
		//System.out.println(fletchXpPerSecond);

		skillXP = client.getSkillExperience((skillToGraph));
		xpList.add(skillXP);

		update(xpList);
		//for (int i = 0; i < graphPoints.size(); i++) {
		//    int x = (int)(graphPoints.get(i)[0]);
		//    int y = (int)(graphPoints.get(i)[1]);
		//
		//}

		tickNum++;
	}

	public void update(ArrayList xpList) {

		width = config.graphWidth();
		height = config.graphHeight();

		ArrayList<Integer[]> newList = new ArrayList<Integer[]>();

		for (int x = 0; x < width; x++) {

			double ratioAcrossGraph = (double)x/(double)width;

			int dataIndex;
			//if the session time is not set, graph showing whole session
			if (!config.sessionTimeSet()) {
				dataIndex = (int)(Math.floor(ratioAcrossGraph*xpList.size()));
			}
			//if the graph width is a specific timeframe
			else {
				double tickLength = 0.61; //seconds
				double tickLengthMinutes = tickLength/60;
				int ticksInTimeFrame = (int)(config.sessionLength()/tickLengthMinutes);
				int startingTick = tickNum - ticksInTimeFrame;
				int endingTick = startingTick + ticksInTimeFrame;
				//System.out.println(startingTick + " through " + endingTick);
				dataIndex = (int)(ticksInTimeFrame*ratioAcrossGraph) + startingTick;



			}


			int maxXp;
			if (config.goalXPExists()) {
				maxXp = config.goalXP();
			}
			else  {
				maxXp = (int)xpList.get(xpList.size()-1);
			}
			int minXp = (int)xpList.get(0);
			int xpRange = maxXp - minXp;
			int xpGained;
			if (dataIndex >= 0)
				xpGained = (int)xpList.get(dataIndex) - minXp;
			else
				xpGained = 0;

			double ratioVertical = xpGained/(double)xpRange;
			//System.out.println(xpGained/(double)xpRange);
			int y = height - (int)((double)height*ratioVertical);
			//System.out.println(y);

			Integer[] newEntry = {x, y};
			newList.add(newEntry);



		}



		graphPoints = newList;

		if (config.resetGraph()) {
			resetData();
		}

		Skill theSkill = config.skillToGraph();
		//check if the tracked skill changed
		if (theSkill != skillToGraph) {
			skillToGraph = theSkill;
			resetData();
		}



	}

	private void resetData() {
		xpList.clear();
		graphPoints.clear();
		skillToGraph = config.skillToGraph();
		tickNum = 0;
	}

	private void printXpList() {
		for (int i = 0; i < xpList.size(); i++) {
			System.out.print(xpList.get(i) + ", ");
		}
		System.out.println("");
	}

}
