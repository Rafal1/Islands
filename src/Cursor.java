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
    private static Map<String, ArrayList<Integer>> granted = new ConcurrentHashMap<String, ArrayList<Integer>>();

    public void setStramStructure(Byte[][] stramStructure) {
        this.streamStructure = stramStructure;
    }

    public void start() {
        isWorking = true;
        if (streamStructure == null) {
            return; //todo error noStructerEstablishedException
        }
        for (Integer i = 0; i < streamStructure.length; i++) {
            for (Integer j = 0; j < streamStructure[i].length; j++) {
                switch (streamStructure[i][j]) {
                    case 0:
                        continue;
                    case 1:
                        Integer dedicatedIslandID;
//                        GrantedPoint tmpGrantedPoint = new GrantedPoint(new Long(j.longValue()), new Long(i.longValue()));
                        String grantedPointKey = String.valueOf(j) + '&' + String.valueOf(i);
                        Boolean test = granted.containsKey(grantedPointKey);
                        ArrayList<Integer> grantedForPoint = granted.remove(grantedPointKey);
                        if (grantedForPoint == null || grantedForPoint.isEmpty()) {
                            if (lostIDs.isEmpty()) {
                                dedicatedIslandID = ++idCounter; //todo uncertain
                            } else {
                                dedicatedIslandID = lostIDs.remove(0); //todo FIFO error
                            }
                        } else {
                            Integer minGrantedID = Integer.MAX_VALUE; //todo 1 index island is out - MAX_VALUE
                            for (Integer pID : grantedForPoint) {
                                if (minGrantedID > pID) {
                                    minGrantedID = pID;
                                }
                            }
                            dedicatedIslandID = minGrantedID; //todo
                        }

                        Point tmpPoint = new Point(j.longValue(), i.longValue(), dedicatedIslandID);
                        ArrayList<Point> pointsOfIsland = Main.findedIslands.get(dedicatedIslandID);
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

//                        if (granted.remove(grantedPointKey) == null) {
//                            System.out.println("Error during a granted point removing");
//                        }
                        if (j == 0) {
                            for (int m = 0; m < 2; m++) {
//                                GrantedPoint grantedPoint = new GrantedPoint(Long.valueOf(j + m), Long.valueOf(i + 1));
                                String keyOfGrantingPoint = String.valueOf(j + m) + '&' + String.valueOf(i + 1);
                                ArrayList<Integer> grantedPointIDs = granted.get(keyOfGrantingPoint);
                                if (grantedPointIDs == null) {
                                    grantedPointIDs = new ArrayList<Integer>();
                                    grantedPointIDs.add(dedicatedIslandID);
                                    granted.put(keyOfGrantingPoint, grantedPointIDs); //todo error
                                } else {
                                    grantedPointIDs.add(dedicatedIslandID); //todo error
                                    granted.replace(keyOfGrantingPoint, grantedPointIDs); //todo error
                                }
                            }
                        } else if (j == (streamStructure[i].length - 1)) {
                            for (int m = 0; m < 2; m++) {
//                                GrantedPoint grantedPoint = new GrantedPoint(Long.valueOf(j - m), Long.valueOf(i + 1));
                                String keyOfGrantingPoint = String.valueOf(j - m) + '&' + String.valueOf(i + 1);
                                ArrayList<Integer> grantedPointIDs = granted.get(keyOfGrantingPoint);
                                if (grantedPointIDs == null) {
                                    grantedPointIDs = new ArrayList<Integer>();
                                    grantedPointIDs.add(tmpPoint.ID);
                                    granted.put(keyOfGrantingPoint, grantedPointIDs); //todo error
                                } else {
                                    grantedPointIDs.add(tmpPoint.ID); //todo error
                                    granted.replace(keyOfGrantingPoint, grantedPointIDs); //todo error
                                }
                            }
                        } else {
                            for (int m = 0; m < 3; m++) {
//                                GrantedPoint grantedPoint = new GrantedPoint(Long.valueOf(j - 1 + m), Long.valueOf(i + 1)); //todo out of border
                                String keyOfGrantingPoint = String.valueOf(j - 1 + m) + '&' + String.valueOf(i + 1);
                                ArrayList<Integer> grantedPointIDs = granted.get(keyOfGrantingPoint);
                                if (grantedPointIDs == null) {
                                    grantedPointIDs = new ArrayList<Integer>();
                                    grantedPointIDs.add(tmpPoint.ID);
                                    granted.put(keyOfGrantingPoint, grantedPointIDs); //todo error
                                } else {
                                    grantedPointIDs.add(tmpPoint.ID); //todo error
                                    granted.replace(keyOfGrantingPoint, grantedPointIDs); //todo error
                                }
                            }
                        }
                }
            }
        }

        isWorking = false;
    }

}
