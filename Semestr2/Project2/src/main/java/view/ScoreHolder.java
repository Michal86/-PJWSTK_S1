package view;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ScoreHolder extends VBox {

    private final String BG_IMAGE;
    private ImageView    imgBackground;

    protected ScoreHolder(String name) {
        BG_IMAGE = "/" + name + ".png";
        imgBackground = new ImageView();
        imgBackground.setImage(new Image(BG_IMAGE));

        this.setAlignment(Pos.CENTER);
        this.getChildren().add(imgBackground);
    }
    //==========================================
    //--- added for mini info panel ---
    protected ScoreHolder(String name, double sizeX, double sizeY,  double positionX, double positionY) {
        this(name);
        this.setPrefSize(sizeX, sizeY);
        this.setLayoutX(positionX);
        this.setLayoutY(positionY);
        this.setAlignment(Pos.CENTER);
    }

}
