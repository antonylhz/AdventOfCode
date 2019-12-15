package day11;

import day5.IntCodeComputer;
import util.AocInputReader;

import java.util.*;

public class PaintingRobot {

    private long[] data;
    private IntCodeComputer intCodeComputer;
    private State state;
    private Map<Position, Long> colorMap;

    public PaintingRobot(long[] data) {
        this.data = data;
        this.intCodeComputer = new IntCodeComputer(1_000_000, data);
        this.state = new State(0, 0);
        this.colorMap = new HashMap<>();
    }

    public void paint() {
        colorMap.put(state.getCurrentPosition(), 1L);
        while (!intCodeComputer.isHalted) {
            LinkedList<Long> inputs = new LinkedList<>(Collections.singleton(getCurrentColor()));
            LinkedList<Long> outputs = intCodeComputer.run(inputs);
            colorMap.put(state.getCurrentPosition(), outputs.pollFirst());
            state.turn(outputs.pollFirst().equals(1L));
        }
        int minX = 0, maxX = 0, minY = 0, maxY = 0;
        for (Position pos : colorMap.keySet()) {
            minX = Math.min(minX, pos.x);
            minY = Math.min(minY, pos.y);
            maxX = Math.max(maxX, pos.x);
            maxY = Math.max(maxY, pos.y);
        }

        boolean[][] graph = new boolean[maxY - minY + 1][maxX - minX + 1];
        for (Map.Entry<Position, Long> entry : colorMap.entrySet()) {
            Position position = entry.getKey();
            long color = entry.getValue();
            graph[position.y - minY][position.x - minX] = color == 1;
        }

        StringBuilder sb = new StringBuilder();
        for (int y = graph.length - 1; y >= 0; y--) {
            for (int x = 0; x < graph[0].length; x++) {
                sb.append(graph[y][x] ? '#' : ' ');
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }

    private long getCurrentColor() {
        return colorMap.getOrDefault(state.getCurrentPosition(), 0L);
    }

    public static void main(String[] args) {
        String[] tokens = AocInputReader.readLines("day11/input")[0].split(",");
        long[] data = new long[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            data[i] = Long.parseLong(tokens[i]);
        }
        PaintingRobot robot = new PaintingRobot(data);
        robot.paint();
    }
}

class State {
    int x;
    int y;
    Direction direction;

    public State(int x, int y) {
        this.x = x;
        this.y = y;
        this.direction = Direction.UP;
    }

    public void turn(boolean clockwise) {
        switch (direction) {
            case UP:
                direction = clockwise ? Direction.RIGHT : Direction.LEFT;
                break;
            case DOWN:
                direction = clockwise ? Direction.LEFT : Direction.RIGHT;
                break;
            case LEFT:
                direction = clockwise ? Direction.UP : Direction.DOWN;
                break;
            case RIGHT:
                direction = clockwise ? Direction.DOWN : Direction.UP;
                break;
        }
        switch (direction) {
            case UP:
                y++;
                break;
            case DOWN:
                y--;
                break;
            case LEFT:
                x--;
                break;
            case RIGHT:
                x++;
                break;
        }
    }

    public Position getCurrentPosition() {
        return new Position(x, y);
    }

}

enum Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

class Position {
    int x;
    int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}