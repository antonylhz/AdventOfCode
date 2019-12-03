package day3;

import java.io.File;
import java.net.URL;
import java.util.*;

public class CrossedWires {

    private Wire wire1, wire2;

    public CrossedWires(Wire wire1, Wire wire2) {
        this.wire1 = wire1;
        this.wire2 = wire2;
    }

    public int findClosestIntersection() {
        Set<Point> visited = new HashSet<>(wire1.path);
        int min = Integer.MAX_VALUE;
        for (Point point : wire2.path) {
            if (visited.contains(point) && point.dist() != 0) {
                min = Math.min(min, point.dist());
            }
        }
        return min;
    }

    public int findClosestIntersection2() {
        Map<Point, Integer> map1 = wire1.getPointStepMap();
        Map<Point, Integer> map2 = wire2.getPointStepMap();

        int min = Integer.MAX_VALUE;
        Point intersection;

        for (Map.Entry<Point, Integer> entry : map2.entrySet()) {
            if (map1.containsKey(intersection =entry.getKey())) {
                int combined = map1.get(intersection) + map2.get(intersection);
                if (combined != 0)
                    min = Math.min(min, combined);
            }
        }
        return min;
    }

    public static void main (String[] args) {
        try {
            URL url = CrossedWires.class.getClassLoader().getResource("day3/input");
            assert url != null;
            Scanner scanner = new Scanner(new File(url.getFile()));
            String[] steps1 = scanner.nextLine().split(",");
            String[] steps2 = scanner.nextLine().split(",");
            CrossedWires crossedWires = new CrossedWires(new Wire(steps1), new Wire(steps2));
            System.out.println(crossedWires.findClosestIntersection2());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Point {
    int x;
    int y;
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Point(Point point, int[] diff) {
        this.x = point.x + diff[0];
        this.y = point.y + diff[1];
    }

    public int dist() {
        return Math.abs(this.x) + Math.abs(this.y);
    }

    @Override
    public int hashCode() {
        return (x << 16) + y;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Point && this.hashCode() == other.hashCode();
    }
}

class Wire {
    private static final int[][] DIR = new int[][] {
            {0, 1},   // U
            {0, -1},  // D
            {1, 0},   // R
            {-1, 0}   // L
    };

    LinkedList<Point> path;
    public Wire(String[] steps) {
        this.path = new LinkedList<>();
        this.path.addLast(new Point(0, 0));
        for (String step : steps) {
            int dir = 0;
            switch (step.charAt(0)) {
                case 'U':
                    dir = 0;
                    break;
                case 'D':
                    dir = 1;
                    break;
                case 'R':
                    dir = 2;
                    break;
                case 'L':
                    dir = 3;
                    break;
            }
            int count = Integer.parseInt(step.substring(1));
            for (int i = 0; i < count; i++) {
                if (path.peekLast() != null)
                    path.add(new Point(path.peekLast(), DIR[dir]));
            }
        }
    }

    public Map<Point, Integer> getPointStepMap() {
        Map<Point, Integer> map = new HashMap<>(path.size());
        int steps = 0;
        for (Point point : path) {
            if (!map.containsKey(point)) {
                map.put(point, steps);
            }
            steps++;
        }
        return map;
    }

}
