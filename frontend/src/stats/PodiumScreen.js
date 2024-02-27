import { useEffect, useState } from "react";
import tokenService from "../services/token.service";
import { Table } from 'reactstrap'

export default function PodiumScreen() {
    const jwt = tokenService.getLocalAccessToken();
    const [podium, setPodium] = useState([]);

    useEffect(() => {
        getBestPlayers();
    }, []);

    async function getBestPlayers() {
        const fetchedPodium = await fetch(`/petris/players/podium`, {
            method: 'GET',
            headers: {
                "Authorization": `Bearer ${jwt}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
        })
            .then(response => response.json())
            .catch((message) => alert(message));
        setPodium(fetchedPodium)
    }

    const Podium = podium.map((player) => {
        const winRate = player.stats.victories / (player.stats.victories + player.stats.losses);
        return (
            <tr key={player.id}>
                <td className="text-center">{player.user.username}</td>
                <td className="text-center">{player.stats.victories}</td>
                <td className="text-center">{(winRate.toFixed(2) * 100).toFixed(2)}%</td>
            </tr>
        );
    });

    return (
        <div className="auth-page-container" style={{ marginTop: 20, fontSize: 12 }}>
            <h1 className="text-center">MEJORES JUGADORES</h1>

            <Table aria-label="Player" className="mt-4">
                <thead>
                    <tr>
                        <th className="text-center">Nombre</th>
                        <th className="text-center">Victorias totales</th>
                        <th className="text-center">Porcentaje de victorias</th>
                    </tr>
                </thead>
                <tbody>{Podium}</tbody>
            </Table>
        </div>
    );
}
