package bot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Field class
 * 
 * Handles everything. Almost. Just the game related
 * stuff and parsing of matrixes got from eninge.
 * Interaction with the engine is in BotParser.
 * 
 *	Version X codename 'In dev'
 *
 */
public class Field 
{
    public int roundNr;
    public int moveNr;
    public int macroboardPlaceNext;
    public int macroboardEnded;
    public int microboard[];
    public int botId;
    public int enemyId;

    public int getMoveNr() 
    {
        return moveNr;
    }
    
    public Field()
    {
        microboard = new int[9];
        for(int i = 0; i < 9; i++)
        {
            microboard[i] = 0;
        }
    }
    
    /*public Field clone()
    {
        Field newF = new Field();
        newF.roundNr = this.roundNr;
        newF.moveNr = this.moveNr;
        newF.botId = this.botId;
        newF.enemyId = this.enemyId;
        newF.macroboardEnded = this.macroboardEnded;
        newF.macroboardPlaceNext = this.macroboardPlaceNext;
        
        for(int i = 0; i < 9; i++)
        {
            newF.microboard[i] = this.microboard[i];
        }
        
        return newF;
    }*/
    
    public void clearMicroboards()
    {
        for(int i = 0; i < 9; i++)
        {
            microboard[i] = 0;
        }
    }
    
    public Field(Field old, Move m, int player, HashMap<Integer, Integer> hashmap)
    {
        this.microboard = new int[9];
        this.moveNr = old.moveNr + 1;
        this.roundNr = old.roundNr;
        if(player == 1)
        {
            this.botId = 1;
            this.enemyId = 2;
        }
        else
        {
            this.botId = 2;
            this.enemyId = 1;
        }
        
        for(int i = 0; i < 9; i++)
        {
            this.microboard[i] = old.microboard[i];
        }
        
        this.placeMove(m, botId);
        /*if(hashmap.get(microboard[m.getX()]) == null)
            System.err.println(microboard[m.getX()]);*/
        int value = hashmap.get(microboard[m.getX()]);
        if(value == -1000 || value == 100 || value == -100)
        {
            this.macroboardEnded = old.macroboardEnded | (1 << m.getX());
        }
        else
        {
            this.macroboardEnded = old.macroboardEnded;
        }
        
        if((macroboardEnded & (1 << m.getY())) == 0)
        {
            this.macroboardPlaceNext = 1 << m.getY();
        }
        else
        {
            this.macroboardPlaceNext = ~ this.macroboardEnded;
        }
    }

    public int getMacroboardEnded() 
    {
        return macroboardEnded;
    }

    public void setMacroboardEnded(int macroboardEnded) 
    {
        this.macroboardEnded = macroboardEnded;
    }
    
    public void setBotId(int botId)
    {
        this.botId = botId;
        this.enemyId = botId == 1 ? 2 : 1;
    }

    public int getBotId() 
    {
        return botId;
    }

    public int getEnemyId() {
        return enemyId;
    }

    public void setRoundNr(int roundNr) 
    {
        this.roundNr = roundNr;
    }

    public void setMoveNr(int moveNr) 
    {
        this.moveNr = moveNr;
    }

    public int getMicroboard(int i) 
    {
        return microboard[i];
    }
    
    public static Move transformIndex(int x, int y)
    {
        return new Move(x / 3 * 3 + y / 3, x % 3 * 3 + y % 3);
    }
    
    public boolean hasEnded()
    {
        return this.macroboardEnded == 511;
    }
    
    public void switchPlayers()
    {
        int temp = this.botId;
        this.botId = this.enemyId;
        this.enemyId = temp;
    }
        
    public void placeMove(Move m, int value)
    {
        if(m.getX() == -1)
        {
            return;
        }
        
        int place = m.getY() * 2;
        
        if(value == 0)
        {
            return;
        }
        
        if(value == 2)
        {
            place++;
        }
        
        microboard[m.getX()] |= 1 << place;
    }
    
    public int getMove(int x, int y)
    {
        int place = 2 * y;
        if((microboard[x] & (1 << place)) != 0)
        {
            return 1;
        }
        
        if((microboard[x] & (1 << (place + 1))) != 0)
        {
            return 2;
        }
        
        return 0;
    }
            
    public ArrayList<Move> getAvailableMoves()
    {
        ArrayList<Move> moves = new ArrayList<>();
        
        for(int i = 0; i < 9; i++)
        {
            if((macroboardPlaceNext & (1 << i)) != 0)
            {
                for(int j = 0; j < 9; j++)
                {
                    if(getMove(i, j) == 0)
                    {
                        moves.add(new Move(i, j));
                    }
                }
            }
        }
        
        return moves;
    }
    
    /**
     * Initialise macroboard from comma separated String
     * @param String : 
     */
    public void parseMacroboardFromString(String s) 
    {
        String[] r = s.split(",");
        int macroboard = 0;
        for (int i = 0; i < 9; i++) 
        {
            int result = Integer.parseInt(r[i]);
            if(result == -1)
            {
                macroboard |= 1 << i; 
            }
        }
        this.macroboardPlaceNext = macroboard;
    }
    
}
