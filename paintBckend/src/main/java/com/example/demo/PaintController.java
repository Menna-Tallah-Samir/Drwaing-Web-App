package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;

@SpringBootApplication
@RequestMapping
@CrossOrigin
@RestController
public class PaintController {

    PaintManager paintManager = PaintManager.getInstance();


    public static void main(String[] args) {

        SpringApplication.run(PaintController.class, args);

    }

    @PostMapping(
            value = "/drawShape" , consumes = "application/json" , produces = "text/plain"
    )
    public void drawShape(@RequestBody Map<String, Object> shape ){
        paintManager.draw(shape);
        paintManager.printShapes();
    }

    @PostMapping(
            value = "/modifyShape" , consumes = "application/json" , produces = "text/plain"
    )
    public void modifyShape( @RequestBody Map<String, Object> shape ){
        paintManager.modify(shape);
    }

    @GetMapping("/deleteShape")

    public void deleteShape(@RequestParam(name="id") int id){
         paintManager.delete(id);
         paintManager.printShapes();
    }

    @GetMapping
    ( value = "/copyShape" ,  produces = "application/json" )
    @ResponseBody
    public Shape copyShape(@RequestParam(name="id") int id) throws CloneNotSupportedException {
         paintManager.printShapes();
         return paintManager.copy(id);
    }

    @GetMapping
            ( value = "/getAction" ,  produces = "application/json" )
    @ResponseBody
    public Shape_IdWrapper getAction ( @RequestParam(name="action") String action){
        Shape_IdWrapper shape = null;
        paintManager.printUndo();

        if(action.equals("undo")){
            shape = paintManager.undo();
        }
        else if ( action.equals("redo")){
            shape = paintManager.redo();
        }
        paintManager.printUndo();
        return shape;
    }

    @GetMapping
            ( value = "/loadShapes" ,  produces = "application/json" )
    @ResponseBody
    public ArrayList<Shape> loadShapes (){
        return paintManager.getShapesArray();
    }

    @GetMapping("/canvasAction")
    @ResponseBody
    public String performCanvasAction(@RequestParam(name="action") String action ){
        if(action.equals("new")){
            paintManager.clearCanvas();
        }
        System.out.println(action);
        return action;

    }

    @GetMapping
            (value = "/save", produces = "application/json")
    public void save(@RequestParam(name="fileType") String fileType,@RequestParam(name="fileName") String fileName,@RequestParam(name="filePath") String filePath){
        if(fileType.equals("xml")){
            paintManager.saveXML(fileName,filePath);
        }else if(fileType.equals("json")){
            paintManager.saveJSON(fileName,filePath);
        }
    }

    @GetMapping
            (value = "/load", produces = "application/json")
    public ArrayList load(@RequestParam(name="filePath") String filePath){
        if(filePath.contains("xml")){
            return paintManager.loadXML(filePath);
        }else if(filePath.contains("json")){
            return paintManager.loadJSON(filePath);
        }
        return null;
    }

}
