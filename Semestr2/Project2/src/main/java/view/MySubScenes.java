package view;

import javafx.animation.TranslateTransition;
import javafx.scene.SubScene;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class MySubScenes extends SubScene {

    private AnchorPane      anchorPane;
    private PlayScene       playScene;
    private SettingScene    settingScene;
    private ScoreScene      scoreScene;
    private boolean         isHidden;

    //==========================================
    protected MySubScenes(String color) {
        super(new AnchorPane(), 660, 620);
        prefHeight(620);
        prefWidth(660);
        isHidden = true;
        anchorPane = (AnchorPane) this.getRoot();
        anchorPane.setStyle("-fx-background-color: #" + color + ";");
        setLayoutX(292);
        setLayoutY(-620);
        //---
        playScene = null;
        settingScene = null;
        scoreScene = null;
    }
    //==========================================

    public void moveSubScene() {
        double move;
        if (isHidden) {
            move = +660;
            isHidden = false;
        } else {
            move = -660;
            isHidden = true;
        }
        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.3));
        transition.setNode(this);
        transition.setToY(move);

        transition.play();
    }

    //--- Add GAME/PLAY SCENE components ---
    public void setPlaySceneComponents(int difficulty, String mapName) {
        if (playScene != null) {
            playScene.clearAnchor();
        }
        playScene = new PlayScene(
                anchorPane, difficulty, difficulty, mapName
        );
    }

    public PlayScene getPlayScene() {
        return playScene;
    }

    //--- Add SETTING SCENE components ---
    public void setSettingSceneComponents(int difficulty) {
        settingScene = new SettingScene(anchorPane, difficulty);
    }

    public SettingScene getSettingScene() {
        return settingScene;
    }

    //--- Add SCORE SCENE components ---
    public void setScoreSceneComponents() {
        scoreScene = new ScoreScene(anchorPane);
    }

    public ScoreScene getScoreScene() {
        return scoreScene;
    }

    //----------------------------
    public boolean isNotDisplayed() {
        return isHidden;
    }

}
