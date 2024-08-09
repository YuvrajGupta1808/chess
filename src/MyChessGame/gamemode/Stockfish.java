package MyChessGame.gamemode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Stockfish {
    private Process engineProcess;
    private BufferedReader processReader;
    private OutputStreamWriter processWriter;

    public boolean startEngine(String path) {
        try {
            engineProcess = Runtime.getRuntime().exec(path);
            processReader = new BufferedReader(new InputStreamReader(engineProcess.getInputStream()));
            processWriter = new OutputStreamWriter(engineProcess.getOutputStream());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sendCommand(String command) {
        try {
            processWriter.write(command + "\n");
            processWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getOutput(int waitTime) {
        StringBuilder buffer = new StringBuilder();
        try {
            Thread.sleep(waitTime);
            while (processReader.ready()) {
                String line = processReader.readLine();
                buffer.append(line).append("\n");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    public String getBestMove(String fen, int waitTime) {
        sendCommand("position fen " + fen);
        sendCommand("go movetime " + waitTime);
        String output = getOutput(waitTime + 20);
        return parseBestMove(output);
    }

    private String parseBestMove(String output) {
        for (String line : output.split("\n")) {
            if (line.startsWith("bestmove")) {
                return line.split(" ")[1];
            }
        }
        return null;
    }

    public void stopEngine() {
        sendCommand("quit");
        try {
            processReader.close();
            processWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Double getEvaluation(String fen, int waitTime) {
        sendCommand("position fen " + fen);
        sendCommand("go movetime " + waitTime);
        String output = getOutput(waitTime);
        for (String line : output.split("\n")) {
            if (line.startsWith("info depth")) {
                String[] parts = line.split(" ");
                for (int i = 0; i < parts.length; i++) {
                    if (parts[i].equals("score")) {
                        if (parts[i + 1].equals("cp")) {
                            return Double.parseDouble(parts[i + 2]) / 100.0;
                        } else if (parts[i + 1].equals("mate")) {
                            return parts[i + 2].startsWith("-") ? -1000.0 : 1000.0;
                        }
                    }
                }
            }
        }
        return null;
    }


}
