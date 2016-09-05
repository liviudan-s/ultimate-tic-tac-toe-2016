# Ultimate Tic Tac Toe Bot

A bot for the game Ultimate Tic Tac Toe.
Rules can be found at:
http://theaigames.com/competitions/ultimate-tic-tac-toe/rules

Uses minmax algorithm with alpha beta improvement to play ultimate tic tac toe.

I did not use the default framework given in the starter bots at http://theaigames.com/competitions/ultimate-tic-tac-toe/getting-started .
Instead I made my own framework that stores game data as bitboards, instead of just regular matrices of numbers. It does keep one matrix for the board that it uses to convert the data that comes from the engine to the bot, however all the bot calculations are done using the bitboards and is thus more efficient.

Bitboard workings in short:

 The board is stored as 9 microboards, each represented as an int. From that int, only the first 18 bits are used. 2 bits for each of the 9 sqares of a microboards. From those 2 bits the first is 1 if player 1 has placed its piece there(X or O) or the second bit is 1 if player 2 has placed its piece there.
 There are 2 more ints that I use, macroboardEnded and macroboardPlaceNext. On both of those only the first 9 bits are used, a bit for each microboard. In macroboardEnded a bit is 1 if its corresponding microboard has ended. In macroboardPlaceNext a bit is 1 if  its corresponding microboard is available, i.e. the player whose turn is currently can place his piece there.
 
A normal evaluation of a gamestate is done as follows:

Each microboard(i.e. each small, regular, tic tac toe) is evaluated using a common method for evaluating a Tic Tac Toe game(based on how close the player is to getting a complete line horizontally, vertically or diagonally).
Then, the result of those 9 microboard evaluations is used to evaluate the whole board.

To improve speed, I wrote and used the program in the folder microboardevaluation_hashmap_calculator to generate a hashmap with all the possible normal Tic Tac Toe boards and their corresponding scores, so as to not do the calculations for the evaluation of each of the 9 microboards on runtime. This was very convenient as with my framework a microboard is stored as just an int so the hashmap was just an int representing the board, as the key, and an int represeting the score, as the value.
