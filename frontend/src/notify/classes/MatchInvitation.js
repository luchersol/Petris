import CustomNotification from "./CustomNotification";
import deleteFromList from "../../util/deleteFromList";
import saveItem from "../../util/saveItem";

export class MatchInvitation extends CustomNotification {

    static type() {
        return CustomNotification.MATCH;
    }
    message() {
        return `${this.notification.author.user.username} quiere jugar una partida.`;
    }
    accept() {
        const url = `/petris/matches/join/${this.notification.match.id}/${this.player.id}`;
        saveItem(url, "PUT", null, this.jwt, this.setMessage, this.setVisible);
        window.location.href = `/match/${this.notification.match.id}`;
    }
    reject() {
        const { id } = this.notification;
        const url = `/petris/matchInvitation/${id}/reject`;
        deleteFromList(url, id, [this.state, this.setState], [this.alerts, this.setAlerts], this.setMessage, this.setVisible);
    }

};