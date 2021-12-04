package y19.day1;

import util.AocInputReader;

public class RocketFuel {

    private int part1(int mass) {
        return mass / 3 - 2;
    }

    private int part2(int mass) {
        int total = 0;
        while (mass > 0) {
            int fuel = mass / 3 - 2;
            if (fuel > 0) {
                total += fuel;
            }
            mass = fuel;
        }
        return total;
    }

    public static void main(String[] args) {
        RocketFuel rocketFuel = new RocketFuel();
        int res = 0;
        String[] lines = AocInputReader.readLines("day1/input");
        for (String line : lines) {
            res += rocketFuel.part2(Integer.parseInt(line));
        }
        System.out.println(res);
    }

}
