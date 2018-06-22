package view;

import javafx.scene.image.ImageView;
import solver.Moves;

public class MovesImg {
    private ImageView   img;
    private Moves       move;

    public MovesImg(ImageView img, Moves move) {
        this.img  = img;
        this.move = move;
    }

    //-- setters & getters ---
    public Moves getMove() {
        return move;
    }

    public void setMove(Moves move) {
        this.move = move;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public ImageView getImg() {
        return img;
    }
}
