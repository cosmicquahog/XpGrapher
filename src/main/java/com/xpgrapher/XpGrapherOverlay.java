package com.xpgrapher;

import com.google.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;

import java.awt.*;
import java.util.ArrayList;

public class XpGrapherOverlay extends OverlayPanel {

    private final Client client;
    private XpGrapherPlugin grapherPlugin;

    //private int width = 200;
    //private int height = 100;


    @Inject
    private XpGrapherOverlay(Client client, XpGrapherPlugin grapherPlugin) {
        this.client = client;
        this.grapherPlugin = grapherPlugin;
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);

    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        LayoutableRenderableEntity graphEntity = new LayoutableRenderableEntity() {
            @Override
            public Dimension render(Graphics2D graphics) {
                graphics.setColor(Color.GRAY);
                graphics.drawRect(0, 0, grapherPlugin.width, grapherPlugin.height);

                graphics.setColor(Color.WHITE);
                int numberOfPoints = grapherPlugin.graphPoints.size();
                int oldX = -1;
                int oldY = -1;
                for (int i = 0; i < numberOfPoints; i++) {
                    Integer[] point = grapherPlugin.graphPoints.get(i);
                    int x = point[0];
                    int y = point[1];
                    if (y < grapherPlugin.height && y >= 0)
                        graphics.drawLine(x, y, x, y);
                    if (oldX != -1 && oldY != -1) {
                        graphics.drawLine(oldX+1, oldY, x, y);
                    }

                    oldX = x;
                    oldY = y;
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



        panelComponent.getChildren().add(graphEntity);
        panelComponent.setBackgroundColor(new Color(0, 0, 0, 0));






        return super.render(graphics);
    }

}

