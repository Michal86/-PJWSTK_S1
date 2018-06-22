package view;

import javafx.scene.image.Image;
import solver.Moves;

import java.util.HashMap;
import java.util.Map;

public class MovesImg {

    private Map<Moves, Image> moveImageList;

    public MovesImg() {
        moveImageList = new HashMap<>();
        initMoves();
    }

    //--- Init available moves ---
    private void initMoves(){
        for (Moves move: Moves.values()) {
            moveImageList.put(move, getMoveImage(move.toString()));
        }
    }

    //-- setters & getters ---
    private Image getMoveImage(String name){
       return new Image("/" + name + ".png");
    }

    public  Map<Moves, Image> getMoveImageList() {
        return moveImageList;
    }

    public Image getMoveImage(Moves move){
        return moveImageList.get(move);
    }
}
