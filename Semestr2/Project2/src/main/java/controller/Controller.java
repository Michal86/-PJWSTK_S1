package controller;

import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import model.ModelManager;
import model.Player;
import solver.Astar;
import view.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Controller {

    private ViewManager              viewManager;
    private ModelManager             modelManager;
    private List<MyButton>           buttonList;
    private Map<String, MySubScenes> mySubScenesList;

    //==========================================
    public Controller(ViewManager viewManager, ModelManager modelManager) {
        this.viewManager = viewManager;
        this.modelManager = modelManager;
        buttonList = viewManager.getButtonList();
        mySubScenesList = new HashMap<>();
        addSubScenes();
        //--- Handle Main Buttons ---
        handleButtonAction();
        this.viewManager.setInfoStrings(modelManager.getPickedPlayer().getNick(),"0");
        //--- Handle Setting Scene ---
        handleSettingAddPlayer();
        handleSettingOptionPicker();
        setChoiceBox();
        sendPlayersFromModelToView();
        setPickMapList();
        //--- Handle Scores ---
        initializeScoreBoards();
    }
    //==========================================

    //--- SCORES ---
    private void initializeScoreBoards() {
        MySubScenes score = mySubScenesList.get("Score");
        setScores(score, modelManager.getRecordFromTable(3));
        setScores(score, modelManager.getRecordFromTable(4));
        setScores(score, modelManager.getRecordFromTable(5));
    }

    private void checkWinnersTable(Player winner) {
        MySubScenes score = mySubScenesList.get("Score");
        int difficulty = winner.getDifficulty();
        if (modelManager.passed(winner)) {
            List<Player> newList = modelManager.getRecordFromTable(difficulty);
            if (newList != null)
                setScores(score, newList);
        }
    }

    private void setScores(MySubScenes score, List<Player> newList) {
        newList.forEach(p -> score.getScoreScene().updateView(
                newList.indexOf(p), p.getDifficulty(),
                p.getPosition(), p.getNick(), p.getMoves(), p.getMap())
        );
    }

    //--- SETTING ---
    //- map picker -
    private void setPickMapList() {
        MySubScenes setting = mySubScenesList.get("Setting");
        MapPicker mapPicker = setting.getSettingScene().getMapPicker();
        mapPicker.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null)
                        mapPicker.setPickedMap((ImageView) newValue);

                    String mapName = mapPicker.getPickedMapName((ImageView) newValue);
                        modelManager.setPickedMap(mapName);
                        viewManager.updateMiniMap(mapName);
                }
        );
    }

    //- add player -
    private void handleSettingAddPlayer() {
        MySubScenes setting = mySubScenesList.get("Setting");
        setting.getSettingScene().getAddButtonPlayer().setOnAction(
                event -> {
                    String checkName = setting.getSettingScene().getEnteredNick();
                    checkNickForNewPlayer(setting, checkName);
                }
        );
        //--- keyboard handle ---
        setting.getSettingScene().getNickField().setOnKeyPressed(
                event -> {
                    if (event.getCode().equals(KeyCode.ENTER)) {
                        String checkName = setting.getSettingScene().getEnteredNick();
                        checkNickForNewPlayer(setting, checkName);
                    }
                }
        );
    }

    private void checkNickForNewPlayer(MySubScenes setting, String nickToAdd) {
        if (!nickToAdd.equals("")) {
            modelManager.checkNicks(nickToAdd);
            if (!modelManager.isTaken()) {
                modelManager.addNewPlayer(nickToAdd);
                setting.getSettingScene().clearNickTextField(true);
                //modelManager.displayPlayers();
            } else {
                setting.getSettingScene().clearNickTextField(false);
            }
        }
    }

    //- choiceBox with Players -
    private void setChoiceBox() {
        MySubScenes setting = mySubScenesList.get("Setting");
        ChoiceBox<Player> choiceBox = setting.getSettingScene().getChoiceBox();
        ObservableList<Player> obsList = modelManager.getObsList();
        choiceBox.setItems(obsList);
        choiceBox.isShowing();
        choiceBox.getSelectionModel().selectFirst();
    }

    private void sendPlayersFromModelToView() {
        MySubScenes setting = mySubScenesList.get("Setting");
        ChoiceBox<Player> choiceBox = setting.getSettingScene().getChoiceBox();
        choiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        modelManager.setPickedPlayer(newValue);
                        viewManager.updateNickInfo(modelManager.getPickedPlayer().getNick());
                        viewManager.updateMovesInfo("0");
                    }
                }
        );
    }

    //- difficulty picker -
    private void handleSettingOptionPicker() {
        MySubScenes setting = mySubScenesList.get("Setting");

        setting.getSettingScene().getDifficultyList().forEach(
                optPicker -> optPicker.setOnMousePressed(event -> {
                    if (!optPicker.isChoosen()) {
                        optPicker.setCircleAsChoosen(!optPicker.isChoosen());
                        int diff = setting.getSettingScene().checkPickedDifficulty(optPicker.getOption());
                        modelManager.setMapDifficulty(diff);
                        setting.getSettingScene().setDifficulty(diff);
                    }
                })
        );
    }

    //--- PLAY ---
    //--- * add mouse handler to images * ---
    private void handlePlayScene() {
        MySubScenes play = mySubScenesList.get("Play");
        int range = modelManager.getMapDifficulty();
        modelManager.setMapDifficulty(range);
        //=== TEST SOLVER ===
        viewManager.getSolverButton().setOnAction( b -> {
            boolean solutionFound = Astar.search(play.getPlayScene().getBoardState());
            System.out.println("Solution found: " + solutionFound);
        });

        //===================
        for (int i = 0; i < range; i++) {
            for (int j = 0; j < range; j++) {
                MyImgView imgToHandle = play.getPlayScene().getBoardPiece(i, j);

                imgToHandle.getImg().setOnMousePressed(event -> {
                    if (play.getPlayScene().isMovable(imgToHandle)) {
                        play.getPlayScene().swapImagesWithBlank(imgToHandle);
                        modelManager.getPickedPlayer().addMoves();
                        viewManager.updateMovesInfo(""+modelManager.getPickedPlayer().getMoves());
                        if (checkGameState(play.getPlayScene().getBoardState(), modelManager.getBoard()))
                        {
                            //- Check score against hitlist records -
                            checkWinnersTable(modelManager.getPickedPlayer());
                            //- Move to Score -
                            moveMySubScene(mySubScenesList.get("Score"), "Score");
                            modelManager.getPickedPlayer().resetMoves();
                            modelManager.save();
                        }
                    }
                });
            }
        }

    }

    //--- Check game state ---
    private boolean checkGameState(int[][] stateNow, int[][] toWin) {
        boolean won = true;
        for (int i = 0; i < toWin.length; i++) {
            for (int j = 0; j < toWin.length; j++)
                if (stateNow[i][j] != toWin[i][j]) won = false;
        }
        return won;
    }

    //--- Set mainButton handler method ---
    private void handleButtonAction() {
        buttonList.forEach( btn -> btn.setOnAction(event -> {
            if (btn.getNAME().equals("Exit")) {
                viewManager.quit();
            } else
                 moveMySubScene(mySubScenesList.get(btn.getNAME()), btn.getNAME());
        }));
    }

    //--- To show/hide my subScenes ---
    private void moveMySubScene(MySubScenes ss, String btnName) {
        if (btnName.equals("Play")) {
            //--- updates ---
            modelManager.UpdatePlayerBeforePlay();
            modelManager.setBoard(modelManager.getMapDifficulty());
            viewManager.updateNickInfo(modelManager.getPickedPlayer().getNick());
            viewManager.updateMovesInfo("0");
            viewManager.updateMiniMap(modelManager.getPickedMap());
            ss.setPlaySceneComponents(modelManager.getMapDifficulty(), modelManager.getPickedMap());
            ss.moveSubScene();
            //--- Handle Play board ---
            handlePlayScene();
        } else {
            ss.moveSubScene();
        }
        hideOtherSubScenes(ss, btnName);
    }

    private void hideOtherSubScenes(MySubScenes ss, String name) {
        List<MySubScenes> tmpList = new LinkedList<>(mySubScenesList.values());

        tmpList.forEach(subScene -> {
            if (!subScene.isNotDisplayed() && !subScene.equals(ss)) {
                subScene.moveSubScene();
                setButtonsToNormal(name);
            }
        });
    }

    //--- set not pressed buttons to normal ---
    private void setButtonsToNormal(String name) {
        buttonList.forEach(btn -> {
            if (!btn.getNAME().equals(name) && btn.isBtnPressed()) {
                btn.setIsPressed();
                btn.setButtonNormalStyle();
            }
        });
    }

    //--- getters & setters ---
    private void setMySubScenesList(String name, MySubScenes subScene) {
        if (subScene != null)
            mySubScenesList.put(name, subScene);
    }

    private void addSubScenes() {
        setMySubScenesList("Play", viewManager.getPlaySub());
        setMySubScenesList("Setting", viewManager.getSettingSub());
        setMySubScenesList("Score", viewManager.getScoreSub());
    }

    private MySubScenes getSubScene(String btnName) {
        return mySubScenesList.get(btnName);
    }

    //--- get from model ---
    public int getMapDifficulty() {
        return modelManager.getMapDifficulty();
    }

    public String getMapName() {
        return modelManager.getPlayerMapName();
    }
}
