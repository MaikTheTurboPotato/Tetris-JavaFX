/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxmlapplication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modelo.*;
import vista.ExitConfirmationController;
import vista.GameController;
import vista.GameOverController;

/**
 *
 * @author mique
 */
public class Play extends Thread{
    //==========================================================================
    //VARIAVLES
    private boolean[][] gridBoolean = new boolean[10][25];
    private Block[][] gridBlocks = new Block[10][25];
    Button gameOverButton,startButton;
    ImageView q1,q2,q3,q4,q5,swap;
    GridPane gridPane;
    Text scoreText, fpsText,goText, linesClearedText, levelText;
    private Piece currPiece;
    String swapPiece = ""; boolean swaped = false;
    private List<Piece> pieceQueue;
    private List<Piece> pieceBag;
    private HashMap<String,Image> pieceImageMap = new HashMap<>();
    //-------------------------------------------------------------------------------------------------------------
    //Movement booleans
    private boolean GAMEOVER = false;
    private boolean moveLeft = false, moveRight = false, softDrop = false, hardDrop = false, harDroping = false, swapingPiece = false,
    rotateRightPiece = false, rotateLeftPiece = false, rotate180Piece = false, rotatingPiece = false;
    //-------------------------------------------------------------------------------------------------------------
    private final int FPS = 60, FPSTOLOCK = 30; private int AVERAGEFPS = FPS, FPSCount = 0,LOCKMovementsCount = 0, level = 1, lineCleared = 0, linesToLevelUp = 5;
    private double TARGETTIME = 1000000000/FPS, DELTA = 0, LOCKTIMER = 0, VELOCITY = 60;
    
    //==========================================================================
    //CONSTRUCTOR
    public Play(ImageView i1,ImageView i2,ImageView i3,ImageView i4,ImageView i5,ImageView swap,
            GridPane grid, Text scoreText, Text fpsText,Text goText, Button gameOverButton,Button startButton,
            Text linesClearedText, Text levelText){
        for(int i = 0; i<gridBoolean.length;i++){ //inicializar a false todo el grid
            for(int j = 0; j<gridBoolean[i].length;j++){
                gridBoolean[i][j] = false;
            }
        }
        q1=i1;q2=i2;q3=i3;q4=i4;q5=i5;this.swap=swap;gridPane=grid; //variables
        this.scoreText = scoreText; this.fpsText = fpsText; this.goText = goText; this.gameOverButton = gameOverButton;
        this.startButton = startButton; this.linesClearedText = linesClearedText; this.levelText = levelText;
        //======================================================================
        //Bindings

        //======================================================================
        //Start bags for piece generating
        pieceQueue = new LinkedList<Piece>();
        pieceBag = new ArrayList<Piece>();
        pieceBag.add(new O(gridPane,gridBoolean));
        pieceBag.add(new I(gridPane,gridBoolean));
        pieceBag.add(new T(gridPane,gridBoolean));
        pieceBag.add(new L(gridPane,gridBoolean));
        pieceBag.add(new J(gridPane,gridBoolean));
        pieceBag.add(new S(gridPane,gridBoolean));
        pieceBag.add(new Z(gridPane,gridBoolean));
        //======================================================================
        //Start Map of Pieces images
        Image piece;
        piece = new Image(getClass().getResourceAsStream("/resources/Pieces_Blocs/I.png/"));
        pieceImageMap.put("cian",piece);
        piece = new Image(getClass().getResourceAsStream("/resources/Pieces_Blocs/J.png/"));
        pieceImageMap.put("blue",piece);
        piece = new Image(getClass().getResourceAsStream("/resources/Pieces_Blocs/L.png/"));
        pieceImageMap.put("orange",piece);
        piece = new Image(getClass().getResourceAsStream("/resources/Pieces_Blocs/O.png/"));
        pieceImageMap.put("yellow",piece);
        piece = new Image(getClass().getResourceAsStream("/resources/Pieces_Blocs/S.png/"));
        pieceImageMap.put("green",piece);
        piece = new Image(getClass().getResourceAsStream("/resources/Pieces_Blocs/T.png/"));
        pieceImageMap.put("purple",piece);
        piece = new Image(getClass().getResourceAsStream("/resources/Pieces_Blocs/Z.png/"));
        pieceImageMap.put("red",piece);
    }
    //==========================================================================
    //GETTERS
    public boolean[][] getGrid(){
        return gridBoolean;
    }
    
