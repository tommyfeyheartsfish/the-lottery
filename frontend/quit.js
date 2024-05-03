function quit(){
  websocket.send("quit");
}
function disconnect_from_server(message_passed_from_the_server){
  if(message_passed_from_the_server === "removed"){
    //testing---(!check)
    console.log(message_passed_from_the_server);
    //testing----
    websocket.close();
  }
  //TODO:display good bye message
}
