package GUI.Model;


import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;


public class Graph {

    private static Graph instance = null;
    
    private GraphModel graphModel;

   // private Group canvas;

    private ZoomableScrollPane scrollPane;
    
    Pane cellLayer;

    private GraphInfo info;
    
    
    protected Graph() {
        this.graphModel = new GraphModel();
        cellLayer = new Pane();
        scrollPane = new ZoomableScrollPane(cellLayer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        info = new GraphInfo();
    }
    
    //Singelston method
    public static Graph getInstance(){
        if (instance == null)
            instance = new Graph();
        return instance;
    }

    
    public ScrollPane getScrollPane() {
        return this.scrollPane;
    }

    public Pane getCellLayer() {
        return this.cellLayer;
    }

    public GraphModel getGraphModel() {
        return graphModel;
    }

    public void beginUpdate() {
    }

    public void endUpdate() {

        // add components to graph pane
        getCellLayer().getChildren().addAll(graphModel.getAddedEdges());
        getCellLayer().getChildren().addAll(graphModel.getAddedCells());

        // remove components from graph pane
        getCellLayer().getChildren().removeAll(graphModel.getRemovedCells());
        getCellLayer().getChildren().removeAll(graphModel.getRemovedEdges());

        getGraphModel().merge();
    }

    public double getScale() {
        return this.scrollPane.getScaleValue();
    }

    /**
     * @return the info
     */
    public GraphInfo getInfo() {
        return info;
    }

    /**
     * @param info the info to set
     */
    public void setInfo(GraphInfo info) {
        this.info = info;
    }
}