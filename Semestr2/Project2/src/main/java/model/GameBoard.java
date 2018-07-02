package model;


import solver.Moves;

public class GameBoard {

    private final int     PUZZLE_SIZE;              // row size, col size
    private int           outOfPlace = 0;           // Difference between GOAL and current state
    private final int[][] GOAL;                     // GOAL[][] board
    private int[][]       curBoard;                 // Holds positions of Image pieces
    private Moves[]       boardMove =               // Available moves
            {Moves.RIGHT, Moves.LEFT, Moves.UP, Moves.DOWN};
    private Moves         lastMove  = Moves.ROOT;   // Keep track of moves

    //==========================================
    public GameBoard(int size){
        PUZZLE_SIZE = size;
        GOAL = new int[PUZZLE_SIZE][PUZZLE_SIZE];
        curBoard = new int[PUZZLE_SIZE][PUZZLE_SIZE];
        setBoard(PUZZLE_SIZE);
    }
    //==========================================

    //--- Check if move is within my board bounds ---
    private boolean isAvailable(int i, int j) {
        return (i >= 0 && j >= 0 && i < PUZZLE_SIZE && j < PUZZLE_SIZE);
    }

    //--- Check available moves [I'm moving blank piece] ---
    protected Point getPointToShuffle(){
        Point blank = getBlank();
        Point[] tmp = new Point[4];
        int size = 0;

        if (blank!=null) {
            int x = blank.getX();
            int y = blank.getY();

            if (isAvailable(x, y + 1) && lastMove != Moves.LEFT){   //right
                tmp[0] = new Point(x,y+1);
                size++;
            }

            if (isAvailable(x, y - 1) && lastMove != Moves.RIGHT){  //left
                tmp[1] =  new Point(x,y-1);
                size++;
            }

            if (isAvailable(x - 1, y) && lastMove != Moves.DOWN){   //up
                tmp[2] =new Point(x-1, y);
                size++;
            }

            if (isAvailable(x + 1, y) && lastMove != Moves.UP){     //down
                tmp[3] = new Point(x+1, y);
                size++;
            }

            Point[] movablePoints = new Point[size];
            Moves[] getMove = new Moves[size];
            size=0;
            for (int i = 0; i < tmp.length ; i++) {
                if (tmp[i]!=null) {
                    getMove[size] = boardMove[i];
                    movablePoints[size++] = tmp[i];
                }
            }
            return pickRandomPointFromAvailable(movablePoints, getMove);
        }
        return null;
    }

    //--- Set last move ---
    public void setLastMove(Moves lastMove) {
        this.lastMove = lastMove;
    }

    public Moves getLastMove() {
        return lastMove;
    }

    //--- Pick point from available Points[] ---
    private Point pickRandomPointFromAvailable(Point[] toPick, Moves[] moves){
        int rand = (int) (Math.random() * toPick.length);
        Point pickedPoint = toPick[rand];

        setLastMove(moves[rand]);
        return pickedPoint;
    }

    //--- Set current state of the board ---
    protected void setCurBoard(int[][] state) {
        for (int i = 0; i < state.length; i++)
            for (int j = 0; j < state[i].length; j++)
                curBoard[i][j] = state[i][j];

        setOutOfPlace();
        System.out.println("OOP: "+ getOutOfPlace());
    }

    public int[][] getCurBoard() {
        return curBoard;
    }

    //--- Compare current state vs goal ---
    protected boolean isGoal() {
        boolean equal = true;
        for (int i = 0; i < curBoard.length; i++) {
            for (int j = 0; j < curBoard[i].length; j++) {
                if (curBoard[i][j] != GOAL[i][j])
                    equal = false;
            }
        }
        return equal;
    }

    //--- Counts misplaced pieces ---
    private void setOutOfPlace(){
        outOfPlace = 0;
        for (int i = 0; i < curBoard.length; i++)
            for (int j = 0; j < curBoard[i].length; j++)
                if (curBoard[i][j] != GOAL[i][j]) outOfPlace++;
    }

    protected int getOutOfPlace() {
        return outOfPlace;
    }

    //--- Get blank piece ---
    private Point getBlank(){
        Point blank = null;
        for (int i = 0; i < PUZZLE_SIZE; i++)
            for (int j = 0; j < PUZZLE_SIZE; j++) {
                if (curBoard[i][j] == 0)
                    blank = new Point(i, j);
            }
        return blank;
    }

    private void setBoard(int size) {
        int index = 0;

        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                GOAL[i][j] = index;
                curBoard[i][j] = index++;
            }
    }
}
