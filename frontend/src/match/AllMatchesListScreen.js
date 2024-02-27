import React, { useEffect, useState } from "react";
import { ImCross } from "react-icons/im";
import { TiTick } from "react-icons/ti";
import { Input, Label, Table } from "reactstrap";
import tokenService from "../services/token.service";
import getErrorModal from "../util/getErrorModal";

const jwt = tokenService.getLocalAccessToken();

export default function AllMatches() {
  const [onlyInCourse, setOnlyInCourse] = useState(false)
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [matches, setMatches] = useState([])
  const [alerts, setAlerts] = useState([]);

  async function fetchMatches() {
    const fetchedMatches = await fetch(`/petris/matches/all`, {
      method: 'GET',
      headers: {
        "Authorization": `Bearer ${jwt}`,
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
    })
      .then(response => response.json())
      .catch((message) => alert(message));
    
      setMatches(fetchedMatches);
    
  }

  function formatDateTime(dateTime = null) {
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

  function handleSwitchChange() {
    setOnlyInCourse(!onlyInCourse)
  }

  useEffect(() => {
    fetchMatches()
  }, [onlyInCourse])

  function getUsername(player){
    return player?.user.username ?? "Cuenta borrada";
  }

  const matchList = matches.filter(match => !onlyInCourse || (match.startDate === null || match.endDate === null))
    .map((match) => {
      return (
        <tr key={match.id}>
          <td>{match.name}</td>
          <td>{getUsername(match.creator)}</td>
          <td>{getUsername(match.player)}</td>
          <td>{formatDateTime(match.startDate)}</td>
          <td>{formatDateTime(match.endDate)}</td>
          <td>{match.isPrivated ? <TiTick /> : <ImCross />}</td>
        </tr>
      );
  });
  const modal = getErrorModal(setVisible, visible, message);
  return (
    <div className="admin-page-container">
      <h1 className="text-center">PARTIDAS</h1>
      {alerts.map((a) => a.alert)}
      {modal}
      <div>
        <div className="custom-form-input" style={{ alignSelf: 'center' }}>
          <Label className="custom-form-input-label">
            Mostrar solo partidas en curso
          </Label>
          <Input
            type="checkbox"
            onChange={handleSwitchChange}
            checked={onlyInCourse}
          />
        </div>
        <Table aria-label="matches" className="mt-4" >
          <thead style={{ textAlign: 'center' }}>
            <tr>
              <th>NOMBRE</th>
              <th>Jugador 1</th>
              <th>Jugador 2</th>
              <th>FECHA INICIO</th>
              <th>FECHA FIN</th>
              <th>PRIVADA</th>
            </tr>
          </thead>
          <tbody style={{ textAlign: 'center' }}>{matchList}</tbody>
        </Table>
      </div>
    </div>
  );
}