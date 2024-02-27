import React, { useEffect, useState } from "react";
import { Button, Modal, ModalBody, ModalHeader } from "reactstrap";

export default function LeaveGame({otherPlayer, setWinner}) {

    const [visible, setVisible] = useState(false);
    
    const openWindow = (event) => {
        if(event.key === 'Escape') 
            setVisible(true);
    }
    const closeWindow = () => setVisible(false);
    const selectWinner = () => {
        if(otherPlayer === null) {
            window.location.href = "/";
        } else {
            setWinner(otherPlayer);
        }
        setVisible(false);
    }
    
    useEffect(() => {
        window.addEventListener("keydown", openWindow);
    }, []);
    
    return (
    <Modal style={{ position: "flex", left: "15%", top: "50%", transform: "translate(-50%, -50%)"}} 
            isOpen={visible}>
        <ModalHeader>¿DESEA ABANDONAR?</ModalHeader>
        <ModalBody style={{display: "flex", justifyContent: "space-around", marginTop: 50, margin: 50}}>
            <Button onClick={() => selectWinner()}>SI</Button>
            <Button  onClick={() => closeWindow()}>NO</Button>
        </ModalBody>
    </Modal>
    );
}