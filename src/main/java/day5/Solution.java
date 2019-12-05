package day5;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;

public class Solution {

    private LinkedList<Integer> run(
            final int[] originalData, final LinkedList<Integer> input) {
        int[] data = Arrays.copyOf(originalData, originalData.length);
        LinkedList<Integer> output = new LinkedList<>();

        int i = 0;
        while (i < data.length) {
            switch (data[i] % 100) {
                case 1:
                    data[data[i + 3]] =
                            getVal(data, i + 1, data[i], 100) +
                            getVal(data, i + 2, data[i], 1000);
                    i += 4;
                    break;
                case 2:
                    data[data[i + 3]] =
                            getVal(data, i + 1, data[i], 100) *
                            getVal(data, i + 2, data[i], 1000);
                    i += 4;
                    break;
                case 3:
                    data[data[i + 1]] = input.pollFirst();
                    i += 2;
                    break;
                case 4:
                    output.addLast(getVal(data, i + 1, data[i], 100));
                    i += 2;
                    break;
                case 5:
                    if (getVal(data, i + 1, data[i], 100) != 0) {
                        i = getVal(data, i + 2, data[i], 1000);
                    } else {
                        i += 3;
                    }
                    break;
                case 6:
                    if (getVal(data, i + 1, data[i], 100) == 0) {
                        i = getVal(data, i + 2, data[i], 1000);
                    } else {
                        i += 3;
                    }
                    break;
                case 7:
                    if (getVal(data, i + 1, data[i], 100) <
                        getVal(data, i + 2, data[i], 1000)) {
                        data[data[i + 3]] = 1;
                    } else {
                        data[data[i + 3]] = 0;
                    }
                    i += 4;
                    break;
                case 8:
                    if (getVal(data, i + 1, data[i], 100).equals(
                            getVal(data, i + 2, data[i], 1000))) {
                        data[data[i + 3]] = 1;
                    } else {
                        data[data[i + 3]] = 0;
                    }
                    i += 4;
                    break;
                case 99:
                    return output;
                default:
                    System.out.println("Bad data!");
                    return output;
            }
        }
        return output;
    }

    private Integer getVal(int[] data, int index, int opcode, int radix) {
        switch ((opcode % (radix * 10)) / radix) {
            case 0: // address mode
                return data[data[index]];
            case 1: // immediate mode
                return data[index];
            default:
                return null;
        }
    }


    public static void main(String[] args) {
        try {
            URL url = Solution.class.getClassLoader().getResource("day5/input");
            assert url != null;
            Scanner scanner = new Scanner(new File(url.getFile()));
            while (scanner.hasNextLine()) {
                String[] tokens = scanner.nextLine().split(",");
                int[] data = new int[tokens.length];
                for (int i = 0; i < data.length; i++) {
                    data[i] = Integer.parseInt(tokens[i]);
                }
                LinkedList<Integer> input = new LinkedList<>(Collections.singletonList(5));
                System.out.println(new Solution().run(data, input));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
