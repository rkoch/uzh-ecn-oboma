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
package ch.uzh.phys.ecn.oboma.control;

import java.util.List;

import ch.uzh.phys.ecn.oboma.functions.sisfunction.SISFunction;
import ch.uzh.phys.ecn.oboma.functions.sisfunction.SISFunctionStation;
import ch.uzh.phys.ecn.oboma.functions.sisfunction.SISFunctionTrain;
import ch.uzh.phys.ecn.oboma.map.api.INode;
import ch.uzh.phys.ecn.oboma.map.api.INodeMap;
import ch.uzh.phys.ecn.oboma.map.api.MapFactory;
import ch.uzh.phys.ecn.oboma.map.view.MapWindow;


public class ObomaApp {

    public void run(String... pArgs) {
        try {
            INodeMap map = MapFactory.buildDefaultSBBMap();

            // TODO rma: Add agents to map
            // TODO retwet: Set infection functions on nodes
            setInfectionFunctions(map);

            MapWindow window = new MapWindow(map);
            window.setVisible(true);

            while (true) {
                map.preelapse();
                map.infect();
                map.postelapse();
                window.repaint();
                Thread.sleep(1000);
            }
        } catch (Exception pEx) {
            pEx.printStackTrace();
        }
    }
    
    private void setInfectionFunctions(INodeMap pMap){
        List<INode> nodes = pMap.getNodes();
        
        for(INode node : nodes){
            SISFunction function;
            
            if(node.isConnecting()){
                function = new SISFunctionTrain(); 
            } else {
                function = new SISFunctionStation();
            }
            
            node.setTransformationFunction(function);
            
        }
    }


    public static void main(String... pArgs) {
        ObomaApp app = new ObomaApp();
        app.run(pArgs);
    }

}
