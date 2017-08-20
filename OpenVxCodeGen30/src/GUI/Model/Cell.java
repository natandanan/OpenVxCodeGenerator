
package GUI.Model;

import BE.CodeParameter;
import BE.DB_Kernels;
import BE.E_IO;
import BE.E_Image_Type;
import BE.E_Kernels_Name;
import BE.E_Type;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import BE.Kernel;
import BE.ParametersMap;
import BE.father_Info;
import com.sun.javafx.collections.ObservableListWrapper;
import java.util.ArrayList;
import static java.util.Collections.list;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;


/**
 * FXML Controller class
 *
 * @author natan
 */
public class Cell extends Pane{


    //BI
    Integer cellId;
    private Kernel kernel; 
    private List<Cell> children = new ArrayList<>();
    private List<Cell> parents = new ArrayList<>();
    private DragContext dragContext = new DragContext();    
    private Graph graph = Graph.getInstance();
    DB_Kernels kernelsInfo = DB_Kernels.getInstance();  
 
    
     public Cell() {
        
    }
    
    public Cell(Integer cellId,Kernel cellKernel) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("GUI/Views/Cell.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.cellId = cellId;
        this.kernel = cellKernel;
        label.setText(toString());
        toolTip.setText(toString());
    }
    
    public void addCellChild(Cell cell) {
        children.add(cell);
    }

    public List<Cell> getCellChildren() {
        return children;
    }

    public void addCellParent(Cell cell) {
        parents.add(cell);
    }

    public List<Cell> getCellParents() {
        return parents;
    }

    public void removeCellChild(Cell cell) {
        children.remove(cell);
    }


    public int getCellId() {
        return cellId;
    }

    public Button getButton(){
//     return button;
return null;
    }

    /**
     * @return the kernel
     */
    public Kernel getKernel() {
        return kernel;
    }

    /**
     * @param kernel the kernel to set
     */
    public void setKernel(Kernel kernel) {
        this.kernel = kernel;
    }

    
    public void removeAllParamMap(Cell source){
        
        for (Iterator<ParametersMap> iterator = this.kernel.getInputParameters().iterator(); iterator.hasNext(); ) {
            ParametersMap value = iterator.next();
             if (value.getFatherInfo().getFatherId().equals(source.kernel.getNumber()))
                   iterator.remove();
        }
    }    
    
