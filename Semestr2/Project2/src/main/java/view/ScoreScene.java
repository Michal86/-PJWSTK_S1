package view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class ScoreScene {

    private AnchorPane      anchorPane;
    private String[]        scoreType = {"scoreTop", "scoreMid", "scoreBot"};
    //--- score tables ---
    private VBox            topScoreBoard,
                            midScoreBoard,
                            botScoreBoard;
    private PlayersData[]   playersTopData,
                            playersMidData,
                            playersBotData;

    //==========================================
    protected ScoreScene(AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
        addTopInfoPanel();
        addScoreBoards();
        //--- 3 boards ---
        topScoreBoard = setScoreBoard(130, 72);
        midScoreBoard = setScoreBoard(130, 258);
        botScoreBoard = setScoreBoard(130, 444);
        initScoreBoard();
    }
    //==========================================

    private void initScoreBoard() {
        playersTopData = createDataHolders();
        playersMidData = createDataHolders();
        playersBotData = createDataHolders();
        //- Put lines of PlayersData[] to specific board -
        addLinesToBoard(topScoreBoard, playersTopData);
        addLinesToBoard(midScoreBoard, playersMidData);
        addLinesToBoard(botScoreBoard, playersBotData);
    }

    //--- set VBox to represent a board ---
    private VBox setScoreBoard(double x, double y) {
        VBox newBox = new VBox();
        newBox.setLayoutX(x);
        newBox.setLayoutY(y);
        newBox.setSpacing(1);
        newBox.setStyle("-fx-padding: 4; -fx-spacing: 1;");
        //---
        anchorPane.getChildren().add(newBox);
        return newBox;
    }

    //--- add score to my boards ---
    private void addScoreBoards() {
        VBox vBoxScores = new VBox();
        vBoxScores.setAlignment(Pos.CENTER);
        vBoxScores.setSpacing(20);
        vBoxScores.setLayoutX(20);
        vBoxScores.setLayoutY(61);
        //--- 3 Score Boards ---
        ScoreHolder maps3x3 = setScoreBox(0);
        ScoreHolder maps4x4 = setScoreBox(1);
        ScoreHolder maps5x5 = setScoreBox(2);
        //--- add all ---
        vBoxScores.getChildren().addAll(maps3x3, maps4x4, maps5x5);
        anchorPane.getChildren().add(vBoxScores);
    }

    private ScoreHolder setScoreBox(int index) {
        return new ScoreHolder(scoreType[index]);
    }

    //- creates empty places to store Players data -
    private PlayersData[] createDataHolders() {
        PlayersData[] newTab = new PlayersData[5];
        for (int j = 0; j < newTab.length; j++) {
            newTab[j] = new PlayersData();
            newTab[j].addBoxesWithData((j + 1) + " ", "", "");
            if (j % 2 != 0) newTab[j].setStyle("-fx-background-color: #eff2d4;");
        }
        return newTab;
    }

    private void addLinesToBoard(VBox whereTo, PlayersData[] what) {
        whereTo.getChildren().addAll(what);
    }

    //--- to update score boards view | list has to be sorted! ---
    public void updateView(int index, int whichBoard, int position, String name, int moves, String map) {
        PlayersData[] listToUpdate;

        if (whichBoard == 3) listToUpdate = playersTopData;
        else if (whichBoard == 4) listToUpdate = playersMidData;
        else listToUpdate = playersBotData;

        listToUpdate[index].updatePlayerData(position, name, String.valueOf(moves), map);
    }

    //--- add top info panel ---
    private void addTopInfoPanel() {
        Label topPanel = new Label();
        String TOP_PANEL = "/topPanel.png";
        BackgroundImage bg = new BackgroundImage(
                new Image(TOP_PANEL, 500, 30, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        topPanel.setPrefSize(500, 30);
        topPanel.setBackground(new Background(bg));
        topPanel.setLayoutX(128);
        topPanel.setLayoutY(20);

        anchorPane.getChildren().add(topPanel);
    }

}
