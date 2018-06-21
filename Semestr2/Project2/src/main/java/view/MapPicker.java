package view;

import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapPicker extends ListView {

    private String                  mapNames[] = {"compass", "panDa", "numbers", "crazy"};
    private ImageView               pickedMap;
    private Map<String, ImageView>  mapList;

    //==========================================
    protected MapPicker() {
        this.setPrefSize(250, 285);
        this.getStylesheets().add("css/style.css");
        mapList = new HashMap<>();
        setDefaultMaps();
    }
    //==========================================

    //--- Pick a map ---
    public String getMapName(int index) {
        return mapNames[index];
    }

    public String getPickedMapName(ImageView pickedMap) {
        for (String map : mapList.keySet()) {
            if (pickedMap.equals(mapList.get(map)))
                return map;
        }
        return "";
    }

    //--- handle maps ---
    public void addMapToListView() {
        for (int i = 0; i < mapNames.length; i++) {
            this.getItems().add(mapList.get(mapNames[i]));
        }
    }

    protected void addNewMap(String name) {
        Image img = new Image("/" + name + ".png");
        ImageView newMap = new ImageView(img);
        //--- set size ---
        newMap.setFitWidth(220);
        newMap.setPreserveRatio(true);
        newMap.setSmooth(true);
        newMap.setCache(true);
        //---
        mapList.put(name, newMap);
    }

    public void setPickedMap(ImageView pickedMap) {
        this.pickedMap = pickedMap;
    }

    public ImageView getPickedMap() {
        return pickedMap;
    }

    private void setDefaultMaps() {
        for (int i = 0; i < mapNames.length; i++) {
            addNewMap(mapNames[i]);
            if (i == 0)
                setPickedMap(getMapImg(mapNames[i]));
        }
    }

    private ImageView getMapImg(String name) {
        return mapList.get(name);
    }

    public List<ImageView> getMapsImgList() {
        return new ArrayList<>(mapList.values());
    }

}
