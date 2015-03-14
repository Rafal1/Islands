import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    private static Integer INITIAL_LENGTH_LIMIT = 100;
    public static Map<Integer, ArrayList<Point>> findedIslands = new ConcurrentHashMap<Integer, ArrayList<Point>>(INITIAL_LENGTH_LIMIT, new Float(0.8));
//    public static Set<Point> findedIslands = Collections.newSetFromMap(new ConcurrentHashMap<Point, Boolean>(INITIAL_LENGTH_LIMIT, new Float(0.8)));
//    private static Integer MAX_THREAD_NUMBER = 20;

    public static void main(String[] args) {
        Byte[][] simpleInpt = { //todo input form file
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

        System.out.println("x");
    }


//    public static void searchForIslands(Byte[][] si) {
//}

}
