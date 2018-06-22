package view;

import controller.Controller;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import solver.Moves;

import java.util.LinkedList;
import java.util.List;

public class ViewManager {

    private static final int WIDTH = 1024;
    private static final int HEIGHT = 768;
    private AnchorPane      mainPane;
    private Scene           mainScene;
    private Stage           mainStage;
    private List<MyButton>  buttonList;
    //--- sub scenes ---
    private MySubScenes     playSub;
    private MySubScenes     settingSub;
    private MySubScenes     scoreSub;
    private int             difficulty;
    //--- mini info panel+miniMap ---
    private ScoreHolder     nickBox, scoreBox,
                            mapBox, movesBox, movesNotFoundBox;
    private ImageView       miniMap, moveImg;
    private Text            nickTxt,
                            scoreTxt;
    private MyButton        solverButton;
    private MovesImg        solutionMoveList;
    //--- * ---
    private Controller      controller;

    //==========================================
    public ViewManager() {
        mainPane = new AnchorPane();
        mainScene = new Scene(mainPane, WIDTH, HEIGHT);
        mainScene.setFill(Color.TRANSPARENT);
        mainStage = new Stage();
        mainStage.setScene(mainScene);
        buttonList = new LinkedList<MyButton>();
        //by default
        String mapName = "compass";
        difficulty = 3;
        createBackground();
        createMainButtons();
        //--- Create Sub scenes ---
        setDefaultPlaySubScene();
        setSettingSubScene();
        setScoreSubScene();
        //--- create mini info ---
        setMiniInfoPanel(mapName);
        //--- add ---
        mainPane.getChildren().addAll(playSub, settingSub, scoreSub);
    }
    //==========================================

    //--- SET PLAY SCENE ---
    private void setDefaultPlaySubScene() {
        playSub = new MySubScenes("57c582");
        playSub.setPlaySceneComponents(difficulty, "");
    }

    //--- SET SETTING SCENE ---
    private void setSettingSubScene() {
        settingSub = new MySubScenes("fab414");
        settingSub.setSettingSceneComponents(difficulty);
    }

    private void setScoreSubScene() {
        scoreSub = new MySubScenes("2094b3");
        scoreSub.setScoreSceneComponents();
    }

    //--- add my buttons and position them ---
    private void addMainButton(MyButton myButton, double positionY) {
        mainPane.getChildren().add(myButton);
        buttonList.add(myButton);
        myButton.setLayoutX(52);
        myButton.setLayoutY(positionY);
    }

    //--- * creates menu buttons * ---
    private void createMainButtons() {
        double positionY = 0.0;
        //- start button -
        MyButton playButton = new MyButton("Play");
        addMainButton(playButton, positionY);
        positionY = playButton.getPrefHeight();

        //- setting button -
        MyButton settingButton = new MyButton("Setting");
        addMainButton(settingButton, positionY);
        positionY += settingButton.getPrefHeight();

        //- score button -
        MyButton scoreButton = new MyButton("Score");
        addMainButton(scoreButton, positionY);
        positionY += scoreButton.getPrefHeight();

        //- exit button -
        MyButton exitButton = new MyButton("Exit");
        addMainButton(exitButton, positionY);
    }

    //--- make updates to info---
    public void updateNickInfo(String newTxt) {
        nickTxt.setText(newTxt);
    }

    public void updateMovesInfo(String newTxt) {
        scoreTxt.setText(newTxt);
    }

    public void updateMiniMap(String mapName){
        miniMap.setImage(new Image("/" + mapName + ".png"));
        miniMap.setFitWidth(190);
        miniMap.setPreserveRatio(true);
        miniMap.setSmooth(true);
        miniMap.setCache(true);
    }

    //--- set: nick, score, minimap ---
    private void setMiniInfoPanel(String mapName){
        nickBox = new ScoreHolder("nick_box",160, 30,76,378);
        scoreBox = new ScoreHolder("score_box",160, 30,76,410);
        mapBox = new ScoreHolder("miniMap_box",200, 200,56,449);
        mainPane.getChildren().addAll(nickBox,scoreBox,mapBox);
        //--- set strings + map img ---
        setMiniMap(mapName);
        setSolverButton("solve");
    }

    private Text txtHolder(String str, double positionX, double positionY) {
        Text txtField = new Text(str);
        txtField.setLayoutX(positionX);
        txtField.setLayoutY(positionY);
        txtField.setStyle("-fx-fill: #fab414; -fx-font-weight: bolder; -fx-font-size: 20px;");
        return txtField;
    }

    public void setInfoStrings(String nick, String moves){
        nickTxt  = txtHolder(nick, 112, nickBox.getLayoutY()+22);
        scoreTxt = txtHolder(moves, 112, scoreBox.getLayoutY()+22);
        mainPane.getChildren().addAll(nickTxt, scoreTxt);
    }

    private void setMiniMap(String mapName){
        miniMap = new ImageView();
        updateMiniMap(mapName);
        miniMap.setLayoutX(mapBox.getLayoutX()+5);
        miniMap.setLayoutY(mapBox.getLayoutY()+5);
        mainPane.getChildren().add(miniMap);
    }

    private void setSolverButton(String name){
        solverButton = new MyButton(name, 160, 33);
        solverButton.setLayoutX(nickBox.getLayoutX());
        solverButton.setLayoutY((miniMap.getLayoutY()+miniMap.getFitWidth())+10);
        mainPane.getChildren().add(solverButton);
        setSolverNotFound();
        setSolverMovesBox();
    }

    private void setSolverMovesBox(){
        movesBox = new ScoreHolder("solverMoves_box",161, 36,542, 670);
    }

    private void setSolverNotFound(){
        movesNotFoundBox = new ScoreHolder("solverNotFound_box",261, 60,492, 670);
    }

    public void setSolutionFound(boolean solutionFound){
        if (solutionFound) {
            mainPane.getChildren().add(movesBox);
            createMoveImages();
            }
        else
            mainPane.getChildren().add(movesNotFoundBox);
    }

    public void removeSolutionBoxes(){
        mainPane.getChildren().remove(movesBox);
        mainPane.getChildren().remove(movesNotFoundBox);
    }

    public MyButton getSolverButton() {
        return solverButton;
    }

    private void createMoveImages(){
        solutionMoveList = new MovesImg();
        moveImg = new ImageView();
        displaySolutionMoves(Moves.ROOT);
        mainPane.getChildren().add(moveImg);
    }

    public void displaySolutionMoves(Moves moveToDisplay) {
        Image img = solutionMoveList.getMoveImage(moveToDisplay);
        moveImg.setImage(img);
        moveImg.setFitWidth(img.getWidth());
        moveImg.setFitHeight(img.getHeight());
        moveImg.setLayoutX(movesBox.getLayoutX()+93.5-(moveImg.getFitWidth()/2));
        moveImg.setLayoutY(movesBox.getLayoutY()+29-(moveImg.getFitHeight()/2));
    }

    //--- Add main background ---
    private void createBackground() {
        Image myBg = new Image("/mainFrame.png", 1024, 768, false, true);
        BackgroundImage background = new BackgroundImage(
                myBg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT, null);
        mainPane.setBackground(new Background(background));
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public MySubScenes getPlaySub() {
        return playSub;
    }

    public MySubScenes getSettingSub() {
        return settingSub;
    }

    public MySubScenes getScoreSub() {
        return scoreSub;
    }

    public List<MyButton> getButtonList() {
        return buttonList;
    }

    //------------------------------------------------
    public void quit() {
        Platform.exit();
        System.exit(0);
    }
}
