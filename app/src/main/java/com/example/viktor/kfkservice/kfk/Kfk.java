package com.example.viktor.kfkservice.kfk;

public class Kfk {
    private double u100 = 4;
    private double zero = 0.05;

    public synchronized double getU100() {
        return u100;
    }

    public synchronized void setU100(double u100) {
        this.u100 = u100;
    }

    public synchronized double getP(double valueG) {
        return  (((valueG - getZero()) / (getU100() - getZero())) * 100);
    }

    public synchronized double getZero() {
        return zero;
    }

    public synchronized void setZero(double value) {
        this.zero = value;
    }
}
