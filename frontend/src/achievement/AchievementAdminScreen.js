import { Button, Table } from "reactstrap";
import { useState, useEffect } from "react";
import tokenService from "../services/token.service";
import deleteFromList from "../util/deleteFromList";
import getErrorModal from "../util/getErrorModal"
import useFetchState from "../util/useFetchState";
import "../static/css/admin/adminPage.css";

import { Link, NavLink } from "react-router-dom";

const imgnotfound = "https://cdn-icons-png.flaticon.com/512/5778/5778223.png";
const jwt = tokenService.getLocalAccessToken();

export default function Achievement() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [alerts, setAlerts] = useState([]);
  const [achievements, setAchievements] = useState([])

  useEffect(() => {
    async function initAchievements() {
      await fetch(`/petris/achievements`, {
        headers: {
          "Authorization": `Bearer ${jwt}`,
        },
      }).then(response => response.json())
        .then(json => {
          setAchievements(json)
          if (json.message) {
            setMessage(json.message);
            setVisible(true);
          }
        }).catch((message) => {
          setMessage(message);
          setVisible(true);
        });
    }
    initAchievements();
  }, [])


  const modal = getErrorModal(setVisible, visible, message);

  const Achievement =
    achievements.map((achievement) => {
      return (
        <tr key={achievement.id}>

          <td className="text-center">
            <img alt="logoLogro" src={achievement.badgeImage ? achievement.badgeImage : imgnotfound} width="50px" />
          </td>
          <td className="text-center"> {achievement.name} </td>
          <td className="text-center"> {achievement.description} </td>
          <td style={{ display: "flex", justifyContent: "space-around" }}>
            <Button outline color="warning" >
              <Link
                to={`/achievements/${achievement.id}/edit`} className="btn sm"
                style={{ textDecoration: "none" }}>Editar</Link>
            </Button>
            <Button outline color="danger"
              onClick={() =>
                deleteFromList(
                  `/petris/achievements/${achievement.id}`,
                  achievement.id,
                  [achievements, setAchievements],
                  [alerts, setAlerts],
                  setMessage,
                  setVisible
                )}>
              Eliminar
            </Button>
          </td>
        </tr>
      );
    });



  return (
    <div className="admin-page-container">
      {modal}
      <h1 className="text-center">Logros</h1>
      <NavLink tag={Link} to={`/achievements/new`}>
        <Button outline color="success" className="btn sm" style={{ textDecoration: "none" }}>
          Crear Logro
        </Button>
      </NavLink>
      <Table aria-label="achievement" className="mt-4">
        <thead>
          <tr>
            <th className="text-center">Icono</th>
            <th className="text-center">Nombre</th>
            <th className="text-center">Descripción</th>
            <th className="text-center">Acciones</th>
          </tr>
        </thead>
        <tbody>{Achievement}</tbody>
      </Table>
    </div>
  );
}
