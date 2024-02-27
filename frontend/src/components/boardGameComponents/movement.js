import React from "react";
import { Form, Input } from "reactstrap";
import Board from "../../boardGame/classes/Board";

export default function Movement({onSubmit, onChange, board = new Board(), source, destination, modal}){
    return (
    <Form onSubmit={onSubmit}>
        {modal}
        <div style={{ fontSize: 12, position: "absolute", left: `${6 + (board.getColor() === "red" ? 70 : 0)}%`, top: "10%" }}>
            <div style={{ width: "75%", display: "flex", flexDirection: "row" }}>
                SOURCE:
                <Input
                    type="number"
                    style={{ width: 50, height: 30, fontSize: 12 }}
                    required
                    name="source"
                    id={source}
                    min={0}
                    max={6}
                    onChange={onChange}
                    value={source}
                />
                {`QUANTITY: ${board.getQuantity(source, board.getColor())}`}
            </div>
            <div>
                {board.getAdyacent(source).map(i => (
                    <div style={{ display: "flex", flexDirection: "row" }}>
                        {`DESTINATION (${i}) => QUANTITY`}
                        <Input
                            type="number"
                            style={{ width: 50, height: 30, fontSize: 12 }}
                            required
                            name={"destination"}
                            id={i}
                            min={0}
                            max={4}
                            onChange={onChange}
                            value={destination[i]}
                        />

                    </div>
                ))}
            </div>
            <button type="submit">NEXT</button>
        </div>
    </Form>
    );
}