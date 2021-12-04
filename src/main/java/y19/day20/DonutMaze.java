package y19.day20;

import util.AocInputReader;
import util.GraphUtils;
import util.Location;
import util.Linkable;

import java.util.*;

public class DonutMaze {

    private Map<String, List<Node>> idMap = new HashMap<>();
    private Map<Node, Node> portalMap = new HashMap<>();

    public DonutMaze(char[][] input) {
        Set<Location> passages = new HashSet<>();
        int height = input.length, width = input[0].length;
        for (int r = 0; r < input.length; r++) {
            for (int c = 0; c < input[0].length; c++) {
                if (input[r][c] == '.') {
                    passages.add(new Location(r, c));
                } else if (Character.isLetter(input[r][c])) {
                    if (r < height - 1 &&
                            Character.isLetter(input[r + 1][c])) {
                        String id = "" + input[r][c] + input[r + 1][c];
                        Location loc = null;
                        boolean outer = false;
                        if (r < height - 2 && input[r + 2][c] == '.') {
                            loc = new Location(r + 2, c);
                            outer = r == 0;
                        }
                        if (r > 0 && input[r - 1][c] == '.') {
                            loc = new Location(r - 1, c);
                            outer = r + 1 == height - 1;
                        }
                        assert loc != null;
                        Node node = new Node(id, loc, outer);
                        idMap.putIfAbsent(id, new ArrayList<>());
                        idMap.get(id).add(node);
                    } else if (c < width - 1 &&
                            Character.isLetter(input[r][c + 1])) {
                        String id = "" + input[r][c] + input[r][c + 1];
                        Location loc = null;
                        boolean outer = false;
                        if (c < width - 2 && input[r][c + 2] == '.') {
                            loc = new Location(r, c + 2);
                            outer = c == 0;
                        }
                        if (c > 0 && input[r][c - 1] == '.') {
                            loc = new Location(r, c - 1);
                            outer = c + 1 == width - 1;
                        }
                        assert loc != null;
                        Node node = new Node(id, loc, outer);
                        idMap.putIfAbsent(id, new ArrayList<>());
                        idMap.get(id).add(node);
                    }
                }
            }
        }

        List<Node> nodes = new ArrayList<>();
        for (List<Node> list : idMap.values()) {
            if (list.size() == 2) {
                Node p1 = list.get(0), p2 = list.get(1);
                portalMap.put(p1, p2);
                portalMap.put(p2, p1);
            }
            nodes.addAll(list);
        }

        GraphUtils.findShortestDistanceBetweenAllVertexPairs(nodes, passages, height, width);
    }

    public int findShortestPath(String entranceId, String exitId) {
        Node entrance = idMap.get(entranceId).get(0);
        Node exit = idMap.get(exitId).get(0);
        return GraphUtils.findShortestDistance(entrance, exit, portalMap);
    }

    public int findShortestPathInRecursiveSpace(String entranceId, String exitId) {
        RecNode entrance = new RecNode(idMap.get(entranceId).get(0), 0);
        RecNode exit = new RecNode(idMap.get(exitId).get(0), 0);
        Map<RecNode, Integer> distMap = new HashMap<>();
        distMap.put(entrance, 0);
        PriorityQueue<RecNode> heap = new PriorityQueue<>(
                Comparator.comparingInt(distMap::get));
        heap.offer(entrance);
        while (!heap.isEmpty()) {
            RecNode recNode = heap.poll();
            Node node = recNode.node;
            int level = recNode.level;
            int dist = distMap.get(recNode);
            System.out.println(dist + ":" + recNode);
            if (distMap.containsKey(exit) && distMap.get(exit) <= dist) break;
            for (Node nei : node.edges.keySet()) {
                RecNode recNei = new RecNode(nei, level);
                if (!distMap.containsKey(recNei) || distMap.get(recNei) > dist + node.edgeTo(nei)) {
                    distMap.put(recNei, dist + node.edgeTo(nei));
                    Node neiPortal = portalMap.get(nei);
                    // outer labels at level 0 are walls, skip its portal
                    if (nei.outer && level == 0) continue;
                    // no portal exists
                    if (neiPortal == null) continue;
                    RecNode recNeiPortal;
                    if (nei.outer) {
                        recNeiPortal = new RecNode(neiPortal, level - 1);
                    } else {
                        recNeiPortal = new RecNode(neiPortal, level + 1);
                    }
                    if (!distMap.containsKey(recNeiPortal) || distMap.get(recNeiPortal) > dist + node.edgeTo(nei) + 1) {
                        distMap.put(recNeiPortal, dist + node.edgeTo(nei) + 1);
                        heap.offer(recNeiPortal);
                    }
                }
            }
        }
        return distMap.getOrDefault(exit, -1);
    }

    public static void main(String[] args) {
        try {
            DonutMaze donutMaze = new DonutMaze(
                    AocInputReader.readCharMatrix("day20/input"));
            System.out.println(donutMaze.findShortestPath("AA", "ZZ"));
            System.out.println(donutMaze.findShortestPathInRecursiveSpace("AA", "ZZ"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class RecNode {
    Node node;
    int level;

    public RecNode(Node node, int level) {
        this.node = node;
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecNode recNode = (RecNode) o;
        return level == recNode.level &&
                Objects.equals(node, recNode.node);
    }

    @Override
    public int hashCode() {
        return Objects.hash(node, level);
    }

    @Override
    public String toString() {
        return "RecNode{" +
                "node=" + node +
                ", level=" + level +
                '}';
    }
}

class Node extends Linkable<Node> {
    String id;
    boolean outer;

    public Node(String id, Location loc, boolean outer) {
        super(loc);
        this.id = id;
        this.outer = outer;
    }

}