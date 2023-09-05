/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package vista;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxmlapplication.JavaFXMLApplication;


/**
 * FXML Controller class
 *
 * @author mique
 */
public class MainMenuController implements Initializable {

    @FXML
    private Button newGameButton;
    @FXML
    private Button exitButton;
    @FXML
    private ImageView backgroundImage;
    @FXML
    private Pane backgroundPane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        backgroundPane.widthProperty().addListener((o,oldVal,newVal) ->
                backgroundImage.setFitWidth(backgroundPane.getWidth())
        );
        backgroundPane.heightProperty().addListener((o,oldVal,newVal) ->
                backgroundImage.setFitHeight(backgroundPane.getHeight())
        );
    }    


    @FXML
    private void exitAction(ActionEvent event) throws IOException{
        playClickButtonSound();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/ExitConfirmation.fxml"));
        
        Parent root = loader.load(); //Conseguir root
        ExitConfirmationController exitConfir = loader.getController(); //Objeto del controlador para tener su referencia (metodos y eso)
        Scene scene = new Scene(root,375,230);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("TETRIS");
        stage.initModality(Modality.APPLICATION_MODAL); 
        stage.setResizable(false);
        stage.showAndWait(); // espera a que se cierre la segunda ventana.
        
        if(exitConfir.getExitPress()){
            exitButton.getScene().getWindow().hide();
        }
    }

    @FXML
    private void newGameAction(ActionEvent event) throws IOException{
        playClickButtonSound();
        JavaFXMLApplication.setRoot("Game");
        JavaFXMLApplication.setFullScreen(true);
        JavaFXMLApplication.setResizable(false);
        
        /*
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/Game.fxml"));
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/VistaPersona_1.fxml"));
        Parent root = loader.load(); //Conseguir root
        GameController exitConfir = loader.getController(); //Objeto del controlador para tener su referencia (metodos y eso)
        Scene scene = new Scene(root,1500,750);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("TETRIS");
        stage.initModality(Modality.APPLICATION_MODAL); 
        //la ventana se muestra modal
        stage.show(); // espera a que se cierre la segunda ventana.
        */
        //exitButton.getScene().getWindow().hide();
    }

    @FXML
    private void mouseEnteredAction(MouseEvent event) throws URISyntaxException {
        JavaFXMLApplication.playSound("buttonSelected");
    }
    
    private void playClickButtonSound(){
        JavaFXMLApplication.playSound("buttonClick2");
    }
}
