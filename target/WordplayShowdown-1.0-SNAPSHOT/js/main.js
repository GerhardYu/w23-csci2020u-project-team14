let ws;

// Receive random word for each game
// function newRoom(){
//     // calling the WordplayShowdown to retrieve a new word
//     let callURL= "http://localhost:8080/WordplayShowdown-1.0-SNAPSHOT/chat-servlet";
//     fetch(callURL, {
//         method: 'GET',
//         headers: {
//             'Accept': 'text/plain',
//         },
//     })
//         .then(response => response.text())
//         .then(function(response){
//             console.log("new word: " + response)
//
//             // parse string to list
//             let roomArray = response.replace(/\r\n/g, "").split(",");
//
//
//
//         }); //enterRoom(response)// enter the room with the code
// }

  

function enterRoom(){

    let code = document.getElementById("roomID").value;

    if(code == "" || code == null)
    {
        // display an error message to the user
        alert("Please enter a valid room ID.");

        return false;
    }


    ws = new WebSocket("ws://localhost:8080/WordplayShowdown-1.0-SNAPSHOT/ws/"+code);

    console.log("here");

    // parse messages received from the server and update the UI accordingly
    ws.onmessage = function (event) {
        console.log(event.data);
        // parsing the server's message as json
        let message = JSON.parse(event.data);
        document.getElementById("log").value += "[" + timestamp() + "] " + message.message + "\n";
        // handle message

        }

    return true;
}


// send msg when user entered a room by hitting keyboard "Enter", and refresh room table
document.getElementById("input").addEventListener("keyup", function (event) {
    if (event.keyCode === 13) {
        let request = {"type":"chat", "msg":event.target.value};
        ws.send(JSON.stringify(request));
        event.target.value = "";

    }
});

// display time for messages
function timestamp(){
    var d = new Date(), minutes = d.getMinutes();
    if (minutes < 10) minutes = '0' + minutes;
    return d.getHours() + ':' + minutes;
}

$("#enterRequestButton").click(function(){
    let request = document.getElementById("enterRequest");
    request.style.display = "none";

    let room = document.getElementById("room");
    room.style.display = "block";

    let footer = document.getElementById("footer");
    footer.style.display = "block";

});