class TokenService {
    getLocalRefreshToken() {
        const user = JSON.parse(localStorage.getItem("user"));
        return user?.refreshToken;
    }

    // getLocalAccessToken() {
    //     const user = JSON.parse(localStorage.getItem("user"));
    //     return user?.token;
    // }

    getLocalAccessToken() {
        const jwt = JSON.parse(localStorage.getItem("jwt"));
        return jwt ? jwt : null;
    }

    updateLocalAccessToken(token) {
        window.localStorage.setItem("jwt", JSON.stringify(token));
    }

    // updateLocalAccessToken(token) {
    //     let user = JSON.parse(localStorage.getItem("user"));
    //     user.token = token;
    //     window.localStorage.setItem("user", JSON.stringify(user));
    // }

    getUser() {
        return JSON.parse(localStorage.getItem("user"));
    }

    setUser(user) {
        window.localStorage.setItem("user", JSON.stringify(user));
    }

    removeUser() {
        fetch(`/petris/users/${this.getUser()?.id}/logout`, {
            method: "PUT",
            headers: {
              Authorization: `Bearer ${this.getLocalAccessToken()}`,
              Accept: "application/json",
              "Content-Type": "application/json",
            }
          });

        window.localStorage.removeItem("user");
        window.localStorage.removeItem("jwt");
    }

}
const tokenService = new TokenService();

export default tokenService;