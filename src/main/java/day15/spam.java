package day15;

import util.AocInputReader;

import java.util.*;

class intcode {
    long[] mem;
    int rel;
    int ip;
    boolean done;
    Queue<Long> input;
    long ans = -1;

    intcode(long[] in) {
        mem = new long[in.length];
        for (int i = 0; i < in.length; i++) {
            mem[i] = in[i];
        }
        rel = 0;
        ip = 0;
        done = false;
        input = new LinkedList<Long>();
    }

    public void pushInput(long a) {
        input.add(a);
    }

    public int get_pointer(int p, int mode) {
        if (mode == 0) {
            return (int) mem[p];
        } else if (mode == 1) {
            return p;
        } else if (mode == 2) {
            return (int) mem[p] + rel;
        } else {
            System.out.println("Napaka");
            System.out.println(Arrays.toString(mem));
            System.out.println("pointer " + ip);
            done = true;
            return -1;
        }
    }

    public void run() {
//		System.out.println(ip+" "+mem[ip]);
        int full = (int) mem[ip];
        int op = full % 100;
        int modea = (full / 100) % 10;
        int modeb = (full / 1000) % 10;
        int modec = (full / 10000) % 10;
        if (op == 99) {
            done = true;
        } else if (op == 1) {
            int pread1 = get_pointer(ip + 1, modea);
            int pread2 = get_pointer(ip + 2, modeb);
            int pwrite = get_pointer(ip + 3, modec);
            mem[pwrite] = mem[pread1] + mem[pread2];
            ip += 4;
        } else if (op == 2) {
            int pread1 = get_pointer(ip + 1, modea);
            int pread2 = get_pointer(ip + 2, modeb);
            int pwrite = get_pointer(ip + 3, modec);
            mem[pwrite] = mem[pread1] * mem[pread2];
            ip += 4;
        } else if (op == 3) {
            int pwrite = get_pointer(ip + 1, modea);
            mem[pwrite] = input.poll();
            ip += 2;
        } else if (op == 4) {
            int pread1 = get_pointer(ip + 1, modea);
//			System.out.println(mem[pread1]);
            ans = mem[pread1];
            ip += 2;
        } else if (op == 5) {
            int pread1 = get_pointer(ip + 1, modea);
            int pwrite = get_pointer(ip + 2, modeb);
            if (mem[pread1] != 0) {
                ip = (int) mem[pwrite];
            } else {
                ip += 3;
            }
        } else if (op == 6) {
            int pread1 = get_pointer(ip + 1, modea);
            int pwrite = get_pointer(ip + 2, modeb);
            if (mem[pread1] == 0) {
                ip = (int) mem[pwrite];
            } else {
                ip += 3;
            }
        } else if (op == 7) {
            int pread1 = get_pointer(ip + 1, modea);
            int pread2 = get_pointer(ip + 2, modeb);
            int pwrite = get_pointer(ip + 3, modec);
            if (mem[pread1] < mem[pread2]) {
                mem[pwrite] = 1;
            } else {
                mem[pwrite] = 0;
            }
            ip += 4;
        } else if (op == 8) {
            int pread1 = get_pointer(ip + 1, modea);
            int pread2 = get_pointer(ip + 2, modeb);
            int pwrite = get_pointer(ip + 3, modec);
            if (mem[pread1] == mem[pread2]) {
                mem[pwrite] = 1;
            } else {
                mem[pwrite] = 0;
            }
            ip += 4;
        } else if (op == 9) {
            int pread1 = get_pointer(ip + 1, modea);
            rel += mem[pread1];
            ip += 2;
        } else {
            System.out.println("Napaka");
            System.out.println(ip);
            System.out.println(mem[ip]);
            done = true;
        }
    }

    public long getOutput() {
        ans = -1;
        while (true) {
            run();
            if (ans != -1) {
                return ans;
            }
        }
    }
}

class point {
    int x, y;

    point(int a, int b) {
        x = a;
        y = b;
    }
}

public class spam {
    public static void print(int[][] grid) {
        for (int[] q : grid) {
            for (int i : q) {
                if (i == 0) {
                    System.out.print("#");
                } else if (i == 1) {
                    System.out.print(" ");
                } else {
                    System.out.print("O");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        long[] mem = new long[1000000];
        String[] tokens = AocInputReader.readLines("day15/input")[0].split(",");
        for (int p = 0; p < tokens.length; p++) {
            mem[p] = Integer.parseInt(tokens[p]);
        }

        intcode pc = new intcode(mem);
        int[] dx = {0, 0, 0, -1, 1};
        int[] dy = {0, -1, 1, 0, 0};
        int[][] grid = new int[100][100];
        int px = 50;
        int py = 50;
        for (int i = 0; i < 10000000; i++) {
            int dir = 1 + (int) Math.floor(4 * Math.random());
            pc.pushInput(dir);
            int ans = (int) pc.getOutput();
            grid[py + dy[dir]][px + dx[dir]] = ans;
            if (ans > 0) {
                py += dy[dir];
                px += dx[dir];
                if (ans == 2) {
                    System.out.println("Found Oxygen System: " + py + "," + px);
                    //break;
                }
            }
        }
        print(grid);

        py = 34; px = 38;

        int[][] dist = new int[100][100];
        for (int i = 0; i < 100; i++) {
            Arrays.fill(dist[i], -1);
        }
        dist[py][px] = 0;
        Queue<point> bfs = new LinkedList<>();
        bfs.add(new point(px, py));
        while (!bfs.isEmpty()) {
            point t = bfs.poll();
            int d = dist[t.y][t.x];
            System.out.println(d + " " + t.x + " " + t.y);
            for (int i = 1; i <= 4; i++) {
                if (dist[t.y + dy[i]][t.x + dx[i]] >= 0) {
                    continue;
                }
                if (grid[t.y + dy[i]][t.x + dx[i]] > 0) {
                    bfs.add(new point(t.x + dx[i], t.y + dy[i]));
                    dist[t.y + dy[i]][t.x + dx[i]] = 1 + d;
                }
            }
        }
    }
}
