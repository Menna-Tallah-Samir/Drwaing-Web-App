package com.example.demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestBody;

import javax.crypto.Cipher;
import java.util.Map;

public class ShapeFactory {
    ObjectMapper objectMapper = new ObjectMapper();
    public Shape getShape(Map<String, Object> map){
        String shapeName = (String) map.get("shape");
       if(shapeName.equals("line")){
            return objectMapper.convertValue(map, Line.class);
        }else if(shapeName.equals("rectangle")){
            return objectMapper.convertValue(map, Rectangle.class);
        }else if(shapeName.equals("square")){
            return objectMapper.convertValue(map, Square.class);
        }else if(shapeName.equals("triangle") ||shapeName.equals("rightAngle")){
            return objectMapper.convertValue(map, Triangle.class);
        }else if(shapeName.equals("circle")){
            return objectMapper.convertValue(map, Circle.class);
        }else if(shapeName.equals("ellipse")){
            return objectMapper.convertValue(map, Ellipse.class);
        }
        return null;
    }
}
