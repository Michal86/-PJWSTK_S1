package solver;

public class Node implements Comparable<Node>{

    private Node    parent;    // link to the parent node
    private State   curState;  // current state of node
    private int     cost;      // cost to get to this state
    private int     hCost;     // heuristic cost
    private int     fCost;     // f(n) cost
    private Moves   move;      // move made to get this state

    //==========================================
    public Node(State state){
        curState = state;
        parent = null;
        cost = 0;
        hCost = 0;
        fCost = 0;
        move = Moves.ROOT;
    }

    public Node(Node prev, State state, int cost, int hCost, Moves move){
        parent = prev;
        curState = state;
        this.cost = cost;
        this.hCost = hCost;
        fCost = cost + hCost;
        this.move = move;
    }
    //==========================================
    //--- Setters ---
    public void setCurState(State curState) {
        this.curState = curState;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setHCost(int hCost) {
        this.hCost = hCost;
    }

    public void setFCost(int fCost) {
        this.fCost = fCost;
    }

    //Set the Parent node
    public void setParent(Node parent) {
        this.parent = parent;
    }

    //--- Getters ---
    public Moves getMove() {
        return move;
    }

    public State getCurState() {
        return curState;
    }

    public int getCost() {
        return cost;
    }

    public int getHCost() {
        return hCost;
    }

    public int getFCost() {
        return fCost;
    }

    public Node getParent() {
        return parent;
    }

    @Override
    public int compareTo(Node toCompare) {
        return this.getFCost()-toCompare.getFCost();
    }
}
