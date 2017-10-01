/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BE;

import BE.Node;
import java.util.ArrayList;
import BE.ImageInfo;

/**
 *
 * @author elyasaf
 */
public class NodeGraph {
    //TODO delete public decleration
    private ArrayList<Node> nodes;
    //private String name;
    private int id;
    private GraphInfo graphInfo;



    public NodeGraph() {
    nodes = new ArrayList<>();
    }

    
    
    
    /**
     * @return the nodes
     */
    public ArrayList<Node> getNodes() {
        return nodes;
    }

    /**
     * @param nodes the nodes to set
     */
    public void setNodes(ArrayList<Node> nodes) {
        this.nodes = nodes;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    public void setGraphInfo(GraphInfo graphInfo) {
        this.graphInfo = graphInfo;
    }

    public GraphInfo getGraphInfo() {
        return graphInfo;
    }
    
    
}

