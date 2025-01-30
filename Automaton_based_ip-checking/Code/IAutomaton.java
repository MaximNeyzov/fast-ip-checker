public interface IAutomaton {
    void next(char sym);
    void reset();
    boolean accept();
}
