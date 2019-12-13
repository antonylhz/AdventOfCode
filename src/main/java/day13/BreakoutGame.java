package day13;

import day5.IntCodeComputer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class BreakoutGame {

    private IntCodeComputer intCodeComputer;
    private int score;
    private Scanner systemInput;
    private int[][] screen;
    private int width, height;
    private int paddle, ball, blocks;
    private LinkedList<Long> inputs;

    public BreakoutGame(long[] program) {
        this.intCodeComputer = new IntCodeComputer(1_000_000, program);
        this.score = 0;
        this.systemInput = new Scanner(System.in);
        this.screen = null;
        this.inputs = new LinkedList<>();
        this.blocks = 0;
        run();
    }

    public void run () {
        List<List<Tile>> tiles = new ArrayList<>(5);
        for (int i = 0; i < 5; i++) {
            tiles.add(new ArrayList<>());
        }

        LinkedList<Long> outputs = intCodeComputer.run(inputs);
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
                inputs.offer(getJoyStickPosition());
            } else {
                movePaddleToBall();
            }
            run();
            printGameState();
        } while (blocks > 0);
    }

    private void movePaddleToBall() {
        long nextInput = 0L;
        if (paddle < ball) { // go right
            nextInput = 1;
        } else if (paddle > ball) { // go left
            nextInput = -1;
        }

        inputs.offer(nextInput);
    }

    private Long getJoyStickPosition() {
        String str = systemInput.nextLine();
        switch (str) {
            case "b":
                return -1L;
            case "n":
                return 0L;
            case "m":
                return 1L;
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
        try {
            URL url = BreakoutGame.class.getClassLoader().getResource("day13/input");
            assert url != null;
            Scanner scanner = new Scanner(new File(url.getFile()));
            while (scanner.hasNextLine()) {
                String[] tokens = scanner.nextLine().split(",");
                long[] data = new long[tokens.length];
                for (int i = 0; i < tokens.length; i++) {
                    data[i] = Long.parseLong(tokens[i]);
                }
                data[0] = 2;
                BreakoutGame breakoutGame = new BreakoutGame(data);
                breakoutGame.playGame(false);
                System.out.println("Game finished! Score = " + breakoutGame.score);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
