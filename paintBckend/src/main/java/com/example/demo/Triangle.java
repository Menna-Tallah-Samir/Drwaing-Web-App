package com.example.demo;

public class Triangle extends Shape{
    private int width;
    private int height;

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
    @Override
    protected Triangle clone() throws CloneNotSupportedException {
        Triangle clone = new Triangle();
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
