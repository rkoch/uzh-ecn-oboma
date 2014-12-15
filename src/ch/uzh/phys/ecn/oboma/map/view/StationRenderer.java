/*
 * The MIT License (MIT)
 * Copyright © 2014 different authors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the “Software”), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package ch.uzh.phys.ecn.oboma.map.view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointRenderer;

import ch.uzh.phys.ecn.oboma.agents.model.Agent;
import ch.uzh.phys.ecn.oboma.common.DiseaseConstants;
import ch.uzh.phys.ecn.oboma.common.DisplayConstants;


public class StationRenderer
        implements WaypointRenderer<NodeWaypoint> {

    private static final double CONST_SIZE = 50d;
    private static final double MAX_SIZE   = 500d;

    @Override
    public void paintWaypoint(Graphics2D pG, JXMapViewer pMap, NodeWaypoint pWaypoint) {
        Point2D point = pMap.getTileFactory().geoToPixel(pWaypoint.getPosition(), pMap.getZoom());

//        boolean isEmpty = pWaypoint.getNode().getOrigins().isEmpty() && pWaypoint.getNode().getDestinations().isEmpty();
        double stationSize = CONST_SIZE / pMap.getZoom();
        double maxHeight = MAX_SIZE / pMap.getZoom();
        double singleWidth = maxHeight / 6;

        // Draw station point
        pG.setColor(Color.BLACK);
        pG.fill(new Ellipse2D.Double(point.getX() - stationSize / 2, point.getY() - stationSize / 2, stationSize, stationSize));

        // base point
        double bottom = point.getY() - stationSize;
        double left = point.getX() - singleWidth * 2;

        int immune = 0;
        int susceptible = 0;
        int infected = 0;
        int recovered = 0;
        List<Agent> agents = pWaypoint.getNode().getAllAgents();
        for (Agent a : agents) {
            switch (a.getState()) {
                case IMMUNE:
                    immune++;
                    break;
                case INFECTED:
                    infected++;
                    break;
                case RECOVERED:
                    recovered++;
                    break;
                case SUSCEPTIBLE:
                    susceptible++;
                    break;
            }
        }
        int all = immune + susceptible + infected + recovered;

        double divisor;
        if (DisplayConstants.ABSOLUTE) {
            divisor = DiseaseConstants.MAX_POPULATION / 5d / 3;
        } else {
            divisor = all;
        }
        // draw immune -> blue
        double height = ((double) immune) / divisor * maxHeight;
        height = Math.min(height, maxHeight);
        pG.setColor(Color.BLUE);
        pG.fill(new Rectangle2D.Double(left, bottom - height, singleWidth, height));

        // draw susceptible -> orange
        left += singleWidth;
        height = ((double) susceptible) / divisor * maxHeight;
        height = Math.min(height, maxHeight);
        pG.setColor(Color.ORANGE);
        pG.fill(new Rectangle2D.Double(left, bottom - height, singleWidth, height));

        // draw infected -> red
        left += singleWidth;
        height = ((double) infected) / divisor * maxHeight;
        height = Math.min(height, maxHeight);
        pG.setColor(Color.RED);
        pG.fill(new Rectangle2D.Double(left, bottom - height, singleWidth, height));

        // draw recovered -> green
        left += singleWidth;
        height = ((double) recovered) / divisor * maxHeight;
        height = Math.min(height, maxHeight);
        pG.setColor(Color.GREEN);
        pG.fill(new Rectangle2D.Double(left, bottom - height, singleWidth, height));
    }

}
