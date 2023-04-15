let ws;

// Receive current players
function players(username){
    // calling the WordplayShowdown to retrieve a new word
    let callURL= "http://localhost:8080/WordplayShowdown-1.0-SNAPSHOT/chat-servlet";
    fetch(callURL, {
        method: 'GET',
        headers: {
            'Accept': 'text/plain',
        },
    })
        .then(response => response.json())
        .then(function(response){

            // get player 1 text
            let player = document.getElementById("player-one-name");

            if (response == undefined || Object.keys(response).length === 0) {
                console.log("enter undefined, player 1 not exist");
                // append username to players text
                player.innerHTML += username;
                // set the title attribute value of player
                player.setAttribute("title", player.innerHTML);
            }
            else
            {
                let keys = Object.keys(response);
                let firstPlayer = keys[keys.length - 1];
                let secondPlayer = keys[keys.length - 2];

                let firstPlayerStatus = response[firstPlayer];

                if(firstPlayerStatus == "ready")
                {
                    let status = document.getElementById("player-one-status");
                    status.innerHTML = "Status: ready";
                }


                console.log("enter else, player 1 exist");
                // set player 2
                let player2 = document.getElementById("player-two-name");
                player2.innerHTML = "Player 2: " +  secondPlayer;
                // set the title attribute value of player
                player2.setAttribute("title", player.innerHTML);

                // get first player name

                // append username to players text
                player.innerHTML = "Player 1: " + firstPlayer;

                // set the title attribute value of player
                player.setAttribute("title", player.innerHTML);
            }



        }); //enterRoom(response)// enter the room with the code
}

  

function enterRoom(){

    // get room id value
    let code = document.getElementById("roomID").value;

    // get username value
    let username = document.getElementById("username").value;

    // alert when no room id is given
    if(code == "" || code == null)
    {
        // display an error message to the user
        alert("Please enter a valid room ID.");

        return false;
    }
    // alert when room id is only whitespaces or tabs
    else if(code.trim().length === 0)
    {
        alert("Please enter a room ID contain letters.");

        return false;
    }

    // alert when no username is given
    if(username == "" || username == null)
    {
        // display an error message to the user
        alert("Please enter a user name.");

        return false;
    }
    // alert when username is only whitespaces or tabs
    else if(username.trim().length === 0)
    {
        alert("Please enter a user name contain letters.");

        return false;
    }


    ws = new WebSocket("ws://localhost:8080/WordplayShowdown-1.0-SNAPSHOT/ws/"+code);

    // user connects send username to server
    ws.onopen = function (event) {
        let username = document.getElementById("username").value;
        let request = {"type":"chat", "msg":username};
        ws.send(JSON.stringify(request));

        players(username);

    };

    // parse messages received from the server and update the UI accordingly
    ws.onmessage = function (event) {
        console.log(event.data);
        // parsing the server's message as json
        let message = JSON.parse(event.data);

        if(message.message.includes("joined the game"))
        {
            players(username);
        }

        if(message.message.includes("#ready."))
        {
            let getPlayer = message.message.substring(1, message.message.indexOf(")"))
            let player1 = document.getElementById("player-one-name").innerHTML;
            let player2 = document.getElementById("player-two-name").innerHTML;

            let status = document.getElementById("player-one-status");

            if(getPlayer != player1)
            {
                if(getPlayer == player2.substring(10))
                {
                    status = document.getElementById("player-two-status");
                    status.innerHTML = "Status: ready";
                }
            }

            status.innerHTML = "Status: ready";


            let playerOneStatus = document.getElementById("player-one-status");
            let playerTwoStatus = document.getElementById("player-two-status");

            let notGameStartDiv = document.getElementById("not_gameStart");
            let gameStartDiv = document.getElementById("gameStart");

            if (!playerOneStatus.innerHTML.includes("Not") && !playerTwoStatus.innerHTML.includes("Not")) {
                console.log("game start")
                // if both players are ready, show the game start <div> and hide the not game start <div>
                notGameStartDiv.style.display = "none";
                gameStartDiv.style.display = "block";
            }
        }
        if(message.message.includes("(GameWord): "))
        {
            // get length of an answer msg
            let str = "(GameWord): ";

            // get answer word and definition
            let word = message.message.substring(str.length, message.message.indexOf(","));
            let definition = message.message.substring(str.length + word.length + 1);

            let blanks = document.getElementById("word-blanks");
            let hint = document.getElementById("hint")

            let dashesString = "";
            for (let i = 0; i < word.length; i++) {
                dashesString += "-";
            }
            blanks.innerHTML = dashesString;
            hint.innerHTML = definition;

        }
        else
        {
            document.getElementById("log").value += "[" + timestamp() + "] " + message.message + "\n";
        }

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

// // get player status <p> elements
// let playerOneStatus = document.getElementById("player-one-status");
// let playerTwoStatus = document.getElementById("player-two-status");
//
// // add an event listener that checks if both players ready
// document.addEventListener("DOMContentLoaded", function() {
//     let notGameStartDiv = document.getElementById("not_gameStart");
//     let gameStartDiv = document.getElementById("gameStart");
//
//     if (!playerOneStatus.innerHTML.includes("Not") && !playerTwoStatus.innerHTML.includes("Not")) {
//         console.log("game start")
//         // if both players are ready, show the game start <div> and hide the not game start <div>
//         notGameStartDiv.style.display = "none";
//         gameStartDiv.style.display = "block";
//     }
// });


// event listener when ready button is clicked
let readyButton = document.getElementById("ready-button"); // get the button element
readyButton.addEventListener("click", function() {
    // add event listener for "click" event
    let request = {"type":"chat", "msg":"#ready."};
    ws.send(JSON.stringify(request));
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

// Makes the READY button disappear when it's pressed, and brings in the input box
$('#ready-button').click(function(){
    $('#input').removeClass('d-none');
    $('#ready-button').addClass('d-none');

});