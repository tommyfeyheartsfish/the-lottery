function setupGuessButton() {
  const guessInput = document.getElementById('guess-input');
  const guessButton = document.getElementById('guess-button');
  const feedback = document.getElementById('feedback');

  guessButton.onclick = function() {
      const guess = parseInt(guessInput.value, 10);
      if (!isNaN(guess)) {
          websocket.send("guess " + guess);
          guessInput.value = '';  // Clear input after sending
      }
  };
}

function guessFeedback(message_passed_from_the_server){
  if (message_passed_from_the_server === "space found")
  {
    feedback.textContent="more than one number detected.";
  }
  else if(message_passed_from_the_server[0] === "number" && message_passed_from_the_server[1] === "out")
  {
    feedback.textContent="please try again with a 3-digit-number.";
  }
  else if(message_passed_from_the_server === "has guessed")
  {
    feedback.textContent="You have made the guessed in this round already.";
  }
  else if (message_passed_from_the_server === "player not found")
  {
      feedback.textContent="player not found";
      //TODO:redirect to the login page
  }
  else if (message_passed_from_the_server[0] === "guess" && message_passed_from_the_server[1] === "recorded") {
      let answer = message_passed_from_the_server[2];
      let NumOfCorrectGuessed = parseInt(message_passed_from_the_server[3]);
      let score = parseInt(message_passed_from_the_server[4]);

      feedback.textContent=`You guessed ${NumOfCorrectGuessed} numbers correctly!
                  The answer is ${answer}.
                  You get ${score} points this round!\n\n`;
  } else {
      // Handle unexpected server response
      feedback.textContent="Unexpected server response for guess: " + message_passed_from_the_server;
  }
}
