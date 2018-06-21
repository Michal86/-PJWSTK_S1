package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ModelManager {

    private Hitlist         hitlist;
    private List<Player>    scores3x3;
    private List<Player>    scores4x4;
    private List<Player>    scores5x5;
    //---
    private int             mapDifficulty;
    private String          pickedMap;
    private int[][]         board;
    private List<Player>    playerList;
    private ObservableList<Player> obsList;
    private Player          pickedPlayer;
    private boolean         isTaken;

    //==========================================
    public ModelManager() {
        //- set default values -
        mapDifficulty = 3;
        setBoard(mapDifficulty);
        pickedMap = "compass";
        //--- prepare list ---
        pickedPlayer = new Player("Anonymous", pickedMap, 0, mapDifficulty);
        isTaken = false;
        playerList = new LinkedList<>();
        playerList.add(pickedPlayer);
        obsList = FXCollections.observableArrayList(playerList);
        //--- Init hitlist ---
        hitlist = initHitlist();

    }
    //==========================================

    //--- player ---
    public void addNewPlayer(String nick) {
        Player newPlayer = new Player(nick);
        playerList.add(newPlayer);
        obsList.add(newPlayer);
    }

    public ObservableList<Player> getObsList() {
        return obsList;
    }

    public void setPickedPlayer(Player selectedPlayer) {
        pickedPlayer = selectedPlayer;
        pickedPlayer.setMap(getPickedMap());
        pickedPlayer.setDifficulty(getMapDifficulty());
    }

    public void UpdatePlayerBeforePlay() {
        pickedPlayer.setMap(pickedMap);
        pickedPlayer.setDifficulty(getMapDifficulty());
        pickedPlayer.resetMoves();
    }

    public Player getPickedPlayer() {
        return pickedPlayer;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    //--- check if player exsists ---
    public void setTaken(boolean status) {
        isTaken = status;
    }

    public boolean isTaken() {
        return isTaken;
    }

    public void checkNicks(String newNick) {
        isTaken = false;
        playerList.forEach(player -> {
            if (player.getNick().equals(newNick))
                setTaken(true);
        });
    }

    //- board -
    public void setBoard(int size) {
        board = new int[size][size];
        int index = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.board[i][j] = index++;
            }
        }
    }

    public int[][] getBoard() {
        return board;
    }

    //- difficulty -
    public void setMapDifficulty(int difficulty) {
        mapDifficulty = difficulty;
        pickedPlayer.setDifficulty(difficulty);
    }

    public int getMapDifficulty() {
        return mapDifficulty;
    }

    //- map name -
    public String getPickedMap() {
        return pickedMap;
    }

    public void setPickedMap(String name) {
        pickedMap = name;
    }

    public String getPlayerMapName() {
        return pickedPlayer.getMap();
    }

    //--- check player score against records ---
    public boolean passed(Player toCheck) {
        boolean addScore = false;
        int index = 0;
        List<Player> checkList = getRecordFromTable(toCheck.getDifficulty());
        int size = checkList.size();

        if (size >= 5 ) {
            for (Player current : checkList) {
                if ((toCheck.getMoves() < current.getMoves()) && index < 5 && !addScore)
                    addScore = true;
                else
                    index++;
            }
        } else addScore = true;

        if (addScore && index <= 5) {
            if (size >= 5) {
                updateNewData(checkList.get(size - 1), toCheck);
            } else {
                Player newWinner = new Player(toCheck.getNick());
                checkList.add(newWinner);
                updateNewData(newWinner, toCheck);
            }
        }
        checkList.sort((p1, p2) -> p1.getMoves() - p2.getMoves());
        checkList.forEach(p -> p.setPosition(checkList.indexOf(p) + 1));

        return addScore;
    }

    private void updateNewData(Player from, Player to) {
        from.setPosition(5);
        from.setDifficulty(to.getDifficulty());
        from.setNick(to.getNick());
        from.setMoves(to.getMoves());
        from.setMap(to.getMap());
    }

    public List<Player> getRecordFromTable(int difficulty) {
        if (difficulty == 3)
            return scores3x3;
        else if (difficulty == 4)
            return scores4x4;
        else if (difficulty == 5)
            return scores5x5;
        else
            return null;
    }

    //--- serialization ---
    public void save(){
        hitlist.flushBeforeSave();
        hitlist.setHitlist("scores3x3", scores3x3);
        hitlist.setHitlist("scores4x4", scores4x4);
        hitlist.setHitlist("scores5x5", scores5x5);
        System.out.println("--- SAVED ---");
        String checkSaves = FilesManager.saveJson(hitlist.getHitlist());
        System.out.println(checkSaves);
        System.out.println("----------------------------");
    }
    private Hitlist initHitlist(){
        //Get my Map obj from JSON
        Hitlist hl = FilesManager.getMapFromJSON();

        scores3x3 = new ArrayList<>(hl.getSpecifiedList("scores3x3"));
        scores4x4 = new ArrayList<>(hl.getSpecifiedList("scores4x4"));
        scores5x5 = new ArrayList<>(hl.getSpecifiedList("scores5x5"));

        return hl;
    }
}
