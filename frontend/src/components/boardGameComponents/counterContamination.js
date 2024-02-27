import React from "react";
import { AiOutlineStar } from "react-icons/ai";
import { FaBiohazard } from "react-icons/fa";
export default function CounterContamination({color, levelContamination, size=30, style = {}}){

   function level(index){
      let colored = index === levelContamination ? "yellow" : color;
      return ( index !== 9 ? 
                  <div>-{index}<AiOutlineStar size={size} color={colored}/></div>
                  :
                  <div><FaBiohazard style={{position: "relative", left:10, top:5}} size={size} color={colored} /></div>
      );
   }

    return (
       <div style={{...style}}>
         {[0,1,2,3,4,5,6,7,8,9].map(i => level(i))}
       </div>
    
      );
}
