package com.example.demo;

import java.util.HashMap;

public class Shape implements Cloneable{
    private int i_d ;
    private String shape ;
    private int x;
    private int y;
    private String fillColor;

    public int getX() {
        return x;
    }

    public String getFillColor() {
        return fillColor;
    }

    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getI_d() {
        return i_d;
    }

    public void setI_d(int i_d) {
        this.i_d = i_d;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
