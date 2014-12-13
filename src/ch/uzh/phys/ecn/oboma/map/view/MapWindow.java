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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JFrame;

import org.jxmapviewer.JXMapKit;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.WaypointPainter;

import ch.uzh.phys.ecn.oboma.map.api.INode;
import ch.uzh.phys.ecn.oboma.map.api.INodeMap;


public class MapWindow
        extends JFrame {

    private static final long        serialVersionUID = 7100923426469112767L;

    private static final GeoPosition CH_CENTRE        = new GeoPosition(46.801111111111105, 8.226666666666667);

    private final INodeMap           mMapData;
    private JXMapKit                 mMapKit;

    public MapWindow(INodeMap pMapData) {
        super("OBOMA");

        mMapData = pMapData;

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        build();
    }

    private void build() {
        mMapKit = new JXMapKit();
        getContentPane().add(mMapKit);

        // Create a TileFactoryInfo for OpenStreetMap
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        tileFactory.setThreadPoolSize(8);
        mMapKit.setTileFactory(tileFactory);
        // Set the focus
        mMapKit.setCenterPosition(CH_CENTRE);
        mMapKit.setZoomSliderVisible(true);
        mMapKit.setMiniMapVisible(false);
        mMapKit.setZoom(10);


        // Build and paint stations
        Set<NodeWaypoint> stations = mMapData.getNodes()
                .stream()
                .filter(n -> !n.isConnecting())
                .map(n -> {
                    GeoPosition gp = new GeoPosition(n.getLatitude(), n.getLongitude());
                    NodeWaypoint wp = new NodeWaypoint(gp, n);
                    return wp;
                })
                .collect(Collectors.toSet());

        WaypointPainter<NodeWaypoint> stationPainter = new WaypointPainter<>();
        stationPainter.setWaypoints(stations);
        stationPainter.setRenderer(new StationRenderer());

        // Build and paint routes
        Set<NodeWaypoint> tracks = mMapData.getNodes()
                .stream()
                .filter(INode::isConnecting)
                .map(n -> {
                    GeoPosition gp = new GeoPosition(n.getLatitude(), n.getLongitude());
                    NodeWaypoint wp = new NodeWaypoint(gp, n);
                    return wp;
                })
                .collect(Collectors.toSet());
        WaypointPainter<NodeWaypoint> trackPainter = new WaypointPainter<>();
        trackPainter.setWaypoints(tracks);
        trackPainter.setRenderer(new TrackRenderer());

        // Create a compound painter that uses both the route-painter and the waypoint-painter
        List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
        painters.add(stationPainter);
        painters.add(trackPainter);

        CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
        mMapKit.getMainMap().setOverlayPainter(painter);
    }

}
