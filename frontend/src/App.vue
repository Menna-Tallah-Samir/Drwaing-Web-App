<template>
  <div class="gridBig">
    <div class="containT">
      <div class="gridTasks">
        <button class="buttons" @click="canvasAction('new')" >New</button>
        <button class="buttons"  v-on:click="set()" >Copy</button>
        <button  class="buttons" @click="undo" >undo</button>
        <button class="buttons" @click="redo" >redo</button>
        <button v-on:click="drag" class="buttons" >drag</button>
        <button class="buttons" v-on:click="setDelete()" >Delete</button>


      </div>

      <div class="gridColor">
        <div class="btn">


          <button class="buttons" v-on:click="setFill()" >fill</button>
        </div>
        <div class="gridColor2">
          <form action="/action_page.php">
            <label for="favColor">Select fill color: </label>
            <input type="color" id="favColor" name="favColor" value="black"><br><br>
          </form>
        </div>
      </div>
      <div class="gridSave">
        <div class="gride1">
          <button class="buttons" @click="loadShapesFromServer" >open</button>
          <button class="buttons" @click="save" >Save</button>
        </div>
        <div class="gride2">
          <input type="text" id="name" placeholder="File Name">
          <input type="text" id="path" placeholder="File Path">
          <input type="radio" id="XML" name="fileType" class="fileType" value="xml" >
          <label for="XML">XML</label>
          <input type="radio" id="json" name="fileType" class="fileType" value="json">
          <label for="json">json</label>
        </div>
        <div class="label">
          <label id="status"></label>

        </div>
      </div>
    </div>



    <div class="gridShapes">
      <button class="buttons" v-on:click="setShape('line')" > <img src="./assets/line.png"></button>
      <button class="buttons" v-on:click="setShape('square')" > <img src="./assets/square.png"></button>
      <button class="buttons" v-on:click="setShape('rectangle')" > <img src="./assets/rectangle.png"></button>
      <button class="buttons" v-on:click="setShape('circle')" > <img src="./assets/circle.png"></button>
      <button class="buttons" v-on:click="setShape('ellipse')" > <img src="./assets/ellipse.png"></button>
      <button class="buttons" v-on:click="setShape('rightAngle')" > <img src="./assets/triangle.png"></button>
      <button class="buttons" v-on:click="setShape('triangle')" > <img src="./assets/equilateral.png"></button>
    </div>


    <div class="contain">

      <div class="coordinates">
        <br>
        <span> X-axis: {{this.mouseCurrentLoc.xLoc}}, Y-axis :{{this.mouseCurrentLoc.yLoc}}</span>
      </div>
      <div class="canv">

        <canvas id="myCanvas" width="1300" height="500"  @mousemove="mouseMove"
                @mousedown="mouseDown"  @mouseup="mouseUp"  />

      </div>
    </div>

  </div>

</template>
<script>
import axios from'axios'
import {line,rectangle,square,circle,ellipse,triangle,rightAngleTriangle} from './shapes.js';

