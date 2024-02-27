import React, { useState, useEffect } from 'react';
import { Navbar, NavbarBrand, NavLink, NavItem, Nav, NavbarText, NavbarToggler, Collapse } from 'reactstrap';
import { Link } from 'react-router-dom';
import tokenService from './services/token.service';
import jwt_decode from "jwt-decode";
import authService from './auth/AuthService';

export default function AppNavbar() {
    const user = authService.getUser();
    const id = user === null ? null : user.id;

    const [roles, setRoles] = useState([]);
    const [username, setUsername] = useState("");
    const jwt = tokenService.getLocalAccessToken();
    const [collapsed, setCollapsed] = useState(true);

    const toggleNavbar = () => setCollapsed(!collapsed);

    useEffect(() => {
        if (jwt) {
            setRoles(jwt_decode(jwt).authorities);
            setUsername(jwt_decode(jwt).sub);
        }
    }, [jwt])

    let publicLinks = <></>;
    let adminLinks = <></>;
    let playerLinks = <></>;
    let userLinks = <></>;
    let userLogout = <></>;

    roles.forEach((role) => {
    
        if (role === "PLAYER") {
            playerLinks = (
                <>
                    <NavItem>
                        <NavLink style={{ color: "white" }} tag={Link} to={`/users/${id}`}>Mi perfil</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink style={{ color: "white" }} tag={Link} to="/achievements">Logros</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink style={{ color: "white" }} tag={Link} to={`/matches/${id}`}>Mis Partidas</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink style={{ color: "white" }} tag={Link} to="/stats">Estadisticas</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink style={{ color: "white" }} tag={Link} to="/friends">Mis Amigos</NavLink>
                    </NavItem>
                    <NavItem>
                        <NavLink style={{ color: "white" }} id="notify" tag={Link} to="/notify">Notificaciones</NavLink>
                    </NavItem>
                </>
            )
        }
    })

    
    if (!jwt) {
        publicLinks = (
            <>
                <NavItem>
                    <NavLink style={{ color: "white" }} id="docs" tag={Link} to="/docs">Documentación</NavLink>
                </NavItem>
                <NavItem>
                    <NavLink style={{ color: "white" }} id="about" tag={Link} to="/about">Qué es Petris</NavLink>
                </NavItem>
                <NavItem>
                    <NavLink style={{ color: "white" }} id="register" tag={Link} to="/register">Crear cuenta</NavLink>
                </NavItem>
                <NavItem>
                    <NavLink style={{ color: "white" }} id="login" tag={Link} to="/login">Iniciar sesión</NavLink>
                </NavItem>
            </>
        )
    } else {
        userLinks = (
            <>
            </>
        )
        userLogout = (
            <>
                <NavItem>
                    <NavLink style={{ color: "white" }} id="docs" tag={Link} to="/docs">Documentación</NavLink>
                </NavItem>
                <NavItem>
                    <NavLink style={{ color: "white" }} id="about" tag={Link} to="/about">Qué es Petris</NavLink>
                </NavItem>
                <NavbarText style={{ color: "white" }} className="justify-content-end">
                    {username}
                </NavbarText>
                <NavItem className="d-flex">
                    <NavLink style={{ color: "white" }} id="logout" tag={Link} to="/logout">Cerrar sesión</NavLink>
                </NavItem>
            </>
        )

    }

    return (
        <div>
            <Navbar expand="md" dark color="dark">
                <NavbarBrand href="/">
                    <img alt="logo" src="/LogoJuego.png" style={{ height: 40, width: 70 }} />
                     
                </NavbarBrand>
                <NavbarToggler onClick={toggleNavbar} className="ms-2" />
                <Collapse isOpen={!collapsed} navbar>
                    <Nav className="me-auto mb-2 mb-lg-0" navbar>
                        {userLinks}
                        {adminLinks}
                        {playerLinks}
                    </Nav>
                    <Nav className="ms-auto mb-2 mb-lg-0" navbar>
                        {publicLinks}
                        {userLogout}
                    </Nav>
                </Collapse>
            </Navbar>
        </div>
    );
}
