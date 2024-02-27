import React from "react";
import bacteriaAzul from "../../static/images/Bacteria_azul.png";
import bacteriaRoja from "../../static/images/Bacteria_roja.png";
import sarcinaAzul from "../../static/images/Sarcina_azul.png";
import sarcinaRoja from "../../static/images/Sarcina_roja.png";

export default function CounterChips({style, player, color, numBacteriasUsadas = 0, numSarcinasUsadas = 0}){
    const bacteria = color === "red" ? bacteriaRoja : bacteriaAzul;
    const sarcina = color === "red" ? sarcinaRoja : sarcinaAzul;
    const numBacteriasRestantes = 20 - numBacteriasUsadas;
    const numSarcinasRestantes = 4 - numSarcinasUsadas;
    const D = 80;
    return (
        <div style={{...style}}>
            <div>
                <img width={D} height={D} src={bacteria} alt="bacteria"/>X{numBacteriasRestantes}
            </div>
            <div>
                <img width={D} height={D} src={sarcina} alt="sarcina"/>X{numSarcinasRestantes}
            </div>
            <div>
                {player ? player.user.username : `Player ${color}`}
            </div>
        </div>
    );
}