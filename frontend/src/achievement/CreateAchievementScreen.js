import React, { useState } from "react";
import { Button, Form, FormGroup, Label, Input, Row, Col } from "reactstrap";
import { Link } from "react-router-dom";
import tokenService from "../services/token.service";
import getErrorModal from "../util/getErrorModal";
import authService from "../auth/AuthService";

const jwt = tokenService.getLocalAccessToken();

export default function CreateAchievement() {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
    
  const [achievement, setAchievement] = useState(
    {
      name: "",
      badgeImage: "",
      description: "",
      meter: "", 
      numCondition: 1, 
    }
  );

  function handleChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    setAchievement({ ...achievement, [name]: value });
  }

  function handleSubmit() {
    const {id} = authService.getUser();

    fetch(`/petris/achievements?userId=${id}`, {
      method: "POST",
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
    })
  }

  const modal = getErrorModal(setVisible, visible, message);

  return (
    <div className="admin-page-container">
      <h1 className="text-center">Nuevo Logro</h1>
      {modal}
      <Form>
        <Row form>
          <Col md={6}>
            <FormGroup>
              <Label for="name">Nombre</Label>
              <Input
                type="text"
                id="name"
                name="name"
                value={achievement.name}
                onChange={handleChange}
              />
            </FormGroup>
          </Col>
        </Row>
        <FormGroup>
          <Label for="description">Descripción</Label>
          <Input
            type="textarea"
            id="description"
            name="description"
            value={achievement.description}
            onChange={handleChange}
          />
        </FormGroup>
        <Row form>
        <Col md={6}>
        <FormGroup>
              <Label for="meter">Tipo de Medidor</Label>
              <Input
                type="select"
                id="meter"
                name="meter"
                value={achievement.meter}
                onChange={handleChange}
              >
                
                <option value="">Tipo</option>
                <option value="DEFEAT">Derrota</option>
                <option value="VICTORY">Victoria</option>
                <option value="MATCH">Partida</option>
              </Input>
            </FormGroup>
          </Col>
          <Col md={6}>
            <FormGroup>
              <Label for="numCondition">Condición</Label>
              <Input
                type="number"
                id="numCondition"
                name="numCondition"
                value={achievement.numCondition}
                onChange={handleChange}
                min="1"
              />
            </FormGroup>
          </Col>
        </Row>
        <div className="custom-button-row">
        <Link
              onClick={() => handleSubmit()}
              to={`/achievements`}
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
  );
}
