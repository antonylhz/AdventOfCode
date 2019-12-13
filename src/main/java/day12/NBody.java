package day12;

import java.io.File;
import java.net.URL;
import java.util.*;

public class NBody {

    private Moon[] moons;

    public NBody(List<String> data) {
        this.moons = new Moon[data.size()];
        for (int i = 0; i < moons.length; i++) {
            String str = data.get(i);
            str = str.substring(1, str.length() - 1);
            String[] tokens = str.split(",");
            int[] pos = new int[3];
            for (int j = 0; j < pos.length; j++) {
                pos[j] = Integer.parseInt(tokens[j].split("=")[1]);
            }
            moons[i] = new Moon(pos);
        }
    }

    public void run(int d) {
        applyGravity(d);
        progress(d);
    }

    private void applyGravity(int k) {
        for (int i = 0; i < moons.length; i++)
        for (int j = i + 1; j < moons.length; j++) {
                if (moons[i].pos[k] < moons[j].pos[k]) {
                    moons[i].velocity[k]++;
                    moons[j].velocity[k]--;
                } else if (moons[i].pos[k] > moons[j].pos[k]) {
                    moons[i].velocity[k]--;
                    moons[j].velocity[k]++;
                }
        }
    }

    private String getState(int d) {
        StringBuilder sb = new StringBuilder();

        for (Moon moon : moons) {
            sb.append(moon.toDimString(d)).append("\n");
        }
        return sb.toString();
    }

    public long findHowManyRoundsToRepeat() {
        long[] rounds = new long[3];
        for (int i = 0; i < rounds.length; i++) {
            String orig = getState(i);
            long counter = 0;
            while (rounds[i] == 0) {
                counter++;
                run(i);
                if (getState(i).equals(orig)) {
                    rounds[i] = counter;
                }
            }
        }
        System.out.println(Arrays.toString(rounds));
        long res = 1;
        for (long r : rounds) {
            res = lcm(res, r);
        }
        return res;
    }

    private long lcm(long number1, long number2) {
        if (number1 == 0 || number2 == 0) {
            return 0;
        }
        long absNumber1 = Math.abs(number1);
        long absNumber2 = Math.abs(number2);
        long absHigherNumber = Math.max(absNumber1, absNumber2);
        long absLowerNumber = Math.min(absNumber1, absNumber2);
        long lcm = absHigherNumber;
        while (lcm % absLowerNumber != 0) {
            lcm += absHigherNumber;
        }
        return lcm;
    }

    public int totalEnergy() {
        int total = 0;
        for (Moon moon : moons) {
            total += moon.totalEnergy();
        }
        return total;
    }

    private void applyGravity() {
        for (int d = 0; d < 3; d++) {
            applyGravity(d);
        }
    }

    private void progress(int d) {
        for (Moon moon : moons) {
            moon.progress(d);
        }
    }


    public static void main(String[] args) {
        try {
            URL url = NBody.class.getClassLoader().getResource("day12/input");
            assert url != null;
            Scanner scanner = new Scanner(new File(url.getFile()));
            List<String> data = new LinkedList<>();
            while (scanner.hasNextLine()) {
                data.add(scanner.nextLine());
            }
            NBody nBody = new NBody(data);
            System.out.println(nBody.findHowManyRoundsToRepeat());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class Moon {
    int[] pos;
    int[] velocity;
    public Moon(int[] pos) {
        this.pos = pos;
        this.velocity = new int[3];
    }

    public void progress() {
        for (int i = 0; i < 3; i++) {
            pos[i] += velocity[i];
        }
    }

    public void progress(int d) {
        pos[d] += velocity[d];
    }

    public String toDimString(int d) {
        return "Moon{" +
                pos[d] +
                "," +
                velocity[d] +
                "}";
    }

    @Override
    public String toString() {
        return "Moon{" +
                "pos=" + Arrays.toString(pos) +
                ", velocity=" + Arrays.toString(velocity) +
                '}';
    }

    public int totalEnergy() {
        int pot = 0;
        for (int p : pos) pot += Math.abs(p);
        int kin = 0;
        for (int v : velocity) kin += Math.abs(v);
        return pot * kin;
    }
}
