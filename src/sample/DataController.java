package sample;

import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Murzynas on 2016-04-18.
 */
public class DataController {
    private ArrayList<CircleData> circleDatas;
    private HashMap<String, Float> idToPercentValues;

    public DataController() {
        circleDatas = new ArrayList<>();
        idToPercentValues = new HashMap<>();
    }

    public void addCircleData(String id, Float value) {
        circleDatas.add(new CircleData(id, value));
    }

    public void addIdToPercentValue(String s, Float f) {
        this.idToPercentValues.put(s, f);
    }

    public ArrayList<CircleData> getCircleDatas() {
        return circleDatas;
    }

    public HashMap<String, Float> getIdToPercentValues() {
        return idToPercentValues;
    }
}
