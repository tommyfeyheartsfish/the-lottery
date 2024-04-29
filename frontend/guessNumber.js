const guessInput = document.getElementById('guess-input');
const guessButton = document.getElementById('guess-button');


guessButton.onclick = function() {
  //converts a string into a integer
    const guess = parseInt(guessInput.value, 10);
    if (!isNaN(guess)) {
        websocket.send("guess "+guess);
        guessInput.value = '';  // Clear input after sending
    }
};
