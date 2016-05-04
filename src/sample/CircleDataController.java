package sample;

import javafx.scene.paint.Color;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Murzynas on 2016-04-18.
 * Class used to keep and modify  any data concerning currently shown circles.
 * There we can add, delete and modify data.
 */
public class CircleDataController {
    /**
     * Map used to keep current state of circles.
     * Key - circle id
     * Value - CircleData - an object keeping all data we need to know about that circle
     */
    private HashMap<String, CircleData> circlesDatasMap;

    /**
     * Sum of all numeric values in circles (CircleData's 'value' field).
     * Needed to count the percentage values of circles.
     */
    private Float summedCirclesValues;

    /**
     * Unique id that will be given to next added circle, incremented every time we add new circle.
     */
    public int uniqueId;

    /**
     * Collection that keeps our colors in concrete order.
     * Used to give unique color to every new circle.
     */
    public Queue<Color> colorsQueue;

    private boolean updateNeeded;


    public CircleDataController() {
        setCirclesDatasMap(new HashMap<String, CircleData>());
        fillColors();
        this.summedCirclesValues = 0f;
        this.uniqueId = 0;
    }

    /**
     * Prepares colorsQueue by filling it with all possible colors.
     * Skips TRANSPARENT and WHITE colors.
     */
    public void fillColors() {
        colorsQueue = new LinkedBlockingQueue<>();

        Color color = Color.TRANSPARENT;
        for(Field field : Color.class.getFields()) {
            if(field.getType().equals(Color.class)) {
                if(!field.getName().equals("WHITE") && !field.getName().equals("TRANSPARENT")) {
                    try {
                        Color colorValue = (Color) field.get(color);
                        colorsQueue.add(colorValue);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Deques next color from colorsQueue and uses it in new circle.
     * @return color to be used in new circle.
     */
    private Color getNextColor() {
        return colorsQueue.poll();
    }

    public void addNewCircle(String id, Float value, Transition transition) {
        addToSummedCirclesValue(value);
        Float percentValue = ( value * 100 )/ summedCirclesValues;
        CircleData newCD = new CircleData(id, value, percentValue, transition);

        newCD.setColor(getNextColor());
        circlesDatasMap.put(id, newCD);
        this.updateNeeded = true;
    }

    public void removeCircle(String id) {
        circlesDatasMap.remove(id);
        this.updateNeeded = true;
    }

    public void modifyExistingCirlce(String id, float value) {
        CircleData cd = circlesDatasMap.get(id);
        cd.setValue(value);
        cd.recalculatePercents(summedCirclesValues);
        this.updateNeeded = true;
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

    /**
     * Used to determine whether we need to update the view or not.
     * Set to true every time a change in data occurs.
     * Set to false when update was finished.
     */
    public boolean isUpdateNeeded() {
        return updateNeeded;
    }

    public void setUpdateNeeded(boolean updateNeeded) {
        this.updateNeeded = updateNeeded;
    }
}
