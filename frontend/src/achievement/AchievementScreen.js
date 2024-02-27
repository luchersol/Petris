import {Button,NavLink,Table} from "reactstrap";
import { useState } from "react";
import tokenService from "../services/token.service";
import useFetchState from "../util/useFetchState";
import { Link } from "react-router-dom";
import authService from "../auth/AuthService";
import useFetchData from "../util/useFetchData";

const imgnotfound = "https://cdn-icons-png.flaticon.com/512/5778/5778223.png";
const jwt = tokenService.getLocalAccessToken();

 export default function Achievement() {
  const [message,setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  
  const [achievements] = useFetchState(
    [],
    `/petris/achievements`,
    jwt,
    setMessage,
    setVisible
  );

  const user = authService.getUser();
  const player = useFetchData(`/petris/players/user/${user.id}`, jwt);

  
  const Achievement = achievements.map((achievement) => {
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
      <NavLink id = "achievement/player" tag={Link} to={`/achievements/${player.id}`}>
        <Button outline color="success" className="btn sm" style={{ textDecoration: "none" }}>
          Mis logros
        </Button>
      </NavLink>
    
    <div>
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
    </div>

  );
}
