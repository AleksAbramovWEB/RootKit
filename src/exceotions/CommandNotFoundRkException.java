package exceotions;

public class CommandNotFoundRkException extends AbstractRkException {

    public CommandNotFoundRkException(String command) {
        super("Command: " + command + " not found");
    }
}
