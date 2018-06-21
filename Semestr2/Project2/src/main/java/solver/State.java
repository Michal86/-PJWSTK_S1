package solver;

import java.util.LinkedList;

public interface State
{
    // determine if current state is goal
    boolean isGoal();

    // generate successors to the current state
    LinkedList<State> genSuccessors(Moves move);

    // determine cost from initial state to THIS state
    int findCost();

    // print the current state
    void printState();

    // compare the actual state data
    boolean equals(State s);
}
