import React, { useEffect, useState } from "react";
import { FaCircle } from "react-icons/fa";
import { Link } from "react-router-dom";
import { Button, ButtonGroup, Form, FormGroup, Input, Label, Table } from "reactstrap";
import authService from "../auth/AuthService";
import tokenService from "../services/token.service";
import getErrorModal from "../util/getErrorModal";
import useFetchState from '../util/useFetchState';

export default function Friends() {

  const jwt = tokenService.getLocalAccessToken();
  const imageOfProfile = "https://cdn-icons-png.flaticon.com/512/6073/6073873.png";

  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [player, setPlayer] = useState({});
  const [allFriendsList, setAllFriendsList] = useState([])

  useEffect(() => {
    const init = async () => {
      const fetchedPlayer = await authService.getPlayer();
      setPlayer(fetchedPlayer);
      if (fetchedPlayer) {
        const friends = await fetch(`/petris/players/allFriends/${fetchedPlayer.id}`, {
          headers: {
            "Authorization": `Bearer ${jwt}`,
          },
        }).then(response => response.json())
          .catch((message) => {
            setMessage('Failed to fetch data');
            setVisible(true);
          });
        setAllFriendsList(friends);
      }
    }
    init();
  }, [])

  const handleDeleteFriend = (friendId) => {

    if (player) {
      fetch(`/petris/players/${player.id}/${friendId}`, {
        method: 'PUT',
        headers: {
          Authorization: `Bearer ${jwt}`,
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify(player),
      }).then((response) => response.json())

        .then(fetch(`/petris/players/friendRequest/player1/${player.id}/player2/${friendId}`, {
          method: "DELETE",
          headers: {
            Authorization: `Bearer ${jwt}`,
            Accept: "application/json",
            "Content-Type": "application/json",
          },
        })
          .then((response) => response.json())
        )

        .then((json) => {
          if (json.message) {
            setMessage("json.message");
            setVisible(true);
          } else window.location.href = "/friends";
        })
        .catch((message) => alert(message));
    } else {
      alert("The friend could not be deleted correctly");
    }
  }

  async function handleExpectFriend(id) {
    const url = `/petris/matches/now?playerId=${id}`;
    const match = await fetch(url, {
      headers: {
        Authorization: `Bearer ${jwt}`,
        "Content-Type": "application/json",
      },
    })
      .then(response => response.json())
      .catch(error => {
        setMessage("No está jugando actualmente")
        setVisible(true)
      })
    if (match) {
      window.location.href = `/match/${match.id}`;
    }
  }

  const friendsList = allFriendsList.map((friend) => {
    return (
      <tr key={friend.id}>
        <td><FaCircle style={{ position: "relative", left: "40%", color: friend.user.online ? 'green' : 'gray' }} /></td>
        <td className="text-center">
          <img src={imageOfProfile} alt={friend.id} width="30px" />
        </td>
        <td>{friend.user.username}</td>
        <td>{friend.user.email}</td>
        <td>
          <ButtonGroup>
            <Button onClick={() => handleExpectFriend(friend.id)} outline color="success" className="btn sm" style={{ textDecoration: "none" }}>
              ESPECTAR
            </Button>
            <Button onClick={() => handleDeleteFriend(friend.id)} outline color="danger" className="btn sm" style={{ textDecoration: "none" }}>
              ELIMINAR
            </Button>

          </ButtonGroup>
        </td>
      </tr>
    );
  });

  const [newName, setNewName] = useState("");


  function handleChange(event) {
    const target = event.target;
    const value = target.value;
    setNewName(value);
  }

  async function handleSubmit() {
    const friend = await fetch(`/petris/players/user/username/${newName}`, {
      method: 'GET',
      headers: {
        "Authorization": `Bearer ${jwt}`,
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
    })
      .then(response => response.json())
      .catch((message) => {
        setMessage("No existe usuario")
        setVisible(true)
      });

    if (friend?.id) {
      const reqBody = {
        isAccepted: false,
        author: player,
        receiver: friend
      };
      fetch(`/petris/players/friendRequest`, {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${jwt}`,
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(reqBody),
      }).then((response) => response.json())
        .then((json) => {
          if (json.message) {
            setMessage(json.message);
            setVisible(true);
          }
        })
        .catch((message) => alert(message));
    }
  }

  const modal = getErrorModal(setVisible, visible, message);

  return (
    <div>
      <div className="auth-page-container">
        <h1 className="text-center">Mis Amigos</h1>
        {modal}
        <div>
          <Table aria-label="friends" className="mt-4">
            <thead>
              <tr>
                <th className="text-center">Online</th>
                <th className="text-center">Perfil</th>
                <th className="text-center">Apodo</th>
                <th className="text-center">Correo</th>
                <th className="text-center">Acciones</th>
              </tr>
            </thead>
            <tbody>{friendsList}</tbody>
          </Table>
        </div>

        <div className="auth-form-container">
          <h3 className="text-center">Añadir nueva amistad</h3>
          <Form onSubmit={handleSubmit}>
            <div className="custom-form-input">
              <FormGroup>
                <Label for="name" className="custom-form-input-label">Nombre</Label>
                <Input
                  type="text"
                  required
                  name="name"
                  id="name"
                  value={newName}
                  onChange={handleChange}
                  className="custom-input"
                />
              </FormGroup>

            </div>
            <div className="custom-button-row">
              <Link
                onClick={() => handleSubmit()}
                to={`/friends`}
                className="auth-button"
                style={{ textDecoration: "none" }}
              >
                ENVIAR
              </Link>
            </div>
          </Form>
        </div>
      </div>
    </div>
  );
}
