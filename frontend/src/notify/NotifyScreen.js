import React, { useEffect, useState } from "react";
import { Table } from "reactstrap";
import authService from "../auth/AuthService";
import tokenService from "../services/token.service";
import getErrorModal from "../util/getErrorModal";
import useFetchState from "../util/useFetchState";
import { FriendInvitation } from "./classes/FriendInvitation";
import { MatchInvitation } from "./classes/MatchInvitation";

export default function Notifications() {

    const jwt = tokenService.getLocalAccessToken();
    const [player, setPlayer] = useState({});
    const [alerts, setAlerts] = useState();
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);

    useEffect(() => {
        const init = async () => {
            const p = await authService.getPlayer();
            setPlayer(p)
        }
        init();
    }, [])

    const [friendRequest, setFriendRequest] = useFetchState(
        [],
        `/petris/players/friendRequest?receiverId=${player.id}`,
        jwt,
        setMessage,
        setVisible,
        null
    );

    const [matchInvitations, setMatchInvitations] = useFetchState(
        [],
        `/petris/matchInvitation?receiverId=${player.id}`,
        jwt,
        setMessage,
        setVisible,
        null
    );

    function showNotifications(notifications, type) {
        return (notifications.map(notification => {
            const notify = type === FriendInvitation.type()  ?
                new FriendInvitation(notification, player, jwt, [friendRequest, setFriendRequest], [alerts, setAlerts], setMessage, setVisible) :
                type === MatchInvitation.type() ?
                    new MatchInvitation(notification, player, jwt, [matchInvitations, setMatchInvitations], [alerts, setAlerts], setMessage, setVisible) :
                    null;
            return notify.show()
        })
        );
    }

    const modal = getErrorModal(setVisible, visible, message);

    return (
        <div>
            {modal}
            <h1 className="text-center">NOTIFICACIONES</h1>
            <Table className="mt-4">
                <thead>
                    <tr>
                        <th className="text-center">Mensage</th>
                        <th className="text-center">Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    {showNotifications(friendRequest, FriendInvitation.type())}
                    {showNotifications(matchInvitations, MatchInvitation.type())}
                </tbody>
            </Table>

        </div>
    );
}
