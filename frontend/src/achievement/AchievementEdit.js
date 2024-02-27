import { useState } from "react";
import { Link } from "react-router-dom";
import { Button, Form, FormGroup, Input, Label } from "reactstrap";
import tokenService from "../services/token.service";
import "../static/css/admin/adminPage.css";
import getErrorModal from "../util/getErrorModal";
import getIdFromUrl from "../util/getIdFromUrl";
import useFetchState from "../util/useFetchState";

export default function AchievementEdit() {
  const jwt = tokenService.getLocalAccessToken();
  const id = getIdFromUrl(2);

  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);

  const [achievement, setAchievement] = useFetchState(
    {},
    `/petris/achievements/${id}`,
    jwt,
    setMessage,
    setVisible,
    id
  );

  
  function handleChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    setAchievement({ ...achievement, [name]: value });
  }

  function handleSubmit() {
    fetch(`/petris/achievements/${achievement.id}`, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(achievement),
    })
    .then((response) => response.json())
    .then((json) => {
      if (json.message) {
        setMessage(json.message);
        setVisible(true);
      }
    });
  }

  const modal = getErrorModal(setVisible, visible, message);
  
  return (
    <div className="auth-page-container" style={{marginTop: 20}}>
      {<h2>{"Editar logro"}</h2>}
      {modal}
      <div className="auth-form-container">
        <Form onSubmit={handleSubmit}>
          <div className="custom-form-input">
            <Label for="name" className="custom-form-input-label">
              Nombre
            </Label>
            <Input
              type="text"
              required
              name="name"
              id="name"
              value={achievement.name}
              onChange={handleChange}
              className="custom-input"
            />
          </div>
          <div className="custom-form-input">
            <Label for="description" className="custom-form-input-label">
              Descripcion
            </Label>
            <Input
              type="text"
              required
              name="description"
              id="description"
              value={achievement.description}
              onChange={handleChange}
              className="custom-input"
            />
          </div>
          <FormGroup>
              <Label for="meter">Tipo de Medidor</Label>
              <Input
                type="select"
                id="meter"
                name="meter"
                value={achievement.meter}
                onChange={handleChange}
              >
                {/* Opciones para el tipo de medidor */}
                <option value="">Tipo</option>
                <option value="DEFEAT">Derrota</option>
                <option value="VICTORY">Victoria</option>
                <option value="MATCH">Partida</option>
              </Input>
            </FormGroup>

          <div className="custom-form-input">
            <Label for="numCondition" className="custom-form-input-label">
              Numero de condicion
            </Label>
            <Input
              type="number"
              required
              name="numCondition"
              id="numCondition"
              min="1"
              value={achievement.numCondition}
              onChange={handleChange}
              className="custom-input"
            />
          </div>


          <div className="custom-button-row">
          <Link
              onClick={() => handleSubmit()}
              to={`/achievements/`}
              className="auth-button"
              style={{ textDecoration: "none" }}
            >
              Guardar
            </Link>
        </div>
        <div className="custom-button-row">
        <Button outline color="danger">
            <Link  to="/achievements" style={{color:"black", textDecoration: "none" }}>Cancelar</Link>
        </Button>
          </div>
        </Form>
      </div>
    </div>
  );
}
