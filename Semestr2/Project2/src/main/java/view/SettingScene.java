package view;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import model.Player;
import java.util.LinkedList;
import java.util.List;

public class SettingScene {

    private final String        PANEL_PLAYER;
    private final String        PANEL_IMAGES;
    private final String        PANEL_DIFFICULTY;
    private final String        BUTTON_IMAGE;
    private final String        TXT_FIELD_IMAGE;
    private AnchorPane          anchorPane;
    //-- Players --
    private ChoiceBox<Player>   choiceBox;
    private MyButton            addButtonPlayer;
    private TextField           nickField;
    ImageView                   imgPlayer,
                                imgImages,
                                imgDifficulty;
    //-- Difficulty --
    private List<OptionPicker> difficultyList;
    private int                 defaultDiff;
    //-- Maps ---
    MapPicker                   mapPicker;

    //==========================================
    protected SettingScene(AnchorPane anchorPane, int defaultDiff) {
        this.anchorPane = anchorPane;
        BUTTON_IMAGE = "-fx-background-color: transparent; -fx-background-image: url('/addButton.png')";
        TXT_FIELD_IMAGE = "/nick_bg.png";
        PANEL_PLAYER = "/panel_player.png";
        PANEL_IMAGES = "/panel_images.png";
        PANEL_DIFFICULTY = "/panel_difficulty.png";
        this.defaultDiff = defaultDiff;
        difficultyList = new LinkedList<>();
        getImgViews();
        //--- Difficulty ---
        setImagesOnScene();
        addDifficultyOptions(defaultDiff);
        //--- Players ---
        addPlayersListView();
        addNickButton();
        addNickTextField();
        //--- Maps ---
        addMapsListView();
    }
    //==========================================

    //=== ADD/PICK PLAYER PANEL ===
    public MapPicker getMapPicker() {
        return mapPicker;
    }

    private void addMapsListView() {
        mapPicker = new MapPicker();
        mapPicker.addMapToListView();
        mapPicker.getSelectionModel().selectFirst();
        mapPicker.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        mapPicker.setLayoutX(getPositionX(imgImages) + 25);
        mapPicker.setLayoutY(getPositionY(imgImages) + 35);
        anchorPane.getChildren().add(mapPicker);
    }

    //-- Add/show list of available players --
    private void addPlayersListView() {
        choiceBox = new ChoiceBox<>(FXCollections.observableArrayList());
        choiceBox.setTooltip(new Tooltip("Select player"));
        choiceBox.getStylesheets().add("/css/style.css");
        choiceBox.setStyle("-fx-text-alignment: right;");
        choiceBox.setPrefSize(250, 50);
        choiceBox.setLayoutX(getPositionX(imgPlayer) + 25);
        choiceBox.setLayoutY(getPositionX(imgPlayer) + 48.75);

        anchorPane.getChildren().add(choiceBox);
    }

    public ChoiceBox<Player> getChoiceBox() {
        return choiceBox;
    }

    //-- New nick field + button --
    private void addNickButton() {
        addButtonPlayer = new MyButton("addButton", 80, 34);
        addButtonPlayer.setLayoutX(getPositionX(imgPlayer) + 110);
        addButtonPlayer.setLayoutY(getPositionY(imgPlayer) + 273);
        anchorPane.getChildren().add(addButtonPlayer);
    }

    private void addNickTextField() {
        nickField = new TextField("Insert nick");
        nickField.setStyle("-fx-text-fill: #333333; -fx-font-size: 14;");
        nickField.setAlignment(Pos.CENTER);
        nickField.setPrefSize(150, 30);
        nickField.setBackground(new Background(new BackgroundImage(
                new Image(TXT_FIELD_IMAGE, 150, 30, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)
        ));
        nickField.setLayoutX(getPositionX(imgPlayer) + 105);
        nickField.setLayoutY(getPositionY(imgPlayer) + 202.5);
        anchorPane.getChildren().add(nickField);
        nickField.setOnMouseClicked(e -> {
            nickField.setStyle("-fx-text-fill: black; -fx-font-size: 14;");
            nickField.setText("");
        });
    }

    //-- for controller to handle --
    public String getEnteredNick() {
        String properNick = nickField.getText();
        if (properNick.length() > 9)
            return properNick.substring(0, 9);
        else
            return properNick;
    }

    public void clearNickTextField(boolean goodNick) {
        if (goodNick) {
            nickField.setStyle("-fx-text-fill: green; -fx-font-size: 14;");
            nickField.setText("Player added");
        } else {
            nickField.setStyle("-fx-text-fill: red; -fx-font-size: 14;");
            nickField.setText("Nick already taken!");
        }
    }

    public TextField getNickField() {
        return nickField;
    }

    public MyButton getAddButtonPlayer() {
        return addButtonPlayer;
    }

    //=== DIFFICULTY PANEL ===
    public int checkPickedDifficulty(int option) {
        int difficulty = 3;

        for (OptionPicker picker : difficultyList) {
            if (picker.isChoosen() && picker.getOption() == option) {
                difficulty = picker.getOption();
            } else
                picker.setCircleAsChoosen(false);
        }

        return difficulty;
    }

    private void addDifficultyOptions(int difficulty) {
        VBox vBox = new VBox();
        vBox.setSpacing(26);
        vBox.setLayoutX(getPositionX(imgDifficulty) + 10);
        vBox.setLayoutY(getPositionY(imgDifficulty) + 57);

        for (int i = 0; i < 3; i++) {
            if (i == 0)
                difficultyList.add(new OptionPicker(difficulty, true));
            else
                difficultyList.add(new OptionPicker(i + difficulty, false));
        }
        difficultyList.forEach(box -> vBox.getChildren().add(box));
        anchorPane.getChildren().add(vBox);
    }

    public LinkedList<OptionPicker> getDifficultyList() {
        return new LinkedList<>(difficultyList);
    }

    public void setDifficulty(int diff) {
        defaultDiff = diff;
    }

    private double getPositionX(ImageView img) {
        return img.getLayoutX();
    }

    private double getPositionY(ImageView img) {
        return img.getLayoutY();
    }

    //--- my image panels ---
    private void setImagesOnScene() {
        ImageView[] tmp = {imgPlayer, imgImages, imgDifficulty};
        double xPosition = 15;
        double yPosition = 15;

        for (int i = 0; i < 3; i++) {
            if (i == 2) {
                xPosition = 130;
                yPosition = 365;
            }
            tmp[i].setLayoutX(xPosition);
            tmp[i].setLayoutY(yPosition);
            if (i != 2) {
                xPosition += 330;
            }
        }
        anchorPane.getChildren().addAll(tmp);
    }

    private void getImgViews() {
        imgPlayer = getImages(PANEL_PLAYER);
        imgImages = getImages(PANEL_IMAGES);
        imgDifficulty = getImages(PANEL_DIFFICULTY);
    }

    private ImageView getImages(String name) {
        return new ImageView(getImg(name));
    }

    private Image getImg(String name) {
        try {
            return new Image(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
