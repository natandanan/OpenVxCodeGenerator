package GUI.Model;


import BE.GraphInfo;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

public class Graph {

    private static Graph instance = null;
    
    private Model model;

    private Group canvas;

    private ZoomableScrollPane scrollPane;
    
    Pane cellLayer;

    private GraphInfo info;
    
    
    protected Graph() {
        this.model = new Model();
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

    public Model getModel() {
        return model;
    }

    public void beginUpdate() {
    }

    public void endUpdate() {

        // add components to graph pane
        getCellLayer().getChildren().addAll(model.getAddedEdges());
        getCellLayer().getChildren().addAll(model.getAddedCells());

        // remove components from graph pane
        getCellLayer().getChildren().removeAll(model.getRemovedCells());
        getCellLayer().getChildren().removeAll(model.getRemovedEdges());

        getModel().merge();

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