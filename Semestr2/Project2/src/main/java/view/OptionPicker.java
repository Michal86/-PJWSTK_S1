package view;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class OptionPicker extends VBox {

    private final String NOT_CHOOSEN = "/empty_circle.png";
    private final String CHOOSEN = "/dot_circle.png";
    private ImageView   circleImg;
    private boolean     isChoosen;
    private int         option;

    //==========================================
    public OptionPicker(int option, boolean isChoosen) {
        String newImg = isChoosen ? CHOOSEN : NOT_CHOOSEN;
        circleImg = new ImageView(newImg);

        this.isChoosen = isChoosen;
        this.option = option;
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
        this.getChildren().add(circleImg);

        initPickListener();
    }
    //==========================================

    public void setCircleAsChoosen(boolean isChoosen) {
        this.isChoosen = isChoosen;
        String newImg = this.isChoosen ? CHOOSEN : NOT_CHOOSEN;
        circleImg.setImage(new Image(newImg));
    }

    public boolean isChoosen() {
        return isChoosen;
    }

    public void setChoosen(Boolean isChoosen) {
        this.isChoosen = isChoosen;
    }

    public int getOption() {
        return option;
    }

    private void initPickListener() {
        setOnMousePressed(event -> setCircleAsChoosen(isChoosen()));
    }
}
