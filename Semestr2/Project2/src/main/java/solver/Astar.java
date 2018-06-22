package solver;
/*
 * A* algorithm - with heuristic (amount of pieces out of place vs goal) [Also implemented the Manhattan distance to my Node]
 * Sometimes it finds correct path in milli seconds but other time it creates few million nodes, and it still can't find solution + "JavaFX Application Thread" java.lang.OutOfMemoryError: GC overhead limit exceeded
 * It's not working as it's supposed to so I did put bound of searching until 1M Nodes
 * TO_DO: optimise and search for possible mistakes in PuzzleState class.
 */
import java.util.*;

public class Astar {

    private static List<Moves> solutionMoves;

    public static boolean search(int[][] board){
        long searchCount = 1;
        boolean found = false;
        Node root = new Node(new PuzzleState(board, Moves.ROOT));
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);

        while (!queue.isEmpty() && !found && searchCount<1000000){
            Node tmpNode = queue.poll();

            if (!Objects.requireNonNull(tmpNode).getCurState().isGoal()){
                LinkedList<State> tmpSuccessors = tmpNode.getCurState().genSuccessors(tmpNode.getMove());
                LinkedList<Node> nodeSuccessors = new LinkedList<>();   //only non-repeated nodes

                //- create tmp node with parent and values -
                for (int i = 0; i < tmpSuccessors.size(); i++) {
                    Node nodeToCheck = new Node(tmpNode, tmpSuccessors.get(i),
                            tmpSuccessors.get(i).findCost(),
                            ((PuzzleState)tmpSuccessors.get(i)).getOutOfPlace(),
                            ((PuzzleState)tmpSuccessors.get(i)).getMove()
                    );

                    if (!checkRepeats(nodeToCheck))
                        nodeSuccessors.add(nodeToCheck);
                }

                if (nodeSuccessors.size()==0)
                    continue;

                addLowestFCost(nodeSuccessors, queue);
                searchCount++;
            }
            //- Solution is found -
            else if(tmpNode.getCurState().isGoal() ){
                Stack<Node> solutionPath = new Stack<>();
                solutionPath.push(tmpNode);
                tmpNode = tmpNode.getParent();
                found = true;

                while (tmpNode.getParent() != null){
                    solutionPath.push(tmpNode);
                    tmpNode = tmpNode.getParent();
                }
                solutionPath.push(tmpNode);

                // The size of the stack before looping through and emptying it.
                int loopSize = solutionPath.size();
                solutionMoves = new LinkedList<>();

                for (int i = 0; i < loopSize; i++){
                    tmpNode = solutionPath.pop();
                    if (tmpNode.getMove()!=Moves.ROOT)
                        solutionMoves.add(revMoves(tmpNode.getMove()));
                }

                return found;
            }

        }
        //System.out.println("Error! NO SOLUTION HAS BEEN FOUND!");
        return found;
    }

    public static List<Moves> getSolutionMoves(){
        return new LinkedList<>(solutionMoves);
    }

    //--- Reverse Moves ---
    private static Moves revMoves(Moves move){
        if (move==Moves.LEFT)
           return Moves.RIGHT;
        else if (move==Moves.RIGHT)
            return Moves.LEFT;
        else if (move==Moves.UP)
            return Moves.DOWN;
        else if (move==Moves.DOWN)
            return Moves.UP;
        else
            return Moves.ROOT;
    }

    //--- Add nodes to my queue also nodes with same F(n) cost ---
    private static void addLowestFCost(LinkedList<Node> nodeSuccessors, Queue<Node> queue){
        nodeSuccessors.sort(Comparator.comparingInt(Node::getFCost));
        int val = nodeSuccessors.get(0).getFCost();

        nodeSuccessors.forEach( node -> {
            if (node.getFCost()==val)
                queue.add(node);
        });

    }

    private static boolean checkRepeats(Node node){
        boolean retValue = false;
        Node checkNode = node;

        while (node.getParent() != null && !retValue){
            if (node.getParent().getCurState().equals(checkNode.getCurState())) {
                retValue = true;
            }
            node = node.getParent();
        }

        return retValue;
    }
}
