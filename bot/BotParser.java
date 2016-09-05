package bot;

import java.util.HashMap;
import java.util.Scanner;

/**
 * BotParser class
 * 
 * Main class that will keep reading output from the engine.
 * Will either update the bot state or get actions.
 * 
 *	Version X codename 'In dev'
 *
 * @author ItworksWhy Team
 */

public class BotParser 
{
    final Scanner scan;
    
    private BotStarter bot;
    private Field field;
    private int[][] board;
    private int botId;

    public BotParser(BotStarter bot) 
    {
        this.scan = new Scanner(System.in);
        this.bot = bot;
        this.board = new int[9][9];

        for(int i = 0; i < 9; i++)
        {
            for(int j = 0; j < 9; j++)
            {
                board[i][j] = 0;
            }
        }
    }
    
    public Field getField() 
    {
        return field;
    }

    public BotStarter getBot() {
        return bot;
    }

    public void run() 
    {
        field = new Field();
        while(scan.hasNextLine()) 
        {
            String line = scan.nextLine();

            if(line.length() == 0) 
            {
                continue;
            }

            String[] parts = line.split(" ");
            if(parts[0].equals("settings")) 
            {
                if (parts[1].equals("your_botid")) 
                {
                    botId = Integer.parseInt(parts[2]);
                    field.setBotId(botId);
                    BotStarter.setBotId(botId);
                    if(botId == 1)
                    {
                        BotStarter.setEnemyId(2);
                    }
                    else
                    {
                        BotStarter.setEnemyId(1);
                    }
                }
            }
            else if(parts[0].equals("update") && parts[1].equals("game")) 
            { /* new game data */
                if (parts[2].equals("round"))
                {
                    field.setRoundNr(Integer.parseInt(parts[3]));
                }
                else if (parts[2].equals("move"))
                {
                    field.setMoveNr(Integer.parseInt(parts[3]));
                }
                else if(parts[2].equals("field"))
                {
                    field.placeMove(this.getMovePlaced(parts[3]), BotStarter.getEnemyId());
                }
                else if(parts[2].equals("macroboard"))
                {
                    field.parseMacroboardFromString(parts[3]);
                }
            } 
                else if(parts[0].equals("action")) 
                {
                    if (parts[1].equals("move")) 
                    { /* move requested */
                        System.err.println("Move " + field.getMoveNr());
                        Move move = this.bot.makeTurn(field);
                        board[move.getX()][move.getY()] = field.getBotId();
                        System.out.println("place_move " + move.getY() + " " + move.getX());
                    }
                } 
                    else 
                    { 
                        System.out.println("unknown command");
                    }
        }
    }
    
    public Move getMovePlaced(String s) 
    {
        int debug = 0;
        if(debug == 1)
        {
            field.clearMicroboards();
        }
        s = s.replace(";", ",");
        String[] r = s.split(",");
        int counter = 0;
        //int counter2 = 0;
        //int changeX = -1, changeY = -1;
        if(field.getMoveNr() == 1)
        {
            return new Move(-1, -1);
        }
        for (int x = 0; x < 9; x++) 
        {
            for (int y = 0; y < 9; y++) 
            {
                int piece = Integer.parseInt(r[counter]);
                if(debug == 1)
                {
                    board[x][y] = piece;
                    field.placeMove(Field.transformIndex(x, y), piece);
                }
                else if(board[x][y] != piece)
                {
                    board[x][y] = piece;
                    //counter2++;
                    //changeX = x; changeY = y;
                    //System.out.println(x + " " + y);
                    return Field.transformIndex(x, y);
                }
                counter++;
            }
        }
        
        /*if(debug == 0)
        {
            //System.err.println("Discrepancies: " + counter2);
            if(field.moveNr == 1)
            {
                return new Move(-1, -1);
            }
            if(counter2 > 1)
            {
                int q = 1;
            }
            return Field.transformIndex(changeX, changeY);
        }*/

        if(debug == 1)
        {
            for(int i = 0; i < 9; i++)
            {
                int microboard = field.getMicroboard(i);
                int value = bot.getMicroboard_evaluations_hashmap().get(microboard);
                if(value == -1000 || value == 100 || value == -100)
                {
                    field.macroboardEnded |= 1 << i;
                }
            }
        }
        
        return new Move(-1, -1);
    }
}