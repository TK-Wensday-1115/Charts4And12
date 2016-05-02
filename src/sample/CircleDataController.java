package sample;

import javafx.scene.paint.Color;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Murzynas on 2016-04-18.
 */
public class CircleDataController {
    /**
     * Variables used for Bubble Bandwidth Chart
     */
    private HashMap<String, CircleData> circlesDatasMap;
    private static CircleDataController instance = null;
    private Float summedCirclesValues;
    public int uniqueId;
    public Queue<Color> colorsQueue;

    protected CircleDataController() {
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
    public static CircleDataController getInstance() {
        if(instance == null) {
            instance = new CircleDataController();
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

    public static void setInstance(CircleDataController instance) {
        CircleDataController.instance = instance;
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

    public void modifyCircle(String id, Float value) {
        System.out.println(id+" ---- "+value);
        this.circlesDatasMap.get(id).setValue(value);
        this.circlesDatasMap.get(id).recalculatePercents(getSummedCirclesValues());

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
