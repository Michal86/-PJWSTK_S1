package view;

import javafx.scene.image.ImageView;

public class MyImgView {

    private ImageView img;
    private int       position;

    public MyImgView(ImageView img, int position) {
        this.img = img;
        this.position = position;
    }

    //-- setters & getters ---
    public void setImg(ImageView img) {
        this.img = img;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public ImageView getImg() {
        return img;
    }


}
