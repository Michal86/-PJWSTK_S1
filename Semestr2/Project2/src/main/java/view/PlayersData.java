package view;

import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class PlayersData extends HBox {

    private HBox nickBox, moveBox, mapBox;
    private Text nickTxt, movesTxt, mapTxt;

    //==========================================
    protected PlayersData() {
        this.setSpacing(20);
        addBoxes();
    }
    //==========================================

    //--- for updates from model ---
    public void updatePlayerData(int position, String name, String moves, String map) {
        nickTxt.setText(position + " " + name);
        movesTxt.setText(moves);
        mapTxt.setText(map);
    }

    //--- set default data ---
    protected void addBoxesWithData(String name, String moves, String map) {
        nickBox.getChildren().add(nickTxt = txtHolder(name));
        moveBox.getChildren().add(movesTxt = txtHolder(moves));
        mapBox.getChildren().add(mapTxt = txtHolder(map));
    }

    //--- ---
    private void addBoxes() {
        setDataBoxStyle(nickBox = new HBox());
        setDataBoxStyle(moveBox = new HBox());
        setDataBoxStyle(mapBox = new HBox());
        nickBox.setAlignment(Pos.BASELINE_LEFT);
        this.getChildren().addAll(nickBox, moveBox, mapBox);
    }

    //--- set style ---
    private Text txtHolder(String str) {
        Text score = new Text(str);
        score.setStyle("-fx-fill: #3e423a; -fx-font-weight: bolder; -fx-font-size: 20px;");
        return score;
    }

    private Text txtHolder(int str) {
        Text score = new Text(String.valueOf(str));
        score.setStyle("-fx-fill: #3e423a; -fx-font-weight: bolder; -fx-font-size: 20px;");
        return score;
    }

    private void setDataBoxStyle(HBox box) {
        box.setAlignment(Pos.CENTER);
        box.setPrefSize(150, 20);
    }

}
