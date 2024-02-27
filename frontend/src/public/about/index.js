import React from "react";

export default function About() {
  const descripcionJuego = "El Petris es un juego de 2 jugadores de aproximadamente 10 minutos de duración. Cada jugador hace el papel de un científico " + 
    "que deberá controlar la propagación de las bacterias bajo su supervisión, cuidando su reproducción y teniendo cuidado con que estas bacterias siempre puedan " +
    "moverse. La partida se desarrolla a través de fases señaladas en el tablero de turnos, dependiendo de las cuales se podrán desarrollar diferentes situaciones:"


  const fasePropagacion = " El jugador deberá mover al menos una bacteria de un disco a uno o más adyacentes, procurando que al realizar estos movimientos ni " +
    "se dejen un mismo número de bacterias de diferentes tipos en ninguna placa, ni se muevan bacterias a una placa donde ya se encuentre una sarcina del mismo jugador."
  const faseFisionBinaria = " Por cada placa de petri se comprueba si en esta hay bacterias de cada tipo, en caso positivo estas se reproducirá añadiendo una " +
    "bacteria, pudiendo dar a lugar a la aparición de una sarcina."
  const faseContaminacion = " Al inicio de esta fase se producirá una fase de fisión binaria, luego de ello por cada placa en la que se encuentren bacterias de " +
    "diferentes jugadores juntas se le sumará 1 punto al contador de contaminación del jugador que tenga más bacterias en dicha placa. Una vez pasadas 4 rondas de " +
    "contaminación se produce el fin de partida."

  const introduccionReglas = "En el transcurso de la partida se pueden dar a lugar a diferentes situaciones que provoquen el fin de la misma:";
  const reglas = [
    "Si un jugador no puede mover ninguna bacteria, pierde automáticamente.",
    "Si un jugador debe poner una sarcina y no le quedan, pierde automáticamente.",
    "Si en algún momento un jugador llega al límite de contaminación, pierde automáticamente.",
    "En caso de que se llegue al final de la partida se comprobará el nivel de contaminación de cada jugador, ganando quien tenga menor nivel. En caso de empate ganará quien tenga menos fichas (bacterias y sarcinas) en las placas, si se produce de nuevo empate ganará quien tenga menos sarcinas en juego."
  ]

  return (
    <div>
      <div style={{ justifyContent: 'flex-start', marginRight: '10%', marginLeft: '10%', marginTop: "2%" }}>
        <h1>DESCRIPCIÓN DEL JUEGO</h1>
        <p>{descripcionJuego}</p>

        <h2>FASES DEL JUEGO</h2>
        <p>
          <u>Fase de propagación:</u>{fasePropagacion}
        </p>
        <p>
          <u>Fase de fisión binario:</u>{faseFisionBinaria}
        </p>
        <p>
          <u>Fase de contaminación:</u>{faseContaminacion}
        </p>

        <h2>CÓMO GANAR</h2>

        {introduccionReglas}
        <ul>
          {reglas.map((regla, index) => <li key={index}>{regla}</li>)}
        </ul>

      </div>
    </div>
  );
}
