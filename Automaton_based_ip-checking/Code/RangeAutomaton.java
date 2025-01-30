public class RangeAutomaton implements IAutomaton {

    private enum State {
        start,      // Initial automaton state.
        reject,     // Reject automaton state.
        $0,         // Accepted state.
        $1,         // Accepted state.
        $1$0_9,     // Accepted state.
        $1$0_9$0_9, // Accepted state.
        $2,         // Accepted state.
        $2$0_4,     // Accepted state.
        $2$0_4$0_9, // Accepted state.
        $25,        // Accepted state.
        $25$0_5,    // Accepted state.
        $2$6_9,     // Accepted state.
        $3_9,       // Accepted state.
        $3_9$0_9    // Accepted state.
    }
    private State state = State.start; // Current automaton state.

    /** Feed the next symbol to the automaton-recogniser input. */
    public void next(char sym) {
        // Transitions valid for all states:
        if (!Character.isDigit(sym)) {
            state = State.reject;
            return;
        }

        // Transitions valid for specific states:
        state = switch (state) {
            case State.start -> {
                if (sym == '0') { yield State.$0; }
                if (sym == '1') { yield State.$1; }
                if (sym == '2') { yield State.$2; }
                yield State.$3_9;
            }

            case State.$1 -> { yield State.$1$0_9; }

            case State.$1$0_9 -> { yield State.$1$0_9$0_9; }

            case $2 -> {
                if (sym >= '0' && sym <= '4') { yield State.$2$0_4; }
                if (sym == '5') { yield State.$25; }
                yield State.$2$6_9;
            }

            case State.$2$0_4 -> { yield State.$2$0_4$0_9; }

            case State.$25 -> {
                if (sym >= '0' && sym <= '5') { yield State.$25$0_5; }
                yield State.reject;
            }

            case State.$3_9 -> { yield State.$3_9$0_9; }

            /* Remaining states:
             * State.$0,
             * State.$1$0_9$0_9,
             * State.$2$0_4$0_9,
             * State.$25$0_5,
             * State.$2$6_9,
             * State.$3_9$0_9,
             * State.reject */
            default -> { yield State.reject; }
        };
    }

    // Automaton-recognizer accepts an input sequence of characters.
    public boolean accept() {
        return state != State.start &&
               state != State.reject;
    }

    public void reset() {
        state = State.start;
    }

    // For testing.
    public String getState() {
        switch (state) {
            case State.start ->      { return "start"; }
            case State.$0 ->         { return "0"; }
            case State.$1 ->         { return "1"; }
            case State.$2 ->         { return "2"; }
            case State.$3_9 ->       { return "[3-9]"; }
            case State.$1$0_9 ->     { return "1[0-9]"; }
            case State.$1$0_9$0_9 -> { return "1[0-9][0-9]"; }
            case State.$2$0_4 ->     { return "2[0-4]"; }
            case State.$2$0_4$0_9 -> { return "2[0-4][0-9]"; }
            case State.$25 ->        { return "25"; }
            case State.$25$0_5 ->    { return "25[0-5]"; }
            case State.$2$6_9 ->     { return "2[6-9]"; }
            case State.$3_9$0_9 ->   { return "[3-9][0-9]"; }
            case State.reject ->     { return "reject"; }
            default ->               { return "Illegal state detected!"; }
        }
    }
}
