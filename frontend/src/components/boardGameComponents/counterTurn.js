import React from "react";
import { BiSolidUpArrow } from "react-icons/bi";
import logoBiparticion from "../../static/images/Biparticion.png";
import logoContaminacion from "../../static/images/Contaminacion.png";
import logoTurnoAzul from "../../static/images/TurnoAzul.png";
import logoTurnoRojo from "../../static/images/TurnoRojo.png";
export default function CounterTurn({actualTurn, style}){

    let ls = []
    for(let i = 1; i <= 18; i++){
        ls.push(i);
    }

    function getLogoTurnByIndex(i){
        return i%9 === 0 ? logoContaminacion : 
        i % 3 === 0 ? logoBiparticion : 
        i % 2 === 0 ? logoTurnoRojo : 
        i % 2 === 1 ? logoTurnoAzul :
        null;
    }

    function getRound(){
        return actualTurn <= 18 ? 2 : actualTurn <= 36? 1 : 0;
    }
    
    return (
        <div style={{height:40, display: "flex", ...style}}>
            <div style={{...styleCounter}} >
                <div style={{marginTop:8}}>X{getRound()}</div>
            </div>
            {ls.map(i => {
                const logo = getLogoTurnByIndex(i)
                return (<div style={{width:40}}>
                            <img width={40} height={40} src={logo} alt="turn"/>
                            {(i === actualTurn%18 || (actualTurn%18 === 0 && i%18===0)) && <BiSolidUpArrow style={{position:"relative", left:10}} fontSize={20} color="yellow"/>}
                        </div>);
            })}
        </div>
    
    );
}

const styleCounter = {
        backgroundColor: "white",
        borderRadius: '50%',
        borderWidth: 1,
        borderColor: 'black',
        borderStyle: "solid",
        height:40,
        width:40,
        textAlign:"center"
    
};