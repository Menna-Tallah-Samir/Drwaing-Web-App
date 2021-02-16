package com.example.demo;

public class Line extends Shape{
    private int x2;
    private int y2;
    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    @Override
    protected Line clone() throws CloneNotSupportedException {
        Line clone = new Line();
        clone.setX2(this.getX2());
        clone.setY2(this.getY2());
        clone.setI_d(this.getI_d());
        clone.setShape(this.getShape());
        clone.setX(this.getX());
        clone.setY(this.getY());
        clone.setFillColor(this.getFillColor());

        return clone;
    }


}

