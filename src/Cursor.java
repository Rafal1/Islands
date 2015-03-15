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
    public volatile ArrayList<Integer> idsToRemove = new ArrayList<Integer>();

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
                        String zeroGrantedPointKey = String.valueOf(j) + '&' + String.valueOf(i);
                        granted.remove(zeroGrantedPointKey);
                        continue;
                    case 1:
                        Integer dedicatedIslandID;
//                        GrantedPoint tmpGrantedPoint = new GrantedPoint(new Long(j.longValue()), new Long(i.longValue()));
                        String grantedPointKey = String.valueOf(j) + '&' + String.valueOf(i);
//                        Boolean test = granted.containsKey(grantedPointKey);
                        ArrayList<Integer> grantedForPoint = granted.remove(grantedPointKey);
                        if (grantedForPoint == null || grantedForPoint.isEmpty()) {
                            if (lostIDs.isEmpty()) {
                                dedicatedIslandID = ++idCounter; //todo uncertain
                            } else {
                                dedicatedIslandID = lostIDs.remove(0); //todo FIFO, error
                            }
                        } else {
                            Integer minGrantedID = grantedForPoint.get(0); //todo 1 index island is out - MAX_VALUE
                            Integer indexOfMinGrantedID = 0;
                            for (Integer k = 1; k < grantedForPoint.size(); k++) {
                                Integer currentID = grantedForPoint.get(k);
                                if (minGrantedID > currentID) {
                                    minGrantedID = currentID;
                                    idsToRemove.add(grantedForPoint.get(indexOfMinGrantedID));
                                    indexOfMinGrantedID = k;
                                } else if(currentID != minGrantedID) {
                                    idsToRemove.add(currentID);
                                }
                            }
                            dedicatedIslandID = minGrantedID; //todo

//                            for(Integer rID : idsToRemove) {
//                                removeID(rID, dedicatedIslandID);
//                            }
//                            idsToRemove.clear();
                        }

                        Point tmpPoint = new Point(j.longValue(), i.longValue());
                        ArrayList<Point> pointsOfIsland = Main.findedIslands.get(dedicatedIslandID);
                        if (pointsOfIsland != null) {
                            if (!pointsOfIsland.add(tmpPoint)) {
                                System.out.println("Not added Island part (to ArrayList structure)");
                            }
                            if (Main.findedIslands.replace(dedicatedIslandID, pointsOfIsland) == null) {
                                System.out.println("Not replaced Island part in an ArrayList structure)");
                            }
                        } else {
                            ArrayList<Point> newIslandPoints = new ArrayList<Point>();
                            newIslandPoints.add(tmpPoint);
                            Main.findedIslands.put(dedicatedIslandID, newIslandPoints);
                        }

                        for(Integer rID : idsToRemove) {
                            removeID(rID, dedicatedIslandID);
                        }
                        idsToRemove.clear();


                        if (j < (streamStructure[i].length - 1)) {
                            String nextLineGrantedKey = String.valueOf(j + 1) + '&' + String.valueOf(i);
                            ArrayList<Integer> grantedPointIDs = granted.get(nextLineGrantedKey);
                            if (grantedPointIDs == null) {
                                grantedPointIDs = new ArrayList<Integer>();
                                grantedPointIDs.add(dedicatedIslandID);
                                granted.put(nextLineGrantedKey, grantedPointIDs); //todo error
                            } else {
                                grantedPointIDs.add(dedicatedIslandID); //todo error
                                granted.replace(nextLineGrantedKey, grantedPointIDs); //todo error
                            }
                        }

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
                                    grantedPointIDs.add(dedicatedIslandID);
                                    granted.put(keyOfGrantingPoint, grantedPointIDs); //todo error
                                } else {
                                    grantedPointIDs.add(dedicatedIslandID); //todo error
                                    granted.replace(keyOfGrantingPoint, grantedPointIDs); //todo error
                                }
                            }
                        } else if (i < (streamStructure.length - 1)) {
                            for (int m = 0; m < 3; m++) {
//                                GrantedPoint grantedPoint = new GrantedPoint(Long.valueOf(j - 1 + m), Long.valueOf(i + 1)); //todo out of border
                                String keyOfGrantingPoint = String.valueOf(j - 1 + m) + '&' + String.valueOf(i + 1);
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
                        }
                }
            }
        }

        isWorking = false;
    }

    private void removeID(Integer rID, Integer takeOverID) {
        ArrayList<Point> rIDsPoints = Main.findedIslands.remove(rID);
        ArrayList<Point> takeoverIDsPoints = Main.findedIslands.get(takeOverID);
        takeoverIDsPoints.addAll(rIDsPoints);
        lostIDs.add(rID);
        Main.findedIslands.replace(takeOverID, takeoverIDsPoints);
    }
}
