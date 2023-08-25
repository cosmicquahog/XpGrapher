package com.xpgrapher;

import com.google.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;

import java.awt.*;
import java.text.DecimalFormat;

public class XpGrapherOverlay extends OverlayPanel {

    private final Client client;
    private XpGrapherPlugin grapherPlugin;


    @Inject
    private XpGrapherOverlay(Client client, XpGrapherPlugin grapherPlugin) {
        this.client = client;
        this.grapherPlugin = grapherPlugin;
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPosition(OverlayPosition.BOTTOM_LEFT);

    }

    private void print(String name, Object object) {
        System.out.println(name + ": " + object);
    }

    LayoutableRenderableEntity graphEntity = new LayoutableRenderableEntity() {
        @Override
        public Dimension render(Graphics2D graphics) {
            int numberOfDivisions = grapherPlugin.getConfig().verticalDivisions();
            graphics.setColor(Color.BLACK);
            graphics.drawRect(0, 0, grapherPlugin.width, grapherPlugin.height);

            graphics.setColor(Color.WHITE);
            int numberOfPoints = grapherPlugin.graphPoints.size();


            DecimalFormat formatter = new DecimalFormat("#,###");

            int xpRange = grapherPlugin.maximumXp - grapherPlugin.minimumXp;

            graphics.setFont(new Font("Arial", Font.PLAIN, 12));
            for (int i = 0; i < numberOfDivisions; i++) {
                graphics.setColor(new Color(50, 50, 50));
                graphics.drawLine(0, i*grapherPlugin.height/numberOfDivisions, grapherPlugin.width, i*grapherPlugin.height/numberOfDivisions);
                int xpAtMark = grapherPlugin.maximumXp - xpRange/numberOfDivisions*i;

                String xpAtMarkString = Integer.toString(xpAtMark);
                int xpAtMarkStringWidth = graphics.getFontMetrics().stringWidth(xpAtMarkString);
                int xpAtMarkStringHeight = graphics.getFontMetrics().getHeight();
                graphics.setColor(Color.WHITE);
                String xpAtMarkStringFormatted = formatter.format(xpAtMark);
                int xpAtNextMark = grapherPlugin.maximumXp - xpRange/numberOfDivisions*(i+1);
                if (xpAtNextMark != xpAtMark)
                    graphics.drawString(xpAtMarkStringFormatted, grapherPlugin.width+5, i*grapherPlugin.height/numberOfDivisions + xpAtMarkStringHeight/2 - 4);
            }
            graphics.setColor(Color.WHITE);
            int xpAtBottom = grapherPlugin.minimumXp;
            String xpAtBottomString = Integer.toString(xpAtBottom);
            int xpAtBottomStringWidth = graphics.getFontMetrics().stringWidth(xpAtBottomString);
            int xpAtBottomStringHeight = graphics.getFontMetrics().getHeight();


            String xpAtBottomStringFormatted = formatter.format(xpAtBottom);
            graphics.drawString(xpAtBottomStringFormatted, grapherPlugin.width+5, grapherPlugin.height + xpAtBottomStringHeight/2 - 4);



            graphics.setColor(Color.WHITE);
            int oldX = -1;
            int oldY = -1;
            for (int i = 0; i < numberOfPoints; i++) {
                Integer[] point = grapherPlugin.graphPoints.get(i);
                int x = point[0];
                int y = point[1];
                if (y < grapherPlugin.height && y >= 0)
                    graphics.setColor(new Color(255,255,255,50));
                    graphics.drawLine(x, grapherPlugin.height, x, y);
                    graphics.setColor(Color.WHITE);
                    graphics.drawLine(x, y, x, y);
                if (oldX != -1 && oldY != -1) {
                    graphics.drawLine(oldX+1, oldY, x, y);
                }

                oldX = x;
                oldY = y;
            }
            String skillName = grapherPlugin.getConfig().skillToGraph().getName() + " XP";
            int skillNameWidth = graphics.getFontMetrics().stringWidth(skillName);
            int skillNameHeight = graphics.getFontMetrics().getHeight();

            graphics.drawString(skillName, grapherPlugin.width/2-skillNameWidth/2, -skillNameHeight/2);


            String timeLabel = "";

            //bottom time label
            if (grapherPlugin.getConfig().sessionTimeSet()) {
                timeLabel = "Last " + Integer.toString(grapherPlugin.getConfig().sessionLength()) + " minutes";
                int timeLabelWidth = graphics.getFontMetrics().stringWidth(timeLabel);
                int timeLabelHeight = graphics.getFontMetrics().getHeight();
                int timeLabelX = grapherPlugin.width/2 - timeLabelWidth/2;
                int timeLabelY = grapherPlugin.height + timeLabelHeight;
                graphics.drawString(timeLabel, timeLabelX, timeLabelY);

            } else {
                int tickLength = grapherPlugin.xpList.size();
                int secondLength = (int)((double)tickLength*0.61);
                int minutes = secondLength/60;
                int seconds = secondLength%60;
                timeLabel = minutes + "m " + seconds + "s";

                int timeLabelWidth = graphics.getFontMetrics().stringWidth(timeLabel);
                int timeLabelHeight = graphics.getFontMetrics().getHeight();
                int timeLabelX = grapherPlugin.width - timeLabelWidth/2;
                int timeLabelY = grapherPlugin.height + timeLabelHeight;

                graphics.drawString(timeLabel, timeLabelX, timeLabelY);
                graphics.drawString("0", -graphics.getFontMetrics().stringWidth("0")/2, timeLabelY);

            }




            return new Dimension(grapherPlugin.width, grapherPlugin.height);
        }

        @Override
        public Rectangle getBounds() {
            return new Rectangle(grapherPlugin.width, grapherPlugin.height);
        }

        @Override
        public void setPreferredLocation(Point position) {
            return;
        }

        @Override
        public void setPreferredSize(Dimension dimension) {
            return;
        }
    };

    @Override
    public Dimension render(Graphics2D graphics)
    {

        panelComponent.getChildren().add(graphEntity);
        panelComponent.setBackgroundColor(new Color(0, 0, 0, 0));

        return super.render(graphics);
    }

}

