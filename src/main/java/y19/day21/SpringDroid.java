package y19.day21;

import y19.day5.IntCodeComputer;
import util.AocInputReader;

import java.util.LinkedList;

public class SpringDroid {

    private IntCodeComputer computer;
    public SpringDroid(long[] program) {
        computer = new IntCodeComputer(1_000_000, program);
        print(computer.run());
    }

    public void walk() {
        String[] cmdList = new String[] {
                "NOT A J\n",
                "NOT B T\n",
                "OR T J\n",
                "NOT C T\n",
                "OR T J\n",
                "AND D J\n",
        };

        for (String cmd : cmdList) {
            computer.run(getInput(cmd));
        }

        print(computer.run(getInput("WALK\n")));
    }

    public void run() {
        String[] cmdList = new String[] {
                "NOT A J\n",
                "NOT B T\n",
                "OR T J\n",
                "NOT C T\n",
                "OR T J\n",
                "AND D J\n",
                "NOT E T\n",
                "NOT T T\n",
                "OR H T\n",
                "AND T J\n"
        };

        for (String cmd : cmdList) {
            computer.run(getInput(cmd));
        }

        print(computer.run(getInput("RUN\n")));
    }

    private long[] getInput(String str) {
        long[] input = new long[str.length()];
        for (int i = 0; i < input.length; i++) {
            input[i] = str.charAt(i);
        }
        return input;
    }

    private void print(LinkedList<Long> status) {
        StringBuilder sb = new StringBuilder();
        for (long num : status) {
            if (num > Byte.MAX_VALUE) {
                sb.append(num);
            } else {
                sb.append((char) num);
            }
        }
        System.out.println(sb);
    }

    public static void main(String[] args) {
        long[] program = AocInputReader.readIntCodeProgram("day21/input");
        new SpringDroid(program).walk();
        new SpringDroid(program).run();
    }
}
