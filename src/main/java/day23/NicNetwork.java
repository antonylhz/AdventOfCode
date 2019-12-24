package day23;

import day5.IntCodeComputer;
import util.AocInputReader;

import java.util.*;

public class NicNetwork {

    private Nic[] computers;
    private Nat nat;
    private Map<Integer, LinkedList<Packet>> history;

    public NicNetwork(long[] program, int n) {
        this.computers = new Nic[n];
        this.nat = new Nat(program, n, this);
        for (int i = 0; i < n; i++) {
            computers[i] = new Nic(this, nat, program, i);
        }
        this.history = new HashMap<>();
    }

    public void run() {
        for (int i = 0; i < 300; i++) {
            for (Nic computer : computers) {
                computer.run();
            }
            nat.maybeResume();
        }
    }

    public void sendPacket(int address, Packet packet) {
        if (address == 255) {
            nat.receivePacket(packet);
        } else if (address < computers.length) {
            computers[address].receivePacket(packet);
        }
        history.putIfAbsent(address, new LinkedList<>());
        history.get(address).offer(packet);
    }

    public static void main(String[] args) {
        try {
            long[] program = AocInputReader.readIntCodeProgram("day23/input");
            NicNetwork network = new NicNetwork(program, 50);
            network.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class Packet {
    long x;
    long y;
    public Packet(long x, long y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

class Nic extends IntCodeComputer {
    private NicNetwork network;
    private int address;
    private boolean initialized;
    private LinkedList<Packet> packets;
    private Nat nat;

    public Nic(NicNetwork network, Nat nat, long[] data, int address) {
        super(1_000_000, data);
        this.network = network;
        this.address = address;
        this.initialized = false;
        this.nat = nat;
        this.packets = new LinkedList<>();
    }

    public void receivePacket(Packet packet) {
        this.packets.offer(packet);
    }

    public void run() {
        boolean idle = true;
        LinkedList<Long> output;
        if (!initialized) {
            output = run(address);
            initialized = true;
            idle = false;
        } else {
            Packet packet = packets.poll();
            if (packet == null) {
                output = run(-1);
            } else {
                output = run(packet.x, packet.y);
                idle = false;
            }
        }
        while (!output.isEmpty()) {
            int address = output.poll().intValue();
            Packet packet = new Packet(output.poll(), output.poll());
            network.sendPacket(address, packet);
            idle = false;
        }
        if (idle) {
            nat.reportIdle(address);
        }
    }
}


class Nat extends IntCodeComputer {
    private NicNetwork network;
    private Packet packet;
    private int n;
    private Set<Integer> idleNic;

    public Nat(long[] data, int n, NicNetwork network) {
        super(1_000_000, data);
        this.n = n;
        this.network = network;
        this.idleNic = new HashSet<>();
    }

    public void receivePacket(Packet packet) {
        this.packet = packet;
    }

    public void reportIdle(int address) {
        idleNic.add(address);
    }

    public void maybeResume() {
        if (idleNic.size() == n) {
            network.sendPacket(0, packet);
            System.out.println("Resume Network: " + packet);
        }
        idleNic.clear();
    }

}