import java.util.*;

/**
 * @author Rafał Zawadzki
 */
public class Cursor {
    private Byte[][] streamStructure = null;
    public Integer idCounter = -1;
    public LinkedList<Integer> lostIDs = new LinkedList<Integer>();
    public ArrayList<Integer> idsToRemove = new ArrayList<Integer>();
    private Integer INITIAL_HASHMAP_LENGTH_LIMIT = 125;
    public Map<Integer, Integer> findedIslands;
    private Map<String, ArrayList<Integer>> granted = new HashMap<String, ArrayList<Integer>>();
    private Boolean isWorking = false;
    private Float hashMapFillFactor = new Float(0.7);

    public Cursor(Integer initHashMapLength) {
        INITIAL_HASHMAP_LENGTH_LIMIT = initHashMapLength;
    }

    public Cursor() {
    }

    public void start() {
        if (streamStructure == null) {
            System.out.println("Input data hasn't been loaded.");
            return;
        }
        if (isWorking) {
            System.out.println("Algorithm has been started.");
        }
        isWorking = true;
        findedIslands = new HashMap<>(INITIAL_HASHMAP_LENGTH_LIMIT, hashMapFillFactor);

        for (Integer i = 0; i < streamStructure.length; i++) {
            for (Integer j = 0; j < streamStructure[i].length; j++) {
                switch (streamStructure[i][j]) {
                    case 0:
                        String zeroGrantedPointKey = String.valueOf(j) + '&' + String.valueOf(i);
                        granted.remove(zeroGrantedPointKey);
                        continue;
                    case 1:
                        //geting id
                        String grantedPointKey = String.valueOf(j) + '&' + String.valueOf(i);
                        Integer dedicatedIslandID = getProperPointID(grantedPointKey);

                        //adding retriving point to island
                        addPointToIsland(dedicatedIslandID);

                        //granting neigbour points
                        gratntOtherPoints(i, j, dedicatedIslandID);

                        continue;
                }
            }
        }
        isWorking = false;
    }

    public Map<Integer, Integer> getResult() {
        if (!isWorking) {
            printStatus("ok");
            return findedIslands;
        }
        printStatus("error");
        return null;
    }

    private void printStatus(String s) {
        switch (s) {
            case "ok":
                System.out.println("Task has been done correctly.");
                break;
            case "error":
                System.out.println("Task is working or an error has occurred.");
                break;
        }
    }

    public void prepareForNextUsage() {
        setStramStructure(null);
        idCounter = -1;
        lostIDs.clear();
        idsToRemove.clear();
        findedIslands.clear();
        granted.clear();
        isWorking = false;
        setHashMapFillFactor(new Float(0.7));
    }

    private Integer getProperPointID(String pointKey) {
        Integer properID;
        ArrayList<Integer> grantedForPoint = granted.remove(pointKey);
        if (grantedForPoint == null || grantedForPoint.isEmpty()) {
            if (idCounter.equals(Integer.MAX_VALUE) && lostIDs.isEmpty()) {
                System.out.println("Lack of empty IDs.");
                return null;
            }
            if (lostIDs.isEmpty()) {
                properID = ++idCounter;
            } else {
                properID = lostIDs.poll();
            }
        } else {
            Integer minGrantedID = grantedForPoint.get(0);
            Integer indexOfMinGrantedID = 0;
            for (Integer i = 1; i < grantedForPoint.size(); i++) {
                Integer currentID = grantedForPoint.get(i);
                if (minGrantedID > currentID) {
                    minGrantedID = currentID;
                    idsToRemove.add(grantedForPoint.get(indexOfMinGrantedID));
                    indexOfMinGrantedID = i;
                } else if (currentID != minGrantedID) {
                    idsToRemove.add(currentID);
                }
            }
            properID = minGrantedID;

        }

        for (Integer rID : idsToRemove) {
            removeID(rID, properID);
        }
        idsToRemove.clear();

        return properID;
    }

    private void removeID(Integer rID, Integer takeOverID) {
        Integer rIDsPoints = findedIslands.remove(rID);
        Integer takeoverIDsPoints = findedIslands.get(takeOverID);
        Integer sum = rIDsPoints + takeoverIDsPoints;
        lostIDs.add(rID);
        findedIslands.replace(takeOverID, sum);
    }

    private void addPointToIsland(Integer ID) {
        Integer pointsOfIsland = findedIslands.get(ID);
        if (pointsOfIsland != null) {
            pointsOfIsland++;
            if (findedIslands.replace(ID, pointsOfIsland) == null) {
                System.out.println("Not replaced an Island part number)");
            }
        } else {
            findedIslands.put(ID, 1);
        }
    }

    private void gratntOtherPoints(Integer i, Integer j, Integer ID) {
        String keyOfGrantingPoint;

        //grant next point in the same row
        if (j < (streamStructure[i].length - 1)) {
            keyOfGrantingPoint = String.valueOf(j + 1) + '&' + String.valueOf(i);
            addGrantToSpecificPoint(keyOfGrantingPoint, ID);
        }

        //grant points in lower row
        if (j == 0 && i != (streamStructure.length - 1)) {
            for (int m = 0; m < 2; m++) {
                keyOfGrantingPoint = String.valueOf(j + m) + '&' + String.valueOf(i + 1);
                addGrantToSpecificPoint(keyOfGrantingPoint, ID);
            }
        } else if (j == (streamStructure[i].length - 1) && i != (streamStructure.length - 1)) {
            for (int m = 0; m < 2; m++) {
                keyOfGrantingPoint = String.valueOf(j - m) + '&' + String.valueOf(i + 1);
                addGrantToSpecificPoint(keyOfGrantingPoint, ID);
            }
        } else if (i < (streamStructure.length - 1)) {
            for (int m = 0; m < 3; m++) {
                keyOfGrantingPoint = String.valueOf(j - 1 + m) + '&' + String.valueOf(i + 1);
                addGrantToSpecificPoint(keyOfGrantingPoint, ID);
            }
        }
    }

    private void addGrantToSpecificPoint(String key, Integer ID) {
        ArrayList<Integer> grantedPointIDs = granted.get(key);
        if (grantedPointIDs == null) {
            grantedPointIDs = new ArrayList<>();
            grantedPointIDs.add(ID);
            granted.put(key, grantedPointIDs);
        } else if (!grantedPointIDs.contains(ID)) {
            grantedPointIDs.add(ID);
            granted.replace(key, grantedPointIDs);
        }
    }

    public Float getHashMapFillFactor() {
        return hashMapFillFactor;
    }

    public void setHashMapFillFactor(Float hashMapFillFactor) {
        if (isWorking) {
            System.out.println("You can't change a fill factor, the default value is " + hashMapFillFactor);
            return;
        }
        this.hashMapFillFactor = hashMapFillFactor;
    }

    public void setStramStructure(Byte[][] stramStructure) {
        this.streamStructure = stramStructure;
    }

}