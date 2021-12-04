package y19.day25;

import y19.day5.IntCodeComputer;
import util.AocInputReader;

import java.util.*;

public class Cryostasis {

    private IntCodeComputer droid;

    private static final String[] prerequisite = {
            "north",
            "take mutex",
            "east",
            "east",
            "east",
            "take whirled peas",
            "west",
            "west",
            "west",
            "south",
            "south",
            "take cake",
            "north",
            "west",
            "take space law space brochure",
            "north",
            "take loom",
            "south",
            "south",
            "take hologram",
            "west",
            "take manifold",
            "east",
            "north",
            "east",
            "south",
            "west",
            "south",
            "take easter egg",
            "south",
            "drop mutex",
            "drop whirled peas",
            "drop cake",
            "drop space law space brochure",
            "drop loom",
            "drop hologram",
            "drop manifold",
            "drop easter egg",
    };

    public Cryostasis(long[] program) {

        for (List<String> items : enumerations()) {
            System.out.println("Testing " + items);

            this.droid = new IntCodeComputer(1_000_000, program);
            printOutput(droid.run());
            for (String cmd : prerequisite) {
                execute(cmd);
            }
            for (String item : items) {
                execute(item);
            }
            if (execute("south")) {
                break;
            }
        }
    }

    public List<List<String>> enumerations() {
        String[] items = new String[] {
                "take mutex", "take whirled peas", "take cake", "take space law space brochure",
                "take loom", "take hologram", "take manifold", "take easter egg"
        };

        List<List<String>> res = new ArrayList<>();
        res.add(new ArrayList<>());
        for (String item : items) {
            int size = res.size();
            for (int i = 0; i < size; i++) {
                List<String> comb = new ArrayList<>(res.get(i));
                comb.add(item);
                res.add(comb);
            }
        }
        return res;
    }

    private boolean execute(String cmd) {
        System.out.println(cmd);
        long[] input = new long[cmd.length() + 1];
        for (int i = 0; i < cmd.length(); i++) {
            input[i] = cmd.charAt(i);
        }
        input[cmd.length()] = '\n';
        LinkedList<Long> output = droid.run(input);
        return printOutput(output);
    }

    private boolean printOutput(LinkedList<Long> output) {
        StringBuilder sb = new StringBuilder();
        while (!output.isEmpty()) {
            sb.append((char) output.poll().byteValue());
        }
        System.out.println(sb);
        return !sb.toString().contains("lighter") && !sb.toString().contains("heavier");
    }


    public static void main(String[] args) {
        try {
            long[] program = AocInputReader.readIntCodeProgram("day25/input");
            Cryostasis cryostasis = new Cryostasis(program);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
