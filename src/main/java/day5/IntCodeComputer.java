package day5;

import java.io.File;
import java.net.URL;
import java.util.*;

public class IntCodeComputer {

    private final int[] data;
    private int ip;
    public boolean isHalted;
    private LinkedList<Integer> output;

    public IntCodeComputer(int[] data) {
        this.data = data;
        this.ip = 0;
        this.isHalted = false;
        this.output = new LinkedList<>();
    }

    public LinkedList<Integer> run(final LinkedList<Integer> inputs) {
        if (isHalted) {
            return output;
        }

        outerLoop:
        while (ip < data.length) {
            switch (data[ip] % 100) {
                case 1:
                    add();
                    break;
                case 2:
                    multiply();
                    break;
                case 3:
                    if (inputs.isEmpty()) {
                        // The program is out of input,
                        // hence pauses and wait for the next input
                        break outerLoop;
                    }
                    read(inputs.pollFirst());
                    break;
                case 4:
                    write();
                    break;
                case 5:
                    jumpIfTrue();
                    break;
                case 6:
                    jumpIfFalse();
                    break;
                case 7:
                    lessThan();
                    break;
                case 8:
                    equals();
                    break;
                case 99:
                    isHalted = true;
                    break outerLoop;
                default:
                    System.out.println("Bad opcode: " + data[ip]);
                    break outerLoop;
            }
        }
        return output;
    }

    private int param(int opcodeIndex, int paramOffset) {
        int opcode = data[opcodeIndex];
        int paramIndex = opcodeIndex + paramOffset;
        int radix = 10 * (int) Math.pow(10, paramOffset);
        int paramMode = (opcode % (radix * 10)) / radix;
        switch (paramMode) {
            case 0: // address mode
                return data[data[paramIndex]];
            case 1: // immediate mode
                return data[paramIndex];
            default:
                throw new IllegalArgumentException("Invalid param mode: " + paramMode);
        }
    }

    private void add() {
        data[data[ip + 3]] =
                param(ip, 1) +
                        param(ip, 2);
        ip += 4;
    }

    private void multiply() {
        data[data[ip + 3]] =
                param(ip, 1) *
                        param(ip, 2);
        ip += 4;
    }

    private void read(int input) {
        data[data[ip + 1]] = input;
        ip += 2;
    }

    private void write() {
        output.add(param(ip, 1));
        ip += 2;
    }

    private void jumpIfTrue() {
        if (param(ip, 1) != 0) {
            ip = param(ip, 2);
        } else {
            ip += 3;
        }
    }

    private void jumpIfFalse() {
        if (param(ip, 1) == 0) {
            ip = param(ip, 2);
        } else {
            ip += 3;
        }
    }

    private void lessThan() {
        if (param(ip, 1) <
                param(ip, 2)) {
            data[data[ip + 3]] = 1;
        } else {
            data[data[ip + 3]] = 0;
        }
        ip += 4;
    }

    private void equals() {
        if (param(ip, 1) ==
                param(ip, 2)) {
            data[data[ip + 3]] = 1;
        } else {
            data[data[ip + 3]] = 0;
        }
        ip += 4;
    }

    public static void main(String[] args) {
        try {
            URL url = IntCodeComputer.class.getClassLoader().getResource("day5/input");
            assert url != null;
            Scanner scanner = new Scanner(new File(url.getFile()));
            while (scanner.hasNextLine()) {
                String[] tokens = scanner.nextLine().split(",");
                int[] data = new int[tokens.length];
                for (int i = 0; i < data.length; i++) {
                    data[i] = Integer.parseInt(tokens[i]);
                }
                LinkedList<Integer> input = new LinkedList<>(Collections.singletonList(5));
                System.out.println(new IntCodeComputer(data).run(input));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
