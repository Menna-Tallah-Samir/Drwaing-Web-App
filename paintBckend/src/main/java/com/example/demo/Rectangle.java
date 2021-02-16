package com.example.demo;

public class Rectangle extends Shape implements Cloneable{
    private int height;
    private int width;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    protected Rectangle clone() throws CloneNotSupportedException {
        Rectangle clone = new Rectangle();
        clone.setHeight(this.getHeight());
        clone.setWidth(this.getWidth());
        clone.setI_d(this.getI_d());
        clone.setShape(this.getShape());
        clone.setX(this.getX());
        clone.setY(this.getY());
        clone.setFillColor(this.getFillColor());

        return clone;
    }
}
