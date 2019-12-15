package day5;

import util.AocInputReader;

import java.util.*;

public class IntCodeComputer {

    private final long[] data;
    private int ip;
    private int relativeBase;
    public boolean isHalted;
    private LinkedList<Long> output;

    public IntCodeComputer(int memorySize, long[] data) {
        this.data = new long[memorySize];
        System.arraycopy(data, 0, this.data, 0, data.length);
        this.ip = 0;
        this.relativeBase = 0;
        this.isHalted = false;
        this.output = new LinkedList<>();
    }

    public LinkedList<Long> run(final LinkedList<Long> inputs) {
        if (isHalted) {
            return output;
        }

        outerLoop:
        while (ip < data.length) {
            switch ((int) data[ip] % 100) {
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
                case 9:
                    adjustRelativeBase();
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

    private long param(int opcodeIndex, int paramOffset) {
        long opcode = data[opcodeIndex];
        int paramIndex = opcodeIndex + paramOffset;
        int radix = 10 * (int) Math.pow(10, paramOffset);
        int paramMode = (int) (opcode % (radix * 10)) / radix;
        switch (paramMode) {
            case 0: // address mode
                return data[(int) data[paramIndex]];
            case 1: // immediate mode
                return data[paramIndex];
            case 2: // relative mode
                return data[relativeBase + (int) data[paramIndex]];
            default:
                throw new IllegalArgumentException("Invalid param mode: " + paramMode);
        }
    }

    private long paramAddr(int opcodeIndex, int paramOffset) {
        long opcode = data[opcodeIndex];
        int paramIndex = opcodeIndex + paramOffset;
        int radix = 10 * (int) Math.pow(10, paramOffset);
        int paramMode = (int) (opcode % (radix * 10)) / radix;
        switch (paramMode) {
            case 0: // address mode
                return data[paramIndex];
            case 1: // immediate mode
                return paramIndex;
            case 2: // relative mode
                return relativeBase + (int) data[paramIndex];
            default:
                throw new IllegalArgumentException("Invalid param mode: " + paramMode);
        }
    }

    private void add() {
        data[(int) paramAddr(ip, 3)] =
                param(ip, 1) +
                        param(ip, 2);
        ip += 4;
    }

    private void multiply() {
        data[(int) paramAddr(ip, 3)] =
                param(ip, 1) *
                        param(ip, 2);
        ip += 4;
    }

    private void read(long input) {
        data[(int) paramAddr(ip, 1)] = input;
        ip += 2;
    }

    private void write() {
        output.add(param(ip, 1));
        ip += 2;
    }

    private void jumpIfTrue() {
        if (param(ip, 1) != 0) {
            ip = (int) param(ip, 2);
        } else {
            ip += 3;
        }
    }

    private void jumpIfFalse() {
        if (param(ip, 1) == 0) {
            ip = (int) param(ip, 2);
        } else {
            ip += 3;
        }
    }

    private void lessThan() {
        if (param(ip, 1) <
                param(ip, 2)) {
            data[(int) paramAddr(ip, 3)] = 1;
        } else {
            data[(int) paramAddr(ip, 3)] = 0;
        }
        ip += 4;
    }

    private void equals() {
        if (param(ip, 1) ==
                param(ip, 2)) {
            data[(int) paramAddr(ip, 3)] = 1;
        } else {
            data[(int) paramAddr(ip, 3)] = 0;
        }
        ip += 4;
    }

    private void adjustRelativeBase() {
        relativeBase += param(ip, 1);
        ip += 2;
    }

    public static void main(String[] args) {
        String[] tokens = AocInputReader.readLines("day5/input")[0].split(",");
        long[] data = new long[tokens.length];
        for (int i = 0; i < data.length; i++) {
            data[i] = Long.parseLong(tokens[i]);
        }
        LinkedList<Long> input = new LinkedList<>(Collections.singletonList(5L));
        System.out.println(new IntCodeComputer(data.length, data).run(input));
    }

}
