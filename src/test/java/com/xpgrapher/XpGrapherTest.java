package com.xpgrapher;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class XpGrapherTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(XpGrapherPlugin.class);
		RuneLite.main(args);
	}
}