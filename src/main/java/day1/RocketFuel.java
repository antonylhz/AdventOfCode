package day1;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Scanner;

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
        try {
            URL url = rocketFuel.getClass().getClassLoader().getResource("day1/RocketComponentMasses");
            assert url != null;
            Scanner scanner = new Scanner(new File(url.getFile()));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                res += rocketFuel.part2(Integer.parseInt(line));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(res);
    }

}
