package day2;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Scanner;

public class Intcode {

    private int[] data;

    private int run(final int[] originalData, final int noun, final int verb) {
        data = Arrays.copyOf(originalData, originalData.length);
        data[1] = noun;
        data[2] = verb;
        for (int i = 0; i < data.length; i += 4) {
            if (data[i] == 99) break;
            else if (data[i] == 1) {
                data[data[i + 3]] = data[data[i + 1]] + data[data[i + 2]];
            } else if (data[i] == 2) {
                data[data[i + 3]] = data[data[i + 1]] * data[data[i + 2]];
            }
        }
        return data[0];
    }

    private Integer find(int[] data, int target) {
        for (int noun = 12; noun < 100000; noun++) {
            for (int verb = 2; verb < 100000; verb++) {
                try {
                    int res = run(data, noun, verb);
                    System.out.println(noun + "," + verb + " = " + res);
                    if (res == target) {
                        return noun * 100 + verb;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    break;
                }
            }
        }

        return null;
    }

    public static void main(String[] args) {
        try {
            URL url = Intcode.class.getClassLoader().getResource("day2/input");
            assert url != null;
            Scanner scanner = new Scanner(new File(url.getFile()));
            while (scanner.hasNextLine()) {
                String[] tokens = scanner.nextLine().split(",");
                int[] data = new int[tokens.length];
                for (int i = 0; i < tokens.length; i++) {
                    data[i] = Integer.parseInt(tokens[i]);
                }
                Intcode intcode = new Intcode();
                System.out.println(intcode.find(data,19690720));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

