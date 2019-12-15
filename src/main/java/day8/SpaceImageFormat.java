package day8;

import util.AocInputReader;

import java.util.*;

public class SpaceImageFormat {

    private int width;
    private int height;
    private int layers;

    private int[][][] image;
    private int[][] alignedImage;
    private List<Map<Integer, Integer>> layerCountMaps;

    public SpaceImageFormat(int[] data, int width, int height) {
        this.width = width;
        this.height = height;
        this.layers = data.length / width / height;
        this.image = new int[layers][height][width];
        this.alignedImage = new int[height][width];
        this.layerCountMaps = new ArrayList<>(layers);
        for (int l = 0; l < layers; l++) {
            Map<Integer, Integer> countMap = new HashMap<>();
            for (int r = 0; r < height; r++)
                for (int c = 0; c < width; c++) {
                    int index = l * width * height + r * width + c;
                    image[l][r][c] = data[index];
                    if (l == 0) {
                        alignedImage[r][c] = image[l][r][c];
                    } else {
                        alignedImage[r][c] = determineColor(alignedImage[r][c], image[l][r][c]);
                    }
                    countMap.put(data[index],
                            countMap.getOrDefault(data[index], 0) + 1);
                }
            layerCountMaps.add(countMap);
        }
    }

    public int findFewestZeroLayer() {
        int fewest = Integer.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < layers; i++) {
            Map<Integer, Integer> countMap = layerCountMaps.get(i);
            if (countMap.containsKey(0) && countMap.get(0) < fewest) {
                index = i;
                fewest = countMap.get(0);
            }
        }
        Map<Integer, Integer> layer = layerCountMaps.get(index);
        return layer.get(1) * layer.get(2);
    }

    public void printAlignedImage() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (alignedImage[r][c] == 0) {
                    sb.append(" ");
                } else {
                    sb.append(alignedImage[r][c]);
                }
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    private int determineColor(int original, int layerColor) {
        if (original == 2) return layerColor;
        else return original;
    }

    public static void main(String[] args) {

        String str = AocInputReader.readLines("day8/input")[0];
        int[] data = new int[str.length()];
        for (int i = 0; i < data.length; i++) {
            data[i] = str.charAt(i) - '0';
        }
        SpaceImageFormat spaceImageFormat =
                new SpaceImageFormat(data, 25, 6);
        System.out.println(spaceImageFormat.findFewestZeroLayer());
        spaceImageFormat.printAlignedImage();
    }

}
