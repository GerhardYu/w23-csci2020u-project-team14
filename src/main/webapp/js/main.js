let ws;

// list of rooms that been created

// Generate existing room when new user enters the page
$(document).ready(function(){
    refreshRooms();
});


// refresh room list
function refreshRooms(){
    let callURL= "http://localhost:8080/WSChatServer-1.0-SNAPSHOT/chat-servlet";
    fetch(callURL, {
        method: 'GET',
        headers: {
            'Accept': 'text/plain',
        },
    })
        .then(response => response.text())
        .then(function(response){
            console.log(response);

            // parse string into array
            let roomArray = response.replace(/\r\n/g, "").split(",");

            // get tbody
            let tbody = document.getElementById("room_list");
            // update tbody innerHTML
            tbody.innerHTML = "";

            // store unique room id
            let tempCheck = [];

            // count room current users
            let dictionary = {};

            // loop through roomArray assign roomID as key, number of roomID occurrence as value
            for (let i = 0; roomArray[i] != 'history'; i++) {
                let key = roomArray[i];
                dictionary[key] = (dictionary[key] || 0) + 1;
            }

            // loop through roomArray assign roomID to tempCheck to prevent duplicate roomID td element
            let indexCount = -1;
            for(let i = 1; roomArray[i] != 'history'; i++) //roomArray[i] != "history"
            {
                // prevent printing duplicate room id when multiple user join
                if(!tempCheck.includes(roomArray[i]))
                {
                    roomList(roomArray[i],dictionary[roomArray[i]]);
                    tempCheck.push(roomArray[i])
                }

                // set index for history roomID
                indexCount = i;
            }

            // generate history rooms: when all users left, room is remain, user can re-enter room
            if(indexCount != -1)
            {
                for(let i = indexCount + 2; i < roomArray.length; i++)
                {
                    if(!tempCheck.includes(roomArray[i]))
                    {
                        roomList(roomArray[i],0);
                    }
                }
            }
            tempCheck = [];
        });
}

// Receive random room ID and establish connect
function newRoom(){
    // calling the ChatServlet to retrieve a new room ID
    let callURL= "http://localhost:8080/WSChatServer-1.0-SNAPSHOT/chat-servlet";
    fetch(callURL, {
        method: 'GET',
        headers: {
            'Accept': 'text/plain',
        },
    })
        .then(response => response.text())
        .then(function(response){
            console.log("enter room: " + response)

            // parse string to list
            let roomArray = response.replace(/\r\n/g, "").split(",");

            // generate room list table
            roomList(roomArray[0], 1);

            // establish connection
            enterRoom(roomArray[0]);

        }); //enterRoom(response)// enter the room with the code
}
function enterRoom(code){

    ws = new WebSocket("ws://localhost:8080/WSChatServer-1.0-SNAPSHOT/ws/"+code);

    // display room title show which room user currently in
    let displayJoinedRoomID = document.getElementById("chat_room_title");
    displayJoinedRoomID.innerHTML = "Room: " + code;

    // parse messages received from the server and update the UI accordingly
    ws.onmessage = function (event) {
        console.log(event.data);
        // parsing the server's message as json
        let message = JSON.parse(event.data);
        document.getElementById("log").value += "[" + timestamp() + "] " + message.message + "\n";
        // handle message

        }


}

//when join create button clicked:clear previous chat log, close previous connection if any
$("#create_room").click(function(){
    document.getElementById("log").value = "";

    // close connection if ws != undefined
    if (ws == undefined) {
        console.log("WebSocket connection is not open");
    } else {
        ws.close();
    }

});

// when join room button clicked: clear previous chat log, close connection before user join a new room
$("#join_room").click(function(){
    document.getElementById("log").value = "";

    // close connection if ws != undefined
    if (ws == undefined) {
        console.log("WebSocket connection is not open");
    } else {
        ws.close();
    }

});

// refresh room table when refresh button is clicked
$("#refresh_room").click(function(){
    refreshRooms();
});



// send msg when user entered a room by hitting keyboard "Enter", and refresh room table
document.getElementById("input").addEventListener("keyup", function (event) {
    if (event.keyCode === 13) {
        let request = {"type":"chat", "msg":event.target.value};
        ws.send(JSON.stringify(request));
        event.target.value = "";

        // refresh room table
        refreshRooms();
    }
});

// display time for messages
function timestamp(){
    var d = new Date(), minutes = d.getMinutes();
    if (minutes < 10) minutes = '0' + minutes;
    return d.getHours() + ':' + minutes;
}

// generate room table
function roomList(room, numOfUser)
{
    var tbody = document.getElementById("room_list");

    // Create a new <tr> element
    var row = document.createElement("tr");
  
    // Create two new <td> elements and set their text content
    var cell1 = document.createElement("td");
    var cell2 = document.createElement("td");
    cell1.textContent = room;
    cell2.textContent = numOfUser;
  
    // Append the new <td> elements to the <tr> element
    row.appendChild(cell1);
    row.appendChild(cell2);
  
    // Append the new <tr> element to the <tbody> element
    tbody.appendChild(row);
}

// when join room button clicked, establish connection with input field room id
$("#join_room").click(function(){

    // get joined room title
    let displayJoinedRoomID = document.getElementById("chat_room_title").innerHTML;

    // get input field room id
    let roomID = document.getElementById("join_room_input").value;

    // display alert if user wants to join the same room that user already in
    if(displayJoinedRoomID == roomID)
    {
        alert("You are currently in room " + roomID + " !");
    }
    // else user join another existing room
    else
    {
        // get existing rooms from tbody
        let tbody = document.querySelector("#room_list");
        let currentRooms = [];
        // loop through the tr elements in the tbody
        tbody.querySelectorAll("tr").forEach((tr) => {
            // get the value of the first td element in the tr
            let currentRoomID = tr.querySelector("td:first-child").textContent;
            currentRooms.push(currentRoomID);
        });

        // join if input field room id is not empty, and roomID exist
        if(roomID.value != "") //currentRooms.includes(roomID)
        {
            // clear previous chat log
            document.getElementById("log").value = "";
            enterRoom(roomID);
        }
        else
        {
            alert("Room " + roomID + " not exist!")
        }
    }
});