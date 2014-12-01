package ch.uzh.phys.ecn.oboma.functions.sisfunction;

import ch.uzh.phys.ecn.oboma.agents.model.Agent;
import ch.uzh.phys.ecn.oboma.map.model.Node;


public class Test {
    
    public static void main(String[] args){
        
        SISFunctionTrain s = new SISFunctionTrain();
        Agent a = new Agent("Bla");
        Node n = new Node("Bla", 0.0d, 1, 1, s);
        
        s.apply(a, n);
    }


    
    
}
