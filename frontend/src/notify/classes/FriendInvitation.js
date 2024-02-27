import CustomNotification from "./CustomNotification";
import deleteFromList from "../../util/deleteFromList";
import saveItem from "../../util/saveItem";

export class FriendInvitation extends CustomNotification {

    static type() {
        return CustomNotification.FRIEND;
    }
    message() {
        return `Solicitud de amistad de ${this.notification.author.user.username}.`;
    }
    accept() {
        const { id } = this.notification;
        const url = `/petris/players/friendRequest/${id}/accept`;
        saveItem(url, "PUT", null, this.jwt, this.setMessage, this.setVisible);
        this.setState(this.state.filter((i) => i.id !== id));    
    }
    reject() {
        const { id } = this.notification;
        const url = `/petris/players/friendRequest/${id}/reject`;
        deleteFromList(url, id, [this.state, this.setState], [this.alerts, this.setAlerts], this.setMessage, this.setVisible);
    }

}