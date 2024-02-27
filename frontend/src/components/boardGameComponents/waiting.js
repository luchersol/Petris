import React, { useState } from "react";
import { Button, Dropdown, DropdownItem, DropdownMenu, DropdownToggle, Form, Modal, ModalFooter, ModalHeader } from "reactstrap";
import tokenService from "../../services/token.service";
import getErrorModal from "../../util/getErrorModal";

export default function Waiting({friends = [], isCreator, matchId, isVisible }) {
    const jwt = tokenService.getLocalAccessToken();
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [dropdownOpen, setDropdownOpen] = useState(false);
    const [selectedOption, setSelectedOption] = useState(null);

    const toggle = () => setDropdownOpen(!dropdownOpen);

    const handleSelect = (option) => {
        setSelectedOption(option);
        setDropdownOpen(false);
    };

    function handleSubmit(event) {
        event.preventDefault();

        fetch(`/petris/matchInvitation?matchId=${matchId}&username=${selectedOption}`, {
            method: "POST",
            headers: {
                Authorization: `Bearer ${jwt}`,
                Accept: "application/json",
                "Content-Type": "application/json",
            }
        })
        .then((json) => {
            setMessage("Mensaje enviado");
            setVisible(true);
        })
        .catch((message) => alert(message));
    }
    const modal = getErrorModal(setVisible, visible, message);

    return (
        <Modal style={{ position: "flex", left: "15%", top: "50%", transform: "translate(-50%, -50%)" }}
            isOpen={isVisible} >
            {modal}
            {isCreator && friends.length > 0 && <>
                <ModalHeader>INVITAR AMIGOS</ModalHeader>
                <Form onSubmit={handleSubmit} style={{ display: "flex", flexDirection: "row" }}>
                    <Dropdown isOpen={dropdownOpen} toggle={toggle}>
                        <DropdownToggle caret>
                            Seleccionar opción
                        </DropdownToggle>
                        <DropdownMenu>
                            {friends.map((friend, index) =>  <DropdownItem key={index} onClick={() => handleSelect(friend.user.username)}>
                                {friend.user.username}
                                </DropdownItem>
                            )}
                        </DropdownMenu>
                        {selectedOption && <p>Opción seleccionada: {selectedOption}</p>}
                    </Dropdown>
                    <Button type="submit">
                        ENVIAR INVITACION
                    </Button>
                </Form>
            </>
            }
            <ModalFooter>ESPERANDO ...</ModalFooter>

        </Modal>
    );

}
