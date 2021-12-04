package y19.day9;

import y19.day5.IntCodeComputer;
import util.AocInputReader;

public class Boost {

    public static void main(String[] args) {

        String[] tokens = AocInputReader.readLines("day9/input")[0].split(",");
        long[] data = new long[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            data[i] = Long.parseLong(tokens[i]);
        }
        IntCodeComputer intCodeComputer = new IntCodeComputer(1_000_000, data);
        System.out.println(intCodeComputer.run(2));
    }

}
