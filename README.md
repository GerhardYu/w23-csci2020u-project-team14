## Wordplay Showdown

# Group Members: James Yang, Thanushan Satheeskumar, Gerhard Yu

This project is about a game which is similar to hangman in which nmultiple players try to find the hidden word that is generated with only the definition of the word being the hint. The project uses websockets to ensure that there are multiple players that can play the game which in this case being a local connection. When a player wants to start a game or join a room, first they must press the "Play Game" in the home screen aftering pressing it, players will then be redirected to another page that will ask for the room number that they want to join and username of the player. After filling all of those necessary information, the player will then be redirected to the game page in which there is a ready button present. The ready button is to ensure that the players present in the room are ready to start the game, the moment they press it. Once all the players present in the room are ready, a word will then be randomly generated, this word will have its definition shown to all the players as a hint. The goal of the game is to guess which word is it based from the hint, players are allowed to keep on guessing until one of the players has managed to successfully guess the word. Once the word is guessed, there will be a message shown who got the word right and after that a new random word will then be generated for continuous gameplay.


Steps on on how to run the program
1. Clone the repository into your computer using git clone.
2. Once cloned, edit the configurations to allow running the API locally.
3. Once ran locally, open the index.html file.
4. Once index.html is opened player can then proceed to press "Play Now" to proceed to the next page.
5. Once redirected to the next page, enter room number and username.
6. Once room number and username are entered, player will then be redirected to game page where there is a ready button that can be pressed to show that player is ready.
7. Once all players ready, game will start.

The design of the webpage was mostly done by using bootstrap.
