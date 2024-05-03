const usernameInput = document.getElementById('username-input');
const StartGameButton = document.getElementById('start-game-button');
const test = document.getElementById('test');

StartGameButton.onclick = function() {
  const username = usernameInput.value;
  websocket.send("checkUsername "+username);
}

function checkUsername(message_passed_from_the_server){
  if (message_passed_from_the_server === "Username is taken.") {
    feedback.textContent ="Username is already taken. Please try again.";
  }else if(message_passed_from_the_server === "username can't have space."){
    feedback.textContent ="Username can't have space.";
} else if (message_passed_from_the_server === "Username OK") {
  feedback.textContent ="player added.";
  document.getElementById('username-password-container').style.display = 'none';
  document.getElementById('game-container').style.display = 'block';
} else {
    // Handle unexpected server response
    console.log("Unexpected server response: " + message_passed_from_the_server);
}
}

