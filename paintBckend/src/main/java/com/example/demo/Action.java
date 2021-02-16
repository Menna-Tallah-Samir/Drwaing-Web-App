package com.example.demo;

public class Action {
    private String actionName;
    private Shape oldShape;
    private Shape newShape;
    private int id;

    public Action(String actionName , Shape oldShape , Shape newShape ,int id){
        this.actionName = actionName;
        this.oldShape = oldShape;
        this.newShape = newShape;
        this.id = id;
    }

    public String getName(){
        return this.actionName;
    }

    public Shape getOldShape(){
        return this.oldShape;
    }

    public int getId() {
        return id;
    }

    public Shape getnewShape(){
        return this.newShape;
    }





}
