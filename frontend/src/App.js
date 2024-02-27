import jwt_decode from "jwt-decode";
import React from "react";
import { ErrorBoundary } from "react-error-boundary";
import { Route, Routes } from "react-router-dom";
import AppNavbar from "./AppNavbar";
import AchievementAdminScreen from "./achievement/AchievementAdminScreen";
import AchievementEdit from "./achievement/AchievementEdit.js";
import AchievementPlayer from "./achievement/AchievementPlayer.js";
import Achievement from "./achievement/AchievementScreen";
import CreateAchievement from "./achievement/CreateAchievementScreen.js";
import Login from "./auth/login/LoginScreen";
import Logout from "./auth/logout/LogOutScreen";
import UserEdit from "./auth/profile/editAccount";
import UserDetails from "./auth/profile/myAccount";
import Register from "./auth/register/RegisterScreen";
import BoardGame from "./boardGame/GameScreen.js";
import Friends from "./friends/FriendsScreen.js";
import Home from "./home/HomeScreen";
import AllMatches from "./match/AllMatchesListScreen.js";
import CreateMatch from "./match/CreateMatchScreen.js";
import MatchesList from "./match/MatchListScreen.js";
import MyMatchesList from "./match/MyMatchesListScreen.js";
import Notifications from "./notify/NotifyScreen.js";
import PrivateRoute from "./privateRoute";
import About from "./public/about";
import SwaggerDocs from "./public/swagger";
import tokenService from "./services/token.service";
import PodiumScreen from "./stats/PodiumScreen.js";
import Stats from "./stats/StatsScreen.js";
import UserMatchesList from "./match/PlayerMatchesAdmin.js";
import UserListAdmin from "./users/UserListAdmin";

function ErrorFallback({ error, resetErrorBoundary }) {
  return (
    <div role="alert">
      <p>Something went wrong:</p>
      <pre>{error.message}</pre>
      <button onClick={resetErrorBoundary}>Try again</button>
    </div>
  )
}

export default function App() {
  const jwt = tokenService.getLocalAccessToken();
  let roles = []
  if (jwt) {
    roles = getRolesFromJWT(jwt);
  }

  function getRolesFromJWT(jwt) {
    return jwt_decode(jwt).authorities;
  }

  let adminRoutes = <></>;
  let userRoutes = <></>;
  let playerRoutes = <></>;
  let publicRoutes = <></>;

  roles.forEach((role) => {
    if (role === "ADMIN") {
      adminRoutes = (
        <>
          <Route path="/users" exact={true} element={<PrivateRoute><UserListAdmin /></PrivateRoute>} />
          <Route path="/achievements" element={<PrivateRoute> <AchievementAdminScreen/> </PrivateRoute>} />
          <Route path="/achievements/:achievementId/edit" exact={true} element={<PrivateRoute> <AchievementEdit/> </PrivateRoute>} />
          <Route path="/achievements/new" exact={true} element={<PrivateRoute> <CreateAchievement/> </PrivateRoute>} />
          <Route path="/matches" element={<PrivateRoute> <AllMatches/> </PrivateRoute>} />
          <Route path="/matches/:userId" element={<UserMatchesList/>} />
        </>)
    }
    if(role === "PLAYER"){
      playerRoutes = (
        <>
          <Route path="/achievements" element={<Achievement />} />
          <Route path="/achievements/:playerId" element={<AchievementPlayer />} />
          <Route path="/users/:userId" exact={true} element={<PrivateRoute> <UserDetails/> </PrivateRoute>} />
          <Route path="/users/:userId/edit" exact={true} element={<PrivateRoute> <UserEdit/> </PrivateRoute>} />
          <Route path="/match/:matchId" element={<BoardGame/>} />
          <Route path="/friends" element={<Friends/>} />
          <Route path="/stats" element={<Stats/>}/>
          <Route path="/matches/create" element={<CreateMatch/>} /> 
          <Route path="/matches/:userId" element={<MyMatchesList/>} />
          <Route path="/matches/play" element={<MatchesList/>} />
          <Route path="/notify" element={<Notifications/>} />
          <Route path="/stats/podium" element={<PodiumScreen/>} />
        </>
      )
    }
  })
  if (!jwt) {
    publicRoutes = (
      <>       
        <Route path="/register" element={<Register />} />
        <Route path="/login" element={<Login />} />
      </>
    )
  } else {
    userRoutes = (
      <>
        <Route path="/logout" element={<Logout />} />
      </>
    )
  }

  return (
    <div>
      <ErrorBoundary FallbackComponent={ErrorFallback} >
        <AppNavbar />
        <Routes>
          <Route path="/" exact={true} element={<Home />} />
          <Route path="/about" element={<About />} />
          <Route path="/docs" element={<SwaggerDocs />} />
          {publicRoutes}
          {userRoutes}
          {adminRoutes}
          {playerRoutes}
        </Routes>
      </ErrorBoundary>
    </div>
  );
}
