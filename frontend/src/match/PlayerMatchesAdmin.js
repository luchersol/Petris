import React, { useEffect, useState } from "react";
import { Table } from "reactstrap";
import tokenService from "../services/token.service";
import getIdFromUrl from "../util/getIdFromUrl";
import authService from "../auth/AuthService";

const jwt = tokenService.getLocalAccessToken();

export default function UserMatchesList() {

  const [matches, setMatches] = useState([]);
  
  useEffect(() => {
    getMyMatches();
  }, []);

  async function getMyMatches() {
    const id = getIdFromUrl(2)
      const playerMatches = await fetch(`/petris/players/user/${id}/matches`, {
        method: 'GET',
        headers: {
          "Authorization": `Bearer ${jwt}`,
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
      })
        .then(response => response.json())
        .catch((message) => alert(message));;
     setMatches(playerMatches)
  }

  function getUsername(player){
    return player?.user.username ?? "Cuenta borrada";
  }

  const matchList = matches.map((match) => {
    return (
      <tr key={match.id} style={{ textAlign: 'center' }}>
        <td>{match.name}</td>
        <td>{formatDate(match.endDate)}</td>
        <td>{getUsername(match.creator)}</td>
        <td>{getUsername(match.player)}</td>
        <td>{getMatchTime(match.startDate, match.endDate)}  minutos</td>
        <td>{match.winner ? match.winner.user.username : 'Cuenta borrada'}</td>
      </tr>
    );
  });

  function formatDate(dateTime) {
    if(dateTime === null) return "---";
    const fecha = new Date(dateTime);

    const dia = String(fecha.getDate()).padStart(2, '0');
    const mes = String(fecha.getMonth() + 1).padStart(2, '0');
    const anio = fecha.getFullYear();
    const horas = String(fecha.getHours()).padStart(2, '0');
    const minutos = String(fecha.getMinutes()).padStart(2, '0');
    const segundos = String(fecha.getSeconds()).padStart(2, '0');

    const fechaFormateada = `${anio}/${mes}/${dia} ${horas}:${minutos}:${segundos}`;
    return fechaFormateada;
  }

  function getMatchTime(start, end) {
    if (start === null || end === null) return "---";

    const startDate = new Date(start);
    const endDate = new Date(end);

    return Math.floor((endDate - startDate) / (1000 * 60));
  }

  if (matches.length === 0) {
    return (
      <div>
        <h1 className="text-center"> Partidas</h1>
        <div style={{ textAlign: 'center', width: '50%', alignSelf: 'center', marginLeft: '25%', marginRight: '25%', marginTop: '6%' }}>
          Vaya, parece que no tiene ninguna partida jugada.
        </div>
      </div>
    )
  } else {
    return (
      <div className="admin-page-container">
        <h1 className="text-center">Partidas</h1>
        <div>
          <Table aria-label="matches" className="mt-4" style={{ textAlign: 'center' }}>
            <thead>
              <tr>
                <th>Nombre</th>
                <th>Fecha</th>
                <th>Jugador 1</th>
                <th>Jugador 2</th>
                <th>Duración</th>
                <th>Ganador</th>
              </tr>
            </thead>
            <tbody>{matchList}</tbody>
          </Table>
        </div>
      </div>
    )
  }

}
