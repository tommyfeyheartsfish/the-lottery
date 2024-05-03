function guess(){
  const guessInput = document.getElementById('guess-input');
  const guessButton = document.getElementById('guess-button');

  websocket.send("guess " + guessInput.value);
  guessInput.value = '';
  }


function guessFeedback(message_passed_from_the_server){
  const feedback_for_the_guess = document.getElementById('feedback-for-the-guess');
  //------testing (check)
  console.log('feedback detected');
  //-----testing
  if (message_passed_from_the_server==="space found")
  {
    //testing-----(check)
    console.log('let me repeat space found');
    feedback_for_the_guess.textContent = 'more than one number detected.';
    //testing----
  }
  else if(message_passed_from_the_server==='number out of range')
  {
    feedback_for_the_guess.textContent="please try again with a 3-digit-number.";
  }
  else if(message_passed_from_the_server === "has guessed")
  {
    feedback_for_the_guess.textContent="You have made the guess in this round already.";
  }
  else if (message_passed_from_the_server === "player not found")
  {
      showModal(message_passed_from_the_server);
      document.getElementById('username-password-container').style.display = 'block';
      document.getElementById('game-container').style.display = 'none';

  }
  else if (message_passed_from_the_server.startsWith("guess recorded")) {
    //testing-----(check)
      console.log('let me repeat:', message_passed_from_the_server);
    //testing----
    //TODO:split the string
      let answer = message_passed_from_the_server[2];
      let NumOfCorrectGuessed = parseInt(message_passed_from_the_server[3]);
      let score = parseInt(message_passed_from_the_server[4]);

      let message=`You guessed ${NumOfCorrectGuessed} numbers correctly!\n
                  The answer is ${answer}.\n
                  You get ${score} points this round!\n\n`;
      showModal(message);
  } else {
      // Handle unexpected server response
      feedback_for_the_guess.textContent="Unexpected server response for guess: " + message_passed_from_the_server;
  }
}

function showModal(message){
  document.getElementById('modalText').textContent = message;
  document.getElementById('Modal-for-guessing-feedback').style.display = 'block';
}

function closeModal(){
  document.getElementById('Modal-for-guessing-feedback').style.display = 'none';
}

