import React, { useEffect, useState } from "react";
import { Button, NavLink, Table } from "reactstrap";
import authService from "../auth/AuthService";
import tokenService from "../services/token.service";
import getErrorModal from "../util/getErrorModal";

export default function MatchesList() {

  const jwt = tokenService.getLocalAccessToken();

  const [player, setPlayer] = useState();
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [matchCode, setMatchCode] = useState("");
  const [matches, setMatches] = useState([]);

  useEffect(() => {
    const initPlayer = async () => {
      const p = await authService.getPlayer();
      setPlayer(p);
    }
    initPlayer();
  }, [])

  useEffect(() => {
    async function initMatches() {
      const fetchedMatches = await fetch(`/petris/matches/play?playerId=${player?.id}`, {
        headers: {
          "Authorization": `Bearer ${jwt}`,
        },
      })
        .then(response => response.json())
        .catch(error => alert(error));
      setMatches(fetchedMatches);
    }
    if(player){
      initMatches();
    }
  }, [player])


  async function joinMatch(matchId) {
    try {
      
    const matchJoined = await fetch(`/petris/matches/join/${matchId}/${player?.id}`, {
      method: 'PUT',
      headers: {
        "Authorization": `Bearer ${jwt}`,
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: {}
    })
      .then(response => response.json())
      .catch(error => alert("partida privada"))
    if (matchJoined?.id)
      {window.location.href = `/match/${matchJoined.id}`;}
    } catch (error) {
      console.log(error)
    }

  }

  async function joinPrivateMatch(code) {

    const matchJoined = await fetch(`/petris/matches/joinPrivate?code=${code}&playerId=${player?.id}`, {
      method: 'PUT',
      headers: {
        "Authorization": `Bearer ${jwt}`,
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: {}
    })
      .then(response => response.json())
      .catch(error => alert(error))
      
    if (matchJoined?.id){
      window.location.href = `/match/${matchJoined.id}`;
    } else {
      setMessage("Codigo incorrecto")
      setVisible(true);
    }
  }

  const matchList = matches.map((match) => {

    return (
      <tr key={match.id}>
        <td>{match.name}</td>
        <td>{match.creator?.user?.username}</td>
        <td>
          {match.startDate === null && !match.isPrivated &&
            <button style={{ backgroundColor: 'red', color: 'white' }} onClick={() => joinMatch(match.id)} >
              UNIRSE
            </button>
            }
          <NavLink href={`/match/${match.id}`}>
            <button style={{ backgroundColor: 'blue', color: 'white' }}>
              VER
            </button>
          </NavLink>
        </td>
      </tr>
    );
  });
  const modal = getErrorModal(setVisible, visible, message);
  return (
    <div className="admin-page-container">
      <h1 className="text-center">PARTIDAS </h1>
      <div style={{ justifyContent: 'space-between' }}>
        <label htmlFor="searchInput"> Introducir código: </label>
        <input
          style={{ borderRadius: 12, marginLeft: 12 }}
          type="text"
          id="searchInput"
          value={matchCode}
          onChange={(e) => setMatchCode(e.target.value)}
        />
        
        <Button onClick={() => joinPrivateMatch(matchCode)} outline color="success" className="btn sm" style={{ textDecoration: "none" }}>UNIRSE A PARTIDA PRIVADA</Button>
      </div>
      {modal}
      <div>
        <Table aria-label="matches" className="mt-4">
          <thead>
            <tr style={{ textAlign: 'center' }}>
              <th>Nombre</th>
              <th>Creador</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>{matchList}</tbody>
        </Table>
      </div>
    </div>
  );
}