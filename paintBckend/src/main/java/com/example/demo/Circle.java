package com.example.demo;

public class Circle extends Shape{
    private int radius;

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    protected Circle clone() throws CloneNotSupportedException {
        Circle clone = new Circle();
        clone.setRadius(this.radius);
        clone.setI_d(this.getI_d());
        clone.setShape(this.getShape());
        clone.setX(this.getX());
        clone.setY(this.getY());
        clone.setFillColor(this.getFillColor());
        return clone;
    }
}
