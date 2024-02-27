import { useEffect, useState } from "react";
import { Form, Button, Input, Table, ButtonGroup } from "reactstrap";
import tokenService from "../services/token.service";
import "../static/css/admin/adminPage.css";
import deleteFromList from "../util/deleteFromList";
import getErrorModal from "../util/getErrorModal";
import { Link, NavLink } from "react-router-dom";
import { FaCircle, FaArrowAltCircleLeft, FaArrowAltCircleRight } from "react-icons/fa";

const jwt = tokenService.getLocalAccessToken();

export default function UserListAdmin() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [alerts, setAlerts] = useState([]);
  const [changeSize, setChangeSize] = useState(false);
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(4);
  const [users, setUsers] = useState([])

  const beforePage = () => {
    const newPage = page - Boolean(page !== 0);
    searchUsers(false, newPage);
    setPage(newPage)
  };

  const nextPage = () => {
    const newPage = page + 1;
    searchUsers(true, newPage);
  }

  async function searchUsers(isNextPage = false, newPage = page) {
    const url = `/petris/users?page=${newPage}&size=${size}`;
    const newUsers = await fetch(url, {
      headers: {
        "Authorization": `Bearer ${jwt}`,
      },
    })
      .then(response => {
        return response.json()
      })
      .catch((error) => alert(error));
    if (isNextPage) {
      if (newUsers.length !== 0) {
        setPage(newPage);
        setUsers(newUsers);
      }
    } else {
      setUsers(newUsers);
    }

  }

  useEffect(() => {
    searchUsers();
  }, [])

  useEffect(() => {
    if (changeSize) {
      searchUsers();
      setChangeSize(false);
    }
  }, [changeSize]);

  const userList = users.map((user) => {
    return (
      <tr key={user.id}>
        <td><FaCircle style={{ position: "relative", left: "40%", color: user.online ? 'green' : 'gray' }} /></td>
        <td>{user.username}</td>
        <td>{user.email}</td>
        <td>
          <ButtonGroup>
            <Button outline color={user.authority.authority === "PLAYER" ? "danger" : "secondary"}
              onClick={() => {
                if (user.authority.authority === "PLAYER") {
                  deleteFromList(
                    `/petris/players?userId=${user.id}`,
                    user.id,
                    [users, setUsers],
                    [alerts, setAlerts],
                    setMessage,
                    setVisible
                  )
                } else {
                  setMessage("No puedes borrar a un administrador")
                  setVisible(true)
                }
              }
              }
            >
              Eliminar
            </Button>
            {user.authority.authority !== 'ADMIN' &&
              <Button
                color={"secondary"}
                outline
              >
                <Link to={`/matches/${user.id}`}>Ver partidas</Link>
              </Button>}
          </ButtonGroup>
        </td>
      </tr>
    );
  });

  function handleSubmit(event) {
    event.preventDefault()
    setChangeSize(true);
    setPage(0);

  }

  function handleChange(event) {
    event.preventDefault();
    const target = event.target;
    const newSize = Number(target.value);
    setSize(newSize);
  }

  const passer = (
    <div style={{ display: "flex", alignItems: "row", justifyContent: "space-around" }}>
      <FaArrowAltCircleLeft onClick={() => beforePage()} />
      <div>{page}</div>
      <FaArrowAltCircleRight onClick={() => nextPage()} />
      <Form onSubmit={handleSubmit}>
        <Input
          type="number"
          min="1"
          onChange={handleChange}
          value={size}
        />
        <Button outline color="success" className="btn sm" style={{ textDecoration: "none" }}>
          SELECCIONAR
        </Button>
      </Form>

    </div>
  );

  const modal = getErrorModal(setVisible, visible, message);

  return (
    <div className="admin-page-container">
      <h1 className="text-center">Usuarios</h1>
      {alerts.map((a) => a.alert)}
      {modal}
      <div>
        <Table aria-label="users" className="mt-4">
          <thead>
            <tr>
              <th className="text-center">Online</th>
              <th className="text-center">Nombre</th>
              <th className="text-center">Correo Electrónico</th>
              <th className="text-center">Botones</th>
            </tr>
          </thead>
          <tbody>{userList}</tbody>
        </Table>
        {passer}
      </div>
    </div>
  );
}
