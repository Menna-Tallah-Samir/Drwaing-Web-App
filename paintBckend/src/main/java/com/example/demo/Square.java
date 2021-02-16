package com.example.demo;

public class Square extends Shape{
    private int length;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    @Override
    protected Square clone() throws CloneNotSupportedException {
        Square clone = new Square();
        clone.setLength(this.getLength());
        clone.setI_d(this.getI_d());
        clone.setShape(this.getShape());
        clone.setX(this.getX());
        clone.setY(this.getY());
        clone.setFillColor(this.getFillColor());

        return clone;
    }
}
