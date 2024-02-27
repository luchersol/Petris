import {useEffect, useState } from "react";
import { Link } from "react-router-dom";
import DeleteModal from "../../components/other/deleteModal";
import tokenService from "../../services/token.service";
import "../../static/css/admin/adminPage.css";
import getIdFromUrl from "../../util/getIdFromUrl";

export default function UserDetails() {
  const jwt = tokenService.getLocalAccessToken();
  const id = getIdFromUrl(2);

  const [user, setUser] = useState({});
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const [userToBeDeleted, setUserToBeDeleted] = useState(null);

  useEffect(() => {
    const init = async () => {
      const fetchUser = await fetch(`/petris/users/${id}`)
          .then(response => response.json())
          .catch(error => alert("Fallo en obtención de usuario"));
      setUser(fetchUser)
    };
    init();
  }, [])

  const handleDeleteUser = () => {
   
    fetch(`/petris/users/${id}`, {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${jwt}`,
        Accept: "application/json",
        "Content-Type": "application/json",
      },
    })
      .then((response) => response.json())
      .then((json) => {
        if (json.message) {
          setMessage(json.message)
          setVisible(true)
        } else {
          window.location.href = "/"
        }
      })
      .catch((error) => console.error("Error al eliminar el usuario:", error))
      tokenService.removeUser()
      window.location.href = "/";
    }
    
  
 


return (
  <div className="auth-page-container">
    <h2 style={{ marginBottom: 20 }}>{"Mi perfil"}</h2>

    {!userToBeDeleted && (
      <div className="user-details-container">
        <div className="user-properties">
          <p>Nombre de usuario: {user.username}</p>
          <p>Correo electrónico: {user.email}</p>
        </div>

        <Link to={`/users/${user.id}/edit`} className="auth-button" style={{ marginRight: 12 }}>
          Editar
        </Link>
        <button
          className="auth-button"
          onClick={() => setUserToBeDeleted(user)}
        >
          Borrar cuenta
        </button>
      </div>
    )}

    <DeleteModal
      isVisible={userToBeDeleted}
      children={"¿Estas seguro? Todos tus datos desaparecerán para siempre."}
      onCancel={() => setUserToBeDeleted(null)}
      onConfirm={() => handleDeleteUser()}
    ></DeleteModal>
  </div>
);
}
