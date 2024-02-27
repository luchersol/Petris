import React, { useState } from "react";
import { Link } from "react-router-dom";
import { Form, Input, Label } from "reactstrap";
import tokenService from "../services/token.service";
import authService from "../auth/AuthService";
import getErrorModal from "../util/getErrorModal";
import PrivateCodeModal from "../components/other/privateCodeModal";

const creatorPlayer = await authService.getPlayer();


export default function CreateMatch() {
  const jwt = tokenService.getLocalAccessToken();

  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [match, setMatch] = useState({
    id: null,
    name: null,
    startDate: null,
    endDate: null,
    code: null,
    numTurn: 1,
    contaminationLevelBlue: 0,
    contaminationLevelRed: 0,
    isPrivated: false,
    winner: null,
    creator: creatorPlayer,
    player: null,
    petriDishes: null,
  });

  function handleChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    setMatch({ ...match, [name]: value });
  }

  function handleSwitchChange() {
    setMatch({ ...match, isPrivated: !match.isPrivated });
  }

  async function handleSubmit(event) {
    event.preventDefault();
    const matchCreated = await fetch("/petris/matches", {
      method: "POST",
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(match),
    })
      .then((response) => response.json())
      .catch((message) => alert(message));
    setMatch(matchCreated);
    if (!matchCreated.isPrivated) {
      window.location.href = `/match/${matchCreated.id}`;
    }

  }


  const modal = getErrorModal(setVisible, visible, message);

  return (
    <div>
      {match.code === null ?
        <div className="auth-page-container">
          <h2>CREAR PARTIDA</h2>
          {modal}
          <div className="auth-form-container">
            <Form onSubmit={handleSubmit}>
              <div className="custom-form-input">
                <Label for="name" className="custom-form-input-label">
                  Nombre de la partida
                </Label>
                <Input
                  type="text"
                  required
                  name="name"
                  id="name"
                  onChange={handleChange}
                  className="custom-input"
                />
              </div>

              <div className="custom-form-input">
                <Label className="custom-form-input-label">
                  Partida privada
                </Label>
                <Input
                  type="checkbox"
                  onChange={handleSwitchChange}
                  checked={match.isPrivated}
                />
              </div>

              <div className="custom-button-row">
                <button className="auth-button">Guardar</button>
                <Link
                  to={`/`}
                  className="auth-button"
                  style={{ textDecoration: "none" }}
                >
                  Cancelar
                </Link>
              </div>
            </Form>
          </div>
        </div>
        :
        <PrivateCodeModal
          onConfirm={() => window.location.href = `/match/${match.id}`}
          code={match.code}
        />}
    </div>


  );
}
