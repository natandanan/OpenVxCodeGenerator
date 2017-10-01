/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BE;

import java.util.ArrayList;

/**
 *
 * @author elyasaf
 */
public class ConnectionGraph {
    private ArrayList<Connection> connections;
    //private String name;
    private int id;
    private GraphInfo graphInfo;
   

    public ConnectionGraph() {
        connections = new ArrayList<>();
        graphInfo = new GraphInfo();
    }

    /**
     * @return the connections
     */
    public ArrayList<Connection> getConnections() {
        return connections;
    }

    /**
     * @param connections the connections to set
     */
    public void setConnections(ArrayList<Connection> connections) {
        this.connections = connections;
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

    
    public Kernel getKernelByid(int id){
        for (Connection con : getConnections()){
            if (con.father.getNumber() == id )
                    return con.father;
            for(Kernel k: con.children)
                if (k.getNumber() == id )
                    return k; 
        }
        return null; 
    }
    
    public static boolean findRoot(ArrayList<Connection> connections, Kernel father) {
        for (Connection c :connections)
            for (Kernel k: c.children)
                if (father.equals(k))
                   return false;
    
        return true;
    }

    /**
     * @return the graphInfo
     */
    public GraphInfo getGraphInfo() {
        return graphInfo;
    }

    /**
     * @param graphInfo the graphInfo to set
     */
    public void setGraphInfo(GraphInfo graphInfo) {
        this.graphInfo = graphInfo;
    }
}
