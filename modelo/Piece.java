/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafxmlapplication.JavaFXMLApplication;

/**
 *
 * @author mique
 */
public abstract class Piece {
    Block[] blocks = new Block[4];
    String color;
    GridPane gameGridPane;
    VBox  gameVBox;
    protected boolean[][] gridBool;
    protected final int NORTH = 0, EAST = 1, SOUTH = 2, WEST = 3;
    protected int orientation = NORTH;
    int[][][] JLSTZ_Offset;// = new int[5][4][2];
    int[][][] I_Offset;// = new int[5][4][2];
    int[][][] O_Offset;// = new int[5][4][2];
    public Piece(String color, GridPane gridP, boolean[][] g){
        this.color = color;
        this.gameGridPane = gridP;
        this.gridBool = g;
        startOffsets();
    }
    //==========================================================================
    //start the offsets matrix
    private void startOffsets(){        
        int[][][] aux = {{{0,0},{0,0},{0,0},{0,0}}, //0
                       {{0,0},{1,0},{0,0},{-1,0}}, //1
                       {{0,0},{1,-1},{0,0},{-1,-1}}, //2
                       {{0,0},{0,2},{0,0},{0,2}}, //3
                       {{0,0},{1,2},{0,0},{-1,2}}}; //4
        JLSTZ_Offset = aux;
        int[][][] aux1 = {{{0,0},{-1,0},{-1,1},{0,1}}, //0
                       {{-1,0},{0,0},{1,1},{0,1}}, //1
                       {{2,0},{0,0},{-2,1},{0,1}}, //2
                       {{-1,0},{0,1},{1,0},{0,-1}}, //3
                       {{2,0},{0,-2},{-2,0},{0,2}}}; //4
        I_Offset = aux1;
        int[][][] aux2 = {{{0,0},{0,-1},{-1,-1},{-1,0}}, //0
                         {{0,0},{0,0},{0,0},{0,0}}, //1 NO USED
                         {{0,0},{0,0},{0,0},{0,0}}, //2 NO USED
                         {{0,0},{0,0},{0,0},{0,0}}, //3 NO USED
                         {{0,0},{0,0},{0,0},{0,0}},}; //4 NO USED
        O_Offset = aux2;
        //Invertir offsets
        /*for(int i = 0;i < 5; i++){
            for(int j = 0;j < 4; j++){
                for(int t = 0;t < 2; t++){
                    JLSTZ_Offset[i][j][t] *= -1;
                    I_Offset[i][j][t] *= -1;
                    O_Offset[i][j][t] *= -1;
                }
            }
        }
        for(int i = 0;i < 5; i++){
            for(int j = 0;j < 4; j++){
                for(int t = 0;t < 2; t++){
                    JLSTZ_Offset[i][j][t] = 0;
                    I_Offset[i][j][t] = 0;
                    O_Offset[i][j][t] = 0;
                }
            }
        }*/
    }
    //==========================================================================
    //Getters
    public String getColor(){return color;}
    public Block[] getBlocks(){return blocks;}
    //==========================================================================
    //Adds the blocs in the grid
    public void startPiece(){
        for(Block b : blocks){
            gameGridPane.add(b.getImageView(), b.getPos()[0], b.getPos()[1]);
        }
    }
    //==========================================================================
    //Draw and unDraw the piece in the grid
    public void drawPiece(){
        for(Block b : blocks){
            gameGridPane.setConstraints(b.getImageView(), b.getPos()[0], b.getPos()[1]);
        }
    }
    public void unDrawPiece(){
        for(Block b : blocks){
            gameGridPane.getChildren().remove(b.getImageView());
        }
    }
    //==========================================================================
    //Checkings
    public boolean spaceToFall(){
        boolean res = true;
        int[] pos;
        for(Block b : blocks){
            pos = b.getPos();
            if((pos[1]+1>24) || gridBool[pos[0]][pos[1]+1]){res = false; break;}
        }
        return res;
    }
    public void recalcRelPos(){
        int[] midPos = blocks[0].getPos();
        for(int i = 0; i<blocks.length;i++){
            blocks[i].setRelPos(midPos[0], midPos[1]);
        }
    }
    //==========================================================================
    //Movements
    public boolean moveDown(){
        boolean res = spaceToFall();
        /*if(res){
            int[] pos;
            for(Block b : blocks){
                pos = b.getPos();
                b.setPos(pos[0],pos[1]+1);
            }
        }
        drawPiece();*/
        if(res){
            return movePiece(0,1);
        }else{return false;}
    }
    public int hardDrop(){
        JavaFXMLApplication.playSound("blockHardDrop");
        int res = 0;
        while(moveDown()){res++;}
        return res;
    }
    public boolean moveRight(){
        /*boolean res = true;
        int[] pos;
        for(Block b : blocks){
            pos = b.getPos();
            if((pos[0]+1>9) || gridBool[pos[0]+1][pos[1]]){res = false; break;}
        }
        if(res){
            for(Block b : blocks){
                pos = b.getPos();
                b.setPos(pos[0]+1,pos[1]);
            }
        }
        if(res){drawPiece(); JavaFXMLApplication.playSound("blockRotate");}*/
        return movePiece(1,0);
    }
    public boolean moveLeft(){
        /*boolean res = true;
        int[] pos;
        for(Block b : blocks){
            pos = b.getPos();
            if((pos[0]-1<0) || gridBool[pos[0]-1][pos[1]]){res = false; break;}
        }
        if(res){
            for(Block b : blocks){
                pos = b.getPos();
                b.setPos(pos[0]-1,pos[1]);
            }
        }
        if(res){drawPiece(); JavaFXMLApplication.playSound("blockRotate");}*/
        return movePiece(-1,0);
    }
    
