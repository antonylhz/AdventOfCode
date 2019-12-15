package day2;

import util.AocInputReader;

import java.util.Arrays;

public class Intcode {

    private int counter;

    private int run(final int[] originalData, final int noun, final int verb) {
        int[] data = Arrays.copyOf(originalData, originalData.length);
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

    /**
     * Brute force.
     * Time Complexity: O(n2)
     */
    private Integer find(int[] data, int target) {
        counter = 0;
        for (int noun = 12; noun < data.length; noun++) {
            for (int verb = 2; verb < data.length; verb++) {
                int res = run(data, noun, verb);
                System.out.println((counter++) + ": " + noun + "," + verb + " = " + res);
                if (res == target) {
                    return noun * 100 + verb;
                }
            }
        }

        return null;
    }

    private Integer binarySearch(int[] data, int target) {
        counter = 0;
        return binarySearch(data, 0, data.length - 1, 0, data.length - 1, target);
    }

    /**
     * Compare the middle point with target.
     * Every time remove one region and thus continue with the other 2 regions.
     * Time Complexity: O(n1.58)
     */
    private Integer binarySearch(int[] data, int n1, int n2, int v1, int v2, int target) {
        if (n1 < 0 || n2 < 0 || n2 >= data.length || n1 > n2 ||
                v1 < 0 || v2 < 0 || v2 >= data.length || v1 > v2) {
            return null;
        }

        int nm = n1 + (n2 - n1) / 2, vm = v1 + (v2 - v1) / 2;
        int res = run(data, nm, vm);
        System.out.println((counter++) + " : " + "n: " + n1 + "->" + n2 + ", v: " + v1 + "->" + v2 + " = " + res);
        Integer config;
        if (res == target) {
            config = nm * 100 + vm;
        } else if (res < target) {
            config = binarySearch(data, n1, nm, vm + 1, v2, target);
            if (config == null)
                config = binarySearch(data, nm + 1, n2, v1, v2, target);
        } else {
            config = binarySearch(data, nm, n2, v1, vm - 1, target);
            if (config == null)
                config = binarySearch(data, n1, nm - 1, v1, v2, target);
        }
        return config;
    }

    /**
     * Compare the top upper corner with target.
     * Every time either remove a row or remove a column.
     * Time Complexity: O(n)
     */
    private Integer cornerSearch(int[] data, int target) {
        counter = 0;
        int n = data.length;
        int noun = 0, verb = data.length - 1;
        while (noun < n && verb >= 0) {
            int res = run(data, noun, verb);
            System.out.println((counter++) + ": n: " + noun + ", v: " + verb + " = " + res);
            if (res == target) {
                return noun * 100 + verb;
            } else if (res > target)
                verb--;
            else
                noun++;
        }
        return null;
    }

    public static void main(String[] args) {
        String[] tokens = AocInputReader.readLines("day2/input")[0].split(",");
        int[] data = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            data[i] = Integer.parseInt(tokens[i]);
        }
        Intcode intcode = new Intcode();
        System.out.println(intcode.cornerSearch(data, 19690720));
    }
}

