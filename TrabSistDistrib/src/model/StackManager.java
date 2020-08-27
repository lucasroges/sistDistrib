package model;

import java.util.Stack;

/**
 * Implementation of a stack with data.
 *
 * @author João Víctor Bolsson Marques (jvmarques@inf.ufsm.br)
 * @version 2020, Aug 26.
 */
public class StackManager {

    private static final byte MAX = 15;
    private final Stack<Short> stack = new Stack<>();

    /**
     * Pushes a value into the stack.
     *
     * @param value Value to be added.
     */
    public synchronized void push(short value) {
        if (stack.size() < MAX) {
            stack.push(value);
        }
    }

    /**
     * Removes a value from the stack.
     *
     * @return Removed value.
     */
    public synchronized short pop() {
        if (!stack.isEmpty()) {
            return stack.pop();
        }
        return 0;
    }

    /**
     * Prints the stack.
     */
    public void print_q() {
        System.out.println("---Stack elements----");

        for (Short s : stack) {
            System.out.print(s + " | ");
        }
        System.out.println("\n-------------");
    }

}
