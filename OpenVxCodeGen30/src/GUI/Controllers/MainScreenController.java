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
import DataProcessing.NodeGraph2ConnGraph;
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
        
        //TODO only for prototype 
        //graph.setInfo(new GraphInfo("EqualizeHist", 150, 100));
        
        mainScreen.setCenter(graph.getScrollPane());
       //KernelBoxFlowPane kernelBox = new KernelBoxFlowPane();
        //mainScreen.setRight(kernelBox.create(graph));
        
    }

    public void GenButtonClick(ActionEvent event) {
        XML2code xml2code = XML2code.getInstance();
    
        try {
                
                NodeGraph nGraph = CellToNodeConvertor.convert();
                ConnectionGraph con_graph = NodeGraph2ConnGraph.fromNodesToConnections(nGraph);
                
                
                //Adding input image as paramater for first kernel
                ArrayList<Connection> conList = new ArrayList<>();
                conList = con_graph.getConnections();
                
                ArrayList<E_Type> ImageInputTypes = new ArrayList<>();
                CodeParameter Cp = new CodeParameter(1, ImageInputTypes);
                ArrayList<ParametersMap> ipar= new ArrayList<>();
                father_Info FI= new father_Info(0, 0);
                ipar.add(new ParametersMap(FI, Cp));
                conList.get(0).father.addInputParameters(ipar);

                
                updateOutputAndInputImagesType(conList);
                
               
                XMLfunctions.writeXml(con_graph);
                con_graph = XMLfunctions.readFromXml();
                XML2code.codeGenerator(con_graph);
               
                String[] args = new String[] {"/bin/bash", "-c", "make"}; //"with", "args"};
                Process proc = new ProcessBuilder(args).start();
                
                args = new String[] {"/bin/bash", "-c", "python3 goproStream2.py"}; //"with", "args"};
                proc = new ProcessBuilder(args).start();

                Thread.sleep(4000);
                
                args = new String[] {"/bin/bash", "-c", "./openVxCode"}; //"with", "args"};
                proc = new ProcessBuilder(args).start();

                
                //args = new String[] {"/bin/bash", "-c", "vlc goprofeed3.ts"}; //"with", "args"};
                //proc = new ProcessBuilder(args).start();
                                
                //Desktop.getDesktop().open(new File("openVxCode.cpp"));
               // exit();
                
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
     }

     private void updateOutputAndInputImagesType(ArrayList<Connection> conList){
        javafx.scene.control.Dialog<Boolean> dialog = new javafx.scene.control.Dialog<>();
        dialog.setHeaderText("Please choose output and input images type");
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
        E_Kernels_Name outputName = conList.get(conList.size()-1).children.get(0).getName();
        ComboBox outputTypes = new ComboBox(
                                   FXCollections.observableArrayList(
                                        kernelsInfo.getOptionalTypesByKernelName( 
                                        outputName,
                                        kernelsInfo.getParamLocationByParamName(
                                            outputName,
                                            kernelsInfo.getVarsNameListByKernelName(outputName,E_IO.output).get(0))
                                        )
                                   )
                                );
        outputTypes.setPromptText("Output optional types");
        grid.add(new Label("Output Type"), 0, 1);
        grid.add(outputTypes, 1, 1);
        
        
        E_Kernels_Name inputName = conList.get(0).children.get(0).getName();
        ComboBox inputTypes = new ComboBox(
                                   FXCollections.observableArrayList(
                                        kernelsInfo.getOptionalTypesByKernelName( 
                                        inputName,
                                        kernelsInfo.getParamLocationByParamName(
                                            inputName,
                                            kernelsInfo.getVarsNameListByKernelName(inputName,E_IO.input).get(0))
                                        )
                                   )
                                );
        inputTypes.setPromptText("Input optional types");
        grid.add(new Label("Input Type"), 0, 0);
        grid.add(inputTypes, 1, 0);
        
      
        dialog.getDialogPane().setContent(grid);
        Platform.runLater(() -> inputTypes.requestFocus());
        dialog.setResultConverter((ButtonType dialogButton) -> {
                            return dialogButton == createButtonType; // if CANCEL option have been chosen
                                });
        Optional<Boolean> res = dialog.showAndWait();
        res.ifPresent(stat -> {
                                    if (stat){
                                      //Updating Graph info
                                      Graph.getInstance().getInfo().setOutputImageInfo(
                                              new ImageInfo((E_Type) outputTypes.getValue())
                                      );
                                      Graph.getInstance().getInfo().setInputImageInfo(
                                              new ImageInfo((E_Type) inputTypes.getValue())
                                      );
                                      Graph.getInstance().getInfo().setName("Example");
                                    }
                                });

     }
     
}