import React, { useEffect, useState } from "react";
import authService from "../auth/AuthService.js";
import BoardComponent from "../components/boardGameComponents/boardComponent.js";
import Chat from "../components/boardGameComponents/chat.js";
import CounterChips from "../components/boardGameComponents/counterChips.js";
import CounterContamination from "../components/boardGameComponents/counterContamination.js";
import CounterTurn from "../components/boardGameComponents/counterTurn.js";
import FinalMessage from "../components/boardGameComponents/finalMessage.js";
import LeaveGame from "../components/boardGameComponents/leaveGame.js";
import Movement from "../components/boardGameComponents/movement.js";
import Timer from "../components/boardGameComponents/timer.js";
import Waiting from "../components/boardGameComponents/waiting.js";
import tokenService from "../services/token.service.js";
import getErrorModal from "../util/getErrorModal.js";
import getIdFromUrl from "../util/getIdFromUrl.js";
import saveItem from "../util/saveItem.js";
import Board from "./classes/Board.js";



export default function BoardGame() {
    const userId = tokenService.getUser()?.id;
    const matchId = getIdFromUrl(2);
    const jwt = tokenService.getLocalAccessToken();
    const update = async (newMatch, finish = false) => {
        await saveItem(`/petris/matches/${matchId}?finish=${finish}`, "PUT", newMatch, jwt, setMessage, setVisible);
    };

    // Estados del modal
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);

    // Reseteo del temporizador
    const [reset, setReset] = useState(false);

    const [winner, setWinner] = useState();
    const [match, setMatch] = useState({});
    const [research, setResearch] = useState(false);
    const [friends, setFriends] = useState([])

    //El estado baseQuantity simboliza la cantiddad inicial de fichas de cada tipo en el tablero.
    //Se utiliza para que en caso de querer cambiar de placa a modificar, resetear los valores que se
    //habían estado cambiando del tablero a como estaban al inicio del turno
    const [baseQuantity, setBaseQuantity] = useState([0, 1, 2, 3, 4, 5, 6].reduce((acc, elem) => {
        acc[elem] = { [Board.RED]: Number(elem === 2), [Board.BLUE]: Number(elem === 4) }
        return acc;
    }, {}));
    const baseDestination = { 0: 0, 1: 0, 2: 0, 3: 0, 4: 0, 5: 0, 6: 0 };
    const [destination, setDestination] = useState(baseDestination);
    const [board, setBoard] = useState(Board.of(baseQuantity, 1, null, null, 0, 0));
    const [source, setSource] = useState(2);
    const [dawn, setDawn] = useState(false);

    async function initial(){
        const fetchedMatch = await fetch(`/petris/matches/${matchId}`, {
            headers: {
                "Authorization": `Bearer ${jwt}`,
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
        })
        .then(response => response.json())
        .catch(error => {
            setMessage("Error de conexión a datos de partida")
            setVisible(true)
        })
        
        if(fetchedMatch){
            const matchNotInicialize = Object.keys(match).length === 0;
            const changeTurn = match.numTurn < fetchedMatch.numTurn;
            const startGame = match.player === null && fetchedMatch.player !== null;
            const winnerSelected = winner === null && fetchedMatch.winner !== null;     
            if(matchNotInicialize || changeTurn || startGame || winnerSelected){
                setMatch(fetchedMatch);
            }
        }
    }

    useEffect(() => {
        initial();
    }, [])

    useEffect(()=>{
        async function getFriends(){
            const player = await authService.getPlayer();
            const friends = await fetch(`/petris/players/allFriends/${player.id}`)
                .then(response => response.json())
                .catch(error => alert("Error al obtener amigos"));

            setFriends(friends);
        }
        getFriends();
    },[])

    useEffect(() => {
        let interval;
        interval = setInterval(() => {
            setResearch(param => !param);
            }, 2000);

        return () => clearInterval(interval); 
    }, [])

    // Crear al inicio intervalo para inicializar e ir actualizando de tablero
    useEffect(() => {
        initial();
    }, [research])

    // Selecciona al ganador si no es empate y termina la partida
    useEffect(() => {
        if ((winner !== null || dawn) && Object.keys(match).length !== 0) {
            const newMatch = match
            for (let i = 0; i < newMatch.petriDishes.length; i++) {
                newMatch.petriDishes[i].chipsPlayerRed = board.quantity[i][Board.RED];
                newMatch.petriDishes[i].chipsPlayerBlue = board.quantity[i][Board.BLUE];
            }
            newMatch.winner = !dawn ? winner : null;
            update(newMatch, true);
        }
    }, [winner, dawn])


    // Inicialización de estados
    useEffect(() => {
        setSource(board.nextSource());
        const newQuantity = parsePetriDish();
        setBaseQuantity(newQuantity);
        setBoard(board.updateQuantity(newQuantity)
            .updateNumTurn(match.numTurn)
            .updatePlayerBlue(match.creator)
            .updatePlayerRed(match.player)
            .updateLvContBlue(match.contaminationLevelBlue)
            .updateLvContRed(match.contaminationLevelRed));
        setWinner(match.winner);
    }, [match, board.numTurn]);

    // Realizar movimiento
    useEffect(() => {
        if (winner === null)
            play();
    }, [winner, board.numTurn]);

    function parsePetriDish() {
        const petriDishes = match.petriDishes;
        const newQuantity = [0, 1, 2, 3, 4, 5, 6].reduce((acc, elem) => {
            acc[elem] = { [Board.RED]: 0, [Board.BLUE]: 0 }
            return acc;
        }, {});
        for (let i = 0; i < petriDishes?.length; i++) {
            newQuantity[i][Board.BLUE] = petriDishes[i].chipsPlayerBlue;
            newQuantity[i][Board.RED] = petriDishes[i].chipsPlayerRed;
        }
        return newQuantity;
    }

    function doMovement(event) {
        event.preventDefault();
        const checkSomeMove = Object.keys(destination).some(key => destination[key] > 0);
        if (!checkSomeMove || !board.isValid()) {
            let message = "";
            if(!checkSomeMove) message += "Se necesita hacer algún movimiento\n";
            if(!board.isValid())  message += "No puede haber un mismo número de bacterias rojas y azules en una misma placa\n";
            setMessage(message);
            setVisible(true);
        } else {
            const newMatch = match
            for (let i = 0; i < newMatch.petriDishes.length; i++) {
                newMatch.petriDishes[i].chipsPlayerRed = board.quantity[i][Board.RED];
                newMatch.petriDishes[i].chipsPlayerBlue = board.quantity[i][Board.BLUE];
            }
            newMatch.numTurn = board.getNextTurn();
            setSource(board.nextSource());
            setDestination(baseDestination);
            setBoard(board.updateNumTurn());
            setReset(!reset);
            update(newMatch);
        }
    }

    function calculeFision() {
        const newQuantity = board.quantity;
        for (let i = 0; i < Object.keys(board.quantity).length; i++) {
            const checkColor = (color) => {
                const otherColor = board.getOtherColor(color);
                const monoColor = board.getQuantity(i, otherColor) === 0 && board.getQuantity(i, color) > 0 && board.getQuantity(i, color) < 5;
                if (monoColor) {
                    newQuantity[i][color] += 1;
                }
            }
            checkColor(Board.RED);
            checkColor(Board.BLUE);
        }

        const newMatch = match
        for (let i = 0; i < newMatch.petriDishes.length; i++) {
            newMatch.petriDishes[i].chipsPlayerRed = newQuantity[i][Board.RED];
            newMatch.petriDishes[i].chipsPlayerBlue = newQuantity[i][Board.BLUE];
        }
        newMatch.numTurn = board.getNextTurn();
        return { newMatch, newQuantity };
    }
    
    function doFision() {
        const { newQuantity, newMatch } = calculeFision();
        let newBoard = board.updateQuantity(newQuantity);
        let { winner, dawn } = newBoard.winnerByChips();
        if(winner !== null){
            newBoard = board;
        } else {
            if(!dawn){
                newBoard = newBoard.updateNumTurn();
            }
        }
        setWinner(winner);
        setDawn(dawn);
        setBoard(newBoard);
        update(newMatch);
    }

    function doContamination() {
        const { newMatch, newQuantity } = calculeFision();
        let newLvContBlue = board.lvContBlue;
        let newLvContRed = board.lvContRed;
        for (let i = 0; i < Object.keys(board.quantity).length; i++) {
            const qRed = board.getQuantity(i, Board.RED);
            const qBlue = board.getQuantity(i, Board.BLUE);
            if (qRed !== 0 && qBlue !== 0) {
                if (qRed > qBlue && newLvContRed < Board.LIMIT_CONTAMINATION) newLvContRed++;
                if (qBlue > qRed && newLvContBlue < Board.LIMIT_CONTAMINATION) newLvContBlue++;
            }
        }
        newMatch.contaminationLevelBlue = newLvContBlue;
        newMatch.contaminationLevelRed = newLvContRed;
        let newBoard = board.updateQuantity(newQuantity)
            .updateLvContBlue(newLvContBlue)
            .updateLvContRed(newLvContRed);
        let { winner, dawn } = newBoard.winnerByChips();
        if(winner !== null){
            newBoard = board.updateLvContBlue(newLvContBlue)
                            .updateLvContRed(newLvContRed);
        }else{
            if(!dawn){
                const recalculate = board.isFinalTurn() ? newBoard.winnerByFinish() : newBoard.winnerByContamination();
                winner = recalculate["winner"];
                dawn = recalculate["dawn"];
            }
        }

        if(winner === null && !dawn && !board.isFinalTurn()){
            newBoard = newBoard.updateNumTurn();
        }

        setWinner(winner);
        setDawn(dawn);
        setBoard(newBoard);
        update(newMatch);
    }

    function handleChange(event) {
        // event.preventDefault();
        const checkValidValue = (value, id, color) => value >= 0 && (value + baseQuantity[id][color]) <= 5;
        const checkValidSource = (value) => value >= 0 && value <= 6;
        const { target } = event;
        const { name } = target;
        let value = Number(target.value);
        const id = Number(target.id);
        let color = board.getColor()
        if (isNaN(value) || isNaN(id) || color === null) return;
        if (name === "source") {
            if (checkValidSource(value)) {
                let variation = value > source ? 1 : -1;
                while (board.getQuantity(value) === 0 || board.getQuantity(value) === 5) {
                    value += variation;
                    if (!checkValidSource(value)) {
                        return;
                    }
                }
                setSource(value);
                setDestination(baseDestination);
                setBoard(board.updateQuantity(baseQuantity));
            }
        } else if (name === "destination") {
            if (checkValidValue(value, id, color)) {
                let variation = value > destination[id] ? 1 : -1;

                let newQuantitySource = board.getQuantity(source) - variation;
                let newQuantityDestination = board.getQuantity(id) + variation;

                const checkEnoughtSarcinas = (newQuantityDestination >= 5) && (board.countSarcinsByColor() >= 4)
                const checkEnoughtBacteris = newQuantitySource < 0;
                const checkThereIsSacrina = newQuantityDestination > 5;
                const isValid = !(
                    checkEnoughtSarcinas ||
                    checkEnoughtBacteris ||
                    checkThereIsSacrina);
                if (isValid) {
                    setDestination({ ...destination, [id]: value });
                    setBoard(board.updateQuantity({
                        ...board.quantity,
                        [source]: { ...board.quantity[source], [color]: newQuantitySource },
                        [id]: { ...board.quantity[id], [color]: newQuantityDestination }
                    }))
                }
            }
        }
    }

    function play() {
        if (match) {
            switch (board.getTypeTurnByIndex()) {
                case Board.CONTAMINATION_STAGE: doContamination(); break;
                case Board.FISION_STAGE: doFision(); break;
                case Board.MOVEMENT_PLAYER_BLUE: 
                    if(board.cantMove()){
                        setWinner(board.playerRed)
                    }
                    break;
                case Board.MOVEMENT_PLAYER_RED: 
                    if(board.cantMove()){
                        setWinner(board.playerBlue)
                    }
                    break;
                default:
            }
        }
    }

    const modal = getErrorModal(setVisible, visible, message);

    return (match && match.length !== 0 &&
        <div>
            <Waiting friends={friends} isCreator={userId === board.playerBlue?.user?.id} matchId={match.id} isVisible={!board.thereIsTwoPlayer()}/>
            <BoardComponent quantity={board.quantity} />
            <CounterTurn actualTurn={board.numTurn} style={{ position: "absolute", left: "24%", top: "10%" }} />
            <CounterContamination id="contaminationBlue" levelContamination={board.lvContBlue} size={40} color={Board.BLUE} style={{ position: "absolute", left: "1%", top: "30%" }} />
            <CounterContamination id="contaminationRed" levelContamination={board.lvContRed} size={40} color={Board.RED} style={{ position: "absolute", left: "95%", top: "30%" }} />
            <CounterChips player={board.playerRed} color={Board.RED} numBacteriasUsadas={board.countBacteriumByColor(Board.RED)} numSarcinasUsadas={board.countSarcinsByColor(Board.RED)} style={{ position: "absolute", left: "83%", top: "50%" }} />
            <CounterChips player={board.playerBlue} color={Board.BLUE} numBacteriasUsadas={board.countBacteriumByColor(Board.BLUE)} numSarcinasUsadas={board.countSarcinsByColor(Board.BLUE)} style={{ position: "absolute", left: "10%", top: "50%" }} />
            {board.thereIsTwoPlayer() &&
            board.isPlayerTurn() && 
            board.isYourTurn(userId) &&
            winner === null &&
                <>
                    <Movement userId={userId} onSubmit={(event) => doMovement(event)} onChange={(event) => handleChange(event)} board={board} source={source} destination={destination} modal={modal} key="movement" />
                    <Timer onReset={reset} board={board} setWinner={setWinner} />
                </>}
            <Chat jwt={jwt} user_id={userId} match_id={matchId} />
            <LeaveGame otherPlayer={board.getOtherPlayerByUserId(userId)} setWinner={setWinner} />
            <FinalMessage friends={friends} board={board} userId={userId} winner={winner} dawn={dawn} />
        </div>
    );
}