    public boolean movePiece(int x, int y){
        boolean res = true;
        int[] pos = {x,y};
        res = canMovePiece(pos);
        if(res){
            for(Block b : blocks){
                pos = b.getPos();
                b.setPos(pos[0]+x,pos[1]+y);
            }
        }
        if(res){drawPiece(); JavaFXMLApplication.playSound("blockMove");}
        return res;
    }
    //==========================================================================
    //Piece Rotation
    public boolean rotateRight(){
        
        boolean res = rotate(true, true);
        if(res) JavaFXMLApplication.playSound("blockRotate");
        return res;
    }
    public boolean rotateLeft(){
        boolean res = rotate(false, true);
        if(res) JavaFXMLApplication.playSound("blockRotate");
        return res;
    }
    public boolean rotate180(){
        boolean res = rotate(false, false);
        if(res){
            res = rotate(false, true);
            if(res){return res;}
            else{rotate(true,false);}
        }
        if(!res){
            res = rotate(true, false);
            if(res){
                res = rotate(true, true);
                if(res){return res;}
                else{rotate(false, false);}
            }
        }
        if(res) JavaFXMLApplication.playSound("blockRotate");
        return res;
    }
    
    private boolean rotate(boolean Right, boolean shouldOffSet){
        int oldOrientation = orientation;
        orientation += Right ? 1 : -1;
        orientation = ModF(orientation, 4);
        
        int[] centerPos =  blocks[0].getPos();
        for(int i = 0; i < blocks.length ;i++){
            blocks[i].rotate(Right, centerPos[0], centerPos[1]);
        }
        
        if(!shouldOffSet){return true;} //If you can't rotate, calls again the function
        //When it does this set to flase to put again the piece were it was without moving it
        
        boolean canOffset = Offset(oldOrientation, orientation);
        
        if(!canOffset){rotate(!Right,false);}
        
        return canOffset;
    }
    
    private int ModF(int x, int m){
        return (x%m + m)% m;
    }
    
    private boolean Offset(int oldOrientation, int newOrientation){
        int[] offset1, offset2, endOffset = {0,0};
        int[][][] currOffsetMatrix;
        switch(color){
            case "cian": currOffsetMatrix = I_Offset;
                break;
            case "yellow": currOffsetMatrix = O_Offset;
                break;
            default: currOffsetMatrix = JLSTZ_Offset;
                break;
        }
        int numberOfTest = (color.equals("yellow")) ? 1 : 5;
        
        boolean movePossible = false;
        
        for(int index = 0; index < numberOfTest && !movePossible;index++){
            offset1 = currOffsetMatrix[index][oldOrientation];
            offset2 = currOffsetMatrix[index][newOrientation];
            endOffset[0] = (offset1[0] - offset2[0]);
            endOffset[1] = -(offset1[1] - offset2[1]);
            
            movePossible = canMovePiece(endOffset);
        }
        
        if(movePossible){movePiece(endOffset[0],endOffset[1]);}
            
        return movePossible;
    }
    private boolean canMovePiece(int[] offset){
        boolean res = true;
        int x = offset[0], y = offset[1];
        
        int[] pos;
        for(Block b : blocks){
            pos = b.getPos();
            if((pos[0]+x<0) || (pos[0]+x>9)|| (pos[1]+y<0) || (pos[1]+y>24) ||
                    gridBool[pos[0]+x][pos[1]+y]){
                res = false; break;
            }
        }
        
        return res;
    }
    
    //==========================================================================
    //
}
