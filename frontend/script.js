const serverUrl = 'ws://localhost:8081/src/main/java/com/anwithayi/Server/WebServerMain.java';
const websocket = new WebSocket(serverUrl);
const statusDisplay = document.getElementById('status');
const feedback = document.getElementById('feedback');


websocket.onopen = function(event) {
    statusDisplay.textContent = 'Connected to server!';
};

websocket.onmessage = function(event) {
    const parts = event.data.split(":");
    const type_of_event = parts[0];
    const message_to_pass_on = parts.slice(1).join(':');
    switch (type_of_event){
        case "username":
          checkUsername(message_to_pass_on);
          break;
        case "guess":
          //-----testing(check)
          console.log('Message for feedback:', event.data);
          //----testing
          guessFeedback(message_to_pass_on);
          break;
        case "score":
          //-----testing (check)
          console.log('Message for feedback:', event.data);
          //----testing
          scoreFeedback(message_to_pass_on);
        case "quit":
          //------testing(check)
          console.log('Message for quiting:', event.data);
          //-------testing
          disconnect_from_server(message_to_pass_on);
          break;
        case "pass":
          //------testing(check)
          console.log('Message for feedback:', event.data);
          //-------testing
          passFeedback(message_to_pass_on);
          break;
        default:
          console.log('Message from server:', event.data);
          displayMessage(event.data);
    }
};

websocket.onclose = function(event) {
    statusDisplay.textContent = 'Disconnected from server.';
    console.log(`WebSocket closed: code=${event.code}, reason=${event.reason}`);
};

websocket.onerror = function(event) {
    statusDisplay.textContent = 'Error connecting to server.';

};


function displayFeedback(correctDigits, rank) {
    feedback.textContent = `You guessed {NumOfCorrectGuessed} numbers correctly!`;
}

function displayMessage(message) {
    feedback.textContent = message;
}
