import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { Form, Input, Label } from "reactstrap";
import tokenService from "../../services/token.service";
import "../../static/css/admin/adminPage.css";
import getErrorModal from "../../util/getErrorModal";
import getIdFromUrl from "../../util/getIdFromUrl";

export default function UserEdit() {
  const jwt = tokenService.getLocalAccessToken();
  const id = getIdFromUrl(2);

  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [user, setUser] = useState({});

  useEffect(() => {
    const init = async () => {
      const fetchUser = await fetch(`/petris/users/${id}`).then(response => response.json()).catch(error => alert(error));
      fetchUser.password = "";
      setUser(fetchUser)
    };
    init();
  }, [])
  
  
  function handleChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    setUser({ ...user, [name]: value });
  }

  function handleSubmit(event) {

    event.preventDefault();

    fetch(`/petris/users/${user.id}`, {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      },
      body: JSON.stringify(user),
    })
      .then((response) => response.json())
      .then((json) => {
        if (json.message) {
          setMessage(json.message);
          setVisible(true);
        } else window.location.href = "/";
      })
      .catch((message) => alert(message));
  }

  const modal = getErrorModal(setVisible, visible, message);
  
  return (
    <div className="auth-page-container">
      {<h2>{"Editar perfil"}</h2>}
      {modal}
      <div className="auth-form-container">
        <Form onSubmit={handleSubmit}>
          <div className="custom-form-input">
            <Label for="username" className="custom-form-input-label">
              Nombre de usuario
            </Label>
            <Input
              type="text"
              required
              name="username"
              id="username"
              value={user.username || ""}
              onChange={handleChange}
              className="custom-input"
            />
          </div>
          <div className="custom-form-input">
            <Label for="Password" className="custom-form-input-label">
              Contraseña
            </Label>
            <Input
              type="password"
              required
              name="password"
              id="password"
              value={user.password || ""}
              onChange={handleChange}
              className="custom-input"
            />
          </div>
          <div className="custom-form-input">
            <Label for="Email" className="custom-form-input-label">
              Correo electrónico
            </Label>
            <Input
              type="email"
              required
              name="email"
              id="email"
              value={user.email || ""}
              onChange={handleChange}
              className="custom-input"
            />
          </div>
          
          <div className="custom-button-row">
            <button className="auth-button">Guardar</button>
            <Link
              to={`/users/${user.id}`}
              className="auth-button"
              style={{ textDecoration: "none" }}
            >
              Cancelar
            </Link>
          </div>
        </Form>
      </div>
    </div>
  );
}
