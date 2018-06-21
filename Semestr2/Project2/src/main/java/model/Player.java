package model;

public class Player {

    private String  nick;
    private String  map;
    private int     moves;
    private int     difficulty;
    private int     position;

    //==========================================
    public Player(){}

    public Player(String nick) {
        this.nick = nick;
        map = "";
        moves = 0;
        difficulty = 0;
    }

    protected Player(String nick, String map, int moves, int difficulty) {
        this(nick);
        this.map = map;
        this.moves = moves;
        this.difficulty = difficulty;
    }
    //==========================================

    //--- getters & setters ---
    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public int getMoves() {
        return moves;
    }

    public void resetMoves() {
        this.moves = 0;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public void addMoves() {
        if (moves < 9999)
            this.moves++;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return this.getNick();
    }
}
