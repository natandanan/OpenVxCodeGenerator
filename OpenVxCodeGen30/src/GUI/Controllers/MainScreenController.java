/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Controllers;


import BE.CodeParameter;
import BE.Connection;
import BE.ConnectionGraph;
import BE.DB_Kernels;
import BE.E_IO;
import BE.E_Kernels_Name;
import BE.E_Type;
import BE.ImageInfo;
import BE.ParametersMap;
import BE.father_Info;
import CodeGeneration.XML2code;
import XMLHandling.XMLfunctions;
import GUI.Main;
import GUI.Model.Graph;
import DataProcessing.CellToNodeConvertor;
import BE.NodeGraph;
import DataProcessing.NodesToConnectionsConvertor;
import java.awt.Desktop;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import static javafx.application.Platform.exit;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;

/**
 *
 * @author natan
 */
public class MainScreenController implements Initializable {
    @FXML
    private BorderPane mainScreen;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       //Create the base graph object
        Graph graph = Graph.getInstance();
        mainScreen.setCenter(graph.getScrollPane());
    }

    public void GenButtonClick(ActionEvent event) {
        XML2code xml2code = XML2code.getInstance();
    
        try {
                
                NodeGraph nGraph = CellToNodeConvertor.convert();
                ConnectionGraph con_graph = NodesToConnectionsConvertor.fromNodesToConnections(nGraph);
                
                
                //Adding input image as paramater for first kernel
                ArrayList<Connection> conList = new ArrayList<>();
                conList = con_graph.getConnections();
                
                ArrayList<E_Type> ImageInputTypes = new ArrayList<>();
                CodeParameter Cp = new CodeParameter(1, ImageInputTypes);
                ArrayList<ParametersMap> ipar= new ArrayList<>();
                father_Info FI= new father_Info(0, 0);
                ipar.add(new ParametersMap(FI, Cp));
                conList.get(0).father.addInputParameters(ipar);

                
                updateOutputImageType(conList);
                
               
                XMLfunctions.writeXml(con_graph);
                con_graph = XMLfunctions.readFromXml();
                xml2code.codeGenerator(con_graph);
               
                //String[] args = new String[] {"/bin/bash", "-c", "make"}; //"with", "args"};
                //Process proc = new ProcessBuilder(args).start();
                
                //args = new String[] {"/bin/bash", "-c", "./openVxCode"}; //"with", "args"};
                //proc = new ProcessBuilder(args).start();
                
                ///String[] args = new String[] {"/bin/bash", "-c", "gedit openVxCode.cpp"}; //"with", "args"};
                //Process proc = new ProcessBuilder(args).start();
                                
                Desktop.getDesktop().open(new File("openVxCode.cpp"));
                exit();
                
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
     }

     private void updateOutputImageType(ArrayList<Connection> conList){
        javafx.scene.control.Dialog<Boolean> dialog = new javafx.scene.control.Dialog<>();
        dialog.setHeaderText("Please choose output image type");
        dialog.initStyle(StageStyle.UTILITY);
        //Set the button types.
        ButtonType createButtonType = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);
        // Create the paramaters labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));
        
        DB_Kernels kernelsInfo = DB_Kernels.getInstance(); 
         E_Kernels_Name name = conList.get(conList.size()-1).children.get(0).getName();
        ComboBox outputTypes = new ComboBox(
                                   FXCollections.observableArrayList(
                                        kernelsInfo.getOptionalTypesByKernelName( 
                                        name,
                                        kernelsInfo.getParamLocationByParamName(
                                            name,
                                            kernelsInfo.getVarsNameListByKernelName(name,E_IO.output).get(0))
                                        )
                                   )
                                );
        outputTypes.setPromptText("Output optional types");
        grid.add(new Label("Output Type"), 0, 0);
        grid.add(outputTypes, 1, 0);
        
        dialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> outputTypes.requestFocus());
        dialog.setResultConverter((ButtonType dialogButton) -> {
                                    if (dialogButton == createButtonType)                                 
                                        return true;
                                    else // if CANCEL option have been chosen 
                                        return false;
                                });
        Optional<Boolean> res = dialog.showAndWait();
        res.ifPresent(stat -> {
                                    if (stat){
                                      //Updating Graph info
                                      Graph.getInstance().getInfo().setOutputImageInfo(
                                              new ImageInfo((E_Type) outputTypes.getValue())
                                      );
                                      Graph.getInstance().getInfo().setName("Example");
                                    }
                                });

     }
     
}