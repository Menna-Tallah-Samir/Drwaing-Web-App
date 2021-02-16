package com.example.demo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class PaintManager {
    private ArrayList<Shape> shapesArray;
    private Deque<Action> undoStack;
    private Stack<Action> redoStack;
    private ShapeFactory shapeFactory;
    private int shapeCounter = 0;

    private static PaintManager instance = new PaintManager();

    private PaintManager(){
        shapesArray = new ArrayList<Shape>();
        undoStack = new ArrayDeque<Action>();
        redoStack = new Stack<Action>();
        shapeFactory = new ShapeFactory();
    }

    public static PaintManager getInstance(){
        return instance;
    }

    public void clearCanvas(){
        this.shapesArray = new ArrayList<Shape>();
        this.undoStack = new ArrayDeque<Action>();
        this.redoStack = new Stack<Action>();
        this.shapeCounter = 0;
    }

    public void saveCanvas(){

    }

    public ArrayList<Shape> getShapesArray(){
        return this.shapesArray;
    }

    public Shape_IdWrapper undo(){
        Shape_IdWrapper temp = null;
        if(!undoStack.isEmpty()){
            temp = new Shape_IdWrapper();
            Action action = undoStack.pop();
            redoStack.push(action);
            Shape oldShape = action.getOldShape();
            shapesArray.set(action.getId(),oldShape);
            temp.setId(action.getId());
            temp.setShape(oldShape);
            System.out.println(action.getId());
        }
        return temp;
    }

    public Shape_IdWrapper redo(){
        Shape_IdWrapper temp = null;
        if(!redoStack.isEmpty()){
            temp = new Shape_IdWrapper();
            Action action = redoStack.pop();
            undoStack.push(action);
            Shape newShape = action.getnewShape();
            shapesArray.set(action.getId(),newShape);
            temp.setId(action.getId());
            temp.setShape(newShape);
            System.out.println();
        }
        return temp;
    }

    public void draw( Map<String, Object> map){
        this.shapeCounter++;
        Shape shape = shapeFactory.getShape(map);
        redoStack.clear();
        shapesArray.add(shape);
        Action draw = new Action("draw",null,shape,shape.getI_d());
        if(undoStack.size()==20){
            undoStack.removeLast();
            undoStack.addFirst(draw);
        }else{
            undoStack.addFirst(draw);
        }
       // System.out.println(undoStack.getLast().getId());
      //  System.out.println(shape.getShape());
    }

    public void delete( int id ){
        redoStack.clear();
        Shape deletedShape = shapesArray.get(id);
        shapesArray.set(id,null);
        Action delete = new Action("delete", deletedShape , null , deletedShape.getI_d());
        if(undoStack.size()==20){
            undoStack.removeLast();
        }
            undoStack.addFirst(delete);
    }

    public void modify(Map<String, Object> map){
        Shape shape = shapeFactory.getShape(map);
        redoStack.clear();
        Action modify = new Action("modify",shapesArray.get(shape.getI_d()),shape,shape.getI_d());
        if(undoStack.size()==20){
            undoStack.removeLast();
            undoStack.addFirst(modify);
        }else{
            undoStack.addFirst(modify);
        }
        shapesArray.set(shape.getI_d(),shape);
    }

    public Shape copy(int id) throws CloneNotSupportedException {
        Shape shape = (Shape) shapesArray.get(id).clone();
        shape.setI_d(shapeCounter);
        shape.setX(shape.getX()+30);
        shape.setY(shape.getY()+30);
        if( shape instanceof Line ){
            ((Line)shape).setX2(((Line)shape).getX2()+30);
            ((Line)shape).setY2(((Line)shape).getY2()+30);

        }
        shapeCounter++;
        redoStack.clear();
        shapesArray.add(shape);
        Action draw = new Action("draw",null,shape,shape.getI_d());
        if(undoStack.size()==20){
            undoStack.removeLast();
            undoStack.addFirst(draw);
        }else{
            undoStack.addFirst(draw);
        }
     //   System.out.println(shape.getFillColor());
        return shape;
    }

    public void printShapes(){
        for(int i = 0 ; i < shapesArray.size() ; i++ ){
            Shape shape = shapesArray.get(i);
            if( shape != null ){
                System.out.print(shape.getShape()+ "  ");
            }
            else{
                System.out.print("empty  ");
            }
        }
        System.out.println();
    }

    public void printUndo(){
        for( int i = 0 ; i < undoStack.size() ; i++){
            Action temp = undoStack.getLast();
            System.out.print(temp.getName());
        }
        System.out.println();
    }

    public void saveXML(String fileName,String filePath){
        if(filePath.equals("")){
            filePath = "./"+fileName+".xml";
        }else{
            filePath = filePath + "/" + fileName + ".xml";
        }
        try{
            FileOutputStream fos = new FileOutputStream(new File(filePath));
            XMLEncoder encoder = new XMLEncoder(fos);
            encoder.writeObject(this.shapesArray);
            encoder.close();
            fos.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void saveJSON(String fileName,String filePath){
        if(filePath.equals("")){
            filePath = "./"+fileName+".json";
        }else{
            filePath = filePath + "/" + fileName + ".json";
        }
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(filePath),this.shapesArray);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public ArrayList loadXML(String filePath){
        try{
            FileInputStream fos = new FileInputStream(new File(filePath));
            XMLDecoder encoder = new XMLDecoder(fos);
            this.shapesArray = (ArrayList<Shape>) encoder.readObject();
            encoder.close();
            fos.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return this.shapesArray;
    }

    public ArrayList loadJSON(String filePath){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayList<Map<String, Object>> arr = objectMapper.readValue(new File(filePath), new TypeReference<ArrayList<Map<String, Object>>>(){});
            this.shapeCounter = arr.size();
            for(int i=0; i<arr.size(); i++){
                Shape shape = shapeFactory.getShape(arr.get(i));
                this.shapesArray.add(shape);
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return this.shapesArray;
    }

}
