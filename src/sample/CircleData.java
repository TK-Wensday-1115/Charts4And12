package sample;

import javafx.scene.shape.Circle;

/**
 * Created by Murzynas on 2016-04-18.
 */
public class CircleData {
    private String id;
    private Float value;
    private Float percentsValue;

    public CircleData(String id, Float value){
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public Float getPercentsValue() {
        return percentsValue;
    }

    public void setPercentsValue(Float percentsValue) {
        this.percentsValue = percentsValue;
    }
}
