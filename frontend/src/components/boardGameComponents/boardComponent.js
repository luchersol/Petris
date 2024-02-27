import React from "react";
import PetriDish from "./petriDish";

export default function BoardComponent({quantity}){
    return (
        <div>
            {[0,1,2,3,4,5,6].map(i => <PetriDish quantity={quantity[i]} color={"#D3D3D3"} size={230} index={i}/>)}
        </div>
    );

}