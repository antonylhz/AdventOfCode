package y19.day7;

import y19.day5.IntCodeComputer;
import util.AocInputReader;

import java.util.*;

public class AmplificationCircuit {

    private int ampSize;
    private LinkedList<IntCodeComputer> amplifiers;
    private long[] data;

    public AmplificationCircuit(long[] data, int ampSize) {
        this.ampSize = ampSize;
        this.data = data;
        amplifiers = new LinkedList<>();
        for (int i = 0; i < ampSize; i++) {
            amplifiers.add(new IntCodeComputer(data.length, Arrays.copyOf(data, data.length)));
        }
    }

    // Reset all int-code computers to original data
    public void reset() {
        amplifiers.clear();
        for (int i = 0; i < ampSize; i++) {
            amplifiers.add(new IntCodeComputer(data.length, Arrays.copyOf(data, data.length)));
        }
    }

    private long calculateSignal(List<Integer> phaseSettings, boolean firstLoop, long start) {
        LinkedList<Long> output;
        long extra = start;
        for (int i = 0; i < amplifiers.size(); i++) {
            if (firstLoop) {
                output = amplifiers.get(i).run(phaseSettings.get(i), extra);
            } else {
                output = amplifiers.get(i).run(extra);
            }
            if (output.isEmpty()) {
                throw new IllegalStateException("No output!");
            }
            extra = output.poll();
        }
        return extra;
    }

    public long calculateSignalInFeedbackLoop(List<Integer> phaseSettings) {
        long signal = calculateSignal(phaseSettings, true, 0);
        while (!amplifiers.isEmpty() && !amplifiers.peekLast().isHalted) {
            signal = calculateSignal(phaseSettings, false, signal);
        }
        return signal;
    }

    public long findLargestSignalInOneLoop() {
        List<List<Integer>> phaseSettings = permute(0, 4);
        long largest = 0;
        for (List<Integer> phaseSetting : phaseSettings) {
            long signal = calculateSignal(phaseSetting, true, 0);
            System.out.println(phaseSetting + "->" + signal);
            largest = Math.max(largest, signal);
            reset();
        }
        return largest;
    }

    public long findLargestSignalInFeedbackLoop() {
        List<List<Integer>> phaseSettings = permute(5, 9);
        long largest = 0;
        for (List<Integer> phaseSetting : phaseSettings) {
            long signal = calculateSignalInFeedbackLoop(phaseSetting);
            System.out.println(phaseSetting + "->" + signal);
            largest = Math.max(largest, signal);
            reset();
        }
        return largest;
    }

    private List<List<Integer>> permute(int start, int end) {
        if (start == end) {
            return Collections.singletonList(Collections.singletonList(start));
        }
        List<List<Integer>> ps = permute(start, end - 1);
        List<List<Integer>> res = new LinkedList<>();
        for (int i = 0; i <= end - start; i++) {
            for (List<Integer> p : ps) {
                List<Integer> np = new LinkedList<>(p);
                np.add(i, end);
                res.add(np);
            }
        }
        return res;
    }

    public static void main(String[] args) {
        String[] tokens = AocInputReader.readLines("day7/input")[0].split(",");
        long[] data = new long[tokens.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = Long.parseLong(tokens[i]);
        }
        AmplificationCircuit amplificationCircuit = new AmplificationCircuit(data, 5);
        System.out.println(amplificationCircuit.findLargestSignalInOneLoop());
        System.out.println(amplificationCircuit.findLargestSignalInFeedbackLoop());
    }
}
