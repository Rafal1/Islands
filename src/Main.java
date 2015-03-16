import java.util.Map;
import java.util.function.BiConsumer;

public class Main {
    public static void main(String[] args) {
        Byte[][] simpleInpt = {
                {0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 0, 0, 0, 1, 0, 0},
                {1, 1, 0, 0, 0, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 1, 1, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0},
                {1, 1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 1, 1, 0}
        };

        Cursor cu = new Cursor();
        cu.setStramStructure(simpleInpt);
        cu.start();
        Map<Integer, Integer> m = cu.getResult();
        showIslands(m);
        cu.prepareForNextUsage();
    }

    private static void showIslands(Map<Integer, Integer> m) {
        BiConsumer<Integer, Integer> biConsumer = (x, size) -> {
            String info = "An Island with id: " + x + " has " + size;
            if (size.equals(1)) {
                System.out.println(info + " element.");
            } else {
                System.out.println(info + " elements.");
            }
        };
        m.forEach(biConsumer);
    }

}
