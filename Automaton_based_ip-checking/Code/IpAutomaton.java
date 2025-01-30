public class IpAutomaton implements IAutomaton {

    private enum State {
        start,      // Initial automaton state.
        reject,     // Reject automaton state.
        checkPart1, // Accepted state.
        checkPart2, // Accepted state.
        checkPart3, // Accepted state.
        checkPart4  // Accepted state.
    }
    private State state = State.start; // Current automaton state.
    private byte symNumber = 1; // Expected symbol number.

    // Automaton-recogniser of range from 0 to 255:
    private final RangeAutomaton rangeRec = new RangeAutomaton();

    /** Feed the next symbol to the automaton-recogniser input. */
    public void next(char sym) {
        boolean continueRangeChecking = (symNumber == 1) ||
                (symNumber <= 3) && (sym != '.') && rangeRec.accept();
        state = switch (state) {
            case State.start -> {
                checkRange(sym);
                yield State.checkPart1;
            }

            case State.checkPart1,
                 State.checkPart2,
                 State.checkPart3 -> {
                boolean correctDot = (sym == '.') && rangeRec.accept();
                if (continueRangeChecking) {
                    checkRange(sym);
                    yield state;
                } else if (correctDot) {
                    resetRangeChecking();
                    yield nextState();
                } else {
                    yield State.reject;
                }
            }

            case State.checkPart4 -> {
                if (continueRangeChecking) {
                    checkRange(sym);
                    yield state;
                } else {
                    yield State.reject;
                }
            }

            // Remaining state: reject
            default -> { yield State.reject; }
        };
    }

    private void resetRangeChecking() {
        rangeRec.reset();
        symNumber = 1;
    }

    private void checkRange(char sym) {
        rangeRec.next(sym);
        symNumber++;
    }

    private State nextState() {
        switch (state) {
            case State.start ->      { return State.checkPart1; }
            case State.checkPart1 -> { return State.checkPart2; }
            case State.checkPart2 -> { return State.checkPart3; }
            case State.checkPart3 -> { return State.checkPart4; }
            /* Remaining states:
             * State.checkPart4,
             * State.reject */
            default -> { return State.reject; }
        }
    }

    // Automaton-recognizer accepts an input sequence of characters.
    public boolean accept() {
        return state != State.start && state != State.reject &&
                (rangeRec.accept() || symNumber == 1);
    }

    public void reset() {
        state = State.start;
        resetRangeChecking();
    }

    // For testing.
    public String getState() {
        return state.toString();
    }
}
