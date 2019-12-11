package day10;

import java.io.File;
import java.net.URL;
import java.util.*;

public class MonitoringStation {

    private Set<Asteroid> asteroids;

    public MonitoringStation(LinkedList<LinkedList<Boolean>> data) {
        this.asteroids = new HashSet<>();
        for (int i = 0; i < data.size(); i++) {
            LinkedList<Boolean> line = data.get(i);
            for (int j = 0; j < line.size(); j++) {
                if (line.get(j)) {
                    asteroids.add(new Asteroid(j, i));
                }
            }
        }
    }

    public Asteroid findBestAsteroid() {
        Asteroid best = null;
        int max = 0;
        for (Asteroid origin : asteroids) {
            int observableAsteroids = findObservableAsteroids(origin).size();
            if (observableAsteroids > max) {
                best = origin;
                max = observableAsteroids;
            }
        }
        if (best != null) {
            System.out.println("Build monitoring station at " + best.toString() +
                    " that observes " + max + " other asteroids");
        }
        return best;
    }

    public List<Asteroid> vaporizeFullRotation(Asteroid origin) {
        Map<Integer, List<Asteroid>> observableAsteroidMap = findObservableAsteroids(origin);
        List<Asteroid> vaporized = new ArrayList<>();
        for (List<Asteroid> list : observableAsteroidMap.values()) {
            list.sort(Comparator.comparingInt(asteroid -> dist(origin, asteroid)));
            vaporized.add(list.get(0));
        }
        vaporized.sort(Comparator.comparing(o -> angle(origin, o)));
        return vaporized;
    }

    private double angle(Asteroid origin, Asteroid asteroid) {
        int xd = asteroid.x - origin.x, yd = asteroid.y - origin.y;
        double res = Math.atan2(-yd, xd);
        if (res > Math.PI / 2) {
            res -= 2 * Math.PI;
        }
        return -res;
    }

    private int dist(Asteroid origin, Asteroid asteroid) {
        return Math.abs(asteroid.x - origin.x) + Math.abs(asteroid.y - origin.y);
    }

    private Map<Integer, List<Asteroid>> findObservableAsteroids(Asteroid origin) {
        Map<Integer, List<Asteroid>> res = new HashMap<>();

        for (Asteroid asteroid : asteroids) {
            if (!asteroid.equals(origin)) {
                int hc = calculateSlopeHashCode(origin, asteroid);
                res.putIfAbsent(hc, new ArrayList<>());
                res.get(hc).add(asteroid);
            }
        }

        return res;
    }

    private int calculateSlopeHashCode(Asteroid origin, Asteroid asteroid) {
        int xd = asteroid.x - origin.x, yd = asteroid.y - origin.y;
        int gcd = gcd(xd, yd);
        xd /= gcd;
        yd /= gcd;

        return (xd << 16) + yd;
    }

    private int gcd(int x, int y) {
        x = Math.abs(x);
        y = Math.abs(y);
        while(y > 0) {
            int t = y;
            y = x % y;
            x = t;
        }
        return x;
    }

    public static void main(String[] args) {
        try {
            URL url = MonitoringStation.class.getClassLoader().getResource("day10/input");
            assert url != null;
            LinkedList<LinkedList<Boolean>> data = new LinkedList<>();
            Scanner scanner = new Scanner(new File(url.getFile()));
            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                LinkedList<Boolean> line = new LinkedList<>();
                for (char c : str.toCharArray()) {
                    line.add(c == '#');
                }
                data.add(line);
            }
            MonitoringStation monitoringStationFinder = new MonitoringStation(data);
            Asteroid asteroid = monitoringStationFinder.findBestAsteroid();
            System.out.println(monitoringStationFinder.vaporizeFullRotation(asteroid).get(199));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Asteroid {
    int x;
    int y;
    public Asteroid(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asteroid asteroid = (Asteroid) o;
        return x == asteroid.x &&
                y == asteroid.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Asteroid{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
