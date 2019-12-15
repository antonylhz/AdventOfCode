package day9;

import day5.IntCodeComputer;
import util.AocInputReader;

import java.util.LinkedList;

public class Boost {

    public static void main(String[] args) {

        String[] tokens = AocInputReader.readLines("day9/input")[0].split(",");
        long[] data = new long[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            data[i] = Long.parseLong(tokens[i]);
        }
        IntCodeComputer intCodeComputer = new IntCodeComputer(1_000_000, data);
        LinkedList<Long> input = new LinkedList<>();
        input.add(2L);
        System.out.println(intCodeComputer.run(input));
    }

}
