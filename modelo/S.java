/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import javafx.scene.layout.GridPane;

/**
 *
 * @author mique
 */
public class S extends Piece{
    public static final String color = "green";
    public S(GridPane grid, boolean[][] gridBool){
        super(color,grid, gridBool);
        blocks[0] = new Block(4,4,color,gameGridPane); //mid
        blocks[1] = new Block(4,3,color,gameGridPane);
        blocks[2] = new Block(5,3,color,gameGridPane);
        blocks[3] = new Block(3,4,color,gameGridPane);
        int[] midPos = blocks[0].getPos();
        for(int i = 0; i<blocks.length;i++){
            blocks[i].setRelPos(midPos[0], midPos[1]);
        }
    }
}
