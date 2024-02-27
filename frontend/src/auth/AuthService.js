import tokenService from "../services/token.service.js";

class AuthService {

  getUser() {
    const user = JSON.parse(localStorage.getItem("user"));
    return user;
  }


  async getPlayer() {
    const user = JSON.parse(localStorage.getItem("user"));
    const player = user?.roles[0] === "PLAYER" ? await fetch(`/petris/players/user/${user.id}`, {
      method: 'GET',
      headers: {
        "Authorization": `Bearer ${tokenService.getLocalAccessToken()}`,
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
    })
      .then(response => response.json()).catch(error => alert(error)) : null;
    return player
  }

}

const authService = new AuthService();


export default authService;