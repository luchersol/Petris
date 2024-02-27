import { Button } from "reactstrap";

export default class CustomNotification {

    static MATCH = "MATCH";
    static FRIEND = "FRIEND";

    constructor(notification, player, jwt, [state, setState], [alerts, setAlerts], setMessage, setVisible){
        this.notification = notification;
        this.player = player;
        this.state = state;
        this.setState = setState;
        this.alerts = alerts;
        this.setAlerts = setAlerts;
        this.jwt = jwt;
        this.setMessage = setMessage;
        this.setVisible = setVisible;
    }

    message() {
        console.log("Metodo no implementado")
    }
    accept() {
        console.log("Metodo no implementado")
    }
    reject() {
        console.log("Metodo no implementado")
    }
    show(){
        return (
        <tr>
            <th className="text-center">{this.message()}</th>
            <th style={{display:"flex", justifyContent:"space-around"}}>
                <Button onClick={() => this.accept()} outline color="success" className="btn sm" style={{ textDecoration: "none" }}>ACEPTAR</Button>
                <Button onClick={() => this.reject()} outline color="danger" className="btn sm" style={{ textDecoration: "none" }}>RECHAZAR</Button>
            </th>
        </tr>);
    }
};