/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package vista;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxmlapplication.JavaFXMLApplication;

/**
 * FXML Controller class
 *
 * @author mique
 */
public class GameOverController implements Initializable {

    @FXML
    private Button newGameButton;
    @FXML
    private Button exitButton;
    
    private boolean exitPress = false;
    
    private boolean newGame = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void newGameAction(ActionEvent event) throws IOException {
        playClickButtonSound();
        newGame = true;
        exitButton.getScene().getWindow().hide();
    }

    @FXML
    private void exitAction(ActionEvent event) throws IOException {
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
            exitPress = true;
            exitButton.getScene().getWindow().hide();
        }
    }

    @FXML
    private void mouseEnteredAction(MouseEvent event) {
        JavaFXMLApplication.playSound("buttonSelected");
    }
    private void playClickButtonSound(){
        JavaFXMLApplication.playSound("buttonClick2");
    }
    
    public boolean getExitPress(){
        return this.exitPress;
    }
    public boolean getNewGame(){
        return this.newGame;
    }
    
}
