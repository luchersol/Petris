import { useEffect, useState } from "react";
import authService from '../auth/AuthService';
import tokenService from "../services/token.service";
import { Link } from "react-router-dom";
import { Button } from "reactstrap";

export default function Stats() {
    const jwt = tokenService.getLocalAccessToken();
  
    const [stats,setStats]= useState({})

    useEffect(()=>{
        getMyStats();
    }, []);

    async function getMyStats() {
        const {id} = await authService.getPlayer();
        const myStats = await fetch(`/petris/players/${id}/stats`, {
            method: 'GET',
            headers: {
              "Authorization": `Bearer ${jwt}`,
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
          })
          .then(response => response.json())
          .catch((message) => alert(message));
        console.log(myStats)
        setStats(myStats);
    }


    return (
        <div className="auth-page-container" style={{marginTop: 20, fontSize: 12}}>
            <h1 className="text-center">Mis estadísticas</h1>
            <div>
                <p className="text-center">Numero de Bacterias: {stats.totalBacterium}</p>
                <p className="text-center">Numero de Sarcinas: {stats.totalSarcinas}</p>
                <p className="text-center">Victorias: {stats.victories}</p>
                <p className="text-center">Derrotas: {stats.losses}</p>
            </div>

            
            <Button tag={Link} to="/stats/podium"   outline color="success" 
              style={{ textDecoration: "none" }}>
                VER MEJORES JUGADORES
            </Button>
        </div>
    );
}