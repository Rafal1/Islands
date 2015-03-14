import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Rafał Zawadzki
 */
public class Cursor {
    // stream
    private Byte[][] streamStructure = null;
    private Boolean isWorking = false;
    public volatile Integer idCounter = -1;
    public volatile ArrayList<Integer> lostIDs = new ArrayList<Integer>();
    //    private Set<Point> granted = Collections.newSetFromMap(new ConcurrentHashMap<Point, Boolean>());
//    private ArrayList<Point> granted = new ArrayList<Point>(); //max size is 3
    private static Map<Point, ArrayList<Integer>> granted = new ConcurrentHashMap<Point, ArrayList<Integer>>();

    public void setStramStructure(Byte[][] stramStructure) {
        this.streamStructure = stramStructure;
    }

    public void start() {
        isWorking = true;
        if (streamStructure == null) {
            return; //todo error noStructerEstablishedException
        }
        for (int i = 0; i < streamStructure.length; i++) {
            for (int j = 0; j < streamStructure[i].length; j++) {
                switch (streamStructure[i][j]) {
                    case 0:
                        continue;
                    case 1:
                        Point tmpPoint = new Point();
                        tmpPoint.x = Long.valueOf(j);
                        tmpPoint.y = Long.valueOf(i);
                        ArrayList<Integer> grantedForPoint = granted.remove(tmpPoint);
                        if (grantedForPoint == null || grantedForPoint.isEmpty()) {
                            if (lostIDs.isEmpty()) {
                                tmpPoint.ID = ++idCounter; //todo uncertain
                            } else {
                                tmpPoint.ID = lostIDs.remove(0); //todo FIFO
                            }
                        } else {
                            Integer minGrantedID = Integer.MAX_VALUE; //todo 1 index island is out - MAX_VALUE
                            for (Integer pID : grantedForPoint) {
                                if (minGrantedID > pID) {
                                    minGrantedID = pID;
                                }
                            }
                            tmpPoint.ID = minGrantedID; //todo
                        }

                        ArrayList<Point> pointsOfIsland = Main.findedIslands.get(tmpPoint.ID);
                        if (pointsOfIsland != null) {
                            if (!pointsOfIsland.add(tmpPoint)) {
                                System.out.println("Not added Island part (to ArrayList structure)");
                            }
                            if (Main.findedIslands.replace(tmpPoint.ID, pointsOfIsland) == null) {
                                System.out.println("Not replaced Island part in an ArrayList structure)");
                            }
                        } else {
                            ArrayList<Point> newIslandPoints = new ArrayList<Point>();
                            newIslandPoints.add(tmpPoint);
                            if (Main.findedIslands.put(tmpPoint.ID, newIslandPoints) == null) {
                                System.out.println("Not added Island part (to Map structure)");
                            }
                        }

                        if (granted.remove(tmpPoint) == null) {
                            System.out.println("Error during a granted point removing");
                        }
                        if (j == 0) {
                            for (int m = 0; m < 2; m++) {
                                Point grantedPoint = new Point(Long.valueOf(j + m), Long.valueOf(i + 1), tmpPoint.ID);
                                ArrayList<Integer> grantedPointIDs = granted.get(grantedPoint);
                                if (grantedForPoint == null) {
                                    grantedPointIDs = new ArrayList<Integer>();
                                    grantedPointIDs.add(tmpPoint.ID);
                                    granted.put(grantedPoint, grantedPointIDs); //todo error
                                } else {
                                    grantedPointIDs.add(tmpPoint.ID); //todo error
                                    granted.replace(grantedPoint, grantedPointIDs); //todo error
                                }
                            }
                        } else if (j == (streamStructure[i].length - 1)) {
                            for (int m = 0; m < 2; m++) {
                                Point grantedPoint = new Point(Long.valueOf(j - m), Long.valueOf(i + 1), tmpPoint.ID);
                                ArrayList<Integer> grantedPointIDs = granted.get(grantedPoint);
                                if (grantedForPoint == null) {
                                    grantedPointIDs = new ArrayList<Integer>();
                                    grantedPointIDs.add(tmpPoint.ID);
                                    granted.put(grantedPoint, grantedPointIDs); //todo error
                                } else {
                                    grantedPointIDs.add(tmpPoint.ID); //todo error
                                    granted.replace(grantedPoint, grantedPointIDs); //todo error
                                }
                            }
                        } else {
                            for (int m = 0; m < 3; m++) {
                                Point grantedPoint = new Point(Long.valueOf(j - 1 + m), Long.valueOf(i + 1), tmpPoint.ID); //todo out of border
                                ArrayList<Integer> grantedPointIDs = granted.get(grantedPoint);
                                if (grantedForPoint == null) {
                                    grantedPointIDs = new ArrayList<Integer>();
                                    grantedPointIDs.add(tmpPoint.ID);
                                    granted.put(grantedPoint, grantedPointIDs); //todo error
                                } else {
                                    grantedPointIDs.add(tmpPoint.ID); //todo error
                                    granted.replace(grantedPoint, grantedPointIDs); //todo error
                                }
                            }
                        }
                    default:
                        continue;
                }
            }
        }

        isWorking = false;
    }

}
