package util;

import java.util.*;

public class GraphUtils {

    public static <T extends Linkable<T>> int findShortestDistance(
            T from, T to, Map<T, T> shortcuts) {
        Map<T, Integer> distMap = new HashMap<>();
        distMap.put(from, 0);
        PriorityQueue<T> heap = new PriorityQueue<>(
                Comparator.comparingInt(distMap::get));
        heap.offer(from);
        while (!heap.isEmpty()) {
            T node = heap.poll();
            int dist = distMap.get(node);
            System.out.println(dist + ":" + node);
            if (distMap.containsKey(to) && distMap.get(to) <= dist) break;
            for (T nei : node.edges.keySet()) {
                if (!distMap.containsKey(nei) || distMap.get(nei) > dist + node.edgeTo(nei)) {
                    distMap.put(nei, dist + node.edgeTo(nei));
                    T neiPortal = shortcuts.get(nei);
                    if (neiPortal == null) continue;
                    if (!distMap.containsKey(neiPortal) ||
                            distMap.get(neiPortal) > dist + node.edgeTo(nei) + 1) {
                        distMap.put(neiPortal, dist + node.edgeTo(nei) + 1);
                        heap.offer(neiPortal);
                    }
                }
            }
        }
        return distMap.getOrDefault(to, -1);
    }

    public static <T extends Linkable<T>> void findShortestDistanceBetweenAllVertexPairs(
            List<T> vertices, Set<Location> passages, int height, int width) {
        Map<Location, T> locMap = new HashMap<>();
        for (T v : vertices) {
            locMap.put(v.loc, v);
        }
        for (T origin : vertices) {
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
                                T nei = locMap.get(neiLoc);
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
