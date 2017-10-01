
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI.Controllers;

import BE.ChannelExtractKernel;
import BE.CodeParameter;
import BE.ConvertDepthKernel;
import BE.DB_Kernels;
import BE.E_Channel_Type;
import BE.E_Convert_Policy;
import BE.E_Data_Type;
import BE.E_IO;
import BE.E_Image_Type;
import BE.E_Kernels_Name;
import static BE.E_Kernels_Name.UserKernel;
import BE.E_Threshold_Type;
import BE.Kernel;
import BE.ParametersMap;
import BE.ThresholdKernel;
import BE.UserKernel;
import BE.father_Info;
import GUI.Model.Graph;
import GUI.Model.Model;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;
import javafx.util.Pair;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.xml.transform.Result;
/**
 * FXML Controller class
 *
 * @author natan
 */
public class KernelsSideToolBarController implements Initializable {

@FXML
private FlowPane kernelsBox;
int kernelId = 0;   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        LoadKernels();
    }    
    
    private void LoadKernels(){
        
        Graph graph = Graph.getInstance();
        Model model = graph.getModel();
        DB_Kernels kernelsInfo = DB_Kernels.getInstance();
                
                
        Button colorConRGB2IYUVButt = new Button("Color Convert");
        Button chanExtButt = new Button("Channel Extract");
        Button equalizeHistButt = new Button("Equalize Histogram");
        Button chanCombButt = new Button("Channel Combine");
        Button sobelButt = new Button("Sobel 3x3");
        Button magnButt = new Button("Magnitude");
        Button conDepButt = new Button("Convert Depth");
        Button gaussButt = new Button("Gaussian 3x3");
        Button phaseButt = new Button("Phase");
        Button thresholdButt = new Button("Threshold");
        Button userKernelButt = new Button("User Kernel");
        
        
        
        colorConRGB2IYUVButt.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {

                 graph.beginUpdate();
                 model.addCell( kernelId++,new Kernel(E_Kernels_Name.colorConvert, kernelId));
                 graph.endUpdate();
           }
       });
        chanExtButt.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
               E_Channel_Type channel = getChannelFromUser();
               graph.beginUpdate();
               model.addCell( kernelId++,new ChannelExtractKernel(E_Kernels_Name.channelExtract, kernelId, channel));
               graph.endUpdate();

           }
       });
        equalizeHistButt.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
            graph.beginUpdate();
            model.addCell( kernelId++,new Kernel(E_Kernels_Name.EqualizeHist, kernelId));
            graph.endUpdate();
          }
       });
        chanCombButt.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
            graph.beginUpdate();
            model.addCell( kernelId++,new Kernel(E_Kernels_Name.ChannelCombine,kernelId));
            graph.endUpdate();
           }
        });
        sobelButt.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
            graph.beginUpdate();
            model.addCell( kernelId++,new Kernel(E_Kernels_Name.Sobel3x3, kernelId));
            graph.endUpdate();
          }
       });
        magnButt.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
            graph.beginUpdate();
            model.addCell( kernelId++,new Kernel(E_Kernels_Name.Magnitude, kernelId));
            graph.endUpdate();
          }
       });
        conDepButt.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
            ConPolDialogResult res = getConvertDepthUserInput();
            if (res != null){
                graph.beginUpdate();
                model.addCell( kernelId++,new ConvertDepthKernel(E_Kernels_Name.ConvertDepth
                                          ,kernelId,res.conPol,res.shift));
                graph.endUpdate();
            }
          }
       });
        gaussButt.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
            graph.beginUpdate();
            model.addCell( kernelId++,new Kernel(E_Kernels_Name.Gaussian3x3, kernelId));
            graph.endUpdate();
          }
       });
        phaseButt.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
            graph.beginUpdate();
            model.addCell( kernelId++,new Kernel(E_Kernels_Name.Phase, kernelId));
            graph.endUpdate();
          }
       }); 
        thresholdButt.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
            ThresholdDialogResult res = getThresholdUserInput();
            if (null != res){
                graph.beginUpdate();
                if (res.type.equals(E_Threshold_Type.VX_THRESHOLD_TYPE_BINARY))
                    model.addCell( kernelId++,new ThresholdKernel(E_Kernels_Name.Threshold
                                              ,kernelId,res.type,res.dataType,res.value));
                else if (res.type.equals(E_Threshold_Type.VX_THRESHOLD_TYPE_RANGE))
                    model.addCell( kernelId++,new ThresholdKernel(E_Kernels_Name.Threshold
                                              ,kernelId,res.type,res.dataType,res.upper,res.lower));
                graph.endUpdate();
            }
          }
       });
        userKernelButt.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
               try{
                 String userKernelName = getUserKernelCode(kernelId + 1);
                 //Check if one of user inputs is missing
                 if (null == userKernelName) return;
                 graph.beginUpdate();
                 model.addCell( kernelId++,new UserKernel(E_Kernels_Name.UserKernel, userKernelName ,kernelId));
                 graph.endUpdate();
               }catch(Exception ex){}
           }

            
       }); 
         
         
        Button buttons[] = {colorConRGB2IYUVButt,chanExtButt,equalizeHistButt,chanCombButt,sobelButt,
                            magnButt,conDepButt,gaussButt,phaseButt,thresholdButt, userKernelButt};
         for (Button butt : buttons){
             butt.setPrefSize(kernelsBox.getPrefWidth(), kernelsBox.getPrefHeight()/10);
             butt.getStyleClass().add("record-sales");
             kernelsBox.getChildren().add(butt);
         }
    }
    
    private String getUserKernelCode(int kernelId){

        
        TextInputDialog userKerneldialog = new TextInputDialog("Kernel name");
        userKerneldialog.setTitle("User Kernel Creation");
        userKerneldialog.setHeaderText("Please insert kernels' name - Follow CamelCase Conv.");
        Optional<String> result = userKerneldialog.showAndWait();
        String userKernelName = "none.";
        if (result.isPresent()) 
            userKernelName= result.get();
        else
            return null;
                
        ArrayList<String> funcNames = new ArrayList<>();
        funcNames.add("user" + userKernelName + "Node");
        funcNames.add(userKernelName+"InputValdiator");
        funcNames.add(userKernelName+"OutputValdiator");
        funcNames.add(userKernelName+"HostSideFunction");
        funcNames.add("registerUserKernel");
        
        for (String name : funcNames) {
            if (!writeFunctionFile(name, kernelId))
                return null;    
        }
        
        return userKernelName;
}
        
    private E_Channel_Type getChannelFromUser(){
         
        ChoiceDialog<E_Channel_Type> dialog =
                new ChoiceDialog<>(E_Channel_Type.VX_CHANNEL_0 ,E_Channel_Type.values());
        dialog.setHeaderText("Please choose channel type");
        dialog.setContentText("Choose your type:");

        // Traditional way to get the response value.
        Optional<E_Channel_Type> result = dialog.showAndWait();

         if (result.isPresent())
            return result.get();
        else
            return null;
    }
    private ConPolDialogResult getConvertDepthUserInput(){
        
        javafx.scene.control.Dialog<ConPolDialogResult> dialog = new javafx.scene.control.Dialog<>();
        dialog.setHeaderText("Please choose and insert values for parameters");
        dialog.initStyle(StageStyle.UTILITY);

        // Set the button types.
        ButtonType createButtonType = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);
        // Create the paramaters labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox conPolComBox = new ComboBox(
                FXCollections.observableArrayList(E_Convert_Policy.values()));
        NumberTextField shiftTextField = new NumberTextField();

        grid.add(new Label("Convert Policy:"), 0, 0);
        grid.add(conPolComBox, 1, 0);
        grid.add(new Label("Shift:"), 0, 1);
        grid.add(shiftTextField, 1, 1);
        
        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> conPolComBox.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
       dialog.setResultConverter((ButtonType dialogButton) -> {
            if (dialogButton == createButtonType) {
                return new ConPolDialogResult((E_Convert_Policy)conPolComBox.getValue()
                                               ,Integer.parseInt(shiftTextField.getText()));
            }
            return null;
        });

        Optional<ConPolDialogResult> res = dialog.showAndWait();
        if (res.isPresent())
            return res.get();
        else
            return null;
    }
    private ThresholdDialogResult getThresholdUserInput(){
        
        javafx.scene.control.Dialog<ThresholdDialogResult> dialog = new javafx.scene.control.Dialog<>();
        dialog.setHeaderText("Please choose and insert values for parameters");
        dialog.initStyle(StageStyle.UTILITY);

        // Set the button types.
        ButtonType createButtonType = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);
        // Create the paramaters labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox threTypeComBox = new ComboBox(
                FXCollections.observableArrayList(E_Threshold_Type.values()));
        ComboBox dataTypeComBox = new ComboBox(
                FXCollections.observableArrayList(E_Data_Type.values()));
        NumberTextField valueTextField = new NumberTextField();
        NumberTextField upperTextField = new NumberTextField();
        NumberTextField lowerTextField = new NumberTextField();
        

        grid.add(new Label("Threshold type:"), 0, 0);
        grid.add(threTypeComBox, 1, 0);
        grid.add(new Label("Threshold data type:"), 0, 1);
        grid.add(dataTypeComBox, 1, 1);
        Label valueLabel = new Label("Value:");
        grid.add(valueLabel, 0, 2);
        grid.add(valueTextField, 1, 2);
        Label upperLabel = new Label("Upper:");
        grid.add(upperLabel, 0, 2);
        grid.add(upperTextField, 1, 2);
        Label lowerLabel = new Label("Lower:");
        grid.add(lowerLabel, 0, 3);
        grid.add(lowerTextField, 1, 3);
        
        
        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> threTypeComBox.requestFocus());
        
        threTypeComBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(E_Threshold_Type.VX_THRESHOLD_TYPE_BINARY)){
                valueTextField.setVisible(true);
                valueLabel.setVisible(true);
                upperLabel.setVisible(false);
                upperTextField.setVisible(false);
                lowerLabel.setVisible(false);
                lowerTextField.setVisible(false);
            }
            else {
                valueTextField.setVisible(false);
                valueLabel.setVisible(false);
                upperLabel.setVisible(true);
                upperTextField.setVisible(true);
                lowerLabel.setVisible(true);
                lowerTextField.setVisible(true);
            }
        });
        
        threTypeComBox.setValue(E_Threshold_Type.VX_THRESHOLD_TYPE_BINARY);
        
        dialog.setResultConverter((ButtonType dialogButton) -> {
            if (dialogButton == createButtonType) {
                if (threTypeComBox.getValue().equals(E_Threshold_Type.VX_THRESHOLD_TYPE_BINARY))
                    return new ThresholdDialogResult(Integer.parseInt(valueTextField.getText()),
                                                    (E_Data_Type)dataTypeComBox.getValue(),
                                                    (E_Threshold_Type)threTypeComBox.getValue());
                if (threTypeComBox.getValue().equals(E_Threshold_Type.VX_THRESHOLD_TYPE_RANGE))
                    return new ThresholdDialogResult(Integer.parseInt(upperTextField.getText()),
                                                    Integer.parseInt(lowerTextField.getText()),
                                                    (E_Data_Type)dataTypeComBox.getValue(),
                                                    (E_Threshold_Type)threTypeComBox.getValue());
                    
            }
            return null;
        });

        Optional<ThresholdDialogResult> res = dialog.showAndWait();
        if (res.isPresent())
            return res.get();
        else
            return null;
    }

    private boolean writeFunctionFile(String funcName, int kernelId){
        try{
        String fileName = funcName + "_"+ kernelId;
        File f = new File(fileName);
        RandomAccessFile raf = new RandomAccessFile(f, "rw");
        raf.setLength(0);
        
            
        
        javafx.scene.control.Dialog<String> dialog = new javafx.scene.control.Dialog<>();
        dialog.setHeaderText("Please insert " + funcName + "Node code");
        dialog.initStyle(StageStyle.UTILITY);

        // Set the button types.
        ButtonType createButtonType = new ButtonType("Done", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(createButtonType, ButtonType.CANCEL);
        // Create the paramaters labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        //TextArea textArea = new TextArea(funcName + "{\n}");

        TextArea textArea = new TextArea();
        
        
        //userKernelNode
        if (funcName.endsWith("Node"))
                    textArea.setText("vx_node " + funcName + "(){\n}");
        //InputValdiator
        if (funcName.endsWith("InputValdiator"))
                    textArea.setText("vx_status VX_CALLBACK " + funcName + "(){\n}");
        //OutputValdiator
        if (funcName.endsWith("OutputValdiator"))
                    textArea.setText("vx_status VX_CALLBACK " + funcName + "(){\n}");
        //HostSideFunction
        if (funcName.endsWith("HostSideFunction"))
                    textArea.setText("vx_status VX_CALLBACK " + funcName + "(){\n}");
        //registerUserKernel
        if (funcName.endsWith("registerUserKernel"))
                    textArea.setText("vx_status " + funcName + "(vx_context context)"+ "{\n}");
        
        
        grid.add(textArea,0,0);
        
        
        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> textArea.requestFocus());
        
        
        dialog.setResultConverter((ButtonType dialogButton) -> {
            if (dialogButton == createButtonType) {
                return textArea.getText();
            }
            return null;
        });

        Optional<String> res = dialog.showAndWait();
        if (res.isPresent()){ 
            raf.writeBytes(res.get());
            return true;
        }
        
        
    
    }catch(Exception ex){}
     return false;
}
}

//Utility class
class ConPolDialogResult{
    E_Convert_Policy conPol;
    int shift;

    public ConPolDialogResult() {
    }
    public ConPolDialogResult(E_Convert_Policy conPol, int shift) {
        this.conPol = conPol;
        this.shift = shift;
    }
}

//Utility class
class ThresholdDialogResult{    
    int value; // for binary threshold 
    int upper; // for range threshold
    int lower;
    E_Data_Type dataType;
    E_Threshold_Type type;

    public ThresholdDialogResult() {
    }

    public ThresholdDialogResult(int value, E_Data_Type dataType, E_Threshold_Type type) {
        this.value = value;
        this.dataType = dataType;
        this.type = type;
    }
    
    public ThresholdDialogResult(int upper, int lower, E_Data_Type dataType, E_Threshold_Type type) {
        
        this.upper = upper;
        this.lower = lower;
        this.dataType = dataType;
        this.type = type;
    }
    
}


class NumberTextField extends TextField
{
    @Override
    public void replaceText(int start, int end, String text)
    {
        if (validate(text))
        {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text)
    {
        if (validate(text))
        {
            super.replaceSelection(text);
        }
    }

    private boolean validate(String text)
    {
        return text.matches("[0-9]*");
    }
}

