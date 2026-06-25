let boardState = "000 000 000";
let isGameOver = false;

document.addEventListener('DOMContentLoaded', () => {
    initGame();
});

function initGame(){
    renderBoard();
}

function renderBoard(){
    const table = document.getElementById('board');
    table.innerHTML = '';

    const boardStateCells = boardState.replace(/ /g, '').split(''); // replace blank spaces with nothing, and split them

    for(let i = 0; i < 3; i++){
        let row = table.insertRow();

        for(let j = 0; j < 3; j++){
            let cell = row.insertCell();
            let index = i * 3 + j;

            cell.innerText = boardStateCells[index] === '0' ? '' : boardStateCells[index];
            cell.style.width = '50px';
            cell.style.height = '50px';

            cell.onclick = () => sendMove(index + 1);
        }
    }
}

async function sendMove(index){
    if (isGameOver) return;

    const response = await fetch('http://localhost:5678/play', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json' 
        },
        body: JSON.stringify(
        {
            "ID": 1, 
            "STATE": boardState, 
            "MOVE": index
        })
    });

    const data = await response.json();
    const messageDiv = document.getElementById('game-message');
    messageDiv.innerText = data.MESSAGE;

    boardState = data.STATE;
    renderBoard();

    if(data.GAME_OVER){
        isGameOver = true;
        messageDiv.style.color = "red";
        console.log("Game over.");
    }
}