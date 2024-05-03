function checkScore(){
    websocket.send("score");
}

function scoreFeedback(message_passed_from_the_server){
  //testing -----(check)
  console.log('let me repeat:', message_passed_from_the_server);
  //testing----
  showModal(message_passed_from_the_server);
}