export default {

  data(){
    return{
      shapesArray: [],
      fillShape:false,
      chosenOp:false,
      dragShape:false,
      deleteShape:false,
      drawShape: false,
      saved: false,

      shapeBoundingBox:{
        x: 0,
        y: 0,
        width: 0,
        height: 0,
      },
      mousedownLoc:{
        xDown: 0,
        yDown: 0,
      },
      mouseCurrentLoc:{
        xLoc:0,
        yLoc: 0,
      },
      selectedId:-1,
      shapesID: 0,
      savedImage: null,
      shape: '',
      ctx: null,
      canvas: null,
      strokeColor: '',
      fillColor: '',
      line_width: 2,
      dragging:false,
      ghostcanvas:null,
      gctx:null,
      url:'http://localhost:8085'
    }
  },
  methods: {
    set(){
      this.chosenOp=true;
      this.dragShape = false;
      this.fillShape = false;
      this.drawShape = false;
      this.deleteShape=false;
    },
    setFill(){
      this.fillShape=true;
      this.chosenOp = false;
      this.dragShape = false;
      this.drawShape = false;
      this.deleteShape=false;
    },
    setDelete(){
      this.deleteShape=true;
      this.fillShape=false;
      this.chosenOp = false;
      this.dragShape = false;
      this.drawShape = false;
    },
    //clear canvas
    clear() {
      this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
    },
    setShape(shape){
      this.shape = shape;
      this.drawShape = true;
      this.chosenOp = false;
      this.dragShape = false;
      this.fillShape = false;
      this.deleteShape=false;
    },

    //to update shape properties
    updateShapeBoundingData(){
      this.shapeBoundingBox.width = Math.abs(this.mouseCurrentLoc.xLoc - this.mousedownLoc.xDown);
      this.shapeBoundingBox.height = Math.abs(this.mouseCurrentLoc.yLoc - this.mousedownLoc.yDown);

      if(this.mouseCurrentLoc.xLoc > this.mousedownLoc.xDown){
        this.shapeBoundingBox.x = this.mousedownLoc.xDown;
      }else{
        this.shapeBoundingBox.x = this.mouseCurrentLoc.xLoc;
      }

      if(this.mouseCurrentLoc.yLoc > this.mousedownLoc.yDown){
        this.shapeBoundingBox.y = this.mousedownLoc.yDown;
      }else{
        this.shapeBoundingBox.y = this.mouseCurrentLoc.yLoc;
      }
    },

    //to update and draw the shape (used in dragging)
    updateShapeMouseMove(){
      //updating shape with new data( dragging )
      this.updateShapeBoundingData();
      //drawing the shape
      this.drawingShapes();
    },

    //shapes
    //line
    drawLine(){
      let l = new line(this.shapesID,this.shape,this.mousedownLoc.xDown,this.mousedownLoc.yDown,this.mouseCurrentLoc.xLoc,this.mouseCurrentLoc.yLoc,this.strokeColor);
      if(!this.dragging){
        this.shapesArray.push(l);
        this.saveShapeToServer("draw",l);
        this.shapesID++;
      }
      l.draw(this.ctx);
    },
    //rectangle
    drawRectangle(){
      let rec = new rectangle(this.shapesID,this.shape,this.shapeBoundingBox.x,this.shapeBoundingBox.y,this.shapeBoundingBox.width,this.shapeBoundingBox.height,this.strokeColor);
      if(!this.dragging) {
        console.log("entered");
        this.shapesArray.push(rec);
        this.saveShapeToServer("draw",rec);
        this.shapesID++;
      }
      rec.draw(this.ctx);
    },

    //square
    drawSquare(){
      let sqr = new square(this.shapesID,this.shape,this.shapeBoundingBox.x,this.shapeBoundingBox.y,this.shapeBoundingBox.width,this.strokeColor);
      if(!this.dragging){
        this.shapesArray.push(sqr);
        this.saveShapeToServer("draw",sqr);
        this.shapesID++;
      }
      sqr.draw(this.ctx);
    },

    //equilateral Triangle
    drawEquilateralTriangle(){
      let tri = new triangle(this.shapesID,this.shape,this.shapeBoundingBox.x,this.shapeBoundingBox.y,this.shapeBoundingBox.width,this.shapeBoundingBox.height,this.strokeColor);
      if(!this.dragging){
        this.shapesArray.push(tri);
        this.saveShapeToServer("draw",tri);
        this.shapesID++;
      }
      tri.draw(this.ctx);
    },

    //Triangle
    drawTriangle(){
      let tri = new rightAngleTriangle(this.shapesID,this.shape,this.shapeBoundingBox.x,this.shapeBoundingBox.y,this.shapeBoundingBox.width,this.shapeBoundingBox.height,this.strokeColor);
      if(!this.dragging){
        this.shapesArray.push(tri);
        this.saveShapeToServer("draw",tri);
        this.shapesID++;
      }
      tri.draw(this.ctx);
    },

    //circle
    drawCircle(){
      let cir = new circle(this.shapesID,this.shape,this.shapeBoundingBox.x,this.shapeBoundingBox.y,this.shapeBoundingBox.width/2,this.strokeColor);
      if(!this.dragging){
        this.shapesArray.push(cir);
        this.saveShapeToServer("draw",cir);
        this.shapesID++;
      }
      cir.draw(this.ctx);
    },

    //ellipse
    drawEllipse(){
      let ell = new ellipse(this.shapesID,this.shape,this.shapeBoundingBox.x,this.shapeBoundingBox.y,this.shapeBoundingBox.width/2,this.shapeBoundingBox.height/2,this.strokeColor);
      if(!this.dragging){
        this.shapesArray.push(ell);
        this.saveShapeToServer("draw",ell);
        this.shapesID++;
      }
      ell.draw(this.ctx);
    },

    //Drawing shapes
    drawingShapes(){
      //this.ctx.fillStyle = document.getElementById("favColor").value;
      this.strokeColor = document.getElementById("favColor").value;
      if(this.shape === "line"){
        this.drawLine();
      }else if(this.shape === "rectangle"){
        this.drawRectangle();
      }else if(this.shape === "square"){
        this.drawSquare();
      }else if(this.shape === "triangle"){
        this.drawEquilateralTriangle();
      }else if(this.shape === "rightAngle"){
        this.drawTriangle();
      }else if(this.shape === "circle"){
        this.drawCircle();
      }else if(this.shape === "ellipse"){
        this.drawEllipse();
      }
    },

    //save canvas data
    saveCanvas(){
      this.savedImage = this.ctx.getImageData(0,0,this.canvas.width,this.canvas.height);
    },

    //load data
    restoreCanvas(){
      this.ctx.putImageData(this.savedImage,0,0);
    },

    ///////////
    //dragging
    drag(){
      this.dragShape = true;
      this.chosenOp = false;
      this.fillShape = false;
      this.deleteShape=false;
      this.drawShape = false;
    } ,

///////////false///////////
//Redraw canvas
    reDraw(){
      this.clear();
      for (let i = 0; i < this.shapesArray.length; i++) {
        let shape = this.shapesArray[i];
        if(shape !== null){
          shape.draw(this.ctx);
          console.log(shape.shape);
        }
      }
    },


    mouseClicked(){
      this.gctx.clearRect(0,0,this.ghostcanvas.width,this.ghostcanvas.height);
      for ( let i = this.shapesArray.length-1 ; i >=  0; i--) {
        let shape = this.shapesArray[i];
        if(shape !== null){
          shape.draw(this.gctx);
          let imageData = this.gctx.getImageData(this.mousedownLoc.xDown, this.mousedownLoc.yDown, 1, 1);
          if (imageData.data[3] > 0) {
            this.selectedId=i;
            this.selectedShape = this.shapesArray[i];
            console.log(shape.i_d);
            //this.reDraw();
            this.chosenOp=false;
            break;
          }
        }
      }
    },

    //to react to mouse being clicked
    mouseDown(e){
      if(this.saved){
        document.getElementById("status").innerHTML='';
        this.saved=false;
      }
      this.canvas.style.cursor = "crosshair";
      this.mousedownLoc.xDown = e.offsetX;
      this.mousedownLoc.yDown = e.offsetY;
      if(this.drawShape){
        this.dragging = true;
      }
      this.saveCanvas();
      if(this.chosenOp){
        this.dragging = false;
        this.mouseClicked();
        this.copyShape(this.selectedId);
        this.selectedId = -1;

      }
      else if(this.dragShape===true){
        this.dragging = false;
        this.mouseClicked();
      }
      else if(this.fillShape){
        this.dragging=false;
        this.mouseClicked();
        this.shapesArray[this.selectedId].fillColor=document.getElementById("favColor").value;
        this.saveShapeToServer("modify",this.shapesArray[this.selectedId]);
        this.reDraw();
        this.fillShape=false;
        document.getElementById("favColor").value = "#ffffff";
        this.selectedId = -1;
      }
      else if(this.deleteShape){
        this.dragging=false;
        this.mouseClicked();
        this.shapesArray[this.selectedId]=null;
        this.deleteShapeFromServer(this.selectedId);
        this.reDraw();
        this.deleteShape=false;
        this.selectedId = -1;
      }

    },

    //to react to mouse moving
    mouseMove(e){
      //console.log(this.mouseCurrentLoc.xLoc+"---"+this.mouseCurrentLoc.yLoc);
      if(this.dragging){
        this.restoreCanvas();
        this.canvas.style.cursor = "crosshair";
        this.mouseCurrentLoc.xLoc = e.offsetX;
        this.mouseCurrentLoc.yLoc = e.offsetY;
        // console.log(this.mouseCurrentLoc.xLoc+"---"+this.mouseCurrentLoc.yLoc);
        this.updateShapeMouseMove();
      }else if(this.dragShape && this.selectedId !== -1){
        this.mouseCurrentLoc.xLoc = e.offsetX;
        this.mouseCurrentLoc.yLoc = e.offsetY
        this.shapesArray[this.selectedId].x = this.mouseCurrentLoc.xLoc;
        this.shapesArray[this.selectedId].y = this.mouseCurrentLoc.yLoc;
        this.reDraw();
      }

    },

    //to react to mouse being lifted
    mouseUp(e){
      if(this.dragging){
        this.dragging = false;
        this.drawShape = false;
        this.restoreCanvas();
        this.canvas.style.cursor = "default";
        this.mouseCurrentLoc.xLoc = e.offsetX;
        this.mouseCurrentLoc.yLoc = e.offsetY;
        this.updateShapeMouseMove();
      }else if(this.dragShape){
        this.mouseCurrentLoc.xLoc = e.offsetX;
        this.mouseCurrentLoc.yLoc = e.offsetY;
        this.shapesArray[this.selectedId].x = this.mouseCurrentLoc.xLoc;
        this.shapesArray[this.selectedId].y = this.mouseCurrentLoc.yLoc;
        this.saveShapeToServer("modify",this.shapesArray[this.selectedId]);
        this.reDraw();
        this.chosenOp = false;
        this.dragShape = false;
        this.selectedId = -1;
      }

    },

    getMousePosition(e){
      return {
        x: e.offsetX,
        y: e.offsetY,
      };
    },
    //saving shape

    saveShapeToServer(action,shape){
      let url = this.url +"/" + action +"Shape";
      console.log(shape);
      axios.post(url, shape)
          .then(function (response) {
            console.log(response.data);
          })
          .catch(function (error) {
            console.log(error);
          });
    },
    deleteShapeFromServer(shapeId){
      let url = this.url +"/deleteShape";
      axios.get(url,{params: {id:shapeId}});
    },
    copyShape(shapeId){
      let url;
      url = this.url + "/copyShape";
      axios.get(url,{params: {id:shapeId}})
          .then(function (response) {
            console.log(response.data);
            this.loadShape(response.data);
            this.reDraw();
            this.shapesID++;
          }.bind(this))
      //  console.log(this.shapesArray[3]);
    },
    getActionFromServer(action){
      let url;
      url = this.url + "/getAction";
      axios.get(url,{params: {action:action}})
          .then(function (response) {
            console.log(response.data);

            if(response.data !== ''){
              console.log(response.data);
              let id = response.data.id;
              let obj = response.data.shape;
              if( obj!==null){
                this.loadShape(obj);
              }
              else{
                this.shapesArray[id] = null;
              }
              this.reDraw();
            }
          }.bind(this))
    },
    undo(){
      this.getActionFromServer("undo");
    },
    redo(){
      this.getActionFromServer("redo");
    },
    loadShape(data){
      let shape;
      if(data.shape === "line" ){
        shape = new line(data.i_d,data.shape,data.x,data.y,data.x2,data.y2,data.fillColor);
      }
      else if(data.shape === "rectangle" ){
        shape = new rectangle(data.i_d,data.shape,data.x,data.y,data.width,data.height,data.fillColor);
      }
      else if(data.shape === "square" ){
        shape = new square(data.i_d,data.shape,data.x,data.y,data.length,data.fillColor);
      }
      else if(data.shape === "circle" ){
        shape = new circle(data.i_d,data.shape,data.x,data.y,data.radius,data.fillColor);
      }
      else if(data.shape === "ellipse" ){
        shape = new ellipse(data.i_d,data.shape,data.x,data.y,data.radiusX,data.radiusY,data.fillColor);
      }
      else if(data.shape === "rightAngle" ){
        shape = new rightAngleTriangle(data.i_d,data.shape,data.x,data.y,data.width,data.height,data.fillColor);
      }
      else if(data.shape === "triangle" ){
        shape = new triangle(data.i_d,data.shape,data.x,data.y,data.width,data.height,data.fillColor);
      }
      console.log(shape.i_d);
      if(shape.i_d < this.shapesArray.length){
        this.shapesArray[shape.i_d]=shape;
      }
      else{
        this.shapesArray.push(shape);
      }},
    canvasAction(action){
      if( action === "new" ){
        this.clearCanvas();
      }
      console.log(action);
      let url = this.url +"/canvasAction";
      axios.get(url,{ params: {action: action }})
          .then(function (response) {
            console.log(response.data);
          })
          .catch(function (error) {
            console.log(error);
          });

    },
    loadShapesFromServer(){
      let firstPath = document.getElementById("path").value;
      let fileName = document.getElementById("name").value;
      let fileType = document.querySelector('input[name="fileType"]:checked').value;
      let filePath = firstPath + fileName + '.' + fileType;
      console.log(filePath);
      let url;
      url = this.url + "/load";
      axios.get(url,{ params: {filePath: filePath }})
          .then(function (response) {
            console.log(response.data);

            if(response.data !== '') {
              let arr = response.data;
              for( let i = 0 ; i < arr.length ; i++ ){
                let obj = arr[i];
                if( obj !== null ){
                  this.loadShape(obj);
                }
                else{
                  this.shapesArray[i] = null;
                }
              }
              this.reDraw();
            }
          }.bind(this));
      document.getElementById("path").value='';
      document.getElementById("name").value='';
      document.querySelector('input[name="fileType"]:checked').checked=false;
    },
    save(){
      let url;
      url = this.url + "/save";
      let fileType = document.querySelector('input[name="fileType"]:checked').value;
      let filePath = document.getElementById("path").value;
      let fileName = document.getElementById("name").value;
      axios.get(url, { params: {fileType: fileType, fileName: fileName, filePath: filePath }})
      .then(function (response){
        console.log(response.data())
      }.bind(this));
      document.getElementById("status").innerHTML='Saved!';
      document.getElementById("path").value='';
      document.getElementById("name").value='';
      document.querySelector('input[name="fileType"]:checked').checked=false;
      this.saved=true;
    },
    clearCanvas(){
      this.shapesArray=[];
      this.clear();
      this.shapesID = 0;
      this.fillShape = false;
      this.chosenOp = false;
      this.dragShape = false;
      this.deleteShape = false;
      this.drawShape = false;
      this.gctx.clearRect(0,0,this.ghostcanvas.width,this.ghostcanvas.height);
      this.selectedId = -1;
      this.savedImage = null;
    }
  },
  //to set up the canvas when the web page is loaded
  mounted(){
    //document.addEventListener('DOMContentLoaded', this.setUpMyCanvas());
    // Get reference to canvas element
    this.canvas = document.getElementById('myCanvas');
    // Get methods for manipulating the canvas
    this.ctx = this.canvas.getContext('2d');
    this.ctx.strokeStyle = this.strokeColor;
    this.ctx.lineWidth = this.line_width;
    this.ghostcanvas = document.createElement('canvas');
    this.gctx =this.ghostcanvas.getContext('2d');
    this.ghostcanvas.height="450";
    this.ghostcanvas.width="1300";
  }

}
</script>
<style>
#myCanvas {
  border: 1px solid ;
  overflow:hidden;
  background-color: white;
}

