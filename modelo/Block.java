/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

/**
 *
 * @author mique
 */
public class Block {
    private int x,y;
    private int relX, relY; //Relative positions in the piece
    private ImageView blockView;
    private Image block;
    private String color;
    private GridPane gameGridPane;
    
    public Block(int x, int y, String color, GridPane gridPane){
        this.x = x;
        this.y = y;
        switch(color){
            case "yellow": block = new Image(getClass().getResourceAsStream("/resources/Pieces_Blocs/yellow.png/")); color = "yellow"; break;
            case "red": block = new Image(getClass().getResourceAsStream("/resources/Pieces_Blocs/red.png/")); color = "red"; break;
            case "green": block = new Image(getClass().getResourceAsStream("/resources/Pieces_Blocs/green.png/")); color = "green"; break;
            case "cian": block = new Image(getClass().getResourceAsStream("/resources/Pieces_Blocs/cian.png/")); color = "cian"; break;
            case "purple": block = new Image(getClass().getResourceAsStream("/resources/Pieces_Blocs/purple.png/")); color = "purple"; break;
            case "orange": block = new Image(getClass().getResourceAsStream("/resources/Pieces_Blocs/orange.png/")); color = "orange"; break;
            case "blue": block = new Image(getClass().getResourceAsStream("/resources/Pieces_Blocs/blue.png/")); color = "blue"; break;
        }
        blockView = new ImageView(block);
        blockView.setFitHeight(25);
        blockView.setFitWidth(25);
        gameGridPane = gridPane;
    }
    //==========================================================================
    //Positions
    public void setPos(int x, int y){
        this.x = x;
        this.y = y;
    }
    public int[] getPos(){
        int[] res = new int[2];
        res[0] = x; res[1] = y;
        return res;
    }
    public void setRelPos(int centerX, int centerY){//The mid block position
        this.relX = this.x-centerX;
        this.relY = this.y-centerY;
    }
    public int[] getRelPos(){
        int[] res = new int[2];
        res[0] = relX; res[1] = relY;
        return res;
    }
    public void rotate(boolean clockWise, int centerX, int centerY){
        setRelPos(centerX,centerY);
        int[][] rotMatrix;
        if(clockWise){
            int[][] aux = {{0,-1},
                           {1,0}};
            rotMatrix = aux;
        }else{
            int[][] aux = {{0,1},
                           {-1,0}};
            rotMatrix = aux;
        }
        int auxRelX = relX, auxRelY = relY;
        relX = (rotMatrix[0][0] * auxRelX) + (rotMatrix[0][1] * auxRelY); //x = -1 y = 0 --> x -1
        relY = (rotMatrix[1][0] * auxRelX) + (rotMatrix[1][1] * auxRelY);
        
        x = centerX + relX;
        y = centerY + relY;
    }
    //==========================================================================
    //getters
    public ImageView getImageView(){
        return blockView;
    }
    public String getColor(){
        return color;
    }
    //==========================================================================
    //Drawing
    public void Draw(){
        gameGridPane.setConstraints(blockView, x, y);
    }
    public void unDraw(){
        gameGridPane.getChildren().remove(blockView);
    }
}
