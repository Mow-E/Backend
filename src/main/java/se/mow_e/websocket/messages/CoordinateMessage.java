package se.mow_e.websocket.messages;

import java.util.UUID;

public class CoordinateMessage {
    private UUID mowerId;

    private double x;

    private double y;

    private Long time;

    private State state;

    private String extra;

    public CoordinateMessage(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getMowerId() {
        return mowerId.toString();
    }

    public void setMowerId(UUID mowerId) {
        this.mowerId = mowerId;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public State getState() {
        return state;
    }

    public Integer getStateId() {
        return state.ordinal();
    }
    public void setState(Integer state) {
        this.state = State.values()[state];
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "CoordinateMessage{" +
                "mowerId=" + mowerId +
                ", x=" + x +
                ", y=" + y +
                ", time=" + time +
                ", state=" + state +
                ", extended='" + extra + '\'' +
                '}';
    }

    public enum State {
        START,      // 0
        WORK,       // 1
        END,        // 2
        ERROR,      // 3
        COLLISION;  // 4
    }
}
