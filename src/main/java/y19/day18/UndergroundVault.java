package y19.day18;

import util.AocInputReader;
import util.Linkable;
import util.Location;

import java.util.*;

public class UndergroundVault {

    private Node start;

    public UndergroundVault(char[][] input) {
        List<Node> nodes = new LinkedList<>();
        int height = input.length, width = input[0].length;
        Set<Location> passages = new HashSet<>();
        for (int r = 0 ; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (input[r][c] == '.') {
                    passages.add(new Location(r, c));
                } else if (input[r][c] == '@' || Character.isLetter(input[r][c])) {
                    Node node = new Node(input[r][c], new Location(r, c));
                    nodes.add(node);
                    if (input[r][c] == '@') start = node;
                }
            }
        }
        assert start != null;
        findShortestDistanceBetweenAllVertexPairs(nodes, passages, height, width);
    }

    public static void main(String[] args) {
        char[][] input = AocInputReader.readCharMatrix("day18/input");
        UndergroundVault undergroundVault = new UndergroundVault(input);

    }

    private void findShortestDistanceBetweenAllVertexPairs(
            List<Node> vertices, Set<Location> passages, int height, int width) {
        Map<Location, Node> locMap = new HashMap<>();
        for (Node v : vertices) {
            locMap.put(v.loc, v);
        }
        for (Node origin : vertices) {
            Set<Location> visited = new HashSet<>();
            Queue<Location> q = new LinkedList<>();
            q.offer(origin.loc);
            int dist = 0;
            while (!q.isEmpty()) {
                int size = q.size();
                dist++;
                for (int j = 0; j < size; j++) {
                    Location loc = q.poll();
                    assert loc != null;
                    visited.add(loc);
                    for (Location neiLoc : loc.getNeighbors(height, width)) {
                        if (passages.contains(neiLoc) && !visited.contains(neiLoc)) {
                            q.offer(neiLoc);
                            if (locMap.containsKey(neiLoc)) {
                                Node nei = locMap.get(neiLoc);
                                if (nei.isLock) {
                                    origin.locksMap.putIfAbsent(nei, new KeyChain());
                                    //origin.locksMap.get(nei).addKey();
                                }

                                origin.edges.putIfAbsent(nei, dist);
                                nei.edges.putIfAbsent(origin, dist);
                            }
                        }
                    }
                }
            }
        }
    }

}

class Node extends Linkable<Node> {
    char id;
    boolean isKey;
    boolean isLock;
    Map<Node, KeyChain> keysMap;
    Map<Node, KeyChain> locksMap;

    public Node(char id, Location loc) {
        super(loc);
        this.id = id;
        this.isKey = Character.isLowerCase(id);
        this.isLock = Character.isUpperCase(id);
        this.keysMap = new HashMap<>();
        this.locksMap = new HashMap<>();
    }

    public char requiresKey() {
        if (isLock) return Character.toLowerCase(id);
        else throw new IllegalStateException("Not a Lock! Call isLock() first to make sure!");
    }
}

class KeyChain {
    boolean[] keys;

    public KeyChain() {
        this.keys = new boolean[26];
    }

    public void addKey(char key) {
        keys[key - 'a'] = true;
    }

    public boolean contains(char key) {
        int index = key - 'a';
        return index < 26 && keys[index];
    }

}