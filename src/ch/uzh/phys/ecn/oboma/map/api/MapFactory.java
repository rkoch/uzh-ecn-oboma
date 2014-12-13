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
package ch.uzh.phys.ecn.oboma.map.api;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import ch.uzh.phys.ecn.oboma.map.model.Node;
import ch.uzh.phys.ecn.oboma.map.model.NodeMap;
import ch.uzh.phys.ecn.oboma.map.model.TrainNode;


public abstract class MapFactory {

    private static final Logger LOGGER = Logger.getLogger(MapFactory.class.getName());


    public static INodeMap buildDefaultSBBMap()
            throws Exception {
        NodeMap map = new NodeMap();
        Random rand = new Random();
        int stationCount = 0;
        int trainCount = 0;

        JSONParser parser = new JSONParser();

        try {
            JSONArray stations = (JSONArray) parser.parse(Files.newBufferedReader(Paths.get("data/stations.json")));
            for (Object o : stations) {
                JSONObject station = (JSONObject) o;

                String id = (String) station.get("id");
                String name = (String) station.get("name");
                JSONObject coord = (JSONObject) ((JSONObject) station.get("location")).get("wgs");
                Double lat = (Double) coord.get("y");
                Double lng = (Double) coord.get("x");

                Node node = new Node(id, name, lat, lng, 0, 0);
                map.add(node);
                stationCount++;

                LOGGER.info(String.format("Station Node %s (%s) added", id, name));
            }

            JSONArray connections = (JSONArray) parser.parse(Files.newBufferedReader(Paths.get("data/connections.json")));
            for (Object o : connections) {
                JSONObject connection = (JSONObject) o;

                String from = (String) connection.get("from");
                String to = (String) connection.get("to");
                int time = (int) ((long) connection.get("time"));

                String id1 = String.format("%s-%s", from, to);
                String name1 = String.format("Train %s - %s", from, to);
                String id2 = String.format("%s-%s", to, from);
                String name2 = String.format("Train %s - %s", to, from);
                int seats = rand.nextInt(1000) + 1;

                Node node1 = new TrainNode(id1, name1, 0, 0, time, seats);
                Node node2 = new TrainNode(id2, name2, 0, 0, time, seats);
                try {
                    map.add(node1, from, to);
                    map.add(node2, to, from);
                    trainCount++;
                    LOGGER.info(String.format("Train Node %s (%s -> %s) added (%d seats)", id1, from, to, seats));
                } catch (Exception pEx) {
                    // either from or to do not exist
                }
            }

            LOGGER.info(String.format("%d stations added / %d trains added", stationCount, trainCount));
        } catch (Exception pEx) {
            throw new Exception("Could not build map", pEx);
        }

        return map;
    }

}
