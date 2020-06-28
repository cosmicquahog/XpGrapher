package com.xpgrapher;

import net.runelite.api.Skill;

import java.awt.*;
import java.util.HashMap;

public class XpGraphColorManager {

    private HashMap<Skill, Color> skillColorMap = new HashMap<Skill, Color>();
    private XpGrapherPlugin grapherPlugin;

    private Color[] skillColorData = {
            //attack
            new Color(79,143,35),
            //Defence
            new Color(115, 115, 115),
            //strength
            new Color(0,64,255),
            //hitpoints
            new Color(143, 35, 35),
            //ranged
            new Color(106,255,0),
            //prayer
            new Color(255,212,0),
            //magic
            new Color(0,149,255),
            //cooking
            new Color(107,35,143),
            //woodcutting
            new Color(35,98,143),
            //fletching
            new Color(255, 127, 0),
            //fishing
            new Color(231, 233, 185),
            //firemaking
            new Color(255, 0, 0),
            //crafting
            new Color(255,0,170),
            //smithing
            new Color(185,237,224),
            //mining
            new Color(204,204,204),
            //herblore
            new Color(191,255,0),
            //agility
            new Color(185,215,237),
            //thieving
            new Color(255,255,0),
            //slayer
            new Color(220,185,237),
            //farming
            new Color(0,234,255),
            //runecraft
            new Color(170,0,255),
            //hunter
            new Color(237, 185, 185),
            //construction
            new Color(220, 190, 255),
            //overall
            new Color(143, 106, 35),
    };

    public XpGraphColorManager(XpGrapherPlugin grapherPlugin) {
        this.grapherPlugin = grapherPlugin;
        for (int i = 0; i < skillColorData.length; i++) {
            skillColorMap.put(grapherPlugin.skillList[i], skillColorData[i]);
        }
    }

    public Color getSkillColor(Skill skill) {
        return skillColorMap.get(skill);
    }

}