    private Image pieceImage(String color){
        return pieceImageMap.get(color);
    }
    //==========================================================================
    //MAIN THREAD
    public void run(){
        try{
            JavaFXMLApplication.playSound("startTick");
            goText.setText("3");
            Thread.sleep(1000);
            JavaFXMLApplication.playSound("startTick");
            goText.setText("2");
            Thread.sleep(1000);
            JavaFXMLApplication.playSound("startTick");
            goText.setText("1");
            Thread.sleep(1000);
            JavaFXMLApplication.playSound("startGame");
            goText.setText("GO!");
            Thread.sleep(1000);
            //goText.setText("");
            Platform.runLater( () -> gridPane.getChildren().remove(goText));
        }catch(InterruptedException e){}
        nextPiece();
        int frames = 0;
        long now,time = 0;
        long lastTime = System.nanoTime();
        while(!GAMEOVER){
            do{
            now = System.nanoTime();
            DELTA += (now - lastTime)/TARGETTIME;
            time += (now - lastTime);
            lastTime = now;
            }while (DELTA < 1);
            DELTA--; FPSCount++; frames++;
            if(time>=1000000000){
                AVERAGEFPS = frames;
                frames = 0;time = 0;
                //fpsText.setText(AVERAGEFPS+"");
                fpsText.setText(VELOCITY+"");
            }
            //Fallin phase------------------------------------------------------
            if(!currPiece.spaceToFall()){
                //FPSCount=0;
                LOCKTIMER++;
                if(LOCKTIMER > FPSTOLOCK){
                    LOCKTIMER = 0;
                    LOCKMovementsCount = 0;
                    lockDown();
                }
            }else{LOCKTIMER = 0; LOCKMovementsCount = 0;}
            if(hardDrop){
                //HardDrop phase------------------------------------------------------
                if(!harDroping){//Hard Drop only 1 time per key pressed
                    harDroping = true;
                    addPoints(currPiece.hardDrop()*2);
                    moveRight =false;moveLeft =false;softDrop = false;swapingPiece = false;FPSCount = 0;
                    lockDown();
                }
                hardDrop = false;
            }else{
                //Movement phase------------------------------------------------------
                if(swapingPiece && !swaped){swapingPiece(); swaped = true; swapingPiece = false; FPSCount = 0;LOCKMovementsCount=0;}
                if(moveRight){
                    moveRight =false; 
                    if(currPiece.moveRight() && LOCKMovementsCount < 15){LOCKTIMER = 0; LOCKMovementsCount++;}
                }
                if(moveLeft){
                    moveLeft =false;
                    if(currPiece.moveLeft() && LOCKMovementsCount < 15){LOCKTIMER = 0; LOCKMovementsCount++;}
                } 
                if(rotateRightPiece){
                    rotateRightPiece = false;
                    if(!rotatingPiece && currPiece.rotateRight() && LOCKMovementsCount < 15){
                        rotatingPiece = true;
                        LOCKTIMER = 0; LOCKMovementsCount++;
                    }
                }
                if(rotateLeftPiece){
                    rotateLeftPiece = false;
                    if(!rotatingPiece && currPiece.rotateLeft() && LOCKMovementsCount < 15){
                        rotatingPiece = true;
                        LOCKTIMER = 0; LOCKMovementsCount++;
                    }
                }
                if(rotate180Piece){
                    rotate180Piece = false;
                    if(!rotatingPiece && currPiece.rotate180() && LOCKMovementsCount < 15){
                        rotatingPiece = true;
                        LOCKTIMER = 0; LOCKMovementsCount++;
                    }
                }
                if(softDrop){
                    if(currPiece.moveDown()){LOCKMovementsCount = 0; JavaFXMLApplication.playSound("blockMove"); addPoints(1);};
                    softDrop = false; FPSCount = 0;
                }else if(FPSCount%VELOCITY==0){
                    if(currPiece.moveDown()){LOCKMovementsCount = 0;}
                    FPSCount = 0;
                }
            }
        }//GAME LOOP
        gameOver();
    }
    //==========================================================================
    //GAME METHODS
    private void nextPiece(){
        while(pieceQueue.size() < 8){refillPiecebag();}
        currPiece = pieceQueue.remove(0);
        checkGameOverBeforeNextPiece();
        refreshImageViews();
        Platform.runLater( () -> currPiece.startPiece() );
    }
    //--------------------------------------------------------------------------
    //Score system
    private void addPoints(int p){
        setPoint(getPoints()+p);
    }
    private void setPoint(int p){
        scoreText.setText(""+p);
    }
    private int getPoints(){
        return Integer.parseInt(scoreText.getText());
    }
    //--------------------------------------------------------------------------
    private void lockDown(){
        JavaFXMLApplication.playSound("blockPlaced");
        Block[] blocks = currPiece.getBlocks();
        for(Block b : blocks){
            int[] pos = b.getPos();
            gridBoolean[pos[0]][pos[1]] = true;
            gridBlocks[pos[0]][pos[1]] = b;
        }
        swaped = false; //Allows other swap
        checkGameOverAfterLockDown();
        checkLines();
        nextPiece();
    }//--------------------------------------------------------------------------
    //Checking game over conditions
    private void checkGameOverAfterLockDown(){
        boolean gameOver = true;
        Block[] blocks = currPiece.getBlocks();
        for(int i = 0; i<blocks.length && gameOver;i++){
            int[] pos = blocks[i].getPos();
            gameOver = pos[1]<=4;
        }
        GAMEOVER = gameOver;
    }
    private void checkGameOverBeforeNextPiece(){
        Block[] blocks = currPiece.getBlocks();
        boolean spaceToSpawn = true;
        for(int i = 0; i < blocks.length && spaceToSpawn;i++){
            int[] pos = blocks[i].getPos();
            spaceToSpawn = !(gridBoolean[pos[0]][pos[1]]);
        }
        GAMEOVER = !spaceToSpawn;
    }
    private void gameOver() {
        JavaFXMLApplication.playSound("gameOver");
        Platform.runLater( () -> gridPane.add(gameOverButton,0,0,9,24));
        gameOverButton.setDisable(false);
        gameOverButton.setOpacity(1);
        
    }
    //--------------------------------------------------------------------------
    private void swapingPiece(){
        JavaFXMLApplication.playSound("blockSwap");
        Platform.runLater( () -> currPiece.unDrawPiece());
        if(swapPiece.equals("")){
            Platform.runLater( () -> currPiece.unDrawPiece());
            swapPiece = currPiece.getColor();
            nextPiece();
        }else{
            String aux = currPiece.getColor();
            switch(swapPiece){
            case "yellow": currPiece = new O(gridPane,gridBoolean); break;
            case "red": currPiece = new Z(gridPane,gridBoolean); break;
            case "green": currPiece = new S(gridPane,gridBoolean); break;
            case "cian": currPiece = new I(gridPane,gridBoolean); break;
            case "purple": currPiece = new T(gridPane,gridBoolean); break;
            case "orange": currPiece = new L(gridPane,gridBoolean); break;
            case "blue": currPiece = new J(gridPane,gridBoolean); break;
            }
            swapPiece = aux;
            Platform.runLater( () -> currPiece.startPiece() );
        }
        refreshImageViews();
    }
    //--------------------------------------------------------------------------
    //Queue image methods
    private void refreshImageViews(){
        if(!swapPiece.equals(""))swap.setImage(pieceImage(swapPiece));
        q1.setImage(pieceImage(pieceQueue.get(0).getColor()));
        q2.setImage(pieceImage(pieceQueue.get(1).getColor()));
        q3.setImage(pieceImage(pieceQueue.get(2).getColor()));
        q4.setImage(pieceImage(pieceQueue.get(3).getColor()));
        q5.setImage(pieceImage(pieceQueue.get(4).getColor()));
        
    }
    private void refillPiecebag(){
        List<Piece> aux = new ArrayList<Piece>();
        aux.add(new O(gridPane,gridBoolean));
        aux.add(new I(gridPane,gridBoolean));
        aux.add(new T(gridPane,gridBoolean));
        aux.add(new L(gridPane,gridBoolean));
        aux.add(new J(gridPane,gridBoolean));
        aux.add(new S(gridPane,gridBoolean));
        aux.add(new Z(gridPane,gridBoolean));
        int num;
        while(!aux.isEmpty()){
            num = (int) (Math.random()*aux.size());
            pieceQueue.add(aux.remove(num));
        }
    }
    //--------------------------------------------------------------------------
    //Complete line cheking and removing
    private void checkLines(){
        boolean fullLine = true;
        int linesCleared = 0;
        for(int j = 0; j<gridBoolean[0].length;j++){
            for(int i = 0; i<gridBoolean.length && fullLine;i++){
                fullLine = gridBoolean[i][j];
            }
            if(fullLine){
                clearLine(j);
                j--; linesCleared++;
            }
            fullLine = true;
        }
        switch(linesCleared){
            case 1: addPoints(100);
               break;
            case 2: addPoints(300);
               break;
            case 3: addPoints(500);
               break;
            case 4: addPoints(800);
               break;  
        }
        if(linesCleared > 0){
            JavaFXMLApplication.playSound("blockLineClear2");
            checkAllClear(linesCleared);
            levelChek(linesCleared);
        }
    }
    private void clearLine(int row){
        for(int i = 0; i < gridBlocks.length;i++){
            gridBoolean[i][row] = false;
            Block b = gridBlocks[i][row];
            Platform.runLater( () -> b.unDraw()); 
        }
        moveDownLines(row-1);
    }
    private void moveDownLines(int row){
        for(int j = row; j >= 0;j--){
            for(int i = 0; i < gridBlocks.length; i++){
                if(gridBoolean[i][j]){
                    gridBoolean[i][j] = false;
                    gridBoolean[i][j+1] = true;
                    Block b = gridBlocks[i][j];
                    gridBlocks[i][j+1] = b;
                    b.setPos(i, j+1);
                    Platform.runLater( () -> b.Draw()); 
                }
            }
        }
    }
    public void checkAllClear(int lineClear){
        boolean allClear = true;
        for(int i = 0; i <gridBoolean.length && allClear; i++){
            for(int j = 0; j<gridBoolean[i].length && allClear; j++){
                allClear = gridBoolean[i][j];
            }
        }
        if(allClear){addPoints(1800*lineClear);}
    }
    private void levelChek(int linesCleared){
        lineCleared += linesCleared; 
        linesClearedText.setText(lineCleared+"");
        //if(lineCleared >= linesToLevelUp*level + level*5){
        if(lineCleared >= level*10){
            levelUp();
        }
    }
    private void levelUp(){
       if(level < 15){
            VELOCITY -= (level<=3) ? 5 : 4;
            linesToLevelUp+=5;
            level++;
            levelText.setText(level+"");
        }
    }
    //==========================================================================
    //Reset everything
    public void resetGame(){
        //Buttons---------------------------------------------------------------
        Platform.runLater( () -> {
            gridPane.getChildren().remove(gameOverButton);
            gridPane.add(goText,2,12) ;
        });
        goText.setText("");
        Platform.runLater( () -> gridPane.add(startButton,0,0,9,24) );
        startButton.setDisable(false);
        startButton.setOpacity(1);
        //Socre-----------------------------------------------------------------
        scoreText.setText("0"); linesClearedText.setText("0");        levelText.setText("1"); 
        linesToLevelUp = 5; VELOCITY = 60; level = 1; lineCleared = 0;
        //ClearGird-------------------------------------------------------------
        Platform.runLater( () -> {
            currPiece.unDrawPiece();
            for(Block[] b1 : gridBlocks){
                for(Block b2 : b1){
                    if(b2!= null)gridPane.getChildren().remove(b2.getImageView());
                }
            }
        });
        //ClearQueue and Swao iame----------------------------------------------
        q1.setImage(null);q2.setImage(null);q3.setImage(null);
        q4.setImage(null);q5.setImage(null);swap.setImage(null);
    }
    //==========================================================================
    //PIECE MOVEMENT FROM CONTROLLER
    public void moveDownPiece(){
        currPiece.moveDown();
    }
    public void moveRightPiece(){
        moveRight = true;
    }
    public void moveLeftPiece(){
        moveLeft = true;
    }
    public void softDropPiece(){
        softDrop = true;
    }
    public void hardDropPiece(){
        hardDrop = true;
    }
    public void releaseHardDropPiece(){
        harDroping = false;
    }
    public void swapPiece(){
        swapingPiece = true;
    }
    public void roteteRightPiece(){
        rotateRightPiece = true;
    }
    public void roteteLeftPiece(){
        rotateLeftPiece = true;
    }
    public void rotete180Piece(){
        rotate180Piece = true;
    }
    public void releaseRotatePiece(){
        rotatingPiece = false;
    }
    //==========================================================================
}
