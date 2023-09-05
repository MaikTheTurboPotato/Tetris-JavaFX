/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxmlapplication;

import java.io.IOException;
import java.util.HashMap;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.AudioClip;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class JavaFXMLApplication extends Application {
    
    private static Scene scene;
    private static Stage stage;
    private static HashMap<String,Parent> roots = new HashMap<>();
    private static HashMap<String,AudioClip> audios = new HashMap<>();
    private static double SOUNDVOLUMEN = 0.25;
    
    public static void setRoot(Parent root){
        scene.setRoot(root);
    }
    
    public static void setRoot(String clave){
        Parent root = roots.get(clave);
        if(root != null){setRoot(root);}
        else{System.err.printf("ROOT NOT FOUND!!");}
    }
    
    public static void setFullScreen(boolean f){
        stage.setMaximized(true);
        stage.setFullScreen(f);
    }
    public static void setResizable(boolean f){
        stage.setResizable(f);
    }
    public static double[] getScreenSize(){
        double[] res = new double[2];
        res[0] = scene.getHeight();
        res[1] = scene.getWidth();
        return res;
    }/*
    public static void reNewGameRoot() throws IOException{
        FXMLLoader loader = new  FXMLLoader(getClass().getResource("/vista/Game.fxml"));
        Parent root = loader.load();
        roots.put("Game",root);
    }*/
    
    public static void playSound(String clave){
        AudioClip audio = audios.get(clave);
        if(audio != null){
            audio.setVolume(SOUNDVOLUMEN);
            audio.play();
        }
        else{System.err.printf("ROOT NOT FOUND!!");}
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        //======================================================================
        // 1- creación del grafo de escena a partir del fichero FXML
        FXMLLoader loader;
        Parent root;
        
        loader = new  FXMLLoader(getClass().getResource("/vista/Game.fxml"));
        root = loader.load();
        roots.put("Game",root);
        //----------------------------------------------------------------------
        loader = new FXMLLoader(getClass().getResource("/vista/ExitConfirmation.fxml"));
        root = loader.load();
        roots.put("ExitConfirmation",root);
        //----------------------------------------------------------------------
        loader = new  FXMLLoader(getClass().getResource("/vista/MainMenu.fxml"));
        root = loader.load();
        roots.put("MainMenu",root);
        //======================================================================
        // 2- creación de la escena con el nodo raiz del grafo de escena
        scene = new Scene(root);
        //======================================================================
        // 3- asiganación de la escena al Stage que recibe el metodo 
        //     - configuracion del stage
        //     - se muestra el stage de manera no modal mediante el metodo show()
        this.stage = stage;
        this.stage.setScene(scene);
        this.stage.setTitle("TETRIS Main Menu");
        this.stage.show();
        
        //======================================================================
        // 4- load soudns
        AudioClip audio;
        audio = new AudioClip("file:///C:/Users/mique/Desktop/IPC_Practicas/Cosis/TETRIS/src/resources/Sounds/button_click_1.mp3");
        audios.put("buttonClick1", audio);
        audio = new AudioClip("file:///C:/Users/mique/Desktop/IPC_Practicas/Cosis/TETRIS/src/resources/Sounds/button_click_2.mp3");
        audios.put("buttonClick2", audio);
        audio = new AudioClip("file:///C:/Users/mique/Desktop/IPC_Practicas/Cosis/TETRIS/src/resources/Sounds/button_click_3.mp3");
        audios.put("buttonClick3", audio);
        audio = new AudioClip("file:///C:/Users/mique/Desktop/IPC_Practicas/Cosis/TETRIS/src/resources/Sounds/button_selected.mp3");
        audios.put("buttonSelected", audio);
        audio = new AudioClip("file:///C:/Users/mique/Desktop/IPC_Practicas/Cosis/TETRIS/src/resources/Sounds/block_hardDrop.mp3");
        audios.put("blockHardDrop", audio);
        audio = new AudioClip("file:///C:/Users/mique/Desktop/IPC_Practicas/Cosis/TETRIS/src/resources/Sounds/block_lineClear1.mp3");
        audios.put("blockLineClear1", audio);
        audio = new AudioClip("file:///C:/Users/mique/Desktop/IPC_Practicas/Cosis/TETRIS/src/resources/Sounds/block_lineClear2.mp3");
        audios.put("blockLineClear2", audio);
        audio = new AudioClip("file:///C:/Users/mique/Desktop/IPC_Practicas/Cosis/TETRIS/src/resources/Sounds/block_placed.mp3");
        audios.put("blockPlaced", audio);
        audio = new AudioClip("file:///C:/Users/mique/Desktop/IPC_Practicas/Cosis/TETRIS/src/resources/Sounds/block_rotate.mp3");
        audios.put("blockRotate", audio);
        audio = new AudioClip("file:///C:/Users/mique/Desktop/IPC_Practicas/Cosis/TETRIS/src/resources/Sounds/block_move.mp3");
        audios.put("blockMove", audio);
        audio = new AudioClip("file:///C:/Users/mique/Desktop/IPC_Practicas/Cosis/TETRIS/src/resources/Sounds/block_swap.mp3");
        audios.put("blockSwap", audio);
        audio = new AudioClip("file:///C:/Users/mique/Desktop/IPC_Practicas/Cosis/TETRIS/src/resources/Sounds/start_game.mp3");
        audios.put("startGame", audio);
        audio = new AudioClip("file:///C:/Users/mique/Desktop/IPC_Practicas/Cosis/TETRIS/src/resources/Sounds/start_tick.mp3");
        audios.put("startTick", audio);
        audio = new AudioClip("file:///C:/Users/mique/Desktop/IPC_Practicas/Cosis/TETRIS/src/resources/Sounds/game_over.mp3");
        audios.put("gameOver", audio);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
        
    }


    
}
