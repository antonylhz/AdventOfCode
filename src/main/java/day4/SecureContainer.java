package day4;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SecureContainer {

    private static Set<Integer> findPasswords(int start, int end) {
        Set<Integer> res = new HashSet<>();
        for (int num = start; num <= end; num ++) {
            if (isPassword(num)) {
                res.add(num);
            }
        }
        return res;
    }

    private static boolean isPassword(int num) {
        Map<Integer, Integer> map = new HashMap<>();
        int digit = num % 10;
        map.put(digit, 1);
        num /= 10;
        while (num != 0) {
            int newDigit = num % 10;
            map.put(newDigit, map.getOrDefault(newDigit, 0) + 1);
            if (newDigit > digit) {
                return false;
            }
            num /= 10;
            digit = newDigit;
        }
        for (int v : map.values()) {
            // if (v > 1) return true; // part 1
            if (v == 2) return true; // part 2
        }
        return false;
    }

    public static void main (String[] args) {
        System.out.println(findPasswords(402328, 864247).size());
    }
}
