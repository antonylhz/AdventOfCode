package y19.day14;

import util.AocInputReader;

import java.util.*;

public class NanoFactory {

    private Map<String, Chemical> map;
    private Map<String, Long> leftover;

    public NanoFactory(String[] data) {
        Chemical[] chemicals = new Chemical[data.length];
        this.map = new HashMap<>();
        for (int i = 0; i < chemicals.length; i++) {
            chemicals[i] = new Chemical(data[i]);
            map.put(chemicals[i].name, chemicals[i]);
        }
        this.leftover = new HashMap<>();
    }

    public int produceOneFuel() {
        LinkedList<Chemical> requiredChemicals = new LinkedList<>();
        LinkedList<Integer> requiredQuantities = new LinkedList<>();
        requiredChemicals.offer(map.get("FUEL"));
        requiredQuantities.offer(1);
        int res = 0;
        while (!requiredChemicals.isEmpty() && !requiredQuantities.isEmpty()) {
            Chemical chemical = requiredChemicals.pollFirst();
            int quantity = requiredQuantities.pollFirst();
            if (leftover.getOrDefault(chemical.name, 0L) > 0) {
                long use = Math.min(leftover.get(chemical.name), quantity);
                leftover.put(chemical.name, leftover.get(chemical.name) - use);
                quantity -= use;
                if (quantity == 0) continue;
            }
            int weight = (quantity % chemical.outputQuantity == 0 ? 0 : 1) +
                    quantity / chemical.outputQuantity;
            leftover.put(chemical.name, (long) (weight * chemical.outputQuantity - quantity));
            for (int i = 0; i < chemical.inputQuantities.length; i++) {
                String itemName = chemical.inputChemicals[i];
                int itemQuantity = chemical.inputQuantities[i] * weight;
                if (itemName.equals("ORE")) {
                    res += itemQuantity;
                } else {
                    requiredChemicals.offer(map.get(itemName));
                    requiredQuantities.offer(itemQuantity);
                }
            }
        }
        return res;
    }

    public long maxFuelFrom(long ore) {
        int fuel = 0;
        while ((ore -= produceOneFuel()) > 0) {
            fuel++;
        }
        return fuel;
    }

    public static void main (String[] args) {
        String[] data = AocInputReader.readLines("day14/input");
        NanoFactory nanoFactory = new NanoFactory(data);
        // System.out.println(nanoFactory.produceOneFuel(Collections.singleton("ORE")));
        System.out.println(nanoFactory.maxFuelFrom(1000000000000L));
    }

}

class Chemical {
    String name;
    String[] inputChemicals;
    int[] inputQuantities;
    int outputQuantity;

    public Chemical(String str) {
        String[] equation = str.split("=>");
        String[] inputRequirements = equation[0].split(",");
        this.inputChemicals = new String[inputRequirements.length];
        this.inputQuantities = new int[inputRequirements.length];
        for (int i = 0; i < inputRequirements.length; i++) {
            String[] inputDetails = inputRequirements[i].trim().split(" ");
            inputQuantities[i] = Integer.parseInt(inputDetails[0]);
            inputChemicals[i] = inputDetails[1];
        }
        String[] outputDetails = equation[1].trim().split(" ");
        this.outputQuantity = Integer.parseInt(outputDetails[0]);
        this.name = outputDetails[1];
    }

}
