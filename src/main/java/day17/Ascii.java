package day17;

import day5.IntCodeComputer;
import util.AocInputReader;

import java.util.*;

public class Ascii {

    private static final int[][] dirs = new int[][]{
            {0, -1}, // N
            {0, 1},  // S
            {-1, 0}, // W
            {1, 0}   // E
    };

    private static final int[][] turned_dirs = new int[][]{
            {
                    0,
                    2, // NL -> W
                    3  // NR -> E
            },
            {
                    1,
                    3, // SL -> E
                    2  // SR -> W
            },
            {
                    2,
                    1, // WL -> S
                    0  // WR -> N
            },
            {
                    3,
                    0, // EL -> N
                    1  // ER -> S
            }

    };

    private IntCodeComputer intCodeComputer;
    private int height, width;
    private char[][] grid;
    private int x0, y0;
    private int d0;

    public Ascii(long[] program) {
        this.intCodeComputer = new IntCodeComputer(1_000_000, program);
        this.d0 = 0;
        parse(intCodeComputer.run());
    }

    private void parse(LinkedList<Long> output) {
        List<List<Character>> lines = new ArrayList<>();
        List<Character> line = new ArrayList<>();
        while (!output.isEmpty()) {
            long val = output.poll();
            if (val > Byte.MAX_VALUE) {
                System.out.println("Invalid number: " + val);
                continue;
            }
            char c = (char) val;
            if (c == '\n') {
                width = Math.max(width, line.size());
                if (!line.isEmpty()) {
                    lines.add(line);
                    line = new ArrayList<>();
                }
            } else {
                line.add(c);
                if (c != '#' && c != '.') {
                    y0 = lines.size();
                    x0 = line.size() - 1;
                }
            }
        }
        height = lines.size();
        grid = new char[height][width];
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).size(); j++) {
                grid[i][j] = lines.get(i).get(j);
            }
        }
    }

    public int sumOfAlignment() {
        int sum = 0;
        for (int i = 1; i < height - 1; i++)
            for (int j = 1; j < width - 1; j++)
                if (grid[i][j] != '.' &&
                        grid[i - 1][j] != '.' &&
                        grid[i + 1][j] != '.' &&
                        grid[i][j + 1] != '.' &&
                        grid[i][j - 1] != '.') {
                    sum += i * j;
                }
        return sum;
    }

    public void printGrid() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                sb.append(grid[i][j]);
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }

    public void rescue() {
        // R,12,L,6,R,12,L,8,L,6,L,10,R,12,L,6,R,12,R,12,L,10,L,6,R,10,L,8,L,6,L,10,R,12,L,10,L,6,R,10,L,8,L,6,L,10,R,12,L,10,L,6,R,10,R,12,L,6,R,12,R,12,L,10,L,6,R,10
        // M: A,B,A,C,B,C,B,C,A,C
        // A: R,12,L,6,R,12
        // B: L,8,L,6,L,10
        // C: R,12,L,10,L,6,R,10

        execute("A,B,A,C,B,C,B,C,A,C\n");
        execute("R,12,L,6,R,12\n");
        execute("L,8,L,6,L,10\n");
        execute("R,12,L,10,L,6,R,10\n");
        execute("n\n");
    }

    private void execute(String cmd) {
        long[] input = new long[cmd.length()];
        for (int i = 0; i < cmd.length(); i++) {
            input[i] = cmd.charAt(i);
        }
        LinkedList<Long> output = intCodeComputer.run(input);
        if (output.size() >= height * width) {
            parse(output);
            printGrid();
        }
    }

    private String findFullPath() {
        StringBuilder sb = new StringBuilder();
        int x = x0, y = y0, di = d0;
        boolean progressed;
        do {
            progressed = false;
            for (int i = 0; i < 3; i++) {
                int ti = turned_dirs[di][i];
                int stepsForward = stepsForward(x, y, ti);
                if (stepsForward > 0) {
                    di = ti;
                    x += stepsForward * dirs[di][0];
                    y += stepsForward * dirs[di][1];
                    switch (i) {
                        case 0:
                            sb.append(stepsForward);
                            break;
                        case 1:
                            sb.append("L,");
                            sb.append(stepsForward);
                            break;
                        case 2:
                            sb.append("R,");
                            sb.append(stepsForward);
                            break;
                    }
                    sb.append(",");
                    progressed = true;
                }
            }
        } while (progressed);
        return sb.toString();
    }

    private int stepsForward(int x, int y, int di) {
        int steps = 0;
        do {
            x += dirs[di][0];
            y += dirs[di][1];
            if (isValidPosition(x, y) && grid[y][x] == '#') {
                steps++;
            } else {
                return steps;
            }
        } while (true);
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < width &&
                y >= 0 && y < height;
    }

    public static void main(String[] args) {
        try {
            String[] tokens = AocInputReader.readLines("day17/input")[0].split(",");
            long[] program = new long[tokens.length];
            for (int i = 0; i < program.length; i++) {
                program[i] = Long.parseLong(tokens[i]);
            }
            program[0] = 2;
            Ascii ascii = new Ascii(program);
            System.out.println(ascii.sumOfAlignment());
            ascii.printGrid();
            System.out.println(ascii.findFullPath());
            ascii.rescue();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
