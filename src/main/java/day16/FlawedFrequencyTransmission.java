package day16;

import util.AocInputReader;

import java.util.Arrays;

public class FlawedFrequencyTransmission {

    private static final int[] PATTERN = new int[]{
            0, 1, 0, -1
    };

    public int[] runPhases(int[] input, int rounds) {
        int[] res = input;
        for (int r = 0; r < rounds; r++) {
            res = runPhase(res);
        }
        return res;
    }

    public int[] runPhase(int[] input) {
        int[] res = new int[input.length];
        for (int i = 0; i < input.length; i++) {
            res[i] = applyPattern(input, i);
        }
        return res;
    }

    public int applyPattern(int[] input, int seq) {
        long sum = 0;
        for (int i = 0; i < input.length; i++) {
            int pid = ((i + 1) / (seq + 1)) % PATTERN.length;
            sum += input[i % input.length] * PATTERN[pid];
        }
        return (int) Math.abs(sum) % 10;
    }

    public static void main(String[] args) {
        String str = AocInputReader.readLines("day16/input")[0];
        int[] input = new int[str.length()];
        for (int i = 0; i < input.length; i++) {
            input[i] = str.charAt(i) - '0';
        }

        FlawedFrequencyTransmission fft = new FlawedFrequencyTransmission();
        System.out.println(Arrays.toString(fft.runPhases(input, 100)));
    }

}
