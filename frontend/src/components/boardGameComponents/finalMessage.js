import React, { useState } from "react";
import { Link } from "react-router-dom";
import { Button, Modal, ModalBody, ModalHeader, NavLink } from "reactstrap";
import saveItem from "../../util/saveItem";
import tokenService from "../../services/token.service.js";
import getErrorModal from "../../util/getErrorModal.js";
import Board from "../../boardGame/classes/Board.js";

export default function FinalMessage({board = new Board(), userId, winner = null, dawn = false }) {

    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const jwt = tokenService.getLocalAccessToken();
    const invitar = () => {
        const friendRequest = { isAccepted: false, 
                                author: board.getPlayerByUserId(userId), 
                                receiver: board.getOtherPlayerByUserId(userId) };
        saveItem("/petris/players/friendRequest", "POST", friendRequest, jwt, setMessage, setVisible);
    };
    const checkNotBeFriends = () => {
        return !board.playerBlue?.beFriends.some(friend => friend.id === board.playerRed?.id) &&
               !board.playerRed?.beFriends.some(friend => friend.id === board.playerBlue?.id);
    }

    const modal = getErrorModal(setVisible, visible, message);

    return (
        <Modal style={{ position: "flex", left: "15%", top: "50%", transform: "translate(-50%, -50%)" }}
            isOpen={winner !== null || dawn}>
            <div style={{ ...style }}>
                {modal}
                <ModalHeader>JUEGO TERMINADO</ModalHeader>
                <ModalBody style={{ ...style }}>
                    <h3> {dawn ? "EMPATE" : `EL GANADOR ES ${winner?.user.username}`}</h3>
                    <div style={{width:"100%", display:"flex", alignItems:"center",  justifyContent:"space-around"}}>
                        <Button>
                            <NavLink tag={Link} to="/" >SALIR</NavLink>
                        </Button>
                        {board.isPlayer(userId) && checkNotBeFriends() &&
                            <Button onClick={() => invitar()}>
                                <NavLink tag={Link} to="/" >AÑADIR AMIGO</NavLink>
                            </Button>}
                    </div>
                </ModalBody>
            </div>
        </Modal>);
}

const style = {
    display: "flex",
    flexDirection: "column",
    alignItems: "center",
    justifyContent: "space-around"
}