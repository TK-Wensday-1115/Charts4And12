package sample;

import java.util.HashMap;

/**
 * Created by Murzynas on 2016-04-18.
 */
public class DataController {
    /**
     * Variables used for Bubble Bandwidth Chart
     */
    private HashMap<String, CircleData> circlesDatasMap;
    private static DataController instance = null;
    private Float summedCirclesValues;
    public int uniqueId;

    protected DataController() {
        setCirclesDatasMap(new HashMap<String, CircleData>());
        this.summedCirclesValues = 0f;
        this.uniqueId = 0;

//        for(int i=0; i<10; i++) {
//            String circleId = String.valueOf(i);
//            Float value = 15f;
//            summedCirclesValues += value;
////            Float percentValue = ( value * 100 )/summedCirclesValues;
//            circlesDatasMap.put(circleId, new CircleData(circleId, 15f, 10f));
//        }
//
    }
    public static DataController getInstance() {
        if(instance == null) {
            instance = new DataController();
        }
        return instance;
    }

    public static void setInstance(DataController instance) {
        DataController.instance = instance;
    }


    public void addNewCircleData(String id, Float value, Transition transition) {
        addToSummedCirclesValue(value);
        Float percentValue = ( value * 100 )/ getSummedCirclesValues();
        getCirclesDatasMap().put(id, new CircleData(id, value, percentValue, transition));
    }

    public HashMap<String, CircleData> getCirclesDatasMap() {
        return circlesDatasMap;
    }

    public void setCirclesDatasMap(HashMap<String, CircleData> circlesDatasMap) {
        this.circlesDatasMap = circlesDatasMap;
    }

    public Float getSummedCirclesValues() {
        return summedCirclesValues;
    }

    public void addToSummedCirclesValue(Float addValue) {
        this.summedCirclesValues += addValue;
    }
}
