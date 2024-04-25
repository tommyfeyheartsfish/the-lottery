const serverUrl = 'ws://localhost:8081/src/main/java/com/anwithayi/Server/WebServerMain.java';
const websocket = new WebSocket(serverUrl);
const statusDisplay = document.getElementById('status');
const guessInput = document.getElementById('guess-input');
const guessButton = document.getElementById('guess-button');
const feedback = document.getElementById('feedback');

websocket.onopen = function(event) {
    statusDisplay.textContent = 'Connected to server!';
};

websocket.onmessage = function(event) {
    const data = JSON.parse(event.data);
    switch (data.type) {
        case 'guess responded ':
            displayFeedback(data.NumOfCorrectGuessed);
            break;
    }
};

websocket.onclose = function(event) {
    statusDisplay.textContent = 'Disconnected from server.';
    console.log(`WebSocket closed: code=${event.code}, reason=${event.reason}`);
};

websocket.onerror = function(event) {
    statusDisplay.textContent = 'Error connecting to server.';

};

guessButton.onclick = function() {
    const guess = parseInt(guessInput.value, 10);
    if (!isNaN(guess)) {
        websocket.send(JSON.stringify("guess "+guess));
        guessInput.value = '';  // Clear input after sending
    }
};

function displayFeedback(correctDigits, rank) {
    feedback.textContent = `You guessed {NumOfCorrectGuessed} numbers correctly!`;
}
