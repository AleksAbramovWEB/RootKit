package commands;

import java.io.IOException;
import java.io.PrintWriter;

public class SystemInfoCommand extends AbstractCommand {
    @Override
    public void run() {
        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            System.getProperties().list(printWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
