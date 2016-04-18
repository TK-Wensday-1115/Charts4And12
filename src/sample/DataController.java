package sample;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Murzynas on 2016-04-18.
 */
public class DataController {
    private ArrayList<CircleData> circleDatas;
    private HashMap<String, Float> idToPercentValues;
    private static DataController instance = null;

    protected DataController() {
        circleDatas = new ArrayList<>();
        idToPercentValues = new HashMap<>();

        circleDatas.add(new CircleData("1", 1.0f));
        circleDatas.add(new CircleData("2", 1.0f));
        circleDatas.add(new CircleData("3", 1.0f));
        circleDatas.add(new CircleData("4", 1.0f));
        circleDatas.add(new CircleData("5", 1.0f));
        circleDatas.add(new CircleData("6", 1.0f));
    }
    public static DataController getInstance() {
        if(instance == null) {
            instance = new DataController();
        }
        return instance;
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
