export default class Board {

    static CONTAMINATION_STAGE = "CONTAMINATION_STAGE";
    static FISION_STAGE = "FISION_STAGE";
    static MOVEMENT_PLAYER_BLUE = "MOVEMENT_PLAYER_BLUE";
    static MOVEMENT_PLAYER_RED = "MOVEMENT_PLAYER_RED";
    static FINISH = "FINISH";

    static RED = "red";
    static BLUE = "blue";

    static LIMIT_BACTERIUMS = 20;
    static LIMIT_SARCINS = 4;
    static LIMIT_NUM_TURNS = 36;
    static LIMIT_CONTAMINATION = 8;

    constructor(quantity, numTurn, playerBlue, playerRed, lvContBlue, lvContRed) {
        this.quantity = quantity;
        this.numTurn = numTurn;
        this.playerBlue = playerBlue;
        this.playerRed = playerRed;
        this.lvContBlue = lvContBlue;
        this.lvContRed = lvContRed;
    }

    static of(quantity, numTurn, playerBlue, playerRed, lvContBlue, lvContRed) {
        return new Board(quantity, numTurn, playerBlue, playerRed, lvContBlue, lvContRed);
    }

    getQuantity(index, color = this.getColor()) {
        return this.quantity[index][color];
    }

    copy() {
        return Board.of(this.quantity, this.numTurn, this.playerBlue, this.playerRed, this.lvContBlue, this.lvContRed);
    }

    updateQuantity(quantity) {
        const newBoard = this.copy();
        newBoard.quantity = quantity;
        return newBoard;
    }

    updateNumTurn(numTurn = this.getNextTurn()) {
        const newBoard = this.copy();
        newBoard.numTurn = numTurn;
        return newBoard;
    }

    updatePlayerBlue(playerBlue) {
        const newBoard = this.copy();
        newBoard.playerBlue = playerBlue;
        return newBoard;
    }

    updatePlayerRed(playerRed) {
        const newBoard = this.copy();
        newBoard.playerRed = playerRed;
        return newBoard;
    }

    updateLvContBlue(lvContBlue) {
        const newBoard = this.copy();
        newBoard.lvContBlue = lvContBlue;
        return newBoard;
    }

    updateLvContRed(lvContRed) {
        const newBoard = this.copy();
        newBoard.lvContRed = lvContRed;
        return newBoard;
    }

    getNextTurn() {
        return this.numTurn + 1;
    }

    getAdyacent(index = this.nextSource()) {
        let ls = [];
        let color = this.getColor();
        if (color !== null) {
            switch (index) {
                case 0: ls = [1, 2, 3]; break;
                case 1: ls = [0, 3, 4]; break;
                case 2: ls = [0, 3, 5]; break;
                case 3: ls = [0, 1, 2, 4, 5, 6]; break;
                case 4: ls = [1, 3, 6]; break;
                case 5: ls = [2, 3, 6]; break;
                case 6: ls = [3, 4, 5]; break;
                default:
            }
        }
        return ls;
    }

    getPlayerByColor(color){
        return color === Board.BLUE ? this.playerBlue : color === Board.RED ? this.playerRed : null;
    }

    getPlayerByUserId(userId){
        return userId === this.playerBlue?.user?.id ? this.playerBlue : 
               userId === this.playerRed?.user?.id ? this.playerRed : null;
    }

    getActualPlayer() {
        const type = this.getTypeTurnByIndex(this.numTurn);
        return type === Board.MOVEMENT_PLAYER_BLUE ? this.playerBlue :
            type === Board.MOVEMENT_PLAYER_RED ? this.playerRed : null;
    }

    getOtherPlayer() {
        const type = this.getTypeTurnByIndex(this.numTurn);
        return type === Board.MOVEMENT_PLAYER_RED ? this.playerBlue :
            type === Board.MOVEMENT_PLAYER_BLUE ? this.playerRed : null;
    }

    getOtherPlayerByUserId(userId){
        return userId === this.playerBlue?.user?.id ? this.playerRed : 
               userId === this.playerRed?.user?.id ? this.playerBlue : null;
    }

    isPlayerTurn() {
        return this.getActualPlayer() !== null;
    }

    isPlayer(userId){
        return this.playerBlue?.user?.id === userId || this.playerRed?.user?.id === userId
    }

    isFinalTurn() {
        return this.numTurn >= Board.LIMIT_NUM_TURNS;
    }

    thereIsTwoPlayer() {
        return this.playerBlue !== null && this.playerRed !== null;
    }

    isValid(){
        let isValid = true;
        for (let index = 0; index < Object.keys(this.quantity).length; index++) {
            if(this.getQuantity(index, Board.RED) !== 0 && 
               this.getQuantity(index, Board.RED) === this.getQuantity(index, Board.BLUE)){
                isValid = false;
                break;
            }
        }
        return isValid;
    }

    nextSource() {
        let color = (this.numTurn + 1) % 3 === 0 ? this.getColor() : this.getOtherColor();
        color = this.getColor();
        for (let i = 0; i < Object.keys(this.quantity).length; i++) {
            if (this.getQuantity(i, color) > 0 && this.getQuantity(i, color) < 5)
                return i;
        }
        return 0;
    }

