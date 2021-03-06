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
import java.util.concurrent.atomic.AtomicBoolean;

public class Controller {

    private ViewManager              viewManager;
    private ModelManager             modelManager;
    //--- Helpers ---
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

    //=== SCORES ===
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

    //- Set new score on specific board -
    private void setScores(MySubScenes score, List<Player> newList) {
        newList.forEach(p -> score.getScoreScene().updateView(
                newList.indexOf(p), p.getDifficulty(),
                p.getPosition(), p.getNick(), p.getMoves(), p.getMap())
        );
    }

    //=== SETTING ===
    //- Map picker -
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

    //- Add player -
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

    //- Difficulty picker -
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

    //=== PLAY ===
    //- Shuffle board --
    private void handleShuffling(){
        while (!modelManager.isShuffleDone()) {
            viewManager.getPlaySub()
                    .getPlayScene().
                    shufflingMethod(modelManager.getBoardPointToShuffle());
            modelManager.updateBoardState(viewManager.getPlaySub().getPlayScene().getBoardState());
        }
    }

    //- Add mouse handler to board images and Solver -
    private void handlePlayScene() {
        MySubScenes play = mySubScenesList.get("Play");
        AtomicBoolean playIsActive = new AtomicBoolean(true);
        int range = modelManager.getMapDifficulty();
        modelManager.setMapDifficulty(range);
        // Shuffle Pieces and add to PlayScene
        handleShuffling();
        play.getPlayScene().addBoardToPane();
        // Solver
        viewManager.getSolverButton().setOnAction( b -> checkForSolution(playIsActive) );
        //==============
        for (int i = 0; i < range; i++) {
            for (int j = 0; j < range; j++) {
                MyImgView imgToHandle = play.getPlayScene().getBoardPiece(i, j);

                imgToHandle.getImg().setOnMousePressed(event -> {
                    if (play.getPlayScene().isMovable(imgToHandle)) {
                        play.getPlayScene().swapImagesWithBlank(imgToHandle);
                        modelManager.getPickedPlayer().addMoves();
                        viewManager.updateMovesInfo(""+modelManager.getPickedPlayer().getMoves());

                        if (modelManager.isSolutionFound()) {
                            viewManager.displaySolutionMoves(
                                    modelManager.getSolutionMoveFromList()
                            );
                        }

                        //- Check current state vs GOAL -
                        modelManager.updateBoardState(play.getPlayScene().getBoardState());
                        if (modelManager.checkGameState()) {
                            //- Check score against hitlist records -
                            checkWinnersTable(modelManager.getPickedPlayer());
                            //- Move to Score -
                            moveMySubScene(mySubScenesList.get("Score"), "Score");
                            modelManager.getPickedPlayer().resetMoves();
                            modelManager.save();
                            viewManager.removeSolutionBoxes();
                            playIsActive.set(false);
                        }
                    }
                });
            }
        }

    }

    //=== View Manager ===
    //- Solver button pressed -
    private void checkForSolution(AtomicBoolean activeButton){
        if (activeButton.get()) {
            viewManager.removeSolutionBoxes();
            boolean found = Astar.search(viewManager.getPlaySub().getPlayScene().getBoardState());
            viewManager.setSolutionFound(found);
            modelManager.setSolutionFound(found);
            if (found) {
                modelManager.setSolutionMovesList(Astar.getSolutionMoves());
                viewManager.displaySolutionMoves(
                        modelManager.getSolutionMoveFromList()
                );
            }
        }
    }

    //- Set mainButton handler method -
    private void handleButtonAction() {
        buttonList.forEach( btn -> btn.setOnAction(event -> {
            if (btn.getNAME().equals("Exit")) {
                viewManager.quit();
            } else {
                moveMySubScene(mySubScenesList.get(btn.getNAME()), btn.getNAME());
                viewManager.removeSolutionBoxes();
            }
        }));
    }

    //- To show/hide my subScenes -
    private void moveMySubScene(MySubScenes sub, String btnName) {
        if (btnName.equals("Play")) {
            //--- updates ---
            modelManager.UpdatePlayerBeforePlay();
            modelManager.setBoard(modelManager.getMapDifficulty());
            viewManager.updateNickInfo(modelManager.getPickedPlayer().getNick());
            viewManager.updateMovesInfo("0");
            viewManager.updateMiniMap(modelManager.getPickedMap());
            sub.setPlaySceneComponents(modelManager.getMapDifficulty(), modelManager.getPickedMap());
            sub.moveSubScene();
            //--- Handle Play board ---
            handlePlayScene();
        } else {
            sub.moveSubScene();
        }
        hideOtherSubScenes(sub, btnName);
    }

    private void hideOtherSubScenes(MySubScenes sub, String name) {
        List<MySubScenes> tmpList = new LinkedList<>(mySubScenesList.values());

        tmpList.forEach(subScene -> {
            if (!subScene.isNotDisplayed() && !subScene.equals(sub)) {
                subScene.moveSubScene();
                setButtonsToNormal(name);
            }
        });
    }

    //- Set not pressed buttons to normal -
    private void setButtonsToNormal(String name) {
        buttonList.forEach(btn -> {
            if (!btn.getNAME().equals(name) && btn.isBtnPressed()) {
                btn.setIsPressed();
                btn.setButtonNormalStyle();
            }
        });
    }

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
