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
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxmlapplication.JavaFXMLApplication;
import javafxmlapplication.Play;
import modelo.Piece;

/**
 * FXML Controller class
 *
 * @author mique
 */
public class GameController implements Initializable{

    @FXML
    private Pane backgroundPane;
    @FXML
    private ImageView backgroundImage;
    @FXML
    private GridPane gameGridPane;
    @FXML
    private GridPane queueGridPane;
    @FXML
    private ImageView queueImage1;
    @FXML
    private ImageView queueImage2;
    @FXML
    private ImageView queueImage4;
    @FXML
    private ImageView queueImage3;
    @FXML
    private ImageView queueImage5;
    @FXML
    private ImageView swapImage;
    @FXML
    private Button startButton;
    @FXML
    private VBox gameVBox;
    @FXML
    private Text scoreText;
    @FXML
    private Text linesClearedText;
    @FXML
    private Text levelText;
    @FXML
    private Text fpsText;
    @FXML
    private Text goText;
    @FXML
    private Button gameOverButton;
    //==========================================================================
    private Play game; //Game thread
   
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
        game = new Play(queueImage1,queueImage2,queueImage3,queueImage4,queueImage5,
                swapImage,gameGridPane, scoreText,fpsText,goText,gameOverButton,startButton,
                linesClearedText,levelText);
    }    

    @FXML
    private void mouseEnteredAction(MouseEvent event) {
        JavaFXMLApplication.playSound("buttonSelected");
    }

    @FXML
    private void startAction(ActionEvent event) {
        JavaFXMLApplication.playSound("buttonClick2");
        startButton.setDisable(true);
        startButton.setOpacity(0);
        gameGridPane.getChildren().remove(startButton);
        gameGridPane.getChildren().remove(gameOverButton);
        game.start();
    }
    @FXML
    private void keyPressendAction(KeyEvent event) {
        if(event.getCode() == KeyCode.W){
            game.swapPiece();
        }
        if(event.getCode() == KeyCode.D){
            game.moveRightPiece();
        }
        if(event.getCode() == KeyCode.A){
            game.moveLeftPiece();
        }
        if(event.getCode() == KeyCode.S){
            game.softDropPiece();
        }
        if(event.getCode() == KeyCode.SPACE){
            game.hardDropPiece();
        }
        if(event.getCode() == KeyCode.RIGHT){
            game.roteteRightPiece();
        }
        if(event.getCode() == KeyCode.LEFT){
            game.roteteLeftPiece();
        }
        if(event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN){
            game.rotete180Piece();
        }   
    }
    @FXML
    private void keyReleasedAction(KeyEvent event) {
        if(event.getCode() == KeyCode.SPACE){
            game.releaseHardDropPiece();
        }
        if(event.getCode() == KeyCode.RIGHT || event.getCode() == KeyCode.LEFT 
           || event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN){
            game.releaseRotatePiece();
        }
    }
    @FXML
    private void gameOvertAction(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vista/GameOver.fxml"));
        
        Parent root = loader.load(); //Conseguir root
        GameOverController exitConfir = loader.getController(); //Objeto del controlador para tener su referencia (metodos y eso)
        Scene scene = new Scene(root,500,200);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("TETRIS");
        stage.initModality(Modality.APPLICATION_MODAL); 
        stage.setResizable(false);
        stage.showAndWait(); // espera a que se cierre la segunda ventana.
        
        if(exitConfir.getExitPress()){
            gameGridPane.getScene().getWindow().hide();
        }
        if(exitConfir.getNewGame()){
            game.resetGame();
            game = new Play(queueImage1,queueImage2,queueImage3,queueImage4,queueImage5,
                swapImage,gameGridPane, scoreText,fpsText,goText,gameOverButton,startButton,
                    linesClearedText,levelText);
        }
    }

    
   
}
