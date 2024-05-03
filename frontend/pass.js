function pass(){
  websocket.send("pass");
}

function passFeedback(message_passed_from_the_server){
  console.log('let me repeat:', message_passed_from_the_server);
  if (message_passed_from_the_server==="waiting for other players")
  {
    //TODO: next slide: display waiting for other players
  }
}
