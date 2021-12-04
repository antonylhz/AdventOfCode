package y19.day6;

import util.AocInputReader;

import java.util.*;

public class UniversalOrbitMap {

    private Map<String, Planet> planetMap;

    public UniversalOrbitMap(String[][] data) {
        this.planetMap = new HashMap<>();
        for (String[] row : data) {
            Planet p1 = getPlanet(row[0]);
            Planet p2 = getPlanet(row[1]);
            p2.parent = p1;
            p1.satellites.add(p2);
            p1.neighbors.add(p2);
            p2.neighbors.add(p1);
        }
    }

    public int findOrbitTotal() {
        int orbitTotal = 0;
        List<Planet> roots = new LinkedList<>();
        for (Planet planet : planetMap.values()) {
            if (planet.parent == null) {
                orbitTotal += findOrbitInTree(planet);
            }
        }
        return orbitTotal;
    }

    private int findShortestTransfer(String fromName, String toName) {
        Planet from = getPlanet(fromName), to = getPlanet(toName);
        int dist = 0;
        LinkedList<Planet> queue = new LinkedList<>();
        queue.offer(from);
        Set<Planet> visited = new HashSet<>();
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Planet planet = queue.poll();
                if (planet == to) {
                    return dist;
                }
                visited.add(planet);
                for (Planet neighbor : planet.neighbors) {
                    if (!visited.contains(neighbor))
                        queue.offer(neighbor);
                }
            }
            dist++;
        }
        return -1;
    }

    private int findOrbitInTree(Planet root) {
        int res = 0;
        int level = 0;
        LinkedList<Planet> queue = new LinkedList<>();
        queue.addLast(root);
        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                Planet planet = queue.poll();
                res += level;
                for (Planet satellite : planet.satellites) {
                    queue.offer(satellite);
                }
            }
            level++;
        }
        return res;
    }

    private Planet getPlanet(String name) {
        if (!planetMap.containsKey(name)) {
            planetMap.put(name, new Planet(name));
        }
        return planetMap.get(name);
    }

    public static void main(String[] args) {

        String[] lines = AocInputReader.readLines("day6/input");
        List<String[]> data = new ArrayList<>();
        for (String line : lines) {
            data.add(line.split("\\)"));
        }
        String[][] dataArray = new String[data.size()][2];
        UniversalOrbitMap universalOrbitMap = new UniversalOrbitMap(data.toArray(dataArray));
        System.out.println(universalOrbitMap.findOrbitTotal());
        System.out.println(universalOrbitMap.findShortestTransfer("YOU", "SAN") - 2);
    }
}

class Planet {
    String name;
    Planet parent;
    Set<Planet> satellites;
    Set<Planet> neighbors;

    public Planet(String name) {
        this.name = name;
        this.satellites = new HashSet<>();
        this.neighbors = new HashSet<>();
    }
}