package com.example.demo;

public class Ellipse extends Shape{
    private int radiusX;
    private int radiusY;

    public int getRadiusY() {
        return radiusY;
    }

    public void setRadiusY(int radiusY) {
        this.radiusY = radiusY;
    }

    public int getRadiusX() {
        return radiusX;
    }

    public void setRadiusX(int radiusX) {
        this.radiusX = radiusX;
    }


    @Override
    protected Ellipse clone() throws CloneNotSupportedException {
        Ellipse clone = new Ellipse();
        clone.setRadiusX(this.getRadiusX());
        clone.setRadiusY(this.getRadiusY());
        clone.setI_d(this.getI_d());
        clone.setShape(this.getShape());
        clone.setX(this.getX());
        clone.setY(this.getY());
        clone.setFillColor(this.getFillColor());

        return clone;
    }
}
