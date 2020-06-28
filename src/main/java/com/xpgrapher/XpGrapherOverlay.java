package com.xpgrapher;

import com.google.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.LayoutableRenderableEntity;

import java.awt.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class XpGrapherOverlay extends OverlayPanel {

    private final Client client;
    private XpGrapherPlugin grapherPlugin;

    private int borderThickness = 2;
    private int divisionLineThickness = 1;

    private Color textColor = Color.WHITE;
    private Color graphLineColor = new Color(56, 36, 24);
    private Color backgroundColor = new Color(132, 109,  71, 200);

    private int marginGraphLeft = 43;
    private int marginGraphTop = 11;
    private int marginGraphRight = 12;
    private int marginGraphBottom = 30;
    private int marginTimeLabelTop = 7;

    //private int marginStartMessageTop = 12;
    private int marginStartMessageBottom = 8;

    private int marginLegendRight = 12;
    private int marginLegendTop = 8;
    private int marginLegendBoxBottom = 2;
    private int marginLegendBoxRight = 4;
    private int marginLegendBoxLeft = 4;
    private int marginLegendBoxTop = 2;
    private int legendBoxSize = 10;
    private int legendWidth = 93;

    private int marginVertAxisValueRight = 4;

    private int bottomAxisTickMarkLength = 6;

    @Inject
    private XpGrapherOverlay(Client client, XpGrapherPlugin grapherPlugin) {
        this.client = client;
        this.grapherPlugin = grapherPlugin;
        setLayer(OverlayLayer.ABOVE_WIDGETS);
        setPosition(OverlayPosition.BOTTOM_LEFT);

    }

    private String formatXpString(int xpToFormat) {

        String result;

        if (xpToFormat < 1000)
            result = Integer.toString(xpToFormat);
        else if (xpToFormat < 1000000) {
            int xpInK = xpToFormat/1000;
            String decimalString = Integer.toString(xpToFormat).substring(1);
            String formattedDecimalString = decimalString.substring(0, 2);
            String formattedXpString = xpInK + "." + formattedDecimalString;
            if (formattedXpString.length() > 4) {
                formattedXpString = formattedXpString.substring(0, 4);
            }
            if ((Character.toString(formattedXpString.charAt(formattedXpString.length()-1))).equals(".")) {
                formattedXpString = formattedXpString.substring(0, formattedXpString.length()-1);
            }

            formattedXpString += "K";
            result = formattedXpString;
        }
        else {
            int xpInM = xpToFormat/1000000;
            String decimalString = Integer.toString(xpToFormat).substring(1);
            String formattedDecimalString = decimalString.substring(0, 2);
            String formattedXpString = xpInM + "." + formattedDecimalString;
            if (formattedXpString.length() > 4) {
                formattedXpString = formattedXpString.substring(0, 4);
            }
            if ((Character.toString(formattedXpString.charAt(formattedXpString.length()-1))).equals(".")) {
                formattedXpString = formattedXpString.substring(0, formattedXpString.length()-1);
            }

            formattedXpString += "M";
            result = formattedXpString;
        }



        return result;
    }

    LayoutableRenderableEntity graphEntity = new LayoutableRenderableEntity() {

        @Override
        public Dimension render(Graphics2D graphics) {
            setPosition(OverlayPosition.BOTTOM_LEFT);
            //overlay background box
            graphics.setColor(backgroundColor);
            graphics.fillRect(0, 0, this.getBounds().width, this.getBounds().height);

            //overlay border box
            graphics.setColor(graphLineColor);
            int borderX = 0;
            int borderY = 0;
            graphics.drawRect(borderX, borderY, this.getBounds().width, this.getBounds().height);
            graphics.drawRect(borderX+1, borderY+1, this.getBounds().width-2, this.getBounds().height-2);

            //graph border box
            borderX = marginGraphLeft;
            borderY = marginGraphTop;
            graphics.drawRect(borderX, borderY, grapherPlugin.graphWidth, grapherPlugin.graphHeight);
            graphics.drawRect(borderX-1, borderY-1, grapherPlugin.graphWidth+2, grapherPlugin.graphHeight+2);

            //vertical divisions
            double verticalIncrement = (double)grapherPlugin.graphHeight/(double)grapherPlugin.numVerticalDivisions;
            for (int i = 0; i <= 5; i++) {

                int x = marginGraphLeft;
                int y = (int)(marginGraphTop+verticalIncrement*i);
                graphics.drawLine(x, y, x+grapherPlugin.graphWidth, y);

            }

            //vertical axis values
            int vertAxisMaxValue = grapherPlugin.xpGraphPointManager.maxVertAxisValue;
            int vertAxisXpInc = vertAxisMaxValue/grapherPlugin.numVerticalDivisions;

            for (int i = 0; i <= 5; i++) {
                int xpAtVerticalDiv = vertAxisMaxValue - i*vertAxisXpInc;

                String xpAtVerticalDivString = formatXpString(xpAtVerticalDiv);

                int stringWidth = graphics.getFontMetrics().stringWidth(xpAtVerticalDivString);
                int stringHeight = graphics.getFontMetrics().getHeight();

                if (xpAtVerticalDiv != 0 || i == 5)
                    graphics.drawString(xpAtVerticalDivString, marginGraphLeft-stringWidth-marginVertAxisValueRight, (int)(marginGraphTop+verticalIncrement*i+stringHeight/2-1));


            }

            //actual xp data lines
            for (int i = 0; i < grapherPlugin.skillList.length; i++) {

                Skill skillToGraph = grapherPlugin.skillList[i];
                Color skillColor = grapherPlugin.xpGraphColorManager.getSkillColor(skillToGraph);
                graphics.setColor(skillColor);

                if (grapherPlugin.isSkillShown(skillToGraph)) {

                    int oldX = -1;
                    int oldY = -1;
                    for (int x = 0; x < grapherPlugin.graphWidth; x++) {

                        int y = grapherPlugin.xpGraphPointManager.getGraphPointData(skillToGraph, x);
                        if (y >= 0) {
                            graphics.drawLine(marginGraphLeft+x, marginGraphTop+y, marginGraphLeft+x, marginGraphTop+y);
                            graphics.drawLine(marginGraphLeft+x, marginGraphTop+y+1, marginGraphLeft+x, marginGraphTop+y+1);
                        }

                        if (oldX != -1 && y >= 0) {
                            graphics.drawLine(marginGraphLeft+oldX, marginGraphTop+oldY, marginGraphLeft+x-1, marginGraphTop+y);
                            graphics.drawLine(marginGraphLeft+oldX+1, marginGraphTop+oldY, marginGraphLeft+x, marginGraphTop+y);
                        }
                        oldX = x;
                        oldY = y;
                    }

                }



            }

            //legend border box
            int legendX = marginGraphLeft+grapherPlugin.graphWidth+marginGraphRight;
            int legendY = marginGraphTop;
            graphics.setColor(graphLineColor);
            graphics.drawRect(legendX, legendY, legendWidth, grapherPlugin.graphHeight);
            graphics.drawRect(legendX-1, legendY-1, legendWidth+2, grapherPlugin.graphHeight+2);

            //legend boxes and labels
            int legendYOffset = marginGraphTop+2*marginLegendBoxTop-1;
            for (int i = 0; i < grapherPlugin.skillList.length; i++) {
                Skill theSkill = grapherPlugin.skillList[i];
                if (grapherPlugin.isSkillShown(theSkill)) {
                    //graphics.setColor(dataLineColors[i]);
                    Color skillColor = grapherPlugin.xpGraphColorManager.getSkillColor(theSkill);
                    graphics.setColor(skillColor);
                    int legendBoxX = legendX + marginLegendBoxLeft;
                    graphics.fillRect(legendBoxX, legendYOffset, legendBoxSize, legendBoxSize);
                    graphics.setColor(Color.BLACK);
                    graphics.drawRect(legendBoxX, legendYOffset, legendBoxSize, legendBoxSize);
                    legendYOffset += legendBoxSize + marginLegendBoxBottom;

                    graphics.setColor(graphLineColor);
                    String skillName = theSkill.getName();
                    int skillNameHeight = graphics.getFontMetrics().getHeight();
                    int skillNameX = legendBoxX + legendBoxSize + marginLegendBoxRight;
                    int skillNameY = legendYOffset;
                    graphics.drawString(skillName, skillNameX, skillNameY);
                }
            }

            //bottom axis tick marks
            int bottomLeftGraphX = marginGraphLeft;
            int bottomLeftGraphY = marginGraphTop+grapherPlugin.graphHeight;
            graphics.drawLine(bottomLeftGraphX, bottomLeftGraphY, bottomLeftGraphX, bottomLeftGraphY + bottomAxisTickMarkLength);
            graphics.drawLine(bottomLeftGraphX-1, bottomLeftGraphY, bottomLeftGraphX-1, bottomLeftGraphY + bottomAxisTickMarkLength);
            int bottomRightGraphX = bottomLeftGraphX + grapherPlugin.graphWidth;
            graphics.drawLine(bottomRightGraphX, bottomLeftGraphY, bottomRightGraphX, bottomLeftGraphY + bottomAxisTickMarkLength);
            graphics.drawLine(bottomRightGraphX+1, bottomLeftGraphY, bottomRightGraphX+1, bottomLeftGraphY + bottomAxisTickMarkLength);

            long timePassed = grapherPlugin.currentTime - grapherPlugin.startTime;
            String timeStartLabel = "0:00";
            int timeStartLabelWidth = graphics.getFontMetrics().stringWidth(timeStartLabel);
            int timeStartLabelHeight = graphics.getFontMetrics().getHeight();
            graphics.drawString(timeStartLabel, bottomLeftGraphX-timeStartLabelWidth/2, bottomLeftGraphY+timeStartLabelHeight+marginTimeLabelTop);

            int secondsPassed = (int)(timePassed/1000);
            int timePassedMinutes = secondsPassed/60;
            int secondsLeft = secondsPassed%60;
            String secondsLeftString = Integer.toString(secondsLeft);
            if (secondsLeft < 10) {
                secondsLeftString = "0" + secondsLeftString;
            }
            String timeEndLabel = timePassedMinutes + ":" + secondsLeftString;
            int timeEndLabelWidth = graphics.getFontMetrics().stringWidth(timeEndLabel);
            int timeEndLabelHeight = graphics.getFontMetrics().getHeight();
            graphics.drawString(timeEndLabel, bottomRightGraphX-timeEndLabelWidth/2, bottomLeftGraphY+timeEndLabelHeight+marginTimeLabelTop);

            //show message to start skilling to start the graph
            if (legendYOffset == marginGraphTop+2*marginLegendBoxTop-1) {

                grapherPlugin.startMessageDisplaying = true;

                int overlayWidth = grapherPlugin.graphWidth+marginGraphRight+marginGraphLeft+legendWidth+marginLegendRight;
                int overlayHeight = marginGraphTop+grapherPlugin.graphHeight+marginGraphBottom;

                int messageHeight = graphics.getFontMetrics().getHeight();

                String message1 = "Gain xp to start";
                int message1Width = graphics.getFontMetrics().stringWidth(message1);
                //int message1x = marginGraphLeft+grapherPlugin.graphWidth/2-message1Width/2;
                int message1x = overlayWidth/2-message1Width/2;

                int totalMessageHeight = 3*messageHeight+3*marginStartMessageBottom;
                int startingYOffset = this.getBounds().height/2 - totalMessageHeight/2;
                int message1y = marginGraphTop+startingYOffset;

                String message2 = "Alt+drag to move";
                int message2Width = graphics.getFontMetrics().stringWidth(message2);
                //int message2x = marginGraphLeft+grapherPlugin.graphWidth/2-message2Width/2;
                int message2x = overlayWidth/2-message2Width/2;
                int message2y = message1y+messageHeight+marginStartMessageBottom;

                String message3 = "Change graph size in settings";
                int message3Width = graphics.getFontMetrics().stringWidth(message3);
                //int message3x = marginGraphLeft+grapherPlugin.graphWidth/2-message3Width/2;
                int message3x = overlayWidth/2-message3Width/2;
                int message3y = message2y+messageHeight+marginStartMessageBottom;

                graphics.setColor(new Color(backgroundColor.getRed(), backgroundColor.getGreen(), backgroundColor.getBlue(), 200));
                //graphics.fillRect(marginGraphLeft+1, marginGraphRight, grapherPlugin.graphWidth+marginGraphRight+legendWidth-1, grapherPlugin.graphHeight-1);
                graphics.fillRect(1, 1,
                        grapherPlugin.graphWidth+marginGraphRight+marginGraphLeft+legendWidth+marginLegendRight-1,
                        marginGraphTop+grapherPlugin.graphHeight+marginGraphBottom-1
                );


                graphics.setColor(graphLineColor);
                graphics.drawString(message1, message1x, message1y);
                graphics.drawString(message2, message2x, message2y);
                graphics.drawString(message3, message3x, message3y);

            } else {
                grapherPlugin.startMessageDisplaying = false;
            }

            return new Dimension(this.getBounds().width, this.getBounds().height);

        }

        @Override
        public Rectangle getBounds() {
            int boundsWidth = marginGraphLeft+grapherPlugin.graphWidth+marginGraphRight+legendWidth+marginLegendRight;
            int boundsHeight = marginGraphTop+grapherPlugin.graphHeight+marginGraphBottom;
            return new Rectangle(boundsWidth, boundsHeight);
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

