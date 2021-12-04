package y19.day15;

import y19.day5.IntCodeComputer;
import util.AocInputReader;
import java.util.*;

public class RepairDrone {

    private IntCodeComputer intCodeComputer;
    private Position start;
    private Position oxygen;
    private Map<Position, Integer> statusMap;
    private Map<Position, Set<Position>> neighborsMap;

    public RepairDrone(long[] program) {
        this.intCodeComputer = new IntCodeComputer(1_000_000, program);
        this.start = new Position(0, 0);
        this.statusMap = new HashMap<>();
        statusMap.put(start, 3);
        this.neighborsMap = new HashMap<>();

        randomWalk();
        assert oxygen != null;

        printGrid();
    }

    private void randomWalk() {
        Position pos = this.start;
        for (int i = 0; i < 1_000_000; i++) {
            int dir = 1 + (int) Math.floor(4 * Math.random());
            Position np = pos.moveTo(dir);
            int status = sendCommand(dir);
            statusMap.putIfAbsent(np, status);
            if (status > 0) {
                pos = np;
                if (status == 2) {
                    oxygen = pos;
                }
            }
        }

        for (Position p : statusMap.keySet()) {
            for (int dir = 1; dir <= 4; dir++) {
                Position np = p.moveTo(dir);
                if (statusMap.getOrDefault(np, 0) > 0) {
                    neighborsMap.putIfAbsent(p, new HashSet<>());
                    neighborsMap.get(p).add(np);
                }
            }
        }
    }

    public int findShortestPath(Position from, Position to) {
        Set<Position> visited = new HashSet<>();
        LinkedList<Position> queue = new LinkedList<>();
        queue.offer(from);
        int dist = 0;
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0 ; !queue.isEmpty() && i < size; i++) {
                Position position = queue.poll();
                if (position.equals(to)) return dist;
                visited.add(position);
                for (Position nei : neighborsMap.get(position)) {
                    if (!visited.contains(nei)) {
                        queue.add(nei);
                    }
                }
            }
            dist++;
        }
        return --dist;
    }

    private void printGrid() {
        int minX = 0, minY = 0, maxX = 0, maxY = 0;
        for (Position position : statusMap.keySet()) {
            minX = Math.min(minX, position.x);
            minY = Math.min(minY, position.y);
            maxX = Math.max(maxX, position.x);
            maxY = Math.max(maxY, position.y);
        }

        StringBuilder sb = new StringBuilder();
        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {
                switch (statusMap.getOrDefault(new Position(x, y), 0)) {
                    case 0:
                        sb.append("#");
                        break;
                    case 1:
                        sb.append(".");
                        break;
                    case 2:
                        sb.append("O");
                        break;
                    case 3:
                        sb.append("R");
                        break;
                }
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }

    private int sendCommand(int dir) {
        return intCodeComputer.run(dir).pollFirst().intValue();
    }

    public static void main(String[] args) {
        String[] tokens = AocInputReader.readLines("day15/input")[0].split(",");
        long[] program = new long[tokens.length];
        for (int i = 0; i < program.length; i++) {
            program[i] = Long.parseLong(tokens[i]);
        }
        RepairDrone repairDrone = new RepairDrone(program);
        System.out.println(repairDrone.findShortestPath(repairDrone.start, repairDrone.oxygen));
        System.out.println(repairDrone.findShortestPath(repairDrone.oxygen, null));
    }
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

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public Position moveTo(int dir) {
        Position pos = new Position(this.x, this.y);
        if (dir == 1) {
            pos.y--;
        }
        if (dir == 2) {
            pos.y++;
        }
        if (dir == 3) {
            pos.x--;
        }
        if (dir == 4) {
            pos.x++;
        }
        return pos;
    }
}