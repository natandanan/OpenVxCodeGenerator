/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author natan
 */
public class Main extends Application {
    
    @Override
    public void start(Stage stage) {
        try{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("GUI/Views/MainScreen.fxml"));
        Scene scene = new Scene(root,1024,768);  
        stage.setScene(scene);
        stage.show();
        }catch(Exception ex){
            System.out.println(ex.toString());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
