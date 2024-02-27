import jwt_decode from "jwt-decode";
import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { NavLink } from 'reactstrap';
import '../App.css';
import authService from "../auth/AuthService";
import CircleButton from '../components/boardGameComponents/circleButton';
import SearchingModal from '../components/other/searchingModal';
import tokenService from "../services/token.service";
import '../static/css/home/home.css';
import logo from '../static/images/PetrisLogo.png';

export default function Home() {

  const [matchMaking, setMatchMaking] = useState(false);
  const [match, setMatch] = useState(null);
  const [suggestion, setSuggestion] = useState(null)
  const [player, setPlayer] = useState(null);
  const jwt = tokenService.getLocalAccessToken();

  useEffect(()=>{
    const init = async () => {
      const playerFetched = await authService.getPlayer()
      setPlayer(playerFetched)
    };
    init()
  },[])


  let roles = [];
  let role = "";
  if (jwt) {
    roles = jwt_decode(jwt).authorities;
    for (const r of roles) {
      if (r === "PLAYER" || r === "ADMIN") {
        role = r;
        break;
      }
    }
  }


  async function strtMatchmaking() {
    setMatchMaking(!matchMaking);
    const matchFetched = await fetch(`/petris/matches/quickplay?playerId=${player?.id}`, {
      method: 'GET',
      headers: {
        "Authorization": `Bearer ${jwt}`,
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
    })
      .then(response => response.status !== 200 ? null : response.json())
      .catch((message) => alert(message));
    if (matchFetched !== null) {
      setMatch(matchFetched);
    } else {
      setSuggestion(true)
    }
  }

  async function joinMatch() {
    const matchJoined = await fetch(`/petris/matches/join/${match.id}/${player?.id}`, {
      method: 'PUT',
      headers: {
        "Authorization": `Bearer ${jwt}`,
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
    })
      .then(response => response.json())
      .catch((message) => alert(message));
    setMatch(null)
    setMatchMaking(null)
    if(matchJoined?.id)
      window.location.href = `/match/${matchJoined.id}`;
  }

  let content = <></>;

  function type(){
    let res = null;
    if(matchMaking) res = "SEARCHING";
    if(match !== null) res = "FOUND_MATCH";
    if(suggestion !== null) res = "SUGGEST_CREATE";
    return res;
  }

  function onConfirm(){
    const check = type()
    if(check !== null){
      if(check === "SUGGEST_CREATE"){
        window.location.href = "/matches/create";
      } else {
        joinMatch();
      }
    }
  }

  function onCancel(){
    const check = type()
    if(check !== null){
      setMatch(null); 
      setMatchMaking(false);
      if(check === "SUGGEST_CREATE"){
        setSuggestion(null);
      }
    }
  }

  function onRepeat(){
    setSuggestion(null); 
    strtMatchmaking();
  }

  if (role === "PLAYER") {
    content = (
      <div className="initial-page-container">
        <NavLink style={{ color: "white" }} tag={Link}>
          <CircleButton onClickButton={strtMatchmaking} url={"/AJugarBoton.png"} style={{ position: "absolute", left: "40%", bottom: "15%" }} />
        </NavLink>
        <NavLink style={{ color: "white" }} tag={Link} to="/matches/create">
          <CircleButton url={"/CrearPartidaBoton.png"} style={{ position: "absolute", left: "25%" }} />
        </NavLink>
        <NavLink style={{ color: "white" }} tag={Link} to="/matches/play">
          <CircleButton url={"/UnirsePartidaBoton.png"} style={{ position: "absolute", left: "55%" }} />
        </NavLink>
        <SearchingModal
            type={type()}
            onCancel={() => onCancel()}
            onConfirm={() => onConfirm()}
            onRepeat={() => onRepeat()}
        />
      </div>
    );
  } else if (role === "ADMIN") {
    content = (
      <div className="initial-page-container-admin">
        <NavLink style={{ color: "white" }} tag={Link} to="/users">
          <CircleButton url={"/Usuarios.png"} style={{ position: "absolute", left: "40%", bottom: "15%" }} />
        </NavLink>
        <NavLink style={{ color: "white" }} tag={Link} to="/achievements">
          <CircleButton url={"/Logros.png"} style={{ position: "absolute", left: "25%" }} />
        </NavLink>
        <NavLink style={{ color: "white" }} tag={Link}  to="/matches">
          <CircleButton url={"/Partidas.png"} style={{ position: "absolute", left: "55%" }} />
        </NavLink>
      </div>
    );
  }


  return (
    <div>
      <div className="home-page-container">
        {jwt ?
          content
          :
          <div className="hero-div">
            <img src={logo} alt="Petris Logo" />
            <h3>¡Crea una cuenta y únete a la comunidad de Petris ahora de forma gratuita!</h3>
          </div>
        }
      </div>
    </div>

  );
}
