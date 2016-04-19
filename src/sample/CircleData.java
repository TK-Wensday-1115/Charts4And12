package sample;

import javafx.scene.shape.Circle;

/**
 * Created by Murzynas on 2016-04-18.
 */
public class CircleData {
    private String id;
    private Float value;
    private Float percentsValue;
    private Transition transition;
    private Circle circle;

    private int x;
    private int y;

    public CircleData(String id, Float value, Float percents, Transition transition){
        this.id = id;
        this.value = value;
        this.percentsValue = percents;
        this.transition = transition;
        setX(-1);
        setY(-1);
    }

    public void recalculatePercents(Float maxValue) {
        this.percentsValue = (this.value * 100) / maxValue;
//        this.percentsValue /= 2; //for visualization - smaller circles
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

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Transition getTransition() {
        return transition;
    }

    public void setTransition(Transition transition) {
        this.transition = transition;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }
}
