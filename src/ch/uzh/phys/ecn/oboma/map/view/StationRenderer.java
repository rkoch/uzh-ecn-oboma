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

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.WaypointRenderer;


public class StationRenderer
        implements WaypointRenderer<NodeWaypoint> {

    private static final double CONST_SIZE = 100d;

    @Override
    public void paintWaypoint(Graphics2D pG, JXMapViewer pMap, NodeWaypoint pWaypoint) {
        Point2D point = pMap.getTileFactory().geoToPixel(pWaypoint.getPosition(), pMap.getZoom());

        double stationSize = CONST_SIZE / pMap.getZoom();

        // Draw station point
        pG.setColor(Color.BLACK);
        pG.fill(new Ellipse2D.Double(point.getX() - stationSize / 2, point.getY() - stationSize / 2, stationSize, stationSize));

        // Draw circles around
        pG.setColor(Color.RED);
        pG.draw(new Ellipse2D.Double(point.getX() - stationSize / 2 - 5, point.getY() - stationSize / 2 - 5, stationSize + 5, stationSize + 5));
    }

}