.coordinates {
  grid-area: co;
  align-content: baseline;
  font-family: "Arial";
  background-color: #ffffff;
}
.canv {
  grid-area: canv;

  font-family: "Arial";
  background-color: #ffffff;
}
.gridBig{
  display: grid;
  width: 100%;
  height: 100%;
  gap: 10px;
  grid-template-rows: 120px 1fr ;
  grid-template-columns: 200px 1fr 1fr ;
  grid-template-areas:
      "shape containT"
      "shape contain"

;
}
.contain{
  grid-area: contain;
  display: grid;
  grid-template-rows: 50px 1fr ;
  grid-template-areas:
      "co "
      "canv "
;
}
.containT{
  grid-area: containT;
  display: grid;
  gap: 5px;
  grid-template-columns: 0.6fr 0.5fr ;
  grid-template-areas:
      " Tasks Color save"
;
}
.gridColor{
  grid-area: Color;
  border: 1px solid;
  display: grid;
  background-color: #cb9494;
  grid-template-columns: 100px  ;
  grid-template-areas:
      " btn Color2"
}
.gridColor2{
  grid-area: Color2;
  padding: 20px;
  gap: 10px;
  font:20px solid;
}
.btn{
  grid-area: btn;
  display: grid;
  padding: 15px;
  gap: 5px;
  grid-template-rows: 0.5fr 0.5fr ;
  border: 1px solid;
}
.gridTasks{
  grid-area: Tasks;
  display: grid;
  font-size: 40px;
  border: 1px solid;
  background-color: #1c3a54;
  padding: 20px;
  gap: 20px;

  grid-template-columns: repeat(3,100px);
}
.gridShapes{
  grid-area: shape;
  display: grid;
  max-width: 400px;
  max-height: 650px;
  border: 1px solid;
  padding: 10px;
  row-gap: 8px;
  background-color: white;
  overflow: auto;
}
.gridSave{
  grid-area: save;
  display: grid;
  background-color: antiquewhite;
  border: 1px solid;
  padding: 5px;
  gap: 5px;
  font:20px solid;
  grid-template-rows: 30px 50px 20px;
  grid-template-areas:
      "gride1 "
      "gride2 "
      "label"
}
.gride1 {
  grid-area: gride1;
    display: grid;
  grid-template-columns: 0.5fr 0.5fr ;
   column-gap: 5px;
  font-family: "Arial";
}
.label {
  grid-area: label;
  font-family: "Arial";
}
.gride2 {
  grid-area: gride2;
  grid-template-columns: 0.5fr 0.5fr ;
  column-gap: 5px;
  font-family: "Arial";
}
.buttons{
  cursor: pointer;
  outline: white;
  color: black;
  font: 20px solid;
  background-color: white;


}
.buttons:hover {
  background-color: #008CBA;
  color: white; }
.buttons:active {
  box-shadow: 0 5px rgb(255, 255, 255);
  transform: translateY(4px);
}
</style>