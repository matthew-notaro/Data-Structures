package app;

import structures.Arc;
import structures.Graph;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Class used for testing the MST
 */
public class Driver {

    public static void main(String[] args) {
        Graph graph = null;
        try {
            graph = new Graph("graph3.txt");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        
        ArrayList<Arc> arcArrayList = PartialTreeList.execute(PartialTreeList.initialize(graph));
        
        for (int i = 0; i < arcArrayList.size(); i++) {
            Arc anArcArrayList = arcArrayList.get(i);
            System.out.println(anArcArrayList);
        }
    }
}