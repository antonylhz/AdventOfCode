package day19;

import day5.IntCodeComputer;
import util.AocInputReader;

import java.util.Objects;

public class TractorBeam {

    private long[] program;

    public TractorBeam(long[] program) {
        this.program = program;
    }

    public void scan(int size) {
        boolean[][] grid = new boolean[size][size];
        int pulled = 0;
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++) {
                grid[r][c] = isPulled(r, c);
                if (grid[r][c]) {
                    pulled++;
                }
            }
        System.out.println(pulled);
        printGrid(grid);
    }

    private void printGrid(boolean[][] grid) {
        StringBuilder sb = new StringBuilder();
        for (boolean[] line : grid) {
            for (boolean b : line) {
                sb.append(b ? "#" : ".");
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }

    public void findSquare(int size) {
        int c0 = 0;
        for (int r = size; ;r++) {
            while (!isPulled(r, c0)) c0++;
            int rp = 1;
            while (isPulled(r, c0 + rp)) rp++;
            if (rp > size) {
                c0 += rp - size;
                int cp = 1;
                while (isPulled(r + cp, c0)) cp++;
                if (cp == size) {
                    System.out.println(c0 * 10000 + r);
                    return;
                }
            }
        }
    }

    private boolean isPulled(int r, int c) {
       return Objects.requireNonNull(new IntCodeComputer(1_000_000, program)
               .run(c, r)
               .poll())
               .intValue() == 1;
    }

    public static void main(String[] args) {
        try {
            String[] tokens = AocInputReader.readLines("day19/input")[0].split(",");
            long[] program = new long[tokens.length];
            for (int i = 0; i < program.length; i++) {
                program[i] = Long.parseLong(tokens[i]);
            }
            TractorBeam tractorBeam = new TractorBeam(program);
            tractorBeam.scan(50);
            tractorBeam.findSquare(100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
