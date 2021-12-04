package y19.day24;

import util.AocInputReader;

import java.util.*;

public class PlanetDiscord {

    private LinkedList<BugGrid> gridList;
    private int height, width;

    public PlanetDiscord(char[][] matrix) {
        this.height = matrix.length;
        this.width = matrix[0].length;
        this.gridList = new LinkedList<>();
        gridList.add(new BugGrid(matrix, 0));
    }

    public void advanceTime() {
        assert gridList.peek() != null;
        BugGrid newHead = new BugGrid(gridList.peekFirst().level - 1);
        BugGrid newTail = new BugGrid(gridList.peekLast().level + 1);
        Iterator<BugGrid> itr = gridList.iterator();
        BugGrid prev = newHead;
        BugGrid cur = itr.next();
        while (itr.hasNext()) {
            BugGrid next = itr.next();
            cur.countBugsInRecursiveSpace(next, prev);
            prev = cur;
            cur = next;
        }
        cur.countBugsInRecursiveSpace(newTail, prev);
        if (gridList.peekFirst().calcBiodiversityRating() > 0) {
            gridList.offerFirst(newHead);
        }
        if (gridList.peekLast().calcBiodiversityRating() > 0) {
            gridList.offerLast(newTail);
        }
        int bugs = 0;
        for (BugGrid grid : gridList) {
            bugs += grid.updateGrid();
            grid.printGrid();
        }
        System.out.println("Total bugs: " + bugs + "\n");
    }

    public void advance200Minutes() {
        gridList.offerFirst(new BugGrid(-1));
        gridList.offerLast(new BugGrid(1));
        for (int i = 1; i <= 200; i++) {
            System.out.println(i + " minutes:");
            advanceTime();
        }
    }

    class BugGrid {
        private final int[][] dir = new int[][] {
                {0, 1},
                {0, -1},
                {1, 0},
                {-1, 0}
        };

        private int level;
        private boolean[][] grid;
        private int[][] bugCount;
        public BugGrid(char[][] matrix, int level) {
            this.level = level;
            this.grid = new boolean[height][width];
            this.bugCount = new int[height][width];
            for (int r = 0; r < height; r++)
                for (int c = 0; c < width; c++) {
                    if (matrix[r][c] == '#') {
                        grid[r][c] = true;
                    }
                }
        }

        public BugGrid(int level) {
            this.level = level;
            this.grid = new boolean[height][width];
            this.bugCount = new int[height][width];
        }

        private void countBugsInNonRecursiveSpace() {
            bugCount = new int[height][width];
            for (int r = 0; r < height; r++)
                for (int c = 0; c < width; c++) {
                    for (int[] d : dir) {
                        int nr = r + d[0], nc = c + d[1];
                        if (nr >= 0 && nr < height && nc >= 0 && nc < width) {
                            bugCount[r][c] += grid[nr][nc] ? 1 : 0;
                        }
                    }
                }
        }

        private void countBugsInRecursiveSpace(BugGrid innerGrid, BugGrid outerGrid) {
            bugCount = new int[height][width];
            int[] outerBugs = outerGrid.getEdgesToInnerLevel();
            int[] innerBugs = innerGrid.getEdgesToOuterLevel();
            for (int r = 0; r < height; r++)
                for (int c = 0; c < width; c++) {
                    if (r == 2 && c == 2) continue;
                    for (int i = 0; i < dir.length; i++) {
                        int[] d = dir[i];
                        int nr = r + d[0], nc = c + d[1];
                        if (nr < 0 || nr >= height || nc < 0 || nc >= width) {
                            bugCount[r][c] += outerBugs[i];
                        } else if (nr == 2 && nc == 2) {
                            bugCount[r][c] += innerBugs[i];
                        } else {
                            bugCount[r][c] += grid[nr][nc] ? 1 : 0;
                        }
                    }
                }
        }

        public int updateGrid() {
            int bugs = 0;
            boolean[][] newGrid = new boolean[height][width];
            for (int r = 0; r < height; r++)
                for (int c = 0; c < width; c++) {
                    if (r == 2 && c == 2) continue;
                    if (grid[r][c]) {
                        newGrid[r][c] = bugCount[r][c] == 1;
                    } else {
                        newGrid[r][c] = bugCount[r][c] == 1 || bugCount[r][c] == 2;
                    }
                    bugs += newGrid[r][c] ? 1 : 0;
                }
            grid = newGrid;
            return bugs;
        }

        private int[] getEdgesToInnerLevel() {
            int[] res = new int[dir.length];
            int r = 2, c = 2;
            for (int i = 0; i < dir.length; i++) {
                res[i] = grid[r + dir[i][0]][c + dir[i][1]] ? 1 : 0;
            }
            return res;
        }

        private int[] getEdgesToOuterLevel() {
            int[] res = new int[dir.length];
            for (int i = 0; i < dir.length; i++) {
                int[] d = dir[i];
                if (d[0] == -1 || d[0] == 1) {
                    int r = d[0] == 1 ? 0: height - 1;
                    for (int c = 0; c < width; c++) {
                        res[i] += grid[r][c] ? 1 : 0;
                    }
                } else {
                    int c = d[1] == 1 ? 0 : width - 1;
                    for (int r = 0; r < height; r++) {
                        res[i] += grid[r][c] ? 1 : 0;
                    }
                }
            }
            return res;
        }

        private int calcBiodiversityRating() {
            int res = 0;
            for (int r = height - 1; r >= 0; r--)
                for (int c = width - 1; c >= 0; c--) {
                    res *= 2;
                    if (grid[r][c]) {
                        res++;
                    }
                }
            return res;
        }

        private void printGrid() {
            StringBuilder sb = new StringBuilder();
            sb.append("level: ").append(level).append("\n");
            for (int r = 0; r < height; r++) {
                for (int c = 0; c < width; c++) {
                    sb.append(grid[r][c] ? "#" : ".");
                }
                sb.append("\n");
            }
            System.out.println(sb);
        }

        public void findDupBiodiversityInNonRecursiveSpace() {
            System.out.println(calcBiodiversityRating());
            printGrid();
            Set<Integer> set = new HashSet<>();
            set.add(calcBiodiversityRating());
            while (true) {
                countBugsInNonRecursiveSpace();
                updateGrid();
                int biodiversity = calcBiodiversityRating();
                System.out.println(biodiversity);
                printGrid();
                if (set.contains(biodiversity)) {
                    return;
                }
                set.add(biodiversity);
            }
        }
    }

    public static void main(String[] args) {
        try {
            char[][] matrix = AocInputReader.readCharMatrix("day24/input");

            System.out.println("===PART I===");
            new PlanetDiscord(matrix).gridList.peek().findDupBiodiversityInNonRecursiveSpace();

            System.out.println("===PART II===");
            new PlanetDiscord(matrix).advance200Minutes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}