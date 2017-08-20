/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataProcessing;
import BE.Node;
import BE.NodeGraph;
import GUI.Model.Cell;
import GUI.Model.Graph;
import java.util.ArrayList;


/**
 *
 * @author natan
 */
public class CellToNodeConvertor {
    public static NodeGraph convert(){
        ArrayList<Cell> cells = (ArrayList<Cell>) Graph.getInstance().getGraphModel().getAllCells();
        NodeGraph nodeGraph = new NodeGraph();
        
        //convert all cells to nodes - mapping each cell to the same palce in nodes list
        for (Cell cell: cells)
            //if (cell.getCellId() != 0)/*TODO delete the root from cell*/
                nodeGraph.getNodes().add(new Node(cell.getKernel()));
        
        
        
        for (Cell cell: cells){
            //adding perants cell to parens node
                for(Cell parent: cell.getCellParents())
                    if (cell.getCellId() != 0) /*TODO delete the root from cell*/
                        nodeGraph.getNodes().get(cells.indexOf(cell))./*Current node*/
                                getParents().add(nodeGraph.getNodes().get(cells.indexOf(parent)));
            //adding children cell to children node
                for(Cell child: cell.getCellChildren())
                    nodeGraph.getNodes().get(cells.indexOf(cell))./*Current node*/
                            getChildren().add(nodeGraph.getNodes().get(cells.indexOf(child)));
        }
        
        //Adding graph info
        nodeGraph.setId(0);
        nodeGraph.setGraphInfo(Graph.getInstance().getInfo());
        
        return nodeGraph;
    }

}