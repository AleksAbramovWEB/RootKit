package exceotions;

abstract public class AbstractRkException extends RuntimeException {

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    public AbstractRkException(String message) {
        super(ANSI_RED + message + ANSI_RESET);
    }
}