package day22;

import util.AocInputReader;

public class SpaceCards {

    private long n;

    public SpaceCards(long n) {
        this.n = n;
    }

    public long shuffle(String[] techs, long index) {
        for (String tech : techs) {
            if (tech.equals("deal into new stack")) {
                index = dealIntoNewStack(index);
            } else if (tech.startsWith("cut")) {
                index = cut(index, Integer.parseInt(tech.split(" ")[1]));
            } else if (tech.startsWith("deal with increment")) {
                index = dealWithIncrement(index, Integer.parseInt(tech.split(" ")[3]));
            } else {
                throw new IllegalStateException("No such technique: " + tech);
            }
        }
        return index;
    }

    public long reverse_shuffle(String[] techs, long index) {
        for (int i = techs.length - 1; i >= 0; i--) {
            String tech = techs[i];
            if (tech.equals("deal into new stack")) {
                index = reverse_dealIntoNewStack(index);
            } else if (tech.startsWith("cut")) {
                index = reverse_cut(index, Integer.parseInt(tech.split(" ")[1]));
            } else if (tech.startsWith("deal with increment")) {
                index = reverse_dealWithIncrement(index, Integer.parseInt(tech.split(" ")[3]));
            } else {
                throw new IllegalStateException("No such technique: " + tech);
            }
        }
        return index;
    }

    public long dealIntoNewStack(long index) {
        return n - 1 - index;
    }

    public long reverse_dealIntoNewStack(long index) {
        return dealIntoNewStack(index);
    }

    public long cut(long index,int x) {
        return Math.floorMod(index - x, n);
    }

    public long reverse_cut(long index, int x) {
        return Math.floorMod(index + x, n);
    }

    public long dealWithIncrement(long index, int inc) {
        return Math.floorMod(index * inc, n);
    }

    public long reverse_dealWithIncrement(long index, int inc) {
        while (index % inc != 0) {
            index += n;
        }
        return index / inc;
    }

    public static void main(String[] args) {
        try {
            String[] lines = AocInputReader.readLines("day22/input");

            SpaceCards cards = new SpaceCards(10007);
            System.out.println(cards.shuffle(lines, 2019));

//            SpaceCards cards = new SpaceCards(119_315_717_514_047L);
//            long index = 2020;
//            for (long i = 0; i < 101741582076661L; i++) {
//                index = cards.reverse_shuffle(lines, index);
//            }
//            System.out.println(index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
