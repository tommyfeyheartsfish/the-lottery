const usernameInput = document.getElementById('username-input');
const StartGameButton = document.getElementById('start-game-button');
const test = document.getElementById('test');


test.onclick = function() {
  websocket.send("test");
  feedback.textContent='tested';
}
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
    contentChange();
} else {
    // Handle unexpected server response
    console.log("Unexpected server response: " + message_passed_from_the_server);
}
}

function contentChange(){
  const contentChange = document.getElementById('content');
  contentChange.innerHTML=`
  <div id="game-container">
  <button id="quit-button">quit</button>
<button id="check-button">check your score</button>
<button id="rule-button">game rule</button>
<br><br><br><br><br><br>
<div id="status">Waiting for players...</div>
<br><br><br><br><br><br>
<input type="number" id="guess-input" placeholder="Enter your guess(100 -999)" min="100" max="999">
<button id="guess-button">GUESS!</button>
<button id="pass-button">PASS.</button>
<button id="test-button">test.</button>
<div id="feedback">feedback</div>
</div>
`;
setupGuessButton();
}

