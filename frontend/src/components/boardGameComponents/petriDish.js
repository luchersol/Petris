import React from "react";
import { BsFill0CircleFill, BsFill1CircleFill, BsFill2CircleFill, BsFill3CircleFill, BsFill4CircleFill, BsFill5CircleFill, BsFill6CircleFill } from "react-icons/bs";
import bacteriaAzul from "../../static/images/Bacteria_azul.png";
import bacteriaRoja from "../../static/images/Bacteria_roja.png";
import sarcinaAzul from "../../static/images/Sarcina_azul.png";
import sarcinaRoja from "../../static/images/Sarcina_roja.png";

export default function PetriDish({color, quantity, size, index}){
  let number;
  const tam = 30;
  const colorIndex = "#0a0a0a" ;
  const position = "absolute";
  switch(index){
    case 0: number = <BsFill0CircleFill style={{position}} size={tam} color={colorIndex}/>; break;
    case 1: number = <BsFill1CircleFill style={{position}} size={tam} color={colorIndex}/>; break;
    case 2: number = <BsFill2CircleFill style={{position}} size={tam} color={colorIndex}/>; break;
    case 3: number = <BsFill3CircleFill style={{position}} size={tam} color={colorIndex}/>; break;
    case 4: number = <BsFill4CircleFill style={{position}} size={tam} color={colorIndex}/>; break;
    case 5: number = <BsFill5CircleFill style={{position}} size={tam} color={colorIndex}/>; break;
    case 6: number = <BsFill6CircleFill style={{position}} size={tam} color={colorIndex}/>; break;
    default:
  }

  let lsRed = [];
  let lsBlue = [];
  for (let i = 0; i < quantity["red"]; i++) {lsRed.push(i);}
  for (let i = 0; i < quantity["blue"]; i++) {lsBlue.push(i);}

  const L = 22;
  const positionsPetriDishes = {
    0:{left: `${L+10}%`, top: "19%"},
    1:{left: `${L+30}%`, top: "19%"},
    2:{left: `${L}%`, top: "44%"},
    3:{left: `${L+20}%`, top: "44%"},
    4:{left: `${L+40}%`, top: "44%"},
    5:{left: `${L+10}%`, top: "69%"},
    6:{left: `${L+30}%`, top: "69%"}
  }

  const positionsBacteriumRed = {
    0: {bottom: "50%", left: "10%"},
    1: {bottom: "50%", left: "30%"},
    2: {bottom: "50%", left: "50%"},
    3: {bottom: "50%", left: "70%"},
    4: {bottom: "50%", left: "40%"}
  }

  const positionsBacteriumBlue = {
    0: {bottom: "20%", left: "10%"},
    1: {bottom: "20%", left: "30%"},
    2: {bottom: "20%", left: "50%"},
    3: {bottom: "20%", left: "70%"},
    4: {bottom: "20%", left: "40%"}
  }

  const stylePetriDishes = {
    backgroundColor: color,
    position: "absolute",
    borderRadius: '50%',
    height: size,
    width: size,
    borderStyle:"solid",
    borderColor: 'black',
    borderWidth: 10,
    marginBottom: 200,
    paddingTop: 100
  };
  
  const D_bacterium = 40;
  const D_sarcina = 60;
  return (
        <div style={{...stylePetriDishes, ...positionsPetriDishes[index]}}>
          {number}
          {lsRed.length !== 5 ?
            lsRed.map(i=> <img src={bacteriaRoja} style={{width:D_bacterium, height:D_bacterium,  position: "absolute", ...positionsBacteriumRed[i]}} alt="rojo"/>) :
            <img src={sarcinaRoja} style={{ height:D_sarcina, width:D_sarcina, position: "absolute", ...positionsBacteriumRed[4]}} alt="rojo"/>
          }
          {lsBlue.length !== 5?
            lsBlue.map(i=> <img src={bacteriaAzul} style={{height:D_bacterium, width:D_bacterium, position: "absolute", ...positionsBacteriumBlue[i]}} alt="azul"/>):
            <img src={sarcinaAzul} style={{ height:D_sarcina, width:D_sarcina, position: "absolute", ...positionsBacteriumBlue[4]}} alt="azul"/>
          }
        </div>
  );

  
    
}

