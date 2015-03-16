import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    private static Integer INITIAL_LENGTH_LIMIT = 100;
    public static Map<Integer, ArrayList<Point>> findedIslands = new ConcurrentHashMap<Integer, ArrayList<Point>>(INITIAL_LENGTH_LIMIT, new Float(0.8));
    public static void main(String[] args) {
        String inputFileName = args[0];
        try {
            BufferedReader bReadet = new BufferedReader(new FileReader(inputFileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//                Cursor cu = new Cursor();
//                cu.setStramStructure(simpleInpt);
        //        cu.start();

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
