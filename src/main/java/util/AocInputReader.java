package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AocInputReader {

    public static String[] readLines(String fileName) {
        URL url = AocInputReader.class.getClassLoader().getResource(fileName);
        assert url != null;
        List<String> data = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(new File(url.getFile()));
            while (scanner.hasNextLine()) {
                data.add(scanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String[] res = new String[data.size()];
        return data.toArray(res);
    }

    public static char[][] readCharMatrix(String fileName) {
        String[] lines = readLines(fileName);
        int height = lines.length, width = 0;
        for (String line : lines) width = Math.max(width, line.length());
        char[][] res = new char[height][width];
        for (int r = 0; r < height; r++)
            for (int c = 0; c < lines[r].length(); c++) {
                res[r][c] = lines[r].charAt(c);
            }
        return res;
    }


}
