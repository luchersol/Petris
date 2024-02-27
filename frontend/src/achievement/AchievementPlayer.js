import React, { useState } from "react";
import { Table } from "reactstrap";
import tokenService from "../services/token.service";
import getIdFromUrl from "../util/getIdFromUrl";
import useFetchState from "../util/useFetchState";

const imgnotfound = "https://cdn-icons-png.flaticon.com/512/5778/5778223.png";
const jwt = tokenService.getLocalAccessToken();

export default function AchievementPlayer() {

  const id = getIdFromUrl(2);

  const [message,setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [achievementsPlayer, setAchievementsPlayer] = useFetchState(
    [],
    `/petris/achievements/player/${id}`,
    jwt,
    setMessage,
    setVisible,
    id
  );
  
  const Achievement = achievementsPlayer.map(achievement => {
      return (
        <tr key={achievement.id}>
          <td className="text-center">
            <img alt="logoLogro" src={achievement.badgeImage ? achievement.badgeImage : imgnotfound} width="50px" />
          </td>
          <td className="text-center"> {achievement.name} </td>
          <td className="text-center"> {achievement.description} </td>
        </tr>
      );
    });



  return (
      <div className="admin-page-container">
        <h1 className="text-center">Logros</h1>
          <Table aria-label="achievement" className="mt-4">
            <thead>
              <tr>
                <th className="text-center">Icono</th>
                <th className="text-center">Nombre</th>
                <th className="text-center">Descripción</th>
              </tr>
            </thead>
            <tbody>{Achievement}</tbody>
          </Table>
      </div>
  );



}
