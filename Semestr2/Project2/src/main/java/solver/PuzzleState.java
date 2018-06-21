package solver;
import model.Point;
import java.util.LinkedList;

public class PuzzleState implements State{

    private final int     PUZZLE_SIZE;
    private int           outOfPlace = 0;
    private int           manDist = 0;
    private Moves         move;
    //---
    private final int[][] GOAL;
    private int[][]       curBoard;

    //==========================================
    public PuzzleState(int[][] board, Moves move){
        curBoard = board;
        this.move = move;
        PUZZLE_SIZE = getBoardSize(curBoard);
        GOAL = new int[PUZZLE_SIZE][PUZZLE_SIZE];
        //- - -
        setGoal();
        setOutOfPlace();
        setManDist();
    }
    //==========================================

    //--- Set ArrayList of the successors for that state ---
    @Override
    public LinkedList<State> genSuccessors(Moves move) {
        LinkedList<State> successors = new LinkedList<>();
        Point blank = getBlank();
        int x = blank.getX();
        int y = blank.getY();

        //- Check if next move is available & not opposite to the one of the parent ---
        if ( isAvailable(x - 1, y) && move!=Moves.DOWN) {
            //System.out.println("Parent: "+move +"!= Moves.DOWN");
            swapAndStore(new Point(x - 1, y), blank, successors, Moves.UP);
        }

        if ( isAvailable(x + 1, y) && move!=Moves.UP ) {
            swapAndStore(new Point(x + 1, y), blank, successors, Moves.DOWN);
        }
        if ( isAvailable(x, y - 1) && move!=Moves.RIGHT ) {
            swapAndStore(new Point(x, y - 1), blank, successors, Moves.LEFT);
        }

        if ( isAvailable(x, y + 1) && move!=Moves.LEFT ) {
            swapAndStore(new Point(x, y + 1), blank, successors, Moves.RIGHT);
        }

        return successors;
    }

    //--- Swap board[][] numbers ---
    private void swapAndStore(Point blank, Point piece, LinkedList<State> successors, Moves move){
        int[][] newBoard = copyBoard(curBoard);

        int tmp = newBoard[blank.getX()][blank.getY()];
        newBoard[blank.getX()][blank.getY()] = curBoard[piece.getX()][piece.getY()];
        newBoard[piece.getX()][piece.getY()] = tmp;
        successors.add(new PuzzleState(newBoard, move));
    }

    //--- Check if move is within puzzle board bounds ---
    private boolean isAvailable(int i, int j) {
        return (i >= 0 && j >= 0 && i < PUZZLE_SIZE && j < PUZZLE_SIZE);
    }

    public Moves getMove(){
        return move;
    }

    //--- Creates a copy of current board ---
    private int[][] copyBoard(int[][] boardToCopy) {
        int[][] copied = new int[PUZZLE_SIZE][PUZZLE_SIZE];

        for (int i = 0; i < boardToCopy.length; i++){
            for (int j = 0; j < boardToCopy[i].length; j++) {
                copied[i][j] = boardToCopy[i][j];
            }
        }
        return copied;
    }

    //--- Compare current board vs State delivered---
    @Override
    public boolean equals(State toCompare) {
        boolean equal = true;
        int[][] check = ((PuzzleState) toCompare).getCurBoard();

        for (int i = 0; i < curBoard.length; i++) {
            for (int j = 0; j < curBoard[i].length; j++) {
                if (curBoard[i][j] != check[i][j])
                    equal = false;
            }
        }
        return equal;
    }

    private int[][] getCurBoard() {
        return curBoard;
    }

    //--- FOR NOW: I'm not using Manhattan Dist. ---
    public int getManDist() {
        return manDist;
    }

    //--- Set The Manhattan distance ---
    /*
    function heuristic(node) =
        dx = abs(node.x - goal.x)
        dy = abs(node.y - goal.y)
        return D * (dx + dy)
     */
    private void setManDist(){
        for (int x = 0; x < PUZZLE_SIZE; x++) {
            for (int y = 0; y < PUZZLE_SIZE; y++) {

                int val = (curBoard[x][y]);

                if (val != 0) {
                    int valX = val % PUZZLE_SIZE;
                    int valY = val / PUZZLE_SIZE;

                    manDist += Math.abs(valX - x) + Math.abs(valY - y);
                }

            }
        }
    }

    //--- Compare current state vs goal ---
    @Override
    public boolean isGoal() {
        boolean equal = true;
        for (int i = 0; i < curBoard.length; i++) {
            for (int j = 0; j < curBoard[i].length; j++) {
                if (curBoard[i][j] != GOAL[i][j])
                    equal = false;
            }
        }
        return equal;
    }

    //--- Cost to get here ---
    @Override
    public int findCost() {
        return 1;
    }

    //--- Counts misplaced pieces ---
    private void setOutOfPlace(){
        for (int i = 0; i < curBoard.length; i++)
            for (int j = 0; j < curBoard.length; j++)
                if (curBoard[i][j] != GOAL[i][j]) outOfPlace++;
    }

    public int getOutOfPlace() {
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

    //--- Prints state ---
    @Override
    public void printState() {
        for (int i = 0; i < PUZZLE_SIZE; i++) {
            for (int j = 0; j < PUZZLE_SIZE; j++) {
                System.out.print(curBoard[i][j] + " | ");
            }
            System.out.println();
        }
    }

    //--- '0' represents blank piece ---
    private void setGoal(){
        int index = 0;
        for (int i = 0; i < PUZZLE_SIZE; i++)
            for (int j = 0; j < PUZZLE_SIZE; j++)
                GOAL[i][j] = index++;
    }

    //--- Get current board size (always square)---
    private int getBoardSize(int[][] curBoard) {
        return curBoard.length;
    }
}
