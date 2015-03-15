import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    private static Integer INITIAL_LENGTH_LIMIT = 100;
    public static Map<Integer, ArrayList<Point>> findedIslands = new ConcurrentHashMap<Integer, ArrayList<Point>>(INITIAL_LENGTH_LIMIT, new Float(0.8));
    public static void main(String[] args) {

        //todo input form file for a larger matrixes (in a spare matrix format)
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

        System.out.println("Zadanie zostało wykonane pomyślnie");
        //showIslands
    }

    private void showIslands() {

    }

}
