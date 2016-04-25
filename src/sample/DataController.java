package sample;

import javafx.scene.paint.Color;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

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
    public Queue<Color> colorsQueue;

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

        instance.fillColors();
        return instance;
    }

    public void fillColors() {
        colorsQueue = new LinkedBlockingQueue<>();

        Color color = Color.TRANSPARENT;
        for(Field field : Color.class.getFields()) {
            if(field.getType().equals(Color.class)) {
                if(field.getName().equals("WHITE") || field.getName().equals("TRANSPARENT")) {
                    continue;
                }
                try {
                    Color colorValue = (Color) field.get(color);
                    colorsQueue.add(colorValue);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Color getNextColor() {
        return colorsQueue.poll();
    }

    public static void setInstance(DataController instance) {
        DataController.instance = instance;
    }


    public void addNewCircleData(String id, Float value, Transition transition) {
        addToSummedCirclesValue(value);
        Float percentValue = ( value * 100 )/ getSummedCirclesValues();
        CircleData newCD = new CircleData(id, value, percentValue, transition);
        if(getNextColor() == null) {
            fillColors();
        }
        newCD.setColor(getNextColor());
        getCirclesDatasMap().put(id, newCD);
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
