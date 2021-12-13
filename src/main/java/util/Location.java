package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Location {
    public int r;
    public int c;

    public Location(int r, int c) {
        this.r = r;
        this.c = c;
    }

    public Location(List<Integer> list) {
        this(list.get(0), list.get(1));
    }

    public List<Location> getNeighbors(int height, int width) {
        List<Location> res = new ArrayList<>();
        if (r > 0) res.add(new Location(r - 1, c));
        if (r < height - 1) res.add(new Location(r + 1, c));
        if (c > 0) res.add(new Location(r, c - 1));
        if (c < width - 1) res.add(new Location(r, c + 1));
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return r == location.r &&
                c == location.c;
    }

    @Override
    public int hashCode() {
        return Objects.hash(r, c);
    }

    @Override
    public String toString() {
        return "Location{" +
                "r=" + r +
                ", c=" + c +
                '}';
    }
}
