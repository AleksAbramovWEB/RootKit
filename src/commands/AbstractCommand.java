package commands;

import exceotions.CommandNotFoundRkException;

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCommand implements Runnable {

    protected static final String COMMAND_SHELL = "rk-shell";
    protected static final String COMMAND_PING = "rk-ping";

    protected Socket socket;

    private String Tag;

    protected AbstractCommand() {}

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    private static final Map<String, AbstractCommand> factoryMap = new HashMap<>(){
        {
            put(COMMAND_SHELL, new ShellCommand());
            put(COMMAND_PING, new PingCommand());
        }
    };

    public static AbstractCommand getCommand(String command) {
        if (!factoryMap.containsKey(command)) {
            throw new CommandNotFoundRkException(command);
        }

        AbstractCommand abstractCommand = factoryMap.get(command);
        abstractCommand.setTag(command);

        return abstractCommand;
    }

    protected void setTag(String tag) {
        Tag = tag;
    }

    protected String getTag() {
        return Tag + "> ";
    }
}
