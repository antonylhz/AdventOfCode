package day9;

import day5.IntCodeComputer;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.Scanner;

public class Boost {

    public static void main(String[] args) {
        try {
            URL url = Boost.class.getClassLoader().getResource("day9/input");
            assert url != null;
            Scanner scanner = new Scanner(new File(url.getFile()));
            while (scanner.hasNextLine()) {
                String[] tokens = scanner.nextLine().split(",");
                long[] data = new long[tokens.length];
                for (int i = 0; i < tokens.length; i++) {
                    data[i] = Long.parseLong(tokens[i]);
                }
                IntCodeComputer intCodeComputer = new IntCodeComputer(1_000_000, data);
                LinkedList<Long> input = new LinkedList<>();
                input.add(2L);
                System.out.println(intCodeComputer.run(input));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
