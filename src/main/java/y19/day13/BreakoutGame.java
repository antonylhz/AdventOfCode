package y19.day13;

import y19.day5.IntCodeComputer;
import util.AocInputReader;

import java.io.IOException;
import java.util.*;

public class BreakoutGame {

    private IntCodeComputer intCodeComputer;
    private int score;
    private Scanner systemInput;
    private int[][] screen;
    private int width, height;
    private int paddle, ball, blocks;

    public BreakoutGame(long[] program) {
        this.intCodeComputer = new IntCodeComputer(1_000_000, program);
        this.score = 0;
        this.systemInput = new Scanner(System.in);
        this.screen = null;
        this.blocks = 0;
        run();
    }

    public void run(long... args) {
        List<List<Tile>> tiles = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            tiles.add(new ArrayList<>());
        }

        LinkedList<Long> outputs = intCodeComputer.run(args);
        while (!outputs.isEmpty()) {
            Tile tile = new Tile(outputs);
            if (tile.isSegmentDisplay) {
                this.score = tile.val;
            } else {
                tiles.get(tile.val).add(tile);
            }
        }

        update(tiles);
    }

    public void playGame(boolean manual) {
        do {
            if (manual) {
                run(getJoyStickPosition());
            } else {
                run(movePaddleToBall());
            }
            printGameState();
        } while (blocks > 0);
    }

    private int movePaddleToBall() {
        int nextInput = 0;
        if (paddle < ball) { // go right
            nextInput = 1;
        } else if (paddle > ball) { // go left
            nextInput = -1;
        }
        return nextInput;
    }

    private int getJoyStickPosition() {
        String str = systemInput.nextLine();
        switch (str) {
            case "b":
                return -1;
            case "n":
                return 0;
            case "m":
                return 1;
            default:
                return getJoyStickPosition();
        }
    }

    private void update(List<List<Tile>> tiles) {
        if (screen == null) {
            for (List<Tile> list : tiles)
                for (Tile tile : list) {
                    width = Math.max(width, tile.x + 1);
                    height = Math.max(height, tile.y + 1);
                }
            screen = new int[height][width];
        }

        for (int i = 0; i < 5; i++) {
            for (Tile tile : tiles.get(i)) {
                if (screen[tile.y][tile.x] == 2 && i != 2)
                    blocks--;
                else if (screen[tile.y][tile.x] != 2 && i == 2)
                    blocks++;
                screen[tile.y][tile.x] = i;
            }
        }

        if (!tiles.get(3).isEmpty()) paddle = tiles.get(3).get(0).x;
        if (!tiles.get(4).isEmpty()) ball = tiles.get(4).get(0).x;

    }

    private void printGameState() {
        try {
            Runtime.getRuntime().exec("clear");
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(score).append("\n");

        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                switch (screen[r][c]) {
                    case 0: // empty
                        sb.append(" ");
                        break;
                    case 1: // wall
                        sb.append("x");
                        break;
                    case 2: // block
                        sb.append("+");
                        break;
                    case 3: // paddle
                        sb.append("_");
                        break;
                    case 4: // ball
                        sb.append("o");
                        break;
                }
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }

    public static void main(String[] args) {
        String[] tokens = AocInputReader.readLines("day13/input")[0].split(",");
        long[] data = new long[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            data[i] = Long.parseLong(tokens[i]);
        }
        data[0] = 2;
        BreakoutGame breakoutGame = new BreakoutGame(data);
        breakoutGame.playGame(false);
        System.out.println("Game finished! Score = " + breakoutGame.score);
    }

}

class Tile {
    int x;
    int y;
    int val;
    boolean isSegmentDisplay;

    public Tile(LinkedList<Long> outputs) {
        this.x = outputs.pollFirst().intValue();
        this.y = outputs.poll().intValue();
        this.val = outputs.poll().intValue();
        this.isSegmentDisplay = x == -1 && y == 0;
    }
}
