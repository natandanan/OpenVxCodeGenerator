package GUI.Model;

//import java.awt.event.MouseEvent;
import java.io.IOException;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import static java.lang.Math.sqrt;



    public class Edge extends Group {

    protected Cell source;
    protected Cell target;

    @FXML
    Line line;
    @FXML
    Label label;
    
    Polygon polygon;
   

    public Edge(Cell source, Cell target,String labelContent){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("GUI/Views/Edge.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.source = source;
        this.target = target;
        label.setText(labelContent);
        
        source.addCellChild(target);
        target.addCellParent(source);

        
        line.setStrokeWidth(2);
        
        //For accessing this edge inside the event handler
      //  Edge thisEdge = this;
        
        line.addEventFilter(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
            Graph graph = Graph.getInstance();
            graph.beginUpdate();
            graph.getGraphModel().removeEdge(this);
            graph.endUpdate();
            this.target.removeAllParamMap(this.source);
            getChildren().remove(line);
        });
      
        
       polygon = new Polygon();
       polygon.getPoints().addAll(new Double[]{0.0,0.0,-10.0,-10.0,10.0,-10.0 });
       polygon.translateXProperty().bind(target.layoutXProperty().
               add(target.getChildren().get(1).getBoundsInParent().getWidth() / 4.0));
       polygon.translateYProperty().bind(target.layoutYProperty());
       polygon.setStyle("-fx-fill: #e17435;");
       this.getChildren().add(polygon);
        
        
       line.startXProperty().bind( source.layoutXProperty().
               add(source.getBoundsInParent().getWidth() / 4.0));
       line.startYProperty().bind( source.layoutYProperty().
               add(source.getBoundsInParent().getHeight()));
     
       line.endXProperty().bind( polygon.translateXProperty());
       line.endYProperty().bind( polygon.translateYProperty().add(-sqrt(75)));
      
       
       label.relocate(line.getStartX() + (line.getEndX()-line.getStartX())/2, 
                   line.getStartY() + (line.getEndY()-line.getStartY())/2);
       
       
       source.addEventFilter(MouseEvent.MOUSE_DRAGGED, (MouseEvent mouseEvent) -> {
           label.relocate(line.getStartX() + (line.getEndX()-line.getStartX())/2 + 10, 
                   line.getStartY() + (line.getEndY()-line.getStartY())/2);
       });
       
       target.addEventFilter(MouseEvent.MOUSE_DRAGGED, (MouseEvent mouseEvent) -> {
           label.relocate(line.getStartX() + (line.getEndX()-line.getStartX())/2 + 10, 
                   line.getStartY() + (line.getEndY()-line.getStartY())/2);
       });
       
       
    }

    public Cell getSource() {
        return source;
    }

    public Cell getTarget() {
        return target;
    }
    
    @Override
    public String toString(){
        return source.cellId + "--" +target.cellId;
    }

}