    getTypeTurnByIndex(numTurn = this.numTurn) {
        return numTurn % 9 === 0 ? Board.CONTAMINATION_STAGE :
               numTurn % 3 === 0 ? Board.FISION_STAGE :
               numTurn % 2 === 0 ? Board.MOVEMENT_PLAYER_RED :
               numTurn % 2 === 1 ? Board.MOVEMENT_PLAYER_BLUE :
               null;
    }

    getColor() {
        let type = this.getTypeTurnByIndex();
        return type === Board.MOVEMENT_PLAYER_RED ? Board.RED :
            type === Board.MOVEMENT_PLAYER_BLUE ? Board.BLUE : null;
    }

    getOtherColor(color = this.getColor()) {
        return color === Board.BLUE ? Board.RED :
            color === Board.RED ? Board.BLUE : null;
    }

    countBacteriumByColor(color = this.getColor()) {
        let res = 0;
        for (let i = 0; i < Object.keys(this.quantity).length; i++) {
            if (this.quantity[i][color] !== 5)
                res += this.quantity[i][color];
        }
        return res;
    }

    countSarcinsByColor(color = this.getColor()) {
        let res = 0;
        for (let i = 0; i < Object.keys(this.quantity).length; i++) {
            if (this.quantity[i][color] === 5)
                res++;
        }
        return res;
    }

    countChipsByColor(color = this.getColor()) {
        return this.countBacteriumByColor(color) + this.countSarcinsByColor(color);
    }

    isYourTurn(id) {
        return this.getActualPlayer()?.user?.id === id;
    }

    winnerByContamination() {
        let winner = null;
        let dawn = false;
        const chipsBlue = this.countChipsByColor(Board.BLUE);
        const chipsRed = this.countChipsByColor(Board.RED);
        const sarcinsBlue = this.countSarcinsByColor(Board.BLUE);
        const sarcinsRed = this.countSarcinsByColor(Board.RED);
        const safe = (lvContamination) => lvContamination < Board.LIMIT_CONTAMINATION;
        const danger = (lvContamination) => lvContamination >= Board.LIMIT_CONTAMINATION;
        const dangerBlue = danger(this.lvContBlue), dangerRed = danger(this.lvContRed),
              safeBlue = safe(this.lvContBlue), safeRed = safe(this.lvContRed);
        if (dangerBlue && safeRed) {
            winner = this.playerRed;
        } else if (dangerRed && safeBlue) {
            winner = this.playerBlue;
        } else if (dangerBlue && dangerRed) {
            if (chipsBlue > chipsRed) {
                winner = this.playerRed;
            } else if (chipsBlue < chipsRed) {
                winner = this.playerBlue;
            } else {
                if (sarcinsBlue > sarcinsRed) {
                    winner = this.playerRed;
                } else if (sarcinsBlue < sarcinsRed) {
                    winner = this.playerBlue;
                } else {
                    dawn = true;
                }
            }
        }

        return {winner, dawn};
    }

    winnerByChips(){
        const limitBacteriumRed = this.countBacteriumByColor(Board.RED) > Board.LIMIT_BACTERIUMS;
        const limitBacteriumBlue = this.countBacteriumByColor(Board.BLUE) > Board.LIMIT_BACTERIUMS;
        const limitSarcinsRed = this.countSarcinsByColor(Board.RED) > Board.LIMIT_SARCINS;
        const limitSarcinsBlue = this.countSarcinsByColor(Board.BLUE) > Board.LIMIT_SARCINS;
        const loseRed = limitBacteriumRed || limitSarcinsRed;
        const loseBlue = limitBacteriumBlue || limitSarcinsBlue;
        let winner = null;
        let dawn = false;
        if(loseRed || loseBlue){
            if(!loseBlue){
                winner = this.playerBlue;
            }else if(!loseRed){
                winner = this.playerRed;
            }else{
                dawn = true;
            }
        }
        return {winner,dawn};
    }

    winnerByFinish(){
        let winner = null;
        let dawn = false;
        if(this.lvContBlue > this.lvContRed){
            winner = this.playerRed;
        } else if(this.lvContBlue < this.lvContRed){
            winner = this.playerBlue;
        } else {
            const recalcule = this.winnerByContamination();
            winner = recalcule["winner"];
            dawn = recalcule["dawn"];
        }
        return {winner, dawn};
    }

    cantMove(color = this.getColor()) {
        const possiblesSources = [];
        for (let index = 0; index < Object.keys(this.quantity).length; index++) {
            if(this.getQuantity(index, color) > 0 && this.getQuantity(index, color) < 5){
                possiblesSources.push(index);
            }
        }
        let can = false;
        check:
        for (const source of possiblesSources) {
            const destinations = this.getAdyacent(source);
            for (const destination of destinations) {
                for (let n = 0; n < this.getQuantity(source); n++) {
                    const newQuantitySource = this.getQuantity(source) - n;
                    const newQuantityDestination = this.getQuantity(destination) - n;
                    if (newQuantitySource >= 0 &&
                        newQuantitySource !== this.getQuantity(source, this.getOtherColor() &&
                            newQuantityDestination <= 5 &&
                            newQuantityDestination !== this.getQuantity(destination, this.getOtherColor()))) {
                        can = true;
                        break check;
                    }
                }
            }
        }

        return !can;

    }
}