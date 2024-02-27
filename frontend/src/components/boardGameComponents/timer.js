import React, { useEffect, useState } from "react";

export default function Timer({ style, onReset, board, setWinner }) {
    const INITIAL_COUNTER = 60;

    const [counter, setCounter] = useState(INITIAL_COUNTER);
    const [stop, setStop] = useState(false);

    useEffect(() => {
        let interval;
        if (!stop) {
            if (counter > 0) {
                interval = setInterval(() => {
                    setCounter(prevTime => prevTime - 1);
                }, 1000);
            } else {
                setWinner(board.getOtherPlayer());
                setStop(true);
                clearInterval(interval);
            }
        }
        return () => clearInterval(interval);
    });

    useEffect(() => {
        setCounter(INITIAL_COUNTER);
    }, [onReset])

    const formattedTime = () => {
        const minutes = Math.floor(counter / 60);
        const seconds = counter % 60;
        return `${String(minutes).padStart(2, '0')}:${String(seconds).padStart(2, '0')}`;
    };

    function getColorTime() {
        const interval = INITIAL_COUNTER / 4;
        const firtTime = 3 * interval;
        const secondTime = 2 * interval;
        const thirdTime = interval;
        return counter > firtTime ? "green" :
            counter > secondTime ? "yellow" :
                counter > thirdTime ? "orange" : "red";
    }

    return (
        <div style={{ ...style, border: "2px solid black", width: "5%", fontSize: 25, color: getColorTime() }}>
            {formattedTime()}
        </div>
    );
}