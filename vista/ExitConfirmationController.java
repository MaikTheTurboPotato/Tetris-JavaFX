/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package vista;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafxmlapplication.JavaFXMLApplication;

/**
 * FXML Controller class
 *
 * @author mique
 */
public class ExitConfirmationController implements Initializable {

    @FXML
    private Button cancelButton;
    @FXML
    private Button okButton;
    
    private boolean exitPress = false;
    @FXML
    private Pane backgroundPane;
    @FXML
    private ImageView backgroundImage;

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
    private void cancelAction(ActionEvent event) {
        JavaFXMLApplication.playSound("buttonClick2");
        cancelButton.getScene().getWindow().hide();
    }

    @FXML
    private void okActtion(ActionEvent event) {
        JavaFXMLApplication.playSound("buttonClick2");
        exitPress = true;
        okButton.getScene().getWindow().hide();
    }
    public boolean getExitPress(){
        return this.exitPress;
    }

    @FXML
    private void mouseEnteredAction(MouseEvent event) {
        JavaFXMLApplication.playSound("buttonSelected");
    }
    
}
