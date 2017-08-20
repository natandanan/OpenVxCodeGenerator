/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataProcessing;



import BE.Node;
import BE.NodeGraph;
import java.util.ArrayList;
import java.util.Collections;
import BE.Connection;
import BE.ConnectionGraph;
import BE.Kernel;

/**
 *
 * @author elyasaf
 */

public class NodesToConnectionsConvertor {
    
    public static ConnectionGraph fromNodesToConnections(NodeGraph graph) throws Exception{
    
        //Finding root - assuming there is only one
        Node root = null;
        for (Node nodeIt: graph.getNodes())
            if (nodeIt.getParents().isEmpty())
                    root = nodeIt;
        if (root == null)
            throw new Exception("Failing to find root");
        
      
        //Attach  all the nodes their level
        ComputingLevel(root);
        
        
        //Sort nodes list by levels
        graph.getNodes().sort((o1, o2) -> {
           return o1.getLevel() > o2.getLevel() ? 1 : o1.getLevel() == o2.getLevel() ? 0  : -1;
        });
        
       
        
        ConnectionGraph con_graph = new ConnectionGraph();
        
        //General graph info
       // con_graph.setName(graph.getName());
       // con_graph.setInputImageInfo(graph.getInputImageInfo());
        con_graph.setId(graph.getId());
        
        
        for (Node curr : graph.getNodes()){
            if (!curr.getChildren().isEmpty()){
                ArrayList<Kernel> children = new ArrayList<>();
                for (Node child: curr.getChildren())
                    children.add(child.getKernel());
                con_graph.getConnections().add(new Connection(curr.getKernel(), children));
            }
        }
     
        //Check
        graph.getNodes().forEach((t) -> {
             System.out.println(t.getKernel().getName() + " - " + t.getKernel().getNumber() + " - " + t.getLevel() + "\n");
        });
        
        
        con_graph.setGraphInfo(graph.getGraphInfo());
        
        return con_graph;
    }

    private static void ComputingLevel(Node root){
        int h = height(root); 
        System.out.println(h);
        for (int level= 1 ;level < h + 1; level++)
            cumputeGivenLevel(root,level,1);
            
    }
    
    private static int height(Node root)
    {
        if (root == null)
           return 0;
        if (root.getChildren().isEmpty())
            return 1;
        
        else
        {
            /* compute  height of each subtree */
            ArrayList<Integer> heights = new ArrayList<>();
            
            for (Node node: root.getChildren())
                heights.add(height(node));
             
            /* use the larger one */
            return Collections.max(heights) + 1; 
        }
    }
 
    private static void cumputeGivenLevel(Node root,int level,int i){
        //if root is a leaf 
        if (root.getChildren().isEmpty()){
             root.setLevel(++i);
             return;
         }
        //if we arrived to the desired level
         if (level == 1)
            root.setLevel(i);
        else 
            if (level > 1)
            {
                i++;
                for (Node node: root.getChildren())
                    cumputeGivenLevel(node, level-1,i);
            }
    }
}
