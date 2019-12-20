package util;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Linkable<T extends Linkable<T>> {
    public Location loc;
    public Map<T, Integer> edges;

    public Linkable(Location loc) {
        this.loc = loc;
        this.edges = new HashMap<>();
    }

    public int edgeTo(T linkable) {
        if (linkable == this) return 0;
        return edges.getOrDefault(linkable, Integer.MAX_VALUE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Linkable)) return false;
        Linkable<?> linkable = (Linkable<?>) o;
        return Objects.equals(loc, linkable.loc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loc);
    }

    @Override
    public String toString() {
        return "Linkable{" +
                "loc=" + loc +
                '}';
    }
}