    @Override
    public String toString() {
        return this.kernel.getName().toString();
    }
    
   
    
    
   
    

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
// Controller Functions
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    @FXML
    private Label label; 
    @FXML
    private Tooltip toolTip; 
    @FXML
    private MenuItem deleteMI;
    
    
    
    
    public void OnDeleteAction(ActionEvent event){
        graph.beginUpdate();
        //Remove parameter maps
        for (Cell child : this.children)
            for(ParametersMap paramMap : child.kernel.getInputParameters())
                if (paramMap.getFatherInfo().getFatherId().equals(this.kernel.getNumber())){
                        child.kernel.getInputParameters().remove(paramMap);
                        break;
                }
        graph.getGraphModel().removeCell(this);
        graph.endUpdate();
    }
    
    
    public void OnMouseClicked(MouseEvent event){
        if (event.getClickCount() == 2 && !event.isSecondaryButtonDown()){
            Cell targetCell = ((Cell)event.getSource());
            Cell sourceCell = (Cell)graph.getGraphModel().getSource();
                if (graph.getGraphModel().isSourceSelected()){ //if same kernel selcted - ignore
                    if (sourceCell.equals(targetCell)) 
                        graph.getGraphModel().setIsSourceSelected(false);
                    else // else - create edge
                        if (true)//!edgeAlreadyExist(sourceCell,targetCell))
                        {
                                // Create the custom dialog.
                                javafx.scene.control.Dialog<Boolean> dialog = new javafx.scene.control.Dialog<>();
                                dialog.setTitle("Paramters Mapping Dialog");
                                dialog.setHeaderText("Please map between i/o parameters");
                                dialog.initStyle(StageStyle.UTILITY);

                                // Set the button types.
                                ButtonType createButtonType = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
                                dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);

                                // Create the paramaters labels and fields.
                                GridPane grid = new GridPane();
                                grid.setHgap(10);
                                grid.setVgap(10);
                                grid.setPadding(new Insets(20, 150, 10, 10));

                                ComboBox sourceParam = new ComboBox(
                                        FXCollections.observableArrayList(
                                                kernelsInfo.getVarsNameListByKernelName(sourceCell.getKernel()
                                                        .getName(),E_IO.output)));
                                sourceParam.getSelectionModel().selectFirst();
                                sourceParam.setPromptText("Source Parameter");
                                
                                ComboBox targetParam = new ComboBox(
                                        FXCollections.observableArrayList(
                                                kernelsInfo.getVarsNameListByKernelName(targetCell.getKernel().getName(),E_IO.input)));
                                targetParam.getSelectionModel().selectFirst();
                                targetParam.setPromptText("Target Parameter");

                                grid.add(new Label("Source Parameter:"), 0, 0);
                                grid.add(sourceParam, 1, 0);
                                grid.add(new Label("Target Parameter:"), 0, 1);
                                grid.add(targetParam, 1, 1);


                                ComboBox paramTypeComBox = new ComboBox();
                                paramTypeComBox.getItems().addAll(intersection(FXCollections.observableArrayList(
                                                                        kernelsInfo.getOptionalTypesByKernelName
                                                                                   (targetCell.getKernel().getName(), 
                                                                                    kernelsInfo.getParamLocationByParamName(targetCell.getKernel().getName()
                                                                                    ,(String)targetParam.getValue()))
                                                                               ),
                                                                               (FXCollections.observableArrayList(
                                                                                kernelsInfo.getOptionalTypesByKernelName
                                                                                   (sourceCell.getKernel().getName(), 
                                                                                    kernelsInfo.getParamLocationByParamName(sourceCell.getKernel().getName()
                                                                                    , (String)sourceParam.getValue()))
                                                                               ))
                                                                                
                                                                   ));
                                paramTypeComBox.setPromptText("Parameter Type");


                                targetParam.valueProperty().addListener((observable, oldValue, newValue) -> {
                                paramTypeComBox.getItems().clear();
                                paramTypeComBox.getItems().addAll(intersection(FXCollections.observableArrayList(
                                                                        kernelsInfo.getOptionalTypesByKernelName
                                                                                   (targetCell.getKernel().getName(), 
                                                                                    kernelsInfo.getParamLocationByParamName(targetCell.getKernel().getName()
                                                                                            ,(String)newValue))
                                                                               ),
                                                                               (FXCollections.observableArrayList(
                                                                                kernelsInfo.getOptionalTypesByKernelName
                                                                                   (sourceCell.getKernel().getName(), 
                                                                                    kernelsInfo.getParamLocationByParamName(sourceCell.getKernel().getName()
                                                                                    , (String)sourceParam.getValue()))
                                                                               ))
                                                                                
                                                                   ));
                                });

                                sourceParam.valueProperty().addListener((observable, oldValue, newValue) -> {
                                paramTypeComBox.getItems().clear();
                                paramTypeComBox.getItems().addAll(intersection(FXCollections.observableArrayList(
                                                                        kernelsInfo.getOptionalTypesByKernelName
                                                                                   (targetCell.getKernel().getName(), 
                                                                                    kernelsInfo.getParamLocationByParamName(targetCell.getKernel().getName()
                                                                                            ,(String)targetParam.getValue()))
                                                                               ),
                                                                               (FXCollections.observableArrayList(
                                                                                kernelsInfo.getOptionalTypesByKernelName
                                                                                   (sourceCell.getKernel().getName(), 
                                                                                    kernelsInfo.getParamLocationByParamName(sourceCell.getKernel().getName()
                                                                                    , (String)newValue))
                                                                               ))
                                                                                
                                                                   ));
                                });
                                grid.add(new Label("Prameter Type:"), 0, 2);
                                grid.add(paramTypeComBox, 1, 2);                            

                                dialog.getDialogPane().setContent(grid);

                                Platform.runLater(() -> paramTypeComBox.requestFocus());

                                
                               dialog.setResultConverter((ButtonType dialogButton) -> {
                                    if (dialogButton == createButtonType) {
                                        Kernel src = graph.getGraphModel().getSource().getKernel();
                                        CodeParameter pOut = new CodeParameter(
                                                kernelsInfo.getParamLocationByParamName(src.getName(),(String)sourceParam.getValue()));
                                        pOut.addToSelectedType((E_Image_Type)paramTypeComBox.getValue());
                                        ArrayList<CodeParameter> lOut = new ArrayList<>();
                                        lOut.add(pOut);
                                        src.addOutputParameters(lOut);


                                        Kernel trg = targetCell.getKernel();
                                        CodeParameter pIn = new CodeParameter(
                                                kernelsInfo.getParamLocationByParamName(trg.getName(),(String)targetParam.getValue()));
                                        pIn.addToSelectedType((E_Image_Type)paramTypeComBox.getValue());
                                        ArrayList<ParametersMap> lIn = new ArrayList<>();
                                        lIn.add(new ParametersMap(new father_Info(src.getNumber(), 
                                                                                  kernelsInfo.getParamLocationByParamName(src.getName(),(String)sourceParam.getValue())),
                                                                  pIn)); 
                                        trg.addInputParameters(lIn);                               
                                        return true;
                                    }
                                    // if CANCEL option have been chosen 
                                    graph.getGraphModel().setIsSourceSelected(false);
                                    return false;
                                });

                                Optional<Boolean> res = dialog.showAndWait();

                                res.ifPresent(stat -> {
                                    if (stat){
                                        graph.beginUpdate();
                                        graph.getGraphModel().addEdge(graph.getGraphModel().getSource().getCellId(),
                                                                 targetCell.getCellId(),
                                                                 "(" +(String) sourceParam.getValue()+
                                                                 "," +(String) targetParam.getValue()
                                                                +")");
                                        graph.endUpdate();                            
                                        graph.getGraphModel().setIsSourceSelected(false);
                                        System.out.println("Done");
                                            System.out.println(targetCell.getKernel().toString());
                                    }
                                });
                        }
                        else
                             graph.getGraphModel().setIsSourceSelected(false);
                }
                else {
                    graph.getGraphModel().setSource(targetCell);
                    graph.getGraphModel().setIsSourceSelected(true);
                }
        }
    }
        
    
//    private boolean edgeAlreadyExist(Cell source,Cell target){
//        
//        for (Edge edge : graph.getGraphModel().getAllEdges()){
//            if (edge.source == source && edge.target == target)
//               return true; 
//        } 
//
//        return false;
//    }
    
    
    public void onMousePressedEventHandler(MouseEvent event) {
        Pane cell = (Pane) event.getSource();
        double scale = graph.getScale();
        dragContext.x = cell.getBoundsInParent().getMinX() * scale - event.getScreenX();
        dragContext.y = cell.getBoundsInParent().getMinY()  * scale - event.getScreenY();
    }



    public void onMouseDraggedEventHandler(MouseEvent event) {
            double offsetX = event.getScreenX() + dragContext.x;
            double offsetY = event.getScreenY() + dragContext.y;
            // adjust the offset in case we are zoomed
            double scale = graph.getScale();
            offsetX /= scale;
            offsetY /= scale;
            ((Pane)event.getSource()).relocate(offsetX,offsetY);
    }



    public void onMouseReleasedEventHandler (MouseEvent event) {

    }


     private <T> List<T> intersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();

        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }

}

    
   
    class DragContext {

        double x;
        double y;

    }
    


